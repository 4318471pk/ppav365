package com.live.fox.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.live.fox.App;


public class FixImageSize {

    public static void setImageSizeOnWidthWithSRC(ImageView view,int width)
    {
        setImageSizeOnWidthWithSRC(view,width,null);
    }

    public static void setImageSizeOnWidthWithSRC(ImageView view,int width,final OnFixListener onFixListener)
    {
        if(view==null)return;
        if(view.getDrawable()==null)return;
        int viewWidth=view.getDrawable().getIntrinsicWidth();
        int viewHeight=view.getDrawable().getIntrinsicHeight();

        App.getHandler().post(new Runnable() {
            @Override
            public void run() {
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
        });

    }

    public static void setImageSizeOnWidthWithBackground(View view,int width)
    {
        setImageSizeOnWidthWithBackground(view,width,null);
    }

    public static void setImageSizeOnWidthWithBackground(View view,int width,final OnFixListener onFixListener)
    {
        if(view==null) return;
        if(view.getBackground()==null)return;
        int viewWidth=view.getBackground().getIntrinsicWidth();
        int viewHeight=view.getBackground().getIntrinsicHeight();

        App.getHandler().post(new Runnable() {
            @Override
            public void run() {
                if(view.getBackground()!=null)
                {
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
