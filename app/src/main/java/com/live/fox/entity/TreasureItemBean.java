package com.live.fox.entity;

public class TreasureItemBean {

    String name;
    int costDiamond;
    String imgUrl;
    boolean isSelected;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCostDiamond() {
        return costDiamond;
    }

    public void setCostDiamond(int costDiamond) {
        this.costDiamond = costDiamond;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
