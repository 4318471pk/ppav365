package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.BotDialogLivingProfileBinding;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.RankProfileView;

import org.jetbrains.annotations.NotNull;

public class LivingProfileBottomDialog extends BaseBindingDialogFragment {

    BotDialogLivingProfileBinding mBind;

    public static LivingProfileBottomDialog getInstance()
    {
        return new LivingProfileBottomDialog();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                dismissAllowingStateLoss();
                break;
        }

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.bot_dialog_living_profile;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        view.setVisibility(View.INVISIBLE);
        mBind.rpv.setIndex(RankProfileView.NONE,0);
        mBind.rpv.setOnConfirmWidthAndHeightListener(new RankProfileView.OnConfirmWidthAndHeightListener() {
            @Override
            public void onValue(int width, int height) {
                RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) mBind.roundLL.getLayoutParams();
                rl.topMargin=height/2;
                mBind.roundLL.setLayoutParams(rl);

                RelativeLayout.LayoutParams rlName=(RelativeLayout.LayoutParams) mBind.tvName.getLayoutParams();
                rlName.topMargin=height/2+ ScreenUtils.getDip2px(getActivity(),5);
                mBind.tvName.setLayoutParams(rlName);

                view.setVisibility(View.VISIBLE);
            }
        });

        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(10, getActivity()));
        mBind.tvSmallLogo.setText(spanUtils.create());

        startAnimate();
    }


    public void startAnimate(){

        Animation animation= new TranslateAnimation(Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0
                ,Animation.RELATIVE_TO_PARENT,1f
                ,Animation.RELATIVE_TO_PARENT,0f);
        animation.setDuration(300);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBind.rlContent.startAnimation(animation);

    }
}
