package com.live.fox.adapter;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Date: 2019/1/20
 */
public class GiftViewPagerAdapter extends PagerAdapter {
    private List<ViewGroup> list = null;
    private final List<String> homeTabs;

    public GiftViewPagerAdapter(List<ViewGroup> list, List<String> homeTabs) {
        this.list = list;
        this.homeTabs = homeTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return homeTabs.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object view) {
        ((ViewPager) container).removeView((View) view);
    }

    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }

}
