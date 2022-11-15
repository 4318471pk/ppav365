package com.live.fox.ui.lottery.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class KaiJiangResultTvAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public KaiJiangResultTvAdapter(List data) {
        super(R.layout.item_kaijiang_result_tv, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {


    }

}
