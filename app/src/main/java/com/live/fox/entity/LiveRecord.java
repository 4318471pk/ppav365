package com.live.fox.entity;

import java.util.List;


public class LiveRecord {

    private String monthTimes;
    private int monthDays;
    private int weekProfit;
    private List<LiveRecordList> liveRecordList;

    public String getMonthTimes() {
        return monthTimes;
    }

    public void setMonthTimes(String monthTimes) {
        this.monthTimes = monthTimes;
    }

    public int getMonthDays() {
        return monthDays;
    }

    public void setMonthDays(int monthDays) {
        this.monthDays = monthDays;
    }

    public List<LiveRecordList> getLiveRecordList() {
        return liveRecordList;
    }

    public void setLiveRecordList(List<LiveRecordList> liveRecordList) {
        this.liveRecordList = liveRecordList;
    }

    public int getWeekProfit() {
        return weekProfit;
    }

    public void setWeekProfit(int weekProfit) {
        this.weekProfit = weekProfit;
    }
}
