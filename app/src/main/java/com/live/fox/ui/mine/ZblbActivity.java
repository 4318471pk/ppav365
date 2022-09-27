package com.live.fox.ui.mine;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundTextView;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.ZbjlBean;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;



public class ZblbActivity extends BaseHeadActivity {

    private TextView tvTotalTime;
    private TextView tvMoney;
    private TextView tvGift;
    private TextView tvCp;
    private LinearLayout dilaogValueLayout;
    private TextView tvFrom;
    private TextView tvTo;
    private RoundTextView tvCx;
    private RecyclerView rv;
    private SmartRefreshLayout refreshLayout;

    BaseQuickAdapter adapter;
    private Long uid;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", MultiLanguageUtils.appLocal);
    int page = 0;
    List<String> openListPos = new ArrayList<>();

    public static void startActivity(Context context, long uid) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, ZblbActivity.class);
        i.putExtra("uid", uid);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zbjl);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        if (getIntent() != null) {
            uid = getIntent().getLongExtra("uid", 0);
        }
        setView();
        doSearch(true);
    }

    public void setView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.liveRecord), true, true);

        tvTotalTime = findViewById(R.id.tv_total_time);
        tvMoney = findViewById(R.id.tv_money);
        tvGift = findViewById(R.id.tv_gift);
        tvCp = findViewById(R.id.tv_cp);
        dilaogValueLayout = findViewById(R.id.dilaog_value_layout);
        tvFrom = findViewById(R.id.tv_from);
        tvTo = findViewById(R.id.tv_to);
        tvCx = findViewById(R.id.tv_cx);
        rv = findViewById(R.id.rv_);
        refreshLayout = findViewById(R.id.refreshLayout);
        findViewById(R.id.tv_cx).setOnClickListener(this);
        findViewById(R.id.tv_from).setOnClickListener(this);
        findViewById(R.id.tv_to).setOnClickListener(this);

        tvFrom.setText(TimeUtils.getNowString(simpleDateFormat));
        tvTo.setText(TimeUtils.getNowString(simpleDateFormat));
        setRecycleView();
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getCtx());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_zbjl, new ArrayList<ZbjlBean.JsonsBean>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                ZbjlBean.JsonsBean bean = (ZbjlBean.JsonsBean) item;
                helper.setText(R.id.tv_name, bean.getNickname());
                helper.setText(R.id.tv_ud, getString(R.string.identity_id) + bean.getUid());
                helper.setText(R.id.tv_timelong, bean.getTotalstartTime());
                helper.setText(R.id.tv_money, RegexUtils.westMoney(bean.getFfml()));
                helper.setText(R.id.tv_gift, RegexUtils.westMoney(bean.getMl()));
                helper.setText(R.id.tv_cp, RegexUtils.westMoney(bean.getCp_statement()));
                helper.setText(R.id.tv_kbsj, getString(R.string.start_live) + TimeUtils.millis2String(bean.getStart_time()));
                helper.setText(R.id.tv_xbsj, getString(R.string.end_live) + TimeUtils.millis2String(bean.getEnd_time()));
                ImageView view = helper.getView(R.id.live_iv_portrait);
                Glide.with(ZblbActivity.this).load(bean.getAvatar()).into(view);
                if (openListPos.contains(helper.getLayoutPosition() + "")) {
                    helper.setImageResource(R.id.iv_isopen, R.drawable.zbjl_up);
                    helper.getView(R.id.layout_extend).setVisibility(View.VISIBLE);
                } else {
                    helper.setImageResource(R.id.iv_isopen, R.drawable.zbjl_down);
                    helper.getView(R.id.layout_extend).setVisibility(View.GONE);
                }
            }
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (openListPos.contains(position + "")) {
                openListPos.remove(position + "");
            } else {
                openListPos.add(position + "");
            }
            adapter.notifyDataSetChanged();
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page = page + 1;
            doSearch(false);
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cx:
                doSearch(true);
                break;
            case R.id.tv_from:
                showDatePickerDialog(tvFrom);
                break;
            case R.id.tv_to:
                showDatePickerDialog(tvTo);
                break;
        }
    }

    private void showDatePickerDialog(final TextView tv) {
        DatePickerDialog datePickerDialog;
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        // 此处得到选择的时间，可以进行你想要的操作
        datePickerDialog = new DatePickerDialog(getCtx(), R.style.dataPicker, (view, year, monthOfYear, dayOfMonth) -> {
            tv.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void doSearch(boolean isFresh) {
        String fromTime = tvFrom.getText().toString().trim();
        String toTime = tvTo.getText().toString().trim();
        String dayNum = TimeUtils.getFitTimeSpan2(fromTime, toTime, simpleDateFormat, 1);
        if (Integer.parseInt(dayNum) < 30) {
            doSearchApi(isFresh, uid, TimeUtils.string2Millis(fromTime, simpleDateFormat),
                    (TimeUtils.string2Millis(toTime, simpleDateFormat) + 24 * 3600 * 1000));
        } else {
            ToastUtils.showShort(getString(R.string.tips_data_30));
        }
    }

    public void doSearchApi(boolean isRefresh, Long uid, Long startTime, Long endTime) {
        showLoadingView();
        Api_User.ins().searchAnchor2(uid, startTime, endTime, page, new JsonCallback<ZbjlBean>() {
            @Override
            public void onSuccess(int code, String msg, ZbjlBean data) {
                hideLoadingView();
                if (code == 0) {
                    if (data == null) {
                        adapter.getData().clear();
                        adapter.notifyDataSetChanged();
                        showEmptyView(getString(R.string.noData));
                        tvTotalTime.setText(String.format(getString(R.string.total_live_time), "0"));
                        tvMoney.setText("0");
                        tvGift.setText("0");
                        tvCp.setText("0");
                    } else {
                        tvTotalTime.setText(String.format(getString(R.string.total_live_time), data.getHeartTime()));
                        tvMoney.setText(RegexUtils.westMoney(data.getToTalffml()));
                        tvGift.setText(RegexUtils.westMoney(data.getToTalMl()));
                        tvCp.setText(RegexUtils.westMoney(data.getToTalcpStatement()));
                        if (data.getJsons() == null) {
                            adapter.getData().clear();
                            adapter.notifyDataSetChanged();
                            showEmptyView(getString(R.string.noData));
                        } else {
                            if (isRefresh) {
                                if (data.getJsons().size() == 0) {
                                    adapter.getData().clear();
                                    adapter.notifyDataSetChanged();
                                    showEmptyView(getString(R.string.noData));
                                } else {
                                    refreshLayout.setEnableRefresh(false);
                                    refreshLayout.finishRefresh();
                                    refreshLayout.setEnableLoadMore(true);
                                    hideEmptyView();
                                    adapter.setNewData(data.getJsons());
                                }
                            } else {
                                refreshLayout.finishLoadMore();
                                List<ZbjlBean> list = adapter.getData();
                                adapter.addData(data.getJsons());
                                adapter.notifyItemRangeInserted(list.size(), data.getJsons().size());
                            }
                            if (data.getJsons().size() < Constant.pageSizeLater) {
                                //如果没有更多的数据 则隐藏加载更多功能
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                    }
                } else {
                    adapter.getData().clear();
                    adapter.notifyDataSetChanged();
                    showEmptyView(msg);
                }
            }
        });
    }
}
