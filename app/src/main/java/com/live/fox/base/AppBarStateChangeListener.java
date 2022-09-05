package com.live.fox.base;

import com.google.android.material.appbar.AppBarLayout;

//AppBarState的状态监听 仿KingTV 个人中心界面有用到
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public enum AppBarState {
        EXPANDED,
        COLLAPSED,
        IDLE
    }
 
    private AppBarState mCurrentState = AppBarState.IDLE;
 
    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != AppBarState.EXPANDED) {
                onStateChanged(appBarLayout, AppBarState.EXPANDED);
            }
            mCurrentState = AppBarState.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != AppBarState.COLLAPSED) {
                onStateChanged(appBarLayout, AppBarState.COLLAPSED);
            }
            mCurrentState = AppBarState.COLLAPSED;
        } else {
            if (mCurrentState != AppBarState.IDLE) {
                onStateChanged(appBarLayout, AppBarState.IDLE);
            }
            mCurrentState = AppBarState.IDLE;
        }
    }
 
    public abstract void onStateChanged(AppBarLayout appBarLayout, AppBarState state);
}