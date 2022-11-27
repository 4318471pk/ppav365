package com.live.fox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class Top20CircleImage extends androidx.appcompat.widget.AppCompatImageView {

    Bitmap placeHoldBitmap;

    public Top20CircleImage(@NonNull @NotNull Context context) {
        super(context);
    }

    public Top20CircleImage(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Top20CircleImage(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(getWidth()>0 && getHeight()>0)
        {
            if(getDrawable()==null)
            {
                if(placeHoldBitmap==null)
                {
                    placeHoldBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.user_head_error);
                }
                CircleDrawable circleDrawable=new CircleDrawable(placeHoldBitmap,getWidth(),getHeight());
                circleDrawable.setBounds(0,0,getWidth(),getHeight());
                circleDrawable.draw(canvas);
            }

        }
    }
}
