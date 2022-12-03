package com.live.fox.ui.living;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.live.fox.R;
import com.live.fox.adapter.LivingTop20OnlineUserAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ControlPanelLivingBinding;
import com.live.fox.dialog.PersonalContactCardDialog;
import com.live.fox.dialog.PleaseDontLeaveDialog;
import com.live.fox.dialog.bottomDialog.TreasureBoxDialog;
import com.live.fox.dialog.bottomDialog.AnchorProtectorListDialog;
import com.live.fox.dialog.bottomDialog.ContributionRankDialog;
import com.live.fox.dialog.bottomDialog.LivingProfileBottomDialog;
import com.live.fox.dialog.bottomDialog.livingPromoDialog.LivingPromoDialog;
import com.live.fox.dialog.bottomDialog.OnlineNobilityAndUserDialog;
import com.live.fox.entity.AnchorGuardListBean;
import com.live.fox.entity.Audience;
import com.live.fox.entity.LivingContactCardBean;
import com.live.fox.entity.OnlineUserBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.entity.SendGiftAmountBean;
import com.live.fox.entity.User;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_User;
import com.live.fox.ui.lottery.LotteryDialog;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.MessageViewWatch;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.LivingRecycleView;
import com.live.fox.view.NotchInScreen;

import java.util.ArrayList;
import java.util.List;

public class LivingControlPanel extends RelativeLayout {

    //上中下模块比例 0.32 0.16 0.52
    ControlPanelLivingBinding mBind;
    public LivingFragment fragment;
    MessageViewWatch messageViewWatch;
    LivingTop20OnlineUserAdapter livingTop20OnlineUserAdapter;
    List<OnlineUserBean> userList=new ArrayList<>();//当前在线用户
    List<OnlineUserBean> vipUserList=new ArrayList<>();//当前贵族在线用户
    AnchorGuardListBean anchorGuardListBean;//当前守护列表数据和人数
    boolean shouldAlertOnExit=false;

    public LivingControlPanel(LivingFragment fragment, ViewGroup parent) {
        super(fragment.getActivity());
        initView(fragment,parent);
    }

    public LivingControlPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LivingControlPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isActivityOK()
    {
        return fragment.isActivityOK();
    }

