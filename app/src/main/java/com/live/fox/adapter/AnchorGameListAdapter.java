package com.live.fox.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.RoomListBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ImageUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.AnchorRoundImageView;
import com.live.fox.view.GradientTextView;

import java.util.List;

public class AnchorGameListAdapter extends BaseQuickAdapter<RoomListBean, BaseViewHolder> {
    Context context;
    int itemWidth;
    Bitmap clock,diamond,ticket;
    int defaultDrawable,dip10,dip13,diamondWidth,diamondHeight;

    public AnchorGameListAdapter(Context context,  List<RoomListBean> data) {
        super(R.layout.item_anchor_list, data);
        this.context=context;
        itemWidth= (ScreenUtils.getScreenWidth(context)-ScreenUtils.dp2px(context,15))/2;
        defaultDrawable=R.mipmap.icon_anchor_loading;
        dip10=ScreenUtils.dp2px(context,10);
        dip13= ScreenUtils.dp2px(context,13);
        diamondWidth= com.live.fox.utils.device.ScreenUtils.getDip2px(context,12.5f);
        diamondHeight= com.live.fox.utils.device.ScreenUtils.getDip2px(context,9.5f);
        ticket= BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_ticket);
        clock= BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_clock);
        diamond=BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_diamond);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomListBean data) {

        ViewGroup.LayoutParams vl= helper.itemView.getLayoutParams();
        vl.width=itemWidth;
        vl.height=itemWidth;

        GradientTextView gtvUnitPrice = helper.getView(R.id.gtvUnitPrice);
        GradientTextView tvAnchorPaymentType = helper.getView(R.id.tvAnchorPaymentType);

        AnchorRoundImageView ivRoundBG = helper.getView(R.id.ivRoundBG);
        TextView tvNum=helper.getView(R.id.tvNum);
        tvNum.setText(data.getLiveSum()+"");

        setPaymentType(data,gtvUnitPrice,tvAnchorPaymentType);

        helper.setText(R.id.tv_nickname,data.getTitle());

        if(TextUtils.isEmpty(data.getRoomIcon()))
        {
            ivRoundBG.setImageDrawable(context.getResources().getDrawable(defaultDrawable));
        }
        else
        {
            ivRoundBG.setRadius(dip10);
            GlideUtils.loadRoundedImage(mContext, dip10,data.getRoomIcon(),0,defaultDrawable, ivRoundBG);
        }

    }


    private void setPaymentType(RoomListBean data,GradientTextView gtvUnitPrice,GradientTextView tvAnchorPaymentType)
    {
        SpanUtils spUtils=new SpanUtils();
        //????????????:0???????????? 1???????????? 2????????????
        switch (data.getRoomType())
        {
            case 0:
                tvAnchorPaymentType.setVisibility(View.INVISIBLE);
                gtvUnitPrice.setVisibility(View.INVISIBLE);
                break;
            case 1:
                gtvUnitPrice.setSolidBackground(0x4c000000, ScreenUtils.dp2px(context,7.5f));
                spUtils.appendImage(ImageUtils.scale(clock, dip13, dip13),SpanUtils.ALIGN_CENTER);
                spUtils.append(" ").append(data.getRoomPrice()).setAlign(Layout.Alignment.ALIGN_CENTER).append(" ");

                spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_CENTER);
                spUtils.append(context.getResources().getString(R.string.unitPriceMin)).setAlign(Layout.Alignment.ALIGN_CENTER);
                gtvUnitPrice.setText(spUtils.create());
                gtvUnitPrice.setVisibility(View.VISIBLE);
                gtvUnitPrice.setSolidBackground(0x4c000000, ScreenUtils.dp2px(context,7.5f));

                tvAnchorPaymentType.setVisibility(View.VISIBLE);
                tvAnchorPaymentType.setText(context.getResources().getString(R.string.charge_on_time));
                tvAnchorPaymentType.setSolidBackground(0x4c000000, ScreenUtils.dp2px(context,7.5f));

                break;
            case 2:
                gtvUnitPrice.setSolidBackground(0x4cBF003A, ScreenUtils.dp2px(context,7.5f));

                gtvUnitPrice.setSolidBackground(0x4c000000, ScreenUtils.dp2px(context,10));
                spUtils.appendImage(ImageUtils.scale(ticket, dip13, dip13),SpanUtils.ALIGN_CENTER);
                spUtils.append(" ").append(data.getRoomPrice()).setFontSize(11,true).setAlign(Layout.Alignment.ALIGN_CENTER).append(" ");

                spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_CENTER);
                spUtils.append(context.getResources().getString(R.string.unitPriceShow)).setFontSize(11,true).setAlign(Layout.Alignment.ALIGN_CENTER);
                gtvUnitPrice.setText(spUtils.create());
                gtvUnitPrice.setVisibility(View.VISIBLE);
                gtvUnitPrice.setSolidBackground(0x4cBF003A, ScreenUtils.dp2px(context,7.5f));

                tvAnchorPaymentType.setVisibility(View.VISIBLE);
                tvAnchorPaymentType.setText(context.getString(R.string.charge_per_site));
                tvAnchorPaymentType.setSolidBackground(0x4cBF003A, ScreenUtils.dp2px(context,7.5f));

                break;
            default:
                tvAnchorPaymentType.setVisibility(View.GONE);
                gtvUnitPrice.setVisibility(View.GONE);
        }
    }
}
