package com.lc.base.danmu;

/**
 * Created by feiyang on 16/3/2.
 */
public class Danmu {
    public long id;
    public int userId;
    public String type;
    public int imgId;
    public String avatarUrl;
    public String content;

    public Danmu(String content) {
        this.content = content;
    }

    public Danmu(String content, int imgId) {
        this.content = content;
        this.imgId = imgId;
    }

    public Danmu(String content, String type, int imgId) {
        this.content = content;
        this.imgId = imgId;
        this.type = type;
    }

    public Danmu() {
    }
}
