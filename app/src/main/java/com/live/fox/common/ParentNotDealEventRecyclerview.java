package com.live.fox.common;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 解决直播聊天界面的滑动冲突
 * 评论/推荐列表，告诉直播间不要处理列表的触摸事件
 */
public class ParentNotDealEventRecyclerview extends RecyclerView {

    public ParentNotDealEventRecyclerview(Context context) {
        super(context);
    }

    public ParentNotDealEventRecyclerview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentNotDealEventRecyclerview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
