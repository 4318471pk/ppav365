package com.live.fox.ui.openLiving;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.live.fox.AnchorLiveActivity;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.LiveFinishActivity;
import com.live.fox.MessageProtocol;
import com.live.fox.R;
import com.live.fox.adapter.LivingMsgBoxAdapter;
import com.live.fox.adapter.LivingTop20OnlineUserAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentStartLivingBinding;
import com.live.fox.db.LocalGiftDao;
import com.live.fox.db.LocalMountResourceDao;
import com.live.fox.db.LocalUserVehiclePlayLimitDao;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.dialog.TipDialog;
import com.live.fox.dialog.bottomDialog.AnchorLivingRoomSettingDialog;
import com.live.fox.dialog.bottomDialog.AnchorProtectorListDialog;
import com.live.fox.dialog.bottomDialog.ContributionRankDialog;
import com.live.fox.dialog.bottomDialog.LivingProfileBottomDialog;
import com.live.fox.dialog.bottomDialog.OnlineNobilityAndUserDialog;
import com.live.fox.dialog.bottomDialog.SetRoomTypeDialog;
import com.live.fox.entity.AnchorGuardListBean;
import com.live.fox.entity.Audience;
import com.live.fox.entity.GiftResourceBean;
import com.live.fox.entity.LivingCurrentAnchorBean;
import com.live.fox.entity.LivingEnterLivingRoomBean;
import com.live.fox.entity.LivingFollowMessage;
import com.live.fox.entity.LivingGiftBean;
import com.live.fox.entity.LivingMessageGiftBean;
import com.live.fox.entity.LivingMsgBoxBean;
import com.live.fox.entity.LotteryCategoryOfBeforeLiving;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.entity.OnlineUserBean;
import com.live.fox.entity.PersonalLivingMessageBean;
import com.live.fox.entity.SvgAnimateLivingBean;
import com.live.fox.entity.User;
import com.live.fox.entity.UserVehiclePlayLimitBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_Pay;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.living.LivingControlPanel;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.CountTimerUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.LivingClickTextSpan;
import com.luck.picture.lib.permissions.RxPermissions;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.tencent.rtmp.TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;

public class StartLivingFragment extends BaseBindingFragment {

    final int playSVGA = 123;
    final int userHeartBeat=987;
    final int enterRoomRefresh=124;

