package com.live.fox.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.live.fox.databinding.ControlPanelLivingBinding;
import com.live.fox.manager.SPManager;
import com.live.fox.ui.living.LivingActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import com.live.fox.utils.device.ScreenUtils;

public class ViewWatch {

    Rect outRect = new Rect();
    LivingActivity activity;
    int windowDefaultVisibleHeight = 0;
    ControlPanelLivingBinding mBind;
    InputMethodManager imm;
    final String ViewBOT = "ViewBOT";
    int screenHeight = 0;
    int screenWidth = 0;
    int mBot = 0;

    public void watchView(LivingActivity activity, ControlPanelLivingBinding mbind) {
        this.activity = activity;
        this.mBind = mbind;
        screenHeight = ScreenUtils.getScreenHeightWithoutBtnsBar(activity);
        screenWidth = ScreenUtils.getScreenWidth(activity);
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mbind.llInputLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    if (isMessagesPanelOpen()) {

                    }
                }
                return false;
            }
        });
        mbind.rlBotView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (windowDefaultVisibleHeight == 0) {
                    windowDefaultVisibleHeight = getWindowVisibleHeight();
                }
                Log.e("windowDefaultV", windowDefaultVisibleHeight + " " + getWindowVisibleHeight());
                if (isKeyboardShow()) {
                    int bot = getBottomMargin();
                    if (mBot != bot) {
                        mBot=bot;
                        SPUtils.getInstance().put(ViewBOT, bot);
                        Log.e("windowDefaultV22", bot + " " + isNavigationBarShow(activity.getWindowManager()));
//                    mbind.rlMessagesPanel.getLayoutParams().height=bot;
//                    mbind.llInputLayout.getLayoutParams().height=bot+ScreenUtils.dp2px(activity,85);
//                    mbind.rlMain.requestLayout();
                        setLayout(true, bot);
                        showKeyboard();
                        mbind.etDiaMessage.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mbind.etDiaMessage.requestFocus();
                            }
                        },500);

                        setScrollEnable(false);
                    }

                }
            }
        });
    }


    // 键盘的高度
    private int getKeyboardHeight() {
        return windowDefaultVisibleHeight - getWindowVisibleHeight();
    }

    // 键盘是否弹起
    public Boolean isKeyboardShow() {
        return windowDefaultVisibleHeight - getWindowVisibleHeight() > 0;
    }

    private int getWindowVisibleHeight() {
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        return outRect.bottom;
    }

    // 获取输入框距离屏幕底部的距离
    private int getBottomMargin() {
        if (isKeyboardShow()) {
            return getKeyboardHeight();
        }
        return 0;
    }

    public void showKeyboard() {
        mBind.etDiaMessage.requestFocus();
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        activity.setWindowsFlag();
        imm.showSoftInput(mBind.etDiaMessage, 0);
        mBind.etDiaMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBind.etDiaMessage.requestFocus();
            }
        },500);
    }

    public void hideKeyboard() {
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
//        activity.setWindowsFlag();
        mBind.etDiaMessage.clearFocus();
        activity.getViewPager().requestLayout();
        imm.hideSoftInputFromWindow(mBind.etDiaMessage.getWindowToken(), 0);
    }

    public boolean isMessagesPanelOpen() {
        boolean condition1 = mBind.llInputLayout.getVisibility() == VISIBLE;
        boolean condition2 = mBind.llInputLayout.getLayoutParams().height
                > ScreenUtils.getDip2px(activity, 85);
        return condition1 && condition2 && !isKeyboardShow();
    }

    public void hideInputLayout() {
        mBind.llTopView.setVisibility(VISIBLE);
        RelativeLayout.LayoutParams rlMessages = (RelativeLayout.LayoutParams) mBind.llMessages.getLayoutParams();
        rlMessages.height = (int) (screenHeight * 0.5f) - com.live.fox.utils.device.ScreenUtils.getDip2px(activity, 45);
        rlMessages.width = (int) (screenWidth * 0.7f);
        rlMessages.bottomMargin = ScreenUtils.getDip2px(activity, 45);
        rlMessages.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mBind.llMessages.setLayoutParams(rlMessages);

        mBind.llMessages.setLayoutParams(rlMessages);
        mBind.llInputLayout.setVisibility(GONE);
        mBind.rlButtons.setVisibility(VISIBLE);
    }

    public void setScrollEnable(boolean status) {
        activity.getViewPager().setUserInputEnabled(status);
    }

    private void setLayout(boolean reloadLayout, int height) {
        mBind.llTopView.setVisibility(GONE);
        RelativeLayout.LayoutParams rlMessages = (RelativeLayout.LayoutParams) mBind.llMessages.getLayoutParams();
        rlMessages.height = (int) (screenHeight * 0.5f) - com.live.fox.utils.device.ScreenUtils.getDip2px(activity, 45);
        rlMessages.width = (int) (screenWidth * 0.7f);
        rlMessages.bottomMargin = height + ScreenUtils.getDip2px(activity, 90);
        rlMessages.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mBind.llMessages.setLayoutParams(rlMessages);

        mBind.rlMessagesPanel.getLayoutParams().height = height;
        mBind.rlMessagesPanel.getChildAt(0).getLayoutParams().height=height;
        mBind.llInputLayout.getLayoutParams().height = height + ScreenUtils.getDip2px(activity, 85);
        mBind.llInputLayout.setVisibility(VISIBLE);
        if (reloadLayout) {
            mBind.rlMessagesPanel.requestLayout();
            mBind.llInputLayout.requestLayout();
            mBind.rlMain.requestLayout();
        }
    }

    public void showInputLayout() {
        int bot = SPUtils.getInstance().getInt(ViewBOT);
//        if(bot>0)
//        {
//            setLayout(false,bot);
//        }
        mBot=0;
        mBind.rlButtons.setVisibility(GONE);

        showKeyboard();
        setScrollEnable(false);
    }

    private int isNavigationBarShow(WindowManager windowManager) {
        int statusHeight = 0;
        Display defaultDisplay = windowManager.getDefaultDisplay(); //获取屏幕高度
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(outMetrics);
        int heightPixels = outMetrics.heightPixels; //宽度
        int widthPixels = outMetrics.widthPixels; //获取内容高度
        DisplayMetrics outMetrics2 = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics2);
        int heightPixels2 = outMetrics2.heightPixels; //宽度
        int widthPixels2 = outMetrics2.widthPixels;

        if (heightPixels - heightPixels2 > 0 || widthPixels - widthPixels2 > 0) {
            try {
                Class clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                String heightStr = clazz.getField("navigation_bar_height").get(object).toString();
                int height = Integer.parseInt(heightStr); //dp--->px
                statusHeight = activity.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return statusHeight;
    }
}
