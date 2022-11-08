package com.live.fox.ui.live;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mmin18.widget.RealtimeBlurView;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.CommonApp;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.entity.Anchor;
import com.live.fox.receiver.TXPhoneStateListener;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.PlayerUtils;
import com.live.fox.utils.TCConstants;
import com.live.fox.utils.ToastUtils;
import com.live.fox.windowmanager.WindowUtils;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;


/**
 * Date: 2016/3/27
 * Time: 19:40
 */
public class VideoFragment extends Fragment implements ITXLivePlayListener, View.OnClickListener {

    private ImageView iv_bg;
    private TXCloudVideoView mTXCloudVideoView;
    private ImageView mLoadingView;            //正在加载
    private ImageView coverIv;                 //视频封面
    private RealtimeBlurView blurView;         //视频封面
    private RelativeLayout videoFrameLayout;
    private ImageView ivCloseWindow;
    private TextView tv_window;

    private Anchor anchor;
    private TXLivePlayConfig mTXPlayConfig;

    private static String roomPaid;
    private static String roomPassword;


    public static VideoFragment newInstance(Anchor anchor) {
        VideoFragment fragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("anchor", anchor);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (getArguments() != null) {
            this.anchor = (Anchor) bundle.getSerializable("anchor");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        initView(view);
        if (anchor != null) {
            GlideUtils.loadImage(requireActivity(), anchor.getAvatar(),
                    R.drawable.liveing_star,
                    R.drawable.liveing_star, coverIv);
            try {
                initPlayer();
            } catch (Exception e) {
                ToastUtils.showShort(getString(R.string.playerInitialException));
            }

            //电话状态监听
            CommonApp.mPhoneListener = new TXPhoneStateListener(Constant.mTXLivePlayer);
            TelephonyManager tm = (TelephonyManager)
                    requireActivity().getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(CommonApp.mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
        return view;
    }



    private void initView(View bindSource) {
        iv_bg = bindSource.findViewById(R.id.iv_bg);
        mTXCloudVideoView = bindSource.findViewById(R.id.live_play_video_view);
        mLoadingView = bindSource.findViewById(R.id.loading);
        coverIv = bindSource.findViewById(R.id.iv_conver);
        blurView = bindSource.findViewById(R.id.blur);
        videoFrameLayout = bindSource.findViewById(R.id.frame_video_layout_push);
        ivCloseWindow = bindSource.findViewById(R.id.iv_close_window);
        tv_window = bindSource.findViewById(R.id.tv_window);
        bindSource.findViewById(R.id.iv_close_window).setOnClickListener(this);
        bindSource.findViewById(R.id.live_play_video_view).setOnClickListener(this);
        bindSource.findViewById(R.id.iv_conver).setOnClickListener(this);

        roomPaid = getString(R.string.live_change_to_paid);
        roomPassword = getString(R.string.live_change_to_password);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //打开fragment后加载video 不是1和3状态
        if(anchor!=null && anchor.getLiveStatus()!=1 && anchor.getLiveStatus()!=3)
        {
            startPlay(anchor);
        }
    }

    private void initPlayer() {
        if (Constant.mTXLivePlayer == null) {
            Constant.mTXLivePlayer = new TXLivePlayer(requireContext());
        }

        mTXPlayConfig = new TXLivePlayConfig();
        setPlayMode(2);
    }


    //设置播放器模式
    public void setPlayMode(int strategy) {
        if (mTXPlayConfig == null) {
            return;
        }
        switch (strategy) {
            case 0: // 極速
                mTXPlayConfig.setAutoAdjustCacheTime(true);
                mTXPlayConfig.setMinAutoAdjustCacheTime(1);
                mTXPlayConfig.setMaxAutoAdjustCacheTime(1);
                Constant.mTXLivePlayer.setConfig(mTXPlayConfig);
                break;
            case 1: // 流暢
                mTXPlayConfig.setAutoAdjustCacheTime(false);
                mTXPlayConfig.setMinAutoAdjustCacheTime(5);
                mTXPlayConfig.setMaxAutoAdjustCacheTime(5);
                Constant.mTXLivePlayer.setConfig(mTXPlayConfig);
                break;

            case 2: // 自動
                mTXPlayConfig.setAutoAdjustCacheTime(true);
                mTXPlayConfig.setMinAutoAdjustCacheTime(1);
                mTXPlayConfig.setMaxAutoAdjustCacheTime(5);
                Constant.mTXLivePlayer.setConfig(mTXPlayConfig);
                break;
        }
    }

    /**
     * 切换房间时 根据状态显示 不一样的界面
     *
     * @param state
     * @param anchor
     */
    public void switchRoomByState(int state, Anchor anchor) {
        LogUtils.e("切换房间时 根据状态显示 不一样的界面" + state);
        this.anchor = anchor;
        if (!isAdded()) return;
        if (state == 1) {//切换后请求接口时的处理
            if (Constant.mTXLivePlayer != null && Constant.mTXLivePlayer.isPlaying()) {
                Constant.mTXLivePlayer.stopPlay(true);
            }
            if (coverIv != null) {
                coverIv.setVisibility(View.VISIBLE);
                blurView.setVisibility(View.VISIBLE);
                GlideUtils.loadDefaultImage(getActivity(), anchor.getAvatar(), coverIv);
            }
            showLiveLoadingAnimation();
        }
    }

    public void startPlay(Anchor anchor) {
        this.anchor = anchor;
        if (!isAdded()) return;
        playRTMP(anchor.getPullStreamUrl());
    }


    private void fromWindow() {
        if (Constant.isOpenWindow) {
            AppIMManager.ins().removeMessageReceivedListener(PlayLiveActivity.class);
            Constant.isOpenWindow = false;
            PlayLiveActivity.startActivity(ActivityUtils.getTopActivity(), Constant.windowAnchor);
        }
    }

    public void changeWindowText(Anchor currentAnchor) {

        anchor = currentAnchor;

        if (3 == anchor.getType()) {
            tv_window.setVisibility(View.VISIBLE);
            tv_window.setText(roomPaid);
            coverIv.setVisibility(View.VISIBLE);
            GlideUtils.loadImage(CommonApp.getInstance(), currentAnchor.getAvatar(),
                    R.drawable.liveing_star, R.drawable.liveing_star, coverIv);
            stopPlay(true);
        } else if (0 == anchor.getType()) {
            tv_window.setVisibility(View.GONE);
            coverIv.setVisibility(View.GONE);
            playRTMP(anchor.getPullStreamUrl());
        } else {
            tv_window.setVisibility(View.VISIBLE);
            tv_window.setText(roomPassword);
            coverIv.setVisibility(View.VISIBLE);
            GlideUtils.loadImage(CommonApp.getInstance(), currentAnchor.getAvatar(),
                    R.drawable.liveing_star, R.drawable.liveing_star, coverIv);
            stopPlay(true);
        }
    }

    /**
     * 添加到Window
     */
    public void onClickAddToWindow() {
        if (Constant.isShowWindow) {
            ivCloseWindow.setVisibility(View.VISIBLE);
            Constant.isOpenWindow = true;//是否开启小窗口
        }
    }

    /**
     * 从Window移除
     */
    public void onClickRemoveFromWindow() {
        Constant.isShowWindow = false;
        Constant.isOpenWindow = false;
        if (Constant.windowAnchor != null) {
            AppIMManager.ins().loginOutGroup(String.valueOf(Constant.windowAnchor.getLiveId()));
        }
        WindowUtils.closeWindowResource(PlayLiveActivity.class);
    }

    //停止播放并清空播放事件
    public void clearStop() {
        LogUtils.e("clearStop");
        if (Constant.mTXLivePlayer != null) {
            Constant.mTXLivePlayer.stopPlay(false);
            Constant.mTXLivePlayer.setPlayerView(null);
            Constant.mTXLivePlayer.setPlayListener(null);
        }
    }

    public void changePKVideo(boolean isPK) {
        LogUtils.e("isPK==" + isPK);
        Constant.isPK = isPK;
        mTXPlayConfig.enableAEC(isPK);
        Constant.mTXLivePlayer.setConfig(mTXPlayConfig);
        if (!Constant.isOpenWindow) {
            adjustFullScreenVideoView(!isPK);
        }
    }

    public void adjustFullScreenVideoView(boolean fullScreen) {
        if (videoFrameLayout.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) videoFrameLayout.getLayoutParams();
            if (fullScreen) {
                LogUtils.e("全屏");
                layoutParams.removeRule(RelativeLayout.BELOW);
                layoutParams.removeRule(RelativeLayout.ABOVE);
                iv_bg.setVisibility(View.GONE);
            } else {
                LogUtils.e("PK 小屏");
                layoutParams.addRule(RelativeLayout.BELOW, R.id.frame_layout_pross);
                layoutParams.addRule(RelativeLayout.ABOVE, R.id.view_center);
                iv_bg.setVisibility(View.VISIBLE);
            }
            videoFrameLayout.setLayoutParams(layoutParams);
        }
    }


    private void playRTMP(String streamUrl) {
        LogUtils.e("streamUrl " + streamUrl);
        int mUrlPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
        if (0 == anchor.getLiveStatus()) {
            streamUrl = anchor.getAdJumpUrl();
            mUrlPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
        } else if (1 == anchor.getLiveStatus() || 3 == anchor.getLiveStatus()) {
            if (!PlayerUtils.checkPlayUrl(streamUrl, requireContext())) {
                ToastUtils.showShort(getString(R.string.illegalStreaming));
                return;
            }
            if (Constant.mTXLivePlayer == null) {
                LogUtils.e("Constant.mTXLivePlayer为空");
                return;
            }
            if ((streamUrl.startsWith(PlayerUtils.HTTP) ||
                    streamUrl.startsWith(PlayerUtils.HTTPS)) && streamUrl.contains(PlayerUtils.FLV)) {
                mUrlPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
            }
        }

        Constant.mTXLivePlayer.setPlayerView(mTXCloudVideoView);

        Constant.mTXLivePlayer.setPlayListener(this);
        // 硬件加速在1080p解码场景下效果显著，但细节之处并不如想象的那么美好：
        // (1) 只有 4.3 以上android系统才支持
        // (2) 兼容性我们目前还仅过了小米华为等常见机型，故这里的返回值您先不要太当真
        Constant.mTXLivePlayer.enableHardwareDecode(false);
        Constant.mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        Constant.mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        Constant.mTXLivePlayer.setConfig(mTXPlayConfig);

        LogUtils.e("直播流 " + streamUrl + "，" + mUrlPlayType);
        int result = Constant.mTXLivePlayer.startPlay(streamUrl, mUrlPlayType);
        Constant.isShowWindow = true;
        LogUtils.e("拉流返回值 result " + result);
        if (0 != result) {
            Intent rstData = new Intent();
            if (-1 != result) {
                ToastUtils.showShort(R.string.splbfsb);
            }
            rstData.putExtra(TCConstants.ACTIVITY_RESULT, getString(R.string.linkRestrictions));
            if (mTXCloudVideoView != null) {
                mTXCloudVideoView.onPause();
            }
        }
        showLiveLoadingAnimation();
    }

    private void showLiveLoadingAnimation() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
            ((AnimationDrawable) mLoadingView.getDrawable()).start();
        }
    }

