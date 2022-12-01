package com.live.fox;

public interface ConstantValue {

    String SwitchStatus="SwitchStatus";
    String GesturePassword="GesturePassword";


    /**
     * activity Request
     */
    public static final int REQUEST_CODE1 = 10001;
    public static final int REQUEST_CODE2 = 10002;
    public static int REQUEST_CODE3 = 10003;
    public static int REQUEST_CODE4 = 10004;
    public static int REQUEST_CROP_PIC=10005;

    /**
     * activity 结果
     */
    int RESULT_CODE1 = 20001>>1;
    int RESULT_CODE2 = 20002>>2;
    int RESULT_CODE3 = 20003>>3;
    int RESULT_CODE4 = 20004>>4;
    int GUEST_BINDPHONE = 20005;
    int RESULT_CROP_PIC=20006;

    public static String hasGuestLogin = "hasGuestLogin";
    public static String isKickOut = "isKickOut";
    String BaseDomain ="BaseDomain";//基础网关
    String SPDefaultName ="aibo";//sharePreference默认名字
    String BulletMessageList ="BulletMessageList";//弹幕列表
    String gameCategoryTitles="gameTitles";//游戏种类
    String liveBeforeGames="liveBeforeGames";//查询开播前游戏列表

    public static final String IM_SDK_APP_ID = "im sdk app id key";
    public static final String ACCESS_ID = "ACCESS_ID";
    public static final String ACCESS_KEY = "ACCESS_KEY";
    public static final String shareUrl = "shareUrl";
    public static final String NOTIFICATION_IS_SHOWED = "notification is showed";
    public static final String pictureOfUpload="pictureOfUpload";
    String resourceDomain="resourceDomain";//资源域名
    String searchHistory="searchHistory";//搜索历史
    String watchedHistory="watchedHistory";//观看历史
    String JumpType="JumpType";
}
