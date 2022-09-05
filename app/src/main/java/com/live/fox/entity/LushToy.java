package com.live.fox.entity;


/**
 * Lush玩具
 */
public class LushToy {

    private int lushType; //玩具type
    private int lushLevel; //玩具level
    private int lushTime; //玩具时长

    public LushToy(int lushType, int lushLevel, int lushTime) {
        this.lushType = lushType;
        this.lushLevel = lushLevel;
        this.lushTime = lushTime;
    }

    public int getLushType() {
        return lushType;
    }

    public void setLushType(int lushType) {
        this.lushType = lushType;
    }

    public int getLushLevel() {
        return lushLevel;
    }

    public void setLushLevel(int lushLevel) {
        this.lushLevel = lushLevel;
    }

    public int getLushTime() {
        return lushTime;
    }

    public void setLushTime(int lushTime) {
        this.lushTime = lushTime;
    }
}
