package com.live.fox.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;


import androidx.appcompat.widget.AppCompatTextView;

import com.live.fox.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/13.
 */
public class AutoVerticalScrollTextView extends AppCompatTextView {

    public int delay = 1000;//延迟1秒 再向上滚
    private Context context;
    private ArrayList<CharSequence> charSequences = new ArrayList<>();
    public static final int Show = 0;//开始
    public static final int Disappear = 1;//开始
    public static final int pause = 2;//停止
    private int TextStrIndex = 0;
    CharSequence CurrentCharacter;
    boolean isRunning = false;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
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

    private void Initview(Context context) {
        this.context = context;
    }

    private void ScrollMethodShow() {
        CurrentCharacter = charSequences.get(TextStrIndex);

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
                if (TextStrIndex + 1 == charSequences.size()) {
                    isRunning = false;
                } else {
                    TextStrIndex++;
                    charSequences.set(TextStrIndex-1, null);
                    handler.sendEmptyMessageDelayed(Disappear, delay);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animationSet);
    }

    private void ScrollMethodDisapper() {
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
//                //清除内存
                handler.sendEmptyMessage(Show);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startAnimation(animationSet);
    }

    public void pauseScroll() {
        handler.removeMessages(Show);
        clearAnimation();
    }

    public void addCharSequence(CharSequence charSequence) {
        this.charSequences.add(charSequence);

        if (charSequences.size() == 1) {
            setText(charSequences.get(0));
            setBackgroundResource(R.drawable.bg_66000000_round_10dip);
        } else {
            if (!isRunning) {
                isRunning = true;
                handler.sendEmptyMessage(Disappear);
                TextStrIndex++;
            }
        }
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

}
