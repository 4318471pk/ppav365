package com.live.fox.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.R;

/**
 * @author keynes
 * @date 2018/04/19/019 上午 10:51
 * @Describe 默认倒计时5秒。
 */
public class CountTimerUtil {

    // 默认计时
    private  final int DEFAULT_REPEAT_COUNT = 2;

    // 当前的计时
    private  int sCurCount = 3;

    OnAnimationFinishListener listener;
    static CountTimerUtil countTimerUtil;

    public static CountTimerUtil getInstance()
    {
        if(countTimerUtil==null)
        {
            countTimerUtil=new CountTimerUtil();
        }
        return countTimerUtil;
    }

    public void start(RelativeLayout relativeLayout,OnAnimationFinishListener onAnimationFinishListener) {
        sCurCount=3;
        listener=onAnimationFinishListener;
        Context context=relativeLayout.getContext();
        int dip100=ScreenUtils.dp2px(context,100);
        TextView textView=new TextView(context);
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(dip100,dip100);
        rl.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        textView.setLayoutParams(rl);
        textView.setTextColor(0xffffffff);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,48);
        textView.setGravity(Gravity.CENTER);
        textView.setBackground(context.getResources().getDrawable(R.drawable.live_room_anima_dot));
        relativeLayout.addView(textView);
        start(textView, DEFAULT_REPEAT_COUNT);
    }

    /**
     * @param animationViewTv 要被倒计时的控件
     * @param repeatCount     要倒计时多久，单位，秒。
     */
    public  <T extends TextView> void start(final T animationViewTv, final int repeatCount) {

        // 设置计时
        animationViewTv.setText(String.valueOf(sCurCount));
        animationViewTv.setVisibility(View.VISIBLE);

        // 透明度渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        // 缩放渐变动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(
            0.1f, 1.3f, 0.1f, 1.3f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setRepeatCount(DEFAULT_REPEAT_COUNT);
        alphaAnimation.setRepeatCount(DEFAULT_REPEAT_COUNT);
        alphaAnimation.setDuration(1000);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束时，隐藏
//                animationViewTv.setVisibility(View.GONE);
                ViewGroup viewGroup=(ViewGroup) animationViewTv.getParent();
                viewGroup.removeView(animationViewTv);
                if(listener!=null)
                {
                    listener.onFinish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // 减秒
                --sCurCount;
                // 设置文本
                if (sCurCount == 0) {
//                   ViewGroup viewGroup=(ViewGroup) animationViewTv.getParent();
//                   viewGroup.removeView(animationViewTv);
                }
                else
                {
                    animationViewTv.setText(sCurCount+"");
                }


            }
        });

        // 两个动画同时播放
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationViewTv.startAnimation(animationSet);
    }

    public interface OnAnimationFinishListener
    {
        void onFinish();
    }
}