    public FragmentStartLivingBinding mBind;
    LivingMsgBoxAdapter livingMsgBoxAdapter;
    List<LivingMsgBoxBean> livingMsgBoxBeans = new ArrayList<>();
    List<SvgAnimateLivingBean> livingMessageGiftBeans = new LinkedList<>();//播放SVGA的数组
    String liveId,myUID;
    LivingTop20OnlineUserAdapter livingTop20OnlineUserAdapter;
    AnchorGuardListBean anchorGuardListBean;//当前守护列表数据和人数
    List<OnlineUserBean> userList=new ArrayList<>();//当前在线用户

    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case playSVGA:
                    playSVGAAnimal();
                    break;
                case userHeartBeat:
                    Api_Live.ins().liveHeart(liveId);
                    sendEmptyMessageDelayed(userHeartBeat,30000);
                    break;
                case enterRoomRefresh:
                    removeMessages(enterRoomRefresh);
                    refresh20AudienceList();//刷新头部20个人
                    getSelfInfo();//刷新个人开播信息
                    break;
            }
        }
    };

    @Override
    public void onClickView(View view) {

        OpenLivingActivity activity=(OpenLivingActivity)getActivity();
        switch (view.getId())
        {
            case R.id.ivCameraChangeSide:
                if(ClickUtil.isClickWithShortTime(R.id.ivCameraChangeSide,3000))
                {
                    return;
                }
                activity.switchCamera();
                break;
            case R.id.ivRoomType:
                SetRoomTypeDialog setRoomTypeDialog=SetRoomTypeDialog.getInstance(true,liveId);
                setRoomTypeDialog.setOnSelectRoomTypeListener(new SetRoomTypeDialog.OnSelectRoomTypeListener() {
                    @Override
                    public void onSelect(String liveId, int type, int price) {
                        changeRoomType(liveId,type,price);
                    }
                });
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),setRoomTypeDialog);
                break;
            case R.id.rivProfileImage:
                LivingProfileBottomDialog livingProfileBottomDialog=LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.AnchorSelf,liveId,myUID);
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),livingProfileBottomDialog);
                break;
            case R.id.gtvOnlineAmount:
                OnlineNobilityAndUserDialog onlineNobilityAndUserDialog=OnlineNobilityAndUserDialog.getInstance(mBind.gtvOnlineAmount.getText().toString(),liveId,null,null);
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),onlineNobilityAndUserDialog);
                break;
            case R.id.gtvProtection:
                AnchorProtectorListDialog anchorProtectorListDialog=AnchorProtectorListDialog.getInstance(myUID,liveId,anchorGuardListBean);
                anchorProtectorListDialog.setOnRefreshDataListener(new AnchorProtectorListDialog.OnRefreshDataListener() {
                    @Override
                    public void onRefresh(AnchorGuardListBean anchorGuardListBean) {
                        StartLivingFragment.this.anchorGuardListBean=anchorGuardListBean;
                    }
                });
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),
                        anchorProtectorListDialog);
                break;
            case R.id.gtvContribution:
                ContributionRankDialog contributionRankDialog=ContributionRankDialog.getInstance(liveId,myUID);
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),contributionRankDialog);                break;
            case R.id.ivSetting:
                AnchorLivingRoomSettingDialog anchorLivingRoomSettingDialog=AnchorLivingRoomSettingDialog.getInstance(liveId,myUID);
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),anchorLivingRoomSettingDialog);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_start_living;
    }

    public int getOnlineAudAmount()
    {
        String text=mBind.gtvOnlineAmount.getText().toString();
        if(!TextUtils.isEmpty(text))
        {
            return Integer.valueOf(text);
        }
        else
        {
            return 0;
        }
    }

    private void setViewLP(View view,int height,int topMargin)
    {
        LinearLayout.LayoutParams ll=(LinearLayout.LayoutParams) view.getLayoutParams();
        ll.topMargin=topMargin;
        ll.height=height;
        view.setLayoutParams(ll);
    }

    private void setViewLPRL(View view,int height,int topMargin)
    {
        RelativeLayout.LayoutParams ll=(RelativeLayout.LayoutParams) view.getLayoutParams();
        ll.topMargin=topMargin;
        ll.height=height;
        view.setLayoutParams(ll);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(handler!=null)
        {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        mBind.llTopView.setVisibility(View.GONE);
        liveId=getMainActivity().liveId;
        myUID=String.valueOf(DataCenter.getInstance().getUserInfo().getUser().getUid());
        int screenHeight= ScreenUtils.getScreenHeightWithoutBtnsBar(getActivity());
        int screenWidth=ScreenUtils.getScreenWidth(getActivity());
//        setViewLP(mBind.llTopView,(int)(screenHeight*0.32f), StatusBarUtil.getStatusBarHeight(getActivity()));
        setViewLPRL(mBind.rlMidView,(int)(screenHeight*0.2f),(int)(screenHeight*0.32f));

        RelativeLayout.LayoutParams rlMessages=(RelativeLayout.LayoutParams)mBind.llMessages.getLayoutParams();
        rlMessages.height=(int)(screenHeight*0.5f)-ScreenUtils.getDip2px(getActivity(),45);
        rlMessages.width= (int)(screenWidth*0.7f)+ScreenUtils.getDip2px(getActivity(),10);
        rlMessages.bottomMargin=ScreenUtils.getDip2px(getActivity(),45);
        rlMessages.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        mBind.llMessages.setLayoutParams(rlMessages);

        RelativeLayout.LayoutParams rlER=(RelativeLayout.LayoutParams)mBind.rlEnterRoom.getLayoutParams();
        rlER.bottomMargin=(int)(screenHeight*0.5f);
        rlER.height=ScreenUtils.getDip2px(getActivity(),21);
        rlER.width=ViewGroup.LayoutParams.MATCH_PARENT;
        rlER.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        mBind.rlEnterRoom.setLayoutParams(rlER);

        //加入弹幕弹道
        int height=(int)(screenHeight*0.2f);
        int dip40= ScreenUtils.getDip2px(getActivity(),40);
        int size=height/dip40;
        for (int i = 0; i < size; i++) {
            RelativeLayout relativeLayout=new RelativeLayout(getActivity());
            LinearLayout.LayoutParams rl=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dip40);
            relativeLayout.setLayoutParams(rl);
            mBind.rlMidView.addView(relativeLayout);
        }

        //设置座驾弹道
        int vehicleView=ScreenUtils.getDip2px(getContext(),61);
        setViewLPRL(mBind.rlVehicleParentView,vehicleView,(int)(screenHeight*0.32f)-((int)(vehicleView*0.75f)));

        LinearLayout.LayoutParams llMsgBox=(LinearLayout.LayoutParams) mBind.msgBox.getLayoutParams();
        llMsgBox.leftMargin=ScreenUtils.getDip2px(getActivity(),10);
        llMsgBox.height=rlMessages.height-ScreenUtils.getDip2px(getActivity(),47);
        llMsgBox.width=(int)(screenWidth*0.7f);
        mBind.msgBox.setLayoutParams(llMsgBox);

        mBind.msgBox.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getContext(),2)));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.msgBox.setLayoutManager(linearLayoutManager);

        LinearLayoutManager horLayoutManager=new LinearLayoutManager(getContext());
        horLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBind.rvTop20Online.setLayoutManager(horLayoutManager);
        mBind.rvTop20Online.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getContext(),2)));

        CountTimerUtil.getInstance().start(mBind.rlMain, new CountTimerUtil.OnAnimationFinishListener() {
            @Override
            public void onFinish() {
                mBind.llTopView.setVisibility(View.VISIBLE);
                mBind.rlBotView.setVisibility(View.VISIBLE);
                startLiving();
            }
        });
    }

    private void sendSystemMsgToChat(CharSequence charSequence) {
        LivingMsgBoxBean bean = new LivingMsgBoxBean();
        bean.setBackgroundColor(0x66000000);
        bean.setType(0);

        bean.setCharSequence(charSequence);
        addNewMessage(bean);
    }


    public void onReceivedMessage(int protocol, String msg)
    {
        if (!isActivityOK()) {
            return;
        }

        Log.e("onNewMessageReceived", msg);

        if (!TextUtils.isEmpty(msg) ) {
            try {
                JSONObject msgJson = new JSONObject(msg);
                String protocolCode = msgJson.optString("protocol", "");
                String liveId = msgJson.optString("liveId", "");
                boolean isHasProtocolCode=!TextUtils.isEmpty(msgJson.optString("protocol", ""));
                boolean isCurrentLiveId=getMainActivity().liveId.equals(liveId);

                if (isHasProtocolCode && isCurrentLiveId) {
                    switch (protocolCode) {
                        case MessageProtocol.SYSTEM_NOTICE:
                            break;
                        case MessageProtocol.LIVE_BLACK_CHAT:
                        case MessageProtocol.LIVE_ROOM_SET_MANAGER_MSG:
                        case MessageProtocol.LIVE_BAN_USER:
                        case MessageProtocol.LIVE_BLACK_CHAT_CANCEL:
                            roomOperate(msgJson);
                            break;
                        case MessageProtocol.LIVE_ENTER_ROOM:
                            livingMessageEnterRoom(msg);
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
                            String nickname= DataCenter.getInstance().getUserInfo().getUser().getNickname();
                            LivingFollowMessage followMessage=new Gson().fromJson(msg,LivingFollowMessage.class);
                            if(followMessage!=null )
                            {
                                followMessage.setAnchorNickName(nickname);
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
     * 进入房间消息
     */
    private void livingMessageEnterRoom(String msg) {

        handler.sendEmptyMessageDelayed(enterRoomRefresh,1000);
        LivingEnterLivingRoomBean livingEnterLivingRoomBean = new Gson().fromJson(msg, LivingEnterLivingRoomBean.class);
        livingEnterLivingRoomBean.setMessage(getStringWithoutContext(R.string.comeWelcome));
        mBind.vtEnterRoom.
                addCharSequence(ChatSpanUtils.enterRoom(livingEnterLivingRoomBean, getActivity()).create());

        long uid=DataCenter.getInstance().getUserInfo().getUser().getUid();
        boolean isPlayAvailable=false;
        UserVehiclePlayLimitBean userVehiclePlayLimitBean = null;
        if(livingEnterLivingRoomBean.getUid()!=uid)
        {
            userVehiclePlayLimitBean= LocalUserVehiclePlayLimitDao.getInstance()
                    .selectByLiveIDAndUID(liveId,String.valueOf(uid),LocalUserVehiclePlayLimitDao.Anchor);
            if(userVehiclePlayLimitBean!=null)
            {
                isPlayAvailable=System.currentTimeMillis()- userVehiclePlayLimitBean.getShowTime()>10*60*1000;
            }
            else
            {
                isPlayAvailable=true;
            }
        }
        else
        {
            isPlayAvailable=true;
        }

        //播放进房 是自己不限制 不是自己限制10分钟内只播放一次
        if(isPlayAvailable)
        {
            //播放进房漂房
            mBind.rlEnterRoom.postEnterRoomMessage(livingEnterLivingRoomBean,getActivity());
            //播放进房座驾
            mBind.rlVehicleParentView.postEnterRoomMessage(livingEnterLivingRoomBean,getActivity());
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

            if(userVehiclePlayLimitBean==null)
            {
                userVehiclePlayLimitBean=new UserVehiclePlayLimitBean();
                userVehiclePlayLimitBean.setShowTime(System.currentTimeMillis());
                userVehiclePlayLimitBean.setType(LocalUserVehiclePlayLimitDao.Anchor);
                userVehiclePlayLimitBean.setUid(String.valueOf(uid));
                userVehiclePlayLimitBean.setLiveId(liveId);
                LocalUserVehiclePlayLimitDao.getInstance().insert(userVehiclePlayLimitBean);
            }
            else
            {
                userVehiclePlayLimitBean.setShowTime(System.currentTimeMillis());
                LocalUserVehiclePlayLimitDao.getInstance().updateData(userVehiclePlayLimitBean);
            }

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
                        if(isActivityOK())
                        {
                            sendSystemMsgToChat(ChatSpanUtils.appendSystemMessageType(MessageProtocol.LIVE_ENTER_ROOM,
                                    getStringWithoutContext(R.string.connectedJoin),getActivity()).create());
                            handler.postDelayed(() ->
                                    sendSystemMsgToChat(ChatSpanUtils.appendSystemMessageType(MessageProtocol.LIVE_ENTER_ROOM,
                                            getStringWithoutContext(R.string.liveSuccess),getActivity()).create()), 1000);
                            handler.sendEmptyMessageDelayed(userHeartBeat,30000);
                        }


                    }

                    @Override
                    public void onError(int code, String desc) {
                        LogUtils.e("IMGroup-> 加入聊天失敗: code->" + code + "  , desc->" + desc);
                        joinGroupFailed(liveId, 2, code, desc);
                    }
                });
    }

    public void checkAndJoinIM() {
        String imLoginUser = V2TIMManager.getInstance().getLoginUser();
        //加入群聊前 先判断用户是否连接IM 如果未连接 则先连接IM后再加入IM群聊
        if (TextUtils.isEmpty(imLoginUser)) {
            //当前IM未连接用户 则先让用户连接IM后
            AppIMManager.ins().connectIM(new V2TIMCallback() {
                @Override
                public void onSuccess() {
                    //连接IM成功后 加入群聊
                    joinIMGroup(liveId);
                }

                @Override
                public void onError(int code, String desc) {
                    LogUtils.e("IM->onError:" + code + "，" + desc);
                    if (6017 == code && "sdk not initialized".equals(desc)) {
                        getActivity().finish();
                    } else if (code == 9506) { //9506调用频率限制，最大每秒发起5次请求。
                        closeLiveRoom(desc, false);
                    } else if (code == 9520) {
                        closeLiveRoom(desc, false);
                    } else if (code == 6012) {
                        sendSystemMsgToChat(ChatSpanUtils.appendSystemMessageType(MessageProtocol.LIVE_ENTER_ROOM,
                                getStringWithoutContext(R.string.chatRetrying)+ code,getActivity()).create());
                        checkAndJoinIM();
                    } else {
                        closeLiveRoom(desc, false);
                    }
                }
            });
        } else {
            handler.postDelayed(() ->
                    sendSystemMsgToChat(ChatSpanUtils.appendSystemMessageType(MessageProtocol.LIVE_ENTER_ROOM,
                            getStringWithoutContext(R.string.liveSuccess),getActivity()).create()), 1600);
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
                    checkAndJoinIM();
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
     * 关闭直播间
     * 是否是管理员或者后台强制关播
     */
    public void closeLiveRoom(String finishTip, boolean isKick) {
        if(!isActivityOK())return;
        OpenLivingActivity openLivingActivity=(OpenLivingActivity)getActivity();
        if (KeyboardUtils.isSoftInputVisible(getActivity())) {
            KeyboardUtils.toggleSoftInput();
        }
//        LiveFinishActivity.startActivity(getActivity(), anchor, finishTip, isKick);
        openLivingActivity.stopRTMPPush();
        //游戏退出
    }

    /**
     * MessageProtocol.LIVE_BLACK_CHAT: 禁言
     * MessageProtocol.LIVE_ROOM_SET_MANAGER_MSG:设置房管
     * MessageProtocol.LIVE_BAN_USER:踢人（拉黑）
     * MessageProtocol.LIVE_BLACK_CHAT_CANCEL 取消禁言
     */
    private void roomOperate(JSONObject jsonObject)
    {
        if(jsonObject!=null)
        {
            String nickname=jsonObject.optString("nickname","");
            String protocol=jsonObject.optString("protocol","");
            String uid=jsonObject.optString("uid","");
            String type=jsonObject.optString("type","");

            SpanUtils spanUtils=ChatSpanUtils.appendSystemMessageType(protocol, "",getActivity());

            LivingClickTextSpan.OnClickTextItemListener listener=new LivingClickTextSpan.OnClickTextItemListener() {
                @Override
                public void onClick(Object bean) {
                    if(bean!=null && bean instanceof String)
                    {
                        showBotDialog((String)bean);
                    }
                }
            };
            LivingClickTextSpan livingClickTextSpan = new LivingClickTextSpan(uid, 0xff85EFFF);
            livingClickTextSpan.setOnClickTextItemListener(listener);
            int length1 = 0;
            int length2 = 0;

            if(!TextUtils.isEmpty(protocol))
            {
                switch (protocol)
                {
                    case MessageProtocol.LIVE_BLACK_CHAT_CANCEL:
                        spanUtils.append(nickname + ": ");
                        length1 = spanUtils.getLength();
                        spanUtils.append(getStringWithoutContext(R.string.unMuted)).setForegroundColor(0xffffffff);
                        length2 = spanUtils.getLength();
                        spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sendSystemMsgToChat(spanUtils.create());
                        break;
                    case MessageProtocol.LIVE_BLACK_CHAT:
                        spanUtils.append(nickname + ": ");
                        length1 = spanUtils.getLength();
                        spanUtils.append(getStringWithoutContext(R.string.muted)).setForegroundColor(0xffffffff);
                        length2 = spanUtils.getLength();
                        spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spanUtils.append(" "+getStringWithoutContext(R.string.forever)).setForegroundColor(0xffFF565A);
                        sendSystemMsgToChat(spanUtils.create());
                        break;
                    case MessageProtocol.LIVE_ROOM_SET_MANAGER_MSG:
                        String whiteText=Strings.isDigitOnly(type) && Integer.valueOf(type)==1?
                                getStringWithoutContext(R.string.obtainAdmin):getStringWithoutContext(R.string.cancelAdminRights);
                        spanUtils.append(nickname + " ");
                        length1 = spanUtils.getLength();
                        spanUtils.append(whiteText).setForegroundColor(0xffffffff);
                        length2 = spanUtils.getLength();
                        spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sendSystemMsgToChat(spanUtils.create());
                        break;
                    case MessageProtocol.LIVE_BAN_USER:
                        spanUtils.append(nickname + " ");
                        length1 = spanUtils.getLength();
                        spanUtils.append(getStringWithoutContext(R.string.kickedOut)).setForegroundColor(0xffffffff);
                        length2 = spanUtils.getLength();
                        spanUtils.getBuilder().setSpan(livingClickTextSpan, length1, length2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        sendSystemMsgToChat(spanUtils.create());
                        break;
                }
            }
        }
    }

    /**
     * 改变房间类型
     */
    private void changeRoomType(String liveId, int type, int price)
    {
        Api_Live.ins().changeRoomType(liveId, type, price, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(code==0)
                {

                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 开始直播
     */
    public void startLiving() {

        String nickName= DataCenter.getInstance().getUserInfo().getUser().getNickname();
        String title=getMainActivity().roomTitle;
        String liveConfigId=getMainActivity().liveConfigId;
        String roomType=String.valueOf(getMainActivity().roomType);
        String roomPrice=String.valueOf(getMainActivity().roomPrice);
        String icon=getMainActivity().imageURL;

        StringBuilder location=new StringBuilder();
        User user= DataCenter.getInstance().getUserInfo().getUser();
        if(TextUtils.isEmpty(user.getProvince()) && TextUtils.isEmpty(user.getCity()))
        {
            location.append(getStringWithoutContext(R.string.mars));
        }
        else
        {
            if(!TextUtils.isEmpty(user.getProvince()))
            {
                location.append(user.getProvince());
            }
            if(!TextUtils.isEmpty(user.getCity()))
            {
                location.append("-").append(user.getCity());
            }
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("liveConfigId",liveConfigId);
        params.put("type",roomType);
        params.put("nickName",nickName);
        params.put("title",title);
        params.put("price",roomPrice);
        if(!TextUtils.isEmpty(user.getProvince()))
        {
            params.put("province",user.getProvince());
        }
        if(!TextUtils.isEmpty(user.getCity()))
        {
            params.put("city",user.getCity());
        }
        if(!TextUtils.isEmpty(icon))
        {
            params.put("icon",icon);
        }
        if(getMainActivity().lotteryCategoryOfBeforeLiving!=null)
        {
           LotteryCategoryOfBeforeLiving bean= getMainActivity().lotteryCategoryOfBeforeLiving;
            params.put("lotteryName",bean.getGameName());
            params.put("nickName",bean.getGameCode());
        }

        if(getMainActivity().contactCostDiamond>0 &&  getMainActivity().contactType>-1 &&
                !TextUtils.isEmpty(getMainActivity().contactAccount))
        {
            params.put("contactType",getMainActivity().contactType);
            params.put("contactDetails",getMainActivity().contactAccount);
            params.put("showContactPrice",getMainActivity().contactCostDiamond);
        }

        Api_Live.ins().starLiving(params,new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 && data != null) {
                    Log.e("checkAuth",data);
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        String pushStreamUrl=jsonObject.optString("pushStreamUrl","");
                        liveId=jsonObject.optString("liveId","");
                        getMainActivity().liveId=liveId;
                        if(TextUtils.isEmpty(pushStreamUrl) || TextUtils.isEmpty(liveId))
                        {
                            ToastUtils.showShort(getString(R.string.startLivingFail));
                            return;
                        }

                        User user= DataCenter.getInstance().getUserInfo().getUser();
                        mBind.tvAnchorName.setText(user.getNickname());
                        mBind.tvAnchorID.setText("ID:"+user.getUid());
                        GlideUtils.loadCircleImage(getActivity(), user.getAvatar(), R.mipmap.user_head_error, R.mipmap.user_head_error,
                                mBind.rivProfileImage);

                        getMainActivity().setPushUrl(pushStreamUrl);
                        initFragment();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    try {
//                        JSONObject jb = new JSONObject(data);
//                        int auth = jb.optInt("auth");
//                        User user = DataCenter.getInstance().getUserInfo().getUser();
//                        if (user == null) {
//                            LogUtils.e("主播状态：" + "开启直播出错，用户信息失败");
//                            return;
//                        }
//
//                        user.setAuth(auth);
//                        DataCenter.getInstance().getUserInfo().updateUser(user);
//                        if (auth == 2 && BuildConfig.IsAnchorClient) {
//                            Constant.isAppInsideClick = true;
//                            Intent intent = new Intent(CenterOfAnchorActivity.this, AnchorLiveActivity.class);
//                            startActivity(intent);
//                        } else if (auth == 1) { //1待审核
//                            showToastTip(false, getString(R.string.certificating));
//                        } else { //未认证
//                            DialogFactory.showTwoBtnDialog(CenterOfAnchorActivity.this,
//                                    getString(R.string.certiGo), getString(R.string.cancel),
//                                    getString(R.string.goCerti), (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
//                                        dialog.dismiss();
//                                        AuthActivity.startActivity(CenterOfAnchorActivity.this);
//                                    });
//                        }
//                    } catch (Exception e) {
//                        e.getStackTrace();
//                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    //初始化相关直播数据和开始推流
    private void initFragment()
    {
        OpenLivingActivity openLivingActivity=(OpenLivingActivity)getActivity();
        openLivingActivity.startRTMPPush();
        openLivingActivity.startAcceptMessage();
        checkAndJoinIM();
        getGuardList();
        refresh20AudienceList();
        doGetAudienceListApi();
    }

    private  OpenLivingActivity getMainActivity()
    {
        if(isActivityOK())
        {
            return ((OpenLivingActivity)getActivity());
        }
        return null;
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
                livingMessageGiftBeans.remove(0);
                if(livingMessageGiftBeans.size()>0)
                {
                    handler.sendEmptyMessage(playSVGA);
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


    private void playBulletMessage(PersonalLivingMessageBean bean)
    {
        if(getActivity()!=null)
        {
            mBind.rlMidView.postBulletMessage(bean,getActivity());
        }
    }

    private void showBotDialog(String uid)
    {
        if( !ClickUtil.isClickWithShortTime(uid.hashCode(),500))
        {
            if(!DialogFramentManager.getInstance().isShowLoading(LivingProfileBottomDialog.class.getName()))
            {
                LivingProfileBottomDialog dialog=LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.AudienceInAnchorRoom,liveId,uid,myUID);
                dialog.setButtonClickListener(new LivingProfileBottomDialog.ButtonClickListener() {
                    @Override
                    public void onClick(String uid, boolean follow, boolean tagSomeone,String nickName) {
                    }
                });
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), dialog);
            }
        }
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
            mBind.msgBox.setAdapter(livingMsgBoxAdapter);
        }
        if (livingMsgBoxAdapter.getBeans().size() > 499) {
            livingMsgBoxAdapter.getBeans().remove(0);
        }
        livingMsgBoxAdapter.getBeans().add(bean);
        livingMsgBoxAdapter.notifyDataSetChanged();
        mBind.msgBox.smoothScrollToPosition(livingMsgBoxAdapter.getBeans().size());
    }


    /**
     * 刷新观众列表
     * 普通用戶根據用戶經驗排序
     */
    private void refresh20AudienceList() {
        if(!isActivityOK() )
        {
            return;
        }

        Api_Live.ins().getAudienceList(liveId, new JsonCallback<List<Audience>>() {
            @Override
            public void onSuccess(int code, String msg, List<Audience> result) {
                if(isActivityOK() )
                {
                    if (code == 0 ) {
                        if(result != null )
                        {
                            if(livingTop20OnlineUserAdapter==null)
                            {
                                livingTop20OnlineUserAdapter=new LivingTop20OnlineUserAdapter(getActivity(),result);
                                livingTop20OnlineUserAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                        Audience audience= (Audience)adapter.getData().get(position);
                                        if(audience!=null)
                                        {
                                            LivingProfileBottomDialog dialog=LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.AudienceInAnchorRoom,liveId,audience.getUid()+"",myUID);
                                            dialog.setAudience(audience);
                                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), dialog);
                                        }
                                    }
                                });

                                mBind.rvTop20Online.setAdapter(livingTop20OnlineUserAdapter);
                            }
                            else
                            {
                                livingTop20OnlineUserAdapter.setNewData(result);
                            }

                        }
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }

            }
        });
    }


    //守护列表
    private void getGuardList()
    {
        if(!isActivityOK())
        {
            return;
        }

        mBind.gtvProtection.setEnabled(false);
        Api_Live.ins().queryGuardListByAnchor(liveId, myUID, new JsonCallback<AnchorGuardListBean>() {
            @Override
            public void onSuccess(int code, String msg, AnchorGuardListBean data) {
                mBind.gtvProtection.setEnabled(true);
                if(code==0)
                {
                    if(isActivityOK() && getArg().equals(liveId) && data!=null)
                    {
                        StringBuilder sb=new StringBuilder();
                        sb.append(data.getGuardCount()).append(getStringWithoutContext(R.string.ren));
                        mBind.gtvProtection.setText(sb.toString());
                        StartLivingFragment.this.anchorGuardListBean=data;
                    }
                }
                else
                {

                }
            }
        });
    }

    /**
     * 观众列表 进入直播间就缓存下数据
     */
    public void doGetAudienceListApi() {
        if(!isActivityOK())
        {
            return;
        }

        //限制两秒内请求一次
        if(ClickUtil.isRequestWithShortTime("doGetAudienceListApi".hashCode(),2000));

        Api_Live.ins().getRoomUserList(liveId, new JsonCallback<List<OnlineUserBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<OnlineUserBean> data) {
                if (code == 0 ) {
                    if(isActivityOK() && getArg().equals(liveId) && data!=null)
                    {
                        userList.clear();
                        userList.addAll(data);
                    }
                } else {

                }
            }
        });
    }

    private void getSelfInfo()
    {
        Api_Live.ins().getAnchorInfo(liveId, myUID, new JsonCallback<LivingCurrentAnchorBean>() {
            @Override
            public void onSuccess(int code, String msg, LivingCurrentAnchorBean data) {
                if(isActivityOK())
                {
                    if(code==0)
                    {
                        if(data!=null)
                        {
                            mBind.gtvOnlineAmount.setText(data.getLiveSum()+"");
                        }
                    }
                    else
                    {

                    }
                }
            }
        });
    }
}
