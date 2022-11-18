package com.live.fox.server;

import android.text.TextUtils;

import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.common.CommonApp;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.DeviceIdUtils;
import com.live.fox.utils.EncryptUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;
import com.lzy.okgo.model.HttpHeaders;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class BaseApi {

    /**
     * Base接口的域名
     */
    public static String getBaseServerDomain() {
        return SPUtils.getInstance().getString(ConstantValue.BaseDomain, "");
    }

    public static HashMap<String, Object> getCommonParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("os", Constant.OS + ""); //系统
        params.put("udid", DeviceIdUtils.getAndroidId(CommonApp.getInstance()));
        params.put("timestamp", System.currentTimeMillis());
        params.put("channel", "");
        params.put("language", MultiLanguageUtils.getRequestHeader());
        String token = DataCenter.getInstance().getUserInfo().getToken();
        if (!TextUtils.isEmpty(token)) {
            params.put("token", token);
        } else {
            params.put("token", "");
        }
        String deviceId = DeviceIdUtils.getAndroidId(CommonApp.getInstance()).substring(0, 6);
        String jiami = deviceId + "8qiezi" + params.get("timestamp");
        params.put("paySign", EncryptUtils.encryptMD5ToString(jiami));
        params.put("sign", EncryptUtils.encryptMD5ToString(DeviceIdUtils.getAndroidId(CommonApp.getInstance()) + "jgyh,kasd" + params.get("timestamp")));
        return params;
    }

    public static JSONObject getCommonParamsString(long time) {
        JSONObject params=new JSONObject();
        try {
            params.put("os", Constant.OS + ""); //系统
            params.put("udid", DeviceIdUtils.getAndroidId(CommonApp.getInstance()));
            params.put("timestamp", time);
            params.put("channel", "");
            params.put("language", MultiLanguageUtils.getRequestHeader());
            String token = DataCenter.getInstance().getUserInfo().getToken();
            if (!TextUtils.isEmpty(token)) {
                params.put("token", token);
            } else {
                params.put("token", "");
            }
            String deviceId = DeviceIdUtils.getAndroidId(CommonApp.getInstance()).substring(0, 6);
            String jiami = deviceId + "8qiezi" + params.get("timestamp");
            params.put("paySign", EncryptUtils.encryptMD5ToString(jiami));
            params.put("sign", EncryptUtils.encryptMD5ToString(DeviceIdUtils.getAndroidId(CommonApp.getInstance()) + "jgyh,kasd" + params.get("timestamp")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static HttpHeaders getCommonHeaders(long timestamp) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("X-UDID", DeviceIdUtils.getAndroidId(CommonApp.getInstance()));
        headers.put("X-Timestamp", timestamp + "");
//        headers.put("X-Language", MultiLanguageUtils.getRequestHeader());
        headers.put("Accept-Language", MultiLanguageUtils.getRequestHeader());
        headers.put("X-Sign", EncryptUtils.encryptMD5ToString(DeviceIdUtils.getAndroidId(CommonApp.getInstance()) + "jgyh,kasd" + timestamp));
        String token = DataCenter.getInstance().getUserInfo().getToken();
        if (!StringUtils.isEmpty(token)) {
            headers.put("Authorization", token);
        }
        return headers;
    }


    public static HttpHeaders getCommonHeaders6(long timestamp) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.HEAD_KEY_USER_AGENT, AppUtils.getUserAgent());
        LogUtils.e("投注参数:" + AppUtils.getUserAgent());
        headers.put("X-UDID", DeviceIdUtils.getAndroidId(CommonApp.getInstance()));
        headers.put("X-Timestamp", timestamp + "");
        headers.put("X-Language", MultiLanguageUtils.getRequestHeader());
        headers.put("X-Sign", EncryptUtils.encryptMD5ToString(DeviceIdUtils.getAndroidId(CommonApp.getInstance()) + "jgyh,kasd" + timestamp));
        String token = SPUtils.getInstance("userinfo").getString("token", "");
        if (!StringUtils.isEmpty(token)) {
            headers.put("Authorization", token);
        }
        return headers;
    }
}
