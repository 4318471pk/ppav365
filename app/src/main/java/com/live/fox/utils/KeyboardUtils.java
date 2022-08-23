package com.live.fox.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;



import java.lang.reflect.Field;

/**
 * openSoftInput                   : 动态显示软键盘
 * hideSoftInput                   : 动态隐藏软键盘
 * toggleSoftInput                 : 切换键盘显示与否状态
 * isSoftInputVisible              : 判断软键盘是否可见
 * setrSoftInputChangedListener    : 设置软键盘改变监听器
 * fixSoftInputLeaks               : 修复软键盘内存泄漏
 * clickBlankArea2HideSoftInputInActivity : Activity点击屏幕空白区域隐藏软键盘 (copy里面的代码到Activity中，而不是调用此方法)
 * clickBlankArea2HideSoftInputInFragment: Fragment点击屏幕空白区域隐藏软键盘 (copy里面的代码到Activity和Fragment中，而不是调用此方法)
 * <p>
 * //软键盘的监听 获取软键盘的高度 软键盘是否已显示
 * KeyboardUtils.registerSoftInputChangedListener(this,
 * new KeyboardUtils.OnSoftInputChangedListener() {
 *
 * @Override public void onSoftInputChanged(int height) {
 * tvAboutKeyboard.setText(new SpanUtils()
 * .appendLine("isSoftInputVisible: " + KeyboardUtils.isSoftInputVisible(KeyboardActivity.this))
 * .append("height: " + height)
 * .create()
 * );
 * }
 * });
 */
public final class KeyboardUtils {

    private static int sContentViewInvisibleHeightPre;

    private KeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void hideForceSoftInput(final Activity activity) {
        if (isSoftInputVisible(activity)) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Show the soft input.
     *
     * @param activity The activity.
     */
    public static void openSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * Show the soft input.
     *
     * @param view The view.
     */
    public static void openSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * Show the soft input.
     *
     * @param view The view.
     */
    public static void openSoftInput(final View view, int delayed) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                openSoftInput(view);
            }
        }, delayed);
    }

    /**
     * Hide the soft input.
     *
     * @param activity The activity.
     */
    public static void hideSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Hide the soft input.
     *
     * @param view The view.
     */
    public static void hideSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Toggle the soft input display or not.
     */
    public static void toggleSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Return whether soft input is visible.
     * <p>The minimum height is 200</p>
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSoftInputVisible(final Activity activity) {
        return isSoftInputVisible(activity, 200);
    }

    /**
     * Return whether soft input is visible.
     *
     * @param activity             The activity.
     * @param minHeightOfSoftInput The minimum height of soft input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSoftInputVisible(final Activity activity,
                                             final int minHeightOfSoftInput) {
        return getContentViewInvisibleHeight(activity) >= minHeightOfSoftInput;
    }

    private static int getContentViewInvisibleHeight(final Activity activity) {
        final View contentView = activity.findViewById(android.R.id.content);
        Rect outRect = new Rect();
        contentView.getWindowVisibleDisplayFrame(outRect);
        return contentView.getBottom() - outRect.bottom;
    }

    /**
     * Register soft input changed listener.
     * 注：
     * 1.如果Dialog中需要此事件，不建议在Dialog中写此监听，而是在Fragment或者Activity中写监听，然后调用Dialog中的方法改变值
     * 2.如果Activity中有多个Fragment,且多个Fragment都实现了这个监听，那么就会在切换Fragment的过程中,此监听失效,此时建议卸载Activit中，有Activit分发
     *
     * @param activity The activity.
     * @param listener The soft input changed listener.
     */
    public static void setSoftInputChangedListener(final Activity activity,
                                                   final OnSoftInputChangedListener listener) {
        final View contentView = activity.findViewById(android.R.id.content);
        sContentViewInvisibleHeightPre = getContentViewInvisibleHeight(activity);
        final int diff = DensityUtils.dp2px(activity, 50);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (listener != null) {
                    int height = getContentViewInvisibleHeight(activity);
                    if (sContentViewInvisibleHeightPre != height) {
                        boolean isOpened = height > diff;
                        listener.onSoftInputChanged(isOpened, height);
                        sContentViewInvisibleHeightPre = height;
                    }
                }
            }
        });
    }

    /**
     * Fix the leaks of soft input.
     * <p>Call the function in {@link Activity#onDestroy()}.</p>
     *
     * @param context The context.
     */
    public static void fixSoftInputLeaks(final Context context) {
        if (context == null) return;
        InputMethodManager imm =
                (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        String[] strArr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        for (int i = 0; i < 3; i++) {
            try {
                Field declaredField = imm.getClass().getDeclaredField(strArr[i]);
                if (declaredField == null) continue;
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                Object obj = declaredField.get(imm);
                if (obj == null || !(obj instanceof View)) continue;
                View view = (View) obj;
                if (view.getContext() == context) {
                    declaredField.set(imm, null);
                } else {
                    return;
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /**
     * Click blankj area to hide soft input.
     * <p>Copy the following code in ur activity.</p>
     */
    public static void clickBlankArea2HideSoftInputInActivity() {
        Log.i("KeyboardUtils", "Please refer to the following code.");
        /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS
                    );
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        // Return whether touch the view.
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }
        */
    }

    /**
     * Click blankj area to hide soft input.
     * <p>Copy the following code in ur activity and fragment.</p>
     */
    public static void clickBlankArea2HideSoftInputInFragment() {
        Log.i("KeyboardUtils", "Please refer to the following code.");

        /*
        //Activity中的代码
        private OnHideKeyboardListener onHideKeyboardListener;
        public interface OnHideKeyboardListener{
            public boolean hideKeyboard();
        }

        public void setOnHideKeyboardListener(OnHideKeyboardListener onHideKeyboardListener){
            this.onHideKeyboardListener = onHideKeyboardListener;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {

            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {
                    onHideKeyboardListener.hideKeyboard();
                }
                return super.dispatchTouchEvent(ev);
            }
            // 必不可少，否则所有的组件都不会有TouchEvent了
            return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
        }

        public boolean isShouldHideInput(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] leftTop = {0, 0};
                //获取输入框当前的location位置
                v.getLocationInWindow(leftTop);
                int left = leftTop[0];
                int top = leftTop[1];
                int bottom = top + v.getHeight();
                int right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }

        //Fragment中的代码
        //软键盘的处理,,点击EditText外的地方，软键盘自动隐藏
        public void onAttach(Activity activity) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            FragmentContentActivity.OnHideKeyboardListener onHideKeyboardListener = new FragmentContentActivity.OnHideKeyboardListener() {
                @Override
                public boolean hideKeyboard() {
                    if(imm.isActive(etSearch)){
                        getView().requestFocus();
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        return true;
                    }
                    return false;
                }
            };
            ((FragmentContentActivity)getActivity()).setOnHideKeyboardListener(onHideKeyboardListener);
            super.onAttach(activity);
        }
        */
    }

    public interface OnSoftInputChangedListener {
        void onSoftInputChanged(boolean isOpen, int height);
    }
}
