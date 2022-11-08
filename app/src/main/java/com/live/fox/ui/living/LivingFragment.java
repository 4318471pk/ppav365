package com.live.fox.ui.living;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.LivingMsgBoxAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentLivingBinding;
import com.live.fox.dialog.FirstTimeTopUpDialog;
import com.live.fox.dialog.PleaseDontLeaveDialog;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.EnterRoomBean;
import com.live.fox.entity.HomeFragmentRoomListBean;
import com.live.fox.entity.LivingMsgBoxBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.PlayerUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.TimeCounter;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.MyFlowLayout;
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

import java.util.ArrayList;
import java.util.List;

import static android.view.View.OVER_SCROLL_NEVER;

public class LivingFragment extends BaseBindingFragment {

    int currentPagePosition;
    int viewPagePosition;
    FragmentLivingBinding mBind;
    LivingControlPanel livingControlPanel;
    LivingMsgBoxAdapter livingMsgBoxAdapter;
    List<LivingMsgBoxBean> livingMsgBoxBeans = new ArrayList<>();
    TXLivePlayer mLivePlayer = null;
    private TXLivePlayConfig mTXPlayConfig;


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
            GlideUtils.loadDefaultImage(activity, activity.getRoomListBeans().get(currentPagePosition).getRoomIcon(),
                    mBind.ivBG);
        }
    }

    private void loadData() {
        LivingActivity activity = (LivingActivity) getActivity();
        if (activity.isFinishing() || activity.isDestroyed()) {
            return;
        }

        GlideUtils.loadDefaultImage(activity, activity.getRoomListBeans().get(currentPagePosition).getRoomIcon(),
                mBind.ivBG);

        Log.e("currentPagePosition", currentPagePosition + " " + activity.getCurrentPosition());
        if (activity.getCurrentPosition() == currentPagePosition && activity.getPagerPosition() == viewPagePosition) {
            getRecommendList();
            addViewPage();

            //如果刷新了主播的信息 设置可以滑动 但是如果消息框在的话不能设置
            if (livingControlPanel != null) {
                livingControlPanel.viewWatch.setScrollEnable(true);
                livingControlPanel.viewWatch.hideInputLayout();
            }
        } else {
            destroyView();
            Log.e("currentPagePosition222", currentPagePosition + " " + activity.getCurrentPosition());
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
        checkAndJoinIM(getRoomBean().getId());
    }

    public RoomListBean getRoomBean() {
        LivingActivity activity = (LivingActivity) getActivity();
        if (activity.isFinishing() || activity.isDestroyed()) {
            return null;
        }
        return activity.getRoomListBeans().get(currentPagePosition);
    }

    private void sendSystemMsgToChat(String msg)
    {
        LivingMsgBoxBean bean=new LivingMsgBoxBean();
        bean.setBackgroundColor(0x66ffffff);
        bean.setType(0);
        bean.setCharSequence(msg);
        addNewMessage(bean);
    }

    private void addNewMessage(LivingMsgBoxBean bean) {
        if(!isActivityOK())
        {
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

            LivingActivity activity = (LivingActivity) getActivity();
            if (!activity.isFinishing() && !activity.isDestroyed() && activity.getRoomListBeans() != null) {
                AppIMManager.ins().loginOutGroup(activity.getRoomListBeans().get(currentPagePosition).getId());
            }

            if (mLivePlayer != null) {
                mLivePlayer.stopPlay(true);
                mLivePlayer = null;
            }
            TXCloudVideoView txCloudVideoView = getView().findViewById(R.id.txLivingVideoView);
            ViewPager viewPager = getView().findViewById(R.id.livingViewPager);

            if (livingControlPanel != null && livingControlPanel.viewWatch != null) {
                livingControlPanel.viewWatch.onDestroy();
            }

            if (viewPager != null) {
                mBind.rlContent.removeView(viewPager);
            }

            if (txCloudVideoView != null) {
                txCloudVideoView.onDestroy();
                mBind.rlContent.removeView(txCloudVideoView);
            }


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
                LogUtils.e("视频播放状态监听 " + event + ", " + bundle.getString(TXLiveConstants.EVT_DESCRIPTION));

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
                            if (mLivePlayer != null && enterRoomBean != null ) {

                                if(!TextUtils.isEmpty(enterRoomBean.getPullStreamUrl()))
                                {
                                    if(!PlayerUtils.checkPlayUrl(enterRoomBean.getPullStreamUrl(),getActivity()))
                                    {
                                        return;
                                    }

                                    //是否真实直播间(0虚拟 1真实)
                                    switch (enterRoomBean.getIsReal())
                                    {
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
                    joinGroupFailed(liveId,1, code, desc);
                }

                @Override
                public void onSuccess() {
                    //连接IM成功后 加入群聊
                    joinIMGroup(liveId);
                }
            });
        } else {
            LogUtils.e("IMGroup-> 当前连接IM的用户:" + currentUser+ " liveid "+liveId);
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
    private void joinGroupFailed(String liveId,int type, int code, String desc) {
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
                sendSystemMsgToChat(getString(R.string.discRetry));
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
                        String nickName= DataCenter.getInstance().getUserInfo().getUser().getNickname();
                        if(!TextUtils.isEmpty(nickName))
                        {
                            String welcome = String.format(getString(R.string.chatWelcome), nickName);
                            sendSystemMsgToChat(welcome);
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
                        joinGroupFailed(liveId,2, code, desc);
                    }
                });
    }


    public void onNewMessageReceived(int protocol, String msg)
    {
        sendSystemMsgToChat(msg);
    }
}
