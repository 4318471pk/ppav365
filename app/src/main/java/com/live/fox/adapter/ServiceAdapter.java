package com.live.fox.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class ServiceAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public ServiceAdapter(List data) {
        super(R.layout.item_service, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {


    }


}
