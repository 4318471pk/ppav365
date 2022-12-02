package com.live.fox.entity;

import java.io.Serializable;

public class LiveRoomGameDetailBean implements Serializable {


    /**
     * id : 9
     * gameCode : yfks
     * lotteryCode : daxiao
     * lotteryName : 大小
     * odds : {"da":1.96,"xiao":1.96,"dan":1.96,"shuang":1.96}
     * status : 1
     * sort : 1
     * createTime : 1
     * updateTime : 1
     * operator : 1
     */

    private int id;
    private String gameCode;
    private String lotteryCode;
    private String lotteryName;
    private String odds;
    private int status;
    private int sort;
    private long createTime;
    private long updateTime;
    private String operator;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(String lotteryCode) {
        this.lotteryCode = lotteryCode;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
