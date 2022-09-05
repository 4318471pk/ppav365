package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;


//分享相关接口
public class Api_Promotion extends BaseApi{
    private Api_Promotion() {
    }

    private static class InstanceHolder {
        private static Api_Promotion instance = new Api_Promotion();
    }

    public static Api_Promotion ins() {
        return InstanceHolder.instance;
    }

    public static final String installStat = "installStat";

    /**
     * 第一次安装统计
     */
    public void installStat(String puid, JsonCallback callback) {
        String url = getBaseServerDomain() + Constant.URL.Promotion_install_URL;
        HashMap<String, Object> params = getCommonParams();
        if(!StringUtils.isEmpty(puid)){
            params.put("puid", StringUtils.isEmpty(puid)? "0": puid);
        }

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 兑换金币
     * 兑换金币数量 200起提 100整数 传入的是钱 app要显示*10
     */
    public void exchange(long amount, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Promotion_exchange_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("amount", amount);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 各种余额以及分享人数(总收益,充值返利收益,邀请好友收益)
     */
    public void index(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Promotion_index_URL;
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
     * 查看邀请人记录
     */
    public void sharelog(int page, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Promotion_sharelog_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("page", page);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 分享收益提现
     * amount 提现金额 200起提 , 100的整数
     */
    public void withdraw(long amount,String cashPassword, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Promotion_withdraw_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("amount", amount);
        params.put("cashPassword",cashPassword);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 分享查看提现记录
     */
    public void withdrawlog(int page, int type, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Promotion_withdrawlog_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("page", page);
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
