package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.entity.ActBean;
import com.live.fox.entity.AvailableGuardBean;
import com.live.fox.entity.BagAndStoreBean;
import com.live.fox.entity.BankInfo;
import com.live.fox.entity.BankListBean;
import com.live.fox.entity.ChargeCoinBean;
import com.live.fox.entity.DiamondListBean;
import com.live.fox.entity.DiamondRecordBean;
import com.live.fox.entity.NobleListBean;
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
import com.lzy.okgo.model.HttpParams;

import org.json.JSONObject;

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
    public void getDiamondGetRecord(JsonCallback<DiamondRecordBean> callback, int pageNum, int pageSize, String date) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_diamondRecord + "?language=" +
                MultiLanguageUtils.getRequestHeader()+ "&pageNum=" + pageNum + "&pageSize=" + pageSize + "&timeStr=" + date ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 钻石支出记录
     */
    public void getDiamondOutRecord(JsonCallback<DiamondRecordBean> callback, int pageNum, int pageSize, String date) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_diamondRecordExpend + "?language=" +
                MultiLanguageUtils.getRequestHeader()+ "&pageNum=" + pageNum + "&pageSize=" + pageSize + "&timeStr=" + date ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 背包列表
     */
    public void getBagList(JsonCallback<BagAndStoreBean> callback, int pageNum, int pageSize) {
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




    /**
     * 启用座驾
     */
    public void openCar(JsonCallback<String> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_openCar;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);

    }

    /**
     * 禁用座驾
     */
    public void closeCar(JsonCallback<String> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_closeCar;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);

    }


    /**
     * 贵族列表
     */
    public void getNobleList(JsonCallback<List<NobleListBean>> callback) {
//        String url = SPManager.getServerDomain() + Constant.URL.USER_getNobleList
//                + "?language=" + MultiLanguageUtils.getRequestHeader();
        String url = "http://47.242.62.138:8204" + Constant.URL.USER_getNobleList
                + "?language=" + MultiLanguageUtils.getRequestHeader();
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 我的贵族
     */
    public void getMyNoble(JsonCallback<NobleListBean> callback) {
        String url =  SPManager.getServerDomain() + Constant.URL.USER_getMyNoble  //"http://47.242.62.138:8204"
                + "?language=" + MultiLanguageUtils.getRequestHeader();
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 购买续费贵族
     */
    public void buyNoble(JsonCallback<String> callback, Map<String, Object> params) {
        String url = SPManager.getServerDomain()  + Constant.URL.USER_buyNoble;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     *  根据类型查询系统公告信息(0系统公告1广告轮播2直播间公告3首页弹窗公告)
     */
    public void getNotice(JsonCallback<String> callback, int type) {
        String url =  SPManager.getServerDomain() + Constant.URL.NOTICE_URL
                + "?language=" + MultiLanguageUtils.getRequestHeader() + "&type=" + type;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     *  获取活动
     */
    public void getAct(int isGame,JsonCallback<List<ActBean>> callback) {
        String url =  SPManager.getServerDomain() + Constant.URL.ACT_URL
                + "?language=" + MultiLanguageUtils.getRequestHeader() + "&activityCategory=" + isGame;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }


    /**
     * 购买、续费守护
     */
    public void buyGuard(String aid, String liveId, AvailableGuardBean bean, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain()  + Constant.URL.GurardOpen;

        HashMap<String,Object> httpParams=getCommonParams();
        httpParams.put("guardId",bean.getId());
        httpParams.put("guardLevel",bean.getGuardLevel());
        httpParams.put("guardName",bean.getName());
        httpParams.put("aid",aid);
        httpParams.put("tags",liveId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(httpParams.get("timestamp").toString())),
                new Gson().toJson(httpParams))
                .execute(callback);
    }

    /**
     * 查询可购买守护列表
     */
    public void buyAvailableGuard(JsonCallback<List<AvailableGuardBean>> callback) {
        String url = SPManager.getServerDomain()  + Constant.URL.GurardAvailableList;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }



}
