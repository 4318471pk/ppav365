package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class DropDownScrollView extends ScrollView {

    DropDownViewGroup dropDownViewGroup;
    int limitDistance,itemViewHeight=-1;

    public DropDownScrollView(Context context) {
        super(context);
    }

    public DropDownScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DropDownScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init()
    {

    }

    public void setDropDown(DropDownViewGroup dropDownView,int limitDistance)
    {
        this.dropDownViewGroup=dropDownView;
        this.limitDistance=limitDistance;
    }

    @Override
    protected void onScrollChanged(int l, int y, int oldl, int y2) {
        super.onScrollChanged(l, y, oldl, y2);

        if(itemViewHeight<1)
        {
            itemViewHeight=dropDownViewGroup.getHeight();
        }
        if(itemViewHeight<1)
        {
            return;
        }

        float ratio =1.0f* y/limitDistance;
        RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) dropDownViewGroup.getLayoutParams();
        int offsetY=-dropDownViewGroup.getHeight()+((int)(dropDownViewGroup.getHeight()*ratio));
        if(offsetY<=0)
        {
            rl.topMargin=offsetY;
            dropDownViewGroup.setLayoutParams(rl);
        }
    }

}
