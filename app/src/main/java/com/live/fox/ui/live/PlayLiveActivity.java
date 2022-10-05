package com.live.fox.ui.live;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.fox.AppIMManager;
import com.live.fox.BuildConfig;
import com.live.fox.Constant;
import com.live.fox.LiveControlFragment;
import com.live.fox.MainActivity;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.Audience;
import com.live.fox.entity.PkStatus;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_LiveRecreation;


import com.live.fox.ui.chat.MVChildFragment;
import com.live.fox.ui.home.LiveListFragment;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.LruCacheUtil;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.RoomSlideLayout;
import com.live.fox.windowmanager.WindowUtils;
import com.lzy.okgo.model.Response;
import com.tencent.android.tpush.TpnsActivity;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 观众直播的直播间
 */
public class PlayLiveActivity extends BaseActivity implements VideoFragment.OnVideoPlayStateListener,
        AppIMManager.OnMessageReceivedListener {

    RoomSlideLayout roomSlideLayout;

    public Anchor currentAnchor;  //当前直播的主播
    public Anchor userAnchor;

    ArrayList<Anchor> anchorList = new ArrayList<>();

    private VideoFragment videoFragment;
    private SlidingFragment slidingFragment;
    private LiveFinishFragment liveFinishFragment;
    private RoomPayFragment roomPayFragment;

    boolean isCloseRoom = false;

    int currentIndex = -1;

    private boolean flag = true;
    private boolean isPay = false;

    public static void startActivityForResult(LiveListFragment liveFragment, Anchor currentAnchor) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(liveFragment.getContext(), PlayLiveActivity.class);
        intent.putExtra("currentAnchor", currentAnchor);
        liveFragment.startActivityForResult(intent, 0x100);
    }

    public static void startActivityForResult(@NonNull MVChildFragment liveFragment, Anchor currentAnchor) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(liveFragment.getContext(), PlayLiveActivity.class);
        intent.putExtra("currentAnchor", currentAnchor);
        liveFragment.startActivityForResult(intent, 0x100);
    }

    public static void startActivity(Activity context, Anchor currentAnchor) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, PlayLiveActivity.class);
        intent.putExtra("currentAnchor", currentAnchor);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //保持屏幕常亮
        setContentView(R.layout.playlive_activity);
        roomSlideLayout = findViewById(R.id.room_root);

        StatusBarUtil.setStatusBarFulAlpha(PlayLiveActivity.this);
        BarUtils.setStatusBarVisibility(PlayLiveActivity.this, true);
        BarUtils.setStatusBarLightMode(PlayLiveActivity.this, true);

        if (Constant.isOpenWindow && Constant.windowAnchor != null) {//从列表来要退群
            Constant.isOpenWindow = false;
            AppIMManager.ins().loginOutGroup(String.valueOf(Constant.windowAnchor.getLiveId()));
            WindowUtils.closeWindowResource(PlayLiveActivity.class);
        }

        Constant.windowAnchor = null;
        currentAnchor = (Anchor) getIntent().getSerializableExtra("currentAnchor");

        if (currentAnchor != null && CommonApp.isNotificationClicked) {  //从通知栏来
            requestLiveList();
        } else {
            anchorList = LruCacheUtil.getInstance().get();
            handleLiveData();
            refreshAnchorInfoAndEnterRoom();
        }

        AppIMManager.ins().addMessageListener(PlayLiveActivity.class, this);
        KeyboardUtils.setSoftInputChangedListener(PlayLiveActivity.this, (isOpen, height) -> {
            //聊天框弹起监听
            if (isOpen) {
                if (getLiveInFragment() != null)
                    getLiveInFragment().onKeyBoardShow(height);
            } else {
                if (getLiveInFragment() != null)
                    getLiveInFragment().onKeyBoardHide(height);
            }
        });
        slidingFragment = SlidingFragment.newInstance(currentAnchor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonApp.getInstance().getFloatView().addToWindow(false, this);
    }

    /**
     * 处理直播数据
     */
    private void handleLiveData() {
        addVideoFragment(currentAnchor);
        addRoomPayFragment(currentAnchor);
        if (anchorList != null) {
            roomSlideLayout.setAnchorList(anchorList);
            getCurrentIndexByAnchor();
            if (currentIndex < 0) {
                //如果列表里没有这个直播间，则把这个直播间添加到第一个
                anchorList.add(0, currentAnchor);
                currentIndex = 0;
            }

            roomSlideLayout.setAnchorIndex(currentIndex);
            roomSlideLayout.setOnSlideListener(index -> {
                currentIndex = index;
                LogUtils.e("滑动到第" + index + "个");
                //切换成当前的房间
                switchRoom(false, anchorList.get(index));
            });
        }
    }

    /**
     * 获取直播列表数据
     */
    private void requestLiveList() {
        Api_Live.ins().getLiveList(1, new JsonCallback<List<Anchor>>() {
            @Override
            public void onSuccess(int code, String msg, List<Anchor> data) {
                if (data == null || data.size() == 0) {
                    return;
                }
                for (Anchor anchor : data) {
                    if (anchor.getIsAd() != 1) {
                        if (anchor.getLiveId() == currentAnchor.getLiveId()) {
                            currentAnchor = anchor;
                        }
                        anchorList.add(anchor);
                    }
                }
                handleLiveData();
                refreshAnchorInfoAndEnterRoom();
            }
        });
    }

    /**
     * 获取主播在播放列表中的位置
     */
    private void getCurrentIndexByAnchor() {
        if (currentAnchor != null) {
            for (int i = 0; i < anchorList.size(); i++) {
                if (currentAnchor.getLiveId() == anchorList.get(i).getLiveId()) {
                    currentIndex = i;
                    break;
                }
            }
        }
    }

    /**
     * 跳到下一个房间
     */
    public void nextRoom() {
        if (currentIndex == anchorList.size() - 1) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }
        roomSlideLayout.setAnchorIndex(currentIndex);
        switchRoom(false, anchorList.get(currentIndex));
    }

    /**
     * 切换房间
     *
     * @param anchor 主播信息
     */
    public void switchRoom(boolean isUpDataIndex, Anchor anchor) {
        payWaitHandler.removeMessages(currentAnchor.getType());
        Constant.isShowWindow = false;
        chargeHandler.removeMessages(1);
        if (getLiveInFragment() != null)
            getLiveInFragment().hideRecommendList();
        //退出 上一个房间的聊天室
        AppIMManager.ins().loginOutGroup(String.valueOf(currentAnchor.getLiveId()));

        //如果软键盘显示 则隐藏软键盘
        outRoomApi();
        if (KeyboardUtils.isSoftInputVisible(PlayLiveActivity.this)) {
            KeyboardUtils.toggleSoftInput();
        }
        currentAnchor = anchor;
        if (videoFragment != null) {
            videoFragment.switchRoomByState(1, anchor);
            videoFragment.changePKVideo(false);
        }

        if (getLiveInFragment() != null) {
            getLiveInFragment().switchRoomByState(1, currentAnchor);//切换房间
            getLiveInFragment().hidePkView();
            getLiveInFragment().removeRecommendAnchor(anchor);
        }

        if (liveFinishFragment != null && liveFinishFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(liveFinishFragment)
                    .commitAllowingStateLoss();
            liveFinishFragment = null;
        }

        if (isUpDataIndex) {
            getCurrentIndexByAnchor();
            roomSlideLayout.setAnchorIndex(currentIndex);
        }

        joinIMGroupHandler.removeMessages(1);

        //调用进房接口
        refreshAnchorInfoAndEnterRoom();
        if (getLiveInFragment() != null) {
            getLiveInFragment().removeRecommendAnchor(anchor);
        }
        cancelSvgAnimation();
    }


    /**
     * 取消播放的动画
     */
    private void cancelSvgAnimation() {
        if (getLiveInFragment() != null) {
            getLiveInFragment().cancelSvgPlay();
        }
    }

    //前往XXX的直播间
    public void toLiveRoom(PkStatus pkStatus) {
        int index = -1;
        if (pkStatus == null) {
            return;
        }

        if (anchorList != null) {
            for (int i = 0; i < anchorList.size(); i++) {
                if (pkStatus.getLiveId() == anchorList.get(i).getLiveId()) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                roomSlideLayout.onSlideListener.switchAnchor(index);
            } else {
                Anchor anchor = new Anchor();
                anchor.setLiveId((int) pkStatus.getLiveId());
                anchor.setNickname(anchor.getNickname());
                anchor.setAnchorId(pkStatus.getTargetId());
                anchorList.add(currentIndex, anchor);//没有彩种信息
                roomSlideLayout.onSlideListener.switchAnchor(currentIndex);
            }
        }
    }

    Handler payWaitHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            //取消dialog
            if (getLiveInFragment() != null && getLiveInFragment().isAdded()) {
                getLiveInFragment().dismissCpListDialog();
            }
            closeRoomAndStopPlay(false, true, false);
            showRoomPayFragment();
            if (roomPayFragment.isAdded()) {
                roomPayFragment.refreshPage(currentAnchor);
            }
            return false;
        }
    });

    public void showRoomPayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .show(roomPayFragment)
                .commitAllowingStateLoss();
        if (getLiveInFragment() != null) {
            getLiveInFragment().hideRecommendList();
        }
    }

    public void hideRoomPayFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(roomPayFragment)
                .commitAllowingStateLoss();
    }

    /**
     * 刷新主播信息
     */
    public void refreshAnchorInfoAndEnterRoom() {
        hideRoomPayFragment();
        if (currentAnchor != null) {
            isPay = false;
            if (1 == currentAnchor.getLiveStatus() || 3 == currentAnchor.getLiveStatus()) {
                Api_Live.ins().getAnchorInfo(currentAnchor.getLiveId(),
                        currentAnchor.getAnchorId(), new JsonCallback<Anchor>() {
                            @Override
                            public void onSuccess(int code, String msg, Anchor data) {
                                if (data != null) LogUtils.e("getAnchorInfo result : " + data);
                                if (code == 0 && data != null) {
                                    currentAnchor.setZb(data.getZb());
                                    currentAnchor.setFollow(data.isFollow());
                                    currentAnchor.setType(data.getType());
                                    currentAnchor.setPrice(data.getPrice());
                                    currentAnchor.setLiveStartLottery(data.getLiveStartLottery());

                                    if (flag) {
                                        flag = false;
                                        addHSlideFragment(currentAnchor);
                                    }
                                    if (data.getType() == 3) {
                                        showRoomPayFragment();
                                        roomPayFragment.refreshPage(currentAnchor);
                                    } else {
                                        checkLiveRoomCanBePreview(currentAnchor, "", false);
                                    }
                                } else if (code == 3001) {
                                    showLiveFinishFragment(currentAnchor, getString(R.string.roomClosed));
                                } else {
                                    ToastUtils.showShort(msg);
                                }
                            }
                        });
            } else {
                if (flag) {
                    flag = false;
                    addHSlideFragment(currentAnchor);
                }

                videoFragment.startPlay(currentAnchor);
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(roomPayFragment)
                        .commitAllowingStateLoss();
            }
        }
    }

    /**
     * 加入群聊前
     * 先判断用户是否连接IM
     * 如果未连接
     * 则先连接IM后再加入IM群聊
     */
    public void checkAndJoinIM() {
        String currentUser = V2TIMManager.getInstance().getLoginUser();
        if (TextUtils.isEmpty(currentUser)) {
            LogUtils.e("IM->currentUser null");
            if (!(ActivityUtils.getTopActivity() instanceof PlayLiveActivity)) {
                return;
            }
            //当前IM未连接用户 则先让用户连接IM后
            AppIMManager.ins().connectIM(new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    LogUtils.e("IMGroup->onError:" + code + "，" + desc);
                    hideLoadingDialog();
                    if (!(ActivityUtils.getTopActivity() instanceof PlayLiveActivity)) {
                        return;
                    }
                    joinGroupFailed(1, code, desc);
                }

                @Override
                public void onSuccess() {
                    //连接IM成功后 加入群聊
                    joinIMGroup();
                }
            });
        } else {
            LogUtils.e("IMGroup-> 当前连接IM的用户:" + currentUser);
            joinIMGroup();
        }
    }

    /**
     * 加入如聊天群组
     */
    private void joinIMGroup() {
        AppIMManager.ins().loginGroup(String.valueOf(currentAnchor.getLiveId()),
                getString(R.string.openJoinChat), new V2TIMCallback() {
                    @Override
                    public void onSuccess() {
                        SPUtils.getInstance("enterRoom").put("liveId", currentAnchor.getLiveId() + "");
                        String welcome = String.format(getString(R.string.chatWelcome), currentAnchor.getNickname());
                        sendSystemMsgToChat(welcome);
                        if (currentAnchor.getShowType() == 0) {
                            if (getLiveInFragment() != null && currentAnchor.getRoomHide() == 0) {
                                showAdmission();
                            }
                        }
                    }

                    @Override
                    public void onError(int code, String desc) {
                        LogUtils.e("IMGroup-> 加入聊天失敗: code->" + code + "  , desc->" + desc);
                        joinGroupFailed(2, code, desc);
                    }
                });
    }

    /**
     * 加入聊天群失败
     *
     * @param type 失败代码
     * @param code 失败代码 类型
     * @param desc 失败的原因
     */
    private void joinGroupFailed(int type, int code, String desc) {
        switch (code) {
            case 6017:
                if ("sdk not initialized".equals(desc)) {
                    LogUtils.e("息屏后，推出播放界面");
                    finish();
                }
                break;
            case 10010: //群组不存在，或者曾经存在过，但是目前已经被解散
                if (0 != currentAnchor.getLiveStatus()) {
                    closeRoomAndStopPlay(false, false, false);
                    sendSystemMsgToChat(getString(R.string.chatNoExist));
                    showLiveFinishFragment(currentAnchor, getString(R.string.chatNoExist));
                }
                break;

            case 6014://SDK 未登∂录，请先登录，成功回调之后重试，或者被踢下线，可使用 TIMManager getLoginUser 检查当前是否在线
                AppIMManager.ins().connectIM(null);
                joinIMGroup();
                break;

            case 6012: //请求超时，请等网络恢复后重试。（Android SDK 1.8.0 以上需要参考 Android 服务进程配置 方式进行配置，否则会出现此错误）
                sendSystemMsgToChat(getString(R.string.discRetry));
                if (type == 1) {
                    checkAndJoinIM();
                } else {
                    joinIMGroup();
                }
                break;

            case 10013://被邀请加入的用户已经是群成员
                LogUtils.e("IMIMGroup->10013 被邀请加入的用户已经是群成员");
                break;

            case 9506:
            case 9520:
                showLiveFinishFragment(currentAnchor, desc);

            default:
                if (0 != currentAnchor.getLiveStatus()) {
                    sendSystemMsgToChat(getString(R.string.app_network_error_unknown) + code);
                    showLiveFinishFragment(currentAnchor, desc);
                }
                break;
        }
    }

    /**
     * 显示入场动画
     */
    private void showAdmission() {
        Audience audience = new Audience();
        audience.setShowType(userAnchor.getShowType());
        audience.setAvatar(DataCenter.getInstance().getUserInfo().getUser().getAvatar());
        audience.setCarId(userAnchor.getCarId());
        audience.setNickname(userAnchor.getNickname());
        audience.setLevel(userAnchor.getLevel());
        if (getLiveInFragment() != null)
            getLiveInFragment().playSvg(audience);
    }

    private final Handler joinIMGroupHandler = new Handler(msg -> {
        checkAndJoinIM();
        return false;
    });

    /**
     * 调用进房接口
     * isDoCharge主要是点击付费按钮
     * 密码房间
     * 付费房
     */
    public void checkLiveRoomCanBePreview(Anchor anchor, String pwd, boolean isDoCharge) {
        Api_Live.ins().interPreviewRoom(anchor.getLiveId(), anchor.getAnchorId(),
                anchor.getType(), pwd, new JsonCallback<Anchor>() {
                    @Override
                    public void onSuccess(int code, String msg, Anchor data) {
                        if (data != null) {
                            if (data.getIsPreview() == 0) {
                                if (anchor.getType() == 1 || anchor.getType() == 2) {
                                    reallyEnterRoom(anchor, pwd, isDoCharge, 1);
                                } else {
                                    reallyEnterRoom(anchor, pwd, isDoCharge, 0);
                                }
                            } else if (data.getIsPreview() == 1) { //不可预览或者已经预览过了
                                payWaitHandler.sendEmptyMessage(currentAnchor.getType());
                            }
                        } else {
                            reallyEnterRoom(anchor, pwd, isDoCharge, 0);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        reallyEnterRoom(anchor, pwd, isDoCharge, 0);
                    }
                });
    }

    /**
     * 真正的进房接口
     *
     * @param anchor     主播信息
     * @param pwd        密码
     * @param isDoCharge 是否是付费房
     * @param preview    是否是预览房间
     */
    public void reallyEnterRoom(Anchor anchor, String pwd, boolean isDoCharge, int preview) {
        showLoadingView();
        LogUtils.e("EnterRoom PP-调用进房接口 ");
        if (getLiveInFragment() != null)
        {
            getLiveInFragment().setPreview(preview);
            getLiveInFragment().sendRoomBulletin(); //发送直播间公告
        }

        Api_Live.ins().interRoom(anchor.getLiveId(), anchor.getAnchorId(), anchor.getType(),
                pwd, preview, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String jsonData) {
                        hideLoadingView();
                        //在进房接口调用过程中 退出此页面中 则获得结果后不做任何操作
                        if (PlayLiveActivity.this.isFinishing() || isCloseRoom) return;
                        if (code == 0 && jsonData != null) {
                            Anchor tempAnchor = new Gson().fromJson(jsonData, Anchor.class);
                            currentAnchor.setPullStreamUrl(tempAnchor.getPullStreamUrl());
                            currentAnchor.setCarId(tempAnchor.getCarId());
                            currentAnchor.setRoomManager(tempAnchor.isRoomManager());
                            currentAnchor.setShowType(tempAnchor.getShowType());
                            currentAnchor.setLevel(tempAnchor.getLevel());
                            currentAnchor.setRoomHide(tempAnchor.getRoomHide());

                            userAnchor = new Anchor();
                            userAnchor.setPullStreamUrl(tempAnchor.getPullStreamUrl());
                            userAnchor.setShowType(tempAnchor.getShowType());
                            userAnchor.setCarId(tempAnchor.getCarId());
                            userAnchor.setRoomManager(tempAnchor.isRoomManager());
                            userAnchor.setLevel(tempAnchor.getLevel());
                            userAnchor.setNickname(DataCenter.getInstance().getUserInfo().getUser().getNickname());
                            userAnchor.setAvatar(DataCenter.getInstance().getUserInfo().getUser().getAvatar());

                            if (isDoCharge) {
                                doChargeRoomApi();
                            } else {
                                if (getLiveInFragment() != null)
                                    getLiveInFragment().daojisi();
                                startPlayAndRefreshLiveRoom();//10秒以后
                            }

                            //获取当前直播间PK状态
                            doGetPKStatus(currentAnchor.getAnchorId());
                        } else if (code == 3001) {
                            hideRoomPayFragment();
                            showLiveFinishFragment(currentAnchor, getString(R.string.roomClosed));
                        } else {
                            hideRoomPayFragment();
                            showLiveFinishFragment(currentAnchor, msg);
                        }
                    }
                });
    }


    /**
     * 获取当前直播间PK状态
     *
     * @param anchorId 主播id
     */
    public void doGetPKStatus(long anchorId) {
        Api_LiveRecreation.ins().getPkStatus(anchorId, new JsonCallback<PkStatus>() {
            @Override
            public void onSuccess(int code, String msg, PkStatus data) {
                if (data != null)
                    LogUtils.e("PkStatus result : " + new Gson().toJson(data) + "," + System.currentTimeMillis());
                if (code == 0) {
                    if (data == null) return;
                    videoFragment.changePKVideo(true);
                    if (data.getResult() < 0) {
                        //PK阶段
                        if (getLiveInFragment() != null)
                            getLiveInFragment().setPkView(0, -1, data);
                    } else {
                        //惩罚阶段
                        if (getLiveInFragment() != null)
                            getLiveInFragment().setPkView(2, data.getResult(), data);
                    }
                } else {
                    if (getLiveInFragment() != null)
                        getLiveInFragment().setPkView(1, -1, null);
                    videoFragment.changePKVideo(false);
                }
            }
        });
    }

    /**
     * 显示还是隐藏付费框
     * TODO 未完成
     *
     * @param isShow 显示或者隐藏
     */
    private void roomPayStatus(boolean isShow) {

    }

    public void startPlayAndRefreshLiveRoom() {
        videoFragment.startPlay(currentAnchor);

        hideRoomPayFragment();

        //刷新直播间信息
        if (getLiveInFragment() != null)
            getLiveInFragment().refreshLiveRoom(currentAnchor, isPay);

        //切换后等一段时间后再加入群聊 防止上下滑动切换过快导致频繁进入聊天室的问题
        joinIMGroupHandler.removeMessages(1);
        joinIMGroupHandler.sendEmptyMessageDelayed(1, 1600);
        if ((currentAnchor.getType() == 1 || currentAnchor.getType() == 2) && DataCenter.getInstance().getUserInfo().getUser() != null &&
                !DataCenter.getInstance().getUserInfo().getUser().isSuperManager() && !isPay) {
            //此房间是付费房间
            LogUtils.e("onDestroy PlayActivity 收费房间");
            //调用扣费接口
            payWaitHandler.sendEmptyMessageDelayed(currentAnchor.getType(), 10000);
        }

        //开启直播间心跳
        /*
         * watchHeart60sHandler.removeMessages(1);
         * watchHeart60sHandler.sendEmptyMessageDelayed(1, heartSpace);
         */
    }

    private final Handler chargeHandler = new Handler(msg -> {
        doChargeRoomApi();
        return false;
    });

    /**
     * 扣费接口
     */
    public void doChargeRoomApi() {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }

        Api_Live.ins().changeRoom(currentAnchor.getLiveId(), currentAnchor.getAnchorId(), new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 || msg.equals("success") || msg.equals("ok")) {
                    isPay = true;
                    startPlayAndRefreshLiveRoom();
                    if (currentAnchor.getType() == 1) {   //如果是计时房间 则每60秒扣一次费用
                        chargeHandler.sendEmptyMessageDelayed(1, 60 * 1000);
                    }
                } else {
                    chargeHandler.removeMessages(1);
                    videoFragment.clearStop();
                    showRoomPayFragment();
                    if (code == 4003) {
                        DialogFactory.showTwoBtnDialog(PlayLiveActivity.this,
                                getString(R.string.insufficientRecharge),
                                (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                                    dialog.dismiss();
                                    RechargeActivity.startActivity(PlayLiveActivity.this);
                                });
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !Settings.canDrawOverlays(getCtx())) {
                ToastUtils.showShort(getString(R.string.sqsb));
            } else {
                ToastUtils.showShort(getString(R.string.sqcg));
            }
        }
    }

    /**
     * 用户点关闭直播间的流程
     */
    public void outRoomByUserClose() {
        Constant.isShowWindow = currentAnchor.getType() == 0;
        if (Constant.isShowWindow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                if (SPManager.getXCK()) {
                    SPManager.saveXCK(false);
                    DialogFactory.showTwoBtnDialog(this, getString(R.string.kqxcktk),
                            (button, dialog) -> {//取消
                                Constant.isShowWindow = false;
                                closeRoomAndStopPlay(false, true, true);
                            }, (button, dialog) -> {
                                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + getCtx().getPackageName())), 0);
                                dialog.dismiss();
                            }).setTextDes(getString(R.string.sfkqxck));
                } else {
                    Constant.isShowWindow = false;
                    closeRoomAndStopPlay(false, true, true);
                }
            } else {
                closeRoomAndStopPlay(true, false, false);
            }
        } else {
            closeRoomAndStopPlay(false, true, true);
        }
    }

    public void onClickAddToWindow() {
        if (videoFragment != null) {
            videoFragment.onClickAddToWindow();
        }
    }

    private void addVideoFragment(Anchor anchor) {
        LogUtils.e("addVideoFragment");
        videoFragment = VideoFragment.newInstance(anchor);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.video_container, videoFragment)
                .commitAllowingStateLoss();

        videoFragment.setOnVideoPlayStateListener(this);
    }

    private void addHSlideFragment(Anchor anchor) {
        LogUtils.e("addLiveInFragment");
        slidingFragment = SlidingFragment.newInstance(anchor);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, slidingFragment)
                .commitAllowingStateLoss();
    }

    /**
     * @param anchor
     */
    private void addRoomPayFragment(Anchor anchor) {
        roomPayFragment = RoomPayFragment.newInstance(anchor);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_room_pay_tip, roomPayFragment)
                .commitAllowingStateLoss();
    }

    private void showLiveFinishFragment(Anchor anchor, String reason) {
        if (getLiveInFragment() != null) getLiveInFragment().dismissUserDetailDialog();
        if (KeyboardUtils.isSoftInputVisible(this)) {
            KeyboardUtils.hideSoftInput(this);
        }

        liveFinishFragment = LiveFinishFragment.newInstance(anchor, reason);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, liveFinishFragment)
                .commitAllowingStateLoss();
    }

    /**
     * 获取控制房间控制页面
     *
     * @return 返回
     */
    public LiveControlFragment getLiveInFragment() {
        return slidingFragment.getLiveControlFragment();
    }

    /**
     * 退出房间和停止播放统一接口
     */
    public void outRoomApi() {
        LogUtils.e("调用退房接口");
        if (!StringUtils.isEmpty(currentAnchor.getPullStreamUrl()) && 0 != currentAnchor.getLiveStatus()) {
            //如果PullStreamUrl为空 说明没进房成功 则不调用退房接口
            Api_Live.ins().outRoom(currentAnchor.getLiveId(), new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String result) {

                }
            });
        }
    }

    /**
     * 直播视频加载完成 开始播放及加载数据
     */
    @Override
    public void onPlayIsFinish(boolean isFinish) {
        if (isFinish && getLiveInFragment() != null) {
            getLiveInFragment().switchRoomByState(2, currentAnchor);
        }
    }

    /**
     * 显示一条系统消息在聊天框里
     * 在聊天室插入消息
     *
     * @param content 消息内容
     */
    public void sendSystemMsgToChat(String content) {
        int protocol = Constant.MessageProtocol.PROTOCOL_MyCoust;
        Map<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("liveId", currentAnchor.getLiveId());
        map.put("protocol", protocol);
        onIMReceived(protocol, new Gson().toJson(map));
    }


    /**
     * 直播间公告消息
     *
     * @param content 公告内容
     */
    public void sendLiveRoomNotice(String content) {
        int protocol = Constant.MessageProtocol.PROTOCOL_LIVE_BROADCAST;
        Map<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("liveId", currentAnchor.getLiveId());
        map.put("protocol", protocol);
        onIMReceived(protocol, new Gson().toJson(map));
    }

    /**
     * 收到消息提示
     *
     * @param protocol 协议
     * @param msg      信息
     */
    @Override
    public void onIMReceived(int protocol, String msg) {
        try {
            LogUtils.e(protocol + ", onIMReceived msg : " + msg);
            JSONObject message = new JSONObject(msg);
            if (!Constant.isOpenWindow) {
                if (getLiveInFragment() != null) {
                    getLiveInFragment().onReceived(protocol, message);
                }
            }

            User user = DataCenter.getInstance().getUserInfo().getUser();
            switch (protocol) {
                case Constant.MessageProtocol.PROTOCOL_LIVE_CLOSE: //2、关播/强制关播消息
                    if (Constant.isOpenWindow) {
                        closeWindow();
                    } else {
                        long liveId1 = message.optLong("liveId", 0);
                        if (liveId1 == currentAnchor.getLiveId()) {
                            closeRoomAndStopPlay(false, true, false);
                            showLiveFinishFragment(currentAnchor, getString(R.string.endLive));
                        }
                    }
                    break;

                case Constant.MessageProtocol.PROTOCOL_LIVE_KICK:  //踢人消息
                    if (Constant.isOpenWindow) {
                        closeWindow();
                    } else {
                        long liveId2 = message.optLong("liveId", 0);
                        long kickedUid = message.optLong("uid");
                        if (currentAnchor.getLiveId() == liveId2 && kickedUid == user.getUid()) {
                            closeRoomAndStopPlay(false, true, false);
                            showLiveFinishFragment(currentAnchor, getString(R.string.kickedOut));
                        }
                    }
                    break;

                case Constant.MessageProtocol.PROTOCOL_RoomPay_Change:  //房间收费消息变动
                    LogUtils.e("房间收费消息变动");
                    long liveId = message.optLong("liveId", 0);
                    int type = message.optInt("type", 0);
                    int price = message.optInt("price");
                    if (liveId != currentAnchor.getLiveId()) return;
                    if (type == currentAnchor.getType()) return;

                    chargeHandler.removeMessages(1);
                    currentAnchor.setType(type);
                    currentAnchor.setPrice(price);
                    if (type > 0) {
                        if (Constant.isOpenWindow) {
                            videoFragment.changeWindowText(currentAnchor);
                        } else {
                            closeRoomAndStopPlay(false, true, false);
                            showRoomPayFragment();
                            LogUtils.e("roomFeeModeChanged");
                            roomPayFragment.refreshPage(currentAnchor);
                        }
                    } else {
                        if (Constant.isOpenWindow) {
                            videoFragment.changeWindowText(currentAnchor);
                        }
                    }
                    break;

                case Constant.MessageProtocol.PROTOCOL_PK_START_STOP:  //18 PK开启关闭消息
                    int status = message.optInt("status", 2);
                    LogUtils.e("PK开启关闭消息 " + status);
                    if (status == 1) {
                        //直播间的主播开始PK
                        if (Constant.isOpenWindow) {
                            CommonApp.getInstance().getFloatView().adjustView(true);
                        } else {
                            videoFragment.changePKVideo(true);
                            if (getLiveInFragment() != null)
                                getLiveInFragment().setPkView(0, -1, null);
                        }
                    } else {
                        if (Constant.isOpenWindow) {
                            CommonApp.getInstance().getFloatView().adjustView(false);
                        } else {
                            //直播间的主播结束PK
                            videoFragment.changePKVideo(false);
                            if (getLiveInFragment() != null)
                                getLiveInFragment().setPkView(1, -1, null);
                        }
                    }
                    break;

                case Constant.MessageProtocol.PROTOCOL_PK_RESULT:  //23 PK结果消息
                    if (!Constant.isOpenWindow) {
                        int result = message.optInt("result", 0);
                        if (getLiveInFragment() != null)
                            getLiveInFragment().setPkView(2, result, null);
                    }
                    break;

                case Constant.MessageProtocol.PROTOCOL_PK_VALUE_CHANGE:  //24 PK比分变动消息
                    if (!Constant.isOpenWindow) {
                        long scoreA = message.optLong("scoreA", 0);
                        long scoreB = message.optLong("scoreB", 0);
                        JSONArray mA = message.optJSONArray("listA");
                        JSONArray mB = message.optJSONArray("listB");
                        List<User> mListA = new Gson().fromJson(String.valueOf(mA), new TypeToken<ArrayList<User>>() {
                        }.getType());
                        List<User> mListB = new Gson().fromJson(String.valueOf(mB), new TypeToken<ArrayList<User>>() {
                        }.getType());
                        if (getLiveInFragment() != null)
                            getLiveInFragment().refreshPkScore(scoreA, scoreB, mListA, mListB);
                    }
                    break;
            }
        } catch (JSONException e) {
            LogUtils.e("解析错误");
            ToastUtils.showShort(getString(R.string.IMDataWrong));
        }
    }

    public void closeWindow() {
        Constant.isOpenWindow = false;
        Constant.isShowWindow = false;
        CommonApp.getInstance().getFloatView().addToWindow(false, this);

        if (Constant.windowAnchor != null) {
            AppIMManager.ins().loginOutGroup(String.valueOf(Constant.windowAnchor.getLiveId()));
        }

        WindowUtils.closeWindowResource(PlayLiveActivity.class);
    }

    /**
     * 退出房间和停止播放统一接口
     *
     * @param isOpenWindow     小窗是否打开
     * @param isExitRoomChat   是否是推出房间
     * @param isFinishActivity 是否关闭
     */
    public void closeRoomAndStopPlay(boolean isOpenWindow, boolean isExitRoomChat, boolean isFinishActivity) {
        cancelSvgAnimation();
        if (isExitRoomChat && currentAnchor != null) {
            AppIMManager.ins().loginOutGroup(String.valueOf(currentAnchor.getLiveId()));
        }

        if (isFinishActivity) {  //释放资源并结束Activity
            joinIMGroupHandler.removeMessages(1);
            chargeHandler.removeMessages(1);
            WindowUtils.closeWindowResource(PlayLiveActivity.class);
            finish();
        }

        if (isOpenWindow) {
            chargeHandler.removeMessages(1);
            Constant.windowAnchor = currentAnchor;
            Constant.isOpenWindow = true;
            Constant.isAppInsideClick = true;
            onClickAddToWindow();
            finish();
        } else {
            outRoomApi();
            if (videoFragment != null) {
                videoFragment.stopPlay(true);
                videoFragment.showCover();
            }
        }

        if (CommonApp.isNotificationClicked) {
            CommonApp.isNotificationClicked = false;
            Constant.isAppInsideClick = true;
            Activity activity = CommonApp.topActivity.get();
            if (activity == null || activity instanceof MainActivity
                    || activity instanceof TpnsActivity) {
                MainActivity.startActivityWithPosition(this, 5);
            }
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        outRoomByUserClose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonApp.isFloatWindowClick = false;
        if (payWaitHandler != null) {
            payWaitHandler.removeMessages(currentAnchor.getType());
        }
    }
}

