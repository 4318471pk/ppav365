package com.tencent.demo.avatar.view.groupitemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.tencent.demo.avatar.model.AvatarItem;
import com.tencent.demo.avatar.model.MainTab;
import com.tencent.demo.avatar.model.SubTab;
import com.tencent.demo.avatar.view.AvatarPanel;
import com.tencent.demo.avatar.widget.AvatarSeekBarLayout;
import com.tencent.xmagic.avatar.AvatarData;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 滑竿页签
 */
public class SliderGroupItemView extends GroupItemView {

    private static final int DISPLAY_MIN_VALUE = -100;
    private static final int DISPLAY_MAX_VALUE = 100;
    private static final int DISPLAY_PROGRESS_VALUE = 100;
    private static final int ITEM_PADDING = 20;
    private LinearLayout contentLayout;

    public SliderGroupItemView(Context context) {
        super(context);
    }

    public SliderGroupItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SliderGroupItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initView(MainTab mainTab, SubTab subTab) {
        contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        addItemView(subTab.items);
        addView(contentLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 用于创建seekBar的item
     */
    private void addItemView(List<AvatarItem> avatarItems) {
        if (avatarItems == null || avatarItems.size() == 0) {
            return;
        }
        this.removeAllViews();
        int padding = AvatarPanel.dip2px(mContext, ITEM_PADDING);
        for (AvatarItem avatarItem : avatarItems) {
            if (avatarItem == null || avatarItem.avatarData == null) {
                continue;
            }
            AvatarData avatarData = avatarItem.avatarData;
            Map<String, String> labels = avatarItem.labels;
            if (labels == null) {
                continue;
            }
            Set<String> keys = labels.keySet();
            for (String key : keys) {
                if (avatarData.value.get(key) == null) {
                    continue;
                }
                float currentValue = avatarData.value.get(key).getAsFloat();
                AvatarSeekBarLayout avatarSeekBarLayout = new AvatarSeekBarLayout(mContext);
                avatarSeekBarLayout.setProgress(DISPLAY_MIN_VALUE, DISPLAY_MAX_VALUE, (int) (DISPLAY_PROGRESS_VALUE * currentValue));
                avatarSeekBarLayout.setName(labels.get(key));
                avatarSeekBarLayout.setOnSeekBarChangeListener((seekBar, progress, fromUser) -> {
                    double value = ((double) progress) / DISPLAY_PROGRESS_VALUE;
                    avatarData.value.addProperty(key, value);
                    if (groupItemViewCallBack != null) {
                        groupItemViewCallBack.onItemValueChange(avatarItem);
                    }
                });
                avatarSeekBarLayout.setPadding(0, padding, 0, padding);
                contentLayout.addView(avatarSeekBarLayout);
            }
        }
    }


}
