package com.live.fox.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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

public class ViewWatch {

    Rect outRect = new Rect();
    LivingActivity activity;
    int windowDefaultVisibleHeight = 0;
    boolean isKeyboardShow = false;
    ControlPanelLivingBinding mBind;
    InputMethodManager imm;
    final String ViewBOT="ViewBOT";

    public void watchView(LivingActivity activity, ControlPanelLivingBinding mbind) {
        this.activity = activity;
        this.mBind = mbind;
        imm=(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mbind.rlMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (windowDefaultVisibleHeight == 0) {
                    windowDefaultVisibleHeight = getWindowVisibleHeight();
                }
                Log.e("windowDefaultV", windowDefaultVisibleHeight + " " + getWindowVisibleHeight());
                if (isKeyboardShow() && !isKeyboardShow) {
                    isKeyboardShow = true;
                    int bot = getBottomMargin();
                    SPUtils.getInstance().put(ViewBOT,bot);
                    Log.e("windowDefaultV22", bot+" "+isNavigationBarShow(activity.getWindowManager()));
                    mbind.rlMessagesPanel.getLayoutParams().height=bot;
//                    RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) mbind.rlBotView.getLayoutParams();
//                    rl.topMargin=rl.topMargin-bot;
//                    mbind.rlBotView.setLayoutParams(rl);
                    mbind.rlMain.requestLayout();
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
            return getKeyboardHeight() ;
        }
        return 0;
    }

    public void showKeyboard()
    {
        imm.showSoftInput(mBind.etDiaMessage,0);
    }

    public void hideKeyboard()
    {
        imm.hideSoftInputFromWindow(mBind.etDiaMessage.getWindowToken(),0);
    }

    public void hideInputLayout()
    {
        hideKeyboard();
        mBind.llInputLayout.setVisibility(GONE);
        mBind.rlButtons.setVisibility(VISIBLE);
        activity.getViewPager().setUserInputEnabled(true);
    }

    public void showInputLayout()
    {
        int bot=SPUtils.getInstance().getInt(ViewBOT);
        mBind.rlMessagesPanel.getLayoutParams().height=bot;
//        RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) mBind.rlBotView.getLayoutParams();
//        rl.topMargin=rl.topMargin-bot;
//        mBind.rlBotView.setLayoutParams(rl);
        mBind.llInputLayout.setVisibility(VISIBLE);
        mBind.rlButtons.setVisibility(GONE);
        mBind.etDiaMessage.requestFocus();

        showKeyboard();
        activity.getViewPager().setUserInputEnabled(false);
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
