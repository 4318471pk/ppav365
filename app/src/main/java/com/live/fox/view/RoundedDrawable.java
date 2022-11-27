package com.live.fox.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class RoundedDrawable extends Drawable {
    private Paint mPaint;
    private Bitmap mBitmap;
    private RectF mRectF;
    private int mRound;
    int width=0;
    int height=0;

    public RoundedDrawable(Bitmap bitmap) {
        this.mBitmap = bitmap;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
    }

    public RoundedDrawable(Bitmap bitmap,int width,int height) {
        this.mBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        BitmapShader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        this.width=width;
        this.height=height;
    }

    /**
     * 初始化区域
     */
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        mRectF = new RectF(left, top, right, bottom);
        super.setBounds(left, top, right, bottom);
    }
    /**
     * 核心代码： 绘制圆角
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRectF, mRound, mRound, mPaint);
    }
    /**
     * 暴露给外面设置圆角的大小
     *
     * @param round
     */
    public void setRound(int round) {
        this.mRound = round;
    }
    /**
     * getIntrinsicWidth、getIntrinsicHeight主要是为了在View使用wrap_content的时候，
     * 提供一下尺寸，默认为-1可不是我们希望的
     */
    @Override
    public int getIntrinsicHeight() {
        return width>0?width:mBitmap.getHeight();
    }
    @Override
    public int getIntrinsicWidth() {
        return height>0?height:mBitmap.getWidth();
    }
    /**
     * 根据画笔设定drawable的透明度
     */
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }
    /**
     * 根据画笔设定drawable的颜色过滤器
     */
    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
