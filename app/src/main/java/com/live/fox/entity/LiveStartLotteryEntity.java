package com.live.fox.entity;

import java.io.Serializable;

public class LiveStartLotteryEntity implements Serializable {

    private String cpName;
    private String lorretyIcon;
    private String lotteryName;
    private String playMethod;

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getLorretyIcon() {
        return lorretyIcon;
    }

    public void setLorretyIcon(String lorretyIcon) {
        this.lorretyIcon = lorretyIcon;
    }

    public String getLotteryName() {
        return lotteryName;
    }

    public void setLotteryName(String lotteryName) {
        this.lotteryName = lotteryName;
    }

    public String getPlayMethod() {
        return playMethod;
    }

    public void setPlayMethod(String playMethod) {
        this.playMethod = playMethod;
    }
}
