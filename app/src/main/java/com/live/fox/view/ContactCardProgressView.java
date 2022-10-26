package com.live.fox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.live.fox.utils.device.ScreenUtils;

public class ContactCardProgressView extends View {

    private int backgroundColor=0xff591B86;
    private int firstGradientColor=0xffFF00F6;
    private int secondGradientColor=0xffFF1679;
    float progress=0f;
    int radius=9;
    GradientDrawable backgroundDrawable=new GradientDrawable();
    GradientDrawable foregroundDrawable=new GradientDrawable();

    public ContactCardProgressView(Context context) {
        super(context);
        initView();
    }

    public ContactCardProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ContactCardProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        radius=ScreenUtils.getDip2px(getContext(),4);

    }

    public void setProgress(float percent)
    {
        if(percent>=1.0f)
        {
            percent=1.0f;
            firstGradientColor=0xff00FF7E;
            secondGradientColor=0xff26B9DE;
        }
        progress=percent;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        backgroundDrawable.setBounds(0,0,getWidth(),getHeight());
        backgroundDrawable.setColor(backgroundColor);
        backgroundDrawable.setCornerRadius(radius);
        backgroundDrawable.draw(canvas);

        if(progress>0)
        {
            foregroundDrawable.setBounds(0,0,(int)(getWidth()*progress),getHeight());
            foregroundDrawable.setColors(new int[]{firstGradientColor,secondGradientColor});
            foregroundDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            foregroundDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            if(progress<1.0f)
            {
                foregroundDrawable.setCornerRadii(new float[]{radius,radius,0f,0f,0f,0f,radius,radius});
            }
            else
            {
                foregroundDrawable.setCornerRadius(radius);
            }
            foregroundDrawable.draw(canvas);
        }
        super.onDraw(canvas);

    }
}
