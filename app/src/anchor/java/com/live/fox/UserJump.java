package com.live.fox;

import android.app.Activity;
import android.content.Context;

import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.ActivityUtils;

import java.util.List;

/**
 * 账号被顶的处理 如果此时在开播界面或观众界面 则需要做一些处理后再跳登录界面
 */
public class UserJump {
    public static void jumpHelp() {
        List<Activity> activityList = ActivityUtils.getActivityList();
        Context context = null;
        for (Activity activity : activityList) {
            if (activity instanceof PlayLiveActivity) {
                PlayLiveActivity playerActivity = (PlayLiveActivity) activity;
                context = playerActivity;
                playerActivity.closeRoomAndStopPlay(false, true, true);
                break;
            }

            if (activity instanceof AnchorLiveActivity) {
                AnchorLiveActivity anchorActivity = (AnchorLiveActivity) activity;
                context = anchorActivity;
                anchorActivity.closeLiveRoom(context.getString(R.string.gameOver), true);
                break;
            }
        }

        if (context == null) {
            LoginModeSelActivity.startActivity(activityList.get(0), activityList.get(0).getString(R.string.gameOver));
        }
    }
}
