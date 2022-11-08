package com.live.fox.entity;

public class EnterRoomBean {

    private String pullStreamUrl;
    private int rq;
    private Object carId;
    private Object isPreview;
    private int showType;
    private int uid;
    private int level;
    private int isRoomPreview;
    private int isReal;
    private boolean roomManager;

    public int getIsReal() {
        return isReal;
    }

    public void setIsReal(int isReal) {
        this.isReal = isReal;
    }

    public String getPullStreamUrl() {
        return pullStreamUrl;
    }

    public void setPullStreamUrl(String pullStreamUrl) {
        this.pullStreamUrl = pullStreamUrl;
    }

    public int getRq() {
        return rq;
    }

    public void setRq(int rq) {
        this.rq = rq;
    }

    public Object getCarId() {
        return carId;
    }

    public void setCarId(Object carId) {
        this.carId = carId;
    }

    public Object getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(Object isPreview) {
        this.isPreview = isPreview;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIsRoomPreview() {
        return isRoomPreview;
    }

    public void setIsRoomPreview(int isRoomPreview) {
        this.isRoomPreview = isRoomPreview;
    }

    public boolean isRoomManager() {
        return roomManager;
    }

    public void setRoomManager(boolean roomManager) {
        this.roomManager = roomManager;
    }
}
