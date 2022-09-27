package com.live.fox.manager;

import android.content.Context;

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

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static User getUserInfo() {

        return user;
    }






}
