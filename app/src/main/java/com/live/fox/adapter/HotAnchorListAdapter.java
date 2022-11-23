package com.live.fox.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.RoomListBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.AnchorRoundImageView;
import com.live.fox.view.GradientTextView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.GradientTextView;

import java.util.List;

public class HotAnchorListAdapter extends BaseQuickAdapter<RoomListBean, BaseViewHolder> {
    Context context;
    int itemWidth;
    Drawable clock,diamond;
    int defaultDrawable,dip10;

    public HotAnchorListAdapter(Context context,  List<RoomListBean> data) {
        super(R.layout.item_anchor_list, data);
        this.context=context;
        itemWidth= (ScreenUtils.getScreenWidth(context)-ScreenUtils.dp2px(context,15))/2;
        clock=context.getResources().getDrawable(R.mipmap.icon_clock);
        diamond=context.getResources().getDrawable(R.mipmap.icon_diamond);
        defaultDrawable=R.mipmap.icon_anchor_loading;
        dip10=ScreenUtils.dp2px(context,10);
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

        //1普通房间2密码房间3计时房间4贵族房间5计场房间
        switch (data.getRoomType())
        {
            case 1:
            case 2:
            case 4:
                tvAnchorPaymentType.setVisibility(View.GONE);
                gtvUnitPrice.setVisibility(View.GONE);
                break;
            case 3:
                tvAnchorPaymentType.setVisibility(View.VISIBLE);
                tvAnchorPaymentType.setText(context.getString(R.string.charge_on_time));
                gtvUnitPrice.setVisibility(View.VISIBLE);
                break;
            case 5:
                tvAnchorPaymentType.setVisibility(View.VISIBLE);
                tvAnchorPaymentType.setText(context.getString(R.string.charge_per_site));
                gtvUnitPrice.setVisibility(View.VISIBLE);
                break;
            default:
                tvAnchorPaymentType.setVisibility(View.GONE);
                gtvUnitPrice.setVisibility(View.GONE);
                break;
        }

        SpanUtils spUtils=new SpanUtils();
        spUtils.appendImage(clock,SpanUtils.ALIGN_CENTER);
        spUtils.append(" 21 ").setAlign(Layout.Alignment.ALIGN_CENTER);
        spUtils.appendImage(diamond,SpanUtils.ALIGN_CENTER);
        spUtils.append("/分钟").setAlign(Layout.Alignment.ALIGN_CENTER);
        gtvUnitPrice.setText(spUtils.create());

        helper.setText(R.id.tv_nickname,data.getTitle());

        if(TextUtils.isEmpty(data.getRoomIcon()))
        {
            ivRoundBG.setImageDrawable(context.getResources().getDrawable(defaultDrawable));
        }
        else
        {
            ivRoundBG.setRadius(dip10);
            GlideUtils.loadRoundedImage(mContext, dip10,data.getRoomIcon(),defaultDrawable,defaultDrawable, ivRoundBG);
        }
    }
}
