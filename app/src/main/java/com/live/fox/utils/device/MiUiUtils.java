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
 * Date: 2017/5/12
 * Time: 9:56
 * 针对小米系统
 */

public class MiUiUtils {

    /**
     * 是否是小米系统
     *
     * @return
     */
    public static boolean isMiUi() {
        String device = Build.MANUFACTURER;
        return device.equals("Xiaomi");
    }

    /**
     * 得到miUi应用权限设置页面的Intent
     */
    public static Intent getSettingPermissionsIntent(String packageName) {
        Intent intent;
        // miUi 8
        intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", packageName);
        if (!isIntentAvailable(intent)) {
            // miUi 5/6/7
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);

            if (!isIntentAvailable(intent)) {
                // 跳转到应用详情
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", packageName, null);
                intent.setData(uri);
            }
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
