package com.live.fox.entity.response;

import android.text.TextUtils;

import com.live.fox.BuildConfig;

import java.io.Serializable;

public class RedBagRainBean implements Serializable {

    private String iconUrl;
    private int id;
    private String redPort;//"1,2"，1为用户，2为主播端
    private long latelyEndTime;
    private long latelyStartTime;
    private String redPacketName;
    private String userHierarchy;
    private int animationTime;
    private int activityStatus;
    private int isShow;//红包雨，1显示 ，0不显示

    //redPort 1用户端，2直播；isShow，红包雨是否展示
    public Boolean isShowRedBag() {
        boolean portIsShow = false;
        if (!TextUtils.isEmpty(redPort)) {
            if (BuildConfig.IsAnchorClient) {
                portIsShow = redPort.contains("2");
            } else {
                portIsShow = redPort.contains("1");
            }
        }
        return isShow == 1 && portIsShow;//对应用户端，是否展示红包雨
    }

    public void setRedPort(String redPort) {
        this.redPort = redPort;
    }

    public String getRedPort() {
        return redPort;
    }

    public int getAnimationTime() {
        return animationTime;
    }

    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }

    /**
     * 1 没开始，2活动进行中 红包已领完，3 活动正常进行中
     * @return
     */
    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLatelyEndTime() {
        return latelyEndTime;
    }

    public void setLatelyEndTime(long latelyEndTime) {
        this.latelyEndTime = latelyEndTime;
    }

    public long getLatelyStartTime() {
        return latelyStartTime;
    }

    public void setLatelyStartTime(long latelyStartTime) {
        this.latelyStartTime = latelyStartTime;
    }

    public String getRedPacketName() {
        return redPacketName;
    }

    public void setRedPacketName(String redPacketName) {
        this.redPacketName = redPacketName;
    }

    public String getUserHierarchy() {
        return userHierarchy;
    }

    public void setUserHierarchy(String userHierarchy) {
        this.userHierarchy = userHierarchy;
    }
}
