package com.live.fox.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.ActBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.TimeUtils;
import com.live.fox.view.RectangleImageWithErrorPic;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DialogPromoAdapter extends BaseQuickAdapter<ActBean, BaseViewHolder> {

    Context context;
    int dip10;

    public DialogPromoAdapter(Context context, List<ActBean> data) {
        super(R.layout.item_dialog_promo, data);
        this.context=context;
        dip10= ScreenUtils.dp2px(context,10);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void convert(BaseViewHolder helper, ActBean data) {
        RoundedImageView iv = helper.getView(R.id.rivImage);//

        Glide.with(mContext).load(Strings.urlConnect(data.getImgUrl())).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull @NotNull Drawable resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Drawable> transition) {
                iv.setBackgroundColor(0xffffffff);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setImageDrawable(resource);
                iv.setScaleX(1f);
                iv.setScaleY(1f);
            }

            @Override
            public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                iv.setImageDrawable(context.getResources().getDrawable(R.mipmap.img_error));
                iv.setScaleX(0.8f);
                iv.setScaleY(0.8f);
//                iv.setBackgroundColor(0xffD2CDE0);
            }
        });

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
