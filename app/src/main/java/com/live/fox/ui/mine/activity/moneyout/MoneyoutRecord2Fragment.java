package com.live.fox.ui.mine.activity.moneyout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.ExchangeCoin;
import com.live.fox.entity.WithdrawForShare;
import com.live.fox.server.Api_Promotion;
import com.live.fox.server.Api_User;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.TimeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class MoneyoutRecord2Fragment extends BaseFragment {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;

    BaseQuickAdapter adapter;
    int page = 0;
    int pageType = 1; //1直播收益提现记录  3.分享收益兑换记录
    List<WithdrawForShare> withdrawForShareList;


    public static MoneyoutRecord2Fragment newInstance(int pageType) {
        MoneyoutRecord2Fragment fragment = new MoneyoutRecord2Fragment();
        Bundle args = new Bundle();
        args.putInt("pageType", pageType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_recycle_view_with_refresh, null, false);
        initData(getArguments());
        setView(rootView);
        return rootView;
    }

    public void initData(Bundle bundle) {
        if (bundle != null) {
            pageType = bundle.getInt("pageType");
        }
    }

    public void setView(View bindSource) {
        refreshLayout = bindSource.findViewById(R.id.refreshLayout);
        rv = bindSource.findViewById(R.id.rv_);

        setRecycleView();
        if (pageType == 3) {
            doGetListApiForShare(true);
        } else {
            doGetListApi(true);
        }
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        if (pageType == 3) {
            rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_exchangemoney, new ArrayList<WithdrawForShare>()) {
                @Override
                protected void convert(BaseViewHolder helper, Object item) {
                    WithdrawForShare withdrawForShare = (WithdrawForShare) item;

                    long exchange = withdrawForShare.getAmount();
                    SpanUtils spanUtils = new SpanUtils();
                    spanUtils.append("-" + exchange).setForegroundColor(Color.parseColor("#F0668A"));
                    spanUtils.append(getString(R.string.changeIntoGold)).setForegroundColor(Color.BLACK);

                    helper.setText(R.id.tv_des, spanUtils.create());
                    helper.setText(R.id.tv_data, TimeUtils.convertShortTime(withdrawForShare.getUpdateTime()));
                    helper.setText(R.id.tv_exchangemoney, "+" + exchange);
                    helper.getView(R.id.tv_money).setVisibility(View.GONE);
                }
            });
        } else {
            rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_exchangemoney, new ArrayList<ExchangeCoin>()) {
                @Override
                protected void convert(BaseViewHolder helper, Object item) {
                    ExchangeCoin exchangeCoin = (ExchangeCoin) item;

                    long exchange = exchangeCoin.getAnchorCoin();
                    SpanUtils spanUtils = new SpanUtils();
                    spanUtils.append("-" + exchange).setForegroundColor(Color.parseColor("#F0668A"));
                    spanUtils.append(getString(R.string.changeIntoGold)).setForegroundColor(Color.BLACK);

                    helper.setText(R.id.tv_des, spanUtils.create());
                    helper.setText(R.id.tv_data, TimeUtils.convertShortTime(exchangeCoin.getCreateTime()));
                    helper.setText(R.id.tv_exchangemoney, "+" + exchangeCoin.getResultCoin());
                    helper.setText(R.id.tv_money, exchangeCoin.getResultCoin() + "");

                }
            });
        }

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                if (pageType == 3) {
//                    tvInreview.setText("待审核");
                    doGetListApiForShare(true);
                } else {
                    doGetListApi(true);
                }
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                if (pageType == 3) {
//                    tvInreview.setText("待审核");
                    doGetListApiForShare(false);
                } else {
                    doGetListApi(false);
                }
            }
        });
    }


    /**
     * 我的兑换金币列表
     */
    public void doGetListApi(boolean isRefresh) {
        Api_User.ins().getChangeCoinList(0, new JsonCallback<List<ExchangeCoin>>() {
            @Override
            public void onSuccess(int code, String msg, List<ExchangeCoin> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            showEmptyView(getString(R.string.noData));
                        } else {
                            adapter.setNewData(data);
                        }
                    } else {
                        refreshLayout.finishLoadMore();
                        List<ExchangeCoin> list = adapter.getData();
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


    /**
     * 兑换金币-分享收益
     */
    public void doGetListApiForShare(boolean isRefresh) {
        Api_Promotion.ins().withdrawlog(0, 0, new JsonCallback<List<WithdrawForShare>>() {
            @Override
            public void onSuccess(int code, String msg, List<WithdrawForShare> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            withdrawForShareList = data;
                            adapter.setNewData(data);
                        } else {
                            adapter.setNewData(data);
                        }
                    } else {
                        refreshLayout.finishLoadMore();
                        List<WithdrawForShare> list = adapter.getData();
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


}

