package com.live.fox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.live.fox.R;

import java.util.List;

public class TuiGuangPageAdapter extends PagerAdapter {

    List<Boolean> data;
    private Context context;

    public TuiGuangPageAdapter(Context context, List<Boolean> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;//无限轮播
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_tuiguang,null);
//        int newwPos=position%(datas.size());//对应数据中的位置
        ImageView img=view.findViewById(R.id.iv);
//        img.setImageResource(datas.get(newwPos));

        img.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setTag(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object
            object) {
//            super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}

