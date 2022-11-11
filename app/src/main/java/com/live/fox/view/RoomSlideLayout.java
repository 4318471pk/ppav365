package com.live.fox.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.customview.widget.ViewDragHelper;

import com.live.fox.R;
import com.live.fox.entity.Anchor;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;

import java.util.ArrayList;


/**
 * User:
 * Date:
 */
public class RoomSlideLayout extends RelativeLayout {

    public static final int STATE_IDLE = ViewDragHelper.STATE_IDLE;

    public static final int STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING;

    public static final int STATE_SETTLING = ViewDragHelper.STATE_SETTLING;

    private static final int MIN_DISTANCE_FOR_FLING = 25; // dips

    private int mDragState;

    private boolean isRestoring;//是否正在还原

    //当前正在追踪的手指id
    private int mScrollPointerId;

    //手指按下时的位置
    private float mInitialTouchX, mInitialTouchY;
    //上次滑动的位置
    private float mLastTouchX, mLastTouchY;

    //最小滚动距离
    private int mTouchSlop;
    //最小最大滑动速度
    private int mMinVelocity, mMaxVelocity;

    //滑动速度检测
    private VelocityTracker mVelocityTracker;

    private int mFlingDistance;

    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    private View mainLayout;
    private View videoLayout;

    private int mWidth, mHeight;

    private ValueAnimator animator;

    private float mLastAnimValue;

    private int anchorIndex;

    private ArrayList<Anchor> anchorList;

    private ImageView sdNextAnchor;

    public RoomSlideLayout(Context context) {
        this(context, null);
    }

    public RoomSlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoomSlideLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final float density = context.getResources().getDisplayMetrics().density;
        final ViewConfiguration configuration = ViewConfiguration.get(context);

        mTouchSlop = configuration.getScaledPagingTouchSlop();
        mVelocityTracker = VelocityTracker.obtain();
        mMinVelocity = (int) (configuration.getScaledMinimumFlingVelocity() * density * 4);
        mMaxVelocity = configuration.getScaledMaximumFlingVelocity();

        mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainLayout = findViewById(R.id.main_container);
                videoLayout = findViewById(R.id.video_container);
                sdNextAnchor = findViewById(R.id.sd_next_anchor_cover);
            }
        });

        animator = new ValueAnimator();
        animator.setInterpolator(sInterpolator);
        animator.addUpdateListener(animation -> {
            float curValue = (float) animation.getAnimatedValue();
            float offset = curValue - mLastAnimValue;
            mLastAnimValue = curValue;
            onScroll(offset);
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mDragState == STATE_SETTLING)
                    setDragState(STATE_IDLE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                setDragState(STATE_SETTLING);
            }
        });
    }




    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (!canTouch()) return false;
        mVelocityTracker.addMovement(e);
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                int actionIndex = e.getActionIndex();
                mScrollPointerId = e.getPointerId(actionIndex);//记录当前手指的索引值
                mInitialTouchX = mLastTouchX = e.getX();
                mInitialTouchY = mLastTouchY = e.getY();
                if (mDragState == STATE_SETTLING) {
                    setDragState(STATE_DRAGGING);
                }
                isRestoring = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final int pointerIndex = e.findPointerIndex(mScrollPointerId);
                if (pointerIndex < 0) return false;
                float currX = e.getX(pointerIndex);
                float currY = e.getY(pointerIndex);
                float dy = currY - mLastTouchY;
                //此事件一旦开始拦截，onInterceptTouchEvent就收不到后续事件,后续事件皆会交由onTouchEvent处理
                float yDiff = Math.abs(dy);

                if (canScrollVertically(this, false, (int) dy, (int) currX, (int) currY)) {
                    mLastTouchX = currX;
                    mLastTouchY = currY;
                    return false;
                }
                if (yDiff >= mTouchSlop) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    setDragState(STATE_DRAGGING);
                    mInitialTouchX = mLastTouchX = currX;
                    mInitialTouchY = mLastTouchY = currY;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //手指抬起时不能拦截，否则子View将收不到onClick事件
