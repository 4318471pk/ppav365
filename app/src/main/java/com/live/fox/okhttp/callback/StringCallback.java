package com.live.fox.okhttp.callback;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * User: ljx
 * Date: 2017/9/13
 * Time: 9:49
 */
public abstract class StringCallback extends FailureCallback {

    private final boolean callbackInUIThread;//回调是否在主线程

    public StringCallback() {
        this(true);
    }

    public StringCallback(boolean callbackInUIThread) {
        this.callbackInUIThread = callbackInUIThread;
    }

    @Override
    public void onResponse(Call call, Response response) {
        try {
            if (!response.isSuccessful()) {
                onSuccessInMainThread(response.code(), response.message(), null);
                return;
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("response empty");
            }

            final String result = body.string();
            onSuccessInMainThread(0, response.message(), result);
//            if (callbackInUIThread) {
//
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        onSuccess(0, result);
//                    }
//                });
//            } else {
//                onSuccess(0, result);
//            }
        } catch (IOException e) {
            e.printStackTrace();
            onFailure(call, e);
        }
    }

    public void onSuccessInMainThread(final int code, final String msg, final String result){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(code, msg, result);
            }
        });
    }

    @Override
    public void onFailure(String error) {
        onSuccessInMainThread(-1, error, null);
    }

    public abstract void onSuccess(int code, String msg, String result);
}
