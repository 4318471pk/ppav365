package com.live.fox.entity;

public class SelectLotteryBean {

    boolean isSelect = false;
    String name;
    boolean isZdy = false;//是否自定义

    public SelectLotteryBean() {
        this.name = name;
    }

    public SelectLotteryBean(String name) {
        this.name = name;
    }

    public SelectLotteryBean(String name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isZdy() {
        return isZdy;
    }

    public void setZdy(boolean zdy) {
        isZdy = zdy;
    }
}
