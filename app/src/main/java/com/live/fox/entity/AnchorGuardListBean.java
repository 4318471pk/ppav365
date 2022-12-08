package com.live.fox.entity;

import android.util.Log;

import java.util.List;

public class AnchorGuardListBean {

    private List<LiveGuardBean> liveGuardVOList;
    private int rq;
    private Integer guardCount;

    public List<LiveGuardBean> getLiveGuardList() {
        return liveGuardVOList;
    }

    public void setLiveGuardList(List<LiveGuardBean> liveGuardVOList) {
        this.liveGuardVOList = liveGuardVOList;
    }

    public int getRq() {
        return rq;
    }

    public void setRq(int rq) {
        this.rq = rq;
    }

    public int getGuardCount() {
        return guardCount==null?0:guardCount;
    }

    public void setGuardCount(int guardCount) {
        this.guardCount = guardCount;
    }

    public static class LiveGuardBean {

        public int id;
        public String uid;
        public int aid;
        public int guardId;
        public long openTime;
        public long renewalTime;
        public Long expireTime;
        public Long gmtCreate;
        public Long gmtUpdate;
        public String operator;
        public int status;
        public String nickname;
        public String guardLevel;
        public String levelName;
        public String avatar;
        public int userLevel;
        public int vipLevel;
        public String vipName;
        public int sendDiamond;
        public int weekUpAmount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getGuardId() {
            return guardId;
        }

        public void setGuardId(int guardId) {
            this.guardId = guardId;
        }

        public long getOpenTime() {
            return openTime;
        }

        public void setOpenTime(long openTime) {
            this.openTime = openTime;
        }

        public long getRenewalTime() {
            return renewalTime;
        }

        public void setRenewalTime(long renewalTime) {
            this.renewalTime = renewalTime;
        }

        public long getExpireTime() {
            return expireTime==null?0:expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public Long getGmtUpdate() {
            return gmtUpdate;
        }

        public void setGmtUpdate(Long gmtUpdate) {
            this.gmtUpdate = gmtUpdate;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getGuardLevel() {
            return guardLevel;
        }

        public void setGuardLevel(String guardLevel) {
            this.guardLevel = guardLevel;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getUserLevel() {
            return userLevel;
        }

        public void setUserLevel(int userLevel) {
            this.userLevel = userLevel;
        }

        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
        }

        public String getVipName() {
            return vipName;
        }

        public void setVipName(String vipName) {
            this.vipName = vipName;
        }

        public int getSendDiamond() {
            return sendDiamond;
        }

        public void setSendDiamond(int sendDiamond) {
            this.sendDiamond = sendDiamond;
        }

        public int getWeekUpAmount() {
            return weekUpAmount;
        }

        public void setWeekUpAmount(int weekUpAmount) {
            this.weekUpAmount = weekUpAmount;
        }
    }
}
