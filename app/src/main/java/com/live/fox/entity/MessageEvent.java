package com.live.fox.entity;

public class MessageEvent {

    public final static int APPEND_BET_TYPE = 110;

    /**
     * 5.关注
     */
    int type;
    private String data;
    private String name;
    private int times=1;

    public MessageEvent(int type, String data, String name, int times) {
        this.type = type;
        this.data = data;
        this.name = name;
        this.times = times;
    }

    public MessageEvent(int type) {
        this.type = type;
    }

    public MessageEvent(int type, String message) {
        this.type = type;
        this.data = message;
    }

    public MessageEvent(int type, String data, int times) {
        this.type = type;
        this.data = data;
        this.times = times;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return data;
    }

    public void setMessage(String message) {
        this.data = message;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}