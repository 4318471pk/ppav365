package com.live.fox.entity;

/**
 * 返回的礼物实体
 */
public class AdmissionEntity {

    private String cover;
    private int duration;
    private int gid;
    private String gname;
    private int level;
    private int playType;
    private String resourceUrl;
    private int type;

    public AdmissionEntity() {

    }

    public AdmissionEntity(int gid, int type, String resourceUrl) {
        this.gid = gid;
        this.type = type;
        this.resourceUrl = resourceUrl;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AdmissionEntity{" +
                "cover='" + cover + '\'' +
                ", duration=" + duration +
                ", gid=" + gid +
                ", gname='" + gname + '\'' +
                ", level=" + level +
                ", playType=" + playType +
                ", resourceUrl='" + resourceUrl + '\'' +
                ", type=" + type +
                '}';
    }
}
