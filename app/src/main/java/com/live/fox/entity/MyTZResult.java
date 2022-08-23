package com.live.fox.entity;

import java.io.Serializable;
import java.util.List;

public class MyTZResult implements Serializable {

    /**
     * uid : 1028924022
     * lotteryName : jsks
     * nickName : TÀI XỈU
     * expect : 202101211229
     * times : 1
     * playNumReq : {"liveId":11100,"lotteryName":"jsks","expect":"202101211229","id":null,"num":"Tài xỉu-Tài","notes":1,"type_text":"和值","type":"1","rebate":0,"money":5,"awardMount":9.85,"realProfitAmount":8}
     * betAmount : 5
     * createTime : 1611232090731
     * resultList : [2,3,2]
     * awardStatus : 1
     * winningNumbers : [7,25,45,61,65,83,85]
     * realProfitAmount : 9.85
     * payMethd : 1
     * updateTime : 1611232143015
     */

    private long uid;
    private String lotteryName;
    private String nickName;
    private String expect;
    private int times;
    private PlayNumReqBean playNumReq;
    private int betAmount;
    private long createTime;
    private int awardStatus;
    private String winningNumbers;
    private double realProfitAmount;
    private int payMethd;
    private Long updateTime;
    private List<Integer> resultList;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public PlayNumReqBean getPlayNumReq() {
        return playNumReq;
    }

    public void setPlayNumReq(PlayNumReqBean playNumReq) {
        this.playNumReq = playNumReq;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getAwardStatus() {
        return awardStatus;
    }

    public void setAwardStatus(int awardStatus) {
        this.awardStatus = awardStatus;
    }

    public String getWinningNumbers() {
        return winningNumbers;
    }

    public void setWinningNumbers(String winningNumbers) {
        this.winningNumbers = winningNumbers;
    }

    public double getRealProfitAmount() {
        return realProfitAmount;
    }

    public void setRealProfitAmount(double realProfitAmount) {
        this.realProfitAmount = realProfitAmount;
    }

    public int getPayMethd() {
        return payMethd;
    }

    public void setPayMethd(int payMethd) {
        this.payMethd = payMethd;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public List<Integer> getResultList() {
        return resultList;
    }

    public void setResultList(List<Integer> resultList) {
        this.resultList = resultList;
    }

    public static class PlayNumReqBean implements Serializable{
        /**
         * liveId : 11100
         * lotteryName : jsks
         * expect : 202101211229
         * id : null
         * num : Tài xỉu-Tài
         * notes : 1
         * type_text : 和值
         * type : 1
         * rebate : 0
         * money : 5
         * awardMount : 9.85
         * realProfitAmount : 8
         */

        private int liveId;
        private String lotteryName;
        private String expect;
        private Object id;
        private String num;
        private int notes;
        private String type_text;
        private String type;
        private int rebate;
        private int money;
        private double awardMount;
        private int realProfitAmount;

        public int getLiveId() {
            return liveId;
        }

        public void setLiveId(int liveId) {
            this.liveId = liveId;
        }

        public String getLotteryName() {
            return lotteryName;
        }

        public void setLotteryName(String lotteryName) {
            this.lotteryName = lotteryName;
        }

        public String getExpect() {
            return expect;
        }

        public void setExpect(String expect) {
            this.expect = expect;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public int getNotes() {
            return notes;
        }

        public void setNotes(int notes) {
            this.notes = notes;
        }

        public String getType_text() {
            return type_text;
        }

        public void setType_text(String type_text) {
            this.type_text = type_text;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getRebate() {
            return rebate;
        }

        public void setRebate(int rebate) {
            this.rebate = rebate;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public double getAwardMount() {
            return awardMount;
        }

        public void setAwardMount(double awardMount) {
            this.awardMount = awardMount;
        }

        public int getRealProfitAmount() {
            return realProfitAmount;
        }

        public void setRealProfitAmount(int realProfitAmount) {
            this.realProfitAmount = realProfitAmount;
        }
    }
}
