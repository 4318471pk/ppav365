package com.live.fox.utils;

import android.content.Context;
import android.text.TextUtils;

import com.live.fox.R;
import com.tencent.rtmp.TXLivePlayer;


public class PlayerUtils {

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String RTMP = "rtmp://";
    public static final String FLV = ".flv";
    public static final String M3U8 = ".m3u8";
    public static final String MP4 = ".mp4";

    public static boolean checkPlayUrl(String url, Context context) {
        String mUrl=url;
        if(!TextUtils.isEmpty(url))
        {
            mUrl=mUrl.toLowerCase().trim();
        }
        if (TextUtils.isEmpty(mUrl) ||
                (!mUrl.startsWith(HTTP) &&
                        !mUrl.startsWith(HTTPS) &&
                        !mUrl.startsWith(RTMP))) {
            ToastUtils.showShort(context.getString(R.string.bfdzbhf));
            return false;
        }

        if (!mUrl.startsWith(RTMP)) {
            if(!mUrl.startsWith(HTTP) && !mUrl.startsWith(HTTPS))
            {
                ToastUtils.showShort(context.getString(R.string.bfdzbhf));
                return false;
            }

            if( !mUrl.endsWith(FLV) &&  !mUrl.endsWith(M3U8) && !mUrl.endsWith(MP4))
            {
                ToastUtils.showShort(context.getString(R.string.bfdzbhf));
                return false;
            }
        }

        return true;
    }

    public static int getVideoType(String url)
    {
        String mUrl=url;
        if(!TextUtils.isEmpty(url))
        {
            mUrl=mUrl.toLowerCase().trim();
        }
        else
        {
            return -1;
        }

        if(mUrl.startsWith(RTMP))
        {
            return TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
        }
        else if(mUrl.endsWith(FLV))
        {
            return TXLivePlayer.PLAY_TYPE_VOD_FLV;
        }
        else if(mUrl.endsWith(M3U8))
        {
            return TXLivePlayer.PLAY_TYPE_LIVE_FLV;
        }
        else if(mUrl.endsWith(MP4))
        {
            return TXLivePlayer.PLAY_TYPE_VOD_MP4;
        }

        return -1;
    }
}
