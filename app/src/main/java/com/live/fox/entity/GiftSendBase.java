package com.live.fox.entity;

import com.live.fox.utils.StringUtils;

import java.io.Serializable;


public class GiftSendBase implements Serializable {
    /**
     * 主播id
     */
    protected String anchoruid;
    /**
     * 礼物数
     */
    protected int count = 1;
    /**
     * 礼物id
     */
    protected int gid = -9999;
    /**
     * 连送数
     */
    protected int combo = 1;
    /**
     * 送礼对象id
     */
    protected String artistid;
    /**
     * 送礼名字
     */
    protected String artistName;

    protected long sendStartTime;


    public String getAnchoruid() {
        return anchoruid;
    }

    public void setAnchoruid(String anchoruid) {
        this.anchoruid = anchoruid;
    }

    public int getCount() {
        return count == 0 ? 1 : count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getCombo() {
        return combo == 0 ? 1 : combo;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public String getArtistid() {
        return StringUtils.isEmpty(artistid) ? anchoruid : artistid;
    }

    public void setArtistid(String artistid) {
        this.artistid = artistid;
    }

    public long getSendStartTime() {
        return sendStartTime;
    }

    public void setSendStartTime(long sendStartTime) {
        this.sendStartTime = sendStartTime;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public String toString() {
        return "GiftSendBase{" +
                "anchoruid='" + anchoruid + '\'' +
                ", count=" + count +
                ", gid=" + gid +
                ", combo=" + combo +
                ", artistid='" + artistid + '\'' +
                ", artistName='" + artistName + '\'' +
                ", sendStartTime=" + sendStartTime +
                '}';
    }
}
