package com.live.fox.windowmanager;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.CommonApp;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.device.DeviceUtils;

/**
 * 悬浮view
 */
public class FFloatView extends FrameLayout {

    private Context context;

    public FFloatView(Context context) {
        super(context);
        this.context = context;
    }

    private RelativeLayout mContentView;
    private final ViewStoreHelper mViewStoreHelper = new ViewStoreHelper();

    private WindowManager.LayoutParams mWindowParams;

    /**
     * 返回设置的内容view
     *
     * @return
     */
    public final View getContentView() {
        return mContentView;
    }

    /**
     * 设置内容view给当前悬浮view
     *
     * @param layoutId 内容view布局id
     */
//    public final void setContentView(int layoutId) {
//        final View view = LayoutInflater.from(getContext()).inflate(layoutId, this, false);
//
//        setContentView(view);
//    }

    /**
     * 设置内容view给当前悬浮view
     *
     * @param view
     */
    public final void setContentView(RelativeLayout view) {

        final View old = getContentView();

        if (old == view)
            return;

        if (old != null) {
            if (old.getParent() == this)
                removeView(old);
        }
        RelativeLayout.LayoutParams lp3 = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (Constant.isPK) {
            lp3.width = DeviceUtils.getScreenWidth(context) / 2;
            lp3.height = DeviceUtils.getScreenHeight(context) / 4;
        } else {
            lp3.width = DeviceUtils.getScreenWidth(context) / 3;
            lp3.height = DeviceUtils.getScreenHeight(context) / 3;
        }
        view.setLayoutParams(lp3);


        mContentView = view;
        mViewStoreHelper.save(view);
        synchronizeContentViewSizeToFloatView();
    }

    /**
     * 调整view大小
     */
    public final void adjustView(boolean isPK) {
        LayoutParams lp3 = (LayoutParams) mContentView.getLayoutParams();
        if (isPK) {
            lp3.width = DeviceUtils.getScreenWidth(context) / 2;
            lp3.height = DeviceUtils.getScreenHeight(context) / 4;
        } else {
            lp3.width = DeviceUtils.getScreenWidth(context) / 3;
            lp3.height = DeviceUtils.getScreenHeight(context) / 3;
        }
        mContentView.setLayoutParams(lp3);
        mViewStoreHelper.save(mContentView);
        synchronizeContentViewSizeToFloatView();
    }

    /**
     * 把内容view的大小同步到悬浮view
     */
    public final void synchronizeContentViewSizeToFloatView() {
        final View view = getContentView();
        if (view == null)
            return;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null)
            return;

