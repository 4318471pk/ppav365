package com.live.fox.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.live.fox.base.BaseFragment;
import com.live.fox.entity.HomeColumn;
import com.live.fox.ui.WebFragment;
import com.live.fox.ui.home.AnchorGameFragment;
import com.live.fox.ui.home.FollowAnchorFragment;
import com.live.fox.ui.home.HotAnchorFragment;
import com.live.fox.ui.home.NearByPeopleFragment;
import com.live.fox.ui.home.RecommendListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeFragmentPagerAdapter<T extends BaseFragment> extends FragmentStatePagerAdapter {

    private final SparseArray<BaseFragment> fragmentList = new SparseArray<>();
    private final String[] titleList;

    public HomeFragmentPagerAdapter(@NonNull @NotNull FragmentManager fm,String[] titleList) {
        super(fm);
        this.titleList=titleList;
    }

    public SparseArray<BaseFragment> getFragmentList() {
        return fragmentList;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        BaseFragment baseFragment=null;
        switch (position)
        {
            case 0:
                baseFragment=FollowAnchorFragment.newInstance();
                break;
            case 1:
                baseFragment= RecommendListFragment.newInstance();
                break;
            case 2:
                baseFragment= HotAnchorFragment.newInstance();
                break;
            case 3:
                baseFragment= AnchorGameFragment.newInstance();
                break;
            case 4:
                baseFragment= NearByPeopleFragment.newInstance();
                break;
        }
        return baseFragment;
    }

    @Override
    public int getCount() {
        return titleList.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        if(((BaseFragment)object).getView()==null)
        {
            return false;
        }
        return ((BaseFragment)object).getView().equals(view);
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {

        BaseFragment baseFragment=(BaseFragment)super.instantiateItem(container,position);
        fragmentList.put(position,baseFragment);
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        fragmentList.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titleList[position];
    }
}
