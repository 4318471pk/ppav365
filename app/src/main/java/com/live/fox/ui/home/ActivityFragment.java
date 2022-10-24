package com.live.fox.ui.home;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseFragment;
import com.live.fox.databinding.FragmentActivityBinding;
import com.live.fox.ui.act.ActivityDetailFragment;
import com.live.fox.ui.live.EmptyFragment;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends BaseBindingFragment {

    FragmentActivityBinding mBind;

    boolean isGame = true;

    List<BaseFragment> fragmentList = new ArrayList<>();

    public static ActivityFragment newInstance() {
        return new ActivityFragment();
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_activity;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        fragmentList.add(ActivityDetailFragment.newInstance(true));
        fragmentList.add(ActivityDetailFragment.newInstance(false));
        mBind.vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        mBind.vp.setCurrentItem(0);
        mBind.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position ==0) {
                    mBind.viewGame.setVisibility(View.VISIBLE);
                    mBind.viewLiving.setVisibility(View.INVISIBLE);
                } else {
                    mBind.viewGame.setVisibility(View.INVISIBLE);
                    mBind.viewLiving.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBind.layoutGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBind.viewGame.setVisibility(View.VISIBLE);
                mBind.viewLiving.setVisibility(View.INVISIBLE);
                mBind.vp.setCurrentItem(0);
            }
        });

        mBind.layoutLiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBind.viewGame.setVisibility(View.INVISIBLE);
                mBind.viewLiving.setVisibility(View.VISIBLE);
                mBind.vp.setCurrentItem(1);
            }
        });
    }
}
