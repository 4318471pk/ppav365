package com.live.fox.utils;

import android.content.Context;
import android.text.TextUtils;

import com.live.fox.R;


public class PlayerUtils {

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String RTMP = "rtmp://";
    public static final String FLV = ".flv";
    public static final String M3U8 = ".m3u8";

    public static boolean checkPlayUrl(String url, Context context) {
        if (TextUtils.isEmpty(url) ||
                (!url.startsWith(HTTP) &&
                        !url.startsWith(HTTPS) &&
                        !url.startsWith(RTMP))) {
            ToastUtils.showShort(context.getString(R.string.bfdzbhf));
            return false;
        }

        if (!url.startsWith(RTMP)) {
            if (((!url.startsWith(HTTP) &&
                    !url.startsWith(HTTPS)) ||
                    !url.contains(FLV)) &&
                    !url.contains(M3U8)) {
                ToastUtils.showShort(context.getString(R.string.bfdzbhf));
                return false;
            }
        }

        return true;
    }
}
