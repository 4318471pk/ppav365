package com.live.fox.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.entity.Gift;
import com.live.fox.svga.GiftSendManager;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.TransferUtils;

import java.util.List;


/**
 * 礼物
 * Date: 2019/2/13
 */
public class GiftGridViewAdapter extends RecyclerView.Adapter<GiftGridViewAdapter.GiftViewHolder> {
    private final List<Gift> list;
    private final LayoutInflater inflater;
    private int mCurrentPage; //当前第几页
    private int size;
    private final Context context;
    private OnItemClickListener listener;

    public GiftGridViewAdapter(Context context, List<Gift> list, int currentPage) {
        this.list = list;
        this.mCurrentPage = currentPage;
        size = list.size();
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GiftViewHolder(inflater.inflate(R.layout.item_item_gift_panel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (position >= list.size()) {
            holder.itemView.setVisibility(View.GONE);
            return;
        }
        Gift data = list.get((int) getItemId(position));
        if (data.getGid() == GiftSendManager.ins().getGid() && GiftSendManager.ins().getGiftType() == 0) {
            holder.rl_item_gift_panel_root.setSelected(true);
        } else {
            holder.rl_item_gift_panel_root.setSelected(false);
            holder.iv_item_gift_panel_gift.setImageResource(0);
            LogUtils.e("image-" + data.getCover());
            GlideUtils.loadImage(holder.iv_item_gift_panel_gift.getContext(), data.getCover(), -1, -1, holder.iv_item_gift_panel_gift);
        }

        holder.tv_item_gift_panel_name.setText(data.getGname());

        holder.tv_item_tag1.setVisibility(View.GONE);
        holder.tv_item_tag2.setVisibility(View.GONE);
        holder.tv_item_tag3.setVisibility(View.GONE);

        LogUtils.e("礼物的位置" + position + " ," + data.getTags());
        //1贵族2守护3抢榜4幸运5周星6豪气
        if (!StringUtils.isEmpty(data.getTags()) && !data.getTags().equals("0")) {
            String[] tags = data.getTags().split(",");
            String[] tagsForRemove0 = remove0(tags);

            for (int i = 0; i < tagsForRemove0.length; i++) {
                if (i == 0) {
                    String tagString = TransferUtils.getTag(context,tagsForRemove0[i]);
                    if (!StringUtils.isEmpty(tagString)) {
                        holder.tv_item_tag1.setVisibility(View.VISIBLE);
                        holder.tv_item_tag1.setText(tagString);
                    }
                } else if (i == 1) {
                    String tagString = TransferUtils.getTag(context,tagsForRemove0[i]);
                    if (!StringUtils.isEmpty(tagString)) {
                        holder.tv_item_tag2.setVisibility(View.VISIBLE);
                        holder.tv_item_tag2.setText(tagString);
                    }
                } else if (i == 2) {
                    String tagString = TransferUtils.getTag(context,tagsForRemove0[i]);
                    if (!StringUtils.isEmpty(tagString)) {
                        holder.tv_item_tag3.setVisibility(View.VISIBLE);
                        holder.tv_item_tag3.setText(tagString);
                    }
                } else if (i == 3) {
                    String tagString = TransferUtils.getTag(context,tagsForRemove0[i]);
                    if (!StringUtils.isEmpty(tagString)) {
                        holder.tv_item_tag4.setVisibility(View.VISIBLE);
                        holder.tv_item_tag4.setText(tagString);
                    }
                }
            }
        }

        if (data.getType() == 0) {
            holder.tv_item_gift_panel_value.setText(RegexUtils.westMoney(data.getGoldCoin()));
        } else {
            holder.tv_item_gift_panel_value.setVisibility(View.GONE);
            holder.tv_item_gift_panel_free.setVisibility(View.VISIBLE);
            holder.tv_item_gift_panel_free.setText(holder.tv_item_gift_panel_free.getContext().getString(R.string.an));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(v, position);
            }
        });
    }

    public String[] remove0(String[] tags) {
        //得到旧数组零的个数
        int count = 0;
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].equals(0)) {
                count++;
            }
        }
        //定义新数组,由于这里需要得知新数组的长度，因此必须求出旧数组中零的个数
        String[] newarr = new String[tags.length - count];
        //遍历原来的旧数组
        for (int i = 0, j = 0; i < tags.length; i++) {
            //将旧数组中不等于0的元素赋给新数组
            if (!tags[i].equals(0)) {
                newarr[j] = tags[i];
                j++;
            }
        }
        return newarr;
    }

    @Override
    public long getItemId(int position) {
        return position + mCurrentPage * 8L;
    }

    @Override
    public int getItemCount() {
        //超过8个就只显示8个
        return Math.min((size - mCurrentPage * 8), 8);
    }


    public void updateData(List<Gift> newList) {
        LogUtils.e("刷新 原数据总数:" + this.list.size() + " 新数据总数:" + newList.size());
        this.list.addAll(newList);
        this.size = this.list.size() + newList.size();
        this.mCurrentPage = 0;
        notifyDataSetChanged();
    }


    static class GiftViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_item_gift_panel_root;
        ImageView iv_item_gift_panel_gift;
        TextView tv_item_gift_panel_name, tv_item_gift_panel_value, tv_item_gift_panel_free;
        TextView tv_item_tag1, tv_item_tag2, tv_item_tag3, tv_item_tag4;

        public GiftViewHolder(View itemView) {
            super(itemView);
            rl_item_gift_panel_root = itemView.findViewById(R.id.rl_item_gift_panel_root);
            iv_item_gift_panel_gift = itemView.findViewById(R.id.iv_item_gift_panel_gift);
            tv_item_gift_panel_name = itemView.findViewById(R.id.tv_item_gift_panel_name);
            tv_item_gift_panel_value = itemView.findViewById(R.id.tv_item_gift_panel_value);
            tv_item_gift_panel_free = itemView.findViewById(R.id.tv_item_gift_panel_free);
            tv_item_tag1 = itemView.findViewById(R.id.tv_item_tag1);
            tv_item_tag2 = itemView.findViewById(R.id.tv_item_tag2);
            tv_item_tag3 = itemView.findViewById(R.id.tv_item_tag3);
            tv_item_tag4 = itemView.findViewById(R.id.tv_item_tag4);
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
