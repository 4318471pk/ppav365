package com.live.fox.entity;

public class LivingMsgBoxBean {

    CharSequence charSequence;
    Integer backgroundColor;
    Integer strokeColor;
    int type;//0 系统 1用户

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CharSequence getCharSequence() {
        return charSequence;
    }

    public void setCharSequence(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Integer strokeColor) {
        this.strokeColor = strokeColor;
    }
}
