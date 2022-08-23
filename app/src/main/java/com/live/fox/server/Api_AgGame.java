package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;


public class Api_AgGame extends BaseApi {
    private Api_AgGame() {
    }

    private static class InstanceHolder {
        private static Api_AgGame instance = new Api_AgGame();
    }

    public static Api_AgGame ins() {
        return Api_AgGame.InstanceHolder.instance;
    }

    public static final String forwardGame = "forwardGame";

    /**
     * 登录游戏
     * gameType 游戏id 进入大厅可以不传该参数
     */
    public void forwardGame(String gameType, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AgGame_forwardGame_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("gameType", gameType);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 退出游戏
     * kindId 游戏id 进入大厅可以不传该参数
     */
    public void logout(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AgGame_logout_URL;
        HashMap<String, Object> params = getCommonParams();

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

//    /**
//     * 查询玩家总分
//     */
//    public void getUserSource(JsonCallback callback) {
//        String url = SPManager.getServerDomain() + Constant.URL.AgGame_userinfo_URL;
//        HashMap<String, Object> params = getCommonParams();
//
////        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
//        OkGoHttpUtil.getInstance().doJsonPost(
//                "",
//                url,
//                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
//                new Gson().toJson(params))
//                .execute(callback);
//    }

    /**
     * 查询玩家总分
     */
    public void getUserBalance(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AgGame_getbalance_URL;
        HashMap<String, Object> params = getCommonParams();

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 上分
     */
    public void balanceUp(long money, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AgGame_balanceIn_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("money", money);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders6(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 下分
     */
    public void balanceDown(long money, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AgGame_balanceOut_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("money", money);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders6(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

}
