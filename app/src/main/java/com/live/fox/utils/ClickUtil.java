package com.live.fox.utils;

import android.app.Instrumentation;
import android.view.KeyEvent;


public class ClickUtil {

    private static long lastClickTime;
    private static long exitClickTime;

    private static final long GIFT_CLICK_TIME = 100;
    private static final long FAST_CLICK_TIME = 500;
    private static final long EXIT_CLICK_TIME = 2000;

    private static long lastClickTime2;


    /**
     * 快速连续点击 (500毫秒内)
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastClickTime != 0 && 0 < timeD && timeD < FAST_CLICK_TIME) { //500毫秒内
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 快速连续点击 (100毫秒内)
     *
     * @return
     */
    public static boolean isGiftFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastClickTime != 0 && 0 < timeD && timeD < GIFT_CLICK_TIME) { //
            return true;
        }
        lastClickTime = time;
        return false;
    }


    /**
     * 点击返回键
     */
    public static void onBackClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            }
        }).start();
    }


    public static boolean isFastDoubleClick(long instanceTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastClickTime != 0 && 0 < timeD && timeD < instanceTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick2(long instanceTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime2;
        if (0 < timeD && timeD < instanceTime) {
            return true;
        }
        lastClickTime2 = time;
        return false;
    }

    /**
     * 退出点击 (2秒内)
     *
     * @return
     */
    public static boolean isExitDoubleClick() {

        long time = System.currentTimeMillis();
        long timeD = time - exitClickTime;
        if (exitClickTime != 0 && timeD < EXIT_CLICK_TIME) {
            return true;
        }
        exitClickTime = time;
        return false;
    }


}
