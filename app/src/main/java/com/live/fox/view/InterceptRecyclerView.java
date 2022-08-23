package com.live.fox.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * 处理拦截事
 */
public class InterceptRecyclerView extends RecyclerView {
    public InterceptRecyclerView(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }
}
