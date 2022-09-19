/*
 Copyright © 2015, 2016 Jenly Yu <a href="mailto:jenly1314@gmail.com">Jenly</a>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */
package com.live.fox.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.live.fox.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用ViewPager结合Fragment使用

 使用方式1：
 listTitle = new ArrayList<>();
 listTitle.add("聊天");
 listTitle.add("排行");
 listTitle.add("主编");
 listData = new ArrayList<>();
 listData.add(ChatFragment.newInstance());
 listData.add(RankFragment.newInstance());
 listData.add(anchorInfoFragment = AnchorInfoFragment.newInstance());
 viewPager.setAdapter(new ViewPagerFragmentAdapter(getChildFragmentManager(), listData, listTitle));

 使用方式2：
 ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getChildFragmentManager());
 adapter.addFragment(HotFragment.newInstance(2), "标题1");
 adapter.addFragment(HotFragment.newInstance(1), "标题2");
 adapter.addFragment(HotFragment.newInstance(3), "标题3");
 viewPager.setAdapter(adapter);

 */
public class ViewPagerFragmentAdapter<T extends BaseFragment> extends FragmentStatePagerAdapter {

    private final List<T> fragmentList = new ArrayList<>();
    private final List<CharSequence> titleList = new ArrayList<>();

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerFragmentAdapter(FragmentManager fm, List<T> fragmentList, List<CharSequence> titleList) {
        super(fm);
        this.fragmentList.clear();
        this.titleList.clear();
        this.fragmentList.addAll(fragmentList);
        this.titleList.addAll(titleList);
    }

    public void addFragment(T t, String title) {
        fragmentList.add(t);
        titleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(titleList.size() != 0){
            return titleList.get(position);
        }
        return super.getPageTitle(position);
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        if(((BaseFragment)object).getView()==null)
        {
            return false;
        }
        return ((BaseFragment)object).getView().equals(view);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
