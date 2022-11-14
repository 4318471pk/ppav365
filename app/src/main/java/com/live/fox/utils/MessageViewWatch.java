package com.live.fox.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ActionMenuView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.databinding.ControlPanelLivingBinding;
import com.live.fox.entity.FlowDataBean;
import com.live.fox.manager.SPManager;
import com.live.fox.ui.living.LivingActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import com.live.fox.ui.living.LivingControlPanel;
import com.live.fox.ui.living.LivingFragment;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.GradientTextView;
import com.live.fox.view.MyFlowLayout;
import com.live.fox.view.MyHoldKeyBackEditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessageViewWatch {

    Rect outRect = new Rect();
    LivingActivity activity;
    LivingControlPanel livingControlPanel;
    int windowDefaultVisibleHeight = 0;
    ControlPanelLivingBinding mBind;
    InputMethodManager imm;
    final String ViewBOT = "ViewBOT";
    int screenHeight = 0;
    int screenWidth = 0;
    int mBot = 0;
    List<FlowDataBean> mData = new ArrayList<>();
    ViewTreeObserver.OnGlobalLayoutListener listener;

    public void watchView(LivingControlPanel livingControlPanel, ControlPanelLivingBinding mbind) {
        this.livingControlPanel=livingControlPanel;
        this.activity = (LivingActivity) livingControlPanel.fragment.getActivity();
        this.mBind = mbind;
        screenHeight = ScreenUtils.getScreenHeightWithoutBtnsBar(activity);
        screenWidth = ScreenUtils.getScreenWidth(activity);
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mbind.etDiaMessage.setOnKeyBackListener(new MyHoldKeyBackEditText.OnKeyBackListener() {
            @Override
            public void onKeyBack() {
                hideInputLayout();
                hideKeyboard();
            }
        });
        mbind.floatingTextSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mbind.etDiaMessage.setHint(activity.getResources().getString(R.string.hint2));
                }
                else
                {
                    mbind.etDiaMessage.setHint(activity.getResources().getString(R.string.hint1));
                }
            }
        });

        listener=new ViewTreeObserver.OnGlobalLayoutListener() {
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
        };
        mbind.rlBotView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    public void onDestroy()
    {
        mBind.rlBotView.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
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

    public boolean isBotViewShow() {
        boolean condition1 = mBind.llInputLayout.getVisibility() == VISIBLE;
        boolean condition2 = mBind.llInputLayout.getLayoutParams().height
                > ScreenUtils.getDip2px(activity, 85);
        return condition1 && condition2 ;
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
        setScrollEnable(true);

    }

    public void setScrollEnable(boolean status) {
        activity.getViewPager().setUserInputEnabled(status);
    }

    private void setLayout(boolean reloadLayout, int height) {
        mBind.llTopView.setVisibility(GONE);
        mBind.flTempleLayout.setVisibility(GONE);
        mBind.llMessageTabs.setVisibility(View.INVISIBLE);
        setMessageListAndTabs();//加载快捷消息列表
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

        mBind.getRoot().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMessageListAndTab();
            }
        },400);

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

    private void setMessageListAndTabs()
    {
        while (mBind.llMessageTabs.getChildCount()>1)
        {
            mBind.llMessageTabs.removeViewAt(1);
        }
        if(mData==null || mData.size()==0)
        {
            String messageStr=SPUtils.getInstance().getString(ConstantValue.BulletMessageList,"");
            if(!TextUtils.isEmpty(messageStr))
            {
                try {
                    JSONArray jsonArray=new JSONArray(messageStr);
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        mData.add(new FlowDataBean(jsonArray.optString(i)));
                        mBind.flTempleLayout.setTextList(mData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showMessageListAndTab()
    {
        if(mData!=null && mData.size()>0)
        {
            while (mBind.llMessageTabs.getChildCount()>1)
            {
                mBind.llMessageTabs.removeViewAt(1);
            }

            int size=new Random().nextInt(2)+1;
            int index[]=randomArray(0,mData.size()-1,size);
            for (int i = 0; i < size; i++) {
                addMessageTab(mData.get(index[i]).getItemText());
            }

            mBind.flTempleLayout.setOnClickItemListener(new MyFlowLayout.OnClickItemListener() {
                @Override
                public void onItemClick(View v, String text, int pos) {
                    livingControlPanel.sendMessage(text);
                }
            });
            mBind.llMessageTabs.setVisibility(VISIBLE);
            mBind.flTempleLayout.setVisibility(VISIBLE);
        }

    }

    private void addMessageTab(final String message)
    {
        int dip5=ScreenUtils.getDip2px(activity,5);
        int dip12=ScreenUtils.getDip2px(activity,12);
        GradientTextView gradientTextView=new GradientTextView(activity);
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,dip5*6);
        ll.leftMargin=dip5;
        gradientTextView.setLayoutParams(ll);
        gradientTextView.setPadding(dip12,0,dip12,0);
        gradientTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        gradientTextView.setText(message);
        gradientTextView.setGravity(Gravity.CENTER);
        gradientTextView.setSolidBackground(0xffF4F1F8,dip5*3);
        mBind.llMessageTabs.addView(gradientTextView);
        gradientTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                livingControlPanel.sendMessage(message);
            }
        });

    }

    public static int[] randomArray(int min,int max,int n){
        int len = max-min+1;

        if(max < min || n > len){
            return null;
        }

        //初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min+len; i++){
            source[i-min] = i;
        }

        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            //待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            //将随机到的数放入结果集
            result[i] = source[index];
            //将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }
}
