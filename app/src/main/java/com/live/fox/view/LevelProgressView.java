package com.live.fox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.live.fox.utils.device.ScreenUtils;

public class LevelProgressView extends View {

    private int strokeColor=0xffD144ED;
    private int backgroundColor=0xff43005D;
    private int firstGradientColor=0xffFD49FF;
    private int secondGradientColor=0xff7B00E3;
    float progress=0f;
    int strokeWidth=2;
    int radius=9;
    GradientDrawable backgroundDrawable=new GradientDrawable();
    GradientDrawable foregroundDrawable=new GradientDrawable();

    public LevelProgressView(Context context) {
        super(context);
        initView();
    }

    public LevelProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LevelProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        strokeWidth= ScreenUtils.getDip2px(getContext(),1);
        radius=ScreenUtils.getDip2px(getContext(),5);

    }

    public void setProgress(float percent)
    {
        progress=percent;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        backgroundDrawable.setBounds(0,0,getWidth(),getHeight());
        backgroundDrawable.setStroke(strokeWidth,strokeColor);
        backgroundDrawable.setColor(backgroundColor);
        backgroundDrawable.setCornerRadius(radius);
        backgroundDrawable.draw(canvas);

        if(progress>0)
        {
            foregroundDrawable.setBounds(strokeWidth,strokeWidth,(int)(getWidth()*progress)-strokeWidth,getHeight()-strokeWidth);
            foregroundDrawable.setColors(new int[]{firstGradientColor,secondGradientColor});
            foregroundDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            foregroundDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            foregroundDrawable.setCornerRadii(new float[]{radius,radius,0f,0f,0f,0f,radius,radius});
            foregroundDrawable.draw(canvas);
        }
        super.onDraw(canvas);

    }

}
