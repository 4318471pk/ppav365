package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;


public class Api_FwGame extends BaseApi {
    private Api_FwGame() {
    }

    private static class InstanceHolder {
        private static Api_FwGame instance = new Api_FwGame();
    }

    public static Api_FwGame ins() {
        return InstanceHolder.instance;
    }

    public static final String login = "login";

    /**
     * 登录游戏
     * kindId 游戏id 进入大厅可以不传该参数
     */
    public void login(String kindId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.FwGame_login_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("kindId", kindId);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void loginBg(String gameType,String uid, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.BGGame_login_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("gameType", gameType);
        params.put("uid",uid);
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
        String url = SPManager.getServerDomain() + Constant.URL.FwGame_logout_URL;
        HashMap<String, Object> params = getCommonParams();
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }
    public void logoutBg(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.BgGame_logout_URL;
        HashMap<String, Object> params = getCommonParams();
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
//        String url = SPManager.getServerDomain() + Constant.URL.FwGame_userinfo_URL;
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
        String url = SPManager.getServerDomain() + Constant.URL.FwGame_balance_URL;
        HashMap<String, Object> params = getCommonParams();

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
        String url = SPManager.getServerDomain() + Constant.URL.FwGame_balanceUp_URL;
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
        String url = SPManager.getServerDomain() + Constant.URL.FwGame_balanceDown_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("money", money);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders6(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

}
