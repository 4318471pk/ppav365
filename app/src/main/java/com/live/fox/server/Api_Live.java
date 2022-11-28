package com.live.fox.server;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.AnchorGuardListBean;
import com.live.fox.entity.BlackOrMuteListItemBean;
import com.live.fox.entity.EnterRoomBean;
import com.live.fox.entity.HomeFragmentRoomListBean;
import com.live.fox.entity.LivingCurrentAnchorBean;
import com.live.fox.entity.LivingGiftBean;
import com.live.fox.entity.LivingRoomAdminListBean;
import com.live.fox.entity.OnlineUserBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.entity.SearchAnchorBean;
import com.live.fox.entity.SendGiftAmountBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.manager.AppUserManger;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;
import java.util.List;

public class Api_Live extends BaseApi {

    private Api_Live() {

    }

    private static class InstanceHolder {
        private static final Api_Live instance = new Api_Live();
    }

    public static Api_Live ins() {
        return InstanceHolder.instance;
    }

    public static final String interRoom = "interRoom";


    /**
     * 获取礼物列表 0普通礼物1守护礼物2贵族礼物
     */
    public void getGiftList(int type, JsonCallback<List<LivingGiftBean>> callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.GiftList);
        stringBuilder.append(String.format("?type=%d", type));

        String url = stringBuilder.toString();

