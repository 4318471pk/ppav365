package com.live.fox.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class MyFollowListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public MyFollowListAdapter(List data) {
        super(R.layout.item_follow_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {
//        if (data == null) {
//            return;
//        }


    }


}
