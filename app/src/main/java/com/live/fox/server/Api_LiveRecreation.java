package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.common.JsonCallback;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.live.fox.Constant;
import com.live.fox.manager.SPManager;

import java.util.HashMap;

public class Api_LiveRecreation extends BaseApi {
    private Api_LiveRecreation() {
    }

    private static class InstanceHolder {
        private static Api_LiveRecreation instance = new Api_LiveRecreation();
    }

    public static Api_LiveRecreation ins() {
        return InstanceHolder.instance;
    }

    public static final String getPkAcceptlist = "getPkAcceptlist";


    /**
     * 获取主播可发起pk的关注列表
     */
    public void getPkAcceptlist(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.PK_acceptlist_URL;
        HashMap<String, Object> params = getCommonParams();

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 取消pk请求,取消匹配
     */
    public void cancelPk(long anchorId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.PK_canclereq_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("anchorId", anchorId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 结束PK
     */
    public void finishPk(JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.PK_finish_URL;
        HashMap<String, Object> params = getCommonParams();
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 请求混流或取消混流
     */
    public void mergestream(String mergeMapJson, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.PK_mergestream_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("mergeMapJson", mergeMapJson);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 发送开启pk请求
     */
    public void sendOpenPk(long anchorId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.PK_sendreq_URL;
        HashMap<String, Object> params = getCommonParams();
        if (anchorId > 0) {
            params.put("anchorId", anchorId);
        }

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 响应pk请求
     * 0拒绝1同意
     */
    public void sendRsp(long anchorId, boolean isAgree, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.PK_sendrsp_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("anchorId", anchorId);
        params.put("isAgree", isAgree ? 1 : 0);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 更改是否接受PK状态(开播默认关闭)
     */
    public void setPkStatus(boolean isAccept, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.PK_setPkStaus_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("isAccept", isAccept);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * 获取PK中的相关数据
     */
    public void getPkStatus(long anchorId, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.PK_status_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("anchorId", anchorId);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

}
