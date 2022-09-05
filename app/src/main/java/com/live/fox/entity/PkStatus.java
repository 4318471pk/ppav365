package com.live.fox.entity;


import java.util.List;

public class PkStatus {

    private String avatar;
    private boolean isFollow;
    private long liveId;
    private String nickname;
    private int result;
    private long scoreA;
    private long scoreB;
    private long startTime;
    private long targetId;
    private List<User> listA;
    private List<User> listB;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public long getLiveId() {
        return liveId;
    }

    public void setLiveId(long liveId) {
        this.liveId = liveId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public long getScoreA() {
        return scoreA;
    }

    public void setScoreA(long scoreA) {
        this.scoreA = scoreA;
    }

    public long getScoreB() {
        return scoreB;
    }

    public void setScoreB(long scoreB) {
        this.scoreB = scoreB;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public List<User> getListA() {
        return listA;
    }

    public void setListA(List<User> mListA) {
        listA = mListA;
    }

    public List<User> getListB() {
        return listB;
    }

    public void setListB(List<User> mListB) {
        listB = mListB;
    }
}