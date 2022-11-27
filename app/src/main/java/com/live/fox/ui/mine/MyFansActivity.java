package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.Strings;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的粉丝列表
 */
public class MyFansActivity extends BaseHeadActivity {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;

    BaseQuickAdapter adapter;

    int page = 0;

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MyFansActivity.class);
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
        setHead(getString(R.string.myFan), true, true);

        refreshLayout = findViewById(R.id.refreshLayout);
        rv = findViewById(R.id.rv_);

        setRecycleView();
        doGetFansListApi(true);
        showLoadingView();
        rv.postDelayed(() -> hideLoadingView(), 500);


    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_fans, new ArrayList<Follow>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                Follow follow = (Follow) item;
                GlideUtils.loadDefaultCircleImage(context, follow.getAvatar(), helper.getView(R.id.iv_head));
                helper.setText(R.id.tv_name, ChatSpanUtils.ins().getNickNameAndLevel(follow.getNickname(), follow.getUserLevel(), context));
                if (follow.isFollow()) {
                    helper.setBackgroundRes(R.id.tv_follow, R.drawable.shape_corners_20_gray);
                    helper.setText(R.id.tv_follow, getString(R.string.focused));
                } else {
                    helper.setBackgroundRes(R.id.tv_follow, R.drawable.shape_gradual_orange_light_corners_20);
                    helper.setText(R.id.tv_follow, getString(R.string.focus));
                }

                helper.addOnClickListener(R.id.tv_follow);
                helper.addOnClickListener(R.id.iv_head);

            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Follow follow = (Follow) adapter.getItem(position);
                if (view.getId() == R.id.tv_follow) {
                    doSetFollowApi(position, follow);
                } else if (view.getId() == R.id.iv_head) {
                    if(Strings.isDigitOnly(follow.getUid()))
                    {
                        UserDetailActivity.startActivity(MyFansActivity.this,Long.valueOf(follow.getUid()));
                    }
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                doGetFansListApi(true);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                doGetFansListApi(false);
            }
        });
    }


    /**
     * 我的黑名单列表
     */
    public void doGetFansListApi(boolean isRefresh) {
        Api_User.ins().getFansList(0, page, new JsonCallback<List<Follow>>() {
            @Override
            public void onSuccess(int code, String msg, List<Follow> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            showEmptyView(getString(R.string.noList));
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
                } else {
                    showEmptyView(msg);
                }

            }
        });
    }


    /**
     * 设置/取消 关注
     */
    public void doSetFollowApi(int position, Follow follow) {
        Api_User.ins().follow(Long.valueOf(follow.getUid()), !follow.isFollow(), new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    ((Follow) adapter.getItem(position)).setFollow(!follow.isFollow());
                    adapter.notifyDataSetChanged();
                } else {
                    showToastTip(false, msg);
                }

            }
        });
    }


}
