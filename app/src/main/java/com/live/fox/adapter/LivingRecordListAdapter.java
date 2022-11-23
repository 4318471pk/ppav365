package com.live.fox.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.LivingRecordListBean;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LivingRecordListAdapter  extends RecyclerView.Adapter<LivingRecordListAdapter.LivingRecordListViewHold> {

    Context context;
    LayoutInflater layoutInflater;
    List<LivingRecordListBean> data;
    int screenWidth;
    int dip20;

    public LivingRecordListAdapter(Context context,List<LivingRecordListBean> data) {
        this.context=context;
        this.data=data;
        screenWidth= ScreenUtils.getScreenWidth(context);
        dip20=ScreenUtils.dp2px(context,20);
        layoutInflater=LayoutInflater.from(context);
    }

    public void setNewData(List<LivingRecordListBean> data)
    {
        this.data=data;
        notifyDataSetChanged();
    }

    public void clear()
    {
        this.data.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @NotNull
    @Override
    public LivingRecordListViewHold onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_living_record_list,parent,false);
        return new LivingRecordListViewHold(view,screenWidth-(dip20*2));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LivingRecordListViewHold holder, int position) {
        if(position==0)
        {
            holder.tvName.setText(context.getString(R.string.nameOfAnchor));
            holder.tvTime.setText(context.getString(R.string.perShow));
            SpanUtils spanUtils = new SpanUtils();
            spanUtils.append(context.getString(R.string.TheoreticalEarnings2)).setFontSize(14,true);
            spanUtils.append("("+context.getString(R.string.yuan)+")").setFontSize(11,true);
            holder.tvAdvantage.setText(spanUtils.create());
            holder.tvStatus.setText(context.getString(R.string.salaryStatus));
            holder.itemView.setBackgroundColor(0xffffffff);
            holder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            holder.tvTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            holder.tvAdvantage.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            holder.tvStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        }
        else
        {
            if(position-1>-1 && position-1<data.size())
            {
                LivingRecordListBean bean=data.get(position-1);
                holder.itemView.setBackgroundColor(position%2==0?0xffffffff:0xffFAFAFA);
                holder.tvName.setText(bean.getNickname());
                holder.tvTime.setText(TimeUtils.long2StringLivingRecord(bean.getStartTime(),bean.getEndTime()));
                holder.tvAdvantage.setText(bean.getProfit());
                holder.tvName.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                holder.tvTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                holder.tvAdvantage.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                holder.tvStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                switch (bean.getStatus())
                {
                    case 0:
                        holder.tvStatus.setText(R.string.finishPayment);
                        holder.tvStatus.setTextColor(0xff1FC478);
                        break;
                    case 1:
                        holder.tvStatus.setText(R.string.notYetFinishPayment);
                        holder.tvStatus.setTextColor(0xffFF008A);
                        break;
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    public static class LivingRecordListViewHold extends RecyclerView.ViewHolder
    {
        TextView tvName,tvTime,tvAdvantage,tvStatus;
        float wight[]={0.3f,0.25f,0.255f,0.1f};
        public LivingRecordListViewHold(@NonNull @NotNull View itemView,int contentWidth) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvName);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvAdvantage=itemView.findViewById(R.id.tvAdvantage);
            tvStatus=itemView.findViewById(R.id.tvStatus);

            LinearLayout linearLayout=(LinearLayout)itemView;
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                LinearLayout.LayoutParams ll=(LinearLayout.LayoutParams)linearLayout.getChildAt(i).getLayoutParams();
                ll.width=(int)(contentWidth*wight[i]);
                linearLayout.getChildAt(i).setLayoutParams(ll);
            }
        }
    }
}
