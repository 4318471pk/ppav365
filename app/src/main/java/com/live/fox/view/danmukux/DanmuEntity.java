package com.live.fox.view.danmukux;

import android.text.SpannableString;

public class DanmuEntity {
    public CharSequence content;
    private String nickname;
    private String avatar;
    public Integer avatarRes;
    public int type;
    private String tempLevel;
    private String avatarother;
    private String nicknameother;
    public SpannableString spannableStr;

    public String getNicknameother() {
        return nicknameother;
    }

    public void setNicknameother(String nicknameother) {
        this.nicknameother = nicknameother;
    }

    public String getAvatarother() {
        return avatarother;
    }

    public void setAvatarother(String avatarother) {
        this.avatarother = avatarother;
    }

    public CharSequence getContent() {
        return content;
    }

    public void setContent(CharSequence content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTempLevel() {
        return tempLevel;
    }

    public void setTempLevel(String tempLevel) {
        this.tempLevel = tempLevel;
    }

    @Override
    public String toString() {
        return "DanmuEntity{" +
                "content=" + content +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", type=" + type +
                ", tempLevel='" + tempLevel + '\'' +
                ", avatarother='" + avatarother + '\'' +
                '}';
    }
}
