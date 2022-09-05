package com.live.fox.entity;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.live.fox.BR;


public class Kefu extends BaseObservable {

    long customId;
    String nickname;
    String url;
    int type;
    String icon;

    public Kefu(String nickname, String url) {
        this.nickname = nickname;
        this.url = url;
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getCustomId() {
        return customId;
    }

    public void setCustomId(long customId) {
        this.customId = customId;
    }


}