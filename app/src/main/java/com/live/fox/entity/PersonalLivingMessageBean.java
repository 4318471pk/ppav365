package com.live.fox.entity;

public class PersonalLivingMessageBean {


    private String guardLevel;
    private boolean isGuard;
    private boolean isRoomManage;
    private boolean isVipUid;
    private int liveId;
    private String msg;
    private String nickname;
    private String protocol;
    private String uid;
    private int userLevel;
    private int vipLevel;
    private String avatar;
    private boolean isMoving;//弹幕用的字段

    public String getGuardLevel() {
        return guardLevel;
    }

    public void setGuardLevel(String guardLevel) {
        this.guardLevel = guardLevel;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isIsGuard() {
        return isGuard;
    }

    public void setIsGuard(boolean isGuard) {
        this.isGuard = isGuard;
    }

    public boolean isIsRoomManage() {
        return isRoomManage;
    }

    public void setIsRoomManage(boolean isRoomManage) {
        this.isRoomManage = isRoomManage;
    }

    public boolean isIsVipUid() {
        return isVipUid;
    }

    public void setIsVipUid(boolean isVipUid) {
        this.isVipUid = isVipUid;
    }

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }
}
