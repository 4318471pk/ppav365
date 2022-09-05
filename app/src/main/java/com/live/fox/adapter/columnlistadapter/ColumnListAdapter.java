package com.live.fox.adapter.columnlistadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;

import java.util.List;


/**
 * 分栏Adapter
 * Created by Administrator on 2016/11/9.
 */

public class ColumnListAdapter extends RecyclerView.Adapter<ColumnListAdapter.ViewHolder> {


    private Context context;
    private List<ColumnListBean> data;

    public ColumnListAdapter(Context context, List<ColumnListBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_column, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ColumnListBean column = data.get(position);

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
        holder.itemView.setTag(column.getLeftTitle());

        if (column.isFirst()) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 24, 0, 0);
            holder.view.setLayoutParams(lp);
            holder.ivTopline.setVisibility(View.GONE);
        } else {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 0);
            holder.view.setLayoutParams(lp);
            holder.ivTopline.setVisibility(View.GONE);
        }

        if (column.getStyle() == 0) {
            holder.tvColumnTitle.setVisibility(View.VISIBLE);
            holder.rlColumnlayout.setVisibility(View.GONE);
        } else {
            holder.tvColumnTitle.setVisibility(View.GONE);
            holder.rlColumnlayout.setVisibility(View.VISIBLE);
        }

        //先隐藏所有的
        holder.tvTitle.setVisibility(View.GONE);
        holder.ivLeftIc.setVisibility(View.GONE);
        holder.tvLeftDes.setVisibility(View.GONE);
        holder.tvRightDes.setVisibility(View.GONE);
        holder.ivRightArrow.setVisibility(View.GONE);
        holder.tvRightDes.setVisibility(View.GONE);
        holder.ivRightDes.setVisibility(View.GONE);
        /**
         * style 对应值 显示的Item
         * 0 栏目名称
         * 1 左边标题 (默认)
         * 2 左边标题+左边详情
         * 3 左边标题+右边详情
         *
         * 4 左边标题+左边图标
         * 5 左边标题+左边图标+右边详情
         *
         * 6 左边详情 （左边详情与左边标题都区别在于 标题是纯黑色 详情则是浅色）
         * 7 左边详情+右边详情
         *
         * 11 左边标题               + 右边箭头
         * 21 左边标题+左边详情      + 右边箭头
         * 31 左边标题+右边详情      + 右边箭头
         *
         * 41 左边图标+左边标题      + 右边箭头
         * 51 左边图标+左边标题+右边详情   + 右边箭头
         */
        switch (column.getStyle()) {
            case 0: //栏目名称
                holder.tvColumnTitle.setText(column.getLeftTitle());
                break;
            case 1: //左边标题 (默认)
            case 11:
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.tvTitle.setText(column.getLeftTitle());
                break;
            case 2: //左边标题+左边详情
            case 21:
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.tvLeftDes.setVisibility(View.VISIBLE);

                holder.tvTitle.setText(column.getLeftTitle());
                holder.tvLeftDes.setText(column.getLeftDes());
                break;
            case 3: //左边标题+右边详情
            case 31:
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.tvRightDes.setVisibility(View.VISIBLE);

                holder.tvTitle.setText(column.getLeftTitle());
                holder.tvRightDes.setText(column.getRightDes());
                break;
            case 4: //左边图标+左边标题
            case 41:
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.ivLeftIc.setVisibility(View.VISIBLE);

                holder.tvTitle.setText(column.getLeftTitle());
                holder.ivLeftIc.setImageResource(column.getLeftIconId());
                break;
            case 5: //左边图标+左边标题+右边详情
            case 51:
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.ivLeftIc.setVisibility(View.VISIBLE);
                holder.tvRightDes.setVisibility(View.VISIBLE);

                holder.tvTitle.setText(column.getLeftTitle());
                holder.ivLeftIc.setImageResource(column.getLeftIconId());
                holder.tvRightDes.setText(column.getRightDes());
                break;
            case 6: //左边详情
            case 61:
                holder.tvLeftDes.setVisibility(View.VISIBLE);

                holder.tvLeftDes.setText(column.getLeftDes());
                break;
            case 7: //左边详情+右边详情
            case 71:
                holder.tvLeftDes.setVisibility(View.VISIBLE);
                holder.tvRightDes.setVisibility(View.VISIBLE);

                holder.tvLeftDes.setText(column.getLeftDes());
                holder.tvRightDes.setText(column.getRightDes());
                break;
            case 8: //左边标题+右边图标
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.ivRightDes.setVisibility(View.VISIBLE);
                holder.ivLeftIc.setVisibility(View.VISIBLE);

                holder.ivLeftIc.setImageResource(column.getLeftIconId());
                holder.tvTitle.setText(column.getLeftTitle());
//                GlideUtils.loadDefaultCircleImage(context, column.getRightDes(), holder.ivRightDes);
                break;
            case 9: //左边标题+右边详情(带颜色的文字)
                holder.tvTitle.setVisibility(View.VISIBLE);
                holder.tvRightDes.setVisibility(View.VISIBLE);

                holder.tvTitle.setText(column.getLeftTitle());
                holder.tvRightDes.setText(column.getRightDes());
                holder.tvRightDes.setTextColor(column.getRightColorId());
                break;
        }

        if (column.getStyle() > 10) {
            holder.ivRightArrow.setVisibility(View.VISIBLE);
        } else {
            holder.ivRightArrow.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        return count;
    }

    //自定义Holder类,并通过findViewById绑定控件
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvColumnTitle;
        RelativeLayout rlColumnlayout;
        ImageView ivTopline;
        ImageView ivLeftIc;
        TextView tvTitle;
        TextView tvLeftDes;
        ImageView ivRightArrow;
        TextView tvRightDes;
        ImageView ivRightDes;
        LinearLayout view;

        public ViewHolder(View view) {
            super(view);
            this.tvColumnTitle = view.findViewById(R.id.tv_columntitle);
            this.rlColumnlayout = view.findViewById(R.id.rl_columnlayout);
            this.ivTopline = view.findViewById(R.id.iv_topline);
            this.ivLeftIc = view.findViewById(R.id.iv_leftic);
            this.tvTitle = view.findViewById(R.id.tv_title);
            this.tvLeftDes = view.findViewById(R.id.tv_leftdes);
            this.ivRightArrow = view.findViewById(R.id.iv_rightarrow);
            this.tvRightDes = view.findViewById(R.id.tv_rightdes);
            this.ivRightDes = view.findViewById(R.id.iv_rightdes);
            this.view = view.findViewById(R.id.view);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