    public void dismissLiveLoadingAnimation() {
        if (!isAdded()) return;
        if (coverIv != null) {
            LogUtils.e("dimissLiveLoadingAnimation()");
            coverIv.setVisibility(View.GONE);
            blurView.setVisibility(View.GONE);
        }
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
            ((AnimationDrawable) mLoadingView.getDrawable()).stop();
        }
    }

    /**
     * 播放事件
     * 正常流程：
     * 2007, 视频缓冲中...
     * 2001, 已连接服务器
     * 2002, 开始拉流
     * 2004, 视频播放开始
     * 2008, 启动软解
     * (如果网络慢,到下一步会有一段时间)
     * 2003, 渲染首个视频数据包(IDR)
     * 2009, 分辨率改变为368x640
     * <p>
     * 出现卡顿流程:
     * 2007, 视频缓冲中...
     * 2004, 视频播放开始
     * 2105, 当前视频播放出现卡顿1816ms
     */
    @Override
    public void onPlayEvent(int event, Bundle param) {
        LogUtils.e("视频播放状态监听 " + event + ", " + param.getString(TXLiveConstants.EVT_DESCRIPTION));

        if (event == TXLiveConstants.PLAY_EVT_CONNECT_SUCC) {
            // 2001 連接服務器成功
        } else if (event == TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN) {
            // 2002 已經連接服務器，開始拉流（僅播放RTMP地址時會抛送）
//            dimissLiveLoadingAnimation();
//            content.setBackground(null);
        } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
            // 2003 網絡接收到首個可渲染的視頻數據包(IDR)
            dismissLiveLoadingAnimation();
            LogUtils.e(Constant.mTXLivePlayer.isPlaying() + ",");
            LogUtils.e((Constant.mTXLivePlayer == null) + ",");
            LogUtils.e((mTXCloudVideoView == null) + ",");
            listener.onPlayIsFinish(true);
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            // 2004 視頻播放開始，如果有轉菊花什麽的這個時候該停了
            if (coverIv.getVisibility() == View.VISIBLE) {
                //说明是第一次加载 则不做任何处理
            } else {
                // 卡顿后的流恢复
                dismissLiveLoadingAnimation();
            }

        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
            // 2007 視頻播放loading，如果能夠恢複，之後會有BEGIN事件
            showLiveLoadingAnimation();
        } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
            //2009 分辨率改变