//                if (mVelocityTracker != null) {
//                    mVelocityTracker.recycle();
////                    mVelocityTracker = null;
//                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                onPointDown(e);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onPointerUp(e);
                break;
        }
        return mDragState == STATE_DRAGGING;
    }

    /**
     * 如果在onInterceptTouchEvent未拦截任何事件，onTouchEvent方法依然执行，
     * 说明子View的onTouchEvent方法返回false，onTouch事件交由父View处理
     * 注:当子View的clickable属性为false时，即不处理onTouch事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isRestoring) return false;
        if (!canTouch()) return false;
        int action = ev.getActionMasked();
        boolean eventAddedToVelocityTracker = false;
        /**
         * pointer代表每个手指的触摸点,每个pointer都会有一个自己的id和index,
         * pointer的id在整个事件流中是不会发生变化的，但是index会发生变化,
         * 可以通过id获取index, findPointerIndex(pointerId),
         * 亦可以通过index获取id, getPointerId(pointerIndex);
         *
         * getAction获得的int值是由pointer的index值(前两个字节)和事件类型(后两个字节)值组合而成的;
         * getActionIndex()获得pointer的index;
         * getActionMasked()获得事件类型;
         */
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int actionIndex = ev.getActionIndex();
                mScrollPointerId = ev.getPointerId(actionIndex);//记录当前手指的索引值
                mInitialTouchX = mLastTouchX = ev.getX();
                mInitialTouchY = mLastTouchY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                int pointerIndex = ev.findPointerIndex(mScrollPointerId);
                if (pointerIndex < 0) return false;
                float currX = ev.getX(pointerIndex);//当前Y轴上的位置
                float currY = ev.getY(pointerIndex);//当前Y轴上的位置

                float yDistance = currY - mInitialTouchY;
                if (mDragState == STATE_DRAGGING) {
                    float offset = currY - mLastTouchY;
                    onScroll(offset);
                } else {
                    if (Math.abs(yDistance) > mTouchSlop) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        setDragState(STATE_DRAGGING);
                        mInitialTouchX = mLastTouchX = currX;
                        mInitialTouchY = mLastTouchY = currY;
                    }
                }
                mLastTouchX = currX;
                mLastTouchY = currY;
                break;

            case MotionEvent.ACTION_UP:
                mVelocityTracker.addMovement(ev);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                final float velocityY = mVelocityTracker.getYVelocity(mScrollPointerId);
                mVelocityTracker.clear();

                pointerIndex = ev.findPointerIndex(mScrollPointerId);
                if (pointerIndex < 0) return false;
                currY = ev.getY(pointerIndex);//当前Y轴上的位置
                yDistance = currY - mInitialTouchY;

                handleVertically(yDistance, velocityY);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                onPointDown(ev);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onPointerUp(ev);
                break;
        }
        if (!eventAddedToVelocityTracker) {
            mVelocityTracker.addMovement(ev);
        }
        return true;
    }

    //处理纵向滑动
    private void handleVertically(float yDistance, float velocityY) {
        float toY;
        int duration;
        float fromY = mainLayout.getTranslationY();
        if (Math.abs(velocityY) > mMinVelocity && Math.abs(yDistance) > mFlingDistance) {
            if (velocityY > 0) {
                toY = fromY > 0 ? mHeight : 0;
            } else {
                toY = fromY < 0 ? -mHeight : 0;
            }
            duration = 600 - (int) (Math.abs(velocityY) / mMaxVelocity * 300);
        } else {
            if (yDistance < 0) { //手指上滑
                if (fromY >= mHeight / 5 * 2) {
                    toY = mHeight;
                } else if (fromY < -mHeight / 5 * 3) {
                    toY = -mHeight;
                } else {
                    toY = 0;
                }
            } else { //手指下滑
                if (fromY >= mHeight / 5 * 3) {
                    toY = mHeight;
                } else if (fromY < -mHeight / 5 * 2) {
                    toY = -mHeight;
                } else {
                    toY = 0;
                }
            }
            duration = 300 + (int) (Math.abs(toY - fromY) / mHeight * 200);
        }
        startScroll(fromY, toY, duration);
    }


    //开始自动滚动
    private void startScroll(float from, float to, int duration) {
        if (from == to) {
            setDragState(STATE_IDLE);
            return;
        }
        mLastAnimValue = from;
        animator.setFloatValues(from, to);
        animator.setDuration(duration);
        animator.start();
    }

    //停止自动滑动
    private void animCancel() {
        final Animator animator = this.animator;
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    private void onScroll(float offset) {
        LayoutParams layoutParams = (LayoutParams) sdNextAnchor.getLayoutParams();
        float oldTranslationY = mainLayout.getTranslationY();
        float newTranslationY = oldTranslationY + offset;
        if (newTranslationY > mHeight) {
            newTranslationY = mHeight;
        } else if (newTranslationY < -mHeight) {
            newTranslationY = -mHeight;
        }
        layoutParams.height = (int) Math.abs(newTranslationY);
        int index = anchorIndex;
        LogUtils.e("index1: " + index + ", " + anchorList.size());
        if (newTranslationY > 0) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            if (--index < 0) {
                LogUtils.e("index2: " + index);
                index = anchorList.size() - 1;
            }
        } else if (newTranslationY < 0) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            if (++index >= anchorList.size()) {
                index = 0;
            }
        }
