package com.live.fox.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class UserLevelResourceBean {

    @Id
    private Long id;
    private String levelName;
    private int level;
    private String levelImg;
    private int rang;
    private String rangMark;
    private int type;
    private String remark;
    private Long createTime;
    private Long updateTime;
    private int localShouldUpdate;//自己加的字段 1就要下载更新 下载完替换成0
    private String localImgPath;//自己加的字段

    @Generated(hash = 1729408906)
    public UserLevelResourceBean(Long id, String levelName, int level,
            String levelImg, int rang, String rangMark, int type, String remark,
            Long createTime, Long updateTime, int localShouldUpdate,
            String localImgPath) {
        this.id = id;
        this.levelName = levelName;
        this.level = level;
        this.levelImg = levelImg;
        this.rang = rang;
        this.rangMark = rangMark;
        this.type = type;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.localShouldUpdate = localShouldUpdate;
        this.localImgPath = localImgPath;
    }

    @Generated(hash = 346365781)
    public UserLevelResourceBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelImg() {
        return levelImg;
    }

    public void setLevelImg(String levelImg) {
        this.levelImg = levelImg;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public String getRangMark() {
        return rangMark;
    }

    public void setRangMark(String rangMark) {
        this.rangMark = rangMark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
