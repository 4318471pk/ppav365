package com.live.fox.ui.lottery.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundRelativeLayout;
import com.live.fox.R;
import com.live.fox.entity.SelectLotteryBean;

import java.util.List;

public class ChouMaAdapter extends BaseQuickAdapter<SelectLotteryBean, BaseViewHolder> {


    public ChouMaAdapter(List data) {
        super(R.layout.item_chouma, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectLotteryBean data) {

        RoundRelativeLayout mLayout = helper.getView(R.id.mLayout);
        if (data.isSelect()) {
            mLayout.getDelegate().setStrokeColor(mContext.getResources().getColor(R.color.colorA800FF));
        } else {
            mLayout.getDelegate().setStrokeColor(mContext.getResources().getColor(R.color.white));
        }
        ImageView iv = helper.getView(R.id.ivChouma);
        TextView tvMoney = helper.getView(R.id.tvMoney);
        if (data.isZdy()) {
            iv.setBackground(mContext.getResources().getDrawable(R.mipmap.chouma_zdy));
            if (!TextUtils.isEmpty(data.getName())) {
                tvMoney.setText(data.getName());
                tvMoney.setVisibility(View.VISIBLE);
            } else {
                tvMoney.setVisibility(View.GONE);
            }
        } else {
            tvMoney.setVisibility(View.GONE);
            if (data.getName().equals("2")) {
                iv.setBackground(mContext.getResources().getDrawable(R.mipmap.chouma2));
            } else if (data.getName().equals("5")) {
                iv.setBackground(mContext.getResources().getDrawable(R.mipmap.chouma5));
            } else if (data.getName().equals("10")) {
                iv.setBackground(mContext.getResources().getDrawable(R.mipmap.chouma10));
            } else if (data.getName().equals("50")) {
                iv.setBackground(mContext.getResources().getDrawable(R.mipmap.chouma50));
            } else if (data.getName().equals("100")) {
                iv.setBackground(mContext.getResources().getDrawable(R.mipmap.chouma100));
            } else if (data.getName().equals("500")) {
                iv.setBackground(mContext.getResources().getDrawable(R.mipmap.chouma500));
            } else if (data.getName().equals("1000")) {
                iv.setBackground(mContext.getResources().getDrawable(R.mipmap.chouma1000));
            }
        }

    }

}
