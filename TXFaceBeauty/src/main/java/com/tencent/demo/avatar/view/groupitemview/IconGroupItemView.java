package com.tencent.demo.avatar.view.groupitemview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.tencent.demo.R;
import com.tencent.demo.avatar.model.AvatarItem;
import com.tencent.demo.avatar.model.MainTab;
import com.tencent.demo.avatar.model.SubTab;

import java.util.List;


/**
 * icon 类型的页签
 */
public class IconGroupItemView extends GroupItemView {


    private static final String COLOR_ICON_TYPE = "#";
    private static final String URL_ICON_TYPE = "http";
    private static final String LOCAL_FILE_ICON_TYPE = "icon.png";
    private static final int COLOR_ICON_COLUMN_NUMBER = 4;
    private static final int URL_ICON_COLUMN_NUMBER = 3;

    private RecyclerView recyclerView;
    private String iconType = COLOR_ICON_TYPE;

    public IconGroupItemView(Context context) {
        super(context);
    }

    public IconGroupItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IconGroupItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView(MainTab mainTab, SubTab subTab) {
        List<AvatarItem> avatarDataList = subTab.items;
        if (avatarDataList != null && avatarDataList.size() > 0) {
            if (!TextUtils.isEmpty(avatarDataList.get(0).icon)) {
                if (avatarDataList.get(0).icon.startsWith(COLOR_ICON_TYPE)) {
                    iconType = COLOR_ICON_TYPE;
                } else if (avatarDataList.get(0).icon.toLowerCase().startsWith(URL_ICON_TYPE)) {
                    iconType = URL_ICON_TYPE;
                } else if (avatarDataList.get(0).icon.toLowerCase().endsWith(LOCAL_FILE_ICON_TYPE)) {
                    iconType = LOCAL_FILE_ICON_TYPE;
                }
            }
            recyclerView = new RecyclerView(mContext);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, COLOR_ICON_TYPE.equals(iconType) ? COLOR_ICON_COLUMN_NUMBER : URL_ICON_COLUMN_NUMBER) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            RecycleViewAdapter adapter = new RecycleViewAdapter(mainTab, subTab, iconType, groupItem -> {
                if (groupItemViewCallBack != null) {
                    groupItemViewCallBack.onItemChecked(groupItem);
                }
            });
            recyclerView.setAdapter(adapter);
            this.addView(recyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    static class RecycleViewAdapter extends RecyclerView.Adapter<IconViewHolder> {

        private LayoutInflater layoutInflater;
        private IconListClickListener iconListClickListener;
        private SubTab subTab;
        private String iconType;
        private List<AvatarItem> avatarDataList;
        private MainTab mainTab = null;
        private AvatarItem lastSelected = null;

        public RecycleViewAdapter(MainTab mainTab, SubTab subTab, String iconType, IconListClickListener iconListClickListener) {
            this.iconType = iconType;
            this.mainTab = mainTab;
            this.iconListClickListener = iconListClickListener;
            this.subTab = subTab;
            this.avatarDataList = subTab.items;

        }

        @NonNull
        @Override
        public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (layoutInflater == null) {
                layoutInflater = LayoutInflater.from(parent.getContext());
            }
            View view = layoutInflater.inflate(COLOR_ICON_TYPE.equals(iconType) ? R.layout.avatar_color_icon_item_layout
                    : R.layout.avatar_url_item_layout, parent, false);
            return new IconViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
            AvatarItem currentItem = avatarDataList.get(position);
            if (!TextUtils.isEmpty(currentItem.icon)) {
                if (currentItem.icon.startsWith(COLOR_ICON_TYPE)) {
                    holder.imageView.setBackgroundColor(Color.parseColor(currentItem.icon));
                } else if (currentItem.icon.toLowerCase().startsWith(URL_ICON_TYPE)) {
                    Glide.with(holder.itemView.getContext()).load(currentItem.icon).into(holder.imageView);
                } else if (currentItem.icon.endsWith(LOCAL_FILE_ICON_TYPE)) {
                    Glide.with(holder.itemView.getContext()).load(currentItem.icon).into(holder.imageView);
                }
            }
            //表示选中的
            if (currentItem.selected) {
                holder.groupItemView.setBackgroundResource(R.drawable.avatar_icon_selected);
                lastSelected = currentItem;
            } else {
                holder.groupItemView.setBackgroundResource(0);
            }
            holder.itemView.setOnClickListener(v -> {
                if (currentItem == lastSelected) {  //表示两次点击了同一个icon
                    return;
                }
                AvatarItem relatedAvatarData = null;
                if (!TextUtils.isEmpty(subTab.relatedCategory)) { //如果relatedCategory字段不为空，则要遍历;
                    relatedAvatarData = getRelatedItem(mainTab, subTab.relatedCategory);
                }
                if (lastSelected != null) {
                    lastSelected.selected = false;
                }
                lastSelected = currentItem;
                lastSelected.selected = true;

                if (iconListClickListener != null) {
                    iconListClickListener.onItemClick(currentItem);
                    if (relatedAvatarData != null) {
                        iconListClickListener.onItemClick(relatedAvatarData);
                    }
                }
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return avatarDataList == null ? 0 : avatarDataList.size();
        }


        private AvatarItem getRelatedItem(MainTab mainTab, String relatedCategory) {
            if (mainTab == null) {
                return null;
            }
            List<SubTab> subTabs = mainTab.subTabs;
            if (subTabs == null || subTabs.size() == 0) {
                return null;
            }
            SubTab relatedSubTab = null;
            for (SubTab tab : subTabs) {
                if (tab.category.equals(relatedCategory)) {  //找到对应的list,然后找到对应的选中项即可
                    relatedSubTab = tab;
                    break;
                }
            }
            if (relatedSubTab == null || relatedSubTab.items == null || relatedSubTab.items.size() == 0) {
                return null;
            }
            for (AvatarItem avatarItem : relatedSubTab.items) {
                if (avatarItem.selected) {
                    return avatarItem;
                }
            }
            return null;
        }


    }


    static class IconViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        View groupItemView;

        public IconViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon_image);
            groupItemView = itemView.findViewById(R.id.group_item_view);
        }
    }


    public interface IconListClickListener {
        void onItemClick(AvatarItem avatarItem);
    }

}
