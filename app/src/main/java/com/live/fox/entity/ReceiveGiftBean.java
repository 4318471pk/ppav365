package com.live.fox.entity;


import com.live.fox.db.DataBase;
import com.live.fox.utils.StringUtils;

import java.io.Serializable;


public class ReceiveGiftBean implements Serializable {
    private String uid;
    private String nickname;
    private String avatar;
    private int userLevel;
    private int gid;
    private String timestamp;
    private int combo = 1;
    private int count = 1;
    public int chatHide;
    private int mlValue;
    private int luck;
    private long rq;
    Gift gift;

    public ReceiveGiftBean() {
    }

    public ReceiveGiftBean(int gid) {
        this.gid = gid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public void setGift(Gift gift) {
        this.gift = gift;
    }

    public int getCombo() {
        return combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Gift getGift() {
        if(gift!=null) {
            return gift;
        }else {
            return DataBase.getDbInstance().getGiftByGid(gid);
        }

    }

    public int getMlValue() {
        return mlValue;
    }

    public void setMlValue(int mlValue) {
        this.mlValue = mlValue;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public long getRq() {
        return rq;
    }

    public void setRq(long rq) {
        this.rq = rq;
    }

    public User getSender() {
        if (StringUtils.isEmpty(uid)) {
            return null;
        }
        User userInfo = new User();
        userInfo.setUid(Long.parseLong(uid));
        userInfo.setNickname(nickname);
        userInfo.setUserLevel(userLevel);
        return userInfo;
    }

    public boolean isCombo(ReceiveGiftBean giftBean) {
        return this.getGid()==giftBean.getGid() && this.getUid().equals(giftBean.getUid())
                && this.getCount() == giftBean.getCount()
                && this.getCombo() < giftBean.getCombo();
    }

    @Override
    public String toString() {
        return "ReceiveGiftBean{" +
                "uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", userLevel=" + userLevel +
                ", gid='" + gid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", combo=" + combo +
                ", count=" + count +
                ", mlValue=" + mlValue +
                ", luck=" + luck +
                '}';
    }
}
