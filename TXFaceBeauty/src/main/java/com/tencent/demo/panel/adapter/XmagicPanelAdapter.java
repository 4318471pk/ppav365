package com.tencent.demo.panel.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tencent.demo.R;
import com.tencent.demo.module.XmagicUIProperty;
import com.tencent.demo.panel.XmagicPanelDataManager;
import com.tencent.demo.utils.ScreenUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class XmagicPanelAdapter extends RecyclerView.Adapter<XmagicPanelAdapter.PropertyHolder> implements View.OnClickListener {
    private XmagicPanelItemClickListener mListener;
    private List<XmagicUIProperty<?>> properties;

    public XmagicPanelAdapter(XmagicPanelItemClickListener xmagicPanelItemClickListener) {
        mListener = xmagicPanelItemClickListener;
    }

    @NonNull
    @Override
    public PropertyHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_property_view, viewGroup, false);
        return new PropertyHolder(view);
    }

    @Override
    public int getItemCount() {
        if (properties == null) {
            return 0;
        }
        return properties.size();
    }

    @Override
    public void onBindViewHolder(PropertyHolder holder, int position) {
        XmagicUIProperty<?> xmagicProperty = properties.get(position);
        if (xmagicProperty.uiCategory == XmagicUIProperty.UICategory.KV) {
            holder.itemView.setOnClickListener(null);
            holder.tvItemName.setText(XmagicPanelDataManager.getInstance().isPanelBeautyOpen() ? holder.itemView.getResources().getString(R.string.pannel_beauty_switch_close_txt) : holder.itemView.getResources().getString(R.string.pannel_beauty_switch_open_txt));
            holder.tvItemName.setTextColor(Color.parseColor("#ffffff"));
            holder.foregroundImg.setVisibility(View.GONE);
            holder.ivItemIcon.setVisibility(View.INVISIBLE);
            holder.aSwitch.setVisibility(View.VISIBLE);
            holder.rightLine.setVisibility(View.VISIBLE);
            holder.aSwitch.setOnCheckedChangeListener(null);
            holder.aSwitch.setChecked(XmagicPanelDataManager.getInstance().isPanelBeautyOpen());
            holder.aSwitch.setEnabled(true);
            holder.aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                buttonView.setEnabled(false);
                XmagicPanelDataManager.getInstance().setPanelBeautyOpen(isChecked);
                if (mListener != null) {
                    mListener.onBeautySwitchCheckedChange(isChecked);
                }
                holder.tvItemName.setText(isChecked ? holder.itemView.getResources().getString(R.string.pannel_beauty_switch_close_txt) : holder.itemView.getResources().getString(R.string.pannel_beauty_switch_open_txt));
                holder.tvItemName.setTextColor(Color.parseColor("#ffffff"));
                notifyDataSetChanged();
            });
        } else {
            holder.aSwitch.setVisibility(View.GONE);
            holder.rightLine.setVisibility(View.GONE);
            //如果是美颜项，并且美颜开关关闭的情况
            holder.itemView.setTag(properties.get(position));
            holder.tvItemName.setText(xmagicProperty.displayName);
            holder.ivItemIcon.setVisibility(View.VISIBLE);
            if (xmagicProperty.thumbDrawable != 0) {
                Glide.with(holder.ivItemIcon).load(xmagicProperty.thumbDrawable).into(holder.ivItemIcon);
            } else if (!TextUtils.isEmpty(xmagicProperty.thumbImagePath)) {
                if(URLUtil.isNetworkUrl(xmagicProperty.thumbImagePath)){
                    Glide.with(holder.ivItemIcon).load(xmagicProperty.thumbImagePath).into(holder.ivItemIcon);
                }else {
                    Glide.with(holder.ivItemIcon).load(new File(xmagicProperty.thumbImagePath)).into(holder.ivItemIcon);
                }
            }
            if (xmagicProperty.uiCategory == XmagicUIProperty.UICategory.BEAUTY && !XmagicPanelDataManager.getInstance().isPanelBeautyOpen()) {
                holder.itemView.setOnClickListener(null);
                holder.foregroundImg.setVisibility(View.VISIBLE);
                holder.tvItemName.setTextColor(Color.parseColor("#80ffffff"));
                holder.tvItemName.setTypeface(null, Typeface.NORMAL);
                holder.ivItemIcon.setBackground(holder.itemView.getResources().getDrawable(R.drawable.ratio_bg_00ffffff));
            } else {
                holder.itemView.setOnClickListener(this);
                holder.foregroundImg.setVisibility(View.GONE);
                if (isSelected(xmagicProperty)) {
                    holder.tvItemName.setTextColor(Color.parseColor("#F14257"));
                    holder.tvItemName.setTypeface(null, Typeface.BOLD);
                    holder.ivItemIcon.setBackground(holder.itemView.getResources().getDrawable(R.drawable.ratio_bg_f14257));
                } else {
                    holder.tvItemName.setTextColor(Color.parseColor("#ffffff"));
                    holder.tvItemName.setTypeface(null, Typeface.NORMAL);
                    holder.ivItemIcon.setBackground(holder.itemView.getResources().getDrawable(R.drawable.ratio_bg_00ffffff));
                }
            }
        }
    }


    private long lastTime = 0L;

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            XmagicUIProperty<?> xmagicUIProperty = (XmagicUIProperty<?>) v.getTag();
            if (xmagicUIProperty != null && xmagicUIProperty.uiCategory == XmagicUIProperty.UICategory.SEGMENTATION
                    && xmagicUIProperty.property != null &&
                    ("video_segmentation_transparent_bg".equals(xmagicUIProperty.property.id) || "video_segmentation_blur_75".equals(xmagicUIProperty.property.id))) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTime < 3 * 1000) {
                    Toast.makeText(v.getContext(),"Don't click too often",Toast.LENGTH_LONG).show();
                    return;
                }
                lastTime = currentTime;
            }
            mListener.onItemClick(xmagicUIProperty);
        }
    }

    public void setProperties(List<XmagicUIProperty<?>> properties) {
        this.properties = properties;
        notifyDataSetChanged();
    }


    /**
     * 获取被选中的item的位置
     * Get the position of the checked item
     * @return position 如果返回值为-1，则表示没有选中任何一项
     * @return position if position is -1 ,Indicates that none of the items are selected
     */
    public int getCheckedPosition() {
        for (int i = 0; i < this.properties.size(); i++) {
            XmagicUIProperty<?> xmagicUIProperty = this.properties.get(i);
            if (xmagicUIProperty != null && isSelected(xmagicUIProperty)) {
                if (mListener != null) {
                    mListener.onChecked(xmagicUIProperty);
                    return i;
                }
            }
        }
        return -1;
    }

    public interface XmagicPanelItemClickListener {
        void onItemClick(XmagicUIProperty<?> xmagicUIProperty);

        void onChecked(XmagicUIProperty<?> xmagicUIProperty);

        void onBeautySwitchCheckedChange(boolean isChecked);
    }

    /**
     * 判断当前item是否是选中状态
     * Determine whether the current item is checked
     * @param xmagicUIProperty
     * @return
     */
    private boolean isSelected(XmagicUIProperty xmagicUIProperty) {
        return XmagicPanelDataManager.getInstance().getSelectedItems().containsValue(xmagicUIProperty);
    }

    static class PropertyHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        ImageView ivItemIcon;
        View itemView;
        ImageView foregroundImg;
        Switch aSwitch;
        View rightLine;

        public PropertyHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            ivItemIcon = itemView.findViewById(R.id.iv_item_icon);
            foregroundImg = itemView.findViewById(R.id.img_item_foreground_gray);
            aSwitch = itemView.findViewById(R.id.item_beauty_switch);
            rightLine = itemView.findViewById(R.id.item_right_line);
        }
    }




    private Drawable getImage(Context context,String thumbPath){
        int iconSize = ScreenUtils.dp2px(context, 50);
        Drawable drawable = new ColorDrawable(Color.argb(32, 0, 0, 0));
        Drawable thumbImg = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(thumbPath);
            thumbImg = Drawable.createFromStream(is,null);
        } catch (Exception e) {
            Log.e("XmagicPropertyAdapter", "getImage: e="+e.toString());
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (thumbImg != null) {
            drawable = thumbImg;
        }
        drawable.setBounds(0, 0, iconSize, iconSize);
        return drawable;
    }

}
