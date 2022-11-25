package com.live.fox.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class RoomWatchedHistoryBean {

    @Id(autoincrement = true)
    private Long id;

    private String liveId;
    private String title;
    private String aid;
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
    private String isPayOver;
    private String roomPrice;

    @Generated(hash = 1198923173)
    public RoomWatchedHistoryBean(Long id, String liveId, String title, String aid,
            String roomIcon, int liveChannel, int roomCategory, int categoryId,
            int categoryType, int roomType, int status, int liveSum, int option,
            String videoUrl, int hot, int recommend, String isPayOver,
            String roomPrice) {
        this.id = id;
        this.liveId = liveId;
        this.title = title;
        this.aid = aid;
        this.roomIcon = roomIcon;
        this.liveChannel = liveChannel;
        this.roomCategory = roomCategory;
        this.categoryId = categoryId;
        this.categoryType = categoryType;
        this.roomType = roomType;
        this.status = status;
        this.liveSum = liveSum;
        this.option = option;
        this.videoUrl = videoUrl;
        this.hot = hot;
        this.recommend = recommend;
        this.isPayOver = isPayOver;
        this.roomPrice = roomPrice;
    }

    @Generated(hash = 2114336892)
    public RoomWatchedHistoryBean() {
    }

    public String getLiveId() {
        return liveId;
    }

    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
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

    public String getIsPayOver() {
        return isPayOver;
    }

    public void setIsPayOver(String isPayOver) {
        this.isPayOver = isPayOver;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static RoomWatchedHistoryBean convertBean(RoomListBean roomListBean)
    {
        RoomWatchedHistoryBean bean=new RoomWatchedHistoryBean();
        if(roomListBean==null)
        {
            return bean;
        }
        bean.setRoomPrice(roomListBean.getRoomPrice());
        bean.setIsPayOver(roomListBean.getIsPayOver());
        bean.setRecommend(roomListBean.getRecommend());
        bean.setHot(roomListBean.getHot());
        bean.setVideoUrl(roomListBean.getVideoUrl());
        bean.setOption(roomListBean.getOption());
        bean.setLiveSum(roomListBean.getLiveSum());
        bean.setStatus(roomListBean.getStatus());
        bean.setRoomType(roomListBean.getRoomType());
        bean.setCategoryType(roomListBean.getCategoryType());
        bean.setLiveId(roomListBean.getId());
        bean.setTitle(roomListBean.getTitle());
        bean.setAid(roomListBean.getAid());
        bean.setRoomIcon(roomListBean.getRoomIcon());
        bean.setLiveChannel(roomListBean.getLiveChannel());
        bean.setRoomCategory(roomListBean.getRoomCategory());
        bean.setCategoryId(roomListBean.getCategoryId());
        return bean;
    }

    public static RoomListBean convertBean(RoomWatchedHistoryBean roomListBean)
    {
        RoomListBean bean=new RoomListBean();
        if(roomListBean==null)
        {
            return bean;
        }
        bean.setRoomPrice(roomListBean.getRoomPrice());
        bean.setIsPayOver(roomListBean.getIsPayOver());
        bean.setRecommend(roomListBean.getRecommend());
        bean.setHot(roomListBean.getHot());
        bean.setVideoUrl(roomListBean.getVideoUrl());
        bean.setOption(roomListBean.getOption());
        bean.setLiveSum(roomListBean.getLiveSum());
        bean.setStatus(roomListBean.getStatus());
        bean.setRoomType(roomListBean.getRoomType());
        bean.setCategoryType(roomListBean.getCategoryType());
        bean.setId(roomListBean.getLiveId());
        bean.setTitle(roomListBean.getTitle());
        bean.setAid(roomListBean.getAid());
        bean.setRoomIcon(roomListBean.getRoomIcon());
        bean.setLiveChannel(roomListBean.getLiveChannel());
        bean.setRoomCategory(roomListBean.getRoomCategory());
        bean.setCategoryId(roomListBean.getCategoryId());
        return bean;
    }










}
