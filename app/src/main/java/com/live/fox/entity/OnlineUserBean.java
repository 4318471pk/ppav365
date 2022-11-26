package com.live.fox.entity;

public class OnlineUserBean {

    private int uid;
    private String avatar;
    private Object userExp;
    private Integer userLevel;
    private int sex;
    private String nickname;
    private Object roomHide;
    private Object rankHide;
    private Object gid;
    private Boolean isVipUid;
    private Boolean isGuard;
    private Integer guardLevel;
    private Integer vipLevel;
    private boolean isRoomManage;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Object getUserExp() {
        return userExp;
    }

    public void setUserExp(Object userExp) {
        this.userExp = userExp;
    }

    public int getUserLevel() {
        return userLevel==null?0:userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Object getRoomHide() {
        return roomHide;
    }

    public void setRoomHide(Object roomHide) {
        this.roomHide = roomHide;
    }

    public Object getRankHide() {
        return rankHide;
    }

    public void setRankHide(Object rankHide) {
        this.rankHide = rankHide;
    }

    public Object getGid() {
        return gid;
    }

    public void setGid(Object gid) {
        this.gid = gid;
    }

    public boolean isIsVipUid() {
        return isVipUid==null?false:isVipUid;
    }

    public void setIsVipUid(boolean isVipUid) {
        this.isVipUid = isVipUid;
    }

    public Boolean getIsGuard() {
        return isGuard;
    }

    public void setIsGuard(Boolean isGuard) {
        this.isGuard = isGuard;
    }

    public Integer getGuardLevel() {
        return guardLevel==null?0:guardLevel;
    }

    public void setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
    }

    public int getVipLevel() {
        return vipLevel==null?0:vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public boolean isIsRoomManage() {
        return isRoomManage;
    }

    public void setIsRoomManage(boolean isRoomManage) {
        this.isRoomManage = isRoomManage;
    }
}
