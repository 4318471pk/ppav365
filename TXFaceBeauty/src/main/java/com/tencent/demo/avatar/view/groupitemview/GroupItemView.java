package com.tencent.demo.avatar.view.groupitemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tencent.demo.avatar.model.AvatarItem;
import com.tencent.demo.avatar.model.MainTab;
import com.tencent.demo.avatar.model.SubTab;

import java.util.List;

/**
 * 面板中最小单元的 view group，
 * 例如：头发-》发色-》发梢view
 */
public abstract class GroupItemView extends RelativeLayout {

    //用于通知使用方的回调
    protected GroupItemViewCallBack groupItemViewCallBack = null;
    protected Context mContext;

    public GroupItemView(Context context) {
        this(context, null);
    }

    public GroupItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        this.initViewSettings();
    }

    private void initViewSettings() {
    }

    /**
     * 根据数据构造页面
     */
    public abstract void initView(MainTab mainTab, SubTab subTab);


    public void setGroupItemViewCallBack(GroupItemViewCallBack groupItemViewCallBack) {
        this.groupItemViewCallBack = groupItemViewCallBack;
    }

    public interface GroupItemViewCallBack {
        void onItemChecked(AvatarItem avatarItem);

        void onItemValueChange(AvatarItem avatarItem);
    }

}
