package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.NetEaseVerifyEntity;
import com.live.fox.entity.RegisterEntity;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;


//授权相关接口
public class Api_Auth extends BaseApi {
    private Api_Auth() {
    }

    private static class InstanceHolder {
        private static final Api_Auth instance = new Api_Auth();
    }

    public static Api_Auth ins() {
        return InstanceHolder.instance;
    }

    public static final String refreshToken = "refreshToken";

    /**
     * POST /auth/phone/login 手机号密码登录
     */
    public void phoneLogin(String mobile, String password, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AUTH_PHONELOGIN_URL;
        callback.setUrlTag("/phone/login");
        HashMap<String, Object> params = getCommonParams();
        params.put("mobile", mobile);
        params.put("password", password);

        params.put("address", "unknow");
        params.put("city", "");
        params.put("province", "");
        params.put("softVersion", AppUtils.getAppVersionName());
        params.put("model", DeviceUtils.getModel());
        params.put("version", DeviceUtils.getSDKVersionName());
        params.put("x", "");
        params.put("y", "");

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 判断是否设置密码
     */
    public void determinepPwd(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AUTH_DETERPWD_URL;
        HashMap<String, Object> params = getCommonParams();
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * POST /auth/phone/reg/codeValidate
     */
    public void register(String mobile, String vcode, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AUTH_PHONEREQ_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("mobile", mobile);
        params.put("vcode", vcode);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * POST /auth/send/vcode 验证码发送
     * type 1注册2绑定3重置密码4快捷登录
     */
    public void sendPhoneCode(RegisterEntity register, JsonCallback<String> callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AUTH_SENDVCODE_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("mobile", register.getName());
        params.put("type", register.getType());
        params.put("captchaValidate", register.getVerify());
        params.put("currentUserAppVersion", register.getVersion());
        params.put("verificationNo", register.getVerification());

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void sendPhoneCode2(String mobile, int type, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AUTH_SENDVCODE_URL2;
        HashMap<String, Object> params = getCommonParams();
        params.put("mobile", mobile);
        params.put("type", type);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * POST /auth/vcode/login 验证码快捷登录
     */
    public void phoneCodeLogin(String mobile, String code, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AUTH_VCODELOGIN_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("mobile", mobile);
        params.put("vcode", code);

        params.put("address", "unknow");
        params.put("city", "");
        params.put("province", "");
        params.put("softVersion", AppUtils.getAppVersionName());
        params.put("model", DeviceUtils.getModel());
        params.put("version", DeviceUtils.getSDKVersionName());
        params.put("x", "");
        params.put("y", "");

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * POST /auth/reset/pwd 忘记密码-密码重置
     */
    public void resetPassword(String mobile, String code, String password, JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AUTH_RESETPWD_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("vcode", code);

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }


    /**
     * POST /auth/phone/reg/info 完善信息?
     */
    public void modifyUserInfo(String mobile, String password,
                               String avatar, int sex, String nickname,
                               String puid, JsonCallback callback) {// String code,
        String url = SPManager.getServerDomain() + Constant.URL.AUTH_PHONEREGINFO_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("mobile", mobile);
        params.put("password", password);
        params.put("avatar", avatar);
        params.put("sex", sex);
        params.put("nickname", nickname);
        params.put("puid", puid);
        params.put("softVersion", AppUtils.getAppVersionName());
        params.put("model", DeviceUtils.getModel());
        params.put("version", DeviceUtils.getSDKVersionName());

        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    public void isRegister(String mobile, JsonCallback callback) {
        callback.setUrlTag("phone/isRegiste");
        String url = SPManager.getServerDomain() + Constant.URL.AUTH_ISREGISTE_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("mobile", mobile);
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * 获取直播路线配置接口
     */
    public void getNetEaseVerify(JsonCallback<NetEaseVerifyEntity> callback) {
        callback.setUrlTag("user/captcha");
        String url = SPManager.getServerDomain() + Constant.URL.Net_Eas_VERIFY;
        HashMap<String, Object> params = getCommonParams();
        OkGoHttpUtil.getInstance().doJsonPost(
                "",
                url,
                getCommonHeaders(Long.parseLong(String.valueOf(params.get("timestamp")))),
                new Gson().toJson(params))
                .execute(callback);
    }

    /**
     * POST /auth/check/token检查token是否有效
     */
    public void refreshToken(JsonCallback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.AUTH_CheckToken_URL;
        HashMap<String, Object> params = getCommonParams();
        params.put("model", DeviceUtils.getModel());
        params.put("version", DeviceUtils.getSDKVersionName());
        params.put("softVersion", AppUtils.getAppVersionName());

        OkGoHttpUtil.getInstance().doJsonPost(
                refreshToken,
                url,
                getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params))
                .execute(callback);
    }
}
