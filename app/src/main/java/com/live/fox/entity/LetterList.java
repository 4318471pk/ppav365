package com.live.fox.entity;


public class LetterList {

    private long loginUid;//登录人的uid
    private long otherUid;//另外个人的uid
    private long letterId; //消息id

    private String avatar;//另外个人的头像
    private String content;//信息内容
    private String nickname;//另外个人的昵称

    private int sex;//另外个人的性别
    private long timestamp;//时间戳
    private int userLevel;//另外个人的等级

    int unReadCount = 1; //未读数

    public long getOtherUid() {
        return otherUid;
    }

    public void setOtherUid(long otherUid) {
        this.otherUid = otherUid;
    }

    public long getLetterId() {
        return letterId;
    }

    public void setLetterId(long letterId) {
        this.letterId = letterId;
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

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public long getLoginUid() {
        return loginUid;
    }

    public void setLoginUid(long loginUid) {
        this.loginUid = loginUid;
    }
}
