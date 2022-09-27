package com.live.fox.ui.mine.gamerecord;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.MyTouzuBean;
import com.live.fox.server.Api_Cp;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class TouzuFragment extends BaseFragment {

    private RecyclerView rv;
    private SmartRefreshLayout refreshLayout;
    BaseQuickAdapter adapter;
    private int pageNum = 0;
    private long uid;
    private String lotteryName;
    private int type;
    private int page = 0;

    public static TouzuFragment newInstance(int pageNum, long uid, String lotteryName, int type) {
        TouzuFragment fragment = new TouzuFragment();
        Bundle args = new Bundle();
        args.putInt("pageNum", pageNum);
        args.putLong("uid", uid);
        args.putString("lotteryName", lotteryName);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_touzu, null, false);
        setView(rootView);
        initData(getArguments());

        return rootView;
    }


    public void initData(Bundle arguments) {
        if (arguments != null) {
            pageNum = arguments.getInt("pageNum");
            uid = arguments.getLong("uid", 0);
            lotteryName = arguments.getString("lotteryName");
            type = arguments.getInt("type", 0);
            getLotteryResultHistory(true, pageNum);
        }
    }

    public void setView(View bindSource) {
        rv = bindSource.findViewById(R.id.rv_);
        refreshLayout = bindSource.findViewById(R.id.refreshLayout);
        setRecycleView();
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_touzu, new ArrayList<MyTouzuBean>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                MyTouzuBean bean = (MyTouzuBean) item;
                TextView tvJieguo = helper.getView(R.id.tv_jieguo);
                ImageView ivJieguo = helper.getView(R.id.iv_jieguo);
                helper.setText(R.id.tv_name, bean.getNickName());
                helper.setText(R.id.tv_qihao, Html.fromHtml(getString(R.string.qihao) + "<font color='#FA0000'>" + bean.getExpect() + "</font>"));
                helper.setText(R.id.tv_moneyb, bean.getBetAmount() + "");
                if (0 == bean.getAwardStatus()) {
                    tvJieguo.setText(getString(R.string.weikaijiang));
                    ivJieguo.setVisibility(View.GONE);
                } else if (1 == bean.getAwardStatus()) {
                    ivJieguo.setVisibility(View.VISIBLE);
                    ivJieguo.setImageResource(R.drawable.weizhongjiang);
                    tvJieguo.setText("");
                } else {
                    ivJieguo.setVisibility(View.VISIBLE);
                    ivJieguo.setImageResource(R.drawable.yizhongjiang);
                    tvJieguo.setText("");
                }
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            MyTouzuBean bean = (MyTouzuBean) adapter.getData().get(position);
            if ("yn_hncp".equals(bean.getLotteryName())) {
                HNCPCaiDetailActivity.startActivity(getActivity(), bean);
            } else {
                CaiDetailActivity.startActivity(getActivity(), bean);
            }

        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                getLotteryResultHistory(true, pageNum);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                getLotteryResultHistory(false, pageNum);
            }
        });
    }

    public void getLotteryResultHistory(boolean isRefresh, int queryType) {
        Api_Cp.ins().getLotteryResultHistoryByName(queryType, uid, lotteryName, type, page, new JsonCallback<ArrayList<MyTouzuBean>>() {
            @Override
            public void onSuccess(int code, String msg, ArrayList<MyTouzuBean> data) {
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            showEmptyView(getString(R.string.noData));
                        } else {
                            hideEmptyView();
                            adapter.getData().clear();
                            adapter.setNewData(data);
                        }
                    } else {
                        refreshLayout.finishLoadMore();
                        List<MyTouzuBean> list = adapter.getData();
                        adapter.addData(data);
                        adapter.notifyItemRangeInserted(list.size(), data.size());
                    }
                    if (data.size() < Constant.pageSizeLater) {
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

