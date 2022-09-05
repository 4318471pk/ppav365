package com.live.fox.network;

import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.live.fox.common.CommonApp;
import com.live.fox.entity.User;
import com.live.fox.utils.DeviceIdUtils;
import com.live.fox.utils.AppUserManger;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求工厂类
 */
public class NetworkFactory {

    private static final long TIME_OUT = 30;
    private static final Logger sLogger = Logger.getLogger("NetworkFactory");
    private static ApiService apiService;

    private NetworkFactory() {

    }


    public static final class Network {

    }


    private static class SingletonInstance {
        private static final NetworkFactory INSTANCE = new NetworkFactory();
    }

    public static NetworkFactory getInstance() {
        return SingletonInstance.INSTANCE;
    }

    /**
     * 初始换网络请求
     *
     * @param baseurl 项目地址
     */
    public ApiService initNetwork(String baseurl) {
        OkHttpClient sOkHttpClient = getClient();
        if (apiService == null) {
            apiService = new Retrofit.Builder()
                    .baseUrl(baseurl)
                    .addConverterFactory(GsonConverterFactory.create(getGonBuild()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(sOkHttpClient)
                    .build()
                    .create(ApiService.class);
        }
        return apiService;
    }

    private static Gson getGonBuild() {
        return new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();
    }

    private static OkHttpClient getClient() {
        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request();
            Request requestBuild;

            if (AppUserManger.isLogin()) {
                User user = AppUserManger.getUserInfo();
                String uid = String.valueOf(user.getUid());
                requestBuild = request.newBuilder()
                        .header("user_id", TextUtils.isEmpty(uid) ? "" : uid)
                        .header("token", TextUtils.isEmpty(user.getImToken()) ? "" : user.getImToken())
                        .header("device_id", DeviceIdUtils.getAndroidId(CommonApp.getInstance()))
                        .method(request.method(), request.body())
                        .build();
            } else {
                requestBuild = request.newBuilder()
                        .header("device_id", DeviceIdUtils.getAndroidId(CommonApp.getInstance()))
                        .method(request.method(), request.body())
                        .build();
            }
            return chain.proceed(requestBuild);
        }).addInterceptor(getHttpLoggingInterceptor())
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 打印请求参数
     */
    private static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor("");
    }

}
