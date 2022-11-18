package com.live.fox.ui.lottery.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundLinearLayout;
import com.live.fox.R;

import java.util.List;

/*
* 鱼虾蟹开奖
* */
public class NiuNiuAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public NiuNiuAdapter(List data) {
        super(R.layout.item_niuniu, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {
        RoundLinearLayout mView = helper.getView(R.id.mView);
        TextView tvStar1 = helper.getView(R.id.tvStar1);
        TextView tvStar2 = helper.getView(R.id.tvStar2);
        TextView tvNiuNiu = helper.getView(R.id.tvNiuNiu);
        if (helper.getLayoutPosition() == 0) {
            mView.getDelegate().setBackgroundColor(mContext.getResources().getColor(R.color.colorF42C2C));
            mView.getDelegate().setStrokeColor(mContext.getResources().getColor(R.color.colorFFEA00));
            tvStar1.setVisibility(View.VISIBLE);
            tvStar2.setVisibility(View.VISIBLE);
            tvNiuNiu.setText(mContext.getResources().getString(R.string.niuniu));
            tvNiuNiu.setTextColor(mContext.getResources().getColor(R.color.colorFFEA00));
        } else if (helper.getLayoutPosition() == 1) {
            mView.getDelegate().setBackgroundColor(mContext.getResources().getColor(R.color.color0F86FF));
            mView.getDelegate().setStrokeColor(mContext.getResources().getColor(R.color.color0F86FF));
            tvStar1.setVisibility(View.GONE);
            tvStar2.setVisibility(View.GONE);
            tvNiuNiu.setText(mContext.getResources().getString(R.string.wuniu));
            tvNiuNiu.setTextColor(mContext.getResources().getColor(R.color.colorCFCFCF));
        }

    }

}
