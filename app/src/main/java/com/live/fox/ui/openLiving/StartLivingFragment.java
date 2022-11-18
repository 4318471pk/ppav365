package com.live.fox.ui.openLiving;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.google.gson.Gson;
import com.live.fox.AnchorLiveActivity;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.LiveFinishActivity;
import com.live.fox.MessageProtocol;
import com.live.fox.R;
import com.live.fox.adapter.LivingMsgBoxAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentStartLivingBinding;
import com.live.fox.db.LocalGiftDao;
import com.live.fox.db.LocalMountResourceDao;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.dialog.TipDialog;
import com.live.fox.dialog.bottomDialog.AnchorLivingRoomSettingDialog;
import com.live.fox.dialog.bottomDialog.AnchorProtectorListDialog;
import com.live.fox.dialog.bottomDialog.ContributionRankDialog;
import com.live.fox.dialog.bottomDialog.LivingProfileBottomDialog;
import com.live.fox.dialog.bottomDialog.OnlineNobilityAndUserDialog;
import com.live.fox.dialog.bottomDialog.SetRoomTypeDialog;
import com.live.fox.entity.GiftResourceBean;
import com.live.fox.entity.LivingEnterLivingRoomBean;
import com.live.fox.entity.LivingFollowMessage;
import com.live.fox.entity.LivingMessageGiftBean;
import com.live.fox.entity.LivingMsgBoxBean;
import com.live.fox.entity.MountResourceBean;
import com.live.fox.entity.PersonalLivingMessageBean;
import com.live.fox.entity.SvgAnimateLivingBean;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_Pay;
import com.live.fox.ui.living.LivingActivity;
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
import java.util.LinkedList;
import java.util.List;

import static com.tencent.rtmp.TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;

public class StartLivingFragment extends BaseBindingFragment {

    final int playSVGA = 123;
    final int userHeartBeat=987;

