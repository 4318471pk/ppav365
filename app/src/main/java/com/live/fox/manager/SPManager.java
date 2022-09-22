package com.live.fox.manager;


import com.google.gson.Gson;
import com.live.fox.entity.CountryCode;
import com.live.fox.entity.HomeColumn;
import com.live.fox.entity.LiveColumn;
import com.live.fox.entity.User;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 整个项目的SP管理类
 */
public class SPManager {

    private static String base="base";
    private static String userInfo="userInfo";
    private static String domain="domain";
    private static String share="share";
    private static String CountryCode="CountryCode";

    public static void setGesturePassword(String password) {
        if (!StringUtils.isEmpty(password))
            SPUtils.getInstance(userInfo).put("GesturePassword", password);
    }

    public static String getGesturePassword() {
        String password= SPUtils.getInstance(userInfo).getString("GesturePassword", "");
        return password;
    }

    public static void setGesturePasswordStatus(boolean status) {
        SPUtils.getInstance(userInfo).put("GesturePasswordStatus", status);
    }

    public static boolean getGesturePasswordStatus() {
        boolean status= SPUtils.getInstance(userInfo).getBoolean("GesturePasswordStatus", false);
        return status;
    }


    /**
     * 更新接口的域名
     */
    public static void saveServerDomain(String domain) {
        if (!StringUtils.isEmpty(domain))
            SPUtils.getInstance(base).put("httpserver", domain);
    }

    /**
     * 获得普通接口的域名
     */
    public static String getServerDomain() {
        return SPUtils.getInstance(base).getString("httpserver", "");
    }


    public static void saveToken(String token) {
        SPUtils.getInstance(userInfo).put("token", token);
    }

    public static String getToken() {
        return SPUtils.getInstance(userInfo).getString("token", "");
    }

    public static void saveShareUrl(String shareUrl) {
        SPUtils.getInstance(share).put("shareUrl", shareUrl);
    }

    public static String getShareUrl() {
        return SPUtils.getInstance(share).getString("shareUrl", "");
    }

    public static void saveXCK(boolean isFirstopen) {
        SPUtils.getInstance("isFirstXCK").put("isFirstopen", isFirstopen);
    }

    public static boolean getXCK() {
        return SPUtils.getInstance("isFirstXCK").getBoolean("isFirstopen", true);
    }

    public static void saveDomain(String domainUrl) {
        SPUtils.getInstance(domain).put(domain, domainUrl);
    }

    public static String getDomain() {
        return SPUtils.getInstance(domain).getString(domain, "");
    }

    public static void saveDomainTwo(String domainURL) {
        SPUtils.getInstance(domain).put("domainTwo", domainURL);
    }

    public static String getDomainTwo() {
        return SPUtils.getInstance(domain).getString("domainTwo", "");
    }

    public static void saveIsGameStart(int isGameStart) {
        SPUtils.getInstance("isGameStart").put("isGameStart", isGameStart);
    }

    public static int getIsGameStart() {
        // 0 开启  1不开启
        return SPUtils.getInstance("isGameStart").getInt("isGameStart", 0);
    }

    public static void saveIsCpStart(int isCpStart) {
        SPUtils.getInstance("isCpStart").put("isCpStart", isCpStart);
    }

    public static int getIsCpStart() {
        // 0 开启  1不开启
        return SPUtils.getInstance("isCpStart").getInt("isCpStart", 1);
    }

    public static void saveIsCpButton(int isCpButton) {
        SPUtils.getInstance("isCpButton").put("isCpButton", isCpButton);
    }

    public static int getIsCpButton() {
        // 0 开启  1不开启
        return SPUtils.getInstance("isCpButton").getInt("isCpButton", 1);
    }

//    public static void saveIsTopCpBtnShow(int TopCpBtnShow) {
//        SPUtils.getInstance("isTopCpBtnShow").put("TopCpBtnShow", TopCpBtnShow);
//    }
//    public static int getIsTopCpBtnShow() {
//        // 0 开启  1不开启
//        return SPUtils.getInstance("isTopCpBtnShow").getInt("TopCpBtnShow", 0);
//    }

