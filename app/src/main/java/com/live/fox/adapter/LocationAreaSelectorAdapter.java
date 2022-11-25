package com.live.fox.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.entity.LocationAreaSelectorBean;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.GradientTextView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LocationAreaSelectorAdapter extends RecyclerView.Adapter<LocationAreaSelectorAdapter.AreaHold> {

    private List<LocationAreaSelectorBean> beans;
    Activity context;
    LayoutInflater layoutInflater;
    int dip2;
    int itemWidth = 0;
    int selectIndex = 0;
    int gradientColors[];
    OnLocationSelectedListener onLocationSelectedListener;

    public LocationAreaSelectorAdapter(Activity context, List<LocationAreaSelectorBean> beans) {
        this.beans = beans;
        this.context = context;
        layoutInflater = context.getLayoutInflater();
        dip2 = ScreenUtils.getDip2px(context, 2);
        itemWidth = (ScreenUtils.getScreenWidth(context) - dip2 * 50) / 3;
        gradientColors = context.getResources().getIntArray(R.array.identificationColor);
        setHasStableIds(true);
    }

    public void setOnLocationSelectedListener(OnLocationSelectedListener onLocationSelectedListener) {
        this.onLocationSelectedListener = onLocationSelectedListener;
    }

    public void setNewData(List<LocationAreaSelectorBean> beans)
    {
        this.beans = beans;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @NotNull
    @Override
    public AreaHold onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        if (viewType == 0) {
            return new AreaHold(layoutInflater.inflate(R.layout.header_dialog_nearby, parent, false), viewType);
        } else {
            RelativeLayout relativeLayout = new RelativeLayout(context);
            relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            GradientTextView textView = new GradientTextView(context);
            relativeLayout.addView(textView);
            textView.setSolidBackground(0xffF4F1F8, (int) (dip2 * 7.5));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(0xff404040);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(itemWidth
                    , dip2 * 15);
            rl.bottomMargin = (int) (dip2 * 2.5);
            rl.topMargin = (int) (dip2 * 2.5);
            textView.setLayoutParams(rl);
            return new AreaHold(relativeLayout, viewType);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AreaHold holder, int position) {

        if (getItemViewType(position) == 1) {
            RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) holder.textView.getLayoutParams();
            rl.leftMargin = position % 3 == 1 ? dip2 * 15 : dip2 * 10;
            rl.rightMargin = position % 3 == 0 ? dip2 * 15 : dip2 * 10;
            rl.bottomMargin = (int) (dip2 * 2.5);
            rl.topMargin = (int) (dip2 * 2.5);
            holder.textView.setLayoutParams(rl);

            holder.textView.setTag(position);
            holder.textView.setOnClickListener(new OnClickFrequentlyListener(0) {
                @Override
                public void onClickView(View view) {
                    int index=(int)view.getTag();
                    beans.get(selectIndex).setSelected(false);
                    beans.get(index).setSelected(true);
                    selectIndex = index;
                    notifyDataSetChanged();
                    if(onLocationSelectedListener!=null && getItemViewType(selectIndex)>0)
                    {
                        onLocationSelectedListener.onSelected(beans.get(selectIndex));
                    }
                }
            });

            holder.textView.setText(beans.get(position).getAreaName());

            if (beans.get(position).isSelected()) {
                holder.textView.setTextColor(0xffffffff);
                holder.textView.setGradientBackground(gradientColors, (int) (dip2 * 7.5));
            } else {
                holder.textView.setTextColor(0xff404040);
                holder.textView.setSolidBackground(0xffF4F1F8, (int) (dip2 * 7.5));
            }

        } else {

        }

    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position > 0 ? 1 : 0;
    }

    protected final static class AreaHold extends RecyclerView.ViewHolder {
        GradientTextView textView;
        RelativeLayout relativeLayout;
        int viewType;

        public AreaHold(@NonNull @NotNull View itemView, int viewType) {
            super(itemView);
            if (viewType == 1) {
                relativeLayout = (RelativeLayout) itemView;
                textView = (GradientTextView) relativeLayout.getChildAt(0);
            }

        }
    }

    public interface OnLocationSelectedListener
    {
        void onSelected(LocationAreaSelectorBean bean);
    }
}
