package com.live.fox.okhttp.callback;


import com.google.gson.internal.$Gson$Types;
import com.live.fox.utils.NetworkUtils;
import com.live.fox.utils.ToastUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import okhttp3.Call;

/**
 * http请求失败回调逻辑
 * User: ljx
 * Date: 2017/12/3
 * Time: 16:41
 */
public abstract class FailureCallback implements BaseCallback {

    @Override
    public final void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (e instanceof UnknownHostException) {
                    if (!NetworkUtils.isConnected()) {
                        ToastUtils.showShort("网络君已失联，请检查你的网络设置");
                    }
                }
                onFailure(e.getMessage());
            }
        });
    }






    Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }
}
