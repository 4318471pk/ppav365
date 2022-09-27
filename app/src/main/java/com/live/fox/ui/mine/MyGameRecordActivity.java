package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.LotteryTypeHistoryBean;
import com.live.fox.server.Api_Cp;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.view.GamePopupWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 游戏记录
 */
public class MyGameRecordActivity extends BaseActivity
        implements View.OnClickListener {

    private ImageView ivHeadLeft;
    private TextView tvHeadRight;
    private TextView tvTzzje;
    private TextView tvZjzje;
    private RecyclerView rv;
    private SmartRefreshLayout refreshLayout;

    private long uid;
    private RelativeLayout re_root;
    BaseQuickAdapter adapter;
    private GamePopupWindow popupWindow;
    private int type = 0;
    private String currentDataString = "";
    List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_record);
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);

        if (getIntent() != null) {
            uid = getIntent().getLongExtra("uid", 0);
        }
        setView();
        initData();
        setTopPaddingStatusBarHeight(re_root);
    }

    public void initData() {
        datas = new ArrayList<>();
        datas.add(getString(R.string.erban));
        datas.add(getString(R.string.yesBan));
        String date = "3" + getString(R.string.date_day);
        datas.add(date);
        date = "7" + getString(R.string.date_day);
        datas.add(date);
        date = "30" + getString(R.string.date_day);
        datas.add(date);
        currentDataString = datas.get(0);
        getLotteryResultHistory();
    }

    public void setView() {
        ivHeadLeft = findViewById(R.id.iv_head_left);
        tvHeadRight = findViewById(R.id.tv_head_right);
        tvTzzje = findViewById(R.id.tv_tzzje);
        tvZjzje = findViewById(R.id.tv_zjzje);
        rv = findViewById(R.id.record_game_recycler);
        refreshLayout = findViewById(R.id.refreshLayout);
        re_root = findViewById(R.id.re_root);
        findViewById(R.id.iv_head_left).setOnClickListener(this);
        findViewById(R.id.tv_head_right).setOnClickListener(this);

        setRecycleView();
        popupWindow = new GamePopupWindow(this);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            getLotteryResultHistory();
        });
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_one, new ArrayList<LotteryTypeHistoryBean.ResultListBean>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                LotteryTypeHistoryBean.ResultListBean bean = (LotteryTypeHistoryBean.ResultListBean) item;
                GlideUtils.loadDefaultCircleImage(MyGameRecordActivity.this, bean.getLotteryIcon(), helper.getView(R.id.iv_portrait));
                helper.setText(R.id.tv_gameName, bean.getNickName());
                helper.setText(R.id.tv_lotteryCount, bean.getLotteryCount() + "");
                helper.setText(R.id.tv_betAmountAll, bean.getBetAmountAll() + "");
                helper.setText(R.id.tv_profitAmountAll, RegexUtils.westMoney(bean.getProfitAmountAll()));
            }
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            LotteryTypeHistoryBean.ResultListBean bean = (LotteryTypeHistoryBean.ResultListBean) adapter.getData().get(position);
            MyTouzuRecordActivity.startActivity(MyGameRecordActivity.this, uid, bean.getLotteryName(), type);
        });
    }

    public void getLotteryResultHistory() {
        showLoadingDialog();
        Api_Cp.ins().getLotteryResultHistory(uid, type, new JsonCallback<LotteryTypeHistoryBean>() {
            @Override
            public void onSuccess(int code, String msg, LotteryTypeHistoryBean data) {
                hideLoadingDialog();
                refreshLayout.finishRefresh();

                if (popupWindow != null) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }

                if (code == 0) {
                    adapter.getData().clear();
                    if (data != null && data.getResultList() != null && data.getResultList().size() > 0) {
                        adapter.setNewData(data.getResultList());
                        tvTzzje.setText(getString(R.string.total_bet) + data.getTotalBet());
                        String winStr = getString(R.string.total_winnings) + (int) data.getTotalProfit();
                        tvZjzje.setText(winStr);
                    } else {
                        setEmptyView(getString(R.string.noData));
                        tvTzzje.setText(getString(R.string.total_bet) + "0");
                        tvZjzje.setText(getString(R.string.total_winnings) + "0");
                    }
                } else {
                    setEmptyView(msg);
                }
            }
        });
    }

    private void setEmptyView(String msg) {
        ViewGroup parent = (ViewGroup) rv.getParent();
        View emptyView = LayoutInflater.from(this).inflate(R.layout.view_empty, parent, false);
        if (emptyView.getParent() != null) {
            parent.removeView(emptyView);
        }
        TextView textView = emptyView.findViewById(R.id.tv_empty);
        textView.setText(msg);
        adapter.setEmptyView(emptyView);
        adapter.notifyDataSetChanged();
    }

    public void setTopPaddingStatusBarHeight(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height += BarUtils.getStatusBarHeight();
        view.setLayoutParams(layoutParams);
        view.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
    }

    public static void startActivity(Context context, long uid) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MyGameRecordActivity.class);
        i.putExtra("uid", uid);
        context.startActivity(i);
    }

    private void showPopWindow() {
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            popupWindow.showAsDropDown(re_root);
            popupWindow.setDatas(datas);
            popupWindow.setOnItemClickListener(position -> {
                if (2 == position) {
                    type = 3;
                } else if (3 == position) {
                    type = 2;
                } else {
                    type = position;
                }
                currentDataString = datas.get(position);
                getLotteryResultHistory();
            });
            popupWindow.setOnDismissClickListener(() -> {
                Drawable drawable = getResources().getDrawable(R.drawable.right_arrowa);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvHeadRight.setText(currentDataString);
                tvHeadRight.setCompoundDrawables(null, null, drawable, null);
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head_left:
                Constant.isAppInsideClick = true;
                finish();
                break;
            case R.id.tv_head_right:
                Drawable drawable = getResources().getDrawable(R.drawable.right_arrowb);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvHeadRight.setCompoundDrawables(null, null, drawable, null);
                showPopWindow();
                break;
        }
    }
}
