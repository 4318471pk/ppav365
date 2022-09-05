package com.live.fox.entity;

import java.util.List;

public class LsResult {

    /**
     * id : 2098952
     * lotteryName : pk10
     * nickName : PK10
     * expect : 202101021178
     * lotteryResult : [6,3,4,1,8,9,5,7,10,2]
     */

    private int id;
    private String lotteryName;
    private String nickName;
    private String expect;
    private List<Integer> lotteryResult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getExpect() {
        return expect;
    }

    public void setExpect(String expect) {
        this.expect = expect;
    }

    public List<Integer> getLotteryResult() {
        return lotteryResult;
    }

    public void setLotteryResult(List<Integer> lotteryResult) {
        this.lotteryResult = lotteryResult;
    }
}
