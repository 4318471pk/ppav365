package com.live.fox.common;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.live.fox.R;
import com.live.fox.UserJump;
import com.live.fox.manager.DataCenter;
import com.live.fox.ui.login.LoginActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.manager.AppUserManger;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created  cxf on 2017/8/7.
 */

public abstract class JsonCallback<T> extends StringCallback {

    private final Type type;

    private String urlTag = "request";

    public JsonCallback() {
        type = getSuperclassTypeParameter(getClass());
    }

    public void setUrlTag(String url) {
        urlTag = url;
    }

    @Override
    public void onSuccess(Response<String> response) {
        String url=response.getRawCall().request().url().toString();
        LogUtils.e(url+" "+response.body());
        try {
            if ("cp/list".equals(urlTag)) {
                Log.e(urlTag, "url: " + response.getRawResponse().request().url() + " \n" + "response: " + response.body());
            }

            if (response.code() == 451 && !(ActivityUtils.getTopActivity() instanceof LoginActivity)) {
                Activity activity = ActivityUtils.getTopActivity();
                if (activity != null) {
                    UserJump.jumpHelp();
                    return;
                }
            }

            if (!response.isSuccessful()) {
                onSuccessInMainThread(response.code(), response.message(), null);
                return;
            }

            String content = response.body();
            if (content == null) {
                throw new IOException("The Server Result Is Null");
            }

            LogUtils.i("请求结果:" + content);

            JSONObject json = new JSONObject(content);
            int code = json.optInt("code", -2000);
            String msg = json.optString("msg", "");
            String data = json.optString("data", "");

            if (response.code() == 401) {
                DataCenter.getInstance().getUserInfo().loginOut();
                msg = json.optString("desc", "");
                onSuccessInMainThread(-1, msg, null);
                return;
            }

            TypeToken<T> tTypeToken = (TypeToken<T>) TypeToken.get(type);
            Class<? super T> rawType = tTypeToken.getRawType();
            if (rawType == String.class) {
                onSuccessInMainThread(code, msg, (T) data);
                return;
            }
            T t = null;
            if (!TextUtils.isEmpty(data)) {
                t = parser(data);
            }
            onSuccessInMainThread(code, msg, t);
        } catch (IOException e) {
            e.printStackTrace();
            onSuccessInMainThread(-1, e.getMessage(), null);
        } catch (JSONException e) {
            e.printStackTrace();
            onSuccessInMainThread(-1, "The Server Result Parsing error", null);
        } catch (Exception e) {
            e.printStackTrace();
            onSuccessInMainThread(-1, "The Server Result Is Exception", null);
        }
    }

    @Override
    public void onError(Response<String> response) {

        if(response.getRawCall()!=null )
        {
            String url=response.getRawCall().request().url().toString();
            LogUtils.e(url+" "+response.message() + "," + response.getException().getMessage());
        }

        String responseMsg = response.message();
        if (response.code() != 0 && StringUtils.isEmpty(responseMsg)) {
            responseMsg = CommonApp.getInstance().getString(R.string.dataWrong) + "(" + response.code() + ")";
        }
        onSuccessInMainThread(response.code(), responseMsg, null);
    }

    public void onSuccessInMainThread(final int code, final String msg, final T tempT) {
        new Handler(Looper.getMainLooper()).post(() -> {
            onSuccess(code, msg, tempT);
        });
    }

    public abstract void onSuccess(int code, String msg, T data);

    @Override
    public void onStart(Request<String, ? extends Request> request) {

    }

    @Override
    public void onFinish() {
    }

    Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }

    //此方法在子线程执行
    public T parser(String data) {
        return GsonUtil.getObject(data, type);
    }
}
