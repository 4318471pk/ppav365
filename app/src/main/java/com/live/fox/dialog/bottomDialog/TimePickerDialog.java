package com.live.fox.dialog.bottomDialog;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.jzxiang.pickerview.TimeWheel;
import com.jzxiang.pickerview.config.PickerConfig;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.data.WheelCalendar;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogTimePickerBinding;

import java.util.Calendar;

/**
 * Created by jzxiang on 16/4/19.
 */
public class TimePickerDialog extends BaseBindingDialogFragment {
    PickerConfig mPickerConfig;
    private TimeWheel mTimeWheel;
    private long mCurrentMillSeconds;
    DialogTimePickerBinding mBind;
    String title;
    OnSelectedListener onSelectedListener;

    public static TimePickerDialog getInstance() {
        TimePickerDialog timePickerDialog = new TimePickerDialog();
        return timePickerDialog;
    }

    public static TimePickerDialog getInstance(String title) {
        TimePickerDialog timePickerDialog = new TimePickerDialog();
        timePickerDialog.title=title;
        return timePickerDialog;
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
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

                Calendar calendar = Calendar.getInstance();
                calendar.clear();

                calendar.set(Calendar.YEAR, mTimeWheel.getCurrentYear());
                calendar.set(Calendar.MONTH, mTimeWheel.getCurrentMonth() - 1);
                calendar.set(Calendar.DAY_OF_MONTH, mTimeWheel.getCurrentDay());
                calendar.set(Calendar.HOUR_OF_DAY, mTimeWheel.getCurrentHour());
                calendar.set(Calendar.MINUTE, mTimeWheel.getCurrentMinute());

                mCurrentMillSeconds = calendar.getTimeInMillis();

                if(onSelectedListener!=null)
                {
                    onSelectedListener.onSelected(year,month,date,mCurrentMillSeconds);
                }
                dismissAllowingStateLoss();
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

        if(!TextUtils.isEmpty(title))
        {
            mBind.tvTitle.setText(title);
        }

        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mPickerConfig=  new Builder()
                .setYearText("")
                .setMonthText("")
                .setDayText("")
                .setHourText("")
                .setMinuteText("")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis()-tenYears*4)
                .setMaxMillseconds(System.currentTimeMillis())
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

    public interface OnSelectedListener
    {
        void onSelected(int year,int month,int date,long time);
    }
}
