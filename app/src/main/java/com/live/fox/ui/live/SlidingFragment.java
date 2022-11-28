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
        return rootView;
    }


}
