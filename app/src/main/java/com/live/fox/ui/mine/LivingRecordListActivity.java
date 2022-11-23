package com.live.fox.ui.mine;

import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

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
import com.live.fox.entity.LivingRecordListBean;
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
import java.util.List;

public class LivingRecordListActivity extends BaseBindingViewActivity {

    ActivityRecordListBinding mBind;
    LivingRecordListAdapter livingRecordListAdapter;
    final int pageSize = 20;
    int pageIndex = 0;
    int status=-1;
    List<LivingRecordListBean> beans=new ArrayList<>();

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
                long time1=(Long)mBind.tvStartTime.getTag();
                long time2=(Long)mBind.tvEndTime.getTag();
                pageIndex=0;
                requestData(time1, time2,status);
            }
        });
        mBind.stlRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                long time1=(Long)mBind.tvStartTime.getTag();
                long time2=(Long)mBind.tvEndTime.getTag();
                pageIndex++;
                requestData(time1, time2,status);
            }
        });
        mBind.rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.rbUnPay:
                        status=1;
                        break;
                    case R.id.rbPaid:
                        status=0;
                        break;
                }
                long time1=(Long)mBind.tvStartTime.getTag();
                long time2=(Long)mBind.tvEndTime.getTag();
                requestData(time1, time2,status);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBind.rvMain.setLayoutManager(linearLayoutManager);
        mBind.rvMain.setAdapter(new LivingRecordListAdapter(this, beans));

        Date endDate = TimeUtils.getTodaEndTime();
        Date beginDate = TimeUtils.getYesterdayBeginTime();
        String endTime = TimeUtils.date2String(endDate, "yyyy/MM/dd");
        String beginTime = TimeUtils.date2String(beginDate, "yyyy/MM/dd");
        mBind.tvStartTime.setText(beginTime);
        mBind.tvStartTime.setTag(beginDate.getTime());
        mBind.tvEndTime.setText(endTime);
        mBind.tvEndTime.setTag(endDate.getTime());
        requestData(beginDate.getTime(), endDate.getTime(),status);
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

                if (isBeginTime) {
                    mBind.tvStartTime.setText(TimeUtils.long2String(time, "yyyy/MM/dd"));
                    mBind.tvStartTime.setTag(time);
                } else {
                    mBind.tvEndTime.setText(TimeUtils.long2String(time + oneDay - 1000, "yyyy/MM/dd"));
                    mBind.tvEndTime.setTag(time + oneDay - 1000);
                }
                long time1=(Long)mBind.tvStartTime.getTag();
                long time2=(Long)mBind.tvEndTime.getTag();
                pageIndex=0;
                requestData(time1, time2,status);
            }
        });
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(), timePickerDialog);
    }

    private void requestData(long beginTime, long endTime,int status) {

        Log.e("time1", TimeUtils.long2String(beginTime));
        Log.e("time2", TimeUtils.long2String(endTime));
        Api_Live.ins().livingRecordList(beginTime, endTime, pageIndex, pageSize,status, new JsonCallback<List<LivingRecordListBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<LivingRecordListBean> data) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                mBind.stlRefresh.finishRefresh(code==0);
                mBind.stlRefresh.finishLoadMore(code==0);
                if (code == 0) {

                    LivingRecordListAdapter adapter=(LivingRecordListAdapter)mBind.rvMain.getAdapter();
                    if(pageIndex==0)
                    {
                        beans.clear();
                        adapter.clear();
                    }
                    if (data != null && data.size()>0) {
                        mBind.stlRefresh.setVisibility(View.VISIBLE);
                        mBind.llEmptyData.setVisibility(View.GONE);
                        beans.addAll(data);
                        adapter.setNewData(beans);
                    }
                    else
                    {
                        if(beans.size()==0)
                        {
                            mBind.stlRefresh.setVisibility(View.GONE);
                            mBind.llEmptyData.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }
}
