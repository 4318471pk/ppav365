package com.live.fox.entity;

import com.chad.library.adapter.base.entity.SectionEntity;

public class RoomListBean extends SectionEntity {
    private int id;
    private String title;
    private int aid;
    private String roomIcon;
    private int liveChannel;
    private int roomCategory;
    private int categoryId;
    private int categoryType;
    private int roomType;
    private int status;
    private int liveSum;
    private int option;
    private String videoUrl;
    private int hot;
    private int recommend;

    public RoomListBean() {
        super(false, "header");

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getRoomIcon() {
        return roomIcon;
    }

    public void setRoomIcon(String roomIcon) {
        this.roomIcon = roomIcon;
    }

    public int getLiveChannel() {
        return liveChannel;
    }

    public void setLiveChannel(int liveChannel) {
        this.liveChannel = liveChannel;
    }

    public int getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(int roomCategory) {
        this.roomCategory = roomCategory;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLiveSum() {
        return liveSum;
    }

    public void setLiveSum(int liveSum) {
        this.liveSum = liveSum;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }
}
