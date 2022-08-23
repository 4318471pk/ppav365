package com.live.fox.entity;


import java.util.List;

public class AnchorRank {

    private List<Rank> allList;
    private List<Rank> dayList;
    private List<Rank> guardList;

    public List<Rank> getAllList() {
        return allList;
    }

    public void setAllList(List<Rank> allList) {
        this.allList = allList;
    }

    public List<Rank> getDayList() {
        return dayList;
    }

    public void setDayList(List<Rank> dayList) {
        this.dayList = dayList;
    }

    public List<Rank> getGuardList() {
        return guardList;
    }

    public void setGuardList(List<Rank> guardList) {
        this.guardList = guardList;
    }
}


