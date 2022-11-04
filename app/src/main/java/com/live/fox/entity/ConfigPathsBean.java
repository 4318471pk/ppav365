package com.live.fox.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ConfigPathsBean implements Parcelable {

    /**
     * id : 1
     * appId : 1302158351
     * licenceUrl : http://license.vod2.myqcloud.com/license/v1/015ee77b4ad4f2341182e0044478fa39/TXLiveSDK.licence
     * licenceKey : 774be64ba03901a3a0824e5ce80705d8
     * apiKey : 5febe95cc47e8464cff03f528cd63dd1
     * authKey : 588828890727c07c55f684dee5c6aB03
     * bizid : 109534
     * name : 测试路线1
     * status : 1
     * pushUrl : rtmp://push1.sdshiya.club/live/%s
     * pullUrl : rtmp://pull1.sdshiya.club/live/%s
     * rtmp : rtmp://pull1.sdshiya.club/live/%s
     * operator : admin
     * remark : null
     * gmtCreate : 1597493280093
     * updateTime : null
     * type : 1
     */

    private int id;
    private String appId;
    private String licenceUrl;
    private String licenceKey;
    private String apiKey;
    private String authKey;
    private String bizid;
    private String name;
    private int status;
    private String pushUrl;
    private String pullUrl;
    private String rtmp;
    private String operator;
    private Object remark;
    private long gmtCreate;
    private Object updateTime;
    private int type;
    private int  choicePosition;

    protected ConfigPathsBean(Parcel in) {
        id = in.readInt();
        appId = in.readString();
        licenceUrl = in.readString();
        licenceKey = in.readString();
        apiKey = in.readString();
        authKey = in.readString();
        bizid = in.readString();
        name = in.readString();
        status = in.readInt();
        pushUrl = in.readString();
        pullUrl = in.readString();
        rtmp = in.readString();
        operator = in.readString();
        gmtCreate = in.readLong();
        type = in.readInt();
        choicePosition = in.readInt();
    }

    public static final Creator<ConfigPathsBean> CREATOR = new Creator<ConfigPathsBean>() {
        @Override
        public ConfigPathsBean createFromParcel(Parcel in) {
            return new ConfigPathsBean(in);
        }

        @Override
        public ConfigPathsBean[] newArray(int size) {
            return new ConfigPathsBean[size];
        }
    };

    public int getChoicePosition() {
        return choicePosition;
    }

    public void setChoicePosition(int choicePosition) {
        this.choicePosition = choicePosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLicenceUrl() {
        return licenceUrl;
    }

    public void setLicenceUrl(String licenceUrl) {
        this.licenceUrl = licenceUrl;
    }

    public String getLicenceKey() {
        return licenceKey;
    }

    public void setLicenceKey(String licenceKey) {
        this.licenceKey = licenceKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getBizid() {
        return bizid;
    }

    public void setBizid(String bizid) {
        this.bizid = bizid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public String getPullUrl() {
        return pullUrl;
    }

    public void setPullUrl(String pullUrl) {
        this.pullUrl = pullUrl;
    }

    public String getRtmp() {
        return rtmp;
    }

    public void setRtmp(String rtmp) {
        this.rtmp = rtmp;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(appId);
        dest.writeString(licenceUrl);
        dest.writeString(licenceKey);
        dest.writeString(apiKey);
        dest.writeString(authKey);
        dest.writeString(bizid);
        dest.writeString(name);
        dest.writeInt(status);
        dest.writeString(pushUrl);
        dest.writeString(pullUrl);
        dest.writeString(rtmp);
        dest.writeString(operator);
        dest.writeLong(gmtCreate);
        dest.writeInt(type);
        dest.writeInt(choicePosition);
    }
}
