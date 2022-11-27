package com.live.fox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.DrawableUtils;

import com.live.fox.R;
import com.live.fox.utils.device.ScreenUtils;
import com.makeramen.roundedimageview.RoundedImageView;

public class AnchorRoundImageView extends androidx.appcompat.widget.AppCompatImageView {

    GradientDrawable gradientDrawable=new GradientDrawable();
    int mColors[]=new int[]{0x00000000,0x77000000};
    float radius=0f;
    Bitmap placeHoldBitmap;

    public AnchorRoundImageView(Context context,float radius) {
        this(context,null);
        this.radius=radius;
    }

    public AnchorRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AnchorRoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attributeSet)
    {
        if(attributeSet!=null)
        {
            final TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.AnchorRoundImageView);
            try {
                radius = typedArray.getDimension(R.styleable.AnchorRoundImageView_viewRadius, 0);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                typedArray.recycle();
            }
        }
        else
        {
            if(radius==0)
            {
                radius= ScreenUtils.getDip2px(context,10);
            }
        }
        placeHoldBitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.user_head_error);
    }


    public void setRadius(float radius) {
        this.radius = radius;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(getWidth()>0 && getHeight()>0 && radius>0)
        {
            if(getDrawable()==null)
            {
                RoundedDrawable roundedDrawable=new RoundedDrawable(placeHoldBitmap,getWidth(),getHeight());
                roundedDrawable.setBounds(0,0,getWidth(),getHeight());
                roundedDrawable.setRound((int)radius);
                roundedDrawable.draw(canvas);
            }

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
