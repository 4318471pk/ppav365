package com.live.fox.entity;

public class Noble {
    private int chatHide;//0 weiyin  1
    private long endTime;//jieshushijia
    private int roomHide;//bandan  yin 0   1
    private int rankHide;//jinfang
    private int status;
    private String name;
    private String group;
    private int vipUid;//lianghao
    private int levelId;
    private int sex;
    private long uid;
    private int bid;

    public int getSex() {
        return sex;
    }

    public void setSex(int mSex) {
        sex = mSex;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long mUid) {
        uid = mUid;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int mBid) {
        bid = mBid;
    }

    public int getChatHide() {
        return chatHide;
    }

    public void setChatHide(int mChatHide) {
        chatHide = mChatHide;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long mEndTime) {
        endTime = mEndTime;
    }

    public int getRoomHide() {
        return roomHide;
    }

    public void setRoomHide(int mRoomHide) {
        roomHide = mRoomHide;
    }

    public int getRankHide() {
        return rankHide;
    }

    public void setRankHide(int mRankHide) {
        rankHide = mRankHide;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int mStatus) {
        status = mStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        name = mName;
    }


    public int getVipUid() {
        return vipUid;
    }

    public void setVipUid(int mVipUid) {
        vipUid = mVipUid;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int mLevelId) {
        levelId = mLevelId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String mGroup) {
        group = mGroup;
    }
}
