package com.live.fox.ui.lottery.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.SelectLotteryBean;

import java.util.List;

public class BeishuAdapter extends BaseQuickAdapter<SelectLotteryBean, BaseViewHolder> {


    public BeishuAdapter(List data) {
        super(R.layout.item_beishu, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectLotteryBean data) {

        TextView tvBeishu = helper.getView(R.id.tvBeishu);
        if (data.isSelect()) {
            tvBeishu.setBackground(mContext.getResources().getDrawable(R.drawable.bg_raido_a800ff_d689ff));
            tvBeishu.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            tvBeishu.setBackground(mContext.getResources().getDrawable(R.drawable.bg_f4f1f8));
            tvBeishu.setTextColor(mContext.getResources().getColor(R.color.color404040));
        }

        if (data.isZdy()) {
            if (TextUtils.isEmpty(data.getName())) {
                tvBeishu.setText(mContext.getText(R.string.edit));
            } else  {
                tvBeishu.setText(data.getName() + mContext.getText(R.string.bei));
            }
        } else {
            tvBeishu.setText(data.getName() + mContext.getText(R.string.bei));
        }


    }


}