        final WindowManager.LayoutParams windowParams = getWindowParams();
        windowParams.width = params.width;
        windowParams.height = params.height;
        updateFloatViewLayout();
    }

    /**
     * 还原内容view到原父容器
     * <p>
     * 如果还原成功，当前悬浮view会被移除，并且内容view会被置为null
     */
    public final void restoreContentView() {
        mViewStoreHelper.restore(context);
    }

    /**
     * 还原内容View到某个容器
     *
     * @param viewGroup
     */
    public final void restoreContentViewTo(ViewGroup viewGroup) {
        mViewStoreHelper.restoreTo(viewGroup, context);
    }

    /**
     * 返回WindowManager的LayoutParams
     *
     * @return
     */
    public final WindowManager.LayoutParams getWindowParams() {
        if (mWindowParams == null)
            mWindowParams = FWindowManager.newLayoutParams();
        return mWindowParams;
    }


    /**
     * 更新悬浮view布局
     */
    public final void updateFloatViewLayout() {
        FWindowManager.getInstance().updateViewLayout(this, getWindowParams());
    }

    private Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable addRunnable = new Runnable() {
        @Override
        public void run() {
            LogUtils.e("addToWindow:addRunnable");
            Constant.isOpenWindow = true;
            addContentViewToFloatViewIfNeed();
            FWindowManager.getInstance().addView(FFloatView.this, getWindowParams());
        }
    };

    private final Runnable removeRunnable = new Runnable() {
        @Override
        public void run() {
            Constant.isShowWindow = false;
            LogUtils.e("addToWindow:removeRunnable");
            FWindowManager.getInstance().removeView(FFloatView.this);
            removeActivity = null;
        }
    };
    private Activity removeActivity;

    /**
     * 把悬浮view添加到window或者移除
     *
     * @param add
     */
    public final void addToWindow(boolean add, Activity activity) {
        if (add) {
            if (removeActivity == ActivityUtils.getTopActivity()) {
                removeActivity = null;
            }
        } else {
            removeActivity = activity;
        }
        addToWindow(add);
    }

    /**
     * 把悬浮view添加到window或者移除
     *
     * @param add
     */
    private void addToWindow(boolean add) {
        try {
            LogUtils.e("addToWindow:" + add);
            if (add) {
                handler.removeCallbacks(removeRunnable);
                handler.post(addRunnable);
            } else if (removeActivity == null || (ActivityUtils.getTopActivity() != null && ActivityUtils.getTopActivity().getClass() == removeActivity.getClass())) {
                handler.postDelayed(removeRunnable, 500);
                removeActivity = null;
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.getMessage();
        }
    }

    /**
     * 把内容view添加到当前悬浮view
     */
    private void addContentViewToFloatViewIfNeed() {
        final View view = getContentView();
        if (view == null)
            throw new NullPointerException("contentView is null");

        if (view.getParent() == this)
            return;

        ViewStoreHelper.removeViewFromParent(view);
        removeAllViews();
        addView(view);
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);

        if (mContentView == child)
            mContentView = null;

        if (getChildCount() <= 0)
            addToWindow(false);
    }

    private static boolean isAttached(View view) {
        if (view == null)
            return false;

        if (Build.VERSION.SDK_INT >= 19)
            return view.isAttachedToWindow();
        else
            return view.getWindowToken() != null;
    }

    //----------drag logic start----------

    /**
     * 是否可以拖动
     */
    private boolean mIsDraggable = true;

    /**
     * 设置是否可以拖动，默认可以拖动
     *
     * @param draggable
     */
    public final void setDraggable(boolean draggable) {
        mIsDraggable = draggable;
    }

    /**
     * 是否可以拖动
     *
     * @return
     */
    public final boolean isDraggable() {
        return mIsDraggable;
    }

    private final FTouchHelper mTouchHelper = new FTouchHelper();
    private boolean mInterceptTouchEvent;
    private boolean mProcessTouchEvent;

    private boolean ignoreTouchEvent() {
        if (getContentView() == null)
            return true;

        if (!mIsDraggable)
            return true;

        return !isAttached(this);
    }

    private boolean canDrag() {
        final int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        if (!mTouchHelper.saveDirection(touchSlop))
            return false;

        switch (mTouchHelper.getDirection()) {
            case MoveLeft:
                if (FTouchHelper.isScrollToRight(getContentView()))
                    return true;
                break;
            case MoveTop:
                if (FTouchHelper.isScrollToBottom(getContentView()))
                    return true;
                break;
            case MoveRight:
                if (FTouchHelper.isScrollToLeft(getContentView()))
                    return true;
                break;
            case MoveBottom:
                if (FTouchHelper.isScrollToTop(getContentView()))
                    return true;
                break;
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mTouchHelper.processTouchEvent(ev);
        if (ignoreTouchEvent())
            return false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (canDrag())
                    mInterceptTouchEvent = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mInterceptTouchEvent = false;
                mProcessTouchEvent = false;
                break;
        }
        return mInterceptTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTouchHelper.processTouchEvent(event);

        if (ignoreTouchEvent())
            return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mProcessTouchEvent) {
                    final int maxX = getResources().getDisplayMetrics().widthPixels - getWidth();
                    final int maxY = getResources().getDisplayMetrics().heightPixels - getHeight();

                    final int dx = FTouchHelper.getLegalDelta(getWindowParams().x, 0, maxX, (int) mTouchHelper.getDeltaX());
                    final int dy = FTouchHelper.getLegalDelta(getWindowParams().y, 0, maxY, (int) mTouchHelper.getDeltaY());

                    if (dx != 0 || dy != 0) {
                        getWindowParams().x += dx;
                        getWindowParams().y += dy;
                        updateFloatViewLayout();
                    }
                } else {
                    if (mInterceptTouchEvent || canDrag()) {
                        mProcessTouchEvent = true;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mInterceptTouchEvent = false;
                mProcessTouchEvent = false;
                break;
        }
        return mProcessTouchEvent;
    }

    //----------drag logic end----------
}
