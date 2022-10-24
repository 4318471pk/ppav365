package com.live.fox.ui.agency;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.AgencyGameDetailAdapter;

import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityAgencyGameDetailBinding;


import java.util.ArrayList;
import java.util.List;


public class AgencyGameDetailActivity extends BaseBindingViewActivity {

    ActivityAgencyGameDetailBinding mBind;

    AgencyGameDetailAdapter mAdapter;
    List<String> mData = new ArrayList<>();

    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, AgencyGameDetailActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_agency_game_detail;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();

        setHeadGone();
        mBind.ivHeadLeft.setOnClickListener(view -> {finish();});

        mData.add("1"); mData.add(""); mData.add(""); mData.add(""); mData.add("1");mData.add(""); mData.add("1");
        mAdapter = new AgencyGameDetailAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rc.setLayoutManager(layoutManager);
        mBind.rc.setAdapter(mAdapter);

    }
}
