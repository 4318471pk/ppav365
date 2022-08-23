package com.live.fox.entity;


public class Withdraw {

    long anchorCoin;
    String cardNo;
    long cash;
    long gmtCreate;
    int status; //0待审核1已发放2已驳回
    String anchorName;

    public String getAnchorName() {
        return anchorName;
    }

    public void setAnchorName(String anchorName) {
        this.anchorName = anchorName;
    }

    public long getAnchorCoin() {
        return anchorCoin;
    }

    public void setAnchorCoin(long anchorCoin) {
        this.anchorCoin = anchorCoin;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public long getCash() {
        return cash;
    }

    public void setCash(long cash) {
        this.cash = cash;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
