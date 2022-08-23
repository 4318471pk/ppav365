package com.live.fox.mvp;

import com.google.gson.Gson;
import com.live.fox.server.BaseApi;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.lzy.okgo.callback.Callback;

import java.util.Map;


public abstract class AbsBaseModel extends BaseApi {
    protected void doPost(Map params, String url, Callback callback) {
        OkGoHttpUtil.getInstance().doJsonPost("", url, getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params)).execute(callback);
    }

    protected void doPost6(Map params, String url, Callback callback) {
        OkGoHttpUtil.getInstance().doJsonPost("", url, getCommonHeaders6(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params)).execute(callback);
    }

}
