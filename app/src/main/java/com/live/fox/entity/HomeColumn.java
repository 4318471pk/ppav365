package com.live.fox.entity;

import java.io.Serializable;

public class HomeColumn implements Serializable {
    private int type;
    private String name;
    private int openWay;
    private String jumpUrl;
    private String url;
    private String imgUrl;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpenWay() {
        return openWay;
    }

    public void setOpenWay(int openWay) {
        this.openWay = openWay;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
