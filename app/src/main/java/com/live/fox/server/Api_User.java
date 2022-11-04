package com.live.fox.server;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.BankInfo;
import com.live.fox.entity.BankInfoList;
import com.live.fox.entity.FilterItemEntity;
import com.live.fox.entity.Letter;
import com.live.fox.entity.Noble;
import com.live.fox.entity.RuleBean;
import com.live.fox.entity.TransactionEntity;
import com.live.fox.entity.User;
import com.live.fox.entity.UserAssetRecord;
import com.live.fox.entity.Withdraw;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.manager.AppUserManger;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Api_User extends BaseApi {
    private Api_User() {
    }

    private static class InstanceHolder {
        private static Api_User instance = new Api_User();
    }

    public static Api_User ins() {
        return InstanceHolder.instance;
    }


    /**
     * 用户信息
     */
    public void getUserInfo(long uid, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_userInfo_URL;
        HashMap<String, Object> params = getCommonParams();
        if (uid >= 0) params.put("uid", uid);

        OkGoHttpUtil.getInstance().doJsonPost(
                url,
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        if (code == 0 && !TextUtils.isEmpty(data)) {
                            DataCenter.getInstance().getUserInfo().setUser(data);
                        }

                        if(callback!=null)
                        {
                            callback.onSuccess(code,msg,data);
                        }
                    }
                });
    }

    /**
     * 用户信息
     */
    public void getBaseInfoWithToken( JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.BaseInfoWithToken;
        HashMap<String, Object> params = getCommonParams();

        OkGoHttpUtil.getInstance().doGet(
                url,
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())))
                .execute(callback);


