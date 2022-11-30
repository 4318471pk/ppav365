package com.live.fox.entity;


public class Follow {

    private String uid;
    private String avatar;
    private String liveId;
    private boolean isFollow;
    private boolean isFans;
    private String nickname;
    private int sex;
    private String signature;
    private Integer userLevel;
    private Integer vipLevel;
    private String vipName;
    private Boolean isBroadcast;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isFans() {
        return isFans;
    }

    public void setFans(boolean fans) {
        isFans = fans;
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

    public int getUserLevel() {
        return userLevel==null?0:userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public Integer getVipLevel() {
        return vipLevel==null?0:vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public Boolean getBroadcast() {
        return isBroadcast==null?false:isBroadcast;
    }

    public void setBroadcast(Boolean broadcast) {
        isBroadcast = broadcast;
    }

    public static RoomListBean convert(Follow follow)
    {
        RoomListBean roomListBean=new RoomListBean();
        roomListBean.setAid(follow.getUid());
        roomListBean.setId(follow.getLiveId());
        roomListBean.setTitle(follow.getNickname());
        return roomListBean;
    }
}
