package com.live.fox.ui.living;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.live.fox.AppIMManager;
import com.live.fox.ConstantValue;
import com.live.fox.MessageProtocol;
import com.live.fox.R;
import com.live.fox.adapter.LivingMsgBoxAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentLivingBinding;
import com.live.fox.db.LocalGiftDao;
import com.live.fox.db.LocalMountResourceDao;
import com.live.fox.dialog.bottomDialog.LivingProfileBottomDialog;
import com.live.fox.entity.EnterRoomBean;
import com.live.fox.entity.GiftResourceBean;
import com.live.fox.entity.LivingCurrentAnchorBean;
import com.live.fox.entity.LivingFollowMessage;
import com.live.fox.entity.LivingEnterLivingRoomBean;
import com.live.fox.entity.LivingMessageGiftBean;
import com.live.fox.entity.LivingMsgBoxBean;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.entity.PersonalLivingMessageBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.entity.SvgAnimateLivingBean;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.BulletViewUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.PlayerUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.LivingClickTextSpan;
import com.live.fox.view.bulletMessage.BulletMessageView;
import com.live.fox.view.bulletMessage.EnterRoomMessageView;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.OVER_SCROLL_NEVER;

public class LivingFragment extends BaseBindingFragment {

    final int playSVGA = 123;
    final int userHeartBeat=987;
    final int alertWhenExit=87272;

    int currentPagePosition;
    int viewPagePosition;
    FragmentLivingBinding mBind;
    LivingControlPanel livingControlPanel;
    LivingMsgBoxAdapter livingMsgBoxAdapter;
    List<LivingMsgBoxBean> livingMsgBoxBeans = new ArrayList<>();
    List<SvgAnimateLivingBean> livingMessageGiftBeans = new LinkedList<>();//播放SVGA的数组
    TXLivePlayer mLivePlayer = null;
    private TXLivePlayConfig mTXPlayConfig;
    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case playSVGA:
                    playSVGAAnimal();
                    break;
                case userHeartBeat:
                    Api_Live.ins().watchHeart();
                    sendEmptyMessageDelayed(userHeartBeat,40000);
                    break;
                case alertWhenExit:
                    if(livingControlPanel!=null && livingCurrentAnchorBean!=null && !livingCurrentAnchorBean.getFollow())
                    {
                        livingControlPanel.shouldAlertOnExit=true;
                    }
                    break;
            }
        }
    };

    LivingCurrentAnchorBean livingCurrentAnchorBean;//当前主播的数据

    public static LivingFragment getInstance(int position, int viewPagePosition) {
        Log.e("LivingFragment", position + " ");
        LivingFragment livingFragment = new LivingFragment();
        livingFragment.currentPagePosition = position;
        livingFragment.viewPagePosition = viewPagePosition;
        return livingFragment;
    }

    public void notifyShow(int position, int viewPagePosition) {
        Log.e("LivingFragment22", position + " ");
        currentPagePosition = position;
        this.viewPagePosition = viewPagePosition;
        if (getView() != null && isAdded()) {
            loadData();
        }
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public void onKeyBack() {
        super.onKeyBack();

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_living;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        LivingActivity activity = (LivingActivity) getActivity();
        //是当前页才加载数据 不然就算了
        if (activity.getCurrentPosition() == currentPagePosition && activity.getPagerPosition() == viewPagePosition) {
            loadData();
//            TimeCounter.getInstance().add(timeListener);
        } else {
            GlideUtils.loadDefaultImage(activity, activity.getRoomListBeans().get(currentPagePosition).getRoomIcon(), R.mipmap.icon_anchor_loading,
                    mBind.ivBG);
        }
    }

    private void loadData() {
        LivingActivity activity = (LivingActivity) getActivity();
        if (activity.isFinishing() || activity.isDestroyed()) {
            return;
        }

        GlideUtils.loadDefaultImage(activity, activity.getRoomListBeans().get(currentPagePosition).getRoomIcon(), R.mipmap.icon_anchor_loading,
                mBind.ivBG);

        Log.e("currentPagePosition", currentPagePosition + " " + activity.getCurrentPosition());
        if (activity.getCurrentPosition() == currentPagePosition && activity.getPagerPosition() == viewPagePosition) {
            getRecommendList();
            addViewPage();

            //如果刷新了主播的信息 设置可以滑动 但是如果消息框在的话不能设置
            if (livingControlPanel != null) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //这个地方也不知道怎么处理最好 就延迟1500 才能滑动
                        if (!livingControlPanel.messageViewWatch.isBotViewShow()) {
                            livingControlPanel.messageViewWatch.setScrollEnable(true);
                        }
                    }
                }, 1500);

