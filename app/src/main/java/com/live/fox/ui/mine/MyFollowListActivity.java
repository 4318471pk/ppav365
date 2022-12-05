package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
import com.live.fox.entity.RankItemBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.server.Api_User;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
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
    String otherUid="";

    public static void startActivity(Context context, boolean isFans,String otherUid) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MyFollowListActivity.class);
        i.putExtra("isFans", isFans);
        i.putExtra("otherUid", otherUid);
        context.startActivity(i);

    }

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
        otherUid= this.getIntent().getStringExtra("otherUid");


        setActivityTitle(isFans? getString(R.string.fans_list) : getString(R.string.follow_list));


        List<String> list = new ArrayList<>();

        myFollowListAdapter = new MyFollowListAdapter(list, isFans,this);
        myFollowListAdapter.setOnCancelFollowListener(new MyFollowListAdapter.OnClickListener() {
            @Override
            public void onCancelFollow(String uid,int pos, boolean isFollow) {
                followFans(uid,pos,isFollow);
            }

            @Override
            public void onClickProfile(int pos) {
                Follow follow= myFollowListAdapter.getData().get(pos);
                if(follow.getBroadcast())
                {
                    List<RoomListBean> listBeans=new ArrayList<>();
                    int position=0;
                    for (int i = 0; i <myFollowListAdapter.getData().size() ; i++) {
                        Follow temp= myFollowListAdapter.getData().get(i);
                        if(temp.getBroadcast())
                        {
                            if(temp.getLiveId().equals(follow.getLiveId()))
                            {
                                position=i;
                            }
                            listBeans.add(Follow.convert(temp));
                        }
                    }
                    LivingActivity.startActivity(MyFollowListActivity.this,listBeans,position);
                    return;
                }
                else
                {
                    UserDetailActivity.startActivity(MyFollowListActivity.this,Long.valueOf(follow.getUid()));
                }
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

        if(TextUtils.isEmpty(otherUid)){
            if (isFans) {
                Api_User.ins().getFansList(0, page, getCallback(isRefresh));
            } else {
                Api_User.ins().getFollowList(0, page, getCallback(isRefresh));
            }
        }else {
            if (isFans) {
                Api_User.ins().getFansList2(0, page, getCallback(isRefresh),otherUid);
            } else {
                Api_User.ins().getFollowList2(0, page, getCallback(isRefresh),otherUid);
            }
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

    //false 取消关注
    //true  关注
    private void followFans(String uid,int position, boolean isFollow){
        showLoadingDialogWithNoBgBlack();
        Api_User.ins().followUser(uid + "", isFollow, new  JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0) {

                    if(!isFollow){
                        ToastUtils.showShort(getString(R.string.cancelFocus));
                    }else {
                        ToastUtils.showShort(getString(R.string.successFocus));
                    }

//                    myFollowListAdapter.remove(position);
                    myFollowListAdapter.notifyItemChanged(position);
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }



}
