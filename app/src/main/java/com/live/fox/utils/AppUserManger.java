package com.live.fox.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.live.fox.db.DataBase;
import com.live.fox.entity.User;
import com.live.fox.ui.login.LoginModeSelActivity;

/**
 * 用户管理
 * 这个类用来管理用户
 * 获取用户相关的信息
 */
public class AppUserManger {

    private static User user;
    private static String tempToken; //临时Token

    private AppUserManger() {

    }

    private enum UserInfo {
        SINGLE_USER;

        private final AppUserManger appUserManger;

        UserInfo() {
            appUserManger = new AppUserManger();
        }

        public AppUserManger getUserManger() {
            return appUserManger;
        }
    }

    public static AppUserManger getInstance() {
        return UserInfo.SINGLE_USER.getUserManger();
    }

    public static void initUser(String userJson) {
        DataBase.getDbInstance().insertUser(userJson);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static User getUserInfo() {
        if (user == null) {
            String userJson = DataBase.getDbInstance().getUser();
            user = new Gson().fromJson(userJson, User.class);
        }
        return user;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static User getUserInfo(boolean isUpData) {
        if (isUpData || user == null) {
            String userJson = DataBase.getDbInstance().getUser();
            user = new Gson().fromJson(userJson, User.class);
        }
        return user;
    }

    public static boolean isLogin() {
        getUserInfo();
        return user != null;
    }

    /**
     * 检查是否登录
     * 带跳转到登录页面
     *
     * @param context context
     * @return 是否登录
     */
    public static boolean isLogin(Context context) {
        getUserInfo();
        if (user == null) {
            LoginModeSelActivity.startActivity(context);
        }
        return user != null;
    }

    /**
     * 用户登出
     */
    public static void loginOut() {
        user = null;
        DataBase.getDbInstance().deleteUserInfo();
    }

    public static void setTempToken(String token) {
        tempToken = token;
    }

    public static String getTempToken() {
        if (TextUtils.isEmpty(tempToken)) {
            new Throwable("Token is null");
        }
        return tempToken;
    }
}
