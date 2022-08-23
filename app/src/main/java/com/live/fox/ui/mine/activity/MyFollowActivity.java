package com.live.fox.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Follow;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的关注列表
 */
public class MyFollowActivity extends BaseHeadActivity {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;

    BaseQuickAdapter adapter;

    int page = 0;

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MyFollowActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recycle_view_with_refresh);
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.myFocus), true, true);

        refreshLayout = findViewById(R.id.refreshLayout);
        rv = findViewById(R.id.rv_);

        setRecycleView();
        doGetFollowListApi(true);
        showLoadingView();
        rv.postDelayed(() -> hideLoadingView(), 500);

    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_follow, new ArrayList<Follow>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                Follow follow = (Follow) item;
                GlideUtils.loadDefaultCircleImage(context, follow.getAvatar(), helper.getView(R.id.iv_head));
                helper.setText(R.id.tv_name, ChatSpanUtils.ins().getNickNameAndLevel(follow.getNickname(),
                        follow.getUserLevel(), context));

                helper.addOnClickListener(R.id.iv_head);
                helper.addOnClickListener(R.id.tv_remove);
            }
        });

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            Follow follow = (Follow) adapter.getItem(position);
            if (view.getId() == R.id.tv_remove) {
                doRemoveFollowApi(position, follow.getUid());
            } else if (view.getId() == R.id.iv_head) {
                UserDetailActivity.startActivity(MyFollowActivity.this, follow.getUid());
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                doGetFollowListApi(true);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                doGetFollowListApi(false);
            }
        });
    }


    /**
     * 我的关注列表
     */
    public void doGetFollowListApi(boolean isRefresh) {
        Api_User.ins().getFollowList(0, page, new JsonCallback<List<Follow>>() {
            @Override
            public void onSuccess(int code, String msg, List<Follow> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            showEmptyView(getString(R.string.noFocus));
                        } else {
                            hideEmptyView();
                            adapter.setNewData(data);
                        }
                    } else {
                        refreshLayout.finishLoadMore();
                        List<Follow> list = adapter.getData();
                        adapter.addData(data);
                        adapter.notifyItemRangeInserted(list.size(), data.size());
                    }
                    if (data.size() < Constant.pageSize) {
                        //如果没有更多的数据 则隐藏加载更多功能
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }

//                    if (data==null || data.size() == 0) {
//                        showEmptyView("您还未关注任何人哦~");
//                    } else {
//                        adapter.setNewData(data);
//                    }
                } else {
                    showEmptyView(msg);
                }

            }
        });
    }


    /**
     * 取消
     */
    public void doRemoveFollowApi(int position, long uid) {
        Api_User.ins().follow(uid, false, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    adapter.remove(position);
                } else {
                    showToastTip(false, msg);
                }

            }
        });
    }


}
