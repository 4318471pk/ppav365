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
    private int uid;
    private int userLevel;
    private int vipLevel;

    public String getGuardLevel() {
        return guardLevel;
    }

    public void setGuardLevel(String guardLevel) {
        this.guardLevel = guardLevel;
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

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }
}
