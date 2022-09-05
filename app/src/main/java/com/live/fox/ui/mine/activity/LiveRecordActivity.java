package com.live.fox.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.LiveRecord;
import com.live.fox.entity.LiveRecordList;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.server.Api_Live;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 直播记录
 */
public class LiveRecordActivity extends BaseHeadActivity {

    private TextView tvDaynum;
    private TextView tvTimenum;
    private TextView tvGetmoney;
    private RecyclerView rv;

    BaseQuickAdapter adapter;

    List<String> openListPos = new ArrayList<>();

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, LiveRecordActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liverecord_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.liveRecord), true, true);

        tvDaynum = findViewById(R.id.tv_daynum);
        tvTimenum = findViewById(R.id.tv_timenum);
        tvGetmoney = findViewById(R.id.tv_getmoney);
        rv = findViewById(R.id.rv_);

        setRecycleView();
        doLiveRecordApi();
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.rv.setLayoutManager(layoutManager);
        this.rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_liverecord, new ArrayList<LiveRecordList>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                LiveRecordList data = (LiveRecordList) item;
                if (data.isEffect()) {
                    helper.setImageResource(R.id.iv_isEffect, R.drawable.liverecord_red);
                    helper.setText(R.id.tv_isEffect, getString(R.string.effective));
                    helper.setTextColor(R.id.tv_isEffect, Color.parseColor("#EE73A0"));
                } else {
                    helper.setImageResource(R.id.iv_isEffect, R.drawable.liverecord_blue);
                    helper.setText(R.id.tv_isEffect, getString(R.string.invalid));
                    helper.setTextColor(R.id.tv_isEffect, Color.parseColor("#AAD8F7"));
                }

                helper.setText(R.id.tv_startdata, TimeUtils.millis2String(data.getStartTime(), new SimpleDateFormat("dd-MM-yyyy", MultiLanguageUtils.appLocal)));
                helper.setText(R.id.tv_starttime, TimeUtils.millis2String(data.getStartTime(), new SimpleDateFormat("HH:mm", MultiLanguageUtils.appLocal)));
                helper.setText(R.id.tv_timelong, data.getLiveMinutes() + getString(R.string.minute));
                helper.setText(R.id.tv_ffsy, data.getFfml() + "");
                helper.setText(R.id.tv_giftsy, data.getMl() + "");
                helper.setText(R.id.tv_cpsy, data.getCpStatement() + "");

                LogUtils.e(helper.getLayoutPosition() + " ???11111");
                if (openListPos.contains(helper.getLayoutPosition() + "")) {
                    helper.getView(R.id.iv_isopen).setVisibility(View.INVISIBLE);
                    helper.getView(R.id.layout_extend).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.iv_isopen).setVisibility(View.VISIBLE);
                    helper.getView(R.id.layout_extend).setVisibility(View.GONE);
                }
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (openListPos.contains(position + "")) {
                LogUtils.e("包含");
                openListPos.remove(position + "");
            } else {
                LogUtils.e(" 不包含");
                openListPos.add(position + "");
            }
            LogUtils.e(openListPos.toString());
            adapter.notifyDataSetChanged();
        });
    }

    public void refreshPage(LiveRecord data) {
        tvDaynum.setText(data.getMonthDays() + "");
        tvTimenum.setText(data.getMonthTimes() + "");
        tvGetmoney.setText(RegexUtils.westMoney(data.getWeekProfit()));
        if (data.getLiveRecordList() != null) {
            adapter.setNewData(data.getLiveRecordList());
        }
    }

    public void doLiveRecordApi() {
        Api_Live.ins().liveRecordList(new JsonCallback<LiveRecord>() {
            @Override
            public void onSuccess(int code, String msg, LiveRecord data) {
                if (code == 0) {
                    refreshPage(data);
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }
}
