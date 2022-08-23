package com.live.fox.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.adapter.CategoryFragmentAdapter;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.LiveColumn;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Config;
import com.live.fox.ui.SearchActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.LogUtils;

import java.util.List;


public class ChatListFragment extends BaseFragment implements View.OnClickListener {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    public static ChatListFragment newInstance() {
        return new ChatListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.chatlist_activity, container, false);
        setView(rootView);
        return rootView;
    }

    private void setView(View bindSource) {
        tabLayout = bindSource.findViewById(R.id.tabLayout);
        viewPager = bindSource.findViewById(R.id.viewPager);
        bindSource.findViewById(R.id.iv_search).setOnClickListener(this);
        ;

        setTopPaddingStatusBarHeight(rootView.findViewById(R.id.ll_title));
        doGetLiveColumnListApi();
    }

    public void doGetLiveColumnListApi() {
        Api_Config.ins().getTag(new JsonCallback<List<LiveColumn>>() {
            @Override
            public void onSuccess(int code, String msg, List<LiveColumn> data) {
                //如果没有数据 则读本地缓存的数据
                LogUtils.e("栏目data：" + data);
                if (data == null) {
                    data = SPManager.getLiveColumn();
                } else {
                    SPManager.saveLiveColumn(data);
                }
                LogUtils.e("栏目：" + new Gson().toJson(data));

                for (int i = 0; i < data.size(); i++) {
                    LiveColumn c = data.get(i);
                    TabLayout.Tab tab = tabLayout.newTab();
                    tab.setText(c.getName());
                    tab.setTag(c);
                    tabLayout.addTab(tab);
                }

                tabLayout.setupWithViewPager(viewPager);
                CategoryFragmentAdapter adapter = new CategoryFragmentAdapter(getChildFragmentManager(), data);
                viewPager.setAdapter(adapter);
                if (tabLayout.getTabCount() > 1) {
                    tabLayout.selectTab(tabLayout.getTabAt(1));
                } else if (tabLayout.getTabCount() > 0) {
                    tabLayout.selectTab(tabLayout.getTabAt(0));
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.iv_search:
                if (AppUserManger.getUserInfo() == null) {
                    LoginModeSelActivity.startActivity(requireActivity());
                    return;
                }
                SearchActivity.startActivity(requireActivity());
                break;
        }
    }
}