//                livingControlPanel.viewWatch.hideInputLayout();
            }
        } else {
            destroyView();
        }

    }

    private void addViewPage() {

        LivingActivity activity = (LivingActivity) getActivity();
        if (activity == null || activity.isDestroyed() || activity.isFinishing()) {
            return;
        }

        //每次都用新的 就不用重置太多东西
        destroyView();

        livingMsgBoxAdapter = null;
        livingMsgBoxBeans.clear();

        mTXPlayConfig = new TXLivePlayConfig();
        mLivePlayer = new TXLivePlayer(getActivity());
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mLivePlayer.enableHardwareDecode(false);
        setLivePlayerListener();
        setPlayMode(2, mLivePlayer);

        TXCloudVideoView txCloudVideoView = new TXCloudVideoView(getActivity());
        txCloudVideoView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBind.rlContent.addView(txCloudVideoView);
        mLivePlayer.setPlayerView(txCloudVideoView);

        ViewPager viewPager = new ViewPager(getActivity());
        viewPager.setId(R.id.livingViewPager);
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mBind.rlContent.addView(viewPager);

        livingControlPanel = new LivingControlPanel(LivingFragment.this, viewPager);

        viewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
//                super.destroyItem(container, position, object);
                ((ViewPager) container).removeView((View) object);
            }

            public Object instantiateItem(ViewGroup container, int position) {

                int screenHeight = ScreenUtils.getScreenHeight(getActivity());
                if (position == 1) {
                    container.addView(livingControlPanel, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    container.post(new Runnable() {
                        @Override
                        public void run() {
                            livingControlPanel.setData(activity.getRoomListBeans().get(currentPagePosition), activity);
                        }
                    });
                    return livingControlPanel;
                }
                return null;
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    activity.getDrawLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
                } else {
                    activity.getDrawLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(1);

        enterRoom();
        getAnchorInfo();
    }

    public RoomListBean getRoomBean() {
        LivingActivity activity = (LivingActivity) getActivity();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return null;
        }
        return activity.getRoomListBeans().get(currentPagePosition);
    }

    private void sendSystemMsgToChat(CharSequence charSequence) {
        LivingMsgBoxBean bean = new LivingMsgBoxBean();
        bean.setBackgroundColor(0x66000000);
        bean.setType(0);

        bean.setCharSequence(charSequence);
        addNewMessage(bean);
    }


    private void sendPersonalMessage(PersonalLivingMessageBean pBean) {
        LivingMsgBoxBean bean = new LivingMsgBoxBean();
        bean.setBackgroundColor(0x66000000);
        bean.setType(1);

        switch (pBean.getProtocol())
        {
            case MessageProtocol.LIVE_ROOM_CHAT_FLOATING_MESSAGE:
                playBulletMessage(pBean);
                break;
        }
        SpanUtils spanUtils = new SpanUtils();
        ChatSpanUtils.appendPersonalMessage(spanUtils, pBean, getActivity(), new LivingClickTextSpan.OnClickTextItemListener<PersonalLivingMessageBean>() {
            @Override
            public void onClick(PersonalLivingMessageBean bean) {
                showBotDialog(bean.getUid());
            }
        });
        bean.setCharSequence(spanUtils.create());
        addNewMessage(bean);
    }

    private void personalSendGiftMessage(LivingMessageGiftBean livingMessageGiftBean) {
        LivingMsgBoxBean bean = new LivingMsgBoxBean();
        bean.setBackgroundColor(0x66000000);
        bean.setType(1);

        SpanUtils spanUtils = new SpanUtils();
        ChatSpanUtils.appendPersonalSendGiftMessage(spanUtils, livingMessageGiftBean, getActivity(), new LivingClickTextSpan.OnClickTextItemListener<LivingMessageGiftBean>() {
            @Override
            public void onClick(LivingMessageGiftBean bean) {
                if(bean!=null )
                {
                    showBotDialog(bean.getUid()+"");
                }
            }
        });
        bean.setCharSequence(spanUtils.create());
        addNewMessage(bean);
    }

    private void addNewMessage(LivingMsgBoxBean bean) {
        if (!isActivityOK()) {
            return;
        }
        if (livingMsgBoxAdapter == null) {
            livingMsgBoxAdapter = new LivingMsgBoxAdapter(getContext(), livingMsgBoxBeans);
            livingControlPanel.mBind.msgBox.setAdapter(livingMsgBoxAdapter);
        }
        if (livingMsgBoxAdapter.getBeans().size() > 499) {
            livingMsgBoxAdapter.getBeans().remove(0);
        }
        livingMsgBoxAdapter.getBeans().add(bean);
        livingMsgBoxAdapter.notifyDataSetChanged();
        livingControlPanel.mBind.msgBox.smoothScrollToPosition(livingMsgBoxAdapter.getBeans().size());
    }

    public void getRecommendList() {
        LivingActivity activity = (LivingActivity) getActivity();
        activity.scrollRecommendViewToTop();
        Api_Live.ins().getRecommendLiveList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                boolean isSuccess = false;
                if (!isActivityOK()) {
                    return;
                }

                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray list = jsonObject.getJSONArray("list");
                        if (list != null && list.length() > 0) {
                            List<RoomListBean> listBeans = new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                RoomListBean bean = new Gson().fromJson(list.getJSONObject(i).toString(), RoomListBean.class);
                                listBeans.add(bean);
                            }
                            isSuccess = true;
                            activity.setRecommendListData(listBeans);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                activity.setRecommendFinish(isSuccess);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyView();
    }

    private void destroyView() {

        if (getView() != null) {

            if(handler!=null)
            {
                handler.removeMessages(playSVGA);
                handler.removeMessages(userHeartBeat);
                handler.removeMessages(alertWhenExit);
            }

            livingMessageGiftBeans.clear();
            LivingActivity activity = (LivingActivity) getActivity();
            if (!activity.isFinishing() && !activity.isDestroyed() && activity.getRoomListBeans() != null) {
                if (activity.getRoomListBeans().size() > currentPagePosition) {
                    AppIMManager.ins().loginOutGroup(activity.getRoomListBeans().get(currentPagePosition).getId());
                }
            }

            if (mLivePlayer != null) {
                mLivePlayer.stopPlay(true);
                mLivePlayer = null;
            }
            TXCloudVideoView txCloudVideoView = getView().findViewById(R.id.txLivingVideoView);
            ViewPager viewPager = getView().findViewById(R.id.livingViewPager);

            if (livingControlPanel != null && livingControlPanel.messageViewWatch != null) {
                livingControlPanel.messageViewWatch.onDestroy();
            }

            if (viewPager != null) {
                mBind.rlContent.removeView(viewPager);
            }

            if (mBind.svImage != null) {
                mBind.svImage.stopAnimation();
                mBind.svImage.clear();
            }

            if (txCloudVideoView != null) {
                txCloudVideoView.onDestroy();
                mBind.rlContent.removeView(txCloudVideoView);
            }

            mBind.ivBG.setVisibility(View.VISIBLE);

            livingMsgBoxBeans.clear();
            if (livingMsgBoxAdapter != null) {
                livingMsgBoxAdapter.getBeans().clear();
            }
            livingMsgBoxAdapter = null;
        }
    }


    private void setLivePlayerListener() {
        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int event, Bundle bundle) {
//                LogUtils.e("视频播放状态监听 " + event + ", " + bundle.getString(TXLiveConstants.EVT_DESCRIPTION));

                if (event == TXLiveConstants.PLAY_EVT_CONNECT_SUCC) {
                    // 2001 連接服務器成功
                } else if (event == TXLiveConstants.PLAY_EVT_RTMP_STREAM_BEGIN) {
                    // 2002 已經連接服務器，開始拉流（僅播放RTMP地址時會抛送）
//            dimissLiveLoadingAnimation();
//            content.setBackground(null);
                } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                    mBind.ivBG.setVisibility(View.GONE);
                    mBind.txVideoView.setVisibility(View.VISIBLE);
                    // 2003 網絡接收到首個可渲染的視頻數據包(IDR)
//                    dismissLiveLoadingAnimation();
//                    LogUtils.e(Constant.mTXLivePlayer.isPlaying() + ",");
//                    LogUtils.e((Constant.mTXLivePlayer == null) + ",");
//                    LogUtils.e((mTXCloudVideoView == null) + ",");
//                    listener.onPlayIsFinish(true);
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    // 2004 視頻播放開始，如果有轉菊花什麽的這個時候該停了
//                    if (coverIv.getVisibility() == View.VISIBLE) {
//                        //说明是第一次加载 则不做任何处理
//                    } else {
//                        // 卡顿后的流恢复
//                        dismissLiveLoadingAnimation();
//                    }

                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING) {
                    // 2007 視頻播放loading，如果能夠恢複，之後會有BEGIN事件
//                    showLiveLoadingAnimation();
                } else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
                    //2009 分辨率改变

                } else if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
                }

                /**
                 *  結束事件
                 */
                if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    // 2006 視頻播放結束

                } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
                    //  -2301 网络多次重连失败失败后 会返回此值
