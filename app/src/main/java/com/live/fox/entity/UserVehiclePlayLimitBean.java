package com.live.fox.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserVehiclePlayLimitBean {

    @Id(autoincrement = true)
    private Long id;
    private String uid;
    private String liveId;
    private long showTime;
    private int type;//0 直播间 1主播端

    @Generated(hash = 1899800721)
    public UserVehiclePlayLimitBean(Long id, String uid, String liveId,
            long showTime, int type) {
        this.id = id;
        this.uid = uid;
        this.liveId = liveId;
        this.showTime = showTime;
        this.type = type;
    }
    @Generated(hash = 515585069)
    public UserVehiclePlayLimitBean() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getLiveId() {
        return this.liveId;
    }
    public void setLiveId(String liveId) {
        this.liveId = liveId;
    }
    public long getShowTime() {
        return this.showTime;
    }
    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
