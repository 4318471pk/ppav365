package com.live.fox.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.RechargeTypeListBean;

import java.util.List;

public class PayWayAdapter extends BaseQuickAdapter<RechargeTypeListBean, BaseViewHolder> {


    public PayWayAdapter(List data) {
        super(R.layout.item_pay_way,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeTypeListBean data) {

        RelativeLayout mView = helper.getView(R.id.mView);
        TextView tvName = helper.getView(R.id.tvName);
        ImageView ivPay = helper.getView(R.id.iv);
        if (data.isSelect()){
            mView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_b763c3_8c64bf));
            tvName.setTextColor(mContext.getResources().getColor(R.color.white));
            ivPay.setVisibility(View.VISIBLE);
        } else {
            mView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_f4f1f8));
            tvName.setTextColor(mContext.getResources().getColor(R.color.colorA800FF));
            ivPay.setVisibility(View.GONE);
        }
        tvName.setText(data.getName());

    }

}
