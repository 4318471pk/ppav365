package com.live.fox.entity;


import java.util.List;

/**
 * 充值渠道
 */
public class RechargeChannel {

    private String name;
    private int type; //1支付宝2微信3人工客服4银联
    private String submitUrl;
    private int callbackType;  //1URL 2HTML 3qrcode
    private List<RecharegPrice> products;
    private String logs;
    private long highest;
    private long lowest;
    private double reward;
    private String supportBank;
    private int sizeStatus;
    private String remark;
    private double rate;
    private String channelImage;
    private boolean isCheck;



    public int getSizeStatus() {
        return sizeStatus;
    }

    public void setSizeStatus(int sizeStatus) {
        this.sizeStatus = sizeStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getHighest() {
        return highest;
    }

    public void setHighest(long highest) {
        this.highest = highest;
    }

    public long getLowest() {
        return lowest;
    }

    public void setLowest(long lowest) {
        this.lowest = lowest;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setCallbackType(int callbackType) {
        this.callbackType = callbackType;
    }

    public int getCallbackType() {
        return callbackType;
    }

    public void setProducts(List<RecharegPrice> products) {
        this.products = products;
    }

    public List<RecharegPrice> getProducts() {
        return products;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public String getSupportBank() {
        return supportBank;
    }

    public void setSupportBank(String supportBank) {
        this.supportBank = supportBank;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getChannelImage() {
        return channelImage;
    }

    public void setChannelImage(String channelImage) {
        this.channelImage = channelImage;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}