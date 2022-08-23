package com.live.fox.ui.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.live.fox.LiveControlFragment;
import com.live.fox.R;
import com.live.fox.common.CommonLiveControlFragment;
import com.live.fox.entity.Anchor;
import com.live.fox.utils.LogUtils;

/**
 * 用户滑动的
 * 直播滑动页面
 * 包含一个空白页面和一个功能页面
 */

public class SlidingFragment extends Fragment {
    protected View rootView;

    ViewPager viewPager;

    public LiveControlFragment liveControlFragment;

    public static SlidingFragment newInstance(Anchor anchor) {
        LogUtils.e("SlidingFragment newInstance: start");
        SlidingFragment fragment = new SlidingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("anchor", anchor);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.e("SlidingFragment onCreateView: start");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.hslide_fragment, container, false);
        }
        setView(rootView);
        return rootView;
    }

    public void setView(View view) {
        LogUtils.e("SlidingFragment setView: start");
        if (getArguments() == null) {
            return;
        }

        Anchor anchor = (Anchor) getArguments().getSerializable("anchor");
        LogUtils.e("PlayFragment初始化 " + new Gson().toJson(anchor));
        liveControlFragment = CommonLiveControlFragment.newInstance(anchor);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return position == 0 ? new EmptyFragment() : liveControlFragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        viewPager.setCurrentItem(1, false);
    }

    public LiveControlFragment getLiveControlFragment() {
        return liveControlFragment;
    }

}
