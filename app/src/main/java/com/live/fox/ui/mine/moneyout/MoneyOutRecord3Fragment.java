package com.live.fox.ui.mine.moneyout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Withdraw;
import com.live.fox.server.Api_User;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.TimeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MoneyOutRecord3Fragment extends BaseHeadFragment implements View.OnClickListener {

    private TextView tvAll;

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;
    int page = 0;

    BaseQuickAdapter adapter;

    int pageType = 1; //1全部 2审核中
    List<Withdraw> withdrawList;
    private TextView tvYdz;
    private TextView tvShz;
    private TextView tvBh;

    public static MoneyOutRecord3Fragment newInstance() {
        MoneyOutRecord3Fragment fragment = new MoneyOutRecord3Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.moneyoutrecordt_fragment, container, false);
        setView(rootView);
        return rootView;
    }

    public void setView(View bindSource) {
        tvAll = bindSource.findViewById(R.id.tv_all);
        refreshLayout = bindSource.findViewById(R.id.refreshLayout);
        rv = bindSource.findViewById(R.id.rv_);
        tvYdz = bindSource.findViewById(R.id.tv_ydz);
        tvShz = bindSource.findViewById(R.id.tv_shz);
        tvBh = bindSource.findViewById(R.id.tv_bh);
        bindSource.findViewById(R.id.tv_all).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_ydz).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_shz).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_bh).setOnClickListener(this);
        initHead(bindSource, "", false);
        getToolbar().setVisibility(View.GONE);
        setRecycleView();
        refreshPage();
        doGetListApi(true);
    }

    public void refreshPage() {
        tvAll.setBackground(null);
        tvAll.setTextColor(Color.parseColor("#2A2E3F"));
        tvYdz.setBackground(null);
        tvYdz.setTextColor(Color.parseColor("#2A2E3F"));
        tvShz.setBackground(null);
        tvShz.setTextColor(Color.parseColor("#2A2E3F"));
        tvBh.setBackground(null);
        tvBh.setTextColor(Color.parseColor("#2A2E3F"));
        switch (pageType) {
            case 1: //1全部
                tvAll.setBackgroundResource(R.drawable.shape_gradual_orange_light_corners_20);
                tvAll.setTextColor(Color.WHITE);
                if (withdrawList != null) {
                    if (withdrawList.size() == 0) {
                        showEmptyView(getString(R.string.noData));
                    } else {
                        hideEmptyView();
                        adapter.setNewData(withdrawList);
                    }
                }

                break;
            case 2: //已到账
                tvYdz.setBackgroundResource(R.drawable.shape_gradual_orange_light_corners_20);
                tvYdz.setTextColor(Color.WHITE);
                if (withdrawList != null) {
                    List<Withdraw> tempList = new ArrayList<>();
                    for (int i = 0; i < withdrawList.size(); i++) {
                        if (withdrawList.get(i).getStatus() == 5 || withdrawList.get(i).getStatus() == 1) {
                            tempList.add(withdrawList.get(i));
                        }
                    }
                    LogUtils.e(tempList.size() + "");
                    if (tempList.size() == 0) {
                        showEmptyView(getString(R.string.noData));
                    } else {
                        hideEmptyView();
                        adapter.setNewData(tempList);
                    }
                }
                break;
            case 3: //审核中
                tvShz.setBackgroundResource(R.drawable.shape_gradual_orange_light_corners_20);
                tvShz.setTextColor(Color.WHITE);
                if (withdrawList != null) {
                    List<Withdraw> tempList = new ArrayList<>();
                    for (int i = 0; i < withdrawList.size(); i++) {
                        if (withdrawList.get(i).getStatus() == 0 || withdrawList.get(i).getStatus() == 3
                                || withdrawList.get(i).getStatus() == 4 || withdrawList.get(i).getStatus() == 7) {
                            tempList.add(withdrawList.get(i));
                        }
                    }
                    LogUtils.e(tempList.size() + "");
                    if (tempList.size() == 0) {
                        showEmptyView(getString(R.string.noData));
                    } else {
                        hideEmptyView();
                        adapter.setNewData(tempList);
                    }
                }
                break;
            case 4: // 驳回
                tvBh.setBackgroundResource(R.drawable.shape_gradual_orange_light_corners_20);
                tvBh.setTextColor(Color.WHITE);
                if (withdrawList != null) {
                    List<Withdraw> tempList = new ArrayList<>();
                    for (int i = 0; i < withdrawList.size(); i++) {
                        if (withdrawList.get(i).getStatus() == 2) {
                            tempList.add(withdrawList.get(i));
                        }
                    }
                    LogUtils.e(tempList.size() + "");
                    if (tempList.size() == 0) {
                        showEmptyView(getString(R.string.noData));
                    } else {
                        hideEmptyView();
                        adapter.setNewData(tempList);
                    }
                }
                break;
        }

    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_moneyoutrecord, new ArrayList<Withdraw>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                Withdraw withdraw = (Withdraw) item;
                helper.setText(R.id.tv_des, withdraw.getCardNo() + "");
                helper.setText(R.id.tv_date, TimeUtils.convertShortTime(withdraw.getGmtCreate()));
                helper.setText(R.id.tv_money, "" + RegexUtils.westMoney(withdraw.getCash()));

                if (withdraw.getStatus() == 5 || withdraw.getStatus() == 1) {
                    helper.setTextColor(R.id.tv_status, Color.parseColor("#949494"));
                    helper.setText(R.id.tv_status, getString(R.string.yifafang));
                } else {
                    helper.setTextColor(R.id.tv_status, Color.parseColor("#F68A92"));
                    if (withdraw.getStatus() == 2) {
                        helper.setText(R.id.tv_status, getString(R.string.refuse));
                    } else if (withdraw.getStatus() == 0 || withdraw.getStatus() == 3 || withdraw.getStatus() == 4 || withdraw.getStatus() == 7) {
                        helper.setText(R.id.tv_status, getString(R.string.tab_change_reviewing));
                    } else if (withdraw.getStatus() == 6) {
                        helper.setText(R.id.tv_status, getString(R.string.withdraw_failed));
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                doGetListApi(true);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                doGetListApi(false);
            }
        });
    }

    /**
     * 提现列表
     */
    public void doGetListApi(boolean isRefresh) {
        Api_User.ins().withdrawList(0, 2, new JsonCallback<List<Withdraw>>() {
            @Override
            public void onSuccess(int code, String msg, List<Withdraw> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            showEmptyView(getString(R.string.noRecord));
                        } else {
                            withdrawList = data;
                            refreshPage();
                        }
                    } else {
                        refreshLayout.finishLoadMore();
                        List<Withdraw> list = adapter.getData();
                        adapter.addData(data);
                        adapter.notifyItemRangeInserted(list.size(), data.size());
                    }
                    if (data != null && data.size() < Constant.pageSizeLater) {
                        //如果没有更多的数据 则隐藏加载更多功能
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    showEmptyView(msg);
                }

            }
        });
    }

    @Override
    public void showEmptyView(String tip) {
        super.showEmptyView(tip);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                pageType = 1;
                refreshPage();
                break;
            case R.id.tv_ydz:
                pageType = 2;
                refreshPage();
                break;
            case R.id.tv_shz:
                pageType = 3;
                refreshPage();
                break;
            case R.id.tv_bh:
                pageType = 4;
                refreshPage();
                break;
        }
    }
}

