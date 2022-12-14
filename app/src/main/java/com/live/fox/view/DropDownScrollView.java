package com.live.fox.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import com.google.gson.Gson;
import com.live.fox.R;
import com.luck.picture.lib.tools.ScreenUtils;

import org.jetbrains.annotations.NotNull;

public class DropDownScrollView extends LinearLayout implements NestedScrollingParent2 {

    private View mTopView;
    private View hostTypeTabs;
    private View rlBanner;
    private int mTopViewHeight;
    private int barHeight,bannerHeight;
    private View list;
    AlphaAnimation fadeIn=new AlphaAnimation(0f,1f);
    AlphaAnimation fadeOut=new AlphaAnimation(1f,0f);


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
     * ?????????????????????View???????????????????????????view??????????????????view??????(????????????view??????????????????????????????view?????????
     *
     * @param target   ?????????????????????????????????
     * @param dx       ??????????????????????????????View?????????????????????
     * @param dy       ??????????????????????????????View????????????????????? dy<0???????????? dy>0 ????????????
     * @param consumed ???????????????????????????????????????????????????????????????????????????View?????????View???????????????
     *                 consumed[0] ????????????????????????consumed[1] ????????????????????? ?????????view?????????????????????
     * @param type     ???????????????ViewCompat.TYPE_NON_TOUCH fling??????,ViewCompat.TYPE_TOUCH ????????????
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

        //??????????????????????????????fling?????????
        boolean hideTop = dy > 0 && getScrollY() < (mTopViewHeight-barHeight);
//        boolean showTop = dy < 0 && getScrollY() >= 0 && !target.canScrollVertically(-1);
        boolean showTop = dy < 0 && getScrollY() >= 0 ;
        if(hideTop)
        {
            if (getScrollY() + dy > (mTopViewHeight - barHeight)) {
                dy = mTopViewHeight - barHeight - getScrollY();
            }
            scrollBy(0, dy);
            consumed[1] = dy;
        }

        if(dy < 0)
        {
            if(getScrollY()+dy < 0)
            {
                dy=-getScrollY();
            }
            scrollBy(0, dy);
            consumed[1] = dy;
        }



        if(hideTop && mTopView!=null && hostTypeTabs!=null && getScrollY()>=mTopViewHeight-barHeight)
        {
            //??????
//            Log.e("HHHHHH",showTop+" "+hideTop+" "+getScrollY()+" "+mTopViewHeight+" "+barHeight);
            startFadeIn(hostTypeTabs);
            startFadeOut(mTopView);
        }

//        Log.e("HHHHHH3333",showTop+" "+hideTop+" "+getScrollY()+" "+mTopViewHeight+" "+barHeight);
        if(showTop && mTopView!=null && hostTypeTabs!=null && getScrollY()<mTopViewHeight-barHeight && getScrollY()>=0)
        {
            //??????
//            Log.e("HHHHHH2222",showTop+" "+hideTop+" "+getScrollY()+" "+mTopViewHeight+" "+barHeight);
            if(mTopView.getVisibility()!=VISIBLE && hostTypeTabs.getVisibility()!=GONE)
            {
                startFadeIn(mTopView);
                startFadeOut(hostTypeTabs);
            }
        }
    }


    private void startFadeIn(View view)
    {
        view.clearAnimation();
        fadeIn.setDuration(100);
//        fadeIn.setFillAfter(true);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(fadeIn);
        fadeIn.start();
    }

    private void startFadeOut(View view)
    {
        view.clearAnimation();
        fadeOut.setDuration(100);
//        fadeOut.setFillAfter(true);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(fadeOut);
        fadeOut.start();
    }
    //DropDownViewAnimationController.getInstance().doAnimate(false,mTopView,hostTypeTabs);

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //?????????????????????????????????????????????????????????
        if (dyUnconsumed <= 0) {//??????????????????????????????
            scrollBy(0, dyUnconsumed);
        }

    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        Log.e("DDDDD"," "+velocityY+" "+getScrollY()+" "+mTopViewHeight+" "+barHeight);
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = list.getLayoutParams();
        layoutParams.height = getMeasuredHeight()-barHeight-bannerHeight;
        list.setLayoutParams(layoutParams);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView=findViewById(R.id.collapseView);
        list=findViewById(R.id.home_refreshLayout);
        hostTypeTabs=findViewById(R.id.hostTypeTabs);
        rlBanner=findViewById(R.id.rlBanner);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
        barHeight=hostTypeTabs.getMeasuredHeight();
        bannerHeight=rlBanner.getMeasuredHeight();
        Log.e("bannerHeight",bannerHeight+"  "+ScreenUtils.dip2px(getContext(),16));
//        barHeight=hostTypeTabs.getMeasuredHeight();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
//        if (y > mTopViewHeight) {
//            y = mTopViewHeight;
//        }
        super.scrollTo(x, y);
    }
}
