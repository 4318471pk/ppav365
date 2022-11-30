package com.live.fox.ui.agency;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.AgencySaveRecordAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.FragmentAgencySaveRecordBinding;
import com.live.fox.dialog.bottomDialog.TimePickerDialog;
//import com.live.fox.dialog.bottomdialog.TimePickerDialog;
import com.live.fox.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class AgencySaveRecordFragment extends BaseBindingFragment {

    FragmentAgencySaveRecordBinding mBind;
    private boolean isOut = true;
    private boolean isStartTime = true;
    private int dayType = 1;

    TimePickerDialog timePickerDialog;

    AgencySaveRecordAdapter agencySaveRecordAdapter;
    List<String> mData = new ArrayList<>();


    public static AgencySaveRecordFragment newInstance(boolean isOut) {
        AgencySaveRecordFragment fragment = new AgencySaveRecordFragment();
        Bundle args = new Bundle();
        args.putBoolean("isOut", isOut);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClickView(View view) {
        if (view == mBind.tvToday) {
            changeDays(1);
        } else if (view == mBind.tvYesterday) {
            changeDays(2);
        } else if (view == mBind.tvThisWeek) {
            changeDays(3);
        } else if (view == mBind.tvLastWeek) {
            changeDays(4);
        } else if (view == mBind.tvThisMonth) {
            changeDays(5);
        } else if (view == mBind.tvLastMonth) {
            changeDays(6);
        } else if (view == mBind.tvStartTime) {
            isStartTime = true;
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(this.getActivity().getSupportFragmentManager(),timePickerDialog);
        } else if (view == mBind.tvEndTime) {
            isStartTime = false;
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(this.getActivity().getSupportFragmentManager(),timePickerDialog);
        }



    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_agency_save_record;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();
        mBind.setClick(this);
        isOut = getArguments().getBoolean("isOut");
        setDate();
        timePickerDialog = TimePickerDialog.getInstance(getString(R.string.choice_ymd));
        timePickerDialog.setOnSelectedListener(new TimePickerDialog.OnSelectedListener() {
            @Override
            public void onSelected(int year, int month, int date, long time,TimePickerDialog dialog) {
                String s = year + "/" + month + "/" + date;
                if (isStartTime) {
                    mBind.tvStartTime.setText(s);
                } else {
                    mBind.tvEndTime.setText(s);
                }
            }
        });

        mData.add("1"); mData.add(""); mData.add(""); mData.add(""); mData.add("1");
        agencySaveRecordAdapter = new AgencySaveRecordAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rc.setLayoutManager(layoutManager);
        mBind.rc.setAdapter(agencySaveRecordAdapter);


    }

    private void changeDays(int type){
        if (dayType == type) return;
        dayType = type;
        if (dayType == 1) {
            mBind.tvToday.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff));
            mBind.tvYesterday.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisWeek.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastWeek.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisMonth.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastMonth.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
        } else if (dayType == 2) {
            mBind.tvToday.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvYesterday.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff));
            mBind.tvThisWeek.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastWeek.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisMonth.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastMonth.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
        } else if (dayType == 3) {
            mBind.tvToday.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvYesterday.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisWeek.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff));
            mBind.tvLastWeek.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisMonth.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastMonth.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
        } else if (dayType == 4) {
            mBind.tvToday.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvYesterday.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisWeek.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastWeek.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff));
            mBind.tvThisMonth.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastMonth.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
        } else if (dayType == 5) {
            mBind.tvToday.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvYesterday.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisWeek.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastWeek.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisMonth.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff));
            mBind.tvLastMonth.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
        } else if (dayType == 6) {
            mBind.tvToday.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvYesterday.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisWeek.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastWeek.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisMonth.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastMonth.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff));
        }
    }

    private void setDate(){
        mBind.tvStartTime.setText(TimeUtils.getToday());
        mBind.tvEndTime.setText(TimeUtils.getToday());
    }

}
