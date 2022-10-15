package com.live.fox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;

public class AnchorRoundImageView extends RoundedImageView {

    GradientDrawable gradientDrawable=new GradientDrawable();
    int mColors[]=new int[]{0x00000000,0x66000000};

    public AnchorRoundImageView(Context context) {
        super(context);
    }

    public AnchorRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnchorRoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getWidth()>0 && getHeight()>0)
        {
            gradientDrawable.setBounds(0,getHeight()/2,getWidth(),getHeight());
            gradientDrawable.setColors(mColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            gradientDrawable.setCornerRadius(getCornerRadius());
            gradientDrawable.draw(canvas);
        }
    }
}
