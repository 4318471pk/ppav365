package com.live.fox.entity;

import java.io.Serializable;


public class LiveClose implements Serializable {

    private boolean isKick;
    private int liveMinutes = 0;
    private double profit = 0;

    public boolean isKick() {
        return isKick;
    }

    public void setKick(boolean kick) {
        isKick = kick;
    }

    public int getLiveMinutes() {
        return liveMinutes;
    }

    public void setLiveMinutes(int liveMinutes) {
        this.liveMinutes = liveMinutes;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
