package com.live.fox.ui.lottery.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class KaiJiangResultIvAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public KaiJiangResultIvAdapter(List data) {
        super(R.layout.item_kaijiang_result_iv, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {


    }

}
