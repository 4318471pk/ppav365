package com.live.fox.ui.rank;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.live.fox.base.BaseFragment;

import java.util.List;

/**
 * RankActivity下的ViewPager适配器
 * 内部维护RankListFragmentList
 */

public class RankFragmentAdapter extends FragmentStatePagerAdapter {

    //这里的Fragment只接受BaseFragment的子类
    private List<? extends BaseFragment> fragments;
    private List<String> titles;

    public RankFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<? extends BaseFragment> fragments, List<String> titles) {
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
