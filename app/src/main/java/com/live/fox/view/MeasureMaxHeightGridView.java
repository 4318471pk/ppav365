package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class MeasureMaxHeightGridView extends GridView {

    public MeasureMaxHeightGridView(Context context) {
        super(context);
    }

    public MeasureMaxHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasureMaxHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(1 << 16, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
