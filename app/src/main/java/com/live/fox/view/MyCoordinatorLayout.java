package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import org.jetbrains.annotations.NotNull;

public class MyCoordinatorLayout extends CoordinatorLayout {

    ScrollListener scrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public MyCoordinatorLayout(@NonNull @NotNull Context context) {
        super(context);
        init();
    }

    public MyCoordinatorLayout(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCoordinatorLayout(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(scrollListener!=null)
        {
            scrollListener.onScroll(l,t,oldl,oldt);
        }

    }

    public interface ScrollListener
    {
        public void onScroll(int x,int y,int x2,int y2);
    }
}
