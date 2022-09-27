package com.live.fox.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseLazyViewPagerFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.HongdongBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Config;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.manager.AppUserManger;
import com.luck.picture.lib.tools.ScreenUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;



/**
 * 主页直播列表
 * Home页面
 * 推广链接
 */
public class PromoteListFragment extends BaseLazyViewPagerFragment {

    RecyclerView rv;
    SmartRefreshLayout refreshLayout;

    BaseQuickAdapter<HongdongBean, BaseViewHolder> adapter;

    public static PromoteListFragment newInstance() {
        return new PromoteListFragment();
    }

    @Override
    public int getContentLayout() {
        return R.layout.layout_recycle_view_with_refresh;
    }

    @Override
    public void bindView(Bundle savedInstanceState) {
        hasInit = true;
        rv = rootView.findViewById(R.id.rv_);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        setRecycleView();
        if (DataCenter.getInstance().getUserInfo().isLogin()) {
            doGetShopApi();
        }
    }

    /**
     * 接口不支持分页
     */
    public void setRecycleView() {

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(refreshLayout -> doGetShopApi());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getCtx());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new RecyclerSpace(ScreenUtils.dip2px(requireContext(), 4)));

        rv.setAdapter(adapter = new BaseQuickAdapter<HongdongBean,
                BaseViewHolder>(R.layout.huodonglist_fragment_item, new ArrayList<>()) {
            @Override
            protected void convert(BaseViewHolder helper, HongdongBean item) {
                helper.setText(R.id.home_activity_type,
                        item.getDistributeType() == 2 ? mContext.getString(R.string.activity_type) : mContext.getString(R.string.activity_type_auto));
                GlideUtils.loadDefaultImage(getCtx(), item.getActivityHomePage(), helper.getView(R.id.image));
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            HongdongBean activity = (HongdongBean) adapter.getData().get(position);
            if (activity != null) {
                FragmentContentActivity.startWebActivity(getActivity(),
                        activity.getMsg(), activity.getActivityDetail());
            }
        });
    }


    /**
     * 获取活动列表
     */
    public void doGetShopApi() {
        Api_Config.ins().getPromote(new JsonCallback<List<HongdongBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<HongdongBean> data) {
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                }
                if (code == 0 && data != null) {
                    if (adapter != null) {
                        adapter.setNewData(data);
                    }
                } else {
                    if (adapter != null) {
                        ViewGroup viewGroup = (ViewGroup) rv.getParent();
                        View emptyView = LayoutInflater.from(requireContext())
                                .inflate(R.layout.view_empty, viewGroup, false);
                        adapter.setEmptyView(emptyView);
                    }
                }
            }
        });
    }
}
