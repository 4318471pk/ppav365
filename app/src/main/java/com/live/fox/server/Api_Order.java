package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.entity.BankInfo;
import com.live.fox.entity.ChargeCoinBean;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.entity.response.BankRechargeVO;
import com.live.fox.manager.SPManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Api_Order extends BaseApi {
    private Api_Order() {
    }

    private static class InstanceHolder {
        private static Api_Order instance = new Api_Order();
    }

    public static Api_Order ins() {
        return Api_Order.InstanceHolder.instance;
    }

    public static final String getVipChannel = "getVipChannel";

    /**
     * vip充值通道列表
     */
    public void getVipChannel(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Order_payvipchannel_URL;
        callback.setUrlTag("vip/channel");
        HashMap<String, Object> params = getCommonParams();

        OkGoHttpUtil.getInstance().doJsonPost(
                        "",
                        url,
                        getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                        new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 转账银行卡信息
     */
    public void doBankCardInfo(JsonCallback<List<BankInfo>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.ORDER_BANK_URL;
        callback.setUrlTag("/pay/bank/list1");
        HashMap<String, Object> params = getCommonParams();
        OkGoHttpUtil.getInstance().doJsonPost(
                        "",
                        url,
                        getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                        null)
                .execute(callback);
    }

    /**
     * 银行卡或者usdt充值
     */
    public void doBankRecharge(JsonCallback<BankRechargeVO> callback, Map<String, Object> params, int type) {
        String url;
        if (type == 29) {
            url = SPManager.getServerDomain() + Constant.URL.ORDER_USDT_RECHARGE_URL;
        } else {
            url = SPManager.getServerDomain() + Constant.URL.ORDER_BANK_RECHARGE_URL;
        }

        OkGoHttpUtil.getInstance().doJsonPost(
                        "",
                        url,
                        getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                        new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 用户资产
     */
    public void getUserLiveUserAssets(JsonCallback<UserAssetsBean> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_liveUserAssets;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 充值中心-充值列表
     */
    public void getChargeCoin(JsonCallback<ChargeCoinBean> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_chargeCenter;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


}
