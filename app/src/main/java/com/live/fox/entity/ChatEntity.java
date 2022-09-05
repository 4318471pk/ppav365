package com.live.fox.entity;

import android.text.Spannable;
import android.text.Spanned;

import org.json.JSONObject;

import java.io.Serializable;

public class ChatEntity implements Serializable {

    private Spanned content;
    private User user;
    private JSONObject jsonObject;
    private long anchorId;
    private int liveId;
    //消息在Adapter中的position
    private int adapterPos;

    boolean isVip = false;
    //高等级的进房消息
    private boolean isHighLeverEnterMsg = false;

    private int protocol;

    public ChatEntity(User user) {
        this.user = user;
    }

    public ChatEntity(Spannable content, User user) {
        this.content = content;
        this.user = user;
    }

    public void setValue(JSONObject jsonObject, long anchorId, int liveId, int adapterPos) {
        this.jsonObject = jsonObject;
        this.anchorId = anchorId;
        this.liveId = liveId;
        this.adapterPos = adapterPos;
    }

    public Spanned getContent() {
        return content;
    }

    public void setContent(Spanned content) {
        this.content = content;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public long getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(long anchorId) {
        this.anchorId = anchorId;
    }

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getAdapterPos() {
        return adapterPos;
    }

    public void setAdapterPos(int adapterPos) {
        this.adapterPos = adapterPos;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public boolean isHighLeverEnterMsg() {
        return isHighLeverEnterMsg;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public void setHighLeverEnterMsg(boolean highLeverEnterMsg) {
        isHighLeverEnterMsg = highLeverEnterMsg;
    }


    @Override
    public String toString() {
        return "ChatEntity{" +
                "content=" + content +
                ", user=" + user +
                ", jsonObject=" + jsonObject +
                ", anchorId=" + anchorId +
                ", liveId=" + liveId +
                ", adapterPos=" + adapterPos +
                ", isVip=" + isVip +
                ", isHighLeverEnterMsg=" + isHighLeverEnterMsg +
                '}';
    }
}
