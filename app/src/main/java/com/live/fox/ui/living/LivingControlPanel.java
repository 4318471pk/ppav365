package com.live.fox.ui.living;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.adapter.LivingMsgBoxAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.ControlPanelLivingBinding;
import com.live.fox.dialog.PleaseDontLeaveDialog;
import com.live.fox.dialog.TreasureBoxDialog;
import com.live.fox.dialog.bottomDialog.AnchorProtectorListDialog;
import com.live.fox.dialog.bottomDialog.ContributionRankDialog;
import com.live.fox.dialog.bottomDialog.LivingProfileBottomDialog;
import com.live.fox.dialog.bottomDialog.livingPromoDialog.LivingPromoDialog;
import com.live.fox.dialog.bottomDialog.OnlineNobilityAndUserDialog;
import com.live.fox.entity.LivingMsgBoxBean;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.TimeCounter;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.NotchInScreen;

import java.util.ArrayList;
import java.util.List;

public class LivingControlPanel extends RelativeLayout {

    //上中下模块比例 0.32 0.16 0.52
    ControlPanelLivingBinding mBind;
    LivingFragment fragment;
    LivingMsgBoxAdapter livingMsgBoxAdapter;
    List<LivingMsgBoxBean> livingMsgBoxBeans=new ArrayList<>();
    TimeCounter.TimeListener timeListener=new TimeCounter.TimeListener(5) {
        @Override
        public void onSecondTick(TimeCounter.TimeListener listener) {
            super.onSecondTick(listener);
            LivingMsgBoxBean bean=new LivingMsgBoxBean();
            bean.setBackgroundColor(0xffBDA3C8);
            bean.setStrokeColor(0xff9E3FD4);
            SpanUtils spanUtils=new SpanUtils();
            spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(78,getContext()));
            spanUtils.append(System.currentTimeMillis()+" ");

            bean.setCharSequence(spanUtils.create());
            addNewMessage(bean);
        }

        @Override
        public void onConditionTrigger(TimeCounter.TimeListener listener) {
            super.onConditionTrigger(listener);

        }
    };

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
        int topPadding= NotchInScreen.hasNotchInScreen(fragment.getActivity())?0:
                StatusBarUtil.getStatusBarHeight(fragment.getActivity());
        int screenHeight= ScreenUtils.getScreenHeightWithoutBtnsBar(parent.getContext());

        setViewLP(mBind.llTopView,(int)(screenHeight*0.32f),topPadding);
        setViewLP(mBind.rlMidView,(int)(screenHeight*0.16f),0);
        setViewLPRL(mBind.rlBotView,(int)(screenHeight*0.52f),0);

        mBind.msgBox.addItemDecoration(new RecyclerSpace(ScreenUtils.getDip2px(getContext(),2)));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.msgBox.setLayoutManager(linearLayoutManager);

        TimeCounter.getInstance().add(timeListener);
        setVisibility(VISIBLE);

    }

    private void addNewMessage(LivingMsgBoxBean bean)
    {
        if(livingMsgBoxAdapter==null)
        {
            livingMsgBoxAdapter=new LivingMsgBoxAdapter(getContext(),livingMsgBoxBeans);
            mBind.msgBox.setAdapter(livingMsgBoxAdapter);
            livingMsgBoxAdapter.getBeans().add(bean);
            livingMsgBoxAdapter.notifyItemRangeInserted(livingMsgBoxBeans.size(),1);
        }
        else
        {
            livingMsgBoxAdapter.getBeans().add(bean);
            livingMsgBoxAdapter.notifyItemRangeInserted(livingMsgBoxBeans.size(),1);
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

    public void onClickView(View view)
    {
        if(ClickUtil.isClickWithShortTime(view.getId(),1000))
        {
            return;
        }
        switch (view.getId())
        {
            case R.id.gtvOnlineAmount:
                OnlineNobilityAndUserDialog onlineNobilityAndUserDialog=OnlineNobilityAndUserDialog.getInstance(mBind.gtvOnlineAmount.getText().toString());
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(),onlineNobilityAndUserDialog);
                break;
            case R.id.rivProfileImage:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(), LivingProfileBottomDialog.getInstance());
                break;
            case R.id.ivFollow:
                break;
            case R.id.ivGift:
                TreasureBoxDialog treasureBoxDialog=TreasureBoxDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(),treasureBoxDialog);
                break;
            case R.id.tvRecommendForYou:
                LivingActivity activity=(LivingActivity) fragment.getActivity();
                activity.getDrawLayout().openDrawer(Gravity.RIGHT);
                break;
            case R.id.ivClose:
                PleaseDontLeaveDialog pleaseDontLeaveDialog=new PleaseDontLeaveDialog();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(),pleaseDontLeaveDialog);
                break;
            case R.id.rlPromo:
                LivingPromoDialog livingPromoDialog=LivingPromoDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(),livingPromoDialog);
                break;
            case R.id.gtvProtection:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(), AnchorProtectorListDialog.getInstance());
                break;
            case R.id.gtvContribution:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(), ContributionRankDialog.getInstance());
                break;
            case R.id.gtvSaySomething:
                InputMessageDialog dialog=InputMessageDialog.getInstance();
                dialog.setDialogListener(new InputMessageDialog.DialogListener() {
                    @Override
                    public void onShowKeyBorad(int height) {
                        RelativeLayout.LayoutParams ll=(RelativeLayout.LayoutParams) mBind.rlBotView.getLayoutParams();
                        ll.bottomMargin=height;
                        mBind.rlBotView.setLayoutParams(ll);
                    }

                    @Override
                    public void onDismiss() {
                        RelativeLayout.LayoutParams ll=(RelativeLayout.LayoutParams) mBind.rlBotView.getLayoutParams();
                        ll.bottomMargin=0;
                        mBind.rlBotView.setLayoutParams(ll);
                    }
                });
                DialogFramentManager.getInstance().showDialog(fragment.getChildFragmentManager(),dialog);

                break ;
        }
    }



}
