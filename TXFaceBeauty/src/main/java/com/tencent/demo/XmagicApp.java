package com.tencent.demo;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;


public class XmagicApp extends Application implements CameraXConfig.Provider {


    @Override
    public void onCreate() {
        super.onCreate();
        XMagicImpl.init(this);
        XMagicImpl.checkAuth(null,false);
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig()).build();
    }
}
