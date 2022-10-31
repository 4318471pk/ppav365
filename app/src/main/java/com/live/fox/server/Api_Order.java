package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.entity.BagAndStoreBean;
import com.live.fox.entity.BankInfo;
import com.live.fox.entity.BankListBean;
import com.live.fox.entity.ChargeCoinBean;
import com.live.fox.entity.DiamondListBean;
import com.live.fox.entity.RechargeTypeBean;
import com.live.fox.entity.RechargeTypeListBean;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.entity.UserBankBean;
import com.live.fox.entity.WithDrawRecordBean;
import com.live.fox.entity.WithdrawChannelTypeBean;
import com.live.fox.entity.response.BankRechargeVO;
import com.live.fox.language.MultiLanguageUtils;
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
     * 用戶绑定的银行卡信息
     */
    public void getUserBank(JsonCallback<UserBankBean> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_userBankInfo;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 用戶绑定的银行卡信息列表
     */
    public void getUserBankList(JsonCallback<List<UserBankBean>> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_userBankList;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 用戶添加银行卡
     */
    public void getUserAddBank(JsonCallback<String> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_userAddBank;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取银行卡列表
     */
    public void getBankList(JsonCallback<List<BankListBean>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_bankList
                + "?language=" + MultiLanguageUtils.getRequestHeader();
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 用戶删除银行卡
     */
    public void deleteBank(JsonCallback<String> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_deleteBank;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 用戶设置默认银行卡
     */
    public void setMorenBank(JsonCallback<String> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_setNorenBank;
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

    /**
     * 充值中心-充值列表2
     */
    public void getChargeList(JsonCallback<List<RechargeTypeListBean>> callback, int type) { //
        String url = SPManager.getServerDomain() + Constant.URL.USER_chargeCenter_list
                + "?language=" + MultiLanguageUtils.getRequestHeader() + "&type=" + type ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 充值中心-充值方式
     */
    public void getChargeType(JsonCallback<List<RechargeTypeBean>> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_chargeCenter_type + "?language=" + MultiLanguageUtils.getRequestHeader();
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())))
                .execute(callback);
    }


    /**
     * 提现
     */
    public void getWithDrawType(JsonCallback<List<WithdrawChannelTypeBean>> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_withDraw_type+ "?language=" + MultiLanguageUtils.getRequestHeader();
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())))
                .execute(callback);
    }

    /**
     * 提现列表2
     */
    public void getWithDrawList(JsonCallback<String> callback, long type) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_withDraw_list
                + "?language=" + MultiLanguageUtils.getRequestHeader() + "&type=" + type ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 用戶提现
     */
    public void getWithDraw(JsonCallback<String> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_withDraw;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取资产信息
     */
    public void getAssets(JsonCallback<UserAssetsBean> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_AssetsInfo;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);

    }

    /**
     * 充值中心-钻石兑换列表
     */
    public void getDiamondList(JsonCallback<List<DiamondListBean>> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_diamondList + "?language=" + MultiLanguageUtils.getRequestHeader();
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())))
                .execute(callback);
    }

    /**
     * 兑换钻石
     */
    public void getDiamondExchange(JsonCallback<String> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_diamondExchange;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);

    }


    /**
     * 提现记录
     */
    public void getWithDrawRecord(JsonCallback<WithDrawRecordBean> callback, int pageNum, int pageSize) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_withDrawRecord + "?language=" +
                MultiLanguageUtils.getRequestHeader()+ "&pageNum=" + pageNum + "&pageSize=" + pageSize ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }



    /**
     * 钻石收入记录
     */
    public void getDiamondRecord(JsonCallback<WithDrawRecordBean> callback, int pageNum, int pageSize) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_diamondRecord + "?language=" +
                MultiLanguageUtils.getRequestHeader()+ "&pageNum=" + pageNum + "&pageSize=" + pageSize ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }


    /**
     * 背包列表
     */
    public void getBagList(JsonCallback<String> callback, int pageNum, int pageSize) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_bagList
                + "?language=" + MultiLanguageUtils.getRequestHeader() + "&pageNum=" + pageNum + "&pageSize=" + pageSize ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 商店列表
     */
    public void getStoreList(JsonCallback<BagAndStoreBean> callback, int pageNum, int pageSize) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_userStoreList
                + "?language=" + MultiLanguageUtils.getRequestHeader() + "&pageNum=" + pageNum + "&pageSize=" + pageSize ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }


    /**
     * 购买座驾
     */
    public void buyCar(JsonCallback<String> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_buyCar;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);

    }

}
