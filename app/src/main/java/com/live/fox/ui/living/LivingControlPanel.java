package com.live.fox.ui.living;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.live.fox.R;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.ControlPanelLivingBinding;
import com.live.fox.dialog.bottomDialog.LivingProfileBottomDialog;
import com.live.fox.entity.FlowDataBean;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.device.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class LivingControlPanel extends RelativeLayout {

    //上中下模块比例 0.32 0.16 0.52
    ControlPanelLivingBinding mBind;
    LivingFragment fragment;

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
        int topPadding=StatusBarUtil.getStatusBarHeight(fragment.getActivity());
        int screenHeight= ScreenUtils.getScreenHeightWithoutBtnsBar(parent.getContext());

        setViewLP(mBind.llTopView,(int)(screenHeight*0.32f),0);
        setViewLP(mBind.rlMidView,(int)(screenHeight*0.16f),0);
        setViewLPRL(mBind.rlBotView,(int)(screenHeight*0.52f),0);
        setVisibility(VISIBLE);

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
        switch (view.getId())
        {
            case R.id.rivProfileImage:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(fragment.getChildFragmentManager(), LivingProfileBottomDialog.getInstance());
                break;
            case R.id.ivFollow:
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
