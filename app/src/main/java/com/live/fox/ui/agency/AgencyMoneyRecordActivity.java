package com.live.fox.ui.agency;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.AgencyMoneyRecordAdapter;
import com.live.fox.adapter.AgencyColumnAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.ActivityAgencyMoneyRecordBinding;
import com.live.fox.dialog.bottomDialog.TimePickerDialog;
import com.live.fox.entity.ColumnBean;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class AgencyMoneyRecordActivity extends BaseBindingViewActivity {

    ActivityAgencyMoneyRecordBinding mBind;
    private int dayType = 1;
    private int monthType = 1;
    private boolean isTitle = true; //是否是佣金概览选择时间
    private boolean isStartTime = true;
   // private boolean isStartTimeTitle = true;

    TimePickerDialog timePickerDialog;

    AgencyColumnAdapter agencyColumnAdapter;
    List<ColumnBean> mColumnData = new ArrayList<>();

    AgencyMoneyRecordAdapter agencyMoneyRecordAdapter;
    List<String> mData = new ArrayList<>();

    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, AgencyMoneyRecordActivity.class);
        activity.startActivity(intent);
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
        } else if (view == mBind.tvThisWeek1) {
            changeMonth(1);
        } else if (view == mBind.tvLastWeek1) {
            changeMonth(2);
        } else if (view == mBind.tvThisMonth1) {
            changeMonth(3);
        } else if (view == mBind.tvLastMonth1) {
            changeMonth(4);
        } else if (view == mBind.tvStartTime1) {
            isTitle = true;
            isStartTime = true;
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(this.getSupportFragmentManager(),timePickerDialog);
        } else if (view == mBind.tvEndTime1) {
            isTitle = true;
            isStartTime = false;
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(this.getSupportFragmentManager(),timePickerDialog);
        } else if (view == mBind.tvStartTime2) {
            isTitle = false;
            isStartTime = true;
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(this.getSupportFragmentManager(),timePickerDialog);
        } else if (view == mBind.tvEndTime2) {
            isTitle = false;
            isStartTime = false;
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(this.getSupportFragmentManager(),timePickerDialog);
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_agency_money_record;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        mBind.setClick(this);
        setHeadGone();
        mBind.ivHeadLeft.setOnClickListener(view -> {finish();});

        timePickerDialog = TimePickerDialog.getInstance(getString(R.string.choice_ymd));
        timePickerDialog.setOnSelectedListener(
            new TimePickerDialog.OnSelectedListener() {
              @Override
              public void onSelected(int year, int month, int date, long time,TimePickerDialog dialog) {
                    String s = year + "/" + month + "/" + date;
                    if (isTitle) {
                        if (isStartTime) {
                            mBind.tvStartTime1.setText(s);
                        } else {
                            mBind.tvEndTime1.setText(s);
                        }
                    } else {
                        if (isStartTime) {
                            mBind.tvStartTime2.setText(s);
                        } else {
                            mBind.tvEndTime2.setText(s);
                         }
                    }
              }
            });

        setDate();

        mData.add("1"); mData.add(""); mData.add(""); mData.add(""); mData.add("1");
        agencyMoneyRecordAdapter = new AgencyMoneyRecordAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rc.setLayoutManager(layoutManager);
        mBind.rc.setAdapter(agencyMoneyRecordAdapter);


        agencyColumnAdapter = new AgencyColumnAdapter(mColumnData);
        setColumDataTest();
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBind.klineRc.setLayoutManager(layoutManager2);
        mBind.klineRc.setAdapter(agencyColumnAdapter);


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


    private void changeMonth(int type){
        if (monthType == type) return;
        monthType = type;
        if (monthType == 1) {
            mBind.tvThisWeek1.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff));
            mBind.tvLastWeek1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisMonth1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastMonth1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
        } else if (monthType == 2) {
            mBind.tvThisWeek1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastWeek1.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff));
            mBind.tvThisMonth1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastMonth1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
        } else if (monthType == 3) {
            mBind.tvThisWeek1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastWeek1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisMonth1.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff));
            mBind.tvLastMonth1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
        } else if (monthType == 4) {
            mBind.tvThisWeek1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastWeek1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvThisMonth1.setBackground(getResources().getDrawable(R.drawable.bg_d8bde7));
            mBind.tvLastMonth1.setBackground(getResources().getDrawable(R.drawable.bg_a800ff_d689ff));
        }
    }


    private void setDate(){
        mBind.tvStartTime2.setText(TimeUtils.getToday());
        mBind.tvEndTime2.setText(TimeUtils.getToday());
    }

    private void setColumDataTest(){
        mColumnData.add(new ColumnBean("10/01", 103));
        mColumnData.add(new ColumnBean("10/02", 4));
        mColumnData.add(new ColumnBean("10/03", 488));
        mColumnData.add(new ColumnBean("10/04", 200));
        mColumnData.add(new ColumnBean("10/05", 47));
//
//
        mColumnData.add(new ColumnBean("10/01", 955));
        mColumnData.add(new ColumnBean("10/02", 34));
        mColumnData.add(new ColumnBean("10/03", 767));
        mColumnData.add(new ColumnBean("10/04", 15));
        mColumnData.add(new ColumnBean("10/05", 455));

        mColumnData.add(new ColumnBean("10/01", 1.05f));
        mColumnData.add(new ColumnBean("10/02", 0.3f));
        mColumnData.add(new ColumnBean("10/03", 324.7f));
        mColumnData.add(new ColumnBean("10/04", 34.085f));
        mColumnData.add(new ColumnBean("10/05", 1.05f));
        getMaxMinKline();

    }

    private void getMaxMinKline(){
        float max = mColumnData.get(0).getColumn();
        float min = mColumnData.get(0).getColumn();
        for (int i = 1; i < mColumnData.size(); i++) {
            if (mColumnData.get(i).getColumn() > max) {
                max = mColumnData.get(i).getColumn();
            }
            if (mColumnData.get(i).getColumn() < min) {
                min = mColumnData.get(i).getColumn();
            }
        }
        float topLine = 0; //k线顶端的数据

        float item = max / 5;
        LogUtils.e("除法", "item: "+ item );

        //最上面的数值为0和5的倍数
        if (max <= 1) {
            if (max <= 0.1) {
                if (item > 0.01) {
                    item = 0.02f;
                } else {
                    item = 0.01f;
                }
            } else {
                if (item > 0.1) {
                    item = 0.2f;
                } else {
                    item = 0.1f;
                }
            }
            mBind.tvKlineRight2.setText(item + "");
            mBind.tvKlineRight3.setText(item * 2  + "");
            mBind.tvKlineRight4.setText(item * 3  + "");
            mBind.tvKlineRight5.setText(item * 4  + "");
        } else {
              if (max <= 10) {
                  if (item > 1) {
                      item = 2;
                  } else {
                      item = 1;
                  }
              } else {
                    String[] arr = String.valueOf(max).split("[.]");
                    int big = Integer.parseInt(arr[0]);
                    String str = "";
                    for (int i = 1; i < arr[0].length(); i++) {
                       str = str + "0";
                    }

                    int mid = Integer.parseInt("1" + str);
                    if (big / mid >= 5) {
                        item = mid *2;
                    } else {
                        item = mid ;
                    }
//                    if (big < mid) {
//                       item = mid;
//                    } else if (big == mid) {
//                        if (arr.length > 1) {
//                           int temp = Integer.parseInt(arr[1]);
//                           if (temp == 0) {
//                              item = mid;
//                           } else {
//                              item = Integer.parseInt("10" + str);
//                           }
//                       } else {
//                            item = mid;
//                        }
//                    } else {
//                       item = Integer.parseInt("10" + str);
//                    }
              }
              topLine = item*5;
                int num = (int) item;
                mBind.tvKlineRight2.setText(num + "");
                mBind.tvKlineRight3.setText(num * 2  + "");
                mBind.tvKlineRight4.setText(num * 3  + "");
                mBind.tvKlineRight5.setText(num * 4  + "");
         }

        LogUtils.e("除法", "item整数: "+ item );
        agencyColumnAdapter.setItemNum(item);
    }



}
