package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class DropDownScrollView extends ScrollView {

    DropDownViewGroup dropDownViewGroup;
    int limitDistance;

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

        if(dropDownViewGroup!=null && limitDistance>0)
        {
            float ratio =1.0f* y/limitDistance;
            int scrollOffset=dropDownViewGroup.getHeight()-dropDownViewGroup.getScrollY()-((int)(dropDownViewGroup.getHeight()*ratio));
            if(scrollOffset+dropDownViewGroup.getScrollY()>0)
            {
                dropDownViewGroup.scroll(scrollOffset);
            }
        }
    }
}
