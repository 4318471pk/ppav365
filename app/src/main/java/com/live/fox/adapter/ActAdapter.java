package com.live.fox.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.ActBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.TimeUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ActAdapter extends BaseQuickAdapter<ActBean, BaseViewHolder> {

    int defaultDrawable;
    public ActAdapter(List data) {
        super(R.layout.item_activity, data);
        defaultDrawable = R.mipmap.img_error;
    }

    @Override
    protected void convert(BaseViewHolder helper, ActBean data) {
        RoundedImageView iv = helper.getView(R.id.iv);//
        Glide.with(mContext).load(data.getImgUrl()).error(defaultDrawable).listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                return false;
            }
        }).into(iv);
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
