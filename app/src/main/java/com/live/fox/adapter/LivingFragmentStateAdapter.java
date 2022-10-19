package com.live.fox.adapter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.live.fox.ui.living.LivingFragment;

import java.util.List;

public class LivingFragmentStateAdapter extends FragmentStateAdapter {

    List<String> list;
    SparseArray<LivingFragment> sparseArray;

    public LivingFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity,List<String> list) {
        super(fragmentActivity);
        this.list=list;
        sparseArray=new SparseArray<>();
    }

    public LivingFragment getFragment(int position)
    {
        return sparseArray.get(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int realPoi=(position-(Integer.MAX_VALUE/2))%list.size();
        if(realPoi<0)
        {
            realPoi=realPoi+list.size();
        }
        sparseArray.put(position,LivingFragment.getInstance(realPoi));
        return sparseArray.get(position);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


}
