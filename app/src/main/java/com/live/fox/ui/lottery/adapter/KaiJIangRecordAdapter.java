package com.live.fox.ui.lottery.adapter;

import android.graphics.Paint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class KaiJIangRecordAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public KaiJIangRecordAdapter(List data) {
        super(R.layout.item_kaijiang_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {
        TextView tv5 = helper.getView(R.id.tv5);
        tv5.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

    }

}
