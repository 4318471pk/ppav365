package com.live.fox.entity;


public class WithdrawForShare {

    String cardNo;
    long goldCoin;
    long amount;
    int status; //0：待审核，1：已通过 ,2:驳回
    int type; //0：充值到钱包，1：提现到银行账户
    long updateTime;


    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public long getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(long goldCoin) {
        this.goldCoin = goldCoin;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
