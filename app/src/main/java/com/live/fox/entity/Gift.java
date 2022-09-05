package com.live.fox.entity;


import java.io.Serializable;

public class Gift implements Serializable {

    private int bimgs;
    private String cover;
    private String descript;
    private double duration;
    private int gid;
    private String gname;
    private int goldCoin;
    private int isShow;
    private int pimgs;
    private int playType; //0连送礼物 1全屏礼物 2半屏礼物
    private String resourceUrl;
    private int simgs;
    private int sort;
    private String tags;
    private int type; //0礼物 1座驾 2其他
    private int version;

    long endTIme;
    int num;

    public void setBimgs(int bimgs) {
        this.bimgs = bimgs;
    }

    public int getBimgs() {
        return bimgs;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return cover;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getDescript() {
        return descript;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getGid() {
        return gid;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGname() {
        return gname;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setPimgs(int pimgs) {
        this.pimgs = pimgs;
    }

    public int getPimgs() {
        return pimgs;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public int getPlayType() {
        return playType;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setSimgs(int simgs) {
        this.simgs = simgs;
    }

    public int getSimgs() {
        return simgs;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getSort() {
        return sort;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public long getEndTIme() {
        return endTIme;
    }

    public void setEndTIme(long endTIme) {
        this.endTIme = endTIme;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Gift{" +
                "bimgs=" + bimgs +
                ", cover='" + cover + '\'' +
                ", descript='" + descript + '\'' +
                ", duration=" + duration +
                ", gid=" + gid +
                ", gname='" + gname + '\'' +
                ", goldCoin=" + goldCoin +
                ", isShow=" + isShow +
                ", pimgs=" + pimgs +
                ", playType=" + playType +
                ", resourceUrl='" + resourceUrl + '\'' +
                ", simgs=" + simgs +
                ", sort=" + sort +
                ", tags='" + tags + '\'' +
                ", type=" + type +
                ", version=" + version +
                '}';
    }
}