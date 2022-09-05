package com.live.fox.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;


public class GsonUtil {

    //使用Gson进行解析普通对象
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    //GsonUtil.getObject(result, Question.class);
    public static <T> T getObject(String jsonString, Type type) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    //
    // 使用Gson进行解析List对象 GsonUtil.getObjects(json, HomeTab[].class)
    // GsonUtil.getObjects(new JSONObject(data).opt("pingList").toString(), PingList[].class);
    public static <T> ArrayList<T> getObjects(String s, Class<T[]> clazz) {
        ArrayList<T> ts = new ArrayList<>();
        try {
            T[] arr = new Gson().fromJson(s, clazz);
            ts.addAll(Arrays.asList(arr));
        } catch (Exception ignore) {
        }
        return ts;
    }
}