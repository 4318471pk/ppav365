package com.live.fox.ui.lottery.adapter;

import android.graphics.Paint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

/*
* 更多投注
* */
public class TouZhuRecordMoreAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public TouZhuRecordMoreAdapter(List data) {
        super(R.layout.item_touzhu_record_more, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {


    }

}
