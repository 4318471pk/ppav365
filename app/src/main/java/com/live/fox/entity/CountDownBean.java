package com.live.fox.entity;

public class CountDownBean {

    private long currentPeriodTime;
    private long currentSealingTime;

    private String openResult;
    private String gameCode;
    private String qs;

    public long getCurrentPeriodTime() {
        return currentPeriodTime;
    }

    public void setCurrentPeriodTime(long currentPeriodTime) {
        this.currentPeriodTime = currentPeriodTime;
    }

    public long getCurrentSealingTime() {
        return currentSealingTime;
    }

    public void setCurrentSealingTime(long currentSealingTime) {
        this.currentSealingTime = currentSealingTime;
    }

    public String getOpenResult() {
        return openResult;
    }

    public void setOpenResult(String openResult) {
        this.openResult = openResult;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getQs() {
        return qs;
    }

    public void setQs(String qs) {
        this.qs = qs;
    }
}
