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

public abstract class BaseFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final SparseArray<BaseFragment> fragmentList = new SparseArray<>();

    public BaseFragmentPagerAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
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
    public int getCount() {
        return getItemCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTitle(position);
    }

    public abstract Fragment getFragment(int position);
    public abstract String getTitle(int position);
    public abstract int getItemCount();
}
