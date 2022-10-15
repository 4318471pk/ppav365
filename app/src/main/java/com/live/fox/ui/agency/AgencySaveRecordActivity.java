package com.live.fox.ui.agency;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityAgencySaveRecordBinding;
import com.live.fox.ui.mine.noble.NobleNewFragment;


import java.util.ArrayList;
import java.util.List;

public class AgencySaveRecordActivity extends BaseBindingViewActivity {

    ActivityAgencySaveRecordBinding mBind;
    private final List<AgencySaveRecordFragment> fragmentList = new ArrayList<>();
    private boolean isOut = true;

    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, AgencySaveRecordActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
    return R.layout.activity_agency_save_record;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();

        setHeadGone();
        mBind.ivHeadLeft.setOnClickListener(view -> {finish();});
        mBind.vp.setOffscreenPageLimit(2);

        AgencySaveRecordFragment fragment1 = AgencySaveRecordFragment.newInstance(true);
        fragmentList.add(fragment1);
        AgencySaveRecordFragment fragment2 = AgencySaveRecordFragment.newInstance(true);
        fragmentList.add(fragment2);

        mBind.vp.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

        });


        mBind.layoutOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOut) return;
                isOut = true;
                mBind.viewOut.setVisibility(View.VISIBLE);
                mBind.viewIn.setVisibility(View.GONE);
                mBind.vp.setCurrentItem(0);
            }
        });

        mBind.layoutIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOut) return;
                isOut = false;
                mBind.viewOut.setVisibility(View.GONE);
                mBind.viewIn.setVisibility(View.VISIBLE);
                mBind.vp.setCurrentItem(1);
            }
        });

    }
}
