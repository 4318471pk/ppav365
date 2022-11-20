package com.live.fox.view;

import android.content.Context;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

public class MyViewPager extends ViewPager {

    private boolean scrollEnable=true;

    public MyViewPager(@NonNull @NotNull Context context) {
        super(context);
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return scrollEnable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(scrollEnable)
        {
            return super.onTouchEvent(ev);
        }
        else
        {
            return false;
        }
    }

//    @Override
//    public void scrollTo(int x, int y) {
//        if(scrollEnable)
//        {
//            super.scrollTo(x, y);
//        }
//    }
}
