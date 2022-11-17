package com.live.fox.ui.lottery.adapter;

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
import com.live.fox.ui.lottery.TouzhuItemListFragment;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.device.DeviceUtils;

import java.util.List;

public class TouzhuDetailAdapter extends BaseAdapter {

    Context context;
    List<Boolean> data;
    int showItemNum = 7;

    int viewType = TouzhuItemListFragment.VIEW_NORMAIL;

    public TouzhuDetailAdapter(Context context, List<Boolean> data){
        this.context = context;
        this.data = data;
    }

    public void setViewType(int type) {
        this.viewType = type;
    }

    public void setShowItemNum(int num) {
        showItemNum = num;
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
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        int type = getItemViewType(position);
        if (type == TouzhuItemListFragment.VIEW_YXX_2) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_yuxiaxie, parent, false);
            }
            view = convertView;
            RoundLinearLayout layoutBg = view.findViewById(R.id.layoutBg);

            if (data.get(position)) {
                layoutBg.getDelegate().setStrokeWidth(3);
            } else {
                layoutBg.getDelegate().setStrokeWidth(ScreenUtils.dp2px(context, 0));
            }

        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_touzhu_detail, parent, false);
            }
            view = convertView;

            TextView tvsShow = view.findViewById(R.id.tvShow);

            RoundTextView tvTz = view.findViewById(R.id.tvTz);

            if (viewType == TouzhuItemListFragment.VIEW_YXX_1) {
                if (data.get(position)) {
                    tvTz.getDelegate().setStrokeColor(context.getResources().getColor(R.color.colorF42C2C));
                } else {
                    tvTz.getDelegate().setStrokeColor(context.getResources().getColor(R.color.color7A757F));
                }
            } else {
                if (data.get(position)) {
                    tvTz.getDelegate().setBackgroundColor(context.getResources().getColor(R.color.white));
                    tvTz.setTextColor(context.getResources().getColor(R.color.color404040));
                } else {
                    tvTz.getDelegate().setBackgroundColor(context.getResources().getColor(R.color.color7A757F));
                    tvTz.setTextColor(context.getResources().getColor(R.color.white));
                }
            }

            LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) tvTz.getLayoutParams();
            if (showItemNum == 7) {
                linearParams.height= DeviceUtils.dp2px(context, 42);
                linearParams.width= DeviceUtils.dp2px(context, 42);
            } else if(showItemNum == 4 || showItemNum == 3 ) {
                if (data.size() <= 4) {
                    linearParams.height= DeviceUtils.dp2px(context, 70);
                    linearParams.width= DeviceUtils.dp2px(context, 70);
                } else {
                    linearParams.height= DeviceUtils.dp2px(context, 60);
                    linearParams.width= DeviceUtils.dp2px(context, 60);
                }

            }

            tvTz.setLayoutParams(linearParams);
        }




        return view;
    }

}
