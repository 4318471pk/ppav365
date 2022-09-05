package com.live.fox.entity;


public class GameStatement{

    private double allNowStatement;
    private double allStatement;
    private double kyNowStatement;
    private double needStatement;
    private boolean withdraw;
    private double zzNowStatement;
    private String content = "";
    private String withDrawContent = "";
    private double goinCoin;
    private long activityGoinCoin;//活动彩金
    private long activityGoinCoinRecord;//活动彩金流水

    public long getActivityGoinCoin() {
        return activityGoinCoin;
    }

    public void setActivityGoinCoin(long activityGoinCoin) {
        this.activityGoinCoin = activityGoinCoin;
    }

    public long getActivityGoinCoinRecord() {
        return activityGoinCoinRecord;
    }

    public void setActivityGoinCoinRecord(long activityGoinCoinRecord) {
        this.activityGoinCoinRecord = activityGoinCoinRecord;
    }

    public double getGoinCoin() {
        return goinCoin;
    }

    public void setGoinCoin(double goinCoin) {
        this.goinCoin = goinCoin;
    }

    public double getAllNowStatement() {
        return allNowStatement;
    }

    public void setAllNowStatement(double allNowStatement) {
        this.allNowStatement = allNowStatement;
    }

    public double getAllStatement() {
        return allStatement;
    }

    public void setAllStatement(double allStatement) {
        this.allStatement = allStatement;
    }

    public double getKyNowStatement() {
        return kyNowStatement;
    }

    public void setKyNowStatement(double kyNowStatement) {
        this.kyNowStatement = kyNowStatement;
    }

    public double getNeedStatement() {
        return needStatement;
    }

    public void setNeedStatement(double needStatement) {
        this.needStatement = needStatement;
    }

    public boolean isWithdraw() {
        return withdraw;
    }

    public void setWithdraw(boolean withdraw) {
        this.withdraw = withdraw;
    }

    public double getZzNowStatement() {
        return zzNowStatement;
    }

    public void setZzNowStatement(double zzNowStatement) {
        this.zzNowStatement = zzNowStatement;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWithDrawContent() {
        return withDrawContent;
    }

    public void setWithDrawContent(String withDrawContent) {
        this.withDrawContent = withDrawContent;
    }
}
