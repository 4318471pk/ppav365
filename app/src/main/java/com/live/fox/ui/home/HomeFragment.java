package com.live.fox.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.R;
import com.live.fox.adapter.ViewPagerFragmentAdapter;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.HomeColumn;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Config;
import com.live.fox.ui.circle.WebFragment;
import com.live.fox.ui.mine.activity.kefu.ServicesActivity;
import com.live.fox.ui.rank.RankActivity;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.view.tab.SimpleTabLayout;

import java.util.List;


/**
 * 主页
 * 直播列表
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    SimpleTabLayout tabLayout;

    ViewPager viewPager;

    ViewPagerFragmentAdapter<BaseFragment> adapter;
    private PromoteListFragment promoteListFragment;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.home_fragment, container, false);
            initView(rootView);
            setView();
        }
        return rootView;
    }

    private void initView(View rootView) {
        viewPager = rootView.findViewById(R.id.man_home_viewpager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        setTopPaddingStatusBarHeight(rootView.findViewById(R.id.ll_title));
        rootView.findViewById(R.id.iv_kefu).setOnClickListener(this);
        rootView.findViewById(R.id.iv_rank).setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) return;

        promoteListFragment = PromoteListFragment.newInstance();
        StatusBarUtil.setStatusBarFulAlpha(requireActivity());
        BarUtils.setStatusBarVisibility(requireActivity(), true);
        BarUtils.setStatusBarLightMode(requireActivity(), true);
    }

    public void setView() {
        //沉浸式布局需要头部留出导航栏的高度
        doGetColumnListApi();

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                if (i == 1 && AppUserManger.isLogin(requireActivity())) {
                    promoteListFragment.doGetShopApi();
                } else {
                    tabLayout.onPageSelected(0);
                }
            }
        });
    }

    public void doGetColumnListApi() {
        Api_Config.ins().getColumn(new JsonCallback<List<HomeColumn>>() {
            @Override
            public void onSuccess(int code, String msg, List<HomeColumn> data) {
                if (data == null) {
                    data = SPManager.getHomeColumn();
                } else {
                    SPManager.saveHomeColumn(data);
                }

                if (!isAdded()) return;

                adapter = new ViewPagerFragmentAdapter<>(getChildFragmentManager());
                //遍历栏目列表 设置Fragment
                for (int i = 0; i < data.size(); i++) {
                    HomeColumn column = data.get(i);
                    if (i == 0) {
                        adapter.addFragment(LiveListFragment.newInstance(), column.getName().trim());
                    } else if (AppUserManger.isLogin() && i == 1) {
                        adapter.addFragment(promoteListFragment, column.getName().trim());
                    } else {
                        adapter.addFragment(WebFragment.newInstance(column.getJumpUrl(),
                                false), column.getName().trim());
                    }
                }

                viewPager.setAdapter(adapter);
                tabLayout.setViewPager(viewPager);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        int id = view.getId();
        if (id == R.id.iv_kefu) {
            ServicesActivity.startActivity(getActivity());
        }

        if (id == R.id.iv_rank && AppUserManger.isLogin(requireContext())) {
            RankActivity.startActivity(requireActivity());
        }
    }
}

