package com.live.fox.entity;

public class SecondAd {
    private String content;
    private int isImg;
    private String jumpUrl;
    private int type; //0二级banner1直播间广告2进房广告文字3飘屏广告 4公告
    private int isweb = 0; //1内部打开 0外部打开

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsImg() {
        return isImg;
    }

    public void setIsImg(int isImg) {
        this.isImg = isImg;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsweb() {
        return isweb;
    }

    public void setIsweb(int isweb) {
        this.isweb = isweb;
    }

    @Override
    public String toString() {
        return "SecondAd{" +
                "content='" + content + '\'' +
                ", isImg=" + isImg +
                ", jumpUrl='" + jumpUrl + '\'' +
                ", type=" + type +
                ", isweb=" + isweb +
                '}';
    }
}
