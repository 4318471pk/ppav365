package com.live.fox.svga;




public class GiftBean {

    private int bimgs;
    private String cover;
    private String descript;
    private double duration;
    private int gid;
    private String gname;
    private int goldCoin;
    private int isShow;
    private int pimgs;
    private int playType;
    private String resourceUrl;
    private int simgs;
    private int sort;
    private String tags;
    private int type; //0礼物 1座驾
    private int version;
    long endTIme;
    int num;


    public GiftBean(int gid, int bimgs, int type, String resourceUrl) {
        this.bimgs = bimgs;
        this.gid = gid;
        this.type = type;
        this.resourceUrl = resourceUrl;
    }

    public int getBimgs() {
        return bimgs;
    }

    public void setBimgs(int bimgs) {
        this.bimgs = bimgs;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
        this.goldCoin = goldCoin;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getPimgs() {
        return pimgs;
    }

    public void setPimgs(int pimgs) {
        this.pimgs = pimgs;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public int getSimgs() {
        return simgs;
    }

    public void setSimgs(int simgs) {
        this.simgs = simgs;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getEndTIme() {
        return endTIme;
    }

    public void setEndTIme(long endTIme) {
        this.endTIme = endTIme;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
