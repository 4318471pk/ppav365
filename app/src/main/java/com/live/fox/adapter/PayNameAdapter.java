package com.live.fox.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.RechargeTypeBean;
import com.live.fox.utils.GlideUtils;

import java.util.List;

public class PayNameAdapter extends BaseQuickAdapter<RechargeTypeBean, BaseViewHolder> {


    public PayNameAdapter(List data) {
        super(R.layout.item_pay_name, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeTypeBean data) {
        LinearLayout mView = helper.getView(R.id.mView);
        TextView tvName = helper.getView(R.id.tvName);
        if (data.isSelect()){
            mView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_b763c3_8c64bf));
            tvName.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            mView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_f4f1f8));
            tvName.setTextColor(mContext.getResources().getColor(R.color.colorA800FF));
        }


        tvName.setText(data.getName());
        ImageView ivPay = helper.getView(R.id.ivPay);
        GlideUtils.loadImage(mContext, data.getLogs(), ivPay);


    }

}
