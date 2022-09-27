package com.live.fox.ui.mine.gamerecord;

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
import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.entity.ZbjlBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class XiaofeiFragment extends BaseFragment {

    BaseQuickAdapter adapter;

    private RecyclerView rv;
    private SmartRefreshLayout refreshLayout;

    List<String> openListPos = new ArrayList<>();

    private TextView tvTime;
    private TextView tvTotalReward;

    public static XiaofeiFragment newInstance() {
        XiaofeiFragment fragment = new XiaofeiFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_xiaofei, null, false);//layout_zbjl
        setView(rootView);
        return rootView;
    }


    public void setView(View bindSource) {

        setRecycleView();
        rv = bindSource.findViewById(R.id.rv_);
        refreshLayout = bindSource.findViewById(R.id.refreshLayout);
        tvTime = bindSource.findViewById(R.id.tv_time);
        tvTotalReward = bindSource.findViewById(R.id.tv_total_reward);
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_xiaofei, new ArrayList<ZbjlBean.JsonsBean>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
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

    }
}

