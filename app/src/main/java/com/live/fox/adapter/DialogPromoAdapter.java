package com.live.fox.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class DialogPromoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    Context context;

    public DialogPromoAdapter(Context context, List<String> data) {
        super(R.layout.item_dialog_promo, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
