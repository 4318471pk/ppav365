package com.live.fox.entity;

public class FunctionItem {
    private int resId;
    private int resSmall;
    public int circleBg;
    private String des;
    private String title;
    public String colorRes;

    public FunctionItem(int mResId, String mDes, String mTitle) {
        resId = mResId;
        des = mDes;
        title = mTitle;

    }

    public FunctionItem(int mResId, String mDes, String mTitle, String mColorRes) {
        resId = mResId;
        des = mDes;
        title = mTitle;
        colorRes = mColorRes;
    }

    public FunctionItem(int mResId, int mResSmall, String mDes, String mTitle, String mColorRes) {
        resId = mResId;
        resSmall = mResSmall;
        des = mDes;
        title = mTitle;
        colorRes = mColorRes;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int mResId) {
        resId = mResId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String mDes) {
        des = mDes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        title = mTitle;
    }

    public int getResSmall() {
        return resSmall;
    }

    public void setResSmall(int mResSmall) {
        resSmall = mResSmall;
    }
}
