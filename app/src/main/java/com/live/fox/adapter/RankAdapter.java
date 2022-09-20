package com.live.fox.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class RankAdapter extends BaseQuickAdapter {


    public RankAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);

    }

    public void notifyData(List data)
    {
        this.replaceData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
