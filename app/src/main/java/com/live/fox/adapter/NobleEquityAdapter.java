package com.live.fox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.roundview.RoundLinearLayout;
import com.live.fox.LiveFinishActivity;
import com.live.fox.R;
import com.live.fox.entity.ChargeCoinBean;
import com.live.fox.entity.NobleEquityBean;
import com.live.fox.utils.GlideUtils;

import java.util.List;

/*
* 贵族权益
* */
public class NobleEquityAdapter extends BaseAdapter {

    Context context;
    List<NobleEquityBean> data;
    int type;
    String nobleName;

    public NobleEquityAdapter(Context context, List<NobleEquityBean> data, String nobleName){
        this.context = context;
        this.data = data;
        this.nobleName = nobleName;
    }

    public void setType(int type){
        this.type = type;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_noble_equity, parent, false);
        }
        view = convertView;

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(data.get(position).getTitle());
        TextView tvTips= view.findViewById(R.id.tvTips);
        if (position == 10) {
            tvTips.setText(nobleName + context.getString(R.string.noble_equity_tips_11_2));
        } else {
            tvTips.setText(data.get(position).getTips());
        }
        ImageView img = view.findViewById(R.id.img);
        ImageView photo = view.findViewById(R.id.photo);
        if (data.get(position).isShowPhoto()) {
            img.setVisibility(View.GONE);
            photo.setVisibility(View.VISIBLE);
           // photo.setImageDrawable(data.get(position).getImg());
            Glide.with(context).load(data.get(position).getUlr()).error(data.get(position).getImg()).into(photo);
        } else {
            img.setVisibility(View.VISIBLE);
            photo.setVisibility(View.GONE);
            img.setImageDrawable(data.get(position).getImg());
        }
        LinearLayout mView = view.findViewById(R.id.mView);
        if (data.get(position).isShow()) {
            mView.setAlpha(1f);
        } else {
            mView.setAlpha(0.2f);
        }


        return view;
    }
}
