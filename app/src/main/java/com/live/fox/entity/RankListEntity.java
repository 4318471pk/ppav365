package com.live.fox.entity;

import java.util.List;

public class RankListEntity {

    private List<Rank> rankList1;
    private List<Rank> rankList2;
    private List<Rank> rankList3;
    private List<Rank> rankList4;

    public List<Rank> getRankList1() {
        return rankList1;
    }

    public void setRankList1(List<Rank> rankList1) {
        this.rankList1 = rankList1;
    }

    public List<Rank> getRankList2() {
        return rankList2;
    }

    public void setRankList2(List<Rank> rankList2) {
        this.rankList2 = rankList2;
    }

    public List<Rank> getRankList3() {
        return rankList3;
    }

    public void setRankList3(List<Rank> rankList3) {
        this.rankList3 = rankList3;
    }

    public List<Rank> getRankList4() {
        return rankList4;
    }

    public void setRankList4(List<Rank> rankList4) {
        this.rankList4 = rankList4;
    }

    @Override
    public String toString() {
        return "RankListEntity{" +
                "rankList1=" + rankList1 +
                ", rankList2=" + rankList2 +
                ", rankList3=" + rankList3 +
                ", rankList4=" + rankList4 +
                '}';
    }
}
