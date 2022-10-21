package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogContributionRankBinding;
import com.live.fox.ui.mine.contribution.ContributionRankFragment;
import com.live.fox.ui.rank.RankActivity;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;

public class ContributionRankDialog extends BaseBindingDialogFragment {

    DialogContributionRankBinding mBind;

    public static final ContributionRankDialog getInstance()
    {
        return new ContributionRankDialog();
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
        if(view.getId()==mBind.ivRank.getId())
        {
            RankActivity.startActivity(getActivity());
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_contribution_rank;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        int screenHeight= ScreenUtils.getScreenHeight(getActivity());
        mBind.rllContent.getLayoutParams().height=(int)(screenHeight*0.7f);

        setData();
        startAnimate();
    }


    private void setData()
    {
        String titles[]=getResources().getStringArray(R.array.rank_tab_contribution);
        int widthScreen= ScreenUtils.getScreenWidth(getActivity());

        mBind.vpDialogMain.setAdapter(new BaseFragmentPagerAdapter(getChildFragmentManager()){
            @Override
            public Fragment getFragment(int position) {
                return ContributionRankFragment.newInstance(position);
            }

            @Override
            public String getTitle(int position) {
                return titles[position];
            }

            @Override
            public int getItemCount() {
                return titles.length;
            }
        });

        mBind.vpDialogMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton radioButton=(RadioButton)mBind.rgTabs.getChildAt(position);
                radioButton.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int dip5=ScreenUtils.getDip2px(getActivity(),5);

        for (int i = 0; i < titles.length; i++) {
            RadioButton radioButton=new RadioButton(getActivity());
            radioButton.setButtonDrawable(new ColorDrawable(0));
            radioButton.setText(titles[i]);
            radioButton.setTextColor(0xffa800ff);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            RadioGroup.LayoutParams rl=new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip5*6);
            rl.leftMargin=dip5;
            rl.gravity=Gravity.CENTER_VERTICAL;
            rl.weight=1;
            radioButton.setLayoutParams(rl);
            radioButton.setTag(i);
            radioButton.setBackgroundResource(R.drawable.round_stroke_a800ff);
//            if(i==0)
//            {
//                radioButton.performClick()
//            }
//            else
//            {
//                radioButton.setChecked(false);
//            }
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    compoundButton.setTextColor(b?0xffffffff:0xffa800ff);
                    compoundButton.setBackgroundResource(b?R.drawable.round_gradient_a800ff_d689ff:R.drawable.round_stroke_a800ff);
                    if(b)
                    {
                        mBind.vpDialogMain.setCurrentItem((int)compoundButton.getTag());
                    }

                }
            });
            mBind.rgTabs.addView(radioButton);
        }

        ((RadioButton)mBind.rgTabs.getChildAt(0)).setChecked(true);
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
        mBind.rllContent.startAnimation(animation);
    }
}
