package com.live.fox.entity;

public class BlackOrMuteListItemBean {


    private int uid;
    private String nickname;
    private int roomId;
    private int sex;
    private String avatar;
    private String signature;
    private String levelImg;
    private String vipName;
    private String gid;
    private String guardImg;
    private long createTime;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLevelImg() {
        return levelImg;
    }

    public void setLevelImg(String levelImg) {
        this.levelImg = levelImg;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getGuardImg() {
        return guardImg;
    }

    public void setGuardImg(String guardImg) {
        this.guardImg = guardImg;
    }
}
