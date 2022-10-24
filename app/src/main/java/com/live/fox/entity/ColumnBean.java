package com.live.fox.entity;

public class ColumnBean {

    private String date;
    private float column;

    public ColumnBean(String date, float column) {
        this.date = date;
        this.column = column;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getColumn() {
        return column;
    }

    public void setColumn(float column) {
        this.column = column;
    }
}
