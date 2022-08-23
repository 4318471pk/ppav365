package com.live.fox.entity;

import java.io.Serializable;


public class Banner implements Serializable {

    private String imgUrl;
    private String jumpUrl;
    private String name;
    private int sort;
    private int share;
    private String logoUrl;
    private int isweb = 0;

    private int id;
    private String rank;

    public Banner() {
    }

    public Banner(String logoUrl, int id) {
        this.logoUrl = logoUrl;
        this.id = id;
    }

    public Banner(String logoUrl, int id, String rank) {
        this.logoUrl = logoUrl;
        this.id = id;
        this.rank = rank;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getIsweb() {
        return isweb;
    }

    public void setIsweb(int isweb) {
        this.isweb = isweb;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "imgUrl='" + imgUrl + '\'' +
                ", jumpUrl='" + jumpUrl + '\'' +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", share=" + share +
                ", logoUrl='" + logoUrl + '\'' +
                ", isweb=" + isweb +
                ", id=" + id +
                ", rank='" + rank + '\'' +
                '}';
    }
}
