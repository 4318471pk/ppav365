package com.live.fox.view.tab;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * Function:
 * Author Name: 姚磊
 * Date: 2019/4/17 17:11
 * Copyright © 2006-2018 高顿网校, All Rights Reserved.
 */
public class FragmentChangeManager {
    private FragmentManager mFragmentManager;
    private int mContainerViewId;
    /**
     * Fragment切换数组
     */
    private ArrayList<Fragment> mFragments;
    /**
     * 当前选中的Tab
     */
    private int mCurrentTab;

    public FragmentChangeManager(FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments) {
        this.mFragmentManager = fm;
        this.mContainerViewId = containerViewId;
        this.mFragments = fragments;
        initFragments();
    }

    /**
     * 初始化fragments
     */
    private void initFragments() {
        for (Fragment fragment : mFragments) {
            mFragmentManager.beginTransaction().add(mContainerViewId, fragment).hide(fragment).commitAllowingStateLoss();
        }

        setFragments(0);
    }

    /**
     * 界面切换控制
     */
    public void setFragments(int index) {
        for (int i = 0; i < mFragments.size(); i++) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            Fragment fragment = mFragments.get(i);
            if (i == index) {
                fragment.setUserVisibleHint(true);
                ft.show(fragment);
            } else {
                fragment.setUserVisibleHint(false);
                ft.hide(fragment);
            }
            ft.commitAllowingStateLoss();
        }
        mCurrentTab = index;
    }

    public Fragment getFragment(int pos) {
        return mFragments.get(pos);
    }

    public Fragment getCurrentFragment() {
        return mFragments.get(mCurrentTab);
    }
}
