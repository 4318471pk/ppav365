package com.live.fox.utils.device;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.live.fox.utils.Utils;

import java.util.List;

/**
 * User: xyp
 * Date: 2017/6/5
 * Time: 19:52
 */

public class MeiTuUtils {
    /**
     * 判断是否是美图手机
     */
    public static boolean isMeiTu(){
        String device = Build.MANUFACTURER;
        return ("Meitu").equals(device);
    }

    /**
     * 由于美图的系统设置里没有权限管理，所以要跳到权限管家
     */
    public static Intent gotoPermissionSetting(String packageName){
        //通过查询美图手机中所有的系统应用找到权限管家的包名
        Intent intent = Utils.getApp().getPackageManager().getLaunchIntentForPackage("com.mediatek.security");
        if(!isIntentAvailable(intent)){
            // 跳转到应用详情
            intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", packageName, null);
            intent.setData(uri);
        }

        return intent;
    }

    /**
     * 判断这个intent是否安全
     *
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Intent intent) {
        PackageManager packageManager = Utils.getApp().getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);
        return list.size() > 0;
    }
}
