package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.common.JsonCallback;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.live.fox.Constant;
import com.live.fox.manager.SPManager;

import java.util.HashMap;


//授权相关接口
public class Api_Rank extends BaseApi{
    private Api_Rank() {
    }

    private static class InstanceHolder {
        private static Api_Rank instance = new Api_Rank();
    }

    public static Api_Rank ins() {
        return InstanceHolder.instance;
    }

    public static final String getRankList = "getRankList";

    /**
     * 获取榜单列表
     * 榜单类型：1:主播榜；2:土豪榜(用户送礼贡献榜单);
     */
    public void getRankList(int type, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.RANK_list_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("type", type);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 主播个人榜单（贡献榜）
     *
     */
    public void getContributionRankList(String liveId, String uid, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.RANK_anchorlist_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("anchorId",uid);

        callback.setArg(liveId);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }








}