        OkGoHttpUtil.getInstance().doGet(
                url,
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 获取礼物数量列表
     */
    public void getGiftAmountList(JsonCallback<List<SendGiftAmountBean>> callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.SendGiftAmountList);

        String url = stringBuilder.toString();

        OkGoHttpUtil.getInstance().doGet(
                url,
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 获取礼物数量列表
     */
    public void getBulletMessageList(JsonCallback<String> callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.BulletMessageList);

        String url = stringBuilder.toString();

        OkGoHttpUtil.getInstance().doGet(
                url,
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 获取喇叭数量
     */
    public void getAmountOfSpeaker(String liveId, JsonCallback<String> callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.amountOfSpeaker);

        callback.setArg(liveId);
        String url = stringBuilder.toString();

        OkGoHttpUtil.getInstance().doGet(
                url,
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     * 房管列表
     */
    public void getLivingRoomManagerList(String liveId, JsonCallback<List<LivingRoomAdminListBean>> callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.LivingRoomManagerList);

        callback.setArg(liveId);
        String url = stringBuilder.toString();

        OkGoHttpUtil.getInstance().doGet(
                url,
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }


    /**
     * 获取直播间禁言用户列表
     */
    public void getLivingMuteList(String liveId, JsonCallback<List<BlackOrMuteListItemBean>> callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.LivingMuteList);

        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);

        callback.setArg(liveId);
        String url = stringBuilder.toString();

        OkGoHttpUtil.getInstance().doJsonPost(
                url,
                url,
                getCommonHeaders(System.currentTimeMillis()), new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取直播间拉黑用户列表
     */
    public void getLivingBlackList(String liveId, JsonCallback<List<BlackOrMuteListItemBean>> callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.LivingBlackList);

        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);

        callback.setArg(liveId);
        String url = stringBuilder.toString();

        OkGoHttpUtil.getInstance().doJsonPost(
                url,
                url,
                getCommonHeaders(System.currentTimeMillis()), new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取移除直播间禁言or黑名单用户 类型 (0禁言用户 1黑名单用户)
     */
    public void removeLivingBlackOrMuteUser(String liveId, String uid, JsonCallback<String> callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.LivingBlackOrMuteUser);

        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("uid", uid);

        callback.setArg(liveId);
        String url = stringBuilder.toString();

        OkGoHttpUtil.getInstance().doJsonPost(
                url,
                url,
                getCommonHeaders(System.currentTimeMillis()), new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 根据主播id查询守护列表、守护总人数
     */
    public void queryGuardListByAnchor(String liveId, String anchorId, JsonCallback<AnchorGuardListBean> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.queryGuardListByAnchor;
        callback.setArg(liveId);
        HashMap<String, Object> params = getCommonParams();
        params.put("aid", anchorId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "queryGuardListByAnchor",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 用户进房
     */
    public void interRoom(String liveId, String anchorId, int type, String password, int preview, JsonCallback<EnterRoomBean> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_interroom_URL;
        callback.setUrlTag("/live/inter/room");
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("anchorId", anchorId);
        params.put("type", type);
        params.put("isRoomPreview", preview);

        if (!StringUtils.isEmpty(password)) params.put("password", password);
        Log.e("interRoom", "params:" + params.toString());
        callback.setArg(liveId);
        OkGoHttpUtil.getInstance().doJsonPost(
                "interRoom",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 首页关注列表
     */
    public void queryGuardListByAnchor(JsonCallback<List<RoomListBean>> callback) {
        StringBuilder sb = new StringBuilder();
        sb.append(SPManager.getServerDomain()).append(Constant.URL.getFollowAnchorList);

        OkGoHttpUtil.getInstance().doGet(
                "",
                sb.toString(),
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }


    /**
     * 主播盈利报表
     */
    public void getAnchorProfitStatement(int type, JsonCallback callback) {
        StringBuilder sb = new StringBuilder();
        sb.append(SPManager.getServerDomain()).append(Constant.URL.getAnchorProfitStatement);
        sb.append("?type=").append(type);

        callback.setArg(type + "");
        OkGoHttpUtil.getInstance().doGet(
                "",
                sb.toString(),
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }


    /**
     * 附近的主播
     */
    public void nearbyAnchorList(String title, int type, JsonCallback<List<RoomListBean>> callback) {

        String url = SPManager.getServerDomain() + Constant.URL.NearbyLivingList;
        callback.setUrlTag(Constant.URL.NearbyLivingList);
        HashMap<String, Object> params = getCommonParams();
        if (!TextUtils.isEmpty(title)) {
            if (type == 1) {
                params.put("city", title);
            } else if (type == 2) {
                params.put("province", title);
            }
        }


        OkGoHttpUtil.getInstance().doJsonPost(
                Constant.URL.NearbyLivingList,
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 首页-主播搜索
     */
    public void searchAnchor(String content, JsonCallback<List<SearchAnchorBean>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.searchAnchor;
        callback.setUrlTag(Constant.URL.searchAnchor);
        HashMap<String, Object> params = getCommonParams();
        params.put("content", content);

        OkGoHttpUtil.getInstance().doJsonPost(
                Constant.URL.searchAnchor,
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 开播记录列表
     */
    public void livingRecordList(Long startTime, Long endTime, int page, int pageSize, int status, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.LivingRecord;
        HashMap<String, Object> params = getCommonParams();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("pageNum", page);
        params.put("pageSize", pageSize);
        if (status == 0 || status == 1) {
            params.put("status", status);
        }

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 用户进房
     */
    public void interPreviewRoom(int liveId, long anchorId, int type, String password, JsonCallback<Anchor> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.LIVE_CHECK_INTER_ROOM_URL;
        callback.setUrlTag("inter/roomPreview");
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("anchorId", anchorId);
        params.put("type", type);
        if (!StringUtils.isEmpty(password)) params.put("password", password);
        OkGoHttpUtil.getInstance().doJsonPost(
                "interRoom",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 用户退房
     */
    public void outRoom(String liveId, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_outerroom_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取开播列表
     */
    public void getLiveList(int type, JsonCallback<HomeFragmentRoomListBean> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_list_URL;
        callback.setUrlTag(Constant.URL.Live_list_URL);
        HashMap<String, Object> params = getCommonParams();
        params.put("type", type + "");

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取推荐
     * 列表
     *
     * @param callback
     */
    public void getRecommendLiveList(JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.liveRecommendURL;
        callback.setUrlTag(Constant.URL.liveRecommendURL);
        HashMap<String, Object> params = getCommonParams();

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 举报主播
     *
     * @param callback
     */
    public void reportAnchor(String anchorId, String content, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.reportAnchor;
        HashMap<String, Object> params = getCommonParams();
        params.put("anchorId", anchorId);
        params.put("content", content);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 主播贡献榜
     *
     * @param callback
     */
    public void getContribution(String anchorId, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.ContributionDaily;
        HashMap<String, Object> params = getCommonParams();
        params.put("aid", anchorId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 主播开播
     */
    public void starLiving(HashMap<String, Object> params,
                           JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_start_URL;
        HashMap<String, Object> args = getCommonParams();
        args.putAll(params);

        OkGoHttpUtil.getInstance().doJsonPost(
                Constant.URL.Live_start_URL,
                url,
                getCommonHeaders(Long.parseLong(args.get("timestamp").toString())),
                new Gson().toJson(args))
                .execute(callback);
    }


    /**
     * 获取主播信息
     */
    public void getAnchorInfo(String liveId, String anchorId, JsonCallback<LivingCurrentAnchorBean> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_anchorinfo_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("anchorId", anchorId);

        callback.setArg(liveId);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 获取主播联系名片
     */
    public void getAnchorContactCard(String liveId, String anchorId, JsonCallback callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.getAnchorCard);

        String url = stringBuilder.toString();
        HashMap<String, Object> params = getCommonParams();
        params.put("anchorId", anchorId);

        callback.setArg(liveId);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取主播中心
     */
    public void getAnchorCenterInfo(JsonCallback<String> callback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.LIVE_AnchorCenter);

        OkGoHttpUtil.getInstance().doGet(stringBuilder.toString(),
                stringBuilder.toString(),
                getCommonHeaders(System.currentTimeMillis())).execute(callback);
//        OkGoHttpUtil.getInstance().doJsonPost(
//                "",
//                url,
//                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
//                new Gson().toJson(params))
//                .execute(callback);
    }


    /**
     * 更改房间类型
     */
    public void changeRoomType(String liveId, int type, int price,
                               JsonCallback<String> callback) {
        callback.setUrlTag("room/change");
        String url = SPManager.getServerDomain() + Constant.URL.Live_chargeroomchange_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("type", type);
//        params.put("userLoginPasword", checkText);
        params.put("uid", DataCenter.getInstance().getUserInfo().getUser().getUid());
//        params.put("currentUserAppVersion", AppUtils.getAppVersionName());
        if (price > 0) params.put("price", price);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 权限检测
     *
     * @param permissions 权限代码
     */
    public void checkPermissions(String permissions, JsonCallback<String> callback) {
        callback.setUrlTag("room/chLoginP");
        String url = SPManager.getServerDomain() + Constant.URL.LIVE_ROOM_PERMISSIONS_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("userLoginPasword", permissions);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 计时房间付费
     */
    public void payForRoom(String liveId, String anchorId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_chargeroom_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("anchorId", anchorId);

        callback.setArg(liveId);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 主播开播
     */
    public void startLive(String liveConfigId, String lotteryName, int type, int price,
                          String password, JsonCallback<Anchor> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_start_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveConfigId", liveConfigId);
        params.put("lotteryName", lotteryName);
        params.put("type", type);
        if (price > 0) params.put("price", price);
        if (!StringUtils.isEmpty(password)) params.put("password", password);
        callback.setUrlTag("live/start");

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 主播关播
     */
    public void liveStop(String anchorId, String liveId, boolean isKick, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_stop_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("anchorId", anchorId);
        params.put("liveId", liveId);
        params.put("isKick", isKick);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 主播端心跳
     */
    public void liveHeart(String liveId) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_heart_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params)).execute(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {

            }
        });
    }


    /**
     * 用户端心跳
     */
    public void watchHeart() {
        String url = SPManager.getServerDomain() + Constant.URL.Live_watchheart_URL;
        HashMap<String, Object> params = getCommonParams();

//        OkHttpUtil.doJsonPost(url, new Gson().toJson(params), Long.parseLong(params.get("timestamp").toString()), callback);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params)).execute(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {

            }
        });
//                .execute(callback);
    }

    /**
     * 主播后台和继续直播切换
     */
    public void leaveSwitch(int liveId, boolean isLeave, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_leaveswitch_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("isLeave", isLeave);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 超管关播
     */
    public void kicklive(long liveId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_kicklive_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 直播间禁言
     * 是否禁言 禁言/解禁
     */
    public void blackChat(String liveId, String uid, boolean isMute, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_blackchat_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("uid", uid);
        params.put("isBlack", isMute);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 直播间拉黑用户
     */
    public void banuser(String liveId, String uid, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_BlockUser_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("uid", uid);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 获取观众列表
     */
    public void getAudienceList(String liveId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_roominfo_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);

        callback.setArg(liveId);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 送礼物
     */
    public void sendGift(String gid, String anchorId, String liveId, int combo, int count, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_sendgift_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("gid", gid);
        params.put("anchorId", anchorId);
        params.put("liveId", liveId);
        params.put("combo", combo);
        params.put("count", count);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 直播间聊天
     */
    public void sendMessage(String liveId, String msg, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_chat_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("msg", msg);
//        params.put("isRoomPreview", preview);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 直播间发送弹幕
     */
    public void sendBulletMessage(String liveId, String msg, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.sendBulletMessage;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("msg", msg);
//        params.put("isRoomPreview", preview);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 直播间观众列表
     */
    public void getRoomUserList(String liveId, JsonCallback<List<OnlineUserBean>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_roomuserlist_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);

        callback.setArg(liveId);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 直播间贵族列表
     */
    public void getRoomVipList(String liveId, JsonCallback<List<OnlineUserBean>> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.vipOnlineList;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);

        callback.setArg(liveId);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 房管添加取消
     * type 1：添加 2：取消
     */
    public void roomManagerOperate(String uid, String anchorId, boolean isSetManager, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_roommanager_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);
        params.put("anchorId", anchorId);
        params.put("type", isSetManager ? 1 : 2);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 更改玩具状态
     */
    public void toychange(int status, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_toychange_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("status", status);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取首页火箭位列表
     */
    public void getRocketlist(long uid, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_rocketlist_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", uid);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


}
