package com.live.fox.adapter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.live.fox.ui.living.LivingFragment;

import java.lang.ref.WeakReference;
import java.util.List;

public class LivingFragmentStateAdapter extends FragmentStateAdapter {

    int size;
    SparseArray<WeakReference<LivingFragment>> sparseArray;

    public LivingFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity,int size) {
        super(fragmentActivity);
        this.size=size;
        sparseArray=new SparseArray<>();
    }

    public LivingFragment getFragment(int position)
    {
        if(sparseArray.get(position)==null)
        {
            return null;
        }
        return sparseArray.get(position).get();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int realPoi=(position-(Integer.MAX_VALUE/2))%size;
        if(realPoi<0)
        {
            realPoi=realPoi+size;
        }
        if(sparseArray.get(position)!=null && sparseArray.get(position).get()!=null)
        {
            return sparseArray.get(position).get();
        }
        else
        {
            sparseArray.put(position,new WeakReference(LivingFragment.getInstance(realPoi,position)));
        }

        return sparseArray.get(position).get();
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public int getRealPosition(int position)
    {
        int realPoi=(position-(Integer.MAX_VALUE/2))%size;
        if(realPoi<0)
        {
            realPoi=realPoi+size;
        }
        return realPoi;
    }


}