//        OkGoHttpUtil.getInstance().doJsonPost(
//                url,
//                url,
//                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
//                new Gson().toJson(params))
//                .execute(callback);
    }

    /**
     * 获取用户名片信息
     */
    public void getCardInfoData(long uid, int liveId, long anchorId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_cardInfo_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);
        if (liveId != 0) {
            params.put("liveId", liveId);
        }
        params.put("anchorId", anchorId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 用户认证
     */
    public void userAuth(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_auth_URL;
        HashMap<String, Object> params = getCommonParams();

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 修改用户信息
     * type 1修改头像 2修改昵称 3修改性别 4修改签名,5感情状态 6生日 7职业
     */
    public void modifyUserInfo(User user, int type, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_modifyuserinfo_URL;
        if (type == 2) {
            url = SPManager.getServerDomain() + Constant.URL.USER_modifyusername_URL;
        }
        HashMap<String, Object> params = getCommonParams();
        switch (type) {
            case 1: //修改头像
                if (!StringUtils.isEmpty(user.getAvatar())) params.put("avatar", user.getAvatar());
                break;
            case 2: //修改昵称
                if (!StringUtils.isEmpty(user.getNickname()))
                    params.put("nickname", user.getNickname());
                break;
            case 3: //修改性别
                if (user.getSex() >= 0) params.put("sex", user.getSex());
                break;
            case 4: //修改签名
                if (!StringUtils.isEmpty(user.getSignature()))
                    params.put("signature", user.getSignature());
                break;
            case 5: //感情状况：
                    params.put("emotionalState", user.getEmotionalState());
                break;
            case 6: //生日：
                if (user.getBirthday() != null ) {
                    params.put("birthday", user.getBirthday());
                }
            case 7: //职业
                if (user.getJob() != null ) {
                    params.put("job", user.getJob());
                }
                break;
        }

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);;
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 关注列表
     */
    public void getFollowList(int start, int page, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_followlist_URL;
        HashMap<String, Object> params = getCommonParams();
//        params.put("start", start);
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
     * 关注
     */
    public void followUser(String targetId, boolean isFollow, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_follow_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("isFollow", isFollow);
        params.put("targetId", targetId);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 粉丝列表
     */
    public void getFansList(int start, int page, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_fanslist_URL;
        HashMap<String, Object> params = getCommonParams();
//        params.put("start", start);
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
     * 主播明细
     */
    public void getAncList(int page, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_subsidylist_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("page", page);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 关注/取关
     */
    public void follow(long targetId, boolean isFollow, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.LIVE_follow_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("targetId", targetId);
        params.put("isFollow", isFollow);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 黑名单列表
     */
    public void getBlackList(int start, int page, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_rejectlist_URL;
        HashMap<String, Object> params = getCommonParams();
//        params.put("start", start);
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
     * 设置/取消 黑名单
     */
    public void setBlack(long uid, boolean isReject, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_reject_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);
        params.put("isReject", isReject);

        //OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 购买座驾
     */
    public void buyCar(int gid, int days, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_propbuyCar_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("gid", gid);
        params.put("days", days);

        //OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取用户座驾
     */
    public void getUserCar(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_propcar_URL;
        HashMap<String, Object> params = getCommonParams();
        //OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 设置直播间展示座驾
     */
    public void setShowCar(int gid, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_propsetShowCar_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("gid", gid);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 刷新用户游戏额度
     */
    public void refreshGameMoney(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_game_URL;
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
     * 查询玩家流水
     */
    public void getStatement(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_statement_URL;
        HashMap<String, Object> params = getCommonParams();

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 用户提现
     * 1银行卡提现 2支付宝提现
     * 1金币提现 2魅力提现
     */
    public void withdraw(long cardId, int cardType, long cash, String cashPassword, int type,
                         JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_withdraw_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("cardId", cardId);
        params.put("cardType", cardType);
        params.put("cash", cash);
        params.put("cashPassword", cashPassword);
        params.put("type", type);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 提现记录列表
     * page 查询分页号(一页10条)
     * withdrawType 提现指定类型 1 金币提现 2 魅力提现
     */
    public void withdrawList(int page, int withdrawType, JsonCallback<List<Withdraw>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_withdrawlist_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("page", page);
        params.put("withdrawType", withdrawType);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 家族提现
     * page 查询分页号(一页10条)
     * withdrawType 提现指定类型 1 金币提现 2 魅力提现
     */
    public void jzList(int page, JsonCallback<List<Withdraw>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_jzlist_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("page", page);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取私信列表
     * localId 本地私信ID
     */
    public void getLetterList(long localId, JsonCallback<List<Letter>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.LIVE_letterlist_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("localId", localId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 发送私信
     */
    public void sendLetter(long destUid, String content, JsonCallback<Integer> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.LIVE_letter_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("destUid", destUid);
        params.put("content", content);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 私信已读
     */
    public void readLetter(long localId, JsonCallback<Integer> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.READ_letter_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("localId", localId);
        params.put("statua", 1);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 删除私信
     */
    public void deleteLetter(long localId, int statua, JsonCallback<Integer> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.DEL_MESSAGE_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("letterId", localId);
        params.put("statua", statua);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 删除私信
     */
    public void deleteLetter1(long localId, int statua, JsonCallback<Integer> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.DEL_MESSAGE_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("localId", localId);
        params.put("statua", statua);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取官方公告列表
     */
    public void getGfNotice(JsonCallback<List<Letter>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Config_gfnotice_URL;
        callback.setUrlTag("systemLetter");
        HashMap<String, Object> params = getCommonParams();
        OkGoHttpUtil.getInstance().doJsonPost(
                "", url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 得到用户绑定银行卡信息
     */
    public void getBankInfo(JsonCallback<BankInfo> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_getbankinfo_URL;
        HashMap<String, Object> params = getCommonParams();
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void getCardInfo(JsonCallback callback) {//// 获取用户已绑定银行卡信息
        String url = SPManager.getServerDomain() + Constant.URL.USER_getcardinfo_URL;
        HashMap<String, Object> params = getCommonParams();
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void changeCard(String cardNo, JsonCallback callback) {//// 获取用户已绑定银行卡信息
        String url = SPManager.getServerDomain() + Constant.URL.CHANGE_BANKCARD_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("cardNo", cardNo);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 得到银行信息
     */
    public void getBankList(JsonCallback<List<BankInfoList>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_getbanklist_URL;
        HashMap<String, Object> params = getCommonParams();
        callback.setUrlTag("userBankList");
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 绑定银行卡
     */
    public void bindBank(String cardNo, String bankName, String bankCode, String trueName,
                         String bankCity, String bankProvince, String bankSub, String mobile,
                         String vcode, String cashPassword, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_bindbank_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("cardNo", cardNo);
        params.put("bankName", bankName);
        params.put("bankCode", bankCode);
        params.put("trueName", trueName);
        params.put("bankCity", bankCity);
        params.put("bankProvince", "unknow");
        params.put("bankSub", "unknow");
        params.put("mobile", mobile);
        params.put("phoneCode", vcode);
        params.put("cashPassword", cashPassword);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 魅力值兑换金币
     */
    public void changeCoin(long anchorCoin, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_changecoin_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("anchorCoin", anchorCoin);

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 魅力值兑换金币记录
     */
    public void getChangeCoinList(int page, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_changecoinlist_URL;
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
     * 搜索用户
     */
    public void searchUser(String content, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_find_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("content", content);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 搜索主播
     */
    public void searchAnchor(Long uid, String anchorNickName, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.anchorfind_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);
//        params.put("startTime",startTime);
//        params.put("endTime",endTime);
//        params.put("anchorUid",anchorUid);
        params.put("anchorNickName", anchorNickName);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void searchAnchor2(Long uid, Long startTime, Long endTime, int page, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.anchorfind_URL2;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("page", page);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    public void getRule(int type, JsonCallback<RuleBean> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_getrule_URL + "?type=" + type;
        HashMap<String, Object> params = getCommonParams();

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 设置用户金币自动转换 1 不自动 2自动
     */
    public void autoUpdownBalance(int autoUpdownBalance, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_autoUpdownBalance_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("autoUpdownBalance", autoUpdownBalance);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 一键回收所有用户游戏金币
     */
    public void backAllGameCoin(long uid, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_backAllGameCoin_URL;
        HashMap<String, Object> params = getCommonParams();
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    public void getVipInnfo(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_VIPINFO_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", DataCenter.getInstance().getUserInfo().getUser().getUid());

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void doVipHide(Noble noble, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_VIPHIDE_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", DataCenter.getInstance().getUserInfo().getUser().getUid());
        params.put("chatHide", noble.getChatHide());
        params.put("group", noble.getGroup());
        params.put("rankHide", noble.getRankHide());
        params.put("roomHide", noble.getRoomHide());

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void doBuyVip(int levelId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_VIPBUY_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("levelId", levelId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void doVipUp(int levelId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_VIPUP_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("levelId", levelId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void doReNewVip(int levelId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.USER_RENEWVIP_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("levelId", levelId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void doVipList(long liveId, int os, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.LIVE_VIPLIST_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("os", os);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取过滤数据
     *
     * @param callback 请求结果回调
     */
    public void requestAssetType(JsonCallback<List<FilterItemEntity>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.ASSET_TYPE;
        HashMap<String, Object> params = getCommonParams();
        Object timestamp = params.get("timestamp");
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(timestamp))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取过滤数据
     *
     * @param callback 请求结果回调
     */
    public void requestAssetList(UserAssetRecord userAsset, JsonCallback<TransactionEntity> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.ASSET_TYPE_DETAIL;
        callback.setUrlTag("/asset/record");
        HashMap<String, Object> params = getCommonParams();
        params.put("page", userAsset.getPage());
        params.put("timeType", userAsset.getTimeType());
        params.put("type", userAsset.getType());

        Object timestamp = params.get("timestamp");
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(timestamp))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 设置支付密码
     *
     * @param callback 请求结果回调
     */
    public void setPaymentPassword(String payPwd,String payPwdTwo,String codeWord, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.setPaymentPassword;
        callback.setUrlTag(Constant.URL.setPaymentPassword);
        HashMap<String, Object> params = getCommonParams();
        params.put("payPwd", payPwd);
        params.put("payPwdTwo", payPwdTwo);
        params.put("codeWord", codeWord);

        Object timestamp = params.get("timestamp");
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(timestamp))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 重置支付密码
     *
     * @param callback 请求结果回调
     */
    public void resetPaymentPassword(String area,String mobile,String vcode,String password,String password2,String codeWord, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.resetPaymentPassword;
        callback.setUrlTag(Constant.URL.resetPaymentPassword);
        HashMap<String, Object> params = getCommonParams();
        params.put("area", area);
        params.put("mobile", mobile);
        params.put("vcode", vcode);
        params.put("password", password);
        params.put("password2", password2);
        params.put("codeWord", codeWord);

        Object timestamp = params.get("timestamp");
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(timestamp))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 修改支付密码
     *
     * @param callback 请求结果回调
     */
    public void modifyPaymentPassword(String oldPwd,String payPwd,String payPwdTwo, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.modifyPaymentPassword;
        callback.setUrlTag(Constant.URL.modifyPaymentPassword);
        HashMap<String, Object> params = getCommonParams();
        params.put("oldPwd", oldPwd);
        params.put("payPwd", payPwd);
        params.put("payPwdTwo", payPwdTwo);

        Object timestamp = params.get("timestamp");
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(timestamp))),
                new Gson().toJson(params))
                .execute(callback);
    }


    public void uploadUserPhoto(File file, JsonCallback<String> callback ){
        String url = SPManager.getServerDomain() + Constant.URL.UPLOAD_USER_PGOTO_URL;
        OkGo.<String>post(url)
                .tag(this)
                .headers(getCommonHeaders(System.currentTimeMillis()))
                .params("myFile", file)
                .isMultipart(true)
                .execute(callback);

    }

}
