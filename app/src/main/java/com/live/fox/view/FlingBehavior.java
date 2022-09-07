package com.live.fox.view;

import android.content.Context;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.live.fox.R;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import java.lang.reflect.Field;

import static android.view.KeyEvent.ACTION_DOWN;

public class FlingBehavior extends AppBarLayout.Behavior {
    private static final int TOP_CHILD_FLING_THRESHOLD = 3;
    private boolean isPositive;

    public FlingBehavior() {
        super();
    }

    public FlingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    //fling上滑appbar然后迅速fling下滑recycler时, HeaderBehavior的mScroller并未停止, 会导致上下来回晃动
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        Log.e("onOffsetChanged888",dx+" "+dy+new Gson().toJson(consumed));
        if (type == ViewCompat.TYPE_NON_TOUCH && getTopAndBottomOffset() == 0) { //recyclerview的惯性比较大 ,会顶在头部一会儿, 到头直接干掉它的滑动
            ViewCompat.stopNestedScroll(target, type);
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        Log.e("onOffsetChanged555",dxConsumed+" "+dyConsumed+" "+dxUnconsumed+" "+dyUnconsumed+" "+new Gson().toJson(consumed));
//        View view=child.findViewById(R.id.btnTest);
//        if(dyUnconsumed>=0)
//        {
//            view.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            view.setVisibility(View.GONE);
//        }
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        if (ev.getAction() == ACTION_DOWN) {
            Object scroller = getSuperSuperField(this, "mScroller");
            if (scroller != null && scroller instanceof OverScroller) {
                OverScroller overScroller = (OverScroller) scroller;
                overScroller.abortAnimation();
            }
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    private Object getSuperSuperField(Object paramClass, String paramString) {
        Field field = null;
        Object object = null;
        try {
            field = paramClass.getClass().getSuperclass().getSuperclass().getDeclaredField(paramString);
            field.setAccessible(true);
            object = field.get(paramClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
