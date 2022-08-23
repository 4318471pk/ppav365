package com.live.fox.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;

import com.live.fox.utils.device.DeviceUtils;


/**
 * 系统的Toast 然后自定义其中的样式
 * <p>
 * 可以先在Application类中设定好样式 之后ToastViewUtils.showToast("")就是这样的样式
 * ToastViewUtils.setBgResource(R.drawable.shape_toastbg);
 * ToastViewUtils.setGravity(Gravity.CENTER, 0 , DeviceUtils.dp2px(this, 80));
 * ToastViewUtils.setMsgColor(Color.WHITE);
 * <p>
 * setGravity     : 设置吐司位置
 * setBgColor     : 设置背景颜色
 * setBgResource  : 设置背景资源
 * setMessageColor: 设置消息颜色
 * showShort      : 显示短时吐司
 * showLong       : 显示长时吐司
 * showCustomShort: 显示短时自定义吐司
 * showCustomLong : 显示长时自定义吐司
 * cancel         : 取消吐司显示      ToastUtils.cancel();
 * <p>
 * 1.支持在子线程中显示吐司 且可以取消
 * new Thread(new Runnable() {
 *
 * @Override public void run() {
 * ToastViewUtils.showShort(R.string.toast_short);
 * }
 * }).start();
 * <p>
 * 2.改变字体颜色显示吐司
 * ToastViewUtils.setMsgColor(Color.GREEN);
 * ToastViewUtils.showLong(R.string.toast_green_font);
 * <p>
 * 3.改变背景颜色
 * ToastViewUtils.setBgColor(ContextCompat.getColor(this, R.color.colorAccent));
 * ToastViewUtils.showLong(R.string.toast_bg_color);
 * <p>
 * 4.居中显示
 * ToastViewUtils.setGravity(Gravity.CENTER, 0, 0);
 * ToastViewUtils.showLong(R.string.toast_middle);
 * <p>
 * 5.显示带图片和文字的吐司
 * ToastViewUtils.showLong(new SpanUtils()
 * .appendImage(R.mipmap.ic_launcher, SpanUtils.ALIGN_CENTER)
 * .appendSpace(32)
 * .append(getString(R.string.toast_span)).setFontSize(24, true)
 * .create()
 * );
 */
public final class ToastViewUtils {

    private static final int COLOR_DEFAULT = 0xFEFFFFFF;
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static Toast sToast;
    private static int sGravity = -1;
    private static int sXOffset = -1;
    private static int sYOffset = -1;
    private static int sBgColor = COLOR_DEFAULT;
    private static int sBgResource = -1;
    private static int sMsgColor = COLOR_DEFAULT;

    private ToastViewUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Set the gravity.
     *
     * @param gravity The gravity.
     * @param xOffset X-axis offset, in pixel.
     * @param yOffset Y-axis offset, in pixel.
     */
    public static void setGravity(final int gravity, final int xOffset, final int yOffset) {
        sGravity = gravity;
        sXOffset = xOffset;
        sYOffset = yOffset;
    }

    /**
     * Set the color of background.
     *
     * @param backgroundColor The color of background.
     */
    public static void setBgColor(@ColorInt final int backgroundColor) {
        sBgColor = backgroundColor;
    }

    /**
     * Set the resource of background.
     *
     * @param bgResource The resource of background.
     */
    public static void setBgResource(@DrawableRes final int bgResource) {
        sBgResource = bgResource;
    }

    /**
     * Set the color of message.
     *
     * @param msgColor The color of message.
     */
    public static void setMsgColor(@ColorInt final int msgColor) {
        sMsgColor = msgColor;
    }

    /**
     * Show the toast for a short period of time.
     *
     * @param text The text.
     */
    public static void showShort(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }


    public static void showDefaultShort(@NonNull final CharSequence text) {
        showTopShort(text);
    }

    public static void showTopShort(@NonNull final CharSequence text) {
        setGravity(Gravity.TOP, 0, DeviceUtils.dp2px(Utils.getApp(), 80));
        show(text, Toast.LENGTH_SHORT);
    }

    public static void showCenterShort(@NonNull final CharSequence text) {
        setGravity(Gravity.CENTER, 0, DeviceUtils.dp2px(Utils.getApp(), 0));
        show(text, Toast.LENGTH_SHORT);
    }

