package com.live.fox.server;

import static com.live.fox.Constant.URL.Saba_get_balance;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;
import java.util.Objects;


public class Api_KyGame extends BaseApi {
    private Api_KyGame() {
    }

    private static class InstanceHolder {
        private static Api_KyGame instance = new Api_KyGame();
    }

    public static Api_KyGame ins() {
        return Api_KyGame.InstanceHolder.instance;
    }

    public static final String login = "login";

    /**
     * 登录游戏
     * kindId 游戏id 进入大厅可以不传该参数
     */
    public void login(String kindId, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.KyGame_login_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("kindId", kindId);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
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
        String url = SPManager.getServerDomain() + Constant.URL.KyGame_logout_URL;
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
        String url = SPManager.getServerDomain() + Constant.URL.KyGame_balance_URL;
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
     *
     * @param type     游戏类型
     * @param callback 接口回掉
     */
    public void requestBalance(int type, JsonCallback<String> callback) {
        String urlBalance = "";
        switch (type) {
            case 1:

                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;

            case 6:
                break;

            case 7: //saba
                urlBalance = Saba_get_balance;
                break;
        }

        String url = SPManager.getServerDomain() + urlBalance;
        HashMap<String, Object> params = getCommonParams();

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(Objects.requireNonNull(params.get("timestamp")).toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 转出
     *
     * @param money    金额
     * @param callback 接口回掉
     */
    public void transferOut(int type, long money, JsonCallback<String> callback) {
        String gameUrl = "";
        switch (type) {
            case 1:
                gameUrl = Constant.URL.KyGame_balanceDown_URL;
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;

            case 6:
                break;

            case 7: //saba
                gameUrl = Constant.URL.Saba_balanceOut_URL;
                break;
        }

        String url = SPManager.getServerDomain() + gameUrl;
        HashMap<String, Object> params = getCommonParams();
        params.put("money", money);
        callback.setUrlTag("transferOut");
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders6(Long.parseLong(Objects.requireNonNull(params.get("timestamp")).toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 转入
     *
     * @param money    金额
     * @param callback 接口回掉
     */
    public void transferIn(int type, long money, JsonCallback<String> callback) {
        String gameUrl = "";
        switch (type) {
            case 1:
                gameUrl = Constant.URL.KyGame_balanceUp_URL;
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;

            case 6:
                break;

            case 7: //saba
                gameUrl = Constant.URL.Saba_balanceIn_URL;
                break;
        }

        String url = SPManager.getServerDomain() + gameUrl;
        HashMap<String, Object> params = getCommonParams();
        params.put("money", money);
        callback.setUrlTag("transferIn");
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders6(Long.parseLong(Objects.requireNonNull(params.get("timestamp")).toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 上分
     */
    public void balanceUp(long money, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.KyGame_balanceUp_URL;
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
        String url = SPManager.getServerDomain() + Constant.URL.KyGame_balanceDown_URL;
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
