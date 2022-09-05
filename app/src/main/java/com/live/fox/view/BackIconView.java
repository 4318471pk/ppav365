package com.live.fox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class BackIconView extends androidx.appcompat.widget.AppCompatImageView {

    Paint paint=new Paint();
    public BackIconView(Context context) {
        super(context);
        init();
    }

    public BackIconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(0x88000000);
        if(getWidth()>0 && getHeight()>0)
        {
            int half=getWidth()>getHeight()?getHeight()/2:getWidth()/2;
            canvas.drawCircle(getWidth()/2,getHeight()/2,half,paint);
        }
        super.onDraw(canvas);
    }
}
