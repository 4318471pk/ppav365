package com.live.fox.ui.lottery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.live.fox.R;
import com.live.fox.entity.SelectLotteryBean;
import com.live.fox.utils.device.DeviceUtils;

import java.util.List;

public class SelectLotteryAdapter extends BaseAdapter {

    Context context;
    List<SelectLotteryBean> data;



    public SelectLotteryAdapter(Context context, List<SelectLotteryBean> data){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_lottery, parent, false);
        }
        view = convertView;


        RoundTextView tvName = view.findViewById(R.id.tvName);
        tvName.setText(data.get(position).getName());
        if (data.get(position).isSelect()) {
            tvName.getDelegate().setBackgroundColor(context.getResources().getColor(R.color.colorA800FF));
            tvName.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            tvName.getDelegate().setBackgroundColor(context.getResources().getColor(R.color.white));
            tvName.setTextColor(context.getResources().getColor(R.color.color404040));
        }

        return view;
    }

}
