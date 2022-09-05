package com.live.fox.utils;

import android.content.Context;
import android.provider.Settings;

import com.live.fox.common.CommonApp;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class DeviceIdUtils {

    /**
     * 获取Mac地址
     */
    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder strBuilder = new StringBuilder();
                for (byte b : macBytes) {
                    strBuilder.append(String.format("%02X:", b));
                }
                if (strBuilder.length() > 0) {
                    strBuilder.deleteCharAt(strBuilder.length() - 1);
                }
                return strBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getAndroidId(CommonApp.getInstance());
        }
        return getAndroidId(CommonApp.getInstance());
    }

    /**
     * 获取ANDROID_ID
     */
    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
