package com.live.fox.okhttp;


import com.live.fox.okhttp.callback.DownloadCallback;
import com.live.fox.okhttp.callback.ProgressCallback;
import com.live.fox.okhttp.callback.UploadFileCallbackBase;
import com.live.fox.okhttp.progress.ProgressRequestBody;
import com.live.fox.okhttp.progress.ProgressResponseBody;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cheng on 2019/3/26.
 */

public class OkHttpUtil {

    //sslSocketFactory和hostnameVerifier是绕过Https的验证 请求Https时用得到
    private static OkHttpClient mOkHttpClient;

    static {
        try {
            TrustManager[] trustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), (X509TrustManager) trustManagers[0])
                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Get请求
    public static void doGet(String url, Callback callback) {
        Request request = new Request.Builder().url(url).get().build();
        doGet(request, callback, true);
    }

    //Get请求 同步
    public static void doGetByExecute(String url, Callback callback) {
        Request request = new Request.Builder().url(url).get().build();
        doGet(request, callback, false);
    }

    //Get请求 此方法可以添加头部 可使用键值对当参数
    public static void doGet(RequestParam param, Callback callback) {
        Request.Builder builder = new Request.Builder();
        builder.url(param.getFullUrl());
        if (param.getHeaders() != null)
            builder.headers(param.getHeaders());
        Request request = builder.get().build();

        doGet(request, callback, true);
    }

    /**
     * 使用的例子:
     * RequestParam requestParam = new RequestParam("http://www.baidu.com");
     * requestParam.put("username", "zhangsan");
     * requestParam.addHeader("111","111");
     * OkHttpUtil.doPost(requestParam, new StringCallback() {
     * ...
     * });
     */
    //Post请求
    public static void doPost(RequestParam param, Callback callback) {
        RequestBody requestBody = BuildUtil.postRequestBody(param);
        doPost(param.getUrl(), param.getHeaders(), requestBody, callback);
    }

    //Post Json请求
    public static void doJsonPost(String url, String json, Callback callback) {
        LogUtils.e("请求地址: " + url);
        LogUtils.e("请求参数: " + json);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        Headers header = null;
        Headers.Builder headersBuilder = new Headers.Builder();
        String token = SPUtils.getInstance("userinfo").getString("token", "");
        if (!StringUtils.isEmpty(token)) {
            headersBuilder.add("Authorization", "HSBox " + token);
            header = headersBuilder.build();
        }

        LogUtils.e("请求token: " + SPUtils.getInstance("userinfo").getString("token", ""));
        doPost(url, header, requestBody, callback);
    }


    // 下载文件 path为全名 eg:/storage/emulated/0/AAA/ccc.png,l,;,,;,
    // 适用下载小文件(<10M) 下载大文件请用DownloadBigFileUtil类
    // eg:OkHttpUtil.doDownlond(url, new DownloadCallback(destPath, false))
    public static void doDownlond(String url, DownloadCallback downloadCallback) {
        Request request = new Request.Builder().url(url).get().build();
        doGet(request, downloadCallback, true);
    }

    /**
     * 上传文件的例子:
     * RequestParam params = new RequestParam(url);
     * params.put("token", BaseSocket.getInstance().getToken());
     * params.put("albumtype", typePhoto);//1：随拍  2：私照
     * for (int i = 0; i < filesList.size(); i++) {
     * params.put("picture" + i, files(i)); //添加文件
     * }
     * OkHttpUtil.doUploadFile(params, new UploadFileCallback<PhotoUploadSuccess>() {
     * ...
     * }
     */
    //上传文件
    public static void doUploadFile(RequestParam param, UploadFileCallbackBase uploadFileCallback) {
        RequestBody requestBody = BuildUtil.postRequestBody(param);
        doPost(param.getUrl(), param.getHeaders(), requestBody, uploadFileCallback);
    }


    //所有的GET请求最终都会走到这里,enqueue代表是否异步
    private static void doGet(Request request, Callback callback, boolean enqueue) {
        OkHttpClient okHttpClient;
        if (callback instanceof ProgressCallback) {
            okHttpClient = clone((ProgressCallback) callback);
        } else {
            okHttpClient = mOkHttpClient;
        }

        if (enqueue) {
            enqueue(okHttpClient, request, callback);
        } else {
            execute(okHttpClient, request, callback);
        }
    }


    //所有的Post请求最终都会走到这里,enqueue代表是否异步
    private static void doPost(String url, Headers headers, RequestBody requestBody, Callback callback) {
        if (callback instanceof ProgressCallback) {
            requestBody = new ProgressRequestBody(requestBody, (ProgressCallback) callback);
        }

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null) {
            builder.headers(headers);
        }
        if (requestBody != null) {
            builder.post((RequestBody) requestBody);
        }
        Request request = builder.build();

        if (true) {
            enqueue(mOkHttpClient, request, callback);
        } else {
            execute(mOkHttpClient, request, callback);
        }
    }


    //克隆一个OkHttpClient,用于监听下载进度
    private static OkHttpClient clone(final ProgressCallback progressCallback) {
        return mOkHttpClient.newBuilder().addNetworkInterceptor(chain -> {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressCallback)).build();
        }).build();
    }

    //异步执行请求
    private static void enqueue(OkHttpClient okHttpClient, Request request, Callback callback) {
        if (callback == null) {
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        } else {
            okHttpClient.newCall(request).enqueue(callback);
        }
    }

    //同步执行请求
    private static void execute(OkHttpClient okHttpClient, Request request, Callback callback) {
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (callback != null) {
                callback.onResponse(call, response);
            }
        } catch (IOException e) {
            if (callback != null) {
                callback.onFailure(call, e);
            }
        }
    }

}

