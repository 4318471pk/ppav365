package com.live.fox.entity.response;

import android.text.TextUtils;

import com.live.fox.BuildConfig;

import java.io.Serializable;

public class RedBagRainGetBean implements Serializable {

    private double money;
    private int type;//0没有奖 1有奖
    private int redNumber;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRedNumber() {
        return redNumber;
    }

    public void setRedNumber(int redNumber) {
        this.redNumber = redNumber;
    }
}