    public static void savePuid(String puid) {
        SPUtils.getInstance("openinstall").put("puid", puid);
    }

    public static String getPuid() {
        return SPUtils.getInstance("openinstall").getString("puid", "");
    }

    public static void saveImToken(String token) {
        SPUtils.getInstance("im").put("token", token);
    }

    public static String getImToken() {
        return SPUtils.getInstance("im").getString("token", "");
    }

    public static void saveIsShownAppNotice(boolean isShown) {
        SPUtils.getInstance("appnotice").put("isShown", isShown);
    }

    public static boolean getIsShownAppNotice() {
        return SPUtils.getInstance("appnotice").getBoolean("isShown", false);
    }

    public static void clearIsShownAppNotice() {
        SPUtils.getInstance("appnotice").clear();
    }

    public static void saveIsShownAppUpdate(boolean isShown) {
        SPUtils.getInstance("appupdate").put("isShown", isShown);
    }

    public static boolean getIsShownAppUpdate() {
        return SPUtils.getInstance("appnupdate").getBoolean("isShown", false);
    }

    public static void clearIsShownAppUpdate() {
        SPUtils.getInstance("appupdate").clear();
    }


    public static void saveUserInfo(User user) {
        SPUtils.getInstance(userInfo).put("user", new Gson().toJson(user));
        if (!StringUtils.isEmpty(user.getImToken())) {
            saveImToken(user.getImToken());
        }
    }

    public static User getUserInfo() {
        String userStr = SPUtils.getInstance(userInfo).getString("user", "");
        if (StringUtils.isEmpty(userStr)) return null;

        return new Gson().fromJson(userStr, User.class);
    }

    public static void clearUserInfo() {
        SPUtils.getInstance(userInfo).clear();
    }

    public static boolean userIsLogin() {
        return SPUtils.getInstance(userInfo).contains("user");
    }


    public static boolean getISFirstInstall() {
        return SPUtils.getInstance("firstinstall").contains("first");
    }

    public static void saveFirstInstall() {
        SPUtils.getInstance("firstinstall").put("first", 1);
    }


    /**
     * 存储Home栏目列表
     */
    public static void saveHomeColumn(List<HomeColumn> data) {
        SPUtils.getInstance("homecolumn").put("data", new Gson().toJson(data));
    }

    /**
     * 获得Home栏目列表
     */
    public static List<HomeColumn> getHomeColumn() {
        String columnStr = SPUtils.getInstance("homecolumn").getString("data", "");
        if (StringUtils.isEmpty(columnStr)) {
            return new ArrayList<>();
        }

        return GsonUtil.getObjects(columnStr, HomeColumn[].class);
    }

    /**
     * 存储Live栏目列表
     */
    public static void saveLiveColumn(List<LiveColumn> data) {
        SPUtils.getInstance("livecolumn").put("data", new Gson().toJson(data));
    }

    /**
     * 获得Live栏目列表
     */
    public static List<LiveColumn> getLiveColumn() {
        String columnStr = SPUtils.getInstance("livecolumn").getString("data", "");
        if (StringUtils.isEmpty(columnStr)) {
            return new ArrayList<>();
        }

        return GsonUtil.getObjects(columnStr, LiveColumn[].class);
    }

    /**
     * 获取国家码
     */
    public static List<CountryCode> getCountryCode() {
        String columnStr = SPUtils.getInstance(CountryCode).getString(CountryCode, "");
        if (StringUtils.isEmpty(columnStr)) {
            return new ArrayList<>();
        }

        return GsonUtil.getObjects(columnStr, CountryCode[].class);
    }

    public static void setCountryCode(String data)
    {
        SPUtils.getInstance(CountryCode).put(CountryCode, data);
    }

}
