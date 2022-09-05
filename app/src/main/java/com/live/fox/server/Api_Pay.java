package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;


public class Api_Pay extends BaseApi {
    private Api_Pay() {
    }

    private static class InstanceHolder {
        private static Api_Pay instance = new Api_Pay();
    }

    public static Api_Pay ins() {
        return Api_Pay.InstanceHolder.instance;
    }

    public static final String getGame = "getGame";

    /**
     * 直播APP调起游戏大厅
     * model 1=直播间进来，半屏模式；2－全屏模式，娱乐版
     */
    public void getGame(String uid, String nick, String gameId, int model, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Pay_getgame_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);
        params.put("nick", nick);
        if (!StringUtils.isEmpty(gameId)) params.put("gameId", gameId);
        params.put("model", model); //

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 通知玩家退出
     */
    public void kickout(String uid, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Pay_kickout_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("kickAll", false);
        params.put("text", "退出");
        params.put("type", 1);
        params.put("uid", uid);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 投注流水排行
     */
    public void betRank(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Pay_betRank_URL;
        HashMap<String, Object> params = getCommonParams();

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 投注流水排行详情
     */
    public void rankDetail(int statType, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Pay_rankdetail_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("statType", statType);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 四方支付
     */
    public void payFour(String url, String code, String supportBank, JsonCallback<String> callback) {
        HashMap<String, Object> params = getCommonParams();
        params.put("code", code);
        params.put("supportBank", supportBank);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void payFoura(String url, long TrueRmb, long sectionGold, JsonCallback callback) {
        HashMap<String, Object> params = getCommonParams();
        params.put("code", 1);
        params.put("trueRmb", TrueRmb);
        params.put("sectionGold", sectionGold);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void payFoura(String url, String amount, String type, String serial, String pin, JsonCallback callback) {
        HashMap<String, Object> params = getCommonParams();
        params.put("amount", amount);
        params.put("type", type);
        params.put("serial", serial);
        params.put("pin", pin);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }
}
