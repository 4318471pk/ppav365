package com.live.fox.utils;

import android.app.Activity;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

import java.lang.ref.WeakReference;
import java.util.Random;

public class BulletViewUtils {

    OnFinishAniListener onFinishAniListener;

    public static void goRightToLeftDisappear(View view,OnFinishAniListener onFinishAniListener)
    {

        Animation animation= new TranslateAnimation(Animation.RELATIVE_TO_PARENT,1,
                Animation.RELATIVE_TO_PARENT,-1
                ,Animation.ABSOLUTE,0
                ,Animation.ABSOLUTE,0);

        int index=new Random().nextInt(2000)+8000;
        animation.setDuration(index);
        animation.setInterpolator(new LinearInterpolator());

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                ViewGroup viewGroup=(ViewGroup) view.getParent();

                Object obj=view.getTag();
                if(viewGroup!=null)
                {
                    viewGroup.removeView(view);
                }
                if(onFinishAniListener!=null)
                {
                    onFinishAniListener.onFinish(obj);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);

    }


    public static void goRightToLeftStopThenDisappear(View view,Activity activity,OnFinishAniListener onFinishAniListener)
    {
        WeakReference<Activity> activityWeakReference=new WeakReference<>(activity);
        Animation animation= new TranslateAnimation(Animation.RELATIVE_TO_PARENT,1,
                Animation.RELATIVE_TO_PARENT,0
                ,Animation.ABSOLUTE,0
                ,Animation.ABSOLUTE,0);

        int index=new Random().nextInt(500)+1500;
        animation.setDuration(index);
        animation.setFillAfter(true);
        animation.setInterpolator(new LinearInterpolator());

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(isActivityAvailable(activityWeakReference))
                {
                    view.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(isActivityAvailable(activityWeakReference))
                            {
                                disappear(view,activityWeakReference,onFinishAniListener);
                            }
                        }
                    },2000);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);

    }

    private static void disappear(View view,WeakReference<Activity> weakReference,OnFinishAniListener onFinishAniListener)
    {
        Animation animation= new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0,
                Animation.RELATIVE_TO_PARENT,-1
                ,Animation.ABSOLUTE,0
                ,Animation.ABSOLUTE,0);

        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(isActivityAvailable(weakReference))
                {
                    weakReference.clear();
                    ViewGroup viewGroup=(ViewGroup) view.getParent();
                    Object obj=view.getTag();
                    if(viewGroup!=null)
                    {
                        viewGroup.removeView(view);
                    }
                    if(onFinishAniListener!=null)
                    {
                        onFinishAniListener.onFinish(obj);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    private static boolean isActivityAvailable(WeakReference<Activity> activityWeakReference)
    {
        boolean isAvailable=false;
        if(activityWeakReference!=null && activityWeakReference.get()!=null)
        {
            if(!activityWeakReference.get().isDestroyed() && !activityWeakReference.get().isFinishing())
            {
                isAvailable=true;
            }
        }
        return isAvailable;
    }


    public interface OnFinishAniListener
    {
        void onFinish(Object obj);
    }
}
