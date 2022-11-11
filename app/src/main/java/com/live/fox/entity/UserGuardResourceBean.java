package com.live.fox.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class UserGuardResourceBean {
    @Id
    private Long id;
    private String name;
    private String imgUrl;
    private String medalUrl;
    private int openType;
    private int openPrice;
    private int renewalPrice;
    private int status;
    private Long createTime;
    private Long updateTime;
    private int guardLevel;
    private int localShouldUpdate;//自己加的字段 1就要下载更新 下载完替换成0
    private String localImgSmallPath;//自己加的字段
    private String localImgMediumPath;//自己加的字段

    @Generated(hash = 161435293)
    public UserGuardResourceBean(Long id, String name, String imgUrl,
            String medalUrl, int openType, int openPrice, int renewalPrice,
            int status, Long createTime, Long updateTime, int guardLevel,
            int localShouldUpdate, String localImgSmallPath,
            String localImgMediumPath) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.medalUrl = medalUrl;
        this.openType = openType;
        this.openPrice = openPrice;
        this.renewalPrice = renewalPrice;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.guardLevel = guardLevel;
        this.localShouldUpdate = localShouldUpdate;
        this.localImgSmallPath = localImgSmallPath;
        this.localImgMediumPath = localImgMediumPath;
    }

    @Generated(hash = 1780046138)
    public UserGuardResourceBean() {
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMedalUrl() {
        return medalUrl;
    }

    public void setMedalUrl(String medalUrl) {
        this.medalUrl = medalUrl;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public int getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(int openPrice) {
        this.openPrice = openPrice;
    }

    public int getRenewalPrice() {
        return renewalPrice;
    }

    public void setRenewalPrice(int renewalPrice) {
        this.renewalPrice = renewalPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getGuardLevel() {
        return guardLevel;
    }

    public void setGuardLevel(int guardLevel) {
        this.guardLevel = guardLevel;
    }

    public int getLocalShouldUpdate() {
        return localShouldUpdate;
    }

    public void setLocalShouldUpdate(int localShouldUpdate) {
        this.localShouldUpdate = localShouldUpdate;
    }

    public String getLocalImgSmallPath() {
        return localImgSmallPath;
    }

    public void setLocalImgSmallPath(String localImgSmallPath) {
        this.localImgSmallPath = localImgSmallPath;
    }

    public String getLocalImgMediumPath() {
        return localImgMediumPath;
    }

    public void setLocalImgMediumPath(String localImgMediumPath) {
        this.localImgMediumPath = localImgMediumPath;
    }
}
