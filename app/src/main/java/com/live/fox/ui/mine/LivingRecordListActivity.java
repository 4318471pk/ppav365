package com.live.fox.ui.mine;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.adapter.LivingRecordListAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityRecordListBinding;
import com.live.fox.dialog.bottomDialog.TimePickerDialog;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class LivingRecordListActivity extends BaseBindingViewActivity {

    ActivityRecordListBinding mBind;
    LivingRecordListAdapter livingRecordListAdapter;
    final int pageSize = 20;
    int pageIndex = 0;

    @Override
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tvStartTime:
                showDialog(true);
                break;
            case R.id.tvEndTime:
                showDialog(false);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_record_list;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        mBind.setClick(this);

        setActivityTitle(getString(R.string.livingRecord));
        mBind.stlRefresh.setRefreshHeader(new MyWaterDropHeader(this));
        mBind.stlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {

            }
        });
        mBind.stlRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.rvMain.setLayoutManager(linearLayoutManager);
        mBind.rvMain.setAdapter(new LivingRecordListAdapter(this, new ArrayList<>()));

        Date endDate = TimeUtils.getTodaEndTime();
        Date beginDate = TimeUtils.getYesterdayBeginTime();
        String endTime = TimeUtils.date2String(endDate, "yyyy/MM/dd");
        String beginTime = TimeUtils.date2String(beginDate, "yyyy/MM/dd");
        mBind.tvStartTime.setText(beginTime);
        mBind.tvStartTime.setTag(beginDate.getTime());
        mBind.tvEndTime.setText(endTime);
        mBind.tvEndTime.setTag(endDate.getTime());
        requestData(beginDate.getTime(), endDate.getTime());
    }

    private void showDialog(boolean isBeginTime) {
        long time1 = 0l;
        long time2 = 0l;
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        long oneDay = 24 * 60 * 60 * 1000;
        if (isBeginTime) {
            long endtime = (Long) mBind.tvEndTime.getTag();
            time1 = endtime - tenYears;
            time2 = endtime - oneDay + 1000;//减去 23小时59分59秒
        } else {
            long beginTime = (Long) mBind.tvStartTime.getTag();
            time1 = beginTime + oneDay - 1000;//加入 23小时59分59秒
            time2 = TimeUtils.getTodaEndTime().getTime();
        }

        TimePickerDialog timePickerDialog = TimePickerDialog.getInstance(getString(R.string.plzSelectedDate), time1, time2);
        timePickerDialog.setOnSelectedListener(new TimePickerDialog.OnSelectedListener() {
            @Override
            public void onSelected(int year, int month, int date, long time) {
                Log.e("time", TimeUtils.long2String(time));
                Log.e("time22", year + " " + month + " " + date);
                if (isBeginTime) {
                    mBind.tvStartTime.setText(TimeUtils.long2String(time, "yyyy/MM/dd"));
                    mBind.tvStartTime.setTag(time);
                } else {
                    mBind.tvEndTime.setText(TimeUtils.long2String(time + oneDay - 1000, "yyyy/MM/dd"));
                    mBind.tvEndTime.setTag(time + oneDay - 1000);
                }
            }
        });
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(), timePickerDialog);
    }

    private void requestData(long beginTime, long endTime) {

        Api_Live.ins().livingRecordList(beginTime, endTime, pageIndex, pageSize, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                if (code == 0) {
                    if (data != null) {
                        Log.e("requestData", data);
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }
}
