package com.live.fox.okhttp.callback;

import android.os.Handler;
import android.os.Looper;

import okhttp3.Callback;

/**
 * User: ljx
 * Date: 2017/12/1
 * Time: 20:03
 */
public interface BaseCallback extends Callback {

    Handler mHandler = new Handler(Looper.getMainLooper());

    void onFailure(String error);


}

