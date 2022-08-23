package com.live.fox.dialog.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.live.fox.utils.ClickUtil;


/**
 * Created  cxf on 2018/9/22.
 */

public abstract class BaseViewHolder {

    protected Context mContext;
    protected ViewGroup mParentView;
    protected View mContentView;
//    protected LifeCycleListener mLifeCycleListener;

    public BaseViewHolder(Context context, ViewGroup parentView) {
        mContext = context;
        mParentView = parentView;
        mContentView = LayoutInflater.from(context).inflate(getLayoutId(), mParentView, false);
        init();
    }

    public BaseViewHolder(Context context, ViewGroup parentView, Object... args) {
        processArguments(args);
        mContext = context;
        mParentView = parentView;
        mContentView = LayoutInflater.from(context).inflate(getLayoutId(), mParentView, false);
        init();
    }

    protected void processArguments(Object... args) {

    }

    protected abstract int getLayoutId();

    public abstract void init();

    protected View findViewById(int res) {
        return mContentView.findViewById(res);
    }

    public View getContentView() {
        return mContentView;
    }

    protected boolean canClick() {
        return ClickUtil.isFastDoubleClick();
    }

//    public LifeCycleListener getLifeCycleListener() {
//        return mLifeCycleListener;
//    }

    public void addToParent() {
        if (mParentView != null && mContentView != null) {
            mParentView.addView(mContentView);
        }
    }

    public void removeFromParent() {
        ViewParent parent = mContentView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mContentView);
        }
    }

}
