package com.live.fox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.live.fox.R;
import com.live.fox.entity.ChargeCoinBean;

import java.util.List;

/*
* 充值页面
* */
public class ChargeAdapter extends BaseAdapter {

    Context context;
    List<ChargeCoinBean.RechargeOptional> data;
    private boolean isMoney;

    public ChargeAdapter(Context context, List<ChargeCoinBean.RechargeOptional> data, boolean isMoney){
        this.isMoney= isMoney;
        this.context = context;
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            if (isMoney) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_charge, parent, false);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_charge_diamond, parent, false);
            }
        }
        view = convertView;
        if (isMoney) {
            TextView tv = view.findViewById(R.id.tv);
            RoundLinearLayout layout = view.findViewById(R.id.layout);
            if (data.get(position).isSelect()) {
                 layout.getDelegate().setBackgroundColor(context.getResources().getColor(R.color.colorFF9DC2));
            } else {
                 layout.getDelegate().setBackgroundColor(context.getResources().getColor(R.color.white));
            }
          tv.setText(data.get(position).getAmount() + "");
        } else {
            TextView tvDiamond = view.findViewById(R.id.tv_diamond);
            TextView tvMoney = view.findViewById(R.id.tv_money);
            tvDiamond.setText(data.get(position).getAmount() * 10+ "  "+ context.getResources().getString(R.string.zuan));
            tvMoney.setText(data.get(position).getAmount()+ "  "+ context.getResources().getString(R.string.yuan));
        }
        return view;
    }
}
