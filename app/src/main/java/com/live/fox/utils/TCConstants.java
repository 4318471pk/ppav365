package com.live.fox.utils;



/**
 * 靜態函數
 */
public class TCConstants {

    //小直播相關配置請參考:https://www.qcloud.com/document/product/454/7999
    //************在騰訊雲開通各項服務後，將您的配置替換到如下的幾個定義中************
    //雲通信服務相關配置
    public static final int IMSDK_ACCOUNT_TYPE = 0;
    public static final int IMSDK_APPID = 0;

    //COS存儲服務相關配置
    public static final String COS_BUCKET = "";
    public static final String COS_APPID = "0";
    //COS服務配置的機房區域，從COS的管理控制台https://console.qcloud.com/cos4/bucket進入Bucket列表後，選擇您所創建的Bucket->基礎配置->所屬地區，查到所屬地區後，根據如下
    //對應關系填入，如是“華南”請填寫COSEndPoint.COS_GZ，“華北”請填寫COSEndPoint.COS_TJ，“華東”請填寫COSEndPoint.COS_SH
//    public static final COSEndPoint COS_REGION = COSEndPoint.COS_SH;

    //雲API服務密鑰，在https://console.qcloud.com/capi查看，用于UGC短視頻上傳並落地到點播系統。已經廢棄，客戶端不用填寫。
    public static final String CLOUD_API_SECRETID = "";

    //業務Server的Http配置
    public static final String SVR_POST_URL = "http://fcgi.video.qcloud.com/common_access";

    //直播分享頁面的跳轉地址，分享到微信、手Q後點擊觀看將會跳轉到這個地址，請參考https://www.qcloud.com/document/product/454/8046 文檔部署html5的代碼後，替換成相應的頁面地址
    public static final String SVR_LivePlayShare_URL = "";
    //設置第三方平台的appid和appsecrect，大部分平台進行分享操作需要在第三方平台創建應用並提交審核，通過後拿到appid和appsecrect並填入這裏，具體申請方式請參考http://dev.umeng.com/social/android/operation
    //有關友盟組件更多資料請參考這裏：http://dev.umeng.com/social/android/quick-integration
    public static final String WEIXIN_SHARE_ID = "";
    public static final String WEIXIN_SHARE_SECRECT = "";

    public static final String SINA_WEIBO_SHARE_ID = "";
    public static final String SINA_WEIBO_SHARE_SECRECT = "";
    public static final String SINA_WEIBO_SHARE_REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";

    public static final String QQZONE_SHARE_ID = "";
    public static final String QQZONE_SHARE_SECRECT = "";

    //小直播appid
    public static final int XIAOZHIBO_APPID = 0;


    //bugly組件Appid，bugly爲騰訊提供的用于App Crash收集和分析的組件
//    public static final String BUGLY_APPID = "0";
    //**********************************************************************


    /**
     * 常量字符串
     */
    public static final String USER_INFO = "user_info";
    public static final String USER_ID = "user_id";
    public static final String USER_SIG = "user_sig";
    public static final String USER_NICK = "user_nick";
    public static final String USER_SIGN = "user_sign";
    public static final String USER_HEADPIC = "user_headpic";
    public static final String USER_COVER = "user_cover";
    public static final String USER_LOC = "user_location";
    public static final String SVR_RETURN_CODE = "returnValue";
    public static final String SVR_RETURN_MSG = "returnMsg";
    public static final String SVR_RETURN_DATA = "returnData";

    //主播退出廣播字段
    public static final String EXIT_APP = "EXIT_APP";

    public static final int USER_INFO_MAXLEN = 20;
    public static final int TV_TITLE_MAX_LEN = 30;
    public static final int NICKNAME_MAX_LEN = 20;

    //直播類型
    public static final int RECORD_TYPE_CAMERA = 991;
    public static final int RECORD_TYPE_SCREEN = 992;


    //碼率
    public static final int BITRATE_SLOW = 900;
    public static final int BITRATE_NORMAL = 1200;
    public static final int BITRATE_FAST = 1600;

    //直播端右下角listview顯示type
    public static final int TEXT_TYPE = 0;
    public static final int MEMBER_ENTER = 1;
    public static final int MEMBER_EXIT = 2;
    public static final int PRAISE = 3;

    public static final int LOCATION_PERMISSION_REQ_CODE = 1;
    public static final int WRITE_PERMISSION_REQ_CODE = 2;

    public static final String PUBLISH_URL = "publish_url";
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_TITLE = "room_title";
    public static final String COVER_PIC = "cover_pic";
    public static final String BITRATE = "bitrate";
    public static final String GROUP_ID = "group_id";
    public static final String PLAY_URL = "play_url";
    public static final String PLAY_TYPE = "play_type";
    public static final String PLAY_TITLE = "play_title";
    public static final String PLAY_NICKNAME = "play_nick_name";
    public static final String PUSHER_AVATAR = "pusher_avatar";
    public static final String PUSHER_ID = "pusher_id";
    public static final String PUSHER_NAME = "pusher_name";
    public static final String MEMBER_COUNT = "member_count";
    public static final String HEART_COUNT = "heart_count";
    public static final String FILE_ID = "file_id";
    public static final String TIMESTAMP = "timestamp";
    public static final String ACTIVITY_RESULT = "activity_result";
    public static final String SHARE_PLATFORM = "share_platform";

    public static final String CMD_KEY = "userAction";
    public static final String DANMU_TEXT = "actionParam";

    public static final String NOTIFY_QUERY_USERINFO_RESULT = "notify_query_userinfo_result";


    /**
     * UGC小視頻錄制信息
     */
    public static final String VIDEO_RECORD_TYPE = "type";
    public static final String VIDEO_RECORD_RESULT = "result";
    public static final String VIDEO_RECORD_DESCMSG = "descmsg";
    public static final String VIDEO_RECORD_VIDEPATH = "path";
    public static final String VIDEO_RECORD_COVERPATH = "coverpath";
    public static final String VIDEO_RECORD_ROTATION = "rotation";
    public static final String VIDEO_RECORD_NO_CACHE = "nocache";
    public static final String VIDEO_RECORD_DURATION = "duration";
    public static final String VIDEO_RECORD_RESOLUTION = "resolution";

    public static final int VIDEO_RECORD_TYPE_PUBLISH = 1;   // 推流端錄制
    public static final int VIDEO_RECORD_TYPE_PLAY = 2;   // 播放端錄制
    public static final int VIDEO_RECORD_TYPE_UGC_RECORD = 3;   // 短視頻錄制
    public static final int VIDEO_RECORD_TYPE_EDIT = 4;   // 短視頻編輯


    //ERROR CODE TYPE
    public static final int ERROR_GROUP_NOT_EXIT = 10010;
    public static final int ERROR_QALSDK_NOT_INIT = 6013;
    public static final int ERROR_JOIN_GROUP_ERROR = 10015;
    public static final int SERVER_NOT_RESPONSE_CREATE_ROOM = 1002;
    public static final int NO_LOGIN_CACHE = 1265;

    public static final int THUMB_COUNT = 10;
}
