package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.MyFollowListAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityMyFollowListBinding;
import com.live.fox.entity.Follow;
import com.live.fox.server.Api_User;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MyFollowListActivity extends BaseBindingViewActivity {

    ActivityMyFollowListBinding mBind;
    boolean isFans = false;

    MyFollowListAdapter myFollowListAdapter;

    int page = 0;


    public static void startActivity(Context context, boolean isFans) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MyFollowListActivity.class);
        i.putExtra("isFans", isFans);
        context.startActivity(i);

    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_my_follow_list;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        isFans = this.getIntent().getBooleanExtra("isFans", isFans);

        setActivityTitle(isFans? getString(R.string.fans_list) : getString(R.string.follow_list));


        List<String> list = new ArrayList<>();

        myFollowListAdapter = new MyFollowListAdapter(list, isFans,this);
        myFollowListAdapter.setOnCancelFollowListener(new MyFollowListAdapter.OnCancelFollowListener() {
            @Override
            public void onCancelFollow(String uid,int pos) {
                followFans(uid,pos);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rv.setLayoutManager(layoutManager);
        mBind.rv.setAdapter(myFollowListAdapter);
        mBind.refreshLayout.setRefreshHeader(new MyWaterDropHeader(this));

//        myFollowListAdapter.setMyFollowInterFace(new MyFollowListAdapter.MyFollowInterFace() {
//            @Override
//            public void clickFollow(int pos, boolean cancelFollow, Follow data) {
//
//            }
//        });

        doGetFollowListApi(true);

        mBind.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                doGetFollowListApi(true);
            }
        });



        mBind.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
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
        if (isFans) {
            Api_User.ins().getFansList(0, page, getCallback(isRefresh));
        } else {
            Api_User.ins().getFollowList(0, page, getCallback(isRefresh));
        }
    }

    private JsonCallback getCallback(boolean isRefresh)
    {
        showLoadingDialogWithNoBgBlack();
        return new JsonCallback<List<Follow>>() {
            @Override
            public void onSuccess(int code, String msg, List<Follow> data) {
                hideLoadingDialog();
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0) {
                    if (isRefresh) {
                        mBind.refreshLayout.finishRefresh();
                        mBind.refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            showEmptyView(isFans ? getString(R.string.no_fans) : getString(R.string.no_anchor));
                        } else {
                            hideEmptyView();
                            myFollowListAdapter.setNewData(data);
                        }
                    } else {
                        mBind.refreshLayout.finishLoadMore();
                        List<Follow> list = myFollowListAdapter.getData();
                        myFollowListAdapter.addData(data);
                        myFollowListAdapter.notifyItemRangeInserted(list.size(), data.size());
                    }
                    if (data.size() < Constant.pageSize) {
                        //如果没有更多的数据 则隐藏加载更多功能
                        mBind.refreshLayout.finishLoadMoreWithNoMoreData();
                    }

                } else {
                    showEmptyView(msg);
                }

            }
        };
    }

    private void followFans(String uid,int position){
        showLoadingDialogWithNoBgBlack();
        Api_User.ins().followUser(uid + "", false, new  JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0) {
                    ToastUtils.showShort(getString(R.string.cancelFocus));
                    myFollowListAdapter.remove(position);
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }



}