//        LogUtils.e("index3: "+index);

        sdNextAnchor.setLayoutParams(layoutParams);
        String bigPic = replaceDomain(anchorList.get(index).getAvatar()).toString();
        if (!bigPic.equals(sdNextAnchor.getContentDescription())) {
            sdNextAnchor.setContentDescription(bigPic);
//            GlideUtils.loadImage(getContext(), bigPic, sdNextAnchor);
        }
        if (Math.abs(oldTranslationY) != mHeight) {
            mainLayout.setTranslationY(newTranslationY);
            videoLayout.setTranslationY(newTranslationY);
        }
    }

    //替换短视频 社区 图片、视频的域名
    public Object replaceDomain(Object path) {
        if (path == null)
            return "";
        if (path instanceof Integer) {
            return path;
        }

        String url = "";
        try {
            url = path.toString();
        } catch (Exception e) {
            return "";
        }

        if (StringUtils.isEmpty(url))
            return "";

        String domain = SPUtils.getInstance("domain").getString("domain", "");
        if (StringUtils.isEmpty(domain)) {
            domain = "www.baudu.com";
        }

        Uri mUri = Uri.parse(url);

        String startStr = url.startsWith("http://") ? "http://" : "https://";
        String endStr = url.split("/")[url.split("/").length - 1];


        url = url.replace(mUri.getScheme() + "://" + mUri.getAuthority(), startStr + domain);
//        LogUtils.e("替换url:"+url);
        return url;
    }

    //当有手指抬起时调用
    private void onPointerUp(MotionEvent e) {
        final int actionIndex = MotionEventCompat.getActionIndex(e);
        if (e.getPointerId(actionIndex) == mScrollPointerId) {
            // Pick a new pointer to pick up the slack.
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mScrollPointerId = e.getPointerId(newIndex);
            //调整触摸位置，防止出现跳动
            mInitialTouchX = mLastTouchX = e.getX(newIndex);
            mInitialTouchY = mLastTouchY = e.getY(newIndex);
        }
    }

    //当有手指按下时调用
    private void onPointDown(MotionEvent e) {
        final int actionIndex = MotionEventCompat.getActionIndex(e);
        mScrollPointerId = e.getPointerId(actionIndex);
        //调整触摸位置，防止出现跳动
        mInitialTouchX = mLastTouchX = e.getX(actionIndex);
        mInitialTouchY = mLastTouchY = e.getY(actionIndex);
    }


    private boolean canScrollVertically(View v, boolean checkV, int dy, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                // This will not work for transformed views in Honeycomb+
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight()
                        && y + scrollY >= child.getTop() && y + scrollY < child.getBottom()
                        && canScrollVertically(child, true, dy, x + scrollX - child.getLeft(),
                        y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }
        return checkV && v.canScrollVertically(-dy);
    }

    public void setAnchorIndex(int anchorIndex) {
        this.anchorIndex = anchorIndex;
    }

    public OnSlideListener onSlideListener;

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }

    public interface OnSlideListener {
        void switchAnchor(int index);
    }

    public void setAnchorList(ArrayList<Anchor> anchorList) {
        this.anchorList = anchorList;

//        WindowManager manager = activity.getWindowManager();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(outMetrics);
//        this.pwidth = outMetrics.widthPixels;
//        this.pheight = outMetrics.heightPixels;
    }

    void setDragState(int state) {
        if (mDragState == state) return;
        mDragState = state;
        if (mDragState == STATE_IDLE) {
            float translationY = mainLayout.getTranslationY();
            if (Math.abs(translationY) == mHeight) {
                anchorIndex += translationY > 0 ? -1 : 1;
                if (anchorIndex < 0) {
                    anchorIndex = anchorList.size() - 1;
                } else if (anchorIndex >= anchorList.size()) {
                    anchorIndex = 0;
                }
                if (onSlideListener != null) {
                    onSlideListener.switchAnchor(anchorIndex);
                }
                mainLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainLayout.setTranslationY(0);
                        videoLayout.setTranslationY(0);
                    }
                }, 500);
            }
        } else if (mDragState == STATE_DRAGGING) {
            sdNextAnchor.setVisibility(View.VISIBLE);
            animCancel();
        }
    }


    public boolean canTouch() {
        //不超过2个主播，不让滑动
        if (anchorList == null || anchorList.size() <= 1) return false;
        //禁止连续滑动
        if (Math.abs(mainLayout.getTranslationY()) >= mHeight && mDragState == STATE_IDLE)
            return false;
        return true;
    }

    //恢复到原始状态，被邀请上麦时调用
    public void restore() {
        if (isRestoring) return;
        if (mDragState == STATE_IDLE) return;
        animCancel();
        isRestoring = true;
        startScroll(mainLayout.getTranslationY(), 0, 200);
    }
}
