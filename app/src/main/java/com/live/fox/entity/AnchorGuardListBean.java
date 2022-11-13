package com.live.fox.entity;

import java.util.List;

public class AnchorGuardListBean {

    private List<LiveGuardBean> liveGuardVOList;
    private int rq;
    private int guardCount;

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
        return guardCount;
    }

    public void setGuardCount(int guardCount) {
        this.guardCount = guardCount;
    }

    public static class LiveGuardBean {
        private int id;
        private int uid;
        private int aid;
        private int guardId;
        private long openTime;
        private long renewalTime;
        private long expireTime;
        private long gmtCreate;
        private int gmtUpdate;
        private String operator;
        private int status;
        private String nickname;
        private String guardLevel;
        private String avatar;
        private int userLevel;
        private int vipLevel;
        private String vipName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
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
            return expireTime;
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

        public int getGmtUpdate() {
            return gmtUpdate;
        }

        public void setGmtUpdate(int gmtUpdate) {
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
    }
}
