package com.live.fox.entity;


import java.util.List;

public class Rank {

    /**
     * uid : 1028924203
     * rankValue : 839841
     * rankHidden : 0
     * anchorId : 1028923940
     * nickname : 傻妞😘
     * avatar : https://bw02.oss-cn-hongkong.aliyuncs.com/avatar/1028923940_1599065294000_IMG_5603.JPG
     * signature : 笔芯❤️
     * liveId : 138632
     * type : 0
     * price : 0
     * pking : false
     * rq : 7552
     * toy : 0
     * liveStartLottery : [{"cpName":"txssc","lorretyIcon":"https://oss.dljksh.com/game/game_2123234355.png","lotteryName":"一分时时彩"}]
     */

    private long uid;
    private long rankValue;
    private int rankHidden;
    private long anchorId;
    private String nickname;
    private String avatar;
    private String signature;
    private int liveId;
    private int type;
    private int price;
    private boolean pking;
    private int rq;
    private int toy;
    private List<LiveStartLotteryBean> liveStartLottery;
    private int userLevel;
    private int anchorLevel;
    private int sex;  //0:男， 1.女

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getRankValue() {
        return rankValue;
    }

    public void setRankValue(int rankValue) {
        this.rankValue = rankValue;
    }

    public int getRankHidden() {
        return rankHidden;
    }

    public void setRankHidden(int rankHidden) {
        this.rankHidden = rankHidden;
    }

    public long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(long anchorId) {
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

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isPking() {
        return pking;
    }

    public void setPking(boolean pking) {
        this.pking = pking;
    }

    public int getRq() {
        return rq;
    }

    public void setRq(int rq) {
        this.rq = rq;
    }

    public int getToy() {
        return toy;
    }

    public void setToy(int toy) {
        this.toy = toy;
    }

    public void setRankValue(long rankValue) {
        this.rankValue = rankValue;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getAnchorLevel() {
        return anchorLevel;
    }

    public void setAnchorLevel(int anchorLevel) {
        this.anchorLevel = anchorLevel;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public List<LiveStartLotteryBean> getLiveStartLottery() {
        return liveStartLottery;
    }

    public void setLiveStartLottery(List<LiveStartLotteryBean> liveStartLottery) {
        this.liveStartLottery = liveStartLottery;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "uid=" + uid +
                ", rankValue=" + rankValue +
                ", rankHidden=" + rankHidden +
                ", anchorId=" + anchorId +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", signature='" + signature + '\'' +
                ", liveId=" + liveId +
                ", type=" + type +
                ", price=" + price +
                ", pking=" + pking +
                ", rq=" + rq +
                ", toy=" + toy +
                ", liveStartLottery=" + liveStartLottery +
                ", userLevel=" + userLevel +
                ", anchorLevel=" + anchorLevel +
                ", sex=" + sex +
                '}';
    }

    public static class LiveStartLotteryBean {
        /**
         * cpName : txssc
         * lorretyIcon : https://oss.dljksh.com/game/game_2123234355.png
         * lotteryName : 一分时时彩
         */

        private String cpName;
        private String lorretyIcon;
        private String lotteryName;

        public String getCpName() {
            return cpName;
        }

        public void setCpName(String cpName) {
            this.cpName = cpName;
        }

        public String getLorretyIcon() {
            return lorretyIcon;
        }

        public void setLorretyIcon(String lorretyIcon) {
            this.lorretyIcon = lorretyIcon;
        }

        public String getLotteryName() {
            return lotteryName;
        }

        public void setLotteryName(String lotteryName) {
            this.lotteryName = lotteryName;
        }
    }
}