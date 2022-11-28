package com.tencent.demo.avatar;


/**
 * 用于存放拍照捏脸页面获取到的数据
 * 在拍照页面存储数据
 * 在捏脸页面使用数据
 */
public class CaptureAvatarDataManager {
    private static class ClassHolder {
        static final CaptureAvatarDataManager CAPTURE_AVATAR_DATA_MANAGER = new CaptureAvatarDataManager();

    }

    public static CaptureAvatarDataManager getInstance() {
        return ClassHolder.CAPTURE_AVATAR_DATA_MANAGER;
    }

    private String matchData =null;

    public String getMatchData() {
        return matchData;
    }

    public void setMatchData(String matchData) {
        this.matchData = matchData;
    }
}
