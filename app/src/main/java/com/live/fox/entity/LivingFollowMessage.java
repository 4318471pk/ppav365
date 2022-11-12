package com.live.fox.entity;

public class LivingFollowMessage {


    private String avatar;
    private boolean isAnchor;
    private boolean isManage;
    private String nickname;
    private String protocol;
    private int uid;
    private int userLevel;
    private String anchorNickName;//自己加的字段

    public String getAnchorNickName() {
        return anchorNickName;
    }

    public void setAnchorNickName(String anchorNickName) {
        this.anchorNickName = anchorNickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isIsAnchor() {
        return isAnchor;
    }

    public void setIsAnchor(boolean isAnchor) {
        this.isAnchor = isAnchor;
    }

    public boolean isIsManage() {
        return isManage;
    }

    public void setIsManage(boolean isManage) {
        this.isManage = isManage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }
}