    public static void showBottomShort(@NonNull final CharSequence text) {
        setGravity(Gravity.BOTTOM, 0, DeviceUtils.dp2px(Utils.getApp(), 80));
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * Show the toast for a short period of time.
     *
     * @param resId The resource id for text.
     */
    public static void showShort(@StringRes final int resId) {
        showDefaultShort(resId);
    }

    public static void showDefaultShort(@StringRes final int resId) {
        showTopShort(resId);
    }

    public static void showTopShort(@StringRes final int resId) {
        setGravity(Gravity.TOP, 0, DeviceUtils.dp2px(Utils.getApp(), 80));
        show(resId, Toast.LENGTH_SHORT);
    }

    public static void showCenterShort(@StringRes final int resId) {
        setGravity(Gravity.CENTER, 0, 0);
        show(resId, Toast.LENGTH_SHORT);
    }


    public static void showBottomShort(@StringRes final int resId) {
        setGravity(Gravity.BOTTOM, 0, DeviceUtils.dp2px(Utils.getApp(), 80));
        show(resId, Toast.LENGTH_SHORT);
    }


    /**
     * Show the toast for a short period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    public static void showShort(@StringRes final int resId, final Object... args) {
        if (args != null && args.length == 0) {
            show(resId, Toast.LENGTH_SHORT);
        } else {
            show(resId, Toast.LENGTH_SHORT, args);
        }
    }

    /**
     * Show the toast for a short period of time.
     *
     * @param format The format.
     * @param args   The args.
     */
    public static void showShort(final String format, final Object... args) {
        if (args != null && args.length == 0) {
            show(format, Toast.LENGTH_SHORT);
        } else {
            show(format, Toast.LENGTH_SHORT, args);
        }
    }

    /**
     * Show the toast for a long period of time.
     *
     * @param text The text.
     */
    public static void showLong(@NonNull final CharSequence text) {
        show(text, Toast.LENGTH_LONG);
    }

    /**
     * Show the toast for a long period of time.
     *
     * @param resId The resource id for text.
     */
    public static void showLong(@StringRes final int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    /**
     * Show the toast for a long period of time.
     *
     * @param resId The resource id for text.
     * @param args  The args.
     */
    public static void showLong(@StringRes final int resId, final Object... args) {
        if (args != null && args.length == 0) {
            show(resId, Toast.LENGTH_SHORT);
        } else {
            show(resId, Toast.LENGTH_LONG, args);
        }
    }

    /**
     * Show the toast for a long period of time.
     *
     * @param format The format.
     * @param args   The args.
     */
    public static void showLong(final String format, final Object... args) {
        if (args != null && args.length == 0) {
            show(format, Toast.LENGTH_SHORT);
        } else {
            show(format, Toast.LENGTH_LONG, args);
        }
    }

    /**
     * Show custom toast for a short period of time.
     */
    public static View showCustomShort(@LayoutRes final int layoutId) {
        final View view = getView(layoutId);
        show(view, Toast.LENGTH_SHORT);
        return view;
    }

    /**
     * Show custom toast for a long period of time.
     */
    public static View showCustomLong(@LayoutRes final int layoutId) {
        final View view = getView(layoutId);
        show(view, Toast.LENGTH_LONG);
        return view;
    }

    /**
     * Cancel the toast.
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

    private static void show(@StringRes final int resId, final int duration) {
        show(Utils.getApp().getResources().getText(resId).toString(), duration);
    }

    private static void show(@StringRes final int resId, final int duration, final Object... args) {
        show(String.format(Utils.getApp().getResources().getString(resId), args), duration);
    }

    private static void show(final String format, final int duration, final Object... args) {
        show(String.format(format, args), duration);
    }

    private static void show(final CharSequence text, final int duration) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                cancel();
                sToast = Toast.makeText(Utils.getApp(), text, duration);
                TextView tvMessage = sToast.getView().findViewById(android.R.id.message);
                int msgColor = tvMessage.getCurrentTextColor();
                //it solve the font of toast
                TextViewCompat.setTextAppearance(tvMessage, android.R.style.TextAppearance);
                if (sMsgColor != COLOR_DEFAULT) {
                    tvMessage.setTextColor(sMsgColor);
                } else {
                    tvMessage.setTextColor(msgColor);
                }
                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                    sToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                setBg(tvMessage);
                sToast.show();
            }
        });
    }

    private static void show(final View view, final int duration) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                cancel();
                sToast = new Toast(Utils.getApp());
                sToast.setView(view);
                sToast.setDuration(duration);
                if (sGravity != -1 || sXOffset != -1 || sYOffset != -1) {
                    sToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                setBg();
                sToast.show();
            }
        });
    }

    private static void setBg() {
        View toastView = sToast.getView();
        if (sBgResource != -1) {
            toastView.setBackgroundResource(sBgResource);
        } else if (sBgColor != COLOR_DEFAULT) {
            Drawable background = toastView.getBackground();
            if (background != null) {
                background.setColorFilter(
                        new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN)
                );
            } else {
                ViewCompat.setBackground(toastView, new ColorDrawable(sBgColor));
            }
        }
    }

    private static void setBg(final TextView tvMsg) {
        View toastView = sToast.getView();
        if (sBgResource != -1) {
            toastView.setBackgroundResource(sBgResource);
            tvMsg.setBackgroundColor(Color.TRANSPARENT);
        } else if (sBgColor != COLOR_DEFAULT) {
            Drawable tvBg = toastView.getBackground();
            Drawable msgBg = tvMsg.getBackground();
            if (tvBg != null && msgBg != null) {
                tvBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
                tvMsg.setBackgroundColor(Color.TRANSPARENT);
            } else if (tvBg != null) {
                tvBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
            } else if (msgBg != null) {
                msgBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
            } else {
                toastView.setBackgroundColor(sBgColor);
            }
        }
    }

    private static View getView(@LayoutRes final int layoutId) {
        LayoutInflater inflate =
                (LayoutInflater) Utils.getApp().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflate != null ? inflate.inflate(layoutId, null) : null;
    }
}
