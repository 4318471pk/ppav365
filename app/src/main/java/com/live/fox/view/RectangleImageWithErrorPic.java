package com.live.fox.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.live.fox.R;
import com.live.fox.utils.ScreenUtils;
import com.makeramen.roundedimageview.RoundedImageView;

public class RectangleImageWithErrorPic extends RoundedImageView {

    boolean isShowErrorPic=false;
    Drawable errorDrawable=null;
    int dip10;
    int width,height;
    Paint paint;

    public RectangleImageWithErrorPic(Context context) {
        this(context,null);
    }

    public RectangleImageWithErrorPic(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RectangleImageWithErrorPic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView()
    {
        paint=new Paint();
        errorDrawable= getContext().getResources().getDrawable(R.mipmap.img_error);
        dip10= ScreenUtils.dp2px(getContext(),10);
        width=errorDrawable.getIntrinsicWidth();
        height=errorDrawable.getIntrinsicHeight();
    }

    public void showErrorPic()
    {
        isShowErrorPic=true;
        invalidate();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        isShowErrorPic=false;
        super.setImageDrawable(drawable);

    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        isShowErrorPic=false;
        super.setImageBitmap(bm);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(getWidth()>0 && getHeight()>0 && isShowErrorPic)
        {
            int sHeight=getHeight()-dip10;
            int sWidth=sHeight*width/height;
            errorDrawable.setBounds(200,dip10,sWidth,sHeight);
            errorDrawable.draw(canvas);
        }
    }
}
