package com.live.fox.entity;

public class RankItemBean {

    private String anchorId;
    private String nickname;
    private String avatar;
    private String signature;
    private int isAd;
    private String adJumpUrl;
    private String liveId;
    private int type;
    private String price;
    private String pking;
    private String rq;
    private int toy;
    private String liveStartLottery;
    private String uid;
    private int rankValue;
    private String rankHidden;
    private Integer userLevel;
    private Integer vipLevel;
    private String vipName;
    private boolean isFollow;
    private Boolean isBroadcast;

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getIsAd() {
        return isAd;
    }

    public void setIsAd(int isAd) {
        this.isAd = isAd;
    }

    public String getAdJumpUrl() {
        return adJumpUrl;
    }

    public void setAdJumpUrl(String adJumpUrl) {
        this.adJumpUrl = adJumpUrl;
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPking() {
        return pking;
    }

    public void setPking(String pking) {
        this.pking = pking;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public int getToy() {
        return toy;
    }

    public void setToy(int toy) {
        this.toy = toy;
    }

    public String getLiveStartLottery() {
        return liveStartLottery;
    }

    public void setLiveStartLottery(String liveStartLottery) {
        this.liveStartLottery = liveStartLottery;
    }

    public Boolean getBroadcast() {
        return isBroadcast==null?false:isBroadcast;
    }

    public void setBroadcast(Boolean broadcast) {
        isBroadcast = broadcast;
    }

    public Integer getVipLevel() {
        return vipLevel==null?0:vipLevel;
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getRankValue() {
        return rankValue;
    }

    public void setRankValue(int rankValue) {
        this.rankValue = rankValue;
    }

    public String getRankHidden() {
        return rankHidden;
    }

    public void setRankHidden(String rankHidden) {
        this.rankHidden = rankHidden;
    }

    public Integer getUserLevel() {
        return userLevel==null?0:userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public static RoomListBean convert(RankItemBean rankItemBean)
    {
        RoomListBean roomListBean=new RoomListBean();
        roomListBean.setId(rankItemBean.getLiveId());
        roomListBean.setAid(rankItemBean.getAnchorId());
        roomListBean.setTitle(rankItemBean.getNickname());
        return roomListBean;
    }
}