//                    clearStop();
//                    if (isAdded()) {
//                        networkDisconnect();
//                    }
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        });
    }


    //设置播放器模式
    private void setPlayMode(int strategy, TXLivePlayer mLivePlayer) {
        if (mTXPlayConfig == null) {
            return;
        }
        switch (strategy) {
            case 0: // 極速
                mTXPlayConfig.setAutoAdjustCacheTime(true);
                mTXPlayConfig.setMinAutoAdjustCacheTime(1);
                mTXPlayConfig.setMaxAutoAdjustCacheTime(1);
                mLivePlayer.setConfig(mTXPlayConfig);
                break;
            case 1: // 流暢
                mTXPlayConfig.setAutoAdjustCacheTime(false);
                mTXPlayConfig.setMinAutoAdjustCacheTime(5);
                mTXPlayConfig.setMaxAutoAdjustCacheTime(5);
                mLivePlayer.setConfig(mTXPlayConfig);
                break;

            case 2: // 自動
                mTXPlayConfig.setAutoAdjustCacheTime(true);
                mTXPlayConfig.setMinAutoAdjustCacheTime(1);
                mTXPlayConfig.setMaxAutoAdjustCacheTime(5);
                mLivePlayer.setConfig(mTXPlayConfig);
                break;
        }
    }

    private void enterRoom() {
        if (isActivityOK()) {
            LivingActivity activity = (LivingActivity) getActivity();
            RoomListBean bean = activity.getRoomListBeans().get(currentPagePosition);
            Api_Live.ins().interRoom(bean.getId(), bean.getAid(), 0,
                    "", 0, new JsonCallback<EnterRoomBean>() {
                        @Override
                        public void onSuccess(int code, String msg, EnterRoomBean enterRoomBean) {
                            if (mLivePlayer != null && enterRoomBean != null) {

                                if (!TextUtils.isEmpty(enterRoomBean.getPullStreamUrl())) {
                                    if (!PlayerUtils.checkPlayUrl(enterRoomBean.getPullStreamUrl(), getActivity())) {
                                        return;
                                    }

                                    //是否真实直播间(0虚拟 1真实)
                                    switch (enterRoomBean.getIsReal()) {
                                        case 0:
                                            mLivePlayer.startPlay(enterRoomBean.getPullStreamUrl(), PlayerUtils.getVideoType(enterRoomBean.getPullStreamUrl()));
                                            break;
                                        case 1:
                                            mLivePlayer.startPlay(enterRoomBean.getPullStreamUrl(), PlayerUtils.getVideoType(enterRoomBean.getPullStreamUrl()));
                                            break;
                                    }

                                }
                            }
                        }
                    });
        }

    }

    /**
     * 加入群聊前
     * 先判断用户是否连接IM
     * 如果未连接
     * 则先连接IM后再加入IM群聊
     */
    public void checkAndJoinIM(String liveId) {

        String currentUser = V2TIMManager.getInstance().getLoginUser();
        if (TextUtils.isEmpty(currentUser)) {
            //当前IM未连接用户 则先让用户连接IM后
            AppIMManager.ins().connectIM(new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    LogUtils.e("IMGroup->onError:" + code + "，" + desc);
                    hideLoadingDialog();
                    joinGroupFailed(liveId, 1, code, desc);
                }

                @Override
                public void onSuccess() {
                    //连接IM成功后 加入群聊
                    joinIMGroup(liveId);
                }
            });
        } else {
            LogUtils.e("IMGroup-> 当前连接IM的用户:" + currentUser + " liveid " + liveId);
            joinIMGroup(liveId);
        }
    }


    /**
     * 加入聊天群失败
     *
     * @param type 失败代码
     * @param code 失败代码 类型
     * @param desc 失败的原因
     */
    private void joinGroupFailed(String liveId, int type, int code, String desc) {
        switch (code) {
            case 6017:
                if ("sdk not initialized".equals(desc)) {
                    LogUtils.e("sdk not initialized");
                }
                break;
            case 10010: //群组不存在，或者曾经存在过，但是目前已经被解散
//                if (0 != currentAnchor.getLiveStatus()) {
//                    closeRoomAndStopPlay(false, false, false);
//                    sendSystemMsgToChat(getString(R.string.chatNoExist));
//                    showLiveFinishFragment(currentAnchor, getString(R.string.chatNoExist));
//                }
                break;

            case 6014://SDK 未登∂录，请先登录，成功回调之后重试，或者被踢下线，可使用 TIMManager getLoginUser 检查当前是否在线
                AppIMManager.ins().connectIM(null);
                joinIMGroup(liveId);
                break;

            case 6012: //请求超时，请等网络恢复后重试。（Android SDK 1.8.0 以上需要参考 Android 服务进程配置 方式进行配置，否则会出现此错误）
                SpanUtils spanUtils=ChatSpanUtils.appendSystemMessageType(MessageProtocol.LIVE_ENTER_ROOM,getStringWithoutContext(R.string.discRetry),getActivity());
                sendSystemMsgToChat(spanUtils.create());
                if (type == 1) {
                    checkAndJoinIM(liveId);
                } else {
                    joinIMGroup(liveId);
                }
                break;

            case 10013://被邀请加入的用户已经是群成员
                LogUtils.e("IMIMGroup->10013 被邀请加入的用户已经是群成员");
                break;

            case 9506:
            case 9520:
                //直播结束
//                showLiveFinishFragment(currentAnchor, desc);
            default:
                //直播结束
//                if (0 != currentAnchor.getLiveStatus()) {
//                    sendSystemMsgToChat(getString(R.string.app_network_error_unknown) + code);
//                    showLiveFinishFragment(currentAnchor, desc);
//                }
                break;
        }
    }

    /**
     * 加入如聊天群组
     */
    private void joinIMGroup(String liveId) {
        AppIMManager.ins().loginGroup(String.valueOf(liveId),
                getString(R.string.openJoinChat), new V2TIMCallback() {
                    @Override
                    public void onSuccess() {
                        if(isActivityOK() && livingCurrentAnchorBean!=null)
                        {
                            if (!TextUtils.isEmpty(livingCurrentAnchorBean.nickname)) {
                                String welcome = String.format(getString(R.string.chatWelcome), livingCurrentAnchorBean.nickname);
                                SpanUtils spanUtils=ChatSpanUtils.appendSystemMessageType(MessageProtocol.LIVE_ENTER_ROOM,welcome,getActivity());
                                sendSystemMsgToChat(spanUtils.create());

                                handler.sendEmptyMessageDelayed(userHeartBeat,40000);
                                handler.sendEmptyMessageDelayed(alertWhenExit,5*60000);
                            }
                        }


//                        if (currentAnchor.getShowType() == 0) {
//                            if (getLiveInFragment() != null && currentAnchor.getRoomHide() == 0) {
//                                showAdmission();
//                            }
//                        }
                    }

                    @Override
                    public void onError(int code, String desc) {
                        LogUtils.e("IMGroup-> 加入聊天失敗: code->" + code + "  , desc->" + desc);
                        joinGroupFailed(liveId, 2, code, desc);
                    }
                });
    }


    public void onNewMessageReceived(int protocol, String msg) {
        if (!isActivityOK() || livingControlPanel == null) {
            return;
        }

        Log.e("onNewMessageReceived", msg);

        if (!TextUtils.isEmpty(msg) && getRoomBean()!=null) {
            try {
                JSONObject msgJson = new JSONObject(msg);
                String protocolCode = msgJson.optString("protocol", "");
                String liveId = msgJson.optString("liveId", "");
                boolean isHasProtocolCode=!TextUtils.isEmpty(msgJson.optString("protocol", ""));
                boolean isCurrentLiveId=getRoomBean().getId().equals(liveId);

                if (isHasProtocolCode && isCurrentLiveId) {
                    switch (protocolCode) {
                        case MessageProtocol.SYSTEM_NOTICE:
                        case MessageProtocol.GAME_CP_WIN:
                            break;
                        case MessageProtocol.LIVE_ENTER_ROOM:
                            LivingEnterLivingRoomBean livingEnterLivingRoomBean = new Gson().fromJson(msg, LivingEnterLivingRoomBean.class);
                            livingEnterLivingRoomBean.setMessage(getStringWithoutContext(R.string.comeWelcome));
                            livingControlPanel.mBind.vtEnterRoom.
                                    addCharSequence(ChatSpanUtils.enterRoom(livingEnterLivingRoomBean, getActivity()).create());
                            if(livingControlPanel==null)return;

                            long uid=DataCenter.getInstance().getUserInfo().getUser().getUid();
                            boolean isPlayAvailable=livingEnterLivingRoomBean.getUid()==uid;
                            if(!isPlayAvailable)
                            {
                                long time=SPUtils.getInstance(ConstantValue.EnterRoomUIDSP).getInt(ConstantValue.EnterRoomUID,0);
                                isPlayAvailable=System.currentTimeMillis()- time>10*60*1000;
                            }

                            //播放进房 是自己不限制 不是自己限制10分钟内只播放一次
                            if(isPlayAvailable)
                            {
                                //播放进房漂房
                                livingControlPanel.mBind.rlEnterRoom.postEnterRoomMessage(livingEnterLivingRoomBean,getActivity());
                                //播放进房座驾
                                livingControlPanel.mBind.rlVehicleParentView.postEnterRoomMessage(livingEnterLivingRoomBean,getActivity());
                                //播放SVGA进房座驾动画
                                if(Strings.isDigitOnly(livingEnterLivingRoomBean.getCarId()))
                                {
                                    MountResourceBean mountResourceBean= LocalMountResourceDao.getInstance().getVehicleById(Long.valueOf(livingEnterLivingRoomBean.getCarId()));
                                    if(mountResourceBean!=null)
                                    {
                                        SvgAnimateLivingBean svgAnimateLivingBean=new SvgAnimateLivingBean();
                                        svgAnimateLivingBean.setLocalSvgPath(mountResourceBean.getLocalSvgPath());
                                        svgAnimateLivingBean.setLoopTimes(1);
                                        livingMessageGiftBeans.add(svgAnimateLivingBean);
                                        handler.sendEmptyMessage(playSVGA);
                                    }
                                }
                                SPUtils.getInstance(ConstantValue.EnterRoomUIDSP).put(ConstantValue.EnterRoomUID,System.currentTimeMillis());
                            }
                            break;
                        case MessageProtocol.LIVE_ROOM_CHAT_FLOATING_MESSAGE:
                        case MessageProtocol.LIVE_ROOM_CHAT:
                            PersonalLivingMessageBean pBean = new Gson().fromJson(msg, PersonalLivingMessageBean.class);
                            sendPersonalMessage(pBean);
                            break;
                        case MessageProtocol.LIVE_SEND_GIFT:
                            LivingMessageGiftBean gBean = new Gson().fromJson(msg, LivingMessageGiftBean.class);
                            if (gBean != null) {
                                personalSendGiftMessage(gBean);
                                GiftResourceBean giftResourceBean = LocalGiftDao.getInstance().getGift(gBean.getGid());
                                if (giftResourceBean != null && !TextUtils.isEmpty(giftResourceBean.getLocalSvgPath())) {
                                    SvgAnimateLivingBean svgAnimateLivingBean=new SvgAnimateLivingBean();
                                    svgAnimateLivingBean.setLocalSvgPath(giftResourceBean.getLocalSvgPath());
                                    svgAnimateLivingBean.setLoopTimes(gBean.getCount());
                                    livingMessageGiftBeans.add(svgAnimateLivingBean);
                                    handler.sendEmptyMessage(playSVGA);
                                }
                            }
                            break;
                        case MessageProtocol.LIVE_FOLLOW:
                            LivingFollowMessage followMessage=new Gson().fromJson(msg,LivingFollowMessage.class);
                            if(followMessage!=null && livingCurrentAnchorBean!=null)
                            {
                                followMessage.setAnchorNickName(livingCurrentAnchorBean.nickname);
                                SpanUtils spanUtils=ChatSpanUtils.appendFollowMessage(new SpanUtils(),followMessage,getActivity());
                                sendSystemMsgToChat(spanUtils.create());
                            }
                            break;
                        case MessageProtocol.GOLD_COIN_CHANGE:
                            JSONObject jsonObject=new JSONObject(msg);
                            try {
                                String diamondCoin=jsonObject.optString("diamondCoin","");
                                String goldCoin=jsonObject.optString("goldCoin","");
                                User user=new User();
                                user.setDiamond(new BigDecimal(diamondCoin));
                                user.setGold(new BigDecimal(goldCoin));
                                DataCenter.getInstance().getUserInfo().updateUser(user);
                            }
                            catch (Exception exception)
                            {
                                exception.printStackTrace();
                            }

                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取当前主播数据
     */
    public void getAnchorInfo() {
        if (!isActivityOK()) {
            return;
        }
        Api_Live.ins().getAnchorInfo(getRoomBean().getId(), getRoomBean().getAid(), new JsonCallback<LivingCurrentAnchorBean>() {
            @Override
            public void onSuccess(int code, String msg, LivingCurrentAnchorBean data) {
                if (code == 0) {
                    if (livingControlPanel != null && data!=null && isActivityOK() && getArg().equals(getRoomBean().getId())) {
                        LivingFragment.this.livingCurrentAnchorBean = data;
                        GlideUtils.loadCircleImage(getActivity(), data.getAvatar(), R.mipmap.user_head_error, R.mipmap.user_head_error,
                                livingControlPanel.mBind.rivProfileImage);
                        livingControlPanel.mBind.gtvOnlineAmount.setText(data.getLiveSum() + "");
                        livingControlPanel.mBind.gtvOnlineAmount.setVisibility(View.VISIBLE);
                        if (data.getFollow() != null) {
                            livingControlPanel.mBind.ivFollow.setVisibility(data.getFollow() ? View.GONE : View.VISIBLE);
                        }
                        checkAndJoinIM(getRoomBean().getId());
                    }

                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    public void playSVGAAnimal() {
        if(livingMessageGiftBeans.size()<1)
        {
            return;
        }
        SvgAnimateLivingBean bean=livingMessageGiftBeans.get(0);
        File file = new File(bean.getLocalSvgPath());
        if (file == null || !file.exists()) {
            return;
        }

        if(mBind.svImage.isAnimating())
        {
            return;
        }

        mBind.svImage.setLoops(bean.getLoopTimes());
        SVGAParser parser = SVGAParser.Companion.shareParser();
        mBind.svImage.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
            }

            @Override
            public void onFinished() {
                if (mBind.svImage != null) {
                    mBind.svImage.clear();
                }
                if(livingMessageGiftBeans.size()>0)
                {
                    livingMessageGiftBeans.remove(0);
                    if(livingMessageGiftBeans.size()>0)
                    {
                        handler.sendEmptyMessage(playSVGA);
                    }
                }
            }

            @Override
            public void onRepeat() {
            }

            @Override
            public void onStep(int i, double v) {
            }
        });

        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            parser.decodeFromInputStream(bufferedInputStream, file.getAbsolutePath(),
                    new SVGAParser.ParseCompletion() {
                        @Override
                        public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                            SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                            mBind.svImage.setImageDrawable(drawable);
                            mBind.svImage.startAnimation();
                        }

                        @Override
                        public void onError() {
                            Log.e("playSvg", "onError: ");
                        }
                    }, true, null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void playBulletMessage(PersonalLivingMessageBean bean)
    {
        if(getActivity()!=null && livingControlPanel!=null)
        {
            livingControlPanel.mBind.rlMidView.postBulletMessage(bean,getActivity());
        }
    }

    private void showBotDialog(String uid)
    {
        if( livingControlPanel!=null && !ClickUtil.isClickWithShortTime(uid.hashCode(),500))
        {
            if(livingControlPanel.messageViewWatch.isKeyboardShow() || livingControlPanel.messageViewWatch.isMessagesPanelOpen())
            {
                livingControlPanel.messageViewWatch.hideInputLayout();
                livingControlPanel.messageViewWatch.hideKeyboard();
                livingControlPanel.messageViewWatch.setScrollEnable(true);
            }
            else
            {
                if(!DialogFramentManager.getInstance().isShowLoading(LivingProfileBottomDialog.class.getName()))
                {
                    LivingProfileBottomDialog dialog=LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.Audience,uid);
                    dialog.setButtonClickListener(new LivingProfileBottomDialog.ButtonClickListener() {
                        @Override
                        public void onClick(String uid, boolean follow, boolean tagSomeone,String nickName) {
                            if(tagSomeone)
                            {
                                livingControlPanel.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        livingControlPanel.mBind.etDiaMessage.setText("@"+nickName+" ");
                                        livingControlPanel.mBind.etDiaMessage.setSelection(livingControlPanel.mBind.etDiaMessage.getText().length());
                                        livingControlPanel.messageViewWatch.showInputLayout();
                                    }
                                },200);
                            }
                        }
                    });
                    DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), dialog);
                }
            }

        }
    }
}
