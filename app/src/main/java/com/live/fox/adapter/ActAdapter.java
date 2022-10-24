package com.live.fox.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class ActAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public ActAdapter(List data) {
        super(R.layout.item_activity, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {



    }

}
