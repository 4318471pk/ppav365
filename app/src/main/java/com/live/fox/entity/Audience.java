package com.live.fox.entity;

import java.io.Serializable;
import java.util.List;


public class Audience implements Serializable {

    long liveId;
    long uid;
    String nickname;
    int userLevel;
    int carId;
    long rq;
    List<Integer> badgeList;
    double userExp;
    String avatar;
    int sex;
    private int chatHide;   //0 weiyin  1
    private int roomHide;   //bandan  yin 0   1
    private int rankHide;   //jinfang
    private int showType;   //是否展示：0展示，1不展示
    private int level;     //用户等级
    private int isRoomPreview;  //房间是否可预览

    public int getChatHide() {
        return chatHide;
    }

    public void setChatHide(int mChatHide) {
        chatHide = mChatHide;
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

    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public long getRq() {
        return rq;
    }

    public void setRq(long rq) {
        this.rq = rq;
    }

    public List<Integer> getBadgeList() {
        return badgeList;
    }

    public void setBadgeList(List<Integer> badgeList) {
        this.badgeList = badgeList;
    }

    public double getUserExp() {
        return userExp;
    }

    public void setUserExp(double userExp) {
        this.userExp = userExp;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
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
}