//            Constant.mTXLivePlayer.stopRecord();
//            Constant.pkHeight = param.getInt(TXLiveConstants.EVT_PARAM2);

        } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
        }

        /**
         *  結束事件
         */
        if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            // 2006 視頻播放結束

        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
            //  -2301 网络多次重连失败失败后 会返回此值
            clearStop();
            if (isAdded()) {
                networkDisconnect();
            }
        }
    }


    /**
     * 网络连接提示
     */
    public void networkDisconnect() {
        if (requireActivity().isFinishing()) return;
        DialogFactory.showTwoBtnDialog(requireActivity(), getString(R.string.networkDelay),
                getString(R.string.exit), getString(R.string.retryConnect), (button, dialog) -> {
                    dialog.dismiss();
                    Constant.mTXLivePlayer.stopPlay(true);
                    ((PlayLiveActivity) requireActivity())
                            .closeRoomAndStopPlay(false, true, true);
                }, (button, dialog) -> {
                    dialog.dismiss();
                    playRTMP(anchor.getPullStreamUrl());
                });
    }

    public void showCover() {
        if (coverIv != null) {
            coverIv.setVisibility(View.VISIBLE);
            blurView.setVisibility(View.VISIBLE);
        }
    }

    // 網絡連接狀態
    @Override
    public void onNetStatus(Bundle param) {
//       LogUtils.e("播放實時數據:"+param);
    }

    //停止播放 是否留最后一帧
    public void stopPlay(boolean isKeepLastFrame) {
        LogUtils.e("stopPlay ");
        if (Constant.mTXLivePlayer != null && Constant.mTXLivePlayer.isPlaying()) {
            try {
                Constant.mTXLivePlayer.stopPlay(isKeepLastFrame); // true 代表清除最后一帧画面
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public boolean isPlay() {
        if (Constant.mTXLivePlayer != null) {
            return Constant.mTXLivePlayer.isPlaying();
        } else {
            return false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mTXCloudVideoView.onResume();
        if (Constant.isOpenWindow) {
            ivCloseWindow.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mTXCloudVideoView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private OnVideoPlayStateListener listener;

    public void setOnVideoPlayStateListener(OnVideoPlayStateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.iv_close_window:
                onClickRemoveFromWindow();
                break;
            case R.id.live_play_video_view:
            case R.id.iv_conver:
                fromWindow();
                break;
        }
    }


    public interface OnVideoPlayStateListener {
        void onPlayIsFinish(boolean isFinish);
    }

}
