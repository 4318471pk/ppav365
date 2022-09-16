package com.live.fox.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import com.live.fox.R;
import com.luck.picture.lib.tools.ScreenUtils;

import org.jetbrains.annotations.NotNull;

public class DropDownScrollView extends LinearLayout implements NestedScrollingParent2 {

    private View mTopView;
    private View hostTypeTabs;
    private int mTopViewHeight;
    private int barHeight;
    private View list;


    private NestedScrollingParentHelper mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

    public DropDownScrollView(Context context) {
        super(context);
    }

    public DropDownScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DropDownScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        boolean hideTop = dy > 0 && getScrollY() < (mTopViewHeight-barHeight);
//        boolean showTop = dy < 0 && getScrollY() >= 0 && !target.canScrollVertically(-1);
        boolean showTop = dy < 0 && getScrollY() >= 0 ;
        if(hideTop)
        {
            if(getScrollY()+dy<=(mTopViewHeight-barHeight))
            {
                scrollBy(0, dy);
            }
            else
            {
                scrollTo(0, mTopViewHeight-barHeight);
            }
        }

        if(showTop)
        {
            scrollBy(0, dy);
        }

        Log.e("BBBB",showTop+" "+hideTop+" "+getScrollY()+" "+mTopViewHeight+" "+barHeight);

        if(hideTop && mTopView!=null && hostTypeTabs!=null && getScrollY()>=mTopViewHeight-barHeight)
        {
            //向上
            mTopView.setVisibility(INVISIBLE);
            hostTypeTabs.setVisibility(VISIBLE);
        }

        if(showTop && mTopView!=null && hostTypeTabs!=null)
        {
            //向下
            mTopView.setVisibility(VISIBLE);
            hostTypeTabs.setVisibility(INVISIBLE);
        }
    }

    //DropDownViewAnimationController.getInstance().doAnimate(false,mTopView,hostTypeTabs);

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //当子控件处理完后，交给父控件进行处理。
        if (dyUnconsumed <= 0) {//表示已经向下滑动到头
            scrollBy(0, dyUnconsumed);
        }

    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        Log.e("DDDDD"," "+velocityX+" "+getScrollY()+" "+mTopViewHeight+" "+barHeight);

        return false;
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            System.out.println("onStopNestedScroll");
        }

//        mNestedScrollingParentHelper.onStopNestedScroll(target, type);
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = list.getLayoutParams();
        layoutParams.height = getMeasuredHeight()-barHeight;
        list.setLayoutParams(layoutParams);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView=findViewById(R.id.collapseView);
        list=findViewById(R.id.home_refreshLayout);
        hostTypeTabs=findViewById(R.id.hostTypeTabs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
        barHeight= ScreenUtils.dip2px(getContext(),40);
//        barHeight=hostTypeTabs.getMeasuredHeight();
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
}
