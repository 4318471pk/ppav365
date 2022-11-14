package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.BaseFragment;
import com.live.fox.databinding.ActivityMybagStoreBinding;

import java.util.ArrayList;
import java.util.List;

public class MyBagAndStoreActivity extends BaseBindingViewActivity {

    ActivityMybagStoreBinding mBind;


    List<BaseFragment> fragmentList = new ArrayList<>();

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context, MyBagAndStoreActivity.class));
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_mybag_store;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setHeadGone();
        mBind.ivBack.setOnClickListener(view-> finish());
       // setActivityTitle(R.string.store_package);

        fragmentList.add(MyBagAndStoreFragment.newInstance(true));
        fragmentList.add(MyBagAndStoreFragment.newInstance(false));
        mBind.vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        mBind.vp.setOffscreenPageLimit(0);
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
