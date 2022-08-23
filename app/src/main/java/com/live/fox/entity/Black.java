package com.live.fox.entity;


public class Black {

    private long uid;
    private long anchorExp;
    private int anchorLevel;
    private String avatar;
    private boolean isFollow;
    private String nickname;
    private int sex;
    private String signature;
    private long userExp;
    private int userLevel;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getAnchorExp() {
        return anchorExp;
    }

    public void setAnchorExp(long anchorExp) {
        this.anchorExp = anchorExp;
    }

    public int getAnchorLevel() {
        return anchorLevel;
    }

    public void setAnchorLevel(int anchorLevel) {
        this.anchorLevel = anchorLevel;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getUserExp() {
        return userExp;
    }

    public void setUserExp(long userExp) {
        this.userExp = userExp;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }
}
