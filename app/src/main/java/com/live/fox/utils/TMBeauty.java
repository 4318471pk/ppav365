package com.live.fox.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.demo.module.XmagicResParser;
import com.tencent.demo.utils.AppUtils;
import com.tencent.xmagic.telicense.TELicenseCheck;
import com.tencent.demo.XMagicImpl;

import java.io.File;

import static com.tencent.demo.activity.CameraXActivity.EXTRA_IS_BACK_CAMERA;

//腾讯美颜相关方法
public class TMBeauty {

    static TMBeauty tmBeauty;

    public static TMBeauty getInstance()
    {
        if(tmBeauty==null)
        {
            tmBeauty=new TMBeauty();
        }
        return tmBeauty;
    }

    public void init(Activity context, AuthCallback authCallback)
    {
        XMagicImpl.init(context);
        XMagicImpl.checkAuth((errorCode, msg) -> {
            if (errorCode == TELicenseCheck.ERROR_OK) {
                //以下是子线程 复制资源到本地
                XmagicResParser.setResPath(new File(context.getFilesDir(), "xmagic").getAbsolutePath());
                if (!isCopyRes(context)) {
                    XmagicResParser.copyRes(context);
                    saveCopyData(context);
                }
                XmagicResParser.parseRes(context);

                context.runOnUiThread(() -> {
                    authCallback.onResourceReady();
                });
            } else {
                authCallback.onAuthFailed(errorCode, msg);
            }
        }, false);
    }


    private boolean isCopyRes(Context context) {
        String appVersionName = com.tencent.demo.utils.AppUtils.getAppVersionName(context);
        return SPUtils.getInstance("ResSetting").getString("resource_copied","").equals(appVersionName);
    }

    private void saveCopyData(Context context) {
        String appVersionName = AppUtils.getAppVersionName(context);
        SPUtils.getInstance("ResSetting").put("resource_copied",appVersionName);
    }

    public interface AuthCallback {

        void onResourceReady();

        void onAuthFailed(int errorCode, String msg);
    }
}
