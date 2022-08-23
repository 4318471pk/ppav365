package com.live.fox.entity;

import java.io.Serializable;

public class ZblbBean implements Serializable{

    /**
     * uid : 1028924022
     * nickName : 123更丰富
     * img : http://bw18.oss-cn-hongkong.aliyuncs.com/1028924022_1615968319_avatar.png
     * heartTime : 00:00:00:
     * goldMedal : 0
     * familyId : 1028924019
     * startStatus : 0
     * toTalMl : 0
     * toTalcpStatement : 0
     * toTalffml : 0
     */

    private long uid;
    private String nickName;
    private String img;
    private String heartTime;
    private int goldMedal;
    private int familyId;
    private int startStatus;
    private int toTalMl;
    private int toTalcpStatement;
    private int toTalffml;
    private int myId;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(String heartTime) {
        this.heartTime = heartTime;
    }

    public int getGoldMedal() {
        return goldMedal;
    }

    public void setGoldMedal(int goldMedal) {
        this.goldMedal = goldMedal;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public int getStartStatus() {
        return startStatus;
    }

    public void setStartStatus(int startStatus) {
        this.startStatus = startStatus;
    }

    public int getToTalMl() {
        return toTalMl;
    }

    public void setToTalMl(int toTalMl) {
        this.toTalMl = toTalMl;
    }

    public int getToTalcpStatement() {
        return toTalcpStatement;
    }

    public void setToTalcpStatement(int toTalcpStatement) {
        this.toTalcpStatement = toTalcpStatement;
    }

    public int getToTalffml() {
        return toTalffml;
    }

    public void setToTalffml(int toTalffml) {
        this.toTalffml = toTalffml;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }
}
