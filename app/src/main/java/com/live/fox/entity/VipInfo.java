package com.live.fox.entity;

public class VipInfo {

    /**
     * bid : 0
     * id : 0
     * level : 0
     * logoUrl : string
     * name : string
     * num : 0
     * pkAddition : 0
     * price : 0
     * renewPrice : 0
     * rewardPrice : 0
     */

    private int bid;
    private int id;
    private int level;
    private String logoUrl;
    private String name;
    private int num;
    private float pkAddition;
    private int price;
    private int renewPrice;
    private int rewardPrice;
    private int returnPrice;

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRenewPrice() {
        return renewPrice;
    }

    public void setRenewPrice(int renewPrice) {
        this.renewPrice = renewPrice;
    }

    public int getRewardPrice() {
        return rewardPrice;
    }

    public void setRewardPrice(int rewardPrice) {
        this.rewardPrice = rewardPrice;
    }

    public int getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(int mReturnPrice) {
        returnPrice = mReturnPrice;
    }

    public float getPkAddition() {
        return pkAddition;
    }

    public void setPkAddition(float mPkAddition) {
        pkAddition = mPkAddition;
    }
}
