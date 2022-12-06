package com.live.fox.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.RoomListBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ImageUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.AnchorRoundImageView;
import com.live.fox.view.GradientTextView;

import java.util.List;

public class FollowAnchorListAdapter extends BaseQuickAdapter<RoomListBean, BaseViewHolder> {

    Context context;
    int itemWidth;
    Bitmap clock,diamond,ticket;
    int defaultDrawable;
    int dip10,dip13,diamondWidth,diamondHeight;


    public FollowAnchorListAdapter(Context context,  List<RoomListBean> data) {
        super(R.layout.item_anchor_list, data);
        this.context=context;
        itemWidth= (com.live.fox.utils.ScreenUtils.getScreenWidth(context))/2;
        ticket= BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_ticket);
        clock= BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_clock);
        diamond=BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_diamond);
        defaultDrawable=R.mipmap.icon_anchor_loading;
        dip10= com.live.fox.utils.ScreenUtils.dp2px(context,10);
        dip13= com.live.fox.utils.ScreenUtils.dp2px(context,13);
        diamondWidth= com.live.fox.utils.device.ScreenUtils.getDip2px(context,12.5f);
        diamondHeight= com.live.fox.utils.device.ScreenUtils.getDip2px(context,9.5f);
    }

    @Override
    public int addHeaderView(View header) {
        return super.addHeaderView(header);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomListBean data) {
        if (data == null) return;

        ViewGroup.LayoutParams vl= helper.itemView.getLayoutParams();
        vl.width=itemWidth;
        vl.height=itemWidth;
        helper.itemView.setLayoutParams(vl);
        int position=helper.getLayoutPosition()-getHeaderLayoutCount();
        int right=position%2==0?0:dip10/2;

        helper.itemView.setPadding(dip10/2,dip10/2,right,dip10/2);

        GradientTextView gtvUnitPrice = helper.getView(R.id.gtvUnitPrice);  //类别
        GradientTextView tvAnchorPaymentType=helper.getView(R.id.tvAnchorPaymentType);
        AnchorRoundImageView ivRoundBG = helper.getView(R.id.ivRoundBG);
        TextView tvNum=helper.getView(R.id.tvNum);
        tvNum.setText(data.getLiveSum()+"");

        helper.setText(R.id.tv_nickname,data.getTitle());

        ivRoundBG.setRadius(dip10);
        GlideUtils.loadRoundedImage(mContext, dip10,data.getRoomIcon(),0,defaultDrawable, ivRoundBG);

        setPaymentType(data,gtvUnitPrice,tvAnchorPaymentType);

    }

    private void setPaymentType(RoomListBean data,GradientTextView gtvUnitPrice,GradientTextView tvAnchorPaymentType)
    {
        SpanUtils spUtils=new SpanUtils();
        //房间类型:0普通房间 1计时付费 2场次付费
        switch (data.getRoomType())
        {
            case 0:
                tvAnchorPaymentType.setVisibility(View.INVISIBLE);
                gtvUnitPrice.setVisibility(View.INVISIBLE);
                break;
            case 1:
                gtvUnitPrice.setSolidBackground(0x4c000000, ScreenUtils.getDip2px(context,7.5f));
                spUtils.appendImage(ImageUtils.scale(clock, dip13, dip13),SpanUtils.ALIGN_CENTER);
                spUtils.append(" ").append(data.getRoomPrice()).setAlign(Layout.Alignment.ALIGN_CENTER).append(" ");

                spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_CENTER);
                spUtils.append(context.getResources().getString(R.string.unitPriceMin)).setAlign(Layout.Alignment.ALIGN_CENTER);
                gtvUnitPrice.setText(spUtils.create());
                gtvUnitPrice.setVisibility(View.VISIBLE);
                gtvUnitPrice.setSolidBackground(0x4c000000, ScreenUtils.getDip2px(context,7.5f));

                tvAnchorPaymentType.setVisibility(View.VISIBLE);
                tvAnchorPaymentType.setText(context.getResources().getString(R.string.charge_on_time));
                tvAnchorPaymentType.setSolidBackground(0x4c000000, ScreenUtils.getDip2px(context,7.5f));

                break;
            case 2:
                gtvUnitPrice.setSolidBackground(0x4cBF003A, ScreenUtils.getDip2px(context,7.5f));

                gtvUnitPrice.setSolidBackground(0x4c000000, ScreenUtils.getDip2px(context,10));
                spUtils.appendImage(ImageUtils.scale(ticket, dip13, dip13),SpanUtils.ALIGN_CENTER);
                spUtils.append(" ").append(data.getRoomPrice()).setFontSize(11,true).setAlign(Layout.Alignment.ALIGN_CENTER).append(" ");

                spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_CENTER);
                spUtils.append(context.getResources().getString(R.string.unitPriceShow)).setFontSize(11,true).setAlign(Layout.Alignment.ALIGN_CENTER);
                gtvUnitPrice.setText(spUtils.create());
                gtvUnitPrice.setVisibility(View.VISIBLE);
                gtvUnitPrice.setSolidBackground(0x4cBF003A, ScreenUtils.getDip2px(context,7.5f));

                tvAnchorPaymentType.setVisibility(View.VISIBLE);
                tvAnchorPaymentType.setText(context.getString(R.string.charge_per_site));
                tvAnchorPaymentType.setSolidBackground(0x4cBF003A, ScreenUtils.getDip2px(context,7.5f));

                break;
            default:
                tvAnchorPaymentType.setVisibility(View.GONE);
                gtvUnitPrice.setVisibility(View.GONE);
        }
    }
}
