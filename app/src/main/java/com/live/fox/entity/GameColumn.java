package com.live.fox.entity;


import java.io.Serializable;
import java.util.List;

public class GameColumn implements Serializable {

    private String title;
    List<GameItem> list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<GameItem> getList() {
        return list;
    }

    public void setList(List<GameItem> list) {
        this.list = list;
    }
}