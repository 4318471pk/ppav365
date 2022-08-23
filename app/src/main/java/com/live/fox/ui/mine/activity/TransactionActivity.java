package com.live.fox.ui.mine.activity;

import static com.live.fox.entity.TransactionEntity.CenterUserAssetsPlusVOSDTO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.TransactionAdapter;
import com.live.fox.adapter.devider.DividerItemDecoration;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.FilterListDialog;
import com.live.fox.entity.FilterDialogEntity;
import com.live.fox.entity.FilterItemEntity;
import com.live.fox.entity.TransactionEntity;
import com.live.fox.entity.UserAssetRecord;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.RegexUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易记录
 * 用户交易记录页面
 */
public class TransactionActivity extends BaseActivity {
    //View
    private TextView filterType;
    private TextView filterTime;
    private TransactionAdapter adapter;
    private RecyclerView recyclerView;
    private SmartRefreshLayout refreshLayout;
    private TextView headSpend;
    private TextView headIncome;
    private TextView headTime;
    private TextView changeStyleText;

    private FilterListDialog typeDialog; //类型过滤弹窗
    private FilterListDialog timeDialog; //时间过滤弹窗

    private final FilterDialogEntity dialogTypeEntity = new FilterDialogEntity();
    private final FilterDialogEntity dialogTimeEntity = new FilterDialogEntity();
    private final UserAssetRecord userAssetRecord = new UserAssetRecord();

    private int page = 0;
    private boolean isRefresh = true;
    private View tabBg;

    public static void launch(Context context) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, TransactionActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        BarUtils.setStatusBarLightMode(this, false);

        findViewById(R.id.common_title_back).setOnClickListener(view -> onBack());
        TextView title = findViewById(R.id.common_title_title);
        title.setText(getString(R.string.transaction_title));

        filterType = findViewById(R.id.transaction_filter_type);
        filterTime = findViewById(R.id.transaction_filter_time);
        headTime = findViewById(R.id.transaction_head_time);

        tabBg = findViewById(R.id.transaction_tab_layout_bg);

        recyclerView = findViewById(R.id.refresh_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration());
        refreshLayout = findViewById(R.id.refresh_refresh_layout);

