package com.live.fox.entity;

public class SearchAnchorBean {


    private String uid;
    private String nickname;
    private String signature;
    private String avatar;
    private Boolean isFollow;
    private String fans;
    private boolean isVipUid;
    private int userLevel;
    private int vipLevel;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean isFollow() {
        return isFollow==null?false:isFollow;
    }

    public void setIsFollow(Boolean isFollow) {
        this.isFollow = isFollow;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public boolean isIsVipUid() {
        return isVipUid;
    }

    public void setIsVipUid(boolean isVipUid) {
        this.isVipUid = isVipUid;
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
