package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;


public class Api_TYGame extends BaseApi {
    private Api_TYGame() {
    }

    private static class InstanceHolder {
        private static Api_TYGame instance = new Api_TYGame();
    }

    public static Api_TYGame ins() {
        return Api_TYGame.InstanceHolder.instance;
    }

    public static final String forwardGame = "forwardGame";

    /**
     * 登录游戏
     * gameType 游戏id 进入大厅可以不传该参数
     */
    public void forwardGame(String gameType, String uid, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.TYGame_forwardGame_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("gameType", gameType);
        params.put("uid", uid);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void sabaForWard(String type, String uid, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Saba_forwardGame;
        HashMap<String, Object> params = getCommonParams();
        params.put("gameType", type);
        params.put("uid", uid);
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
        String url = SPManager.getServerDomain() + Constant.URL.TYGame_logout_URL;
        HashMap<String, Object> params = getCommonParams();

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
    public void logoutSaba(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Saba_logout_URL;
        HashMap<String, Object> params = getCommonParams();

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 查询玩家总分
     */
    public void getUserBalance(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.TYGame_getbalance_URL;
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
        String url = SPManager.getServerDomain() + Constant.URL.TYGame_balanceIn_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("money", money);

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
        String url = SPManager.getServerDomain() + Constant.URL.TYGame_balanceOut_URL;
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
