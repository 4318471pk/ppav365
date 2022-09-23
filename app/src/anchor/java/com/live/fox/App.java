package com.live.fox;


import android.os.Handler;
import android.os.Looper;

import com.faceunity.beautycontrolview.FURenderer;
import com.live.fox.common.CommonApp;
import com.lovense.sdklibrary.Lovense;

/**
 * 主播端的App
 */
public class App extends CommonApp {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化美颜
        FURenderer.initFURenderer(this);

        //初始化玩具
        Lovense.getInstance(this).setDeveloperToken(Constant.ToyToken);
    }

}
