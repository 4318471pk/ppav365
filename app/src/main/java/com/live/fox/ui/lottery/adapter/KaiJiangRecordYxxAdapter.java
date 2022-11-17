package com.live.fox.ui.lottery.adapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.ArrayList;
import java.util.List;

/*
* 鱼虾蟹开奖
* */
public class KaiJiangRecordYxxAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public KaiJiangRecordYxxAdapter(List data) {
        super(R.layout.item_kaijiang_record_yxx, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {


    }

}
