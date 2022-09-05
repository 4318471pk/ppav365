package com.live.fox.entity;


public class SysNotice {

    LanguageUtilsEntity content;
    int type; //0普通1可跳转
    String url;
    long createTime;

    public LanguageUtilsEntity getContent() {
        return content;
    }

    public void setContent(LanguageUtilsEntity content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}