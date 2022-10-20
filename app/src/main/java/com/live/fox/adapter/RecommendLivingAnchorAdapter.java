package com.live.fox.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.GradientTextView;

import java.util.List;

public class RecommendLivingAnchorAdapter<T> extends RecyclerView.Adapter<RecommendLivingAnchorAdapter.RecommendListHolder> {

    Context context;
    Drawable clock,diamond;
    int itemWidth,dip5;
    List<T> data;
    LayoutInflater layoutInflater;

    public RecommendLivingAnchorAdapter(BaseActivity context, @Nullable List<T> data) {
        this.data=data;
        this.context=context;
        itemWidth= ScreenUtils.getDip2px(context,112);
        clock=context.getResources().getDrawable(R.mipmap.icon_clock);
        diamond=context.getResources().getDrawable(R.mipmap.icon_diamond);
        dip5=ScreenUtils.getDip2px(context,5);
        layoutInflater=context.getLayoutInflater();
    }


    @NonNull
    @Override
    public RecommendListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_anchor_list,parent,false);
        return new RecommendListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendListHolder holder, int position) {

        ViewGroup.LayoutParams vl= holder.itemView.getLayoutParams();
        vl.height=itemWidth;
        vl.width=itemWidth;

        GradientTextView gtvUnitPrice = holder.itemView.findViewById(R.id.gtvUnitPrice);
        GradientTextView tvAnchorPaymentType = holder.itemView.findViewById(R.id.tvAnchorPaymentType);

        SpanUtils spUtils=new SpanUtils();
        spUtils.appendImage(clock,SpanUtils.ALIGN_CENTER);
        spUtils.append(" 21 ").setAlign(Layout.Alignment.ALIGN_CENTER);
        spUtils.appendImage(diamond,SpanUtils.ALIGN_CENTER);
        spUtils.append("/分钟").setAlign(Layout.Alignment.ALIGN_CENTER);
        gtvUnitPrice.setText(spUtils.create());

        tvAnchorPaymentType.setText(context.getResources().getString(R.string.payByEachShow));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static final class RecommendListHolder extends RecyclerView.ViewHolder
    {

        public RecommendListHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
