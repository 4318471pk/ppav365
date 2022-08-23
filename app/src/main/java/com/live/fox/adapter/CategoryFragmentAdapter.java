package com.live.fox.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.live.fox.entity.LiveColumn;
import com.live.fox.ui.chat.MVChildFragment;

import java.util.List;

public class CategoryFragmentAdapter extends FragmentPagerAdapter {
    private List<LiveColumn> data;

    public CategoryFragmentAdapter(FragmentManager childFragmentManager, List<LiveColumn> data) {
        super(childFragmentManager);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Fragment getItem(int position) {
        return MVChildFragment.newInstance(data.get(position).getType());
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getName();
    }
}
