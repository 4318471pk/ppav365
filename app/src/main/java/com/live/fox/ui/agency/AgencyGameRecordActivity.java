package com.live.fox.ui.agency;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.AgencyGameRecordAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityAgencyGameRecordBinding;
import com.live.fox.dialog.bottomDialog.TimePickerDialog;
import com.live.fox.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;


public class AgencyGameRecordActivity extends BaseBindingViewActivity {

    ActivityAgencyGameRecordBinding mBind;

    TimePickerDialog timePickerDialog;
    AgencyGameRecordAdapter mAdapter;
    List<String> mData = new ArrayList<>();

    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, AgencyGameRecordActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClickView(View view) {
        if (view == mBind.tvDate){

        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_agency_game_record;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        mBind.setClick(this);
        setHeadGone();
        mBind.ivHeadLeft.setOnClickListener(view -> {finish();});
        mBind.tvDate.setText(TimeUtils.getCurrentTime("yyyy-MM-dd"));
        timePickerDialog = TimePickerDialog.getInstance(getString(R.string.choice_ymd));
        timePickerDialog.setOnSelectedListener(
                new TimePickerDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int year, int month, int date, long time) {
                        String s = year + "-" + month + "-" + date;
                        mBind.tvDate.setText(s);
                    }
                });


        mData.add("1"); mData.add(""); mData.add(""); mData.add(""); mData.add("1");mData.add(""); mData.add("1");
        mAdapter = new AgencyGameRecordAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rc.setLayoutManager(layoutManager);
        mBind.rc.setAdapter(mAdapter);

    }
}
