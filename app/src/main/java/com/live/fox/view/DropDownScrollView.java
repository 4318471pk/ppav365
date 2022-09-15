package com.live.fox.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class DropDownScrollView extends ScrollView {

    DropDownViewGroup dropDownViewGroup;
    int limitDistance,itemViewHeight=-1;
    ValueAnimator mValueAnimator;
    boolean isInterrupt=false;

    public DropDownScrollView(Context context) {
        super(context);
    }

    public DropDownScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DropDownScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init()
    {

    }

    public void setDropDown(DropDownViewGroup dropDownView,int limitDistance)
    {
        this.dropDownViewGroup=dropDownView;
        this.limitDistance=limitDistance;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(ev.getAction()==MotionEvent.ACTION_UP)
        {

        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int Y, int oldl, int oldY) {
        super.onScrollChanged(l, Y, oldl, oldY);

        if(isInterrupt)
        {
            return;
        }

        if(itemViewHeight<1)
        {
            itemViewHeight=dropDownViewGroup.getHeight();
        }
        if(itemViewHeight<1)
        {
            return;
        }
        if(Y<0 || oldY<0)
        {
            return;
        }

//        float ratio =1.0f* y/limitDistance;
//        RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) dropDownViewGroup.getLayoutParams();
//        int offsetY=-dropDownViewGroup.getHeight()+((int)(dropDownViewGroup.getHeight()*ratio));
//        if(offsetY<=0)
//        {
//            rl.topMargin=offsetY;
//            dropDownViewGroup.setLayoutParams(rl);
//        }

        if(Y-oldY>0 && Y<limitDistance)
        {
            //向上推 而且小于收缩高度 显示
            if(dropDownViewGroup.getScrollY()==itemViewHeight)
            {
                isInterrupt=true;
                smoothScrollTo(0,limitDistance);
                startAnimation(dropDownViewGroup,dropDownViewGroup.getHeight(),itemViewHeight,0);
            }
        }

        if(oldY-Y>0)
        {
            //向下拉 收回
            if(dropDownViewGroup.getScrollY()==0)
            {
                isInterrupt=true;
                startAnimation(dropDownViewGroup,dropDownViewGroup.getHeight(),0,itemViewHeight);
            }
        }

    }


    private void startAnimation(View view, long duration, int startY, int endY) {
        if (mValueAnimator == null) {
            mValueAnimator = new ValueAnimator();
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    view.scrollTo(0, animatedValue);
                }
            });
            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isInterrupt=false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        } else {
            mValueAnimator.cancel();
        }

        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.setIntValues(startY, endY);
        mValueAnimator.setDuration(duration);
        mValueAnimator.start();
    }
}
