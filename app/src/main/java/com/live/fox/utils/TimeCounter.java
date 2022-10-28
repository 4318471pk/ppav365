package com.live.fox.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

//此计时器不需要关闭
public class TimeCounter {

    static TimeCounter timeCounter;
    List<TimeListener> listeners = new ArrayList<>();


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            for (int i = 0; i < listeners.size(); i++) {
                TimeListener listener = listeners.get(i);
                if (listener != null) {
                    listener.onSecondTick(listener);
                }
            }
            sendEmptyMessageDelayed(0, 1000);
        }
    };

    public TimeCounter() {
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    public static TimeCounter getInstance() {
        if (timeCounter == null) {
            timeCounter = new TimeCounter();
        }
        return timeCounter;
    }

    public void add(TimeListener timeListener) {
        if (!listeners.contains(timeListener)) {
            timeListener.revertCounting();
            listeners.add(timeListener);
        }

    }

    public void remove(TimeListener timeListener) {
        if(timeListener!=null)
        {
            timeListener.revertCounting();
            listeners.remove(timeListener);
        }
    }

    public void onAppExit()
    {
        listeners.clear();
    }

    public abstract static class TimeListener {
        int second=1;
        int countingSecond=0;

        public  final void revertCounting()
        {
            countingSecond=0;
        }

        public TimeListener() {
            //不写参数默认是1秒响应一次
        }

        public TimeListener(int second) {
            if(second<1)
            {
                second=1;
            }
            this.second = second;
        }

        public void onSecondTick(TimeListener listener) {
            if(second>1)
            {
                countingSecond++;
                if(countingSecond==second)
                {
                    countingSecond=0;
                    onConditionTrigger(listener);
                }
            }
        }

        public void onConditionTrigger(TimeListener listener) {

        }
    }
}

