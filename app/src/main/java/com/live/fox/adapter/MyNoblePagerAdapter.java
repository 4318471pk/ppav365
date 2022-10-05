package com.live.fox.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.live.fox.base.BaseFragment;


import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyNoblePagerAdapter <T extends BaseFragment> extends FragmentStatePagerAdapter {

    private final SparseArray<BaseFragment> fragmentList = new SparseArray<>();
    private final List<String> titleList;

    public MyNoblePagerAdapter(@NonNull @NotNull FragmentManager fm, List<String> titleList) {
        super(fm);
        this.titleList=titleList;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        BaseFragment baseFragment=null;
        switch (position)
        {
            case 0:
                //baseFragment= FollowAnchorFragment.newInstance();
                break;
            case 1:
                //baseFragment= LiveListFragment.newInstance();
                break;
            default:
                //baseFragment= WebFragment.newInstance(titleList.get(position).getJumpUrl(),false);
                break;
        }
        return baseFragment;
    }

    @Override
    public int getCount() {
        return titleList.size();
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
        if(titleList.size() != 0){
            return titleList.get(position).trim();
        }
        return super.getPageTitle(position);
    }
}
