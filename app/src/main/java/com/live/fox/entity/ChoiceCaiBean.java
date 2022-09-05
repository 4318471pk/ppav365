package com.live.fox.entity;

public class ChoiceCaiBean {
    private boolean check;
    private String name;
    private String lotteryName;

    public ChoiceCaiBean(boolean check, String name, String lotteryName) {
        this.check = check;
        this.name = name;
        this.lotteryName = lotteryName;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
