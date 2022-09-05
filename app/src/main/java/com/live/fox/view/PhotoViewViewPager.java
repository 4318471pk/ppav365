package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

//解决ViewPager与PhotoView的冲突 ViewPager中使用了PhotoView时使用
public class PhotoViewViewPager extends ViewPager {

    private static final String TAG = PhotoViewViewPager.class.getName();

    public PhotoViewViewPager(Context context) {
        super(context);
    }

    public PhotoViewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}