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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DanmuContainerLuckView extends ViewGroup {
    public static final int LOW_SPEED = 1;
    public static final int NORMAL_SPEED = 3;
    public static final int HIGH_SPEED = 5;
    public static final int GRAVITY_TOP = 1;
    public static final int GRAVITY_CENTER = 2;
    public static final int GRAVITY_BOTTOM = 4;
    public static final int GRAVITY_FULL = 7;
    private int spanCount;
    private int WIDTH;
    private int HEIGHT;
    public List<View> spanList;
    private int singleLineHeight;
    DanmuConverter danmuConverter;
    int speed;
    OnItemClickListener onItemClickListener;
    Handler handler;
    ExecutorService singleThreadExecutor;
    private DanmuMgrLuck luck;

    public DanmuContainerLuckView(Context context) {
        this(context, (AttributeSet) null, 0);
    }

    public DanmuContainerLuckView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanmuContainerLuckView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
        this.spanCount = 8;
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
                    view.offsetLeftAndRight(0 - DanmuContainerLuckView.this.speed);
                } else if (msg.what == 2) {
                    view = (View) msg.obj;
                    DanmuContainerLuckView.this.removeView(view);
                } else if (msg.what == 110) {
                    TextView content = null;
                    View var = DanmuContainerLuckView.this.findViewById(R.id.content);
                    if (var != null && var instanceof TextView) {
                        content = (TextView) var;
                        content.setFocusable(true);
                        content.setSelected(true);

                    }
                }

            }
        };
        this.spanList = new ArrayList();
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
        if (0 != this.singleLineHeight) {
            this.spanCount = this.HEIGHT / this.singleLineHeight;
        }
        for (int i = 0; i < this.spanCount; ++i) {
            if (this.spanList.size() <= this.spanCount) {
                this.spanList.add(i, null);
            }
        }

    }

    public void setLeader(DanmuMgrLuck luck) {
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
                    if (DanmuContainerLuckView.this.onItemClickListener != null) {
                        DanmuContainerLuckView.this.onItemClickListener.onItemClick(model);
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
        if (this.spanList.size() == 0) {
            this.spanList.add(child);
        } else {
            this.spanList.set(bestLine, child);
        }

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
            float midleX = 0;
            if (DanmuContainerLuckView.this.WIDTH > childWidth) {
                midleX = Math.abs(DanmuContainerLuckView.this.WIDTH - childWidth) / 2.0f + childWidth;
            } else {
                midleX = childWidth - Math.abs(DanmuContainerLuckView.this.WIDTH - childWidth) / 2.0f;
            }

            while (tempX >= 0.0F) {
                tempX = this.view.getX() + childWidth;
                try {
                    if (tempX < midleX && !isWait) {
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
            if (DanmuContainerLuckView.this.spanList.get(((Integer) this.view.getTag()).intValue()) == this.view) {
                DanmuContainerLuckView.this.spanList.set(((Integer) this.view.getTag()).intValue(), null);
            }

            slideMessage(2, false);
        }

        private void slideMessage(int i, boolean isDelay) {
            Message msg;
            msg = Message.obtain(DanmuContainerLuckView.this.handler);
            msg.obj = this.view;
            msg.what = i;
            if (isDelay) DanmuContainerLuckView.this.handler.sendMessageDelayed(msg, 800);
            else DanmuContainerLuckView.this.handler.sendMessage(msg);
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
