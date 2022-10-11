package com.live.fox.ui.mine.contribution;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityContributionRankBinding;
import com.live.fox.ui.rank.RankActivity;
import com.live.fox.ui.rank.RankFragment;
import com.live.fox.utils.device.ScreenUtils;

public class ContributionRankActivity extends BaseBindingViewActivity {

    ActivityContributionRankBinding mBind;

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,ContributionRankActivity.class));
    }

    @Override
    public void onClickView(View view) {
        if(view.getId()==mBind.ivRank.getId())
        {
            RankActivity.startActivity(this);
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_contribution_rank;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        mBind.setClick(this);
        setActivityTitle(R.string.listOfContributionBoss);

        String titles[]=getResources().getStringArray(R.array.rank_tab_contribution);
        int widthScreen= ScreenUtils.getScreenWidth(this);
        mBind.vpMain.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager()){
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

        mBind.vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        int dip5=ScreenUtils.getDip2px(this,5);

        for (int i = 0; i < titles.length; i++) {
            RadioButton radioButton=new RadioButton(this);
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
                        mBind.vpMain.setCurrentItem((int)compoundButton.getTag());
                    }

                }
            });
            mBind.rgTabs.addView(radioButton);
        }

        ((RadioButton)mBind.rgTabs.getChildAt(0)).setChecked(true);
    }


}
