package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
import com.live.fox.entity.Black;
import com.live.fox.entity.User;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
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
 * 黑名单列表
 */
public class BlackLIstActivity extends BaseHeadActivity {
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;

    BaseQuickAdapter adapter;

    int page = 0;

    public static void startActivity(Context context) {
        Constant.isAppInsideClick=true;
        Intent i = new Intent(context, BlackLIstActivity.class);
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
        setHead(getString(R.string.blackName), true, true);

        refreshLayout = findViewById(R.id.refreshLayout);
        rv = findViewById(R.id.rv_);

        setRecycleView();
        doGetBlackListApi(true);
        showLoadingView();
        rv.postDelayed(() -> hideLoadingView(), 500);
    }

    public void setRecycleView() {

//        refreshLayout.setEnablePureScrollMode(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_blacklist, new ArrayList<Black>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                Black black = (Black) item;
                GlideUtils.loadDefaultCircleImage(context, black.getAvatar(), (ImageView) helper.getView(R.id.iv_head));
                helper.setText(R.id.tv_name, black.getNickname());

                helper.addOnClickListener(R.id.tv_removeblack);

            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Black black = (Black)adapter.getItem(position);
                if(view.getId()== R.id.tv_removeblack){
                    doRemoveBlackApi(position, black.getUid());
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                doGetBlackListApi(true);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                doGetBlackListApi(false);
            }
        });
    }


    /**
     * 我的黑名单列表
     */
    public void doGetBlackListApi(boolean isRefresh) {
        Api_User.ins().getBlackList(0, page, new JsonCallback<List<Black>>() {
            @Override
            public void onSuccess(int code, String msg, List<Black> data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data==null || data.size() == 0) {
                            showEmptyView(getString(R.string.noList));
                        }else {
                            hideEmptyView();
                            adapter.setNewData(data);
                        }
                    } else {
                        refreshLayout.finishLoadMore();
                        List<Black> list = adapter.getData();
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
     * 取消
     */
    public void doRemoveBlackApi(int position, long uid) {
        Api_User.ins().setBlack(uid, false, new JsonCallback<String>() {
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

    public List<User> createData() {
        List<User> data = new ArrayList<>();
        data.add(new User());
        data.add(new User());
        data.add(new User());
        data.add(new User());data.add(new User());
        data.add(new User());
        data.add(new User());data.add(new User());data.add(new User());data.add(new User());data.add(new User());data.add(new User());data.add(new User());data.add(new User());data.add(new User());

        return data;

    }


}
