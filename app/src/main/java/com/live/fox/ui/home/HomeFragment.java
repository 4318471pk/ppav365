package com.live.fox.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.R;
import com.live.fox.adapter.HomeFragmentPagerAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseFragment;
import com.live.fox.manager.DataCenter;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.ui.rank.RankActivity;
import com.live.fox.ui.search.SearchAnchorActivity;
import com.live.fox.utils.ClickUtil;
import com.live.fox.view.tab.SimpleTabLayout;

import org.jetbrains.annotations.NotNull;


/**
 * 主页
 * 直播列表
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    SimpleTabLayout tabLayout;

    ViewPager viewPager;

    HomeFragmentPagerAdapter<BaseBindingFragment> adapter;

    private boolean hasPause=false;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.home_fragment, container, false);
            initView(rootView);
        }
        return rootView;
    }

    private void initView(View rootView) {
        viewPager = rootView.findViewById(R.id.man_home_viewpager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setGradient(0xffA800FF,0xffEA00FF);
        setTopPaddingStatusBarHeight(rootView.findViewById(R.id.ll_title));
        rootView.findViewById(R.id.iv_rank).setOnClickListener(this);
        rootView.findViewById(R.id.ivSearch).setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(hasPause && adapter.getFragmentList()!=null && adapter.getFragmentList().size()>0)
        {
            adapter.getFragmentList().get(viewPager.getCurrentItem()).onResumeFromPause();
        }
        hasPause=false;
    }

    @Override
    public void onPause() {
        super.onPause();
        hasPause=true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) return;

//        StatusBarUtil.setStatusBarFulAlpha(requireActivity());
//        BarUtils.setStatusBarVisibility(requireActivity(), true);
//        BarUtils.setStatusBarLightMode(requireActivity(), true);
    }

    public void setView() {
        //沉浸式布局需要头部留出导航栏的高度
        String[] strs=getResources().getStringArray(R.array.homeTabs);
        adapter = new HomeFragmentPagerAdapter(getChildFragmentManager(),strs);
        //遍历栏目列表 设置Fragment

//                if(data!=null && data.size()>5)
//                {
        viewPager.setOffscreenPageLimit(4);
//                }
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        tabLayout.setCurrentTab(1);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
//                if (i == 1 && AppUserManger.isLogin(requireActivity())) {
//                    promoteListFragment.doGetShopApi();
//                } else {
//                    tabLayout.onPageSelected(0);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if(state==0)
                {
                    adapter.getFragmentList().get(viewPager.getCurrentItem()).onHiddenChanged(false);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        int id = view.getId();
//        if (id == R.id.iv_kefu) {
//            ServicesActivity.startActivity(getActivity());
//        }

        if (id == R.id.iv_rank) {
            if(DataCenter.getInstance().getUserInfo().isLogin())
            {
                RankActivity.startActivity(requireActivity());
            }
            else
            {
                LoginModeSelActivity.startActivity(requireActivity());
            }
        }

        if(id==R.id.ivSearch)
        {
            if(DataCenter.getInstance().getUserInfo().isLogin())
            {
                SearchAnchorActivity.startActivity(requireActivity());
            }
            else
            {
                LoginModeSelActivity.startActivity(requireActivity());
            }
        }
    }
}
