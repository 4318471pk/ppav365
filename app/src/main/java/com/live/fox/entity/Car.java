package com.live.fox.entity;

import java.util.List;


public class Car{

    private int showGid;
    private List<CarList> carList;

    public int getShowGid() {
        return showGid;
    }

    public void setShowGid(int showGid) {
        this.showGid = showGid;
    }

    public List<CarList> getCarList() {
        return carList;
    }

    public void setCarList(List<CarList> carList) {
        this.carList = carList;
    }
}
