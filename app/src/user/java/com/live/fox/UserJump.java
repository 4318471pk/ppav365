package com.live.fox;

import android.app.Activity;

import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.ActivityUtils;

/**
 * 用户端跳转帮助
 */
public class UserJump {

    public static void jumpHelp() {
        Activity activity = ActivityUtils.getTopActivity();
        if (activity instanceof PlayLiveActivity) {
            PlayLiveActivity playerActivity = (PlayLiveActivity) activity;
            playerActivity.closeRoomAndStopPlay(false, true, true);
        }
        //跳登录界面
        LoginModeSelActivity.startActivity(activity, activity.getString(R.string.gameOver));
    }
}
