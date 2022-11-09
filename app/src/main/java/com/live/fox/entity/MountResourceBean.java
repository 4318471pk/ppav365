package com.live.fox.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class MountResourceBean {

    @Id
    private Long id;
    private String name;
    private String ename;
    private String gshort;
    private int type;
    private int price;
    private String logUrl;
    private String animationUrl;
    private int animationTime;
    private int compress;
    private int status;
    private int sort;
    private int showFront;
    private String descript;
    private Long createTime;
    private Long updateTime;
    private int localShouldUpdate;//自己加的字段 1就要下载更新 下载完替换成0
    private String localImgPath;//自己加的字段
    private String localSvgPath;//自己加的字段

    @Generated(hash = 9687812)
    public MountResourceBean(Long id, String name, String ename, String gshort,
            int type, int price, String logUrl, String animationUrl,
            int animationTime, int compress, int status, int sort, int showFront,
            String descript, Long createTime, Long updateTime,
            int localShouldUpdate, String localImgPath, String localSvgPath) {
        this.id = id;
        this.name = name;
        this.ename = ename;
        this.gshort = gshort;
        this.type = type;
        this.price = price;
        this.logUrl = logUrl;
        this.animationUrl = animationUrl;
        this.animationTime = animationTime;
        this.compress = compress;
        this.status = status;
        this.sort = sort;
        this.showFront = showFront;
        this.descript = descript;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.localShouldUpdate = localShouldUpdate;
        this.localImgPath = localImgPath;
        this.localSvgPath = localSvgPath;
    }

    @Generated(hash = 18948929)
    public MountResourceBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getGshort() {
        return gshort;
    }

    public void setGshort(String gshort) {
        this.gshort = gshort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public String getAnimationUrl() {
        return animationUrl;
    }

    public void setAnimationUrl(String animationUrl) {
        this.animationUrl = animationUrl;
    }

    public int getAnimationTime() {
        return animationTime;
    }

    public void setAnimationTime(int animationTime) {
        this.animationTime = animationTime;
    }

    public int getCompress() {
        return compress;
    }

    public void setCompress(int compress) {
        this.compress = compress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getShowFront() {
        return showFront;
    }

    public void setShowFront(int showFront) {
        this.showFront = showFront;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public int getLocalShouldUpdate() {
        return localShouldUpdate;
    }

    public void setLocalShouldUpdate(int localShouldUpdate) {
        this.localShouldUpdate = localShouldUpdate;
    }

    public String getLocalImgPath() {
        return localImgPath;
    }

    public void setLocalImgPath(String localImgPath) {
        this.localImgPath = localImgPath;
    }

    public String getLocalSvgPath() {
        return localSvgPath;
    }

    public void setLocalSvgPath(String localSvgPath) {
        this.localSvgPath = localSvgPath;
    }
}
