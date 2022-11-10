package com.live.fox.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.entity.TreasureItemBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ScreenUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class TreasureBoxPagerAdapter extends RecyclerView.Adapter<TreasureBoxPagerAdapter.TreasureBoxHolder> {

    Context context;
    List<List<? extends TreasureItemBean>> lists;
    int pageIndex=0;
    LayoutInflater layoutInflater;
    int itemHeight;
    int itemWidth;

    public TreasureBoxPagerAdapter(Activity context,int viewPagerHeight,int pageIndex, List<List<? extends TreasureItemBean>> lists) {
        this.lists=lists;
        this.context=context;
        layoutInflater=context.getLayoutInflater();
        this.itemHeight=viewPagerHeight/2;
        this.pageIndex=pageIndex;
        itemWidth= ScreenUtils.getScreenWidth(context)/4;

    }

    public List<List<? extends TreasureItemBean>> getLists() {
        return lists;
    }

    @NonNull
    @Override
    public TreasureBoxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= layoutInflater.inflate( R.layout.item_treasurebox_pager,parent,false);

        return new  TreasureBoxHolder(context,itemHeight,itemWidth,view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreasureBoxHolder holder, int position) {
            if(lists.size()<=pageIndex || lists.get(pageIndex)==null || lists.get(pageIndex).size()==0)
            {
                holder.gridLayout.setVisibility(View.GONE);
                holder.llEmpty.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.gridLayout.setVisibility(View.VISIBLE);
                holder.llEmpty.setVisibility(View.GONE);
                for (int i = 0; i < holder.gridLayout.getChildCount(); i++) {
                    LinearLayout llContent=holder.gridLayout.getChildAt(i).findViewById(R.id.llContent);

                    if(position*8+i<lists.get(pageIndex).size())
                    {
                        llContent.setVisibility(View.VISIBLE);
                        if(lists.get(pageIndex).get(position*8+i).isSelected())
                        {
                            llContent.setBackground(context.getResources().getDrawable(R.drawable.round_stroke_ff008a));
                        }
                        else
                        {
                            llContent.setBackground(null);
                        }

                        RoundedImageView rivGiftImg= llContent.findViewById(R.id.rivGiftImg);
                        TextView tvGiftName= llContent.findViewById(R.id.tvGiftName);
                        TextView tvCostDiamond= llContent.findViewById(R.id.tvCostDiamond);

                        TreasureItemBean treasureItemBean=lists.get(pageIndex).get(position*8+i);
                        tvGiftName.setText(treasureItemBean.getName());
                        if(TextUtils.isEmpty(treasureItemBean.getImgUrl()))
                        {

                        }
                        else
                        {
                            GlideUtils.loadDefaultImage(context,treasureItemBean.getImgUrl(),R.mipmap.img_error,rivGiftImg);
                        }

                        tvCostDiamond.setText(treasureItemBean.getCostDiamond()+"");
                    }
                    else
                    {
                        llContent.setVisibility(View.INVISIBLE);
                    }

                    llContent.setTag(i);
                    llContent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int j = 0; j < lists.get(pageIndex).size(); j++) {
                                lists.get(pageIndex).get(j).setSelected(false);
                            }
                            int index=(int)v.getTag();
                            lists.get(pageIndex).get(position*8+index).setSelected(true);
                            LinearLayout llContent=v.findViewById(R.id.llContent);
                            llContent.setBackground(context.getResources().getDrawable(R.drawable.round_stroke_ff008a));
                            notifyDataSetChanged();
                        }
                    });
                }

            }


    }

    @Override
    public int getItemCount() {
        if(lists.size()<=pageIndex || lists.get(pageIndex)==null || lists.get(pageIndex).size()==0)
        {
            return 1;
        }

        int totalSize=lists.get(pageIndex).size();
        int pageSize=totalSize%8==0?(totalSize/8):(totalSize/8+1);
        return pageSize;
    }

    public static final class TreasureBoxHolder extends RecyclerView.ViewHolder {

        GridLayout gridLayout;
        LinearLayout llEmpty;
        public TreasureBoxHolder(Context context,int itemHeight,int itemWidth, @NonNull View itemView) {
            super(itemView);
            llEmpty=itemView.findViewById(R.id.llEmpty);
            gridLayout=itemView.findViewById(R.id.gridLayout);
            gridLayout.setColumnCount(4);

            gridLayout.removeAllViews();
            for (int i = 0; i < 8; i++) {
                View view=View.inflate(context,R.layout.item_treasure_list_dialog,null);
                gridLayout.addView(view,itemWidth,itemHeight);
            }
        }
    }


}
