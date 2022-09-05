package com.live.fox.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class WebViewDialogEntity implements Parcelable {

    private String title;
    private String webUrl;

    public WebViewDialogEntity() {

    }

    protected WebViewDialogEntity(Parcel in) {
        title = in.readString();
        webUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(webUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WebViewDialogEntity> CREATOR = new Creator<WebViewDialogEntity>() {
        @Override
        public WebViewDialogEntity createFromParcel(Parcel in) {
            return new WebViewDialogEntity(in);
        }

        @Override
        public WebViewDialogEntity[] newArray(int size) {
            return new WebViewDialogEntity[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
