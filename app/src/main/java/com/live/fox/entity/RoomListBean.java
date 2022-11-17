package com.live.fox.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.SectionEntity;

public class RoomListBean extends SectionEntity implements Parcelable {
    private String id;
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
    private int isPayOver;

    public RoomListBean() {
        super(false, "header");

    }

    protected RoomListBean(Parcel in) {
        super(in);
        id = in.readString();
        title = in.readString();
        aid = in.readString();
        roomIcon = in.readString();
        liveChannel = in.readInt();
        roomCategory = in.readInt();
        categoryId = in.readInt();
        categoryType = in.readInt();
        roomType = in.readInt();
        status = in.readInt();
        liveSum = in.readInt();
        option = in.readInt();
        videoUrl = in.readString();
        hot = in.readInt();
        recommend = in.readInt();
        isPayOver = in.readInt();
    }

    public static final Creator<RoomListBean> CREATOR = new Creator<RoomListBean>() {
        @Override
        public RoomListBean createFromParcel(Parcel in) {
            return new RoomListBean(in);
        }

        @Override
        public RoomListBean[] newArray(int size) {
            return new RoomListBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(aid);
        parcel.writeString(roomIcon);
        parcel.writeInt(liveChannel);
        parcel.writeInt(roomCategory);
        parcel.writeInt(categoryId);
        parcel.writeInt(categoryType);
        parcel.writeInt(roomType);
        parcel.writeInt(status);
        parcel.writeInt(liveSum);
        parcel.writeInt(option);
        parcel.writeString(videoUrl);
        parcel.writeInt(hot);
        parcel.writeInt(recommend);
        parcel.writeInt(isPayOver);
    }
}
