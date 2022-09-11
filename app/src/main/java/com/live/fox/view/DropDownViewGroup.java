package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class DropDownViewGroup extends LinearLayout {

    Scroller scroller;
    int scY=0;
    public DropDownViewGroup(Context context) {
        super(context);
        init();
    }

    public DropDownViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DropDownViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init()
    {
        scroller=new Scroller(getContext());
        scroller.forceFinished(false);
        scroller.extendDuration(500);
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if(scroller.computeScrollOffset() )
        {

            Log.e("computeScroll",scroller.getFinalY()+" "+scroller.getCurrY());
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
            scY=scroller.getCurrY();
        }
    }

    public void scroll(int y)
    {
        scroller.startScroll(getScrollX(),getScrollY(),getScrollX(),y);
        invalidate();
    }
}
