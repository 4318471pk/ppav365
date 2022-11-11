package com.live.fox.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.entity.RoomListBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.AnchorRoundImageView;
import com.live.fox.view.GradientTextView;

import java.util.List;

public class RecommendLivingAnchorAdapter extends RecyclerView.Adapter<RecommendLivingAnchorAdapter.RecommendListHolder> {

    Context context;
    Drawable clock,diamond;
    int itemWidth,dip5,defaultDrawable;
    List<RoomListBean> data;
    LayoutInflater layoutInflater;
    OnItemClickListener onItemClickListener;

    public RecommendLivingAnchorAdapter(BaseActivity context, @Nullable List<RoomListBean> data) {
        this.data=data;
        this.context=context;
        itemWidth= ScreenUtils.getDip2px(context,112);
        clock=context.getResources().getDrawable(R.mipmap.icon_clock);
        diamond=context.getResources().getDrawable(R.mipmap.icon_diamond);
        dip5=ScreenUtils.getDip2px(context,5);
        layoutInflater=context.getLayoutInflater();
        defaultDrawable=R.mipmap.icon_anchor_loading;
    }

    public void setNewData(List<RoomListBean> data)
    {
        this.data=data;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecommendListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_recommend_layout,parent,false);
        return new RecommendListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendListHolder holder, int position) {

        ViewGroup.LayoutParams vl= holder.itemView.getLayoutParams();
        vl.height=itemWidth;
        vl.width=itemWidth;

        TextView name = holder.itemView.findViewById(R.id.tvName);
        name.setText(data.get(position).getTitle());

        TextView gtvLocation=holder.itemView.findViewById(R.id.gtvLocation);
        gtvLocation.setText(context.getString(R.string.mars));


        AnchorRoundImageView ivRoundBG=holder.itemView.findViewById(R.id.ivRoundBG);
        ivRoundBG.setRadius(dip5*2);
        GlideUtils.loadRoundedImage(context, dip5*2,data.get(position).getRoomIcon(),0,defaultDrawable, ivRoundBG);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                if(onItemClickListener!=null)
                {
                    int position=(int)view.getTag();
                    onItemClickListener.onClick(position,data);
                }
            }
        });
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

    public interface OnItemClickListener
    {
        void onClick(int position,List<RoomListBean> data);
    }
}