    public void initView(LivingFragment fragment,ViewGroup parent)
    {
        this.fragment=fragment;
        fragment.getLifecycle().addObserver(new LifecycleObserver() {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            public void onDestroy()
            {
                if(mBind!=null)
                {
                    mBind.unbind();
                    mBind=null;
                }
            }
        });


        mBind= DataBindingUtil.inflate(fragment.getLayoutInflater(),R.layout.control_panel_living,parent,false);
        mBind.setClick(this);

        addView(mBind.getRoot());

        setVisibility(GONE);
        LivingActivity livingActivity=(LivingActivity) fragment.getActivity();
        int topPadding= NotchInScreen.hasNotchInScreen(fragment.getActivity())?0:
                StatusBarUtil.getStatusBarHeight(fragment.getActivity());
        int screenHeight= ScreenUtils.getScreenHeightWithoutBtnsBar(parent.getContext());
        int screenWidth=ScreenUtils.getScreenWidth(parent.getContext());

        messageViewWatch =new MessageViewWatch();
        messageViewWatch.watchView(this,mBind);
        setViewLP(mBind.llTopView,(int)(screenHeight*0.32f),StatusBarUtil.getStatusBarHeight(fragment.getActivity()));
        setViewLPRL(mBind.rlMidView,(int)(screenHeight*0.2f),(int)(screenHeight*0.32f));
        mBind.rlMain.setViewPager(livingActivity.getViewPager());
        mBind.rlMain.setMessageViewWatch(messageViewWatch);
        mBind.etDiaMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && mBind.gtvSend.isEnabled()){
                    mBind.gtvSend.performClick();
                }
                return true;
            }
        });

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

        //设置新开通贵族消息弹道
        int messageHeight=ScreenUtils.getDip2px(getContext(),30);
        setViewLPRL(mBind.rlNewBornMessage,messageHeight,(int)(screenHeight*0.32f)-messageHeight);

        RelativeLayout.LayoutParams rlMessages=(RelativeLayout.LayoutParams)mBind.llMessages.getLayoutParams();
        rlMessages.height=(int)(screenHeight*0.5f)-ScreenUtils.getDip2px(fragment.getActivity(),45);
        rlMessages.width=(int)(screenWidth*0.7f)+ScreenUtils.getDip2px(fragment.getActivity(),10);
        rlMessages.bottomMargin=ScreenUtils.getDip2px(fragment.getActivity(),45);
        rlMessages.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        mBind.llMessages.setLayoutParams(rlMessages);

        RelativeLayout.LayoutParams rlER=(RelativeLayout.LayoutParams)mBind.rlEnterRoom.getLayoutParams();
        rlER.topMargin=(int)(screenHeight*0.5f)-ScreenUtils.getDip2px(fragment.getActivity(),21);
        rlER.height=ScreenUtils.getDip2px(fragment.getActivity(),21);
        rlER.width=ViewGroup.LayoutParams.MATCH_PARENT;
        mBind.rlEnterRoom.setLayoutParams(rlER);

        LinearLayout.LayoutParams llMsgBox=(LinearLayout.LayoutParams) mBind.msgBox.getLayoutParams();
        llMsgBox.leftMargin=ScreenUtils.getDip2px(getActivity(),10);
        llMsgBox.height=rlMessages.height-ScreenUtils.getDip2px(fragment.getActivity(),47);
        llMsgBox.width=(int)(screenWidth*0.7f);
        mBind.msgBox.setLayoutParams(llMsgBox);

        mBind.msgBox.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getContext(),2)));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.msgBox.setLayoutManager(linearLayoutManager);

        mBind.msgBox.setViewPager(livingActivity.getViewPager());
        mBind.msgBox.setOnTouchViewUpListener(new LivingRecycleView.OnTouchViewUpListener() {
            @Override
            public void onTouch() {
                if(messageViewWatch.isMessagesPanelOpen() || messageViewWatch.isKeyboardShow())
                {
                    messageViewWatch.hideInputLayout();
                    messageViewWatch.hideKeyboard();
                }
            }
        });

        LinearLayoutManager horLayoutManager=new LinearLayoutManager(getContext());
        horLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBind.rvTop20Online.setLayoutManager(horLayoutManager);
        mBind.rvTop20Online.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getContext(),2)));

        setVisibility(VISIBLE);

        int dip10=ScreenUtils.getDip2px(fragment.getActivity(),10);
        mBind.flTempleLayout.setHorizontalMargin(dip10);
        mBind.flTempleLayout.setVerticalMargin(dip10);
        mBind.flTempleLayout.setTextMaxLength(20);
        mBind.flTempleLayout.setTextBackground(R.drawable.bg_f4f1f8_round_15);
        mBind.flTempleLayout.setTextColor(0xff404040);

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

    private LivingActivity getActivity()
    {
        if(fragment.isActivityOK())
        {
            LivingActivity activity=(LivingActivity) fragment.getActivity();
            return activity;
        }
        return null;
    }

    public void onClickView(View view)
    {
        if(ClickUtil.isClickWithShortTime(view.getId(),500))
        {
            return;
        }
        String aid=fragment.getRoomBean().getAid();
        String liveId=fragment.getRoomBean().getId();
        switch (view.getId())
        {
            case R.id.gtvOnlineAmount:
                showOnlineAudience();
                break;
            case R.id.rivProfileImage:
                if(fragment.livingCurrentAnchorBean!=null)
                {
                    showProfileBotWindows();
                }
                break;
            case R.id.ivFollow:
                if(fragment.getRoomBean()!=null)
                {
                    follow(fragment.getRoomBean().getAid());
                }
                break;
            case R.id.ivGame:
                LotteryDialog lotteryDialog = LotteryDialog.getInstance();
                DialogFramentManager.getInstance().showDialog(this.getActivity().getSupportFragmentManager(), lotteryDialog);
                break;
            case R.id.ivGift:
                showTreasureDialog();
                break;
            case R.id.tvRecommendForYou:
                LivingActivity activity=(LivingActivity) fragment.getActivity();
                activity.getDrawLayout().openDrawer(Gravity.RIGHT);
                break;
            case R.id.ivClose:
                if(shouldAlertOnExit)
                {
                    String iconPath="";
                    if(fragment.livingCurrentAnchorBean!=null)
                    {
                        iconPath=fragment.livingCurrentAnchorBean.getAvatar();
                    }
                    PleaseDontLeaveDialog pleaseDontLeaveDialog=PleaseDontLeaveDialog.getInstance(iconPath,aid);
                    DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(),pleaseDontLeaveDialog);
                }
                else
                {
                    getActivity().finish();
                }
                break;
            case R.id.rlPromo:
                LivingPromoDialog livingPromoDialog=LivingPromoDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(),livingPromoDialog);
                break;
            case R.id.gtvProtection:
                AnchorProtectorListDialog anchorProtectorListDialog=AnchorProtectorListDialog.getInstance(aid,liveId,anchorGuardListBean);
                anchorProtectorListDialog.setOnRefreshDataListener(new AnchorProtectorListDialog.OnRefreshDataListener() {
                    @Override
                    public void onRefresh(AnchorGuardListBean anchorGuardListBean) {
                        LivingControlPanel.this.anchorGuardListBean=anchorGuardListBean;
                    }
                });
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(),
                        anchorProtectorListDialog);
                break;
            case R.id.ivGetAnchorContactCard:
                getContactCard();
                break;
            case R.id.gtvContribution:
                ContributionRankDialog contributionRankDialog=ContributionRankDialog.getInstance(liveId,aid);
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(),contributionRankDialog);
                break;
            case R.id.rlMain:
                messageViewWatch.hideInputLayout();
                messageViewWatch.hideKeyboard();
                break;
            case R.id.gtvMoreTemple:
                if(messageViewWatch.isKeyboardShow())
                {
                    messageViewWatch.hideKeyboard();
                    messageViewWatch.setScrollEnable(false);
                }
                else
                {
                    messageViewWatch.showKeyboard();
                }
                break;
            case R.id.gtvSaySomething:
                messageViewWatch.showInputLayout();
                break ;
            case R.id.gtvSend:
                String msg=mBind.etDiaMessage.getText().toString();
                if(TextUtils.isEmpty(msg))
                {
                    mBind.gtvSend.setEnabled(true);
                    return;
                }
                sendMessage(msg);
                break;
        }
    }

    //在线观众
    private void showOnlineAudience()
    {
        if(getActivity()!=null )
        {
            mBind.rlBotView.setVisibility(INVISIBLE);
            mBind.llMessages.setVisibility(INVISIBLE);
            OnlineNobilityAndUserDialog onlineNobilityAndUserDialog=OnlineNobilityAndUserDialog.getInstance(mBind.gtvOnlineAmount.getText().toString(),
                    fragment.getRoomBean().getId(),userList,vipUserList);
            onlineNobilityAndUserDialog.setOnDismissListener(new BaseBindingDialogFragment.OnDismissListener() {
                @Override
                public void onDismiss(BaseBindingDialogFragment fragment) {
                    mBind.rlBotView.setVisibility(VISIBLE);
                    mBind.llMessages.setVisibility(VISIBLE);
                }
            });
            onlineNobilityAndUserDialog.setDataChangeListener(new OnlineNobilityAndUserDialog.DataChangeListener() {
                @Override
                public void onChange(List<OnlineUserBean> userList, List<OnlineUserBean> vipUserList) {
                    if(userList!=null)
                    {
                        LivingControlPanel.this.userList.clear();
                        LivingControlPanel.this.userList.addAll(userList);
                    }
                    if(vipUserList!=null)
                    {
                        LivingControlPanel.this.vipUserList.clear();
                        LivingControlPanel.this.vipUserList.addAll(vipUserList);
                    }
                }
            });
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(),onlineNobilityAndUserDialog);
        }
    }

    public void showProfileBotWindows()
    {
        if(fragment.getRoomBean()==null)
        {
            return;
        }

        String liveId=fragment.getRoomBean().getId();
        String uid=fragment.getRoomBean().getAid();
        LivingProfileBottomDialog dialog=LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.AudienceAnchor,liveId,uid);
        dialog.setLivingCurrentAnchorBean(fragment.livingCurrentAnchorBean);
        dialog.setButtonClickListener(new LivingProfileBottomDialog.ButtonClickListener() {
            @Override
            public void onClick(String uid, boolean follow, boolean tagSomeone,String nickName) {
                if(fragment.getRoomBean()!=null && uid.equals(fragment.getRoomBean().getAid()))
                {
                    if(tagSomeone)
                    {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(fragment.isActivityOK())
                                {
                                    mBind.etDiaMessage.setText("@"+nickName+" ");
                                    mBind.etDiaMessage.setSelection(mBind.etDiaMessage.getText().length());
                                    messageViewWatch.showInputLayout();
                                }
                            }
                        },200);
                    }
                    if(follow)
                    {
                        fragment.livingCurrentAnchorBean.setFollow(true);
                        mBind.ivFollow.setVisibility(GONE);
                    }
                }
            }
        });
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(), dialog);
    }


    //显示礼物弹窗
    private void showTreasureDialog()
    {
        if(getActivity()!=null )
        {
            List<SendGiftAmountBean> sendGiftAmountBeanList=getActivity().getSendGiftAmountBeans();
            if(sendGiftAmountBeanList!=null && sendGiftAmountBeanList.size()>0)
            {
                mBind.rlBotView.setVisibility(INVISIBLE);
                mBind.llMessages.setVisibility(INVISIBLE);
                TreasureBoxDialog treasureBoxDialog=TreasureBoxDialog.getInstance(fragment.getRoomBean().getId(),fragment.getRoomBean().getAid());
                treasureBoxDialog.setGiftListData(getActivity().giftListData);
                treasureBoxDialog.setVipGiftListData(getActivity().vipGiftListData);
                treasureBoxDialog.setSendGiftAmountBeans(sendGiftAmountBeanList);
                treasureBoxDialog.setOnDismissListener(new BaseBindingDialogFragment.OnDismissListener() {
                    @Override
                    public void onDismiss(BaseBindingDialogFragment fragment) {
                        //去掉选中状态
                        if(getActivity().giftListData!=null)
                        {
                            for (int i = 0; i < getActivity().giftListData.size(); i++) {
                                getActivity().giftListData.get(i).setSelected(false);
                            }
                        }
                        if(getActivity().vipGiftListData!=null)
                        {
                            for (int i = 0; i < getActivity().vipGiftListData.size(); i++) {
                                getActivity().vipGiftListData.get(i).setSelected(false);
                            }
                        }
                        mBind.rlBotView.setVisibility(VISIBLE);
                        mBind.llMessages.setVisibility(VISIBLE);
                    }
                });
                treasureBoxDialog.setOnSelectedGiftListener(new TreasureBoxDialog.OnSelectedGiftListener() {
                    @Override
                    public void onSelect(String gid, int amount) {
                        doSendGiftApi(gid,amount);
                    }
                });

                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(),treasureBoxDialog);
            }

        }
    }
    public void setData(RoomListBean roomListBean,LivingActivity activity)
    {
//        mBind.tvAnchorName.setText(roomListBean.getTitle());
        mBind.tvAnchorID.setText("ID:"+roomListBean.getAid());
        refresh20AudienceList();
        doGetAudienceListApi();
        doGetVipAudienceListApi();
        getGuardList();
        getAmountOfSpeaker(roomListBean.getId());
    }

    public void follow(String targetId)
    {
        mBind.ivFollow.setEnabled(false);
        Api_User.ins().followUser(targetId, true, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(fragment.isActivityOK())
                {
                    mBind.ivFollow.setEnabled(true);
                    if(code==0)
                    {
                        mBind.ivFollow.setVisibility(GONE);
                        fragment.livingCurrentAnchorBean.setFollow(true);
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });
    }

    public void sendMessage(String message)
    {
        mBind.gtvSend.setEnabled(false);
        mBind.gtvSend.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fragment.isActivityOK())
                {
                    mBind.gtvSend.setEnabled(true);
                }
            }
        },2000);

        if(mBind.floatingTextSwitch.isChecked())
        {
            Api_Live.ins().sendBulletMessage(fragment.getRoomBean().getId(), message, new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String result) {
                    mBind.etDiaMessage.setText("");
                    mBind.gtvSend.setEnabled(true);
                    if(code!=0)
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        }
        else
        {
            Api_Live.ins().sendMessage(fragment.getRoomBean().getId(), message, new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String result) {
                    mBind.etDiaMessage.setText("");
                    mBind.gtvSend.setEnabled(true);
                    if(code!=0)
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            });

        }

    }

    private void getContactCard()
    {
        if(!fragment.isActivityOK() )
        {
            return;
        }
        if(DialogFramentManager.getInstance().isShowLoading(PersonalContactCardDialog.class.getName()))
        {
            return;
        }

        mBind.ivGetAnchorContactCard.setEnabled(false);
        RoomListBean roomListBean=fragment.getRoomBean();
        Api_Live.ins().getAnchorContactCard(roomListBean.getId(), roomListBean.getAid(), new JsonCallback<LivingContactCardBean>() {
            @Override
            public void onSuccess(int code, String msg, LivingContactCardBean data) {
                mBind.ivGetAnchorContactCard.setEnabled(true);
                if (code == 0 ) {
                    if(isActivityOK() && getArg().equals(fragment.getRoomBean().getId()) && data!=null)
                    {
                        PersonalContactCardDialog dialog=PersonalContactCardDialog.getInstance(data);
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(), dialog);
                    }
                } else {

                }
            }
        });
    }


    /**
     * 刷新观众列表
     * 普通用戶根據用戶經驗排序
     */
    public void refresh20AudienceList() {
        if(!fragment.isActivityOK() )
        {
            return;
        }

        Api_Live.ins().getAudienceList(fragment.getRoomBean().getId(), new JsonCallback<List<Audience>>() {
            @Override
            public void onSuccess(int code, String msg, List<Audience> result) {
                if (code == 0 ) {
                    if(result != null )
                    {
                        if(fragment.isActivityOK() && fragment.getRoomBean()!=null && getArg().equals(fragment.getRoomBean().getId()))
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
                                            String liveId=fragment.getRoomBean().getId();
                                            String uid=fragment.getRoomBean().getAid();

                                            LivingProfileBottomDialog dialog=LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.Audience,liveId,uid);
                                            dialog.setAudience(audience);
                                            dialog.setButtonClickListener(new LivingProfileBottomDialog.ButtonClickListener() {
                                                @Override
                                                public void onClick(String uid, boolean follow, boolean tagSomeone,String nickName) {
                                                    if(tagSomeone)
                                                    {
                                                        postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if(fragment.isActivityOK())
                                                                {
                                                                    mBind.etDiaMessage.setText("@"+nickName+" ");
                                                                    mBind.etDiaMessage.setSelection(mBind.etDiaMessage.getText().length());
                                                                    messageViewWatch.showInputLayout();
                                                                }
                                                            }
                                                        },200);
                                                    }
                                                }
                                            });
                                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(), dialog);
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
                }
                else
                {
                    ToastUtils.showShort(msg);
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

        Api_Live.ins().getRoomUserList(fragment.getRoomBean().getId(), new JsonCallback<List<OnlineUserBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<OnlineUserBean> data) {
                if (code == 0 ) {
                    if(isActivityOK() && getArg().equals(fragment.getRoomBean().getId()) && data!=null)
                    {
                        userList.clear();
                        userList.addAll(data);
                    }
                } else {

                }
            }
        });
    }

    /**
     * 调用赠送礼物接口
     */
    public void doSendGiftApi(String gid, int count) {
        if(fragment.isActivityOK())
        {
            Api_Live.ins().sendGift(gid, fragment.getRoomBean().getAid(),
                    fragment.getRoomBean().getId()+"", 1, count, new JsonCallback<String>() {
                        @Override
                        public void onSuccess(int code, String msg, String result) {
                            LogUtils.e("json : " + result);
                            if (code != 0) {
                                ToastUtils.showShort(msg);
                            }
                        }
                    });
        }
    }

    /**
     * 观众列表 进入直播间就缓存下数据
     */
    public void doGetVipAudienceListApi() {
        if(!isActivityOK())
        {
            return;
        }

        Api_Live.ins().getRoomVipList(fragment.getRoomBean().getId(), new JsonCallback<List<OnlineUserBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<OnlineUserBean> data) {
                if (code == 0 ) {
                    if(isActivityOK() && getArg().equals(fragment.getRoomBean().getId()) && data!=null)
                    {
                        vipUserList.clear();
                        vipUserList.addAll(data);
                    }
                } else {

                }
            }
        });
    }

    private void getGuardList()
    {
        if(!isActivityOK())
        {
            return;
        }

        mBind.gtvProtection.setEnabled(false);
        Api_Live.ins().queryGuardListByAnchor(fragment.getRoomBean().getId(), fragment.getRoomBean().getAid(), new JsonCallback<AnchorGuardListBean>() {
            @Override
            public void onSuccess(int code, String msg, AnchorGuardListBean data) {

                if(isActivityOK())
                {
                    mBind.gtvProtection.setEnabled(true);
                    if(code==0)
                    {
                        StringBuilder sb=new StringBuilder();
                        if(getArg().equals(fragment.getRoomBean().getId()) && data!=null)
                        {
                            sb.append(data.getGuardCount()).append(fragment.getStringWithoutContext(R.string.ren));
                            mBind.gtvProtection.setText(sb.toString());
                            LivingControlPanel.this.anchorGuardListBean=data;
                        }
                        else
                        {
                            sb.append(0).append(fragment.getStringWithoutContext(R.string.ren));
                            mBind.gtvProtection.setText(sb.toString());
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

    private void getAmountOfSpeaker(String liveId)
    {
        Api_Live.ins().getAmountOfSpeaker(liveId,new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 ) {
                    if(isActivityOK() && getArg().equals(fragment.getRoomBean().getId()) && data!=null)
                    {
                       if(Strings.isDigitOnly(data) && Integer.valueOf(data)>0)
                       {
                           mBind.rlBroadcast.setVisibility(VISIBLE);
                           mBind.tvAmountOfBroadcast.setText("x"+data);
                       }
                       else
                       {
                           mBind.rlBroadcast.setVisibility(GONE);
                       }
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });

    }

}
