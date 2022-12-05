package com.live.fox.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;

import org.jetbrains.annotations.NotNull;

public class UserProfileImageView extends androidx.appcompat.widget.AppCompatImageView {

    Drawable drawable=null;
    Bitmap placeHoldBitmap;
    Paint paint;

    public UserProfileImageView(@NonNull @NotNull Context context) {
        super(context);
        initView();
    }

    public UserProfileImageView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public UserProfileImageView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView()
    {
        drawable=getResources().getDrawable(R.mipmap.bg_profile_img_background);
        placeHoldBitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.user_head_error);
//        drawable.setAlpha(200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getWidth()>0 && getHeight()>0)
        {
            if(getDrawable()==null)
            {
                canvas.drawBitmap(placeHoldBitmap,0,0,paint);
            }

            drawable.setBounds(0,0,getWidth(),getHeight());
            drawable.draw(canvas);
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        return false;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return false;
//    }

}
