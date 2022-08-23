package com.live.fox.okhttp.callback;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.live.fox.okhttp.model.Response;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * User: ljx
 * Date: 2017/9/13
 * Time: 10:01
 */
public abstract class BaseJsonCallback<T> extends FailureCallback {

    private Type type;

    private Response result;

    public BaseJsonCallback() {
        type = getSuperclassTypeParameter(getClass());
    }

    @Override
    public void onResponse(Call call, okhttp3.Response response) {
        LogUtils.e(response.code()+", "+response.message());
        try {
            if (!response.isSuccessful()) {
                LogUtils.e(response.code()+", "+response.message());
                onSuccessInMainThread(response.code(), response.message(), null);
                return;
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("response empty");
            }

            String content = body.string();
//            LogUtils.e(content);
            if (content == null) {
                throw new IOException("data is null");
            }

            LogUtils.e("请求结果:"+content);

            result = new Response();
            JSONObject json = new JSONObject(content);
            result.setCode(json.optInt("code", -2000));
            result.setMsg(json.optString("msg", ""));
            result.setData(json.optString("data", ""));


            TypeToken<T> tTypeToken = (TypeToken<T>) TypeToken.get(type);
            Class<? super T> rawType = tTypeToken.getRawType();
            if (rawType == String.class) {
//                LogUtils.e("请求结果:12 1");
                onSuccessInMainThread(result.getCode(), result.getMsg(), (T) result.getData());
                return;
            }
//            LogUtils.e("请求结果:11 2");
            int code = result.getCode();
            T t = null;
            if (!TextUtils.isEmpty(result.getData())) {
//                LogUtils.e("请求结果:121 3");
                t = parser(result.getData());
            }
            if (code == 100 && t == null) {
//                LogUtils.e("请求结果:121 4  ");
                throw new IOException("object is null");
            }
//            LogUtils.e("请求结果:121 5");
            final T tempT = t;
//            LogUtils.e("请求结果:121 9");
            onSuccessInMainThread(result.getCode(), result.getMsg(), tempT);
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    onSuccess(result.getCode(), result.getMsg(), tempT);
//                }
//            });

        } catch (IOException e) {
//            LogUtils.e("请求结果:121 6");
            e.printStackTrace();
            onFailure(call, e);
        } catch (JSONException e) {
//            LogUtils.e("请求结果:121 7");
            e.printStackTrace();
            onFailure(call, new IOException("data error"));
        } catch (Exception e) {
//            LogUtils.e("请求结果:121 8");
            e.printStackTrace();
            onFailure(call, new IOException("data error"));
        }
    }

    public void onSuccessInMainThread(final int code, final String msg, final T tempT){
//        LogUtils.e("请求结果:121 51");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                LogUtils.e("请求结果:121 52" +code+","+msg+","+tempT);
                onSuccess(code, msg, tempT);
            }
        });
    }

    @Override
    public void onFailure(String error) {
        LogUtils.e("1111,"+error);
        onSuccessInMainThread(-1, error, null);
    }

    public String getTip() {
        return result == null ? "" : result.getMsg();
    }

    public abstract void onSuccess(int code, String msg, T data);

    //此方法在子线程执行
    public T parser(String data) {
        return GsonUtil.getObject(data, type);
    }
}
