package com.live.fox.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import com.live.fox.R;
import com.live.fox.utils.utilconstants.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 *
 setStatusBarHalfAlpha                : 设置状态栏半透明
 setStatusBarFulAlpha                 : 设置状态栏全透明 此时布局会上移直手机最顶部,需在布局中空出位置
 setStatusBarAlpha                    : 设置状态栏透明度
 setStatusBarTranslucentInFullTheme   : 设置状态栏半透明
 setStatusBarTransparentInFullTheme   : 设置状态栏全透明

 setStatusBarColor                    : 设置状态栏颜色
 setStatusBarColorForDrawerLayout     : 为DrawerLayout 布局设置状态栏颜色
 setNavigationBarColor                : 设置底部状态栏颜色

 setStatusBarTextDarkColor            : 设置导航栏字体颜色-黑色
 setStatusBarTextLightColor           : 设置导航栏字体颜色-灰色

 其中：
 setStatusBarTranslucentInFullTheme   : 设置状态栏半透明
 setNavigationBarColor                : 设置底部状态栏颜色
 需要在 Activity的Theme为下列Theme时才会有效果（使用此主题时，App最低版本需要>=21 否则在低于21版本的就会报错 或者把此主题名改成AppTheme）
 <style name="AppThemeBarTranslucent" parent="AppTheme.Base">
     <item name="android:windowTranslucentStatus">true</item>
     <item name="android:statusBarColor">@android:color/transparent</item>
     <item name="android:navigationBarColor">@android:color/transparent</item>
     <item name="android:windowContentTransitions">true</item>
     <item name="android:windowTranslucentNavigation">true</item>
 </style>


 * Created by cheng on 18/5/09.
 *
 * Des:此类参考自：Github上的jgilfelt/SystemBarTint和laobie/StatusBarUtil
 */
public class StatusBarUtil {

    public static final int DEFAULT_STATUS_BAR_ALPHA = 86;
    private static final int FAKE_STATUS_BAR_VIEW_ID = R.id.statusbarutil_fake_status_bar_view;
    private static final int FAKE_TRANSLUCENT_VIEW_ID = R.id.statusbarutil_translucent_view;
    private static final int TAG_KEY_HAVE_SET_OFFSET = -123;


    public static void setStatusBarHalfAlpha(Activity activity, View needOffsetView) {
        setStatusBarAlpha(activity, 86, needOffsetView);
    }

    /**
     * 注：此方法与软键盘的弹起有冲突，有软键盘的界面谨慎使用此方法
     *    会导致软键盘弹起时不顶起底部EditText
     */
    public static void setStatusBarFulAlpha(Activity activity) {
        setStatusBarFulAlpha(activity, null);
    }

    public static void setStatusBarFulAlpha(Activity activity, View needOffsetView) {
        setStatusBarAlpha(activity, 0, needOffsetView);
    }

