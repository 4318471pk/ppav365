package com.live.fox.network;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryInterceptor implements Interceptor {

    int maxRequest;
    int retryCount;

    public RetryInterceptor(int maxRequest) {
        this.maxRequest = maxRequest;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request=chain.request();
        Response response=chain.proceed(request);
        while (!response.isSuccessful() && retryCount<maxRequest)
        {
            retryCount++;
            response=chain.proceed(request);
        }
        return response;
    }
}
