package com.live.fox.entity;

public class LivingCurrentAnchorBean {


    public Integer zb;
    public Boolean follow;
    public String anchorId;
    public String nickname;
    public String avatar;
    public Integer type;
    public String price;
    public Object liveStartLottery;
    public Integer guardCount;
    public Integer liveSum;
    private String isPayOver;
    private int vipLevel;//用户Vip等级 贵族等级

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getIsPayOver() {
        return isPayOver;
    }

    public void setIsPayOver(String isPayOver) {
        this.isPayOver = isPayOver;
    }

    public Integer getZb() {
        return zb;
    }

    public void setZb(Integer zb) {
        this.zb = zb;
    }

    public Boolean getFollow() {
        return follow==null?false:follow;
    }

    public void setFollow(Boolean follow) {
        this.follow = follow;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Object getLiveStartLottery() {
        return liveStartLottery;
    }

    public void setLiveStartLottery(Object liveStartLottery) {
        this.liveStartLottery = liveStartLottery;
    }

    public Integer getGuardCount() {
        return guardCount;
    }

    public void setGuardCount(Integer guardCount) {
        this.guardCount = guardCount;
    }

    public Integer getLiveSum() {
        return liveSum;
    }

    public void setLiveSum(Integer liveSum) {
        this.liveSum = liveSum;
    }
}
