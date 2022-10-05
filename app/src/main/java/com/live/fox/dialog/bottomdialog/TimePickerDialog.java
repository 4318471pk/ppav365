package com.live.fox.dialog.bottomdialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jzxiang.pickerview.TimeWheel;
import com.jzxiang.pickerview.config.PickerConfig;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.data.WheelCalendar;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogTimePickerBinding;
import com.live.fox.dialog.RxBaseDialog;

import java.util.Calendar;

/**
 * Created by jzxiang on 16/4/19.
 */
public class TimePickerDialog extends BaseBindingDialogFragment {
    PickerConfig mPickerConfig;
    private TimeWheel mTimeWheel;
    private long mCurrentMillSeconds;
    DialogTimePickerBinding mBind;

    private static TimePickerDialog newIntance(PickerConfig pickerConfig) {
        TimePickerDialog timePickerDialog = new TimePickerDialog();
        timePickerDialog.initialize(pickerConfig);
        return timePickerDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }


    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
            case R.id.tvCancel:
                dismissAllowingStateLoss();
                break;
            case R.id.tvConfirm:
                int year=mTimeWheel.getCurrentYear();
                int month=mTimeWheel.getCurrentMonth()-1;
                int date=mTimeWheel.getCurrentDay();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_time_picker;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mPickerConfig=  new Builder()
                .setYearText("")
                .setMonthText("")
                .setDayText("")
                .setHourText("")
                .setMinuteText("")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(0xff404040)
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSize(12)
                .build();

        mTimeWheel = new TimeWheel(view, mPickerConfig);
    }

    private void initialize(PickerConfig pickerConfig) {
        mPickerConfig = pickerConfig;
    }


    /*
     * @desc This method returns the current milliseconds. If current milliseconds is not set,
     *       this will return the system milliseconds.
     * @param none
     * @return long - the current milliseconds.
     */
    public long getCurrentMillSeconds() {
        if (mCurrentMillSeconds == 0)
            return System.currentTimeMillis();

        return mCurrentMillSeconds;
    }


    public static class Builder {
        PickerConfig mPickerConfig;

        public Builder() {
            mPickerConfig = new PickerConfig();
        }

        public TimePickerDialog.Builder setType(Type type) {
            mPickerConfig.mType = type;
            return this;
        }

        public TimePickerDialog.Builder setThemeColor(int color) {
            mPickerConfig.mThemeColor = color;
            return this;
        }

        public TimePickerDialog.Builder setCancelStringId(String left) {
            mPickerConfig.mCancelString = left;
            return this;
        }

        public TimePickerDialog.Builder setSureStringId(String right) {
            mPickerConfig.mSureString = right;
            return this;
        }

        public TimePickerDialog.Builder setTitleStringId(String title) {
            mPickerConfig.mTitleString = title;
            return this;
        }

        public TimePickerDialog.Builder setToolBarTextColor(int color) {
            mPickerConfig.mToolBarTVColor = color;
            return this;
        }

        public TimePickerDialog.Builder setWheelItemTextNormalColor(int color) {
            mPickerConfig.mWheelTVNormalColor = color;
            return this;
        }

        public TimePickerDialog.Builder setWheelItemTextSelectorColor(int color) {
            mPickerConfig.mWheelTVSelectorColor = color;
            return this;
        }

        public TimePickerDialog.Builder setWheelItemTextSize(int size) {
            mPickerConfig.mWheelTVSize = size;
            return this;
        }

        public TimePickerDialog.Builder setCyclic(boolean cyclic) {
            mPickerConfig.cyclic = cyclic;
            return this;
        }

        public TimePickerDialog.Builder setMinMillseconds(long millseconds) {
            mPickerConfig.mMinCalendar = new WheelCalendar(millseconds);
            return this;
        }

        public TimePickerDialog.Builder setMaxMillseconds(long millseconds) {
            mPickerConfig.mMaxCalendar = new WheelCalendar(millseconds);
            return this;
        }

        public TimePickerDialog.Builder setCurrentMillseconds(long millseconds) {
            mPickerConfig.mCurrentCalendar = new WheelCalendar(millseconds);
            return this;
        }

        public TimePickerDialog.Builder setYearText(String year){
            mPickerConfig.mYear = year;
            return this;
        }

        public TimePickerDialog.Builder setMonthText(String month){
            mPickerConfig.mMonth = month;
            return this;
        }

        public TimePickerDialog.Builder setDayText(String day){
            mPickerConfig.mDay = day;
            return this;
        }

        public TimePickerDialog.Builder setHourText(String hour){
            mPickerConfig.mHour = hour;
            return this;
        }

        public TimePickerDialog.Builder setMinuteText(String minute){
            mPickerConfig.mMinute = minute;
            return this;
        }

        public TimePickerDialog.Builder setCallBack(OnDateSetListener listener) {
            mPickerConfig.mCallBack = listener;
            return this;
        }

        public PickerConfig build() {
            return mPickerConfig;
        }

    }


}
