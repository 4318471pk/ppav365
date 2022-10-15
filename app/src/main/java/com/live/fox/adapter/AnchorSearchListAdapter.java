package com.live.fox.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class AnchorSearchListAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    Context context;

    public AnchorSearchListAdapter(Context context, @Nullable @org.jetbrains.annotations.Nullable List<T> data) {
        super(R.layout.item_search_anchor, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {

    }
}
