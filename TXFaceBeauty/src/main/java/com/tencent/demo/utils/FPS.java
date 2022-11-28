package com.tencent.demo.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

public class FPS {

    private static final String TAG = "FPS";

    /** static: 多个实例共享一个Handler就可以了 */
    private static Handler sHandler;
    private volatile boolean isRunning = true;
    private final Recorder mRecorder = new Recorder();
    private final String mName;
    
    private OnFpsListener mOnFpsListener;

    public FPS(String name) {
        mName = name;
        if (sHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_LESS_FAVORABLE);
            handlerThread.start();
            sHandler = new Handler(handlerThread.getLooper());
        }
        sHandler.postDelayed(mCalcFpsRunnable, 250);
    }

    private Runnable mCalcFpsRunnable = new Runnable() {

        private volatile float fps;
        private final Recorder mCopy = new Recorder();
        private final Handler mUiThreadHandler = new Handler(Looper.getMainLooper());

        private Runnable mInvokeListenerRunnable = new Runnable() {
            @Override
            public void run() {
                if (mOnFpsListener != null) {
                    mOnFpsListener.onFps(mName, fps);
                }
            }
        };

        @Override
        public void run() {
            mRecorder.copyToAndReset(mCopy);
            long cost = mCopy.lastTime - mCopy.startTime;
            if (cost > 0) {
                fps = mCopy.jobCount * 1f / cost * 1000;
                mUiThreadHandler.post(mInvokeListenerRunnable);
            }

            //定时重复
            if (isRunning) {
                sHandler.postDelayed(mCalcFpsRunnable, 1000);
            }
        }
    };

    public void quit() {
        // 不能把 sHandler 干掉, 应为是多个实例共享的, 可能由于时机先后, 把别人刚启动的示例也停止了
        // if (sHandler != null) {
        //     sHandler.removeCallbacksAndMessages(null);
        //     sHandler.getLooper().quit();
        //     sHandler = null;
        // }
        isRunning = false;
    }

    public void onOneFrame() {
        mRecorder.record();
    }

    private static class Recorder {

        private final Object mLock = new Object();
        
        volatile int jobCount = 0;
        volatile long startTime = 0;
        volatile long lastTime = 0;

        void record() {
            synchronized (mLock) {
                this.jobCount++;
                if (this.startTime == 0) {
                    this.startTime = System.currentTimeMillis();
                }
                this.lastTime = System.currentTimeMillis();
            }
        }

        void copyToAndReset(Recorder target) {
            synchronized (mLock) {
                target.jobCount = jobCount;
                target.startTime = startTime;
                target.lastTime = lastTime;
                jobCount = 0;
                startTime = 0;
                lastTime = 0;
            }
        }
    }

    public void setOnFpsListener(OnFpsListener onFpsListener) {
        mOnFpsListener = onFpsListener;
    }

    public interface OnFpsListener {

        void onFps(String name, float fps);
    }
}
