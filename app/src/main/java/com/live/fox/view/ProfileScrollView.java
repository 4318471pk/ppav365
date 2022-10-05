package com.live.fox.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import android.content.Context;
        import android.graphics.Rect;
        import android.os.CountDownTimer;
        import android.util.AttributeSet;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.core.view.NestedScrollingParent2;
        import androidx.core.view.NestedScrollingParentHelper;
        import androidx.core.view.ViewCompat;

import com.live.fox.R;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.device.ScreenUtils;


/**
 * Author:  andy.xwt
 * Date:    2018/8/8 14:28
 * Description:NestedScrolling2机制下的嵌套滑动，实现NestedScrollingParent2接口下，处理fling效果的区别
 */

public class ProfileScrollView extends LinearLayout implements NestedScrollingParent2 {

    private View mTopView;
    private View roundLLMain;
    private int mTopViewHeight;
    private Rect zoomViewSrcRect = new Rect();

    private ViewGroup.LayoutParams zoomViewLp;
    private ViewGroup.LayoutParams roundViewLp;

    private int startY;
    private int currentY;
    private int lastY;
    private int offset;
    int divideHeight;


    private NestedScrollingParentHelper mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

    public ProfileScrollView(Context context) {
        this(context, null);
    }

    public ProfileScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    /**
     * 在嵌套滑动的子View未滑动之前，判断父view是否优先与子view处理(也就是父view可以先消耗，然后给子view消耗）
     *
     * @param target   具体嵌套滑动的那个子类
     * @param dx       水平方向嵌套滑动的子View想要变化的距离
     * @param dy       垂直方向嵌套滑动的子View想要变化的距离 dy<0向下滑动 dy>0 向上滑动
     * @param consumed 这个参数要我们在实现这个函数的时候指定，回头告诉子View当前父View消耗的距离
     *                 consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离 好让子view做出相应的调整
     * @param type     滑动类型，ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //这里不管手势滚动还是fling都处理
        boolean hideTop = dy > 0 && getScrollY() < mTopViewHeight-divideHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !target.canScrollVertically(-1);
        boolean isTopOnGoingDown=dy<0 && getScrollY()<=0;

        if (hideTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }

        Log.e("MMM",getScrollY()+" "+mTopViewHeight);
        if(isTopOnGoingDown && type == ViewCompat.TYPE_NON_TOUCH)
        {

            if(zoomViewLp.height>mTopViewHeight+60 )
            {
                ViewCompat.stopNestedScroll(target, type);
                countDownTimer.cancel();
                countDownTimer.start();

            }
            else
            {
                zoomViewLp.height = (int) (zoomViewLp.height + Math.abs(dy) * 0.45);
                mTopView.setLayoutParams(zoomViewLp);
            }
        }

//        if(isTopOnGoingDown && type == ViewCompat.TYPE_NON_TOUCH)
//        {
//            if(zoomViewLp.height>mTopViewHeight*1.15f )
//            {
//                ViewCompat.stopNestedScroll(target, type);
//                countDownTimer.cancel();
//                countDownTimer.start();
//
//            }
//            else
//            {
//                roundLLMain.getLayoutParams().height = (int) (roundLLMain.getLayoutParams()
//                        .height+ Math.abs(dy) * 0.45);
//                roundLLMain.setLayoutParams(zoomViewLp);
//            }
//        }



    }


    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //当子控件处理完后，交给父控件进行处理。
        if (dyUnconsumed < 0) {//表示已经向下滑动到头
            scrollBy(0, dyUnconsumed);
        }

    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            System.out.println("onStopNestedScroll");
        }

        mNestedScrollingParentHelper.onStopNestedScroll(target, type);
    }


    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //ViewPager修改后的高度= 总高度-导航栏高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = roundLLMain.getLayoutParams();
        layoutParams.height = getMeasuredHeight();
        roundLLMain.setLayoutParams(layoutParams);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = findViewById(R.id.ivHeader);
        roundLLMain = findViewById(R.id.roundLLMain);
        int dip10= ScreenUtils.getDip2px(getContext(),10);
        int statusBarHeight= StatusBarUtil.getStatusBarHeight(getContext());
        divideHeight=dip10*5+statusBarHeight+dip10*9;
        mTopView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)mTopView.getLayoutParams();
                lp.height=mTopView.getWidth();
                mTopView.setLayoutParams(lp);
                mTopViewHeight=mTopView.getLayoutParams().height;
            }
        });
//        if (!(mViewPager instanceof ViewPager)) {
//            throw new RuntimeException("id view_pager should be viewpager!");
//        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        super.scrollTo(x, y);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mTopView == null) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                lastY = startY;
                zoomViewSrcRect.set(mTopView.getLeft(), mTopView.getTop(), mTopView.getRight(), mTopView.getBottom());
                zoomViewLp = mTopView.getLayoutParams();
                break;
            case MotionEvent.ACTION_MOVE:
                currentY = (int) ev.getY();
                offset = currentY - lastY;
                lastY = currentY;
                if (((isVisibleLocal(mTopView, true) && offset > 0) || (mTopView.getBottom() > zoomViewSrcRect.bottom))) {
                    zoomViewLp.height = (int) (zoomViewLp.height + offset * 0.45);
                    mTopView.setLayoutParams(zoomViewLp);
                }
                if ((mTopView.getBottom() > zoomViewSrcRect.bottom)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isVisibleLocal(mTopView, true)) {
                    countDownTimer.cancel();
                    countDownTimer.start();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private final CountDownTimer countDownTimer = new CountDownTimer(500, 10) {
        @Override
        public void onTick(long millisUntilFinished) {
            zoomViewLp = mTopView.getLayoutParams();
            zoomViewLp.height = zoomViewLp.height - (int) ((zoomViewLp.height - zoomViewSrcRect.bottom) * ((float) (500 - millisUntilFinished) / 500));
            mTopView.setLayoutParams(zoomViewLp);
        }

        @Override
        public void onFinish() {
            zoomViewLp = mTopView.getLayoutParams();
            zoomViewLp.height = mTopViewHeight;
            Log.e("MMM2222",mTopViewHeight+" "+zoomViewLp.height+ " "+zoomViewSrcRect.bottom );
            mTopView.setLayoutParams(zoomViewLp);
        }
    };



    /**
     * 判断View是否可见
     *
     * @param target   View
     * @param judgeAll 为 true时,判断 View 全部可见才返回 true
     * @return boolean
     */
    public static boolean isVisibleLocal(View target, boolean judgeAll) {
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        if (judgeAll) {
            return rect.top == 0;
        } else {
            return rect.top >= 0;
        }
    }
}