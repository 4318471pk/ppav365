package com.live.fox.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.TextView;

import com.live.fox.utils.device.DeviceUtils;


@SuppressLint("AppCompatCustomView")
public class MarqueeTextView extends TextView {
    /** 是否停止滚动 */
    Context context;
    private boolean mStopMarquee;
    private String mText;
    private float mCoordinateX;
    private float mTextWidth;
    private float windowWith;
    int paddingTop = 0;
    Paint paint;
    private int index;

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setText(Context context, int index, String text) {
        this.context = context;
        this.mText = text;
        this.index = index;
        setGravity(Gravity.CENTER);
        mTextWidth = getPaint().measureText(mText);
        paddingTop = DeviceUtils.dp2px(context, 14);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        windowWith = displayMetrics.widthPixels;

        paint = getPaint();
        paint.setTextSize(DeviceUtils.dp2px(context, 12));//设置字体大小
        paint.setColor(Color.WHITE);

        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    public void updateText(int index, String text){
        this.index = index;
        this.mText = text;
    }


    @SuppressLint("NewApi")
    @Override
    protected void onAttachedToWindow() {
        mStopMarquee = false;
        if (!(mText == null || mText.isEmpty()))
            mHandler.sendEmptyMessageDelayed(0, 2000);
        super.onAttachedToWindow();
    }


    @Override
    protected void onDetachedFromWindow() {
        mStopMarquee = true;
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        super.onDetachedFromWindow();
    }


    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!(mText == null || mText.isEmpty()))
            canvas.drawText(mText, mCoordinateX+20, paddingTop, paint);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if(mCoordinateX < 0 && Math.abs(mCoordinateX) > mTextWidth){
                        if(onCompleteListener!=null) {
                            onCompleteListener.onComplete();
                        }
                        mCoordinateX = windowWith;
                    }else{
                        mCoordinateX -= 1;
                    }
                    invalidate();
                    sendEmptyMessageDelayed(0,15);
                    break;
            }
        }
    };

    public int getIndex() {
        return index;
    }



    private setOnCompleteListener onCompleteListener;
    public void setOnCompleteListener(setOnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public interface setOnCompleteListener {
        void onComplete();
    }
}
