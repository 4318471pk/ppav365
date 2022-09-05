package com.live.fox.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;


public class Letter implements Serializable,MultiItemEntity {

    private int type;//1系统通知2普通消息
    private int layout;//信息类型 0收消息 1发消息
    private long letterId; //消息id
    private long sendUid;//发送人uid
    private long otherUid;//接收人uid


    private String avatar;//发送人头像
    private String content;//信息内容
    private String nickname;//发送人昵称

    private int sex = 0;//发送人性别
    private long timestamp;//时间戳
    private int userLevel;//发送人等级
    private int statua;//1已读
    int unReadCount = 1; //未读数
    private int isRead = 1;

    @Override
    public int getItemType() {
        return getLayout();
    }

    public long getLetterId() {
        return letterId;
    }

    public void setLetterId(long letterId) {
        this.letterId = letterId;
    }

    public int getStatua() {
        return statua;
    }

    public void setStatua(int statua) {
        this.statua = statua;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getOtherUid() {
        return otherUid;
    }

    public void setOtherUid(long otherUid) {
        this.otherUid = otherUid;
    }

    public long getSendUid() {
        return sendUid;
    }

    public void setSendUid(long sendUid) {
        this.sendUid = sendUid;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }
}
