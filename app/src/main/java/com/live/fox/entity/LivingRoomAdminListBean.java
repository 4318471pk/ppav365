package com.live.fox.entity;

public class LivingRoomAdminListBean {

    private String uid;
    private String nickname;
    private Integer sex;
    private String avatar;
    private Integer userLevel;
    private Integer gid;
    private Boolean isRoomManage;
    private Boolean isGuard;
    private Integer guardLevel;
    private Integer vipLevel;

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

    public int getSex() {
        return sex==null?0:sex;
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

    public int getUserLevel() {
        return userLevel==null?0:userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public boolean isRoomManage() {
        return isRoomManage==null?false:isRoomManage;
    }

    public void setRoomManage(boolean roomManage) {
        isRoomManage = roomManage;
    }

    public Boolean getGuard() {
        return isGuard==null?false:isGuard;
    }

    public void setGuard(Boolean guard) {
        isGuard = guard;
    }

    public Integer getGuardLevel() {
        return guardLevel==null?0:guardLevel;
    }

    public void setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
    }

    public Integer getVipLevel() {
        return vipLevel==null?0:vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }
}
