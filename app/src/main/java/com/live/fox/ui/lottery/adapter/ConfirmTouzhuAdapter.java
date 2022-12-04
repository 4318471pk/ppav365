package com.live.fox.ui.lottery.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundRelativeLayout;
import com.live.fox.R;
import com.live.fox.entity.SelectLotteryBean;
import com.live.fox.entity.TouzhuDetailBean;

import java.util.List;

import retrofit2.http.PUT;

public class ConfirmTouzhuAdapter extends BaseQuickAdapter<TouzhuDetailBean, BaseViewHolder> {


    public ConfirmTouzhuAdapter(List data) {
        super(R.layout.item_confirm_touzhu, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TouzhuDetailBean data) {

        TextView tvName = helper.getView(R.id.tvName);
        tvName.setText(data.name);

        TextView tvOdds = helper.getView(R.id.tvOdds);
        tvOdds.setText(data.odds+"");


        TextView tvMoney = helper.getView(R.id.tvMoney);
        tvMoney.setText(data.money);

        tvMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMoneyClick(helper.getLayoutPosition(), "");
                }
            }
        });

        helper.getView(R.id.layout_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.delete(helper.getLayoutPosition());
                }
            }
        });


    }

    public interface ConfirmTouzhuItemListener{
        public void onMoneyClick(int pos, String money);
        public void delete(int pos);
    }

    private ConfirmTouzhuItemListener mListener;

    public void setSelectChouMaSuc(ConfirmTouzhuItemListener listener){
        this.mListener = listener;
    }

}
