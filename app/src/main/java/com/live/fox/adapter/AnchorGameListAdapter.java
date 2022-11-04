package com.live.fox.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.RoomListBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.GradientTextView;

import java.util.List;

public class AnchorGameListAdapter extends BaseQuickAdapter<RoomListBean, BaseViewHolder> {
    Context context;
    int itemWidth;
    Drawable clock,diamond;
    int defaultDrawable;

    public AnchorGameListAdapter(Context context,  List<RoomListBean> data) {
        super(R.layout.item_anchor_list, data);
        this.context=context;
        itemWidth= (ScreenUtils.getScreenWidth(context)-ScreenUtils.getDip2px(context,15))/2;
        clock=context.getResources().getDrawable(R.mipmap.icon_clock);
        diamond=context.getResources().getDrawable(R.mipmap.icon_diamond);
        defaultDrawable=R.mipmap.icon_anchor_loading;
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomListBean data) {

        ViewGroup.LayoutParams vl= helper.itemView.getLayoutParams();
        vl.width=itemWidth;
        vl.height=itemWidth;

        GradientTextView gtvUnitPrice = helper.getView(R.id.gtvUnitPrice);
        GradientTextView tvAnchorPaymentType = helper.getView(R.id.tvAnchorPaymentType);

        ImageView ivRoundBG = helper.getView(R.id.ivRoundBG);

        SpanUtils spUtils=new SpanUtils();
        spUtils.appendImage(clock,SpanUtils.ALIGN_CENTER);
        spUtils.append(" 21 ").setAlign(Layout.Alignment.ALIGN_CENTER);
        spUtils.appendImage(diamond,SpanUtils.ALIGN_CENTER);
        spUtils.append("/分钟").setAlign(Layout.Alignment.ALIGN_CENTER);
        gtvUnitPrice.setText(spUtils.create());

        tvAnchorPaymentType.setText(context.getResources().getString(R.string.payByEachShow));

        helper.setText(R.id.tv_nickname,data.getTitle());

        if(TextUtils.isEmpty(data.getRoomIcon()))
        {
            ivRoundBG.setImageDrawable(context.getResources().getDrawable(defaultDrawable));
        }
        else
        {
            GlideUtils.loadDefaultImage(mContext, data.getRoomIcon(),defaultDrawable, ivRoundBG);
        }

    }
}
