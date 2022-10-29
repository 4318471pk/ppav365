package com.live.fox.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/13.
 */
public class AutoVerticalScrollTextView extends androidx.appcompat.widget.AppCompatTextView {

    public int delay=1000;//延迟1秒 再向上滚
    private Context context;
    private String[] titles;
    private ArrayList<String> Arrtitles;
    public static final int Show=0;//开始
    public static final int Disappear=1;//开始
    public static final int pause=2;//停止
    private int TextstrIndex=0;
    String CurrentCharacter;

    private Handler handler=new Handler(Looper.myLooper()){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what)
            {
                case 0:
                    ScrollMethodShow();
                    break;
                case 1:
                    ScrollMethodDisapper();
                    break;
                case 2:

                    break;

            }
        }
    };

    public AutoVerticalScrollTextView(Context context) {
        super(context);
        Initview(context);
    }

    public AutoVerticalScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Initview(context);
    }

    public AutoVerticalScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Initview(context);
    }

    private void Initview(Context context)
    {
        this.context=context;
    }

    private void ScrollMethodShow()
    {

        if(titles!=null && titles.length>0)
            CurrentCharacter=titles[TextstrIndex];
        else
            CurrentCharacter=Arrtitles.get(TextstrIndex);

        setText(CurrentCharacter);

        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 0f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation);
        animationSet.setFillBefore(true);
        animationSet.setFillAfter(true);
        animationSet.setDuration(500);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.sendEmptyMessageDelayed(Disappear,delay);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animationSet);
    }

    private void ScrollMethodDisapper()
    {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation);
        animationSet.setFillBefore(true);
        animationSet.setFillAfter(true);
        animationSet.setDuration(500);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TextstrIndex++;
                if(titles!=null && titles.length>0 && TextstrIndex==titles.length)
                    TextstrIndex=0;
                if(Arrtitles!=null && Arrtitles.size()>0 && TextstrIndex==Arrtitles.size())
                    TextstrIndex=0;
                handler.sendEmptyMessage(Show);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animationSet);
    }

    public void StartScroll()
    {
        boolean arr=Arrtitles==null || Arrtitles.size()<1;
        boolean ti=titles==null || titles.length<1;
        if(arr && ti)
        {
            return;
        }
        handler.sendEmptyMessage(Show);
    }

    public void PauseScroll()
    {
        handler.removeMessages(Show);
        clearAnimation();
    }


    public ArrayList<String> getArrtitles() {
        return Arrtitles;
    }

    public void setArrtitles(ArrayList<String> arrtitles) {
        Arrtitles = arrtitles;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }
}
