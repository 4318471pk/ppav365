package com.live.fox.ui.lottery.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class LotteryTypeAdapter extends BaseQuickAdapter<Boolean, BaseViewHolder> {


    public LotteryTypeAdapter(List data) {
        super(R.layout.item_lottery_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Boolean data) {

        TextView tvName = helper.getView(R.id.tvName);
        if (data) {
            tvName.setBackground(mContext.getResources().getDrawable(R.drawable.bg_lottery_type));

        } else {
            tvName.setBackgroundColor(mContext.getResources().getColor(R.color.color6C6470));
        }

    }

}
