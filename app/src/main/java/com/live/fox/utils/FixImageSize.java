package com.live.fox.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FixImageSize {

    public static void setImageSizeOnWidthWithSRC(ImageView view,int width)
    {
        setImageSizeOnWidthWithSRC(view,width,null);
    }

    public static void setImageSizeOnWidthWithSRC(ImageView view,int width,final OnFixListener onFixListener)
    {
        view.post(new Runnable() {
            @Override
            public void run() {
                if(view.getDrawable()!=null)
                {
                    int viewWidth=view.getDrawable().getIntrinsicWidth();
                    int viewHeight=view.getDrawable().getIntrinsicHeight();
                    ImageView imageView=(ImageView)view;
                    ViewGroup.LayoutParams layoutParams= imageView.getLayoutParams();
                    float ratio=1.0f*viewWidth/viewHeight;
                    layoutParams.width=width;
                    layoutParams.height=(int)(width/ratio);
                    view.setLayoutParams(layoutParams);
                    if(onFixListener!=null)
                    {
                        onFixListener.onfix(width,layoutParams.height,ratio);
                    }
                }

            }
        });
    }

    public static void setImageSizeOnWidthWithBackground(View view,int width)
    {
        setImageSizeOnWidthWithBackground(view,width,null);
    }

    public static void setImageSizeOnWidthWithBackground(View view,int width,final OnFixListener onFixListener)
    {
        view.post(new Runnable() {
            @Override
            public void run() {
                if(view.getBackground()!=null)
                {
                    int viewWidth=view.getBackground().getIntrinsicWidth();
                    int viewHeight=view.getBackground().getIntrinsicHeight();
                    ImageView imageView=(ImageView)view;
                    ViewGroup.LayoutParams layoutParams= imageView.getLayoutParams();
                    float ratio=1.0f*viewWidth/viewHeight;
                    layoutParams.width=width;
                    layoutParams.height=(int)(width/ratio);
                    view.setLayoutParams(layoutParams);
                    if(onFixListener!=null)
                    {
                        onFixListener.onfix(width,layoutParams.height,ratio);
                    }
                }
            }
        });
    }

    public interface OnFixListener
    {
        void onfix(int width,int height,float ratio);
    }
}
