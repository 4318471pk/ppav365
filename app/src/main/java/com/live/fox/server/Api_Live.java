package com.live.fox.server;

import android.util.Log;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.AnchorGuardListBean;
import com.live.fox.entity.EnterRoomBean;
import com.live.fox.entity.HomeFragmentRoomListBean;
import com.live.fox.entity.LivingCurrentAnchorBean;
import com.live.fox.entity.LivingGiftBean;
import com.live.fox.entity.RoomListBean;
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
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.GiftList);
        stringBuilder.append(String.format("?type=%d",type));

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
        StringBuilder stringBuilder=new StringBuilder();
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
        OkGoHttpUtil.getInstance().doJsonPost(
                "interRoom",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
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
    public void outRoom(int liveId, JsonCallback callback) {
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
        params.put("type", type+"");

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
     *
     * 举报主播
     * @param callback
     */
    public void reportAnchor(String anchorId,String content,JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.reportAnchor;
        HashMap<String, Object> params = getCommonParams();
        params.put("anchorId",anchorId);
        params.put("content",content);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     *
     *主播贡献榜
     * @param callback
     */
    public void getContribution(String anchorId,JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.ContributionDaily;
        HashMap<String, Object> params = getCommonParams();
        params.put("aid",anchorId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }




    /**
     * 获取主播认证状态*/
    public void getAnchorAuth(String liveConfigId,String type,String nickName,String title,String price,
                              JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_start_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveConfigId",liveConfigId);
        params.put("type",type);
        params.put("nickName",nickName);
        params.put("title",title);
        params.put("price",price);

        OkGoHttpUtil.getInstance().doJsonPost(
                Constant.URL.Live_start_URL,
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
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
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(SPManager.getServerDomain());
        stringBuilder.append(Constant.URL.getAnchorCard);
        stringBuilder.append("?anchorId=").append(anchorId);

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
     * 获取主播中心
     */
    public void getAnchorCenterInfo(JsonCallback<String> callback) {
        StringBuilder stringBuilder=new StringBuilder();
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
    public void changeRoomType(int liveId, int type, int price, String password,
                               String checkText, JsonCallback<String> callback) {
        callback.setUrlTag("room/change");
        String url = SPManager.getServerDomain() + Constant.URL.Live_chargeroomchange_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("type", type);
        params.put("userLoginPasword", checkText);
        params.put("uid", DataCenter.getInstance().getUserInfo().getUser().getUid());
        params.put("currentUserAppVersion", AppUtils.getAppVersionName());
        if (price > 0) params.put("price", price);
        if (!StringUtils.isEmpty(password)) params.put("password", password);

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
    public void changeRoom(int liveId, long anchorId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_chargeroom_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("anchorId", anchorId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 开播记录列表
     */
    public void liveRecordList(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_recordlist_URL;
        HashMap<String, Object> params = getCommonParams();

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
    public void liveStop(long anchorId, int liveId, boolean isKick, JsonCallback callback) {
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
    public void liveHeart(int liveId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_heart_URL;
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
                new Gson().toJson(params));
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
    public void blackChat(long liveId, long uid, boolean isBlack, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_blackchat_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("liveId", liveId);
        params.put("uid", uid);
        params.put("isBlack", isBlack);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 直播间踢用户
     */
    public void banuser(long liveId, long uid, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_banuser_URL;
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
     * 直播间观众列表
     */
    public void getRoomUserList(String liveId, JsonCallback callback) {
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
    public void getRoomVipList(String liveId, JsonCallback callback) {
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
    public void roomManager(long targetUid, long anchorId, boolean isSetManager, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.Live_roommanager_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("uid", targetUid);
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
