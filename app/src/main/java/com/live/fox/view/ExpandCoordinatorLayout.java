package com.live.fox.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.live.fox.utils.DensityUtils;


/**
 * User: cheng
 * Date: 2017/6/18
 * Time: 17:07
 */
public class ExpandCoordinatorLayout extends CoordinatorLayout {

    private static int MIN_CONSUMED;
    private static int ONCE_MIN_CONSUMED;
    private boolean isHide;
    private int totalConsumed;
    private int viewHeight;


    private ValueAnimator bottomViewAnim;

    private View bottomView;
    private OnScrollListener onScrollListener;


    public ExpandCoordinatorLayout(Context context) {
        this(context, null);
    }

    public ExpandCoordinatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        MIN_CONSUMED = dp2px(context, 10);
        ONCE_MIN_CONSUMED = dp2px(context, 1);
        viewHeight = DensityUtils.dp2px(context,65);

        bottomViewAnim = new ValueAnimator();
        bottomViewAnim.setInterpolator(new LinearOutSlowInInterpolator());
        bottomViewAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setViewBottomMargin(bottomView, (Integer) animation.getAnimatedValue());
            }
        });
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed, int type) {
        super.onNestedPreScroll(target, dx, dy, consumed, type);
//        Log.e("ExpandCoordinatorLayout", "onNestedPreScroll()");

        if (bottomView == null) {
//            Log.e("ExpandCoordinatorLayout", getChildCount()+"");
//            Log.e("ExpandCoordinatorLayout", ((ViewGroup) getParent()).getChildCount()+"");
//            Log.e("ExpandCoordinatorLayout", ((ViewGroup) getParent().getParent()).getChildCount()+"");
//
//            ViewGroup viewGroup2 = this;
//            ViewGroup viewGroup3 = (ViewGroup) getParent();
//            ViewGroup viewGroup4 = (ViewGroup) getParent().getParent();
//            Log.e("ExpandCoordinatorLayout", (viewGroup4.getChildAt(1) instanceof View) +"");
//            Log.e("ExpandCoordinatorLayout", (viewGroup4.getChildAt(1) instanceof LinearLayout) +"");

            //这个地方要根据代码层数决定 按实际代码更改
            ViewGroup viewGroup = (ViewGroup) getParent().getParent();
            if (viewGroup != null && viewGroup.getChildCount() > 1) {
                bottomView = viewGroup.getChildAt(1);
                viewHeight = bottomView.getHeight();
            }

        }
        totalConsumed += dy;

        if (dy > 0) {
            if (dy > ONCE_MIN_CONSUMED || totalConsumed > MIN_CONSUMED) {
                hide(bottomView);
            }
        } else if (dy < 0) {
            if (dy < -ONCE_MIN_CONSUMED || totalConsumed < -MIN_CONSUMED) {
                show(bottomView);
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes, int type) {
        super.onStartNestedScroll(child, target, nestedScrollAxes, type);
        totalConsumed = 0;
        //竖直方向滚动时才操作
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    private void setEnabled(View view, boolean enabled) {
        if (view == null) return;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setEnabled(enabled);
            }
        } else {
            view.setEnabled(enabled);
        }
    }

    public void show(final View view) {
        if (!isHide || view == null) return;
        isHide = false;
        setEnabled(view, true);
        totalConsumed = 0;
        if (bottomViewAnim != null && bottomViewAnim.isRunning()) {
            bottomViewAnim.cancel();
        }
        //final MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
        //int bottomMargin = layoutParams.bottomMargin;
        bottomViewAnim.setIntValues(0, viewHeight);
        int duration = 300;
        bottomViewAnim.setDuration(duration);
        bottomViewAnim.start();
    }

    public void hide(final View view) {
        if (isHide || view == null) return;
        isHide = true;
        setEnabled(view, false);
        totalConsumed = 0;
        if (bottomViewAnim != null && bottomViewAnim.isRunning()) {
            bottomViewAnim.cancel();
        }
        //final MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
        //int bottomMargin = layoutParams.bottomMargin;
        //int height = layoutParams.height;
        bottomViewAnim.setIntValues(viewHeight, 0);
        int duration = 300;
        bottomViewAnim.setDuration(duration);
        bottomViewAnim.start();
        if (onScrollListener != null) {
            onScrollListener.onScrollX();
        }
    }

    //主要代码 这里要弄清楚根据移动数值来改变其那个margin值
    private void setViewBottomMargin(View view, int bottomMargin) {
//        Log.e("ExpandCoordinatorLayout", bottomMargin+"");
        if (view == null) return;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.bottomMargin = -(viewHeight - bottomMargin);
        view.setLayoutParams(layoutParams);
    }

    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 上下滑动监听
     */
    public interface OnScrollListener {
        void onScrollX();
    }
}
