package com.live.fox.entity;

public class HongdongBean {

    /**
     * activityStatus : 1
     * distributeStatus : 0
     * type : 47
     * msg : Chưa đến thời gian khuyến mãi
     * activityDetail : qq
     */

    private int activityStatus;
    private int distributeStatus;
    private int type;
    private String msg;
    private int distributeType;
    private String activityDetail;
    private String activityHomePage;

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public int getDistributeStatus() {
        return distributeStatus;
    }

    public void setDistributeStatus(int distributeStatus) {
        this.distributeStatus = distributeStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getDistributeType() {
        return distributeType;
    }

    public void setDistributeType(int distributeType) {
        this.distributeType = distributeType;
    }

    public String getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(String activityDetail) {
        this.activityDetail = activityDetail;
    }

    public String getActivityHomePage() {
        return activityHomePage;
    }

    public void setActivityHomePage(String activityHomePage) {
        this.activityHomePage = activityHomePage;
    }
}
