package com.live.fox.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.GradientTextView;

import java.util.List;

public class FollowAnchorListAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    Context context;
    int itemWidth,dip5;
    Drawable clock,diamond;
    boolean hasHead=false;


    public FollowAnchorListAdapter(Context context,  List<T> data) {
        super(R.layout.item_anchor_list, data);
        itemWidth= (ScreenUtils.getScreenWidth(context)-ScreenUtils.getDip2px(context,15))/2;
        this.context=context;
        clock=context.getResources().getDrawable(R.mipmap.icon_clock);
        diamond=context.getResources().getDrawable(R.mipmap.icon_diamond);
        dip5=ScreenUtils.getDip2px(context,5);
    }

    @Override
    public int addHeaderView(View header) {
        if(header!=null)
        {
            hasHead=true;
        }
        return super.addHeaderView(header);
    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        Log.e("convert",helper.getLayoutPosition()+" "+helper.getAdapterPosition());
        ViewGroup.LayoutParams vl= helper.itemView.getLayoutParams();
        vl.height=itemWidth;

        ImageView ivRoundBG = helper.getView(R.id.ivRoundBG);
        GradientTextView gtvUnitPrice = helper.getView(R.id.gtvUnitPrice);
        GradientTextView tvAnchorPaymentType = helper.getView(R.id.tvAnchorPaymentType);
        if(helper.getAdapterPosition()%2==1)
        {
            RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) ivRoundBG.getLayoutParams();
            rl.leftMargin=dip5;
            rl.rightMargin=0;
            ivRoundBG.setLayoutParams(rl);
        }
        else
        {
            RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) ivRoundBG.getLayoutParams();
            rl.leftMargin=dip5;
            rl.rightMargin=dip5;
            ivRoundBG.setLayoutParams(rl);
        }

        SpanUtils spUtils=new SpanUtils();
        spUtils.appendImage(clock,SpanUtils.ALIGN_CENTER);
        spUtils.append(" 21 ").setAlign(Layout.Alignment.ALIGN_CENTER);
        spUtils.appendImage(diamond,SpanUtils.ALIGN_CENTER);
        spUtils.append("/分钟").setAlign(Layout.Alignment.ALIGN_CENTER);
        gtvUnitPrice.setText(spUtils.create());

        tvAnchorPaymentType.setText(context.getResources().getString(R.string.payByEachShow));
    }
}
