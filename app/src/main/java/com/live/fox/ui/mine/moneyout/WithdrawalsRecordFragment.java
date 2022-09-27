package com.live.fox.ui.mine.moneyout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.live.fox.entity.WithdrawForShare;
import com.live.fox.server.Api_Promotion;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.TimeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 提现记录
 */
public class WithdrawalsRecordFragment extends BaseHeadFragment
        implements View.OnClickListener {

    private TextView tvAll;
    private TextView tvInreview;
    private TextView tvYdz;
    private TextView tvBh;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;
    int page = 0;

    BaseQuickAdapter adapter;

    int pageType = 1; //1全部 2审核中
    int withdrawType = 1; // 1金币提现记录  3. 分享收益提现   4 家族明细

    List<Withdraw> withdrawList;
    List<WithdrawForShare> withdrawForShareList;

    public static WithdrawalsRecordFragment newInstance(int withdrawType) {
        WithdrawalsRecordFragment fragment = new WithdrawalsRecordFragment();
        Bundle args = new Bundle();
        args.putInt("withdrawType", withdrawType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_withdrawals_fragment, container, false);
        initData(getArguments());
        setView(rootView);
        return rootView;
    }

    public void initData(Bundle bundle) {
        if (bundle != null) {
            withdrawType = bundle.getInt("withdrawType");
        }
    }

    public void setView(View bindSource) {
        tvAll = bindSource.findViewById(R.id.tv_all);
        tvInreview = bindSource.findViewById(R.id.tv_inreview);
        tvYdz = bindSource.findViewById(R.id.tv_ydz);
        tvBh = bindSource.findViewById(R.id.tv_bh);
        refreshLayout = bindSource.findViewById(R.id.refreshLayout);
        rv = bindSource.findViewById(R.id.rv_);
        bindSource.findViewById(R.id.tv_all).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_inreview).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_ydz).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_bh).setOnClickListener(this);

        if (withdrawType == 1) {
            StatusBarUtil.setStatusBarFulAlpha(getActivity());
            BarUtils.setStatusBarVisibility(requireActivity(), true);
            BarUtils.setStatusBarLightMode(requireActivity(), false);
            initHead(bindSource, getString(R.string.goldTixianRecord), true, true);
        } else if (withdrawType == 4) {
            StatusBarUtil.setStatusBarFulAlpha(getActivity());
            BarUtils.setStatusBarVisibility(requireActivity(), true);
            BarUtils.setStatusBarLightMode(requireActivity(), false);
            initHead(bindSource, getString(R.string.jiazhutixian), true, true);
        } else {
            initHead(bindSource, "", false);
            getToolbar().setVisibility(View.GONE);
        }

        setRecycleView();
        refreshPage(1);
        if (withdrawType == 3) {
            tvInreview.setText(getString(R.string.daishenhe));
            doGetListApiForShare(true);
        } else if (withdrawType == 4) {
            doGetJzApi(true);
        } else {
            doGetListApi(true);
        }

        showLoadingView();
        rv.postDelayed(this::hideLoadingView, 500);
    }

    public void refreshPage(int pageType) {
        LogUtils.e(pageType + "," + withdrawType);
        tvAll.setBackground(null);
        tvAll.setTextColor(Color.parseColor("#2A2E3F"));
        tvInreview.setBackground(null);
        tvInreview.setTextColor(Color.parseColor("#2A2E3F"));
        tvYdz.setBackground(null);
        tvYdz.setTextColor(Color.parseColor("#2A2E3F"));
        tvBh.setBackground(null);
        tvBh.setTextColor(Color.parseColor("#2A2E3F"));

        switch (pageType) {
            case 1: //1全部
                tvAll.setBackgroundResource(R.drawable.shape_gradual_orange_light_corners_20);
                tvAll.setTextColor(Color.WHITE);
                if (withdrawType == 1 || withdrawType == 4) {
                    if (withdrawList != null) {
                        if (withdrawList.size() == 0) {
                            adapter.getData().clear();
                            setEmptyView(getString(R.string.noData));
                        } else {
                            hideEmptyView();
                            adapter.setNewData(withdrawList);
                        }
                    }
                } else {
                    if (withdrawForShareList != null) {
                        if (withdrawForShareList.size() == 0) {
                            adapter.getData().clear();
                            setEmptyView(getString(R.string.noData));
                        } else {
                            hideEmptyView();
                            adapter.setNewData(withdrawForShareList);
                        }
                    }
                }
                break;
            case 2: //已到账
                tvYdz.setBackgroundResource(R.drawable.shape_gradual_orange_light_corners_20);
                tvYdz.setTextColor(Color.WHITE);
                if (withdrawType == 1 || withdrawType == 4) {
                    if (withdrawList != null) {
                        List<Withdraw> tempList = new ArrayList<>();
                        for (int i = 0; i < withdrawList.size(); i++) {
                            if (withdrawList.get(i).getStatus() == 5 || withdrawList.get(i).getStatus() == 1) {
                                tempList.add(withdrawList.get(i));
                            }
                        }
                        if (tempList.size() == 0) {
                            adapter.getData().clear();
                            setEmptyView(getString(R.string.noData));
                        } else {
                            hideEmptyView();
                            adapter.setNewData(tempList);
                        }
                    }
                } else {
                    if (withdrawForShareList != null) {
                        List<WithdrawForShare> tempList2 = new ArrayList<>();
                        for (int i = 0; i < withdrawForShareList.size(); i++) {
                            if (withdrawForShareList.get(i).getType() == 0) {
                                if (withdrawList.get(i).getStatus() == 1) {
                                    tempList2.add(withdrawForShareList.get(i));
                                }
                            }
                        }
                        if (tempList2.size() == 0) {
                            adapter.getData().clear();
                            setEmptyView(getString(R.string.noData));
                        } else {
                            hideEmptyView();
                            adapter.setNewData(tempList2);
                        }
                    }
                }
                break;

            case 3: //审核中
                tvInreview.setBackgroundResource(R.drawable.shape_gradual_orange_light_corners_20);
                tvInreview.setTextColor(Color.WHITE);
                if (withdrawType == 1 || withdrawType == 4) {
                    if (withdrawList != null) {
                        List<Withdraw> tempList = new ArrayList<>();
                        for (int i = 0; i < withdrawList.size(); i++) {
                            if (withdrawList.get(i).getStatus() == 0 ||
                                    withdrawList.get(i).getStatus() == 3 ||
                                    withdrawList.get(i).getStatus() == 4 ||
                                    withdrawList.get(i).getStatus() == 7) {
                                tempList.add(withdrawList.get(i));
                            }
                        }
                        if (tempList.size() == 0) {
                            adapter.getData().clear();
                            setEmptyView(getString(R.string.noData));
                        } else {
                            hideEmptyView();
                            adapter.setNewData(tempList);
                        }
                    }
                } else {
                    if (withdrawForShareList != null) {
                        List<WithdrawForShare> tempList2 = new ArrayList<>();
                        for (int i = 0; i < withdrawForShareList.size(); i++) {
                            if (withdrawList.get(i).getStatus() == 0) {
                                tempList2.add(withdrawForShareList.get(i));
                            }
                        }
                        if (tempList2.size() == 0) {
                            adapter.getData().clear();
                            setEmptyView(getString(R.string.noData));
                        } else {
                            hideEmptyView();
                            adapter.setNewData(tempList2);
                        }
                    }
                }
                break;
            case 4: //驳回
                tvBh.setBackgroundResource(R.drawable.shape_gradual_orange_light_corners_20);
                tvBh.setTextColor(Color.WHITE);
                if (withdrawType == 1 || withdrawType == 4) {
                    if (withdrawList != null) {
                        List<Withdraw> tempList = new ArrayList<>();
                        for (int i = 0; i < withdrawList.size(); i++) {
                            if (withdrawList.get(i).getStatus() == 2) {
                                tempList.add(withdrawList.get(i));
                            }
                        }
                        if (tempList.size() == 0) {
                            adapter.getData().clear();
                            setEmptyView(getString(R.string.noData));
                        } else {
                            hideEmptyView();
                            adapter.setNewData(tempList);
                        }
                    }
                } else {
                    if (withdrawForShareList != null) {
                        List<WithdrawForShare> tempList2 = new ArrayList<>();
                        for (int i = 0; i < withdrawForShareList.size(); i++) {
                            if (withdrawForShareList.get(i).getType() == 2) {
                                tempList2.add(withdrawForShareList.get(i));
                            }
                        }
                        if (tempList2.size() == 0) {
                            adapter.getData().clear();
                            setEmptyView(getString(R.string.noData));
                        } else {
                            hideEmptyView();
                            adapter.setNewData(tempList2);
                        }
                    }
                }
                break;
        }
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        if (withdrawType == 1 || withdrawType == 4) {
            rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_moneyoutrecord, new ArrayList<Withdraw>()) {
                @Override
                protected void convert(BaseViewHolder helper, Object item) {
                    Withdraw withdraw = (Withdraw) item;
                    if (withdrawType == 4) {
                        helper.setText(R.id.tv_des, withdraw.getAnchorName());
                    } else {
                        helper.setText(R.id.tv_des, withdraw.getCardNo() + "");
                    }
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
        } else {
            rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_moneyoutrecord, new ArrayList<WithdrawForShare>()) {
                @Override
                protected void convert(BaseViewHolder helper, Object item) {
                    try {
                        WithdrawForShare withdrawForShare = (WithdrawForShare) item;
                        String cardNo;
                        cardNo = withdrawForShare.getCardNo().substring(0, 4) + " **** **** ";
                        cardNo = cardNo + withdrawForShare.getCardNo().substring(withdrawForShare.getCardNo().length() - 5, withdrawForShare.getCardNo().length() - 1);
                        helper.setText(R.id.tv_des, cardNo);
                        helper.setText(R.id.tv_date, TimeUtils.convertShortTime(withdrawForShare.getUpdateTime()));
                        helper.setText(R.id.tv_money, "" + RegexUtils.westMoney(withdrawForShare.getAmount()));
                        if (withdrawForShare.getStatus() == 1 || withdrawForShare.getStatus() == 5) {
                            helper.setTextColor(R.id.tv_status, Color.parseColor("#949494"));
                            helper.setText(R.id.tv_status, getString(R.string.yitongguo));
                        } else if (withdrawForShare.getStatus() == 0 || withdrawForShare.getStatus() == 7) {
                            helper.setTextColor(R.id.tv_status, Color.parseColor("#F68A92"));
                            helper.setText(R.id.tv_status, getString(R.string.daishenhe));
                        } else if (withdrawForShare.getStatus() == 2 || withdrawForShare.getStatus() == 6) {
                            helper.setTextColor(R.id.tv_status, Color.parseColor("#F68A92"));
                            helper.setText(R.id.tv_status, getString(R.string.rejected));
                        } else {
                            helper.setText(R.id.tv_status, " ");
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            });
        }
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 0;
            if (withdrawType == 3) {
                doGetListApiForShare(true);
            } else if (withdrawType == 4) {
                doGetJzApi(true);
            } else {
                doGetListApi(true);
            }
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page = page + 1;
            if (withdrawType == 3) {
                doGetListApiForShare(false);
            } else if (withdrawType == 4) {
                doGetJzApi(false);
            } else {
                doGetListApi(false);
            }
        });
    }

    /**
     * 提现列表
     */
    public void doGetListApi(boolean isRefresh) {
        Api_User.ins().withdrawList(0, withdrawType, new JsonCallback<List<Withdraw>>() {
            @Override
            public void onSuccess(int code, String msg, List<Withdraw> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            setEmptyView(getString(R.string.noRecord));
                        } else {
                            withdrawList = data;
                            refreshPage(pageType);
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

    /**
     * 家族提现
     */
    public void doGetJzApi(boolean isRefresh) {
        Api_User.ins().jzList(page, new JsonCallback<List<Withdraw>>() {
            @Override
            public void onSuccess(int code, String msg, List<Withdraw> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            setEmptyView(getString(R.string.noFamilyRecord));
                        } else {
                            withdrawList = data;
                            refreshPage(pageType);
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
                    setEmptyView(msg);
                }

            }
        });
    }

    private void setEmptyView(String tip) {
        ViewGroup viewGroup = (ViewGroup) rv.getParent();
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.view_empty, viewGroup, false);
        TextView textTip = view.findViewById(R.id.tv_empty);
        textTip.setText(tip);
        adapter.setEmptyView(view);
        adapter.notifyDataSetChanged();
    }

    /**
     * 提现列表-分享收益
     */
    public void doGetListApiForShare(boolean isRefresh) {
        Api_Promotion.ins().withdrawlog(page, 1, new JsonCallback<List<WithdrawForShare>>() {
            @Override
            public void onSuccess(int code, String msg, List<WithdrawForShare> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            withdrawForShareList = new ArrayList<>();
                            refreshPage(pageType);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                pageType = 1;
                refreshPage(pageType);
                break;
            case R.id.tv_inreview:
                pageType = 3;
                refreshPage(pageType);
                break;
            case R.id.tv_ydz:
                pageType = 2;
                refreshPage(pageType);
                break;
            case R.id.tv_bh:
                pageType = 4;
                refreshPage(pageType);
                break;
        }
    }
}