    FragmentStartLivingBinding mBind;
    LivingMsgBoxAdapter livingMsgBoxAdapter;
    List<LivingMsgBoxBean> livingMsgBoxBeans = new ArrayList<>();
    List<SvgAnimateLivingBean> livingMessageGiftBeans = new LinkedList<>();//播放SVGA的数组
    Handler mHandler=new Handler(Looper.myLooper());
    String liveId;

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
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.AnchorSelf));
                break;
            case R.id.gtvOnlineAmount:
                OnlineNobilityAndUserDialog onlineNobilityAndUserDialog=OnlineNobilityAndUserDialog.getInstance(mBind.gtvOnlineAmount.getText().toString(),liveId,null,null);
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),onlineNobilityAndUserDialog);
                break;
            case R.id.gtvProtection:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), AnchorProtectorListDialog.getInstance("","",null));
                break;
            case R.id.gtvContribution:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), ContributionRankDialog.getInstance("",""));
                break;
            case R.id.ivSetting:
                AnchorLivingRoomSettingDialog anchorLivingRoomSettingDialog=AnchorLivingRoomSettingDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),anchorLivingRoomSettingDialog);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_start_living;
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
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        mBind.llTopView.setVisibility(View.GONE);
        liveId=getMainActivity().liveId;
        int screenHeight= ScreenUtils.getScreenHeightWithoutBtnsBar(getActivity());
        int screenWidth=ScreenUtils.getScreenWidth(getActivity());

        setViewLP(mBind.llTopView,(int)(screenHeight*0.32f), StatusBarUtil.getStatusBarHeight(getActivity()));
        setViewLPRL(mBind.rlMidView,(int)(screenHeight*0.2f),(int)(screenHeight*0.32f));

        RelativeLayout.LayoutParams rlMessages=(RelativeLayout.LayoutParams)mBind.llMessages.getLayoutParams();
        rlMessages.height=(int)(screenHeight*0.5f)-ScreenUtils.getDip2px(getActivity(),45);
        rlMessages.width= ViewGroup.LayoutParams.MATCH_PARENT;
        rlMessages.bottomMargin=ScreenUtils.getDip2px(getActivity(),45);
        rlMessages.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        mBind.llMessages.setLayoutParams(rlMessages);

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

        mBind.msgBox.getLayoutParams().height=rlMessages.height-ScreenUtils.getDip2px(getActivity(),47);
        mBind.msgBox.getLayoutParams().width=(int)(screenWidth*0.7f);

        mBind.msgBox.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getContext(),2)));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.msgBox.setLayoutManager(linearLayoutManager);

        CountTimerUtil.getInstance().start(mBind.rlMain, new CountTimerUtil.OnAnimationFinishListener() {
            @Override
            public void onFinish() {
                mBind.llTopView.setVisibility(View.VISIBLE);
                checkAuth();
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
                        case MessageProtocol.GAME_CP_WIN:
                            break;
                        case MessageProtocol.LIVE_ENTER_ROOM:
                            LivingEnterLivingRoomBean livingEnterLivingRoomBean = new Gson().fromJson(msg, LivingEnterLivingRoomBean.class);
                            livingEnterLivingRoomBean.setMessage(getStringWithoutContext(R.string.comeWelcome));
                            mBind.vtEnterRoom.
                                    addCharSequence(ChatSpanUtils.enterRoom(livingEnterLivingRoomBean, getActivity()).create());

                            long uid=DataCenter.getInstance().getUserInfo().getUser().getUid();
                            boolean isPlayAvailable=livingEnterLivingRoomBean.getUid()==uid;
                            if(!isPlayAvailable)
                            {
                                long time= SPUtils.getInstance(ConstantValue.EnterRoomUIDSP).getInt(ConstantValue.EnterRoomUID,0);
                                isPlayAvailable=System.currentTimeMillis()- time>10*60*1000;
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

    public void checkAndJoinIM() {
        String imLoginUser = V2TIMManager.getInstance().getLoginUser();
        //加入群聊前 先判断用户是否连接IM 如果未连接 则先连接IM后再加入IM群聊
        if (TextUtils.isEmpty(imLoginUser)) {
            //当前IM未连接用户 则先让用户连接IM后
            AppIMManager.ins().connectIM(new V2TIMCallback() {
                @Override
                public void onSuccess() {
                    //连接IM成功后 加入群聊
                    sendSystemMsgToChat(getString(R.string.connectedJoin));
                    mHandler.postDelayed(() ->
                            sendSystemMsgToChat(getString(R.string.liveSuccess)), 1000);

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
                        sendSystemMsgToChat(getString(R.string.chatRetrying) + code);
                        checkAndJoinIM();
                    } else {
                        closeLiveRoom(desc, false);
                    }
                }
            });
        } else {
            mHandler.postDelayed(() ->
                    sendSystemMsgToChat(getString(R.string.liveSuccess)), 1600);
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
     * 改变房间类型
     */
    private void changeRoomType(String liveId, int type, int price)
    {
        Api_Live.ins().changeRoomType(liveId, type, price, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(code==0)
                {
                    ToastUtils.showShort(msg);
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
    public void checkAuth() {

        String nickName= DataCenter.getInstance().getUserInfo().getUser().getNickname();
        String title=getMainActivity().roomTitle;
        String roomType=String.valueOf(getMainActivity().roomType);
        String roomPrice=String.valueOf(getMainActivity().roomPrice);

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

        Api_Live.ins().getAnchorAuth("84",roomType,nickName,title,roomPrice,location.toString(),new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 && data != null) {
                    Log.e("checkAuth",data);
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        String pushStreamUrl=jsonObject.optString("pushStreamUrl","");
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
                        OpenLivingActivity openLivingActivity=(OpenLivingActivity)getActivity();
                        openLivingActivity.startRTMPPush();
                        openLivingActivity.startAcceptMessage();
                        checkAndJoinIM();

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
                LivingProfileBottomDialog dialog=LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.Audience,uid);
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
}
