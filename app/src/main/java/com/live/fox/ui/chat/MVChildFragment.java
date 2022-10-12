package com.live.fox.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Anchor;
import com.live.fox.manager.DataCenter;
import com.live.fox.svga.AnchorInfoBean;
import com.live.fox.server.Api_Live;
import com.live.fox.adapter.LiveListAdapter;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.LruCacheUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.luck.picture.lib.tools.DoubleUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class MVChildFragment extends BaseFragment {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView livelistRv;
    private static final String ARG_TYPE = "arg_type";
    private int mType;
    LiveListAdapter livelistAdapter;

    public static MVChildFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        MVChildFragment fragment = new MVChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_child, container, false);
        initData(getArguments());
        setView(rootView);
        return rootView;
    }

    private void initData(Bundle arguments) {
        if (arguments != null) {
            mType = arguments.getInt(ARG_TYPE);
        }
    }

    private void setView(View bindSource) {
        refreshLayout = bindSource.findViewById(R.id.refreshLayout);
        livelistRv = bindSource.findViewById(R.id.rv_);

        initListRecycleView();
        initRefreshLayout();
        doGetLiveListApi(mType);

    }

    /**
     * 初始直播列表
     */
    public void initListRecycleView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                2, GridLayoutManager.VERTICAL, false);
        livelistRv.setLayoutManager(layoutManager);
        livelistRv.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 4)));
        livelistAdapter = new LiveListAdapter(new ArrayList<>());
        livelistRv.setAdapter(livelistAdapter);

        livelistAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (DoubleUtils.isFastDoubleClick()) return;
            if (livelistAdapter.getItem(position) == null) return;
            AnchorInfoBean item = livelistAdapter.getItem(position);
            if (item != null && item.t != null) {
                Anchor liveRoom = item.t;
                toLiveRoom(liveRoom, position);
            }

        });
    }

    //每次回来都刷新直播列表
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x100) {
            doGetLiveListApi(mType);
        }
    }

    public void initRefreshLayout() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> doGetLiveListApi(mType));
    }

    public void doGetLiveListApi(int type) {
        Api_Live.ins().getLiveList(type, new JsonCallback<List<Anchor>>() {
            @Override
            public void onSuccess(int code, String msg, List<Anchor> data) {
                refreshLayout.finishRefresh();
                if (code == 0 && data != null) {
                    if (data.size() == 0) {
                        showEmptyView(getString(R.string.noData));
                        livelistAdapter.setEmptyView(R.layout.view_empty, (ViewGroup) livelistRv.getParent());
                        return;
                    }
                    hideEmptyView();
                    List<AnchorInfoBean> anchorInfoBeanList = new ArrayList<>();
                    for (Anchor anchor : data) {
                        anchor.setRoomType(0);
                        anchorInfoBeanList.add(new AnchorInfoBean(anchor));
                    }
                    livelistAdapter.setData(type, anchorInfoBeanList);
                } else {
                    LogUtils.e("code:" + code);
                    if (livelistAdapter.getData().size() == 0) {
                        showEmptyView(msg);
                    }
                }
            }
        });
    }

    //跳往直播间
    public void toLiveRoom(Anchor anchor, int position) {
        if (DataCenter.getInstance().getUserInfo().getUser() == null) {
            LoginModeSelActivity.startActivity(requireActivity());
            return;
        }
        LogUtils.e(new Gson().toJson(anchor));
        //广告直播间
        if (anchor.getIsAd() == 1) {
            if (StringUtils.isEmpty(anchor.getAdJumpUrl())) return;
            IntentUtils.toBrowser(getActivity(), anchor.getAdJumpUrl());
        } else {
            if (DataCenter.getInstance().getUserInfo().getUser() == null) {
                LoginModeSelActivity.startActivity(requireActivity());
                return;
            }
            if (ClickUtil.isFastDoubleClick2(2000)) return;
            //过滤广告直播间 获取直播列表
            List<AnchorInfoBean> tempAnchorList = livelistAdapter.getData();
            LruCacheUtil.getInstance().get().clear();
            for (int i = 0; i < tempAnchorList.size(); i++) {
                if (!tempAnchorList.get(i).isHeader && tempAnchorList.get(i).t.getIsAd() == 0) {
                    LruCacheUtil.getInstance().put(tempAnchorList.get(i).t);
                }
            }
            PlayLiveActivity.startActivityForResult(MVChildFragment.this, anchor);
        }
    }
}
