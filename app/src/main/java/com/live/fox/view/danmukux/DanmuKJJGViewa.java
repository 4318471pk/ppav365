package com.live.fox.view.danmukux;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.utils.LogUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DanmuKJJGViewa extends ViewGroup {

    public static final int LOW_SPEED = 1;
    public static final int NORMAL_SPEED = 3;
    public static final int HIGH_SPEED = 5;
    public static final int GRAVITY_TOP = 1;
    public static final int GRAVITY_CENTER = 2;
    public static final int GRAVITY_BOTTOM = 4;
    public static final int GRAVITY_FULL = 7;
    private int WIDTH;
    private int HEIGHT;
    private int singleLineHeight;
    DanmuConverter danmuConverter;
    int speed;
    OnItemClickListener onItemClickListener;
    Handler handler;
    ExecutorService singleThreadExecutor;
    private DanmuMgrKJJGa luck;

    public DanmuKJJGViewa(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public DanmuKJJGViewa(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanmuKJJGViewa(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }

        this.danmuConverter = null;
        this.speed = NORMAL_SPEED;
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                View view;
                if (msg.what == 0) {
                } else if (msg.what == 1) {
                    view = (View) msg.obj;
                    view.offsetLeftAndRight(0 - DanmuKJJGViewa.this.speed);
                } else if (msg.what == 2) {
                    view = (View) msg.obj;
                    DanmuKJJGViewa.this.removeView(view);
                } else if (msg.what == 110) {
                    TextView content = null;
                    View var = DanmuKJJGViewa.this.findViewById(R.id.content);
                    if (var != null && var instanceof TextView) {
                        content = (TextView) var;
                        content.setFocusable(true);
                        content.setSelected(true);

                    }
                }
            }
        };
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setSpeed(int s) {
        this.speed = s;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        this.WIDTH = width;
        this.HEIGHT = height;

    }

    public void setLeader(DanmuMgrKJJGa luck) {
        this.luck = luck;
    }

    public void setConverter(DanmuConverter converter) {
        this.danmuConverter = converter;
        this.singleLineHeight = converter.getSingleLineHeight();
    }

    public <M> void addDanmu(final M model) throws Error {
        if (this.danmuConverter == null) {
            throw new Error("DanmuConverter can't be null,you should call setConverter firstly");
        } else {
            View danmuView = this.danmuConverter.convert(model);
            this.addView(danmuView);
            danmuView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DanmuKJJGViewa.this.onItemClickListener != null) {
                        DanmuKJJGViewa.this.onItemClickListener.onItemClick(model);
                    }

                }
            });
        }
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        child.measure(0, 0);
        int width = child.getMeasuredWidth();
        int height = child.getMeasuredHeight();
        int bestLine = this.getBestLine();
        child.layout(this.WIDTH, this.singleLineHeight * bestLine, this.WIDTH + width, this.singleLineHeight * bestLine + height);
        child.setTag(Integer.valueOf(bestLine));
        if (!singleThreadExecutor.isShutdown()) {
            singleThreadExecutor.submit(new MyRunnable(child));
        }
    }

    private int getBestLine() {

        return 0;
    }

    private class MyRunnable implements Runnable {
        View view;

        public MyRunnable(View v) {
            this.view = v;
        }

        @Override
        public void run() {
            slideMessage(0, false);
            float childWidth = this.view.getWidth();
            float tempX = this.view.getX() + childWidth;
            boolean isWait = false;

            while (tempX >= 0.0F) {
                tempX = this.view.getX() + childWidth;
                try {
                    if (tempX < childWidth + 30 && !isWait) {
                        slideMessage(110, true);
                        isWait = true;
                        Thread.sleep(3500);
                    } else {
                        Thread.sleep(8);
                    }
                } catch (InterruptedException var3) {
                    LogUtils.e(var3.getMessage());
                }
                slideMessage(1, false);
            }

            slideMessage(2, false);
        }

        private void slideMessage(int i, boolean isDelay) {
            Message msg;
            msg = Message.obtain(DanmuKJJGViewa.this.handler);
            msg.obj = this.view;
            msg.what = i;
            if (isDelay) DanmuKJJGViewa.this.handler.sendMessageDelayed(msg, 800);
            else DanmuKJJGViewa.this.handler.sendMessage(msg);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.luck != null) this.luck.destroy();
        if (this.singleThreadExecutor != null) this.singleThreadExecutor.shutdown();
        if (this.handler != null) {
            this.handler.removeMessages(0);
            this.handler.removeMessages(1);
            this.handler.removeMessages(2);
            this.handler.removeMessages(110);
        }
    }

    public interface OnItemClickListener<M> {
        void onItemClick(M var1);
    }
}
