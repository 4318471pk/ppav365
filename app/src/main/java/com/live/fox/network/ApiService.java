package com.live.fox.network;


import com.google.gson.JsonObject;
import com.live.fox.entity.BaseInfo;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 获取项目基础配置
     *
     * @return 返回配置信息
     * @deviceType 配置信息
     */
    @GET("config-client/base/baseInfo")
    Call<JsonObject> getConfigInfo(@Query("os") String os);

}
