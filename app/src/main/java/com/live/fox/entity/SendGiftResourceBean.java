package com.live.fox.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SendGiftResourceBean {
    @Id
    private Long id;
    private String cflag;
    private String eflag;
    private int amount;
    private int sort;
    private Long createTime;
    private Long updateTime;
    private int localShouldUpdate;//自己加的字段 1就要下载更新 下载完替换成0
    private String localImgPath;//自己加的字段
    private String localSvgPath;//自己加的字段

    @Generated(hash = 29533923)
    public SendGiftResourceBean(Long id, String cflag, String eflag, int amount,
            int sort, Long createTime, Long updateTime, int localShouldUpdate,
            String localImgPath, String localSvgPath) {
        this.id = id;
        this.cflag = cflag;
        this.eflag = eflag;
        this.amount = amount;
        this.sort = sort;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.localShouldUpdate = localShouldUpdate;
        this.localImgPath = localImgPath;
        this.localSvgPath = localSvgPath;
    }

    @Generated(hash = 951501161)
    public SendGiftResourceBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCflag() {
        return cflag;
    }

    public void setCflag(String cflag) {
        this.cflag = cflag;
    }

    public String getEflag() {
        return eflag;
    }

    public void setEflag(String eflag) {
        this.eflag = eflag;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
