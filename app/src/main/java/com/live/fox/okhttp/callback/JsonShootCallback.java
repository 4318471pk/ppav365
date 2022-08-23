package com.live.fox.okhttp.callback;


import com.google.gson.reflect.TypeToken;
import com.live.fox.okhttp.model.ResponseShoot;
import com.live.fox.utils.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * User: ljx
 * Date: 2017/9/13
 * Time: 10:18
 */
public abstract class JsonShootCallback<T> extends FailureCallback {

    private Type type;

    public JsonShootCallback() {
        type = getSuperclassTypeParameter(getClass());
    }

    @Override
    public void onResponse(Call call, okhttp3.Response response) {
        try {
            if (!response.isSuccessful()) {
//                throw new IOException(String.valueOf(response.code()));
                onSuccessInMainThread(response.code(), response.message(), null);
                return;
            }
            ResponseBody body = response.body();
            if (body == null)
                throw new IOException("response empty");
            String content = body.string();
            if (content == null)
                throw new IOException("data is null");
//            content = Encrypt.decryptShoot(content, Constants.DB_SHOOT_KEY);
            if (content == null)
                throw new IOException("data is null");

            final ResponseShoot result = new ResponseShoot();
            JSONObject json = new JSONObject(content);
            result.setResult(json.optString("result"));
            result.setCode(json.optString("errorcode"));
            TypeToken<T> tTypeToken = (TypeToken<T>) TypeToken.get(type);
            Class<? super T> rawType = tTypeToken.getRawType();
            if (rawType == String.class) {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        onSuccess(result.getCode(), (T) result.getResult());
//                    }
//                });
                onSuccessInMainThread(Integer.parseInt(result.getCode()), response.message(), (T) result.getResult());
                return;
            }
            T t = null;
            if ("00000:ok".equals(result.getCode())) {
                t = parser(result.getResult());
                if (t == null) {
                    throw new IOException("object is null");
                }
            }
            final T t1 = t;
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    onSuccess(result.getCode(), t1);
//                }
//            });
            onSuccessInMainThread(Integer.parseInt(result.getCode()), response.message(), t1);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            onFailure(call, e);
        } catch (JSONException e) {
            e.printStackTrace();
            onFailure(call, new IOException("data error"));
        }
    }

    public void onSuccessInMainThread(final int code, final String msg, final T t) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(code, msg, t);
            }
        });
    }

    @Override
    public void onFailure(String error) {
        onSuccessInMainThread(-1, error, null);
    }

    public abstract void onSuccess(int code, String msg, T t);

    //此方法在子线程执行
    public T parser(String data) {
        return GsonUtil.getObject(data, type);
    }
}