    /**
     * 设置状态栏半透明（根布局中 不设置android:fitsSystemWindows属性 或设置此属性为false）
     *
     * @param needOffsetView  需要偏移的View,一般传入第一个子控件
     * alpha  0:完全透明 122:半透明 255:黑色
     */
    public static void setStatusBarAlpha(Activity activity, @IntRange(from = 0, to = 255) int alpha,
                                               View needOffsetView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForWindow(activity);
        addStatusBarView(activity, alpha);
        if (needOffsetView != null) {
            Object haveSetOffset = needOffsetView.getTag(TAG_KEY_HAVE_SET_OFFSET);
            if (haveSetOffset != null && (Boolean) haveSetOffset) {
                return;
            }
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin + getStatusBarHeight(activity),
                    layoutParams.rightMargin, layoutParams.bottomMargin);
            needOffsetView.setTag(TAG_KEY_HAVE_SET_OFFSET, true);
        }
    }

    /**
     * 设置状态栏半透明（当Theme为AppThemeBarTranslucent时,用此方法）
     <style name="AppThemeBarTranslucent" parent="AppTheme.Base">
     <item name="android:windowTranslucentStatus">true</item>
     <item name="android:statusBarColor">@android:color/transparent</item>
     <item name="android:navigationBarColor">@android:color/transparent</item>
     <item name="android:windowContentTransitions">true</item>
     <item name="android:windowTranslucentNavigation">true</item>
     </style>
     */
    public static void setStatusBarTranslucentInFullTheme(Activity activity, @IntRange(from = 0, to = 255) int alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setStatusBarTransparentInFullTheme(activity);
        addStatusBarView(activity, alpha);
    }

    /**
     * 设置状态栏全透明 （当Theme为AppThemeBarTranslucent时,用此方法）
     <style name="AppThemeBarTranslucent" parent="AppTheme.Base">
     <item name="android:windowTranslucentStatus">true</item>
     <item name="android:statusBarColor">@android:color/transparent</item>
     <item name="android:navigationBarColor">@android:color/transparent</item>
     <item name="android:windowContentTransitions">true</item>
     <item name="android:windowTranslucentNavigation">true</item>
     </style>
     */
    public static void setStatusBarTransparentInFullTheme(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        setRootView(activity);
    }


    /**
     * 设置状态栏颜色（需要在根布局中加 android:fitsSystemWindows="true"属性）
     *
     * alpha  0:原色 122:设置的颜色+半透明 255:黑色
     */
    public static void setStatusBarColor(Activity activity, int color) {
        setStatusBarColor(activity, color, 0);
    }


    /**
     * 设置状态栏颜色（需要在根布局中加 android:fitsSystemWindows="true"属性）

     * alpha  0:原色 122:设置的颜色+半透明 255:黑色
     */
    public static void setStatusBarColor(Activity activity, int color, @IntRange(from = 0, to = 255) int alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForWindow(activity);
        addStatusBarView(activity, color, alpha);
    }


    /**
     * 为DrawerLayout 布局设置状态栏颜色 最终显示为半透明的效果（需要在根布局中加 android:fitsSystemWindows="true"属性）
     */
    public static void setStatusBarColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setStatusBarColorForDrawerLayout(activity, drawerLayout, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 为DrawerLayout 布局设置状态栏颜色,最终显示为设置的颜色效果（需要在根布局中加 android:fitsSystemWindows="true"属性）
     */
    public static void setStatusBarColorForDrawerLayoutNoTranslucent(Activity activity, DrawerLayout drawerLayout, @ColorInt int color) {
        setStatusBarColorForDrawerLayout(activity, drawerLayout, color, 0);
    }

    /**
     * 为DrawerLayout 布局设置状态栏颜色（需要在根布局中加 android:fitsSystemWindows="true"属性）
     */
    public static void setStatusBarColorForDrawerLayout(Activity activity, DrawerLayout drawerLayout, @ColorInt int color,
                                                        @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 生成一个状态栏大小的矩形
        // 添加 statusBarView 到布局中
        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        View fakeStatusBarView = contentLayout.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(color);
        } else {
            contentLayout.addView(createStatusBarView(activity, color, 0), 0);
        }
        // 内容布局不是 LinearLayout 时,设置padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                    .setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(activity) + contentLayout.getPaddingTop(),
                            contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
        }
        // 设置属性
        setDrawerLayoutProperty(drawerLayout, contentLayout);
        addStatusBarView(activity, statusBarAlpha);
    }


    /**
     * 设置底部状态栏颜色 （需要设置主题）
     */
    public static void setNavigationBarColor(Activity activity, int color) {
        SystemBarTintManager mTintManager = new SystemBarTintManager(activity);
        mTintManager.setNavigationBarTintEnabled(true);
        mTintManager.setNavigationBarTintColor(color);
    }

    /**
     * 设置底部状态栏颜色 （需要设置主题）
     *
     * alpha  0:原色 122:设置的颜色+半透明 255:黑色
     */
    public static void setNavigationBarColor(Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        boolean isTranslucent = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (isTranslucent) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setNavigationBarTintColor(calculateStatusColor(color, alpha));
    }


    /**
     * 设置导航栏字体颜色-黑色
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void setStatusBarTextDarkColor(Activity activity) {
        setMIUIStatusBarDarkIcon(activity, true);
        setMeizuStatusBarDarkIcon(activity, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * 设置导航栏字体颜色-灰色
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void setStatusBarTextLightColor(Activity activity) {
        setMIUIStatusBarDarkIcon(activity, false);
        setMeizuStatusBarDarkIcon(activity, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }







/** 私有方法-----------------------------------------------------------------------------------*/



    /**
     * 修改 MIUI V6  以上状态栏颜色
     */
    private static void setMIUIStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkIcon ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * 修改魅族状态栏字体颜色 Flyme 4.0
     */
    private static void setMeizuStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkIcon) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }


    /**
     * 设置 DrawerLayout 属性
     *
     * @param drawerLayout              DrawerLayout
     * @param drawerLayoutContentLayout DrawerLayout 的内容布局
     */
    private static void setDrawerLayoutProperty(DrawerLayout drawerLayout, ViewGroup drawerLayoutContentLayout) {
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @param alpha    透明值
     * @return 状态栏矩形条
     */
    private static View createStatusBarView(Activity activity, @ColorInt int color, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        statusBarView.setId(FAKE_STATUS_BAR_VIEW_ID);
        return statusBarView;
    }

    /**
     * 添加StatusBarView
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private static void addStatusBarView(Activity activity, int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.getVisibility() == View.GONE) {
                fakeTranslucentView.setVisibility(View.VISIBLE);
            }
            fakeTranslucentView.setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, color, statusBarAlpha));
        }
    }



    /**
     * 添加StatusBarView
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private static void addStatusBarView(Activity activity, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeTranslucentView = contentView.findViewById(FAKE_TRANSLUCENT_VIEW_ID);
        if (fakeTranslucentView != null) {
            if (fakeTranslucentView.getVisibility() == View.GONE) {
                fakeTranslucentView.setVisibility(View.VISIBLE);
            }
            fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }

    /**
     * 设置透明
     */
    private static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private static View createTranslucentStatusBarView(Activity activity, int color, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        statusBarView.setId(FAKE_TRANSLUCENT_VIEW_ID);
        return statusBarView;
    }


    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private static View createTranslucentStatusBarView(Activity activity, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        statusBarView.setId(FAKE_TRANSLUCENT_VIEW_ID);
        return statusBarView;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 设置根布局参数
     */
    private static void setRootView(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 根据颜色和alpha计算出最终颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }
}
