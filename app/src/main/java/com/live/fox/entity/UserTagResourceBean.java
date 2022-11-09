package com.live.fox.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class UserTagResourceBean {

    @Id
    private Long id;
    private String vipName;
    private int vipLevel;
    private String vipImg;
    private String vipFrams;
    private int type;
    private int vipMount;
    private String vipMdeal;
    private String medalUrl;
    private int payType;
    private int openPrice;
    private int openGiveDiamond;
    private int renewalPrice;
    private int renewalGiveDiamond;
    private Long createTime;
    private Long updateTime;
    private int localShouldUpdate;//自己加的字段 1就要下载更新 下载完替换成0
    private String localVipImgPath;//自己加的字段
    private String localMedalUrlPath;//自己加的字段

    @Generated(hash = 1996842584)
    public UserTagResourceBean(Long id, String vipName, int vipLevel, String vipImg,
            String vipFrams, int type, int vipMount, String vipMdeal,
            String medalUrl, int payType, int openPrice, int openGiveDiamond,
            int renewalPrice, int renewalGiveDiamond, Long createTime,
            Long updateTime, int localShouldUpdate, String localVipImgPath,
            String localMedalUrlPath) {
        this.id = id;
        this.vipName = vipName;
        this.vipLevel = vipLevel;
        this.vipImg = vipImg;
        this.vipFrams = vipFrams;
        this.type = type;
        this.vipMount = vipMount;
        this.vipMdeal = vipMdeal;
        this.medalUrl = medalUrl;
        this.payType = payType;
        this.openPrice = openPrice;
        this.openGiveDiamond = openGiveDiamond;
        this.renewalPrice = renewalPrice;
        this.renewalGiveDiamond = renewalGiveDiamond;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.localShouldUpdate = localShouldUpdate;
        this.localVipImgPath = localVipImgPath;
        this.localMedalUrlPath = localMedalUrlPath;
    }

    @Generated(hash = 1029425771)
    public UserTagResourceBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getVipImg() {
        return vipImg;
    }

    public void setVipImg(String vipImg) {
        this.vipImg = vipImg;
    }

    public String getVipFrams() {
        return vipFrams;
    }

    public void setVipFrams(String vipFrams) {
        this.vipFrams = vipFrams;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVipMount() {
        return vipMount;
    }

    public void setVipMount(int vipMount) {
        this.vipMount = vipMount;
    }

    public String getVipMdeal() {
        return vipMdeal;
    }

    public void setVipMdeal(String vipMdeal) {
        this.vipMdeal = vipMdeal;
    }

    public String getMedalUrl() {
        return medalUrl;
    }

    public void setMedalUrl(String medalUrl) {
        this.medalUrl = medalUrl;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(int openPrice) {
        this.openPrice = openPrice;
    }

    public int getOpenGiveDiamond() {
        return openGiveDiamond;
    }

    public void setOpenGiveDiamond(int openGiveDiamond) {
        this.openGiveDiamond = openGiveDiamond;
    }

    public int getRenewalPrice() {
        return renewalPrice;
    }

    public void setRenewalPrice(int renewalPrice) {
        this.renewalPrice = renewalPrice;
    }

    public int getRenewalGiveDiamond() {
        return renewalGiveDiamond;
    }

    public void setRenewalGiveDiamond(int renewalGiveDiamond) {
        this.renewalGiveDiamond = renewalGiveDiamond;
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

    public String getLocalVipImgPath() {
        return localVipImgPath;
    }

    public void setLocalVipImgPath(String localVipImgPath) {
        this.localVipImgPath = localVipImgPath;
    }

    public String getLocalMedalUrlPath() {
        return localMedalUrlPath;
    }

    public void setLocalMedalUrlPath(String localMedalUrlPath) {
        this.localMedalUrlPath = localMedalUrlPath;
    }
}
