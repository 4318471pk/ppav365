package com.live.fox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;

import org.jetbrains.annotations.NotNull;

public class BannerImageView extends androidx.appcompat.widget.AppCompatImageView {

    Bitmap placeHoldBitmap;
    Paint paint;
    public BannerImageView(@NonNull @NotNull Context context) {
        super(context);
        initView();
    }

    public BannerImageView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BannerImageView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        paint=new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(getWidth()>0 && getHeight()>0 && getDrawable()==null)
        {
            if(placeHoldBitmap==null)
            {
                placeHoldBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.img_error);
            }

            int bitmapWidth=placeHoldBitmap.getWidth();
            int bitmapHeight=placeHoldBitmap.getHeight();

            int height=(int)(getHeight()*0.8f);
            int width=height*bitmapWidth/bitmapHeight;
            placeHoldBitmap=Bitmap.createScaledBitmap(placeHoldBitmap,width,height,false);
            int left=(getWidth()-width)/2;
            int top=(getHeight()-height)/2;

            canvas.drawBitmap(placeHoldBitmap,left,top,paint);
        }
    }
}
