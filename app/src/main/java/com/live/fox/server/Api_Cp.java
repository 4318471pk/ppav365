package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;


public class Api_Cp extends BaseApi {
    private Api_Cp() {
    }

    private static class InstanceHolder {
        private static final Api_Cp instance = new Api_Cp();
    }

    public static Api_Cp ins() {
        return Api_Cp.InstanceHolder.instance;
    }

    public static final String login = "login";

    /**
     * 获取cph5地址
     */
    public void geth5url(String gameId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.CP_geth5url;
        HashMap<String, Object> params = getCommonParams();
        params.put("lotteryName", gameId);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获得红包
     */
    public void getRedBagRain(int redPacketId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_RED_BAG_GET;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", AppUserManger.getUserInfo().getUid());
        params.put("redPacketId", redPacketId);
        url = OkGoHttpUtil.getInstance().mapToUrlWithValue(url, params);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获得红包雨活动
     */
    public void getRedBagRainInfo(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_RED_BAG_RAIN_INFO;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", AppUserManger.getUserInfo().getUid());
        url = OkGoHttpUtil.getInstance().mapToUrlWithValue(url, params);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获得cp期号
     */
    public void getCp(String Name, JsonCallback<GamePeriodInfoVO> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.CP_GETISSUE;
        HashMap<String, Object> params = getCommonParams();
        params.put("name", Name);
        callback.setUrlTag(url);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获得hn开奖结果
     */
    public void gethn(String name, String expect, JsonCallback<String[]> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.CP_GETHNLOTTERY;
        HashMap<String, Object> params = getCommonParams();
        params.put("name", name);
        params.put("issue", expect);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    //获取所有彩票最近一期开奖结果
    public void getLatestResult(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.CPLatestResult;
        HashMap<String, Object> params = getCommonParams();
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    //获取快三结果
    public void getKSResult(String lotteryName, int page, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.ResultHistoryByName;
        HashMap<String, Object> params = getCommonParams();
        params.put("lotteryName", lotteryName);
        params.put("page", page);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    //获取个人投注记录
    public void getMyTZResult(Long uid, int page, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.BetHistorByUid;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);
        params.put("page", page);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    //直播间设置彩票弹框
    public void getPushResultMsgFlag(Long liveId, int flag, String lotteryNames, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.pushResultMsgFlag;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("flag", flag);
        params.put("lotteryNames", lotteryNames);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    //获取彩票开奖历史
    public void getLotteryResultHistory(Long uid, int type, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.lotteryResultHistory;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);
        params.put("type", type);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void getLotteryResultHistoryByName(int queryType, Long uid, String lotteryName, int type, int page, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.lotteryResultHistoryByName;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);
        params.put("page", page);
        params.put("type", type);
        params.put("queryType", queryType);
        params.put("lotteryName", lotteryName);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }
}

