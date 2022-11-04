package com.live.fox.entity;

import android.graphics.drawable.Drawable;

public class NobleEquityBean {

    private String title;
    private boolean show = true;
    private String tips;
    private Drawable img;
    private boolean showPhoto = false;
    private String ulr;

    public NobleEquityBean() {

    }

    public NobleEquityBean(String title, String tips, Drawable drawable,boolean show) {
        this.title = title;
        this.tips= tips;
        this.img = drawable;
        this.show = show;
    }

    public NobleEquityBean(String title, String tips, Drawable drawable) {
        this.title = title;
        this.tips= tips;
        this.img = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public boolean isShowPhoto() {
        return showPhoto;
    }

    public void setShowPhoto(boolean showPhoto) {
        this.showPhoto = showPhoto;
    }

    public String getUlr() {
        return ulr;
    }

    public void setUlr(String ulr) {
        this.ulr = ulr;
    }
}
