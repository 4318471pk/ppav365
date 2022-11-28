package com.tencent.demo.utils;

import android.view.View;

public abstract class NoDoubleClickListener implements View.OnClickListener {
    private long lastClickTime = 0;
    private static long DELAY_TIME = 1000;

    @Override
    public void onClick(View v) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime >= DELAY_TIME) {
            lastClickTime = time;
            onViewClick(v);
        }
    }

    public abstract void onViewClick(View v);
}
