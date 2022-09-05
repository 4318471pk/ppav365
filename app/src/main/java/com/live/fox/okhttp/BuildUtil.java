package com.live.fox.okhttp;

import android.util.Log;


import java.io.File;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * User: ljx
 * Date: 2017/12/1
 * Time: 18:36
 */
class BuildUtil {

    private static final MediaType MEDIA_TYPE_ATTACH = MediaType.parse("application/octet-stream");


    //构建一个get请求
    static Request getRequest(RequestParam param) {
        return getRequest(param.getFullUrl(), param.getHeaders());
    }

    //构建一个get请求
    static Request getRequest(String url) {
        return getRequest(url, null);
    }

    //构建一个get请求
    static Request getRequest(String url, Headers headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null)
            builder.headers(headers);
        return builder.get().build();
    }

    //构建一个post请求
    static Request postRequest(String url, Headers headers, RequestBody requestBody) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null)
            builder.headers(headers);
        if (requestBody != null)
            builder.post(requestBody);
        return builder.build();
    }


    static RequestBody postRequestBody(RequestParam param) {
        return param.hasFile() ? postRequestBody(param, param.getFileMap())
                : postRequestBody((Map<String, String>) param);
    }



    //构建一个表单,用于post请求
    private static RequestBody postRequestBody(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        Set<Map.Entry<String, String>> sets = params.entrySet();
        for (Map.Entry<String, String> entry : sets) {
            Log.e(entry.getKey(), entry.getValue());
            builder.add(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    //构建一个表单,用于post请求(带文件)
    private static RequestBody postRequestBody(Map<String, String> params, Map<String, File> fileMap) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        //遍历参数
        Set<Map.Entry<String, String>> sets = params.entrySet();
        for (Map.Entry<String, String> entry : sets) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        //遍历文件
        Set<Map.Entry<String, File>> fileSets = fileMap.entrySet();
        for (Map.Entry<String, File> entry : fileSets) {
            File file = entry.getValue();
            if (!file.exists() || !file.isFile()) continue;
            builder.addFormDataPart(entry.getKey(), file.getName(), RequestBody.create(MEDIA_TYPE_ATTACH, file));
        }
        return builder.build();
    }
}
