package com.live.fox.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatRadioButton;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 *
 * 带动画效果的RadioButton
 *
 */
public class RadioButtonWithAnim extends AppCompatRadioButton {
    /**
     * 缩放比
     */
    private float animatedScaleRate = 1f;
    /**
     * 最小缩放比例
     */
    private float minScaleRate = .85f;
    /**
     * 最大缩放比例
     */
    private float maxScaleRate = 1.0f;

    /**
     * 是否打开了动画
     */
    private boolean animationOpen = true;

    /**
     * 默认图标drawable的Rect位置
     */
    private ArrayList<Rect> mDefaultDrawableBounds = null;

    public RadioButtonWithAnim(Context context){
        super(context);
        init(context, null);
    }

    public RadioButtonWithAnim(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public RadioButtonWithAnim(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
          //设置默认值
//        animationOpen = true;
//        maxScaleRate = 1.1f;
//        minScaleRate = .85f;
    }


    @Override
    public void setButtonDrawable(Drawable buttonDrawable) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            try {
                Field field = CompoundButton.class.getDeclaredField("mButtonDrawable");
                field.setAccessible(true);
                field.set(this, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.setButtonDrawable(buttonDrawable);
        }
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        if (checked) {
            ValueAnimator animator = ValueAnimator.ofFloat(minScaleRate, maxScaleRate);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();
                    scaleXY(v);
                }
            });
            animator.setDuration(300);
            animator.start();
        } else {
            if (animatedScaleRate != 1f) {
                ValueAnimator animator = ValueAnimator.ofFloat(animatedScaleRate, 1f);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float v = (float) animation.getAnimatedValue();
                        scaleXY(v);
                    }
                });
                animator.setDuration(0);
                animator.start();
            }
        }
    }

    public void scaleXY(float v){
        this.setScaleX(v);
        this.setScaleY(v);
    }
}
