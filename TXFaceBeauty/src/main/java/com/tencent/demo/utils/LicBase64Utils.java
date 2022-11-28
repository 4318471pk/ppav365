package com.tencent.demo.utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * lic文件转base64 工具类
 */
public class LicBase64Utils {
    private static final String TAG = LicBase64Utils.class.getSimpleName();
    public static String lic2Base64(Context context, String licName){
        InputStream licInputStream = null;
        String base64LicStr = null;
        try {
            licInputStream = context.getAssets().open(licName);
            byte[] licBuffer = new byte[2048];
            int index = licInputStream.read(licBuffer);
            if (index < 2048) {
                base64LicStr = Base64.encodeToString(licBuffer, 0, index, 2);
            }
        } catch (IOException e) {
            Log.e(TAG,"lic2Base64 read lic file error-"+e.toString());
        }finally {
            try {
                if(licInputStream != null) {
                    licInputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG,"lic2Base64 close lic file IO error-"+e.toString());
            }
        }
        return base64LicStr;
    }
}
