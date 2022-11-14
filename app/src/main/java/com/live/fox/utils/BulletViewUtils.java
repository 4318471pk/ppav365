package com.live.fox.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import java.lang.ref.WeakReference;

public class BulletViewUtils {

    static WeakReference<Activity> activityWeakReference;


    public static void goRightToLeftDisappear(View view, Activity activity)
    {
        activityWeakReference=new WeakReference<>(activity);
        Animation animation= new TranslateAnimation(Animation.RELATIVE_TO_PARENT,1,
                Animation.RELATIVE_TO_PARENT,-1
                ,Animation.ABSOLUTE,0
                ,Animation.ABSOLUTE,0);
        animation.setDuration(10000);
        animation.setInterpolator(new LinearInterpolator());

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(activityWeakReference!=null && activityWeakReference.get()!=null)
                {
                    if(!activityWeakReference.get().isDestroyed() && !activityWeakReference.get().isDestroyed())
                    {
                        ViewGroup viewGroup=(ViewGroup) view.getParent();
                        if(viewGroup!=null)
                        {
                            viewGroup.removeView(view);
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
}
