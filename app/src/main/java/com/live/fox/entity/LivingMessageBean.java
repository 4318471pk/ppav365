package com.live.fox.entity;

public class LivingMessageBean {

    public String avatar;
    public boolean isInter;
    public int isRoomPreview;
    public int liveId;
    public String nickname;
    public String protocol;
    public int rq;
    public int showType;
    public int uid;
    public double userExp;
    public int userLevel;
    public String message;

    public static LivingMessageBean simpleSystemMessage(String message,String protocol)
    {
        LivingMessageBean livingMessageBean=new LivingMessageBean();
        livingMessageBean.setMessage(message);
        livingMessageBean.setProtocol(protocol);
        return livingMessageBean;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isInter() {
        return isInter;
    }

    public void setInter(boolean inter) {
        isInter = inter;
    }

    public int getIsRoomPreview() {
        return isRoomPreview;
    }

    public void setIsRoomPreview(int isRoomPreview) {
        this.isRoomPreview = isRoomPreview;
    }

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
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

    public int getRq() {
        return rq;
    }

    public void setRq(int rq) {
        this.rq = rq;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public double getUserExp() {
        return userExp;
    }

    public void setUserExp(double userExp) {
        this.userExp = userExp;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
