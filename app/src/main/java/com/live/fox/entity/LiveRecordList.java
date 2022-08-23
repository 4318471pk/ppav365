package com.live.fox.entity;


public class LiveRecordList {

    private long startTime;
    private int liveMinutes;
    private boolean isEffect;
    private int ml;
    private int ffml;
    private long cpStatement;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getLiveMinutes() {
        return liveMinutes;
    }

    public void setLiveMinutes(int liveMinutes) {
        this.liveMinutes = liveMinutes;
    }

    public boolean isEffect() {
        return isEffect;
    }

    public void setEffect(boolean effect) {
        isEffect = effect;
    }

    public int getMl() {
        return ml;
    }

    public void setMl(int ml) {
        this.ml = ml;
    }

    public int getFfml() {
        return ffml;
    }

    public void setFfml(int ffml) {
        this.ffml = ffml;
    }

    public long getCpStatement() {
        return cpStatement;
    }

    public void setCpStatement(long cpStatement) {
        this.cpStatement = cpStatement;
    }
}
