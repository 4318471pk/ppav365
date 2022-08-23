package com.live.fox.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.reflect.Field;

/**
 *
 * ImageSpan使用网络Url 结合Glide使用
 * Created by 罗成 on 2019/01/06
 *
 */
public class UrlImageSpan extends ImageSpan {

    private String url;
    private TextView tv;
    private boolean picShowed;

    private int width;
    private int height;

    public UrlImageSpan(Context context, int defaultId, String url, TextView tv) {
        super(context, defaultId);
        this.url = url;
        this.tv = tv;
    }

    public UrlImageSpan(Context context, int defaultId, String url, TextView tv, int width, int height) {
        super(context, defaultId);
        this.url = url;
        this.tv = tv;
        this.width = width;
        this.height = height;
    }

    @Override
    public Drawable getDrawable() {
        if (!picShowed) {
            Glide.with(tv.getContext()).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    Resources resources = tv.getContext().getResources();
                    int targetWidth = (int) (resources.getDisplayMetrics().widthPixels * 0.8);
                    Bitmap zoom = zoom(resource, targetWidth);
                    BitmapDrawable b = new BitmapDrawable(resources, zoom);

                    if(width>0){
                        b.setBounds(0, 0, width, height);
                    }else {
                        b.setBounds(0, 0, b.getIntrinsicWidth(), b.getIntrinsicHeight());
                    }

                    Field mDrawable;
                    Field mDrawableRef;
                    try {
                        mDrawable = ImageSpan.class.getDeclaredField("mDrawable");
                        mDrawable.setAccessible(true);
                        mDrawable.set(UrlImageSpan.this, b);

                        mDrawableRef = DynamicDrawableSpan.class.getDeclaredField("mDrawableRef");
                        mDrawableRef.setAccessible(true);
                        mDrawableRef.set(UrlImageSpan.this, null);

                        picShowed = true;
                        tv.setText(tv.getText());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }

//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    Resources resources = tv.getContext().getResources();
//                    int targetWidth = (int) (resources.getDisplayMetrics().widthPixels * 0.8);
//                    Bitmap zoom = zoom(resource, targetWidth);
//                    BitmapDrawable b = new BitmapDrawable(resources, zoom);
//
//                    b.setBounds(0, 0, b.getIntrinsicWidth(), b.getIntrinsicHeight());
//                    Field mDrawable;
//                    Field mDrawableRef;
//                    try {
//                        mDrawable = ImageSpan.class.getDeclaredField("mDrawable");
//                        mDrawable.setAccessible(true);
//                        mDrawable.set(UrlImageSpan.this, b);
//
//                        mDrawableRef = DynamicDrawableSpan.class.getDeclaredField("mDrawableRef");
//                        mDrawableRef.setAccessible(true);
//                        mDrawableRef.set(UrlImageSpan.this, null);
//
//                        picShowed = true;
//                        tv.setText(tv.getText());
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (NoSuchFieldException e) {
//                        e.printStackTrace();
//                    }
//                }
            });
        }
        return super.getDrawable();
    }

    /**
     * 按宽度缩放图片
     *
     * @param bmp  需要缩放的图片源
     * @param newW 需要缩放成的图片宽度
     *
     * @return 缩放后的图片
     */
    public static Bitmap zoom(@NonNull Bitmap bmp, int newW) {

        // 获得图片的宽高
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        // 计算缩放比例
        float scale = ((float) newW) / width;

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

        return newbm;
    }
}