        headSpend = findViewById(R.id.transaction_head_spend);
        headIncome = findViewById(R.id.transaction_head_income);

        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);
        initData();
        requestRemoteData();
        showFilterDialog();
    }

    /**
     * 显示过滤弹窗
     */
    private void showFilterDialog() {
        filterType.setOnClickListener(view -> {
            changeStyleText = filterType;
            if (timeDialog.isAdded()) {
                timeDialog.dismiss();
                return;
            }
            setTextStyle(true);
            typeDialog.show(getSupportFragmentManager(), "type dialog");
        });

        filterTime.setOnClickListener(view -> {
            changeStyleText = filterTime;
            if (typeDialog.isAdded()) {
                typeDialog.dismiss();
                return;
            }
            setTextStyle(true);
            timeDialog.show(getSupportFragmentManager(), "time dialog");
        });
    }

    public void setTextStyle(boolean unfold) {
        Drawable arrow = ContextCompat.getDrawable(this,
                unfold ? R.drawable.ic_arrow_down_solid_white : R.drawable.ic_arrow_solid);
        Drawable bg = ContextCompat.getDrawable(this,
                unfold ? R.drawable.shape_light_red_corners_20 : R.drawable.shape_light_gray_corners_20);
        int color = ContextCompat.getColor(this, unfold ? R.color.white : R.color.textColor_alert_title);
        changeStyleText.setTextColor(color);
        changeStyleText.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, arrow, null);
        changeStyleText.setBackground(bg);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tabBg.post(() -> {
            dialogTypeEntity.setPosition(tabBg.getBottom());
            dialogTimeEntity.setPosition(tabBg.getBottom());
        });

        userAssetRecord.setPage(page);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            CenterUserAssetsPlusVOSDTO assets = (CenterUserAssetsPlusVOSDTO) adapter.getData().get(position);
            if (assets != null) {
                TransactionDetailActivity.launch(TransactionActivity.this, assets);
            }
        });

        //刷新
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            isRefresh = true;
            requestTransaction();
        });

        //加载更多
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            isRefresh = false;
            requestTransaction();
        });
    }

    /**
     * 请求远程数据
     */
    private void requestRemoteData() {
        Api_User.ins().requestAssetType(new JsonCallback<List<FilterItemEntity>>() {
            @Override
            public void onSuccess(int code, String msg, List<FilterItemEntity> data) {
                if (data == null || data.size() == 0) {
                    finish();
                    return;
                }
                setDialogData(data);
            }
        });
    }

    /**
     * 设置弹窗数据
     *
     * @param data 后台请求的弹窗数据
     */
    private void setDialogData(List<FilterItemEntity> data) {
        typeDialog = new FilterListDialog();
        timeDialog = new FilterListDialog();

        Bundle type = new Bundle();
        List<FilterItemEntity> typeList = new ArrayList<>();
        List<FilterItemEntity> timeList = new ArrayList<>();
        for (FilterItemEntity filter : data) {
            if (filter.getQueryType() == 1) {
                typeList.add(filter);
            } else {
                timeList.add(filter);
            }
        }

        dialogTypeEntity.setFilterItems(typeList);
        dialogTimeEntity.setFilterItems(timeList);
        type.putParcelable(FilterListDialog.FILTER_DIALOG_POSITION, dialogTypeEntity);
        typeDialog.setArguments(type);

        //时间
        Bundle args = new Bundle();
        args.putParcelable(FilterListDialog.FILTER_DIALOG_POSITION, dialogTimeEntity);
        timeDialog.setArguments(args);

        userAssetRecord.setType(typeList.get(0).getType());
        userAssetRecord.setTimeType(timeList.get(0).getType());

        filterType.setText(typeList.get(0).getName());
        filterTime.setText(timeList.get(0).getName());
        headTime.setText(timeList.get(0).getName());
        filterType.setVisibility(View.VISIBLE);
        filterTime.setVisibility(View.VISIBLE);

        requestTransaction();
        setTotal(false, "-.-", "-.-");
    }

    /**
     * 请求页面数据
     */
    private void requestTransaction() {
        if (isRefresh) {
            page = 0;
        } else {
            page++;
        }

        userAssetRecord.setPage(page);
        Api_User.ins().requestAssetList(userAssetRecord, new JsonCallback<TransactionEntity>() {
            @Override
            public void onSuccess(int code, String msg, TransactionEntity data) {
                //结束刷新或者加载更多
                if (isRefresh) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }

                //设置空试图
                if (setEmptyView(data)) return;

                if (data.getCenterUserAssetsPlusVOS().size() > 0) {
                    if (isRefresh) {
                        adapter.setNewData(data.getCenterUserAssetsPlusVOS());
                    } else {
                        adapter.addData(data.getCenterUserAssetsPlusVOS());
                    }
                }

                double expenditure = data.getTotalExpenditure();
                double totalIncome = data.getTotalIncome();
                setTotal(true, RegexUtils.westMoney(expenditure), RegexUtils.westMoney(totalIncome));
            }
        });
    }

    private void setTotal(boolean isNeedColor, String spendStr, String incomeStr) {
        String spend = getString(R.string.transaction_spend) + spendStr.replace("-", "").replace("+", "");
        String income = getString(R.string.transaction_income) + incomeStr.replace("-", "").replace("+", "");

        if (isNeedColor) {
            headSpend.setText(getSpannable(true, spend));
            headIncome.setText(getSpannable(false, income));
        } else {
            headSpend.setText(spend);
            headIncome.setText(income);
        }
    }

    /**
     * 设置文本颜色
     *
     * @param isSpend 是否是花费
     * @param str     文本
     * @return 返回Spannable
     */
    private SpannableStringBuilder getSpannable(boolean isSpend, String str) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(str);
        int color;
        int start;
        if (isSpend) {
            color = getResources().getColor(R.color.colorRed);
            start = getString(R.string.transaction_spend).length();
        } else {
            color = getResources().getColor(R.color.colorGreen);
            start = getString(R.string.transaction_income).length();
        }
        stringBuilder.setSpan(new ForegroundColorSpan(color), start, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return stringBuilder;
    }

    /**
     * 设置空试图
     *
     * @param data 请求数据
     * @return 判断是为空
     */
    @SuppressLint("NotifyDataSetChanged")
    private boolean setEmptyView(TransactionEntity data) {
        if (data == null || data.getCenterUserAssetsPlusVOS() == null || data.getCenterUserAssetsPlusVOS().size() == 0) {
            if (!isRefresh) return true;

            adapter.getData().clear();
            ViewGroup parent = (ViewGroup) recyclerView.getParent();
            View emptyView = LayoutInflater.from(TransactionActivity.this).inflate(R.layout.view_empty, parent, false);

            if (emptyView.getParent() != null) {
                parent.removeView(emptyView);
            }

            adapter.setEmptyView(emptyView);
            adapter.notifyDataSetChanged();
            setTotal(false, "--", "--");
            return true;
        }
        return false;
    }

    public void onFilterClick(FilterItemEntity itemEntity) {
        if (itemEntity.getQueryType() == 1) {
            changeStyleText = filterType;
            userAssetRecord.setType(itemEntity.getType());
            typeDialog.dismiss();
            filterType.setText(itemEntity.getName());
            setTextStyle(false);
        } else {
            changeStyleText = filterTime;
            String name = itemEntity.getName();
            headTime.setText(name);
            filterTime.setText(name);
            userAssetRecord.setTimeType(itemEntity.getType());
            setTextStyle(false);
            timeDialog.dismiss();
        }
        isRefresh = true;
        requestTransaction();
    }
}