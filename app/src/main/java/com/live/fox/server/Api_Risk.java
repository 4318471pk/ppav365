package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.common.JsonCallback;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.live.fox.Constant;
import com.live.fox.manager.SPManager;

import java.util.HashMap;


public class Api_Risk extends BaseApi{
    private Api_Risk() {
    }

    private static class InstanceHolder {
        private static Api_Risk instance = new Api_Risk();
    }

    public static Api_Risk ins() {
        return Api_Risk.InstanceHolder.instance;
    }

    public static final String blackUser = "blackUser";


    /**
     * 封号、封终端
     * type 1账号、2终端
     */
    public void blackUser(long uid, int type, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Risk_blackUser_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);
        params.put("type", type);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


}
