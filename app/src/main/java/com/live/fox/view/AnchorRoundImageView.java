package com.live.fox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.makeramen.roundedimageview.RoundedImageView;

public class AnchorRoundImageView extends AppCompatImageView {

    GradientDrawable gradientDrawable=new GradientDrawable();
    int mColors[]=new int[]{0x00000000,0x77000000};
    float radius=0f;

    public AnchorRoundImageView(Context context) {
        super(context);
    }

    public AnchorRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnchorRoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getWidth()>0 && getHeight()>0 && radius>0)
        {
            gradientDrawable.setBounds(0,getHeight()/2,getWidth(),getHeight());
            gradientDrawable.setColors(mColors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
//            gradientDrawable.setCornerRadii(radius);
            gradientDrawable.setCornerRadii(new float[]{0f,0f,0f,0f,radius,radius,radius,radius});
            gradientDrawable.draw(canvas);
        }
    }
}
