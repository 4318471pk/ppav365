package com.live.fox.utils.okgo;

import android.app.Application;
import android.text.TextUtils;

import com.live.fox.common.JsonCallback;
import com.live.fox.utils.LogUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by lc on 2018/9/17.
 */

public class OkGoHttpUtil {

    private static OkGoHttpUtil sInstance;
    private OkHttpClient mOkHttpClient;

    private OkGoHttpUtil() {
    }

    public static OkGoHttpUtil getInstance() {
        if (sInstance == null) {
            synchronized (OkGoHttpUtil.class) {
                if (sInstance == null) {
                    sInstance = new OkGoHttpUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 在Application中初始化
     */
    public void init(Application app, boolean isShowHttpLog) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS); //全局的连接超时时间
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);    //全局的读取超时时间
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);   //全局的写入超时时间 对这三种时间的详细解释见https://github.com/jeasonlzy/okhttp-OkGo/wiki/Init
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));//使用内存保持cookie，APP退出后，cookie消失
        builder.retryOnConnectionFailure(true);
        builder.proxy(Proxy.NO_PROXY); //防抓包

        //Log输出HTTP请求 响应信息
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("http", isShowHttpLog);
        //Log打印级别，决定了Log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        mOkHttpClient = builder.build();

        OkGo.getInstance().init(app)
                .setOkHttpClient(mOkHttpClient)
                .setCacheMode(CacheMode.NO_CACHE)
                .setRetryCount(0);             //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    }

    /**
     * 取消网络请求
     * OkGoHttpUtil.getInstance().cancel(HttpConsts.ADD_CASH_ACCOUNT);
     */
    public void cancel(String tag) {
        OkGo.cancelTag(mOkHttpClient, tag);
    }


    /**
     * Get请求
     */
    public GetRequest<String> doGet(String tag, String url) {
        return OkGo.<String>get(url)
                .tag(tag);
    }

    /**
     * Get请求 可添加头部
     * HttpHeaders headers = new HttpHeaders();
     * headers.put("key", "value");
     */
    public GetRequest<String> doGet(String tag, String url, HttpHeaders headers) {
        GetRequest<String> getRequest = OkGo.<String>get(url)
                .tag(tag);
        if (headers != null) {
            getRequest.headers(headers);
        }

        return getRequest;
    }


    /**
     * Post请求
     * tag用于取消请求
     */
    public PostRequest<String> doPost(String tag, String url, HttpHeaders headers, HttpParams params) {
        PostRequest<String> postRequest = OkGo.<String>post(url)
                .tag(tag);
        if (headers != null) {
            postRequest.headers(headers);
        }
        if (params != null) {
            postRequest.params(params);
        }

        return postRequest;
    }

    /**
     * Post Json请求
     */
    public PostRequest<String> doJsonPost(String tag, String url, HttpHeaders headers, String json) {
        PostRequest<String> postRequest = OkGo.<String>post(url)
                .tag(tag);
        if (headers != null) {
            postRequest.headers(headers);
        }
        if (!TextUtils.isEmpty(json)) {
            LogUtils.i("Request params：" + json);
            postRequest.upRequestBody(RequestBody.create(json, MediaType.parse("application/json; charset=utf-8")));
        }
        return postRequest;
    }

    public PostRequest<String> doFormPost(String tag, String url) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("data", "12123123123123")
                .build();
        PostRequest<String> postRequest = OkGo.<String>post(url)
                .tag(tag);

        postRequest.upRequestBody(requestBody);

        return postRequest;
    }

    public String mapToUrlWithValue(String url, HashMap<String, Object> map) {
        Set<String> keySet = map.keySet();
        if (!url.contains("?")) {
            url = url + "?";
        }
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            url = url + key + "=" + map.get(key);
            if (iterator.hasNext()) {
                url = url + "&";
            }
        }
        return url;
    }
}
