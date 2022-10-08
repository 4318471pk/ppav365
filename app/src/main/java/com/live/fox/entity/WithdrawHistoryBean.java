package com.live.fox.entity;

import java.math.BigDecimal;

public class WithdrawHistoryBean {

    long withdrawTime;
    BigDecimal amountOfMoney;
    String WithdrawWay;
    String statusStr;

    public long getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(long withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    public BigDecimal getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(BigDecimal amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public String getWithdrawWay() {
        return WithdrawWay;
    }

    public void setWithdrawWay(String withdrawWay) {
        WithdrawWay = withdrawWay;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
}
