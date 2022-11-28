package com.live.fox;


import android.os.Handler;
import android.os.Looper;

import com.fm.openinstall.OpenInstall;
import com.live.fox.common.CommonApp;

/**
 * 主播端的App
 */
public class App extends CommonApp {

    @Override
    public void onCreate() {
        super.onCreate();
        OpenInstall.init(this);
    }

}
