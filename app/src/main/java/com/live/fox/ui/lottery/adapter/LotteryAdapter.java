package com.live.fox.ui.lottery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.live.fox.R;
import com.live.fox.entity.LivingLotteryListBean;
import com.live.fox.utils.GlideUtils;
import com.makeramen.roundedimageview.RoundedImageView;


import java.util.List;

public class LotteryAdapter extends BaseAdapter {

    Context context;
    List<LivingLotteryListBean.ItemsBean.ConfigGameBaseListBean> data;


    public LotteryAdapter(Context context, List<LivingLotteryListBean.ItemsBean.ConfigGameBaseListBean> data){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lottery, parent, false);
        }
        view = convertView;

        TextView name = view.findViewById(R.id.name);
        name.setText(data.get(position).getGameName());

        ImageView iv = view.findViewById(R.id.iv);
        GlideUtils.loadCircleImage(context,data.get(position).getSmallImg(),R.mipmap.user_head_error,R.mipmap.user_head_error,iv);

        return view;
    }

}
