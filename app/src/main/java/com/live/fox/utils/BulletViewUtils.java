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

    static WeakReference<Activity> activityWeakReference;
    OnFinishAniListener onFinishAniListener;
    static boolean test;

    public static void goRightToLeftDisappear(View view, Activity activity,OnFinishAniListener onFinishAniListener)
    {

        activityWeakReference=new WeakReference<>(activity);
        Animation animation= new TranslateAnimation(Animation.RELATIVE_TO_PARENT,1,
                Animation.RELATIVE_TO_PARENT,-1
                ,Animation.ABSOLUTE,0
                ,Animation.ABSOLUTE,0);

        int index=new Random().nextInt(2000)+8000;
        animation.setDuration(index);
        animation.setInterpolator(new LinearInterpolator());
        test=true;

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                test=false;
                if(activityWeakReference!=null && activityWeakReference.get()!=null)
                {
                    if(!activityWeakReference.get().isDestroyed() && !activityWeakReference.get().isDestroyed())
                    {
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);

    }

    public interface OnFinishAniListener
    {
        void onFinish(Object obj);
    }
}
