package com.live.fox.entity;

public class DiamondIncomeAndExpenseBean {

    int id;
    long uid;
    long aid;
    long createTime;
    long updateTime;
    float amount;
    String userNick;
    long amountOfDiamond;
    int type;
    int payType; //0钻石1金币
    int category;
    int status;


    public long getTime() {
        return createTime;
    }

    public void setTime(long time) {
        this.createTime = time;
    }

    public String getNickname() {
        return userNick;
    }

    public void setNickname(String nickname) {
        this.userNick = nickname;
    }

    public long getAmountOfDiamond() {
        return amountOfDiamond;
    }

    public void setAmountOfDiamond(long amountOfDiamond) {
        this.amountOfDiamond = amountOfDiamond;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }
}
