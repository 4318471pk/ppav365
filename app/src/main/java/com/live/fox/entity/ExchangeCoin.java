package com.live.fox.entity;


public class ExchangeCoin{

    long uid;
    long anchorCoin;
    long createTime;
    long resultCoin;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getAnchorCoin() {
        return anchorCoin;
    }

    public void setAnchorCoin(long anchorCoin) {
        this.anchorCoin = anchorCoin;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getResultCoin() {
        return resultCoin;
    }

    public void setResultCoin(long resultCoin) {
        this.resultCoin = resultCoin;
    }
}
