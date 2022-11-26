package com.live.fox.entity;

public class BlackOrMuteListItemBean {


    private String uid;
    private String nickname;
    private int roomId;
    private int sex;
    private String avatar;
    private String signature;
    private Integer vipLevel;
    private boolean isGuard;
    private Integer guardLevel;
    private Integer userLevel;
    private String gid;
    private Long createTime;
    private String operator;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

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

    public int getVipLevel() {
        return vipLevel==null?0:vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public boolean isIsGuard() {
        return isGuard;
    }

    public void setIsGuard(boolean isGuard) {
        this.isGuard = isGuard;
    }

    public Integer getGuardLevel() {
        return guardLevel==null?0:guardLevel;
    }

    public void setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
    }

    public int getUserLevel() {
        return userLevel==null?0:userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public long getCreateTime() {
        return createTime==null?0:createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
