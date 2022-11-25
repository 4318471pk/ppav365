package com.live.fox.entity;

public class LocationAreaSelectorBean {

    String areaName;
    int type;
    String id;
    boolean isSelected=false;

    public LocationAreaSelectorBean(String areaName, int type) {
        this.areaName = areaName;
        this.type = type;
    }

    public LocationAreaSelectorBean() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
