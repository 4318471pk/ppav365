package com.live.fox.ui.act;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.live.fox.R;
import com.live.fox.adapter.ActAdapter;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.ActBean;
import com.live.fox.server.Api_Order;
import com.live.fox.ui.h5.H5Activity;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.JumpLinkUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActivityDetailFragment extends BaseFragment {

    RecyclerView rc;
    SmartRefreshLayout refreshLayout;
    private List<ActBean> mData = new ArrayList<>();
    ActAdapter mAdapter;

    boolean isGame = true;

    public static ActivityDetailFragment newInstance(boolean isGame) {
        ActivityDetailFragment fragment = new ActivityDetailFragment();
        Bundle args = new Bundle();
        args.putBoolean("isGame", isGame);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_refresh_recycler_view, container, false);
        setView(rootView);
        return rootView;
    }


    private void setView(View bindSource) {
        isGame = getArguments().getBoolean("isGame");
        rc = bindSource.findViewById(R.id.refresh_recycler_view);
        refreshLayout =  bindSource.findViewById(R.id.refresh_refresh_layout);
        refreshLayout.setBackgroundColor(getResources().getColor(R.color.colorF5F1F8));//colorD2CDE0
        refreshLayout.setRefreshHeader(new MyWaterDropHeader(getActivity()));

        mAdapter = new ActAdapter(mData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rc.setLayoutManager(layoutManager);
        rc.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActBean actBean=mData.get(position);
                actBean.setJumpType(1);
                JumpLinkUtils.jumpActivityLinks(getContext(),actBean);
            }
        });


        getAct();
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                getAct();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getAct(){
        showLoadingDialog();
        int game = 1;
        if (isGame) {
            game = 2;
        }
        Api_Order.ins().getAct(game,new JsonCallback<List<ActBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<ActBean> data) {
                hideLoadingDialog();
                refreshLayout.finishRefresh();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    if (data != null && data.size() > 0) {
                        mData.clear();
                        mData.addAll(data);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

}
