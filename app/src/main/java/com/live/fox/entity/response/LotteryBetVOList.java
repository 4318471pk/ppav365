package com.live.fox.entity.response;

public class LotteryBetVOList {


    /**
     * amount : 0
     * bounsRate :
     * lotteryCode :
     * lotteryName :
     * playMethod :
     */

    private double amount;
    private String bounsRate;
    private String lotteryCode;
    private String lotteryName;
    private String playMethod;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBounsRate() {
        return bounsRate;
    }

    public void setBounsRate(String bounsRate) {
        this.bounsRate = bounsRate;
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

    public String getPlayMethod() {
        return playMethod;
    }

    public void setPlayMethod(String playMethod) {
        this.playMethod = playMethod;
    }
}
