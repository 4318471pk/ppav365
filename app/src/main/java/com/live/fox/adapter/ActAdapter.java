package com.live.fox.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.ActBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.TimeUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ActAdapter extends BaseQuickAdapter<ActBean, BaseViewHolder> {


    public ActAdapter(List data) {
        super(R.layout.item_activity, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActBean data) {
        RoundedImageView iv = helper.getView(R.id.iv);
        GlideUtils.loadDefaultImage(mContext, data.getImgUrl(), iv);
        TextView tv = helper.getView(R.id.tvTime);
        if (data.getActivityStatus() == 1) {
            tv.setTextColor(mContext.getResources().getColor(R.color.color646464));
            if (data.getActivityType() == 1) {
                tv.setText(mContext.getResources().getString(R.string.long_time_act));
            } else if (data.getActivityType() == 2) {
                try {
                    String b = TimeUtils.getDate2(data.getBeginTime());
                    String e = TimeUtils.getDate2(data.getEndTime());
                    tv.setText(b + " ~ " + e);
                } catch (Exception e) {

                }

            }
        } else {
            tv.setText(mContext.getResources().getString(R.string.act_finish));
            tv.setTextColor(mContext.getResources().getColor(R.color.colorF42C2C));
        }


    }

}
