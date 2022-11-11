package com.live.fox.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class GiftResourceBean {

    @Id
    private Long id;
    private String name;
    private String ename;
    private String gshort;
    private int type;
    private int mark;
    private int needdiamond;
    private String gitficon;
    private String swf;
    private int swftime;
    private String compressUrl;
    private int compress;
    private int status;
    private int sort;
    private int showFront;
    private String remark;
    private Long createTime;
    private Long updateTime;
    private int localShouldUpdate;//自己加的字段 1就要下载更新 下载完替换成0
    private String localImgPath;//自己加的字段
    private String localSvgPath;//自己加的字段

    @Generated(hash = 2108074312)
    public GiftResourceBean(Long id, String name, String ename, String gshort,
            int type, int mark, int needdiamond, String gitficon, String swf,
            int swftime, String compressUrl, int compress, int status, int sort,
            int showFront, String remark, Long createTime, Long updateTime,
            int localShouldUpdate, String localImgPath, String localSvgPath) {
        this.id = id;
        this.name = name;
        this.ename = ename;
        this.gshort = gshort;
        this.type = type;
        this.mark = mark;
        this.needdiamond = needdiamond;
        this.gitficon = gitficon;
        this.swf = swf;
        this.swftime = swftime;
        this.compressUrl = compressUrl;
        this.compress = compress;
        this.status = status;
        this.sort = sort;
        this.showFront = showFront;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.localShouldUpdate = localShouldUpdate;
        this.localImgPath = localImgPath;
        this.localSvgPath = localSvgPath;
    }

    @Generated(hash = 808526538)
    public GiftResourceBean() {
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

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getNeeddiamond() {
        return needdiamond;
    }

    public void setNeeddiamond(int needdiamond) {
        this.needdiamond = needdiamond;
    }

    public String getGitficon() {
        return gitficon;
    }

    public void setGitficon(String gitficon) {
        this.gitficon = gitficon;
    }

    public String getSwf() {
        return swf;
    }

    public void setSwf(String swf) {
        this.swf = swf;
    }

    public int getSwftime() {
        return swftime;
    }

    public void setSwftime(int swftime) {
        this.swftime = swftime;
    }

    public String getCompressUrl() {
        return compressUrl;
    }

    public void setCompressUrl(String compressUrl) {
        this.compressUrl = compressUrl;
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

    public String getLocalSvgPath() {
        return localSvgPath;
    }

    public void setLocalSvgPath(String localSvgPath) {
        this.localSvgPath = localSvgPath;
    }
}
