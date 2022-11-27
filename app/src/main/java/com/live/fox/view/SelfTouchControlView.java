package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class SelfTouchControlView extends RelativeLayout {
    public SelfTouchControlView(Context context) {
        super(context);
    }

    public SelfTouchControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelfTouchControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        getParent().requestDisallowInterceptTouchEvent(true);
//        switch (e.getAction())
//        {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                return true;
//        }
//        return true;
//    }
}
