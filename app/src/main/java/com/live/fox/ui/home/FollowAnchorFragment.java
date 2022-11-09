package com.live.fox.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.adapter.FollowAnchorListAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentFollowAnchorBinding;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.RoomListBean;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.EmptyDataView;
import com.live.fox.view.RecommendAnchorListFooter;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FollowAnchorFragment extends BaseBindingFragment {

    FragmentFollowAnchorBinding mBind;
    FollowAnchorListAdapter adapter;
    EmptyDataView emptyDataView;
    RecommendAnchorListFooter recommendAnchorListFooter;

    public static FollowAnchorFragment newInstance() {
        return new FollowAnchorFragment();
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
        {
            getRecommendList();
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_follow_anchor;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        mBind.srlRefresh.setRefreshHeader(new MyWaterDropHeader(getActivity()));
        mBind.srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                if(adapter.getData().size()>0)
                {
                    refreshLayout.finishRefresh(false);
                    setEmptyData(true);
                }
                else
                {
                    refreshLayout.finishRefresh(true);
                    for (int i = 0; i < 7; i++) {
                        adapter.getData().add(new Anchor());
                    }
                    setEmptyData(false);
                }

            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                2);
        mBind.rvMain.setLayoutManager(layoutManager);
        int dip5=DeviceUtils.dp2px(requireActivity(), 5.0f);
        mBind.rvMain.addItemDecoration(new RecyclerSpace(dip5,RecyclerSpace.AnchorGrid));

        adapter=new FollowAnchorListAdapter(getActivity(),new ArrayList());
        recommendAnchorListFooter=new RecommendAnchorListFooter(getActivity());
        recommendAnchorListFooter.setOnClickChangeListListener(new RecommendAnchorListFooter.onClickChangeListListener() {
            @Override
            public void onChange() {
                getRecommendList();
            }
        });
        adapter.setFooterView(recommendAnchorListFooter);
        mBind.rvMain.setAdapter(adapter);
        emptyDataView=new EmptyDataView(getActivity());
        adapter.addHeaderView(emptyDataView);

        setEmptyData(true);
    }

    private void setEmptyData(boolean isEmpty)
    {
        if(isEmpty)
        {
            emptyDataView.getLayoutParams().height=ScreenUtils.getDip2px(getActivity(),200);
            adapter.setNewData(new ArrayList());
            recommendAnchorListFooter.setBotTextVisible(false);
        }
        else
        {
            emptyDataView.getLayoutParams().height=0;
            recommendAnchorListFooter.setBotTextVisible(true);
        }
        adapter.notifyDataSetChanged();
    }


    private void getRecommendList() {
        Api_Live.ins().getRecommendLiveList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (!TextUtils.isEmpty(data)) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        JSONArray list = jsonObject.getJSONArray("list");
                        if (list != null && list.length() > 0) {
                            List<RoomListBean> listBeans=new ArrayList<>();
                            for (int i = 0; i < list.length(); i++) {
                                RoomListBean bean=new Gson().fromJson(list.getJSONObject(i).toString(),RoomListBean.class);
                                listBeans.add(bean);
                            }
                            recommendAnchorListFooter.setData(listBeans);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }
}
