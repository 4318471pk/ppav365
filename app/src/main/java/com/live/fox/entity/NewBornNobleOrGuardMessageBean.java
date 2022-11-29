package com.live.fox.entity;

//新的贵族或者守护者的广播消息
public class NewBornNobleOrGuardMessageBean {

    private String avatar;
    private Integer guardCount;
    private Integer guardLevel;
    private String nickname;
    private String protocol;
    private Integer uid;
    private Integer userLevel;
    private Integer vipLevel;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGuardCount() {
        return guardCount==null?0:guardCount;
    }

    public void setGuardCount(Integer guardCount) {
        this.guardCount = guardCount;
    }

    public int getGuardLevel() {
        return guardLevel==null?0:guardLevel;
    }

    public void setGuardLevel(int guardLevel) {
        this.guardLevel = guardLevel;
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
        return uid==null?0:uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUserLevel() {
        return userLevel==null?0:userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }
}
