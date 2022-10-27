package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lc.base.danmu.CircleDrawable;
import com.live.fox.R;
import com.live.fox.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class IndicatorListView extends RelativeLayout {

    LinearLayout linearLayout;
    int index=0;

    public IndicatorListView(Context context) {
        this(context,null);
    }

    public IndicatorListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndicatorListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        linearLayout=new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rl.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        linearLayout.setLayoutParams(rl);

        addView(linearLayout);
    }

    public void setIndicatorAmount(int amount)
    {
        linearLayout.removeAllViews();
        index=0;
        int dip5= ScreenUtils.dp2px(getContext(),5);
        for (int i = 0; i < amount; i++) {
            View circle=new View(getContext());
            LinearLayout.LayoutParams ll= new LinearLayout.LayoutParams(dip5,dip5);
            ll.leftMargin=i>0?dip5:0;
            circle.setLayoutParams(ll);
            circle.setBackground(getResources().getDrawable(R.drawable.circle_a4a4a4));

            linearLayout.addView(circle);
        }
        if(amount>0)
        {
            linearLayout.getChildAt(index).setBackground(getResources().getDrawable(R.drawable.circle_white));
        }
    }

    public void setSelectedIndex(int index)
    {
        if(linearLayout.getChildCount()>this.index)
        {
            linearLayout.getChildAt(this.index).setBackground(getResources().getDrawable(R.drawable.circle_a4a4a4));
        }

        if(linearLayout.getChildCount()>index)
        {
            linearLayout.getChildAt(index).setBackground(getResources().getDrawable(R.drawable.circle_white));
        }
        this.index=index;
    }

}
