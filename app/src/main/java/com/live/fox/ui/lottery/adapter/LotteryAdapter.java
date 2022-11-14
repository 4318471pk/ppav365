package com.live.fox.ui.lottery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;


import com.live.fox.R;


import java.util.List;

public class LotteryAdapter extends BaseAdapter {

    Context context;
    List<String> data;


    public LotteryAdapter(Context context, List<String> data){
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

//        Glide.with(context).load(data.get(position).getUlr()).error(data.get(position).getImg()).into(photo);



        return view;
    }

}
