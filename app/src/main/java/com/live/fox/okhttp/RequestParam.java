package com.live.fox.okhttp;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import okhttp3.Headers;

/**
 * 此类包含请求的url、参数、以及附件
 * User: ljx
 * Date: 2017/3/23
 * Time: 09:43
 */
public class RequestParam extends LinkedHashMap<String, String> {

    //链接地址
    private String url;
    //附件集合
    private LinkedHashMap<String, File> fileMap;
    //头部信息
    private Headers.Builder headers;

    public RequestParam() {}
    public RequestParam(String url) {
        this.url = url;
    }

    //添加头部
    public void addHeader(String key, String value) {
        if (headers == null) {
            headers = new Headers.Builder();
        }
        headers.add(key, value);
    }

    //返回带参数的完整的链接
    public String getFullUrl() {
        return mergeUrlAndParams(url);
    }

    //添加文件
    public void put(String key, File file) {
        if (fileMap == null) {
            fileMap = new LinkedHashMap<>(10);
        }
        fileMap.put(key, file);
    }


    //组合url及参数
    public String mergeUrlAndParams(String url) {
        StringBuilder builder = new StringBuilder();
        if (size() == 0) {
            builder.append(url);
        } else {
            builder.append(url).append("?");
            Iterator<Entry<String, String>> iterator = entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> entry = iterator.next();
                builder.append(entry.getKey()).append("=").append(entry.getValue());
                if (iterator.hasNext())
                    builder.append("&");
            }
        }
        return builder.toString();
    }


    public boolean hasFile() {
        return fileMap != null && fileMap.size() > 0;
    }

    public HashMap<String, File> getFileMap() {
        return fileMap;
    }

    public Headers getHeaders() {
        return headers == null ? null : headers.build();
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String put(String key, int value) {
        return put(key, String.valueOf(value));
    }

    public String put(String key, long value) {
        return put(key, String.valueOf(value));
    }

    public String put(String key, double value) {
        return put(key, String.valueOf(value));
    }

    public String put(String key, float value) {
        return put(key, String.valueOf(value));
    }

    public String put(String key, boolean value) {
        return put(key, String.valueOf(value));
    }

    public String put(String key, char value) {
        return put(key, String.valueOf(value));
    }

    public String put(String key, char[] value) {
        return put(key, String.valueOf(value));
    }
}
