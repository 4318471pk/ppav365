package com.live.fox;

public interface MessageProtocol {

    /**
     * 系统消息
     */
    public static final String SYSTEM_MAINTAIN = "1001";//系统维护信息
    public static final String SYSTEM_ADVERTISE = "1002";//系统广告
    public static final String SYSTEM_NOTICE = "1003";//系统公告

    /**
     * 直播间
     */
    public static final String LIVE_START_LIVE = "2001";//开播
    public static final String LIVE_STOP_LIVE= "2002";//关播
    public static final String LIVE_ANCHOR_LEAVE_BACK = "2003";//主播离开或返回
    public static final String LIVE_FOLLOW = "2004";//关注
    public static final String LIVE_ENTER_ROOM = "2005";//进房:{}来了
    public static final String LIVE_UP_LEVEL = "2006";//主播或用户 升级
    public static final String LIVE_ROOM_CHAT = "2007";//直播间聊天
    public static final String LIVE_SEND_GIFT = "2008";//送礼消息
    public static final String LIVE_BLACK_CHAT = "2009";//禁言消息
    public static final String LIVE_BLACK_CHAT_CANCEL = "2010";//取消禁言消息
    public static final String LIVE_ROOM_NOTICE = "2011";//直播间公告
    public static final String LIVE_BAN_USER = "2012";//直播间踢人消息
    public static final String LIVE_KICK_LIVE = "2013";//强制关播
    public static final String LIVE_ROOM_MANAGER_MSG = "2014";//直播间管理员消息
    public static final String LIVE_ROOM_SET_MANAGER_MSG = "2015";//直播间设置管理员通知
    public static final String GAME_CP_BET = "2016";//彩票下注消息
    public static final String GAME_CP_WIN = "2017";//彩票中奖消息
    public static final String LIVE_ROOM_CHAT_VIP = "2018";//直播间贵族聊天(大于一定等级的贵族)
    public static final String LIVE_ROOM_CHAT_FLOATING_MESSAGE = "2019";//弹幕消息

    /**
     * 弹幕消息
     */

    public static final String LIVE_INTER_ROOM_MODULE= "3001";//{xxx}通过{推荐}进入直播间-->大于一定等级

    public static final String LIVE_INTER_ROOM_VIP= "3002";//贵族{}进入直播间

    public static final String LIVE_INTER_ROOM_GUARD= "3003";//守护{}进入直播间大于一定等级

    public static final String LIVE_BUY_GUARD = "3004";//开通守护成功

    public static final String LIVE_BUY_VIP = "3005";//开通贵族成功

    public static final String GAME_WIN_LOG = "3006";//游戏跑马灯（彩票中奖消息大奖 大于一定金额）
    public static final String GAME_LOTTERY_RESULT = "3006";//彩票开奖结果推送消息


    /**
     * 个人中心
     */
    public static final String USER_LETTER = "4001";//私信
    public static final String GOLD_COIN_CHANGE = "5001";//资产变动消息
    /**
     * 资源
     */
    public static final String UPDATE_RES = "5002";//更新礼物资源消息


    /**
     * 推送相关
     */
    public static final String CHARGE_ROOM_CHANGE = "5003";//房间价格变动
    public static final String HANDED_SUBSIDY = "5004";//发放补贴成功消息

    /**
     * 暂时用不到
     */
    public static final String PK_START_STOP = "18";//PK开启关闭消息
    public static final String SEND_PK_COMM = "19";//PK请求响应消息
    public static final String CANCLE_PK = "22";//取消PK匹配
    public static final String PK_RESULT = "23";//PK结果消息
    public static final String PK_RANK = "24";//PK比分变动消息
}
