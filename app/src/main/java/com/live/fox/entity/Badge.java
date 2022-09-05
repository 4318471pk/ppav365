package com.live.fox.entity;


import java.io.Serializable;

public class Badge implements Serializable {
    //徽章描述
    private String descript;
    //徽章图标URL
    private String logoUrl;
    //徽章名称
    private String name;
    //bid
    private int bid;

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "BadgeBean{" +
                "descript='" + descript + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", name='" + name + '\'' +
                ", bid=" + bid +
                '}';
    }
}
