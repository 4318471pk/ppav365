package com.live.fox.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.FragmentDialogLoadingBinding;
import com.live.fox.utils.ScreenUtils;

import org.jetbrains.annotations.NotNull;

public class LoadingBindingDialogFragment extends BaseBindingDialogFragment {

    public static final int white=0;
    public static final int purple=1;
    String msg;
    boolean isCancelable=false;
    boolean isBgBlack=true;

    FragmentDialogLoadingBinding mBind;
    int style=0;

    public static LoadingBindingDialogFragment getInstance()
    {
        return new LoadingBindingDialogFragment();
    }

    public static LoadingBindingDialogFragment getInstance(int style)
    {
        LoadingBindingDialogFragment fragment=new LoadingBindingDialogFragment();
        fragment.style=style;
        return fragment;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void setCancelable(boolean cancelable) {
        isCancelable = cancelable;
    }

    public void setBgBlack(boolean bgBlack) {
        isBgBlack = bgBlack;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_dialog_loading;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsKeyCanback=isCancelable;
        mIsOutCanback=isCancelable;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

//        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        setWindowsFlag();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        switch (style)
        {
            case purple:
                mBind.llMain.setBackground(null);
                mBind.tipTextView.setText("");
                int dip50= ScreenUtils.dp2px(getActivity(),50);
                LinearLayout.LayoutParams ll=(LinearLayout.LayoutParams) mBind.pbLoading.getLayoutParams();
                ll.width=dip50;
                ll.height=dip50;
                mBind.pbLoading.setLayoutParams(ll);
                mBind.pbLoading.setImageDrawable(getResources().getDrawable(R.mipmap.icon_loading_purple));
                break;
            case white:
                mBind.pbLoading.setImageDrawable(getResources().getDrawable(R.drawable.loading_ios));
                break;
        }

        if(TextUtils.isEmpty(msg))
        {
            mBind.tipTextView.setText(msg);
        }

        if(isBgBlack)
        {
            mBind.rlMain.setBackgroundColor(0x99000000);
        }
        else
        {
            mBind.rlMain.setBackgroundColor(0x00000000);
        }


        if(getActivity()!=null && !getActivity().isFinishing() && !getActivity().isDestroyed())
        {
            Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate360);
            LinearInterpolator interpolator = new LinearInterpolator();
            rotateAnimation.setInterpolator(interpolator);
            mBind.pbLoading.startAnimation(rotateAnimation);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mBind!=null)
        {
            mBind.pbLoading.clearAnimation();
        }
    }
}
