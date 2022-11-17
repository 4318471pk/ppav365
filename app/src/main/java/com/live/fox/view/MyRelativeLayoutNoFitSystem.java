package com.live.fox.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowInsets;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.viewpager2.widget.ViewPager2;

import com.live.fox.utils.MessageViewWatch;

public class MyRelativeLayoutNoFitSystem extends RelativeLayout {

    private ViewPager2 viewPager;
    private MessageViewWatch messageViewWatch;

    public MyRelativeLayoutNoFitSystem(Context context) {
        super(context);
    }

    public MyRelativeLayoutNoFitSystem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayoutNoFitSystem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setFitsSystemWindows(boolean fitSystemWindows) {
         super.setFitsSystemWindows(false);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }
        return super.fitSystemWindows(insets);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()));
        }
        else {
            return insets;
        }
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, 0, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, 0, end, bottom);
    }

    public void setViewPager(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("onTouchEvent22",event.getAction()+" ");
        if(viewPager!=null && messageViewWatch!=null )
        {
            if(!messageViewWatch.isKeyboardShow() && !messageViewWatch.isMessagesPanelOpen())
            {
                viewPager.setUserInputEnabled(true);
            }
            else
            {
                viewPager.setUserInputEnabled(false);
            }

        }
        return super.onTouchEvent(event);
    }

    public void setMessageViewWatch(MessageViewWatch messageViewWatch) {
        this.messageViewWatch = messageViewWatch;
    }
}
