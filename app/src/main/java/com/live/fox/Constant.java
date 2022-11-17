package com.live.fox;

import com.live.fox.entity.Anchor;
import com.live.fox.utils.FileUtils;
import com.tencent.rtmp.TXLivePlayer;

import java.io.File;

/**
 * Created  Administrator on 2018/2/26.
 */

public class Constant {

    /**
     * IS_PUBLISH 当前APP true正式版 false测试版
     * IS_SHOWLOG 当为true时，记得设置所有Log不在控制台打印 LogUtils.getConfig().setLogSwitch(false);
     */
    public static final boolean isPublish = BuildConfig.IS_PUBLISH;
    public static final boolean isShowLog = BuildConfig.IS_SHOWLOG;

    /**
     * 建议测试时关闭 正式服开启
     * 是否进行异常拦截 开启后 APP奔溃率会大大降低  但会
     * 影响bugly的提交率
     */
    public static final boolean isCarsh = BuildConfig.IS_CARSH;

    //测试服地址 正式服地址
    public static final String BASEURL_TEST = BuildConfig.BASEURL_TEST;

    /**
     * 基本配置
     */
    public static final int OS = BuildConfig.OS; //(1安卓用户端2苹果用户端3安卓主播端4苹果用户端)
    public static String LiveAppId;
    public static final String ToyToken = BuildConfig.ToyToken;
    public static final boolean isOpenBeauty = true;
    public static final int pageSize = 20;
    public static final int pageSizeLater = 10;

    //贵族
    public static final int LEVEL1 = 1;
    public static final int LEVEL2 = 2;
    public static final int LEVEL3 = 3;
    public static final int LEVEL4 = 4;
    public static final int LEVEL5 = 5;
    public static final int LEVEL6 = 6;
    public static final int LEVEL7 = 7;

    //推送
    public static final int TEST_LOCAL_NOTIFICATION = 1;
    public static final int TEST_NOTIFICATION = 2;
    public static final int TEST_SET_TAG = 3;
    public static final int TEST_DEL_TAG = 4;
    public static final int TEST_SET_ACCOUNT = 5;
    public static final int TEST_DEL_ACCOUNT = 6;
    public static final String LOCAL_NOTIFICATION_TITLE = "localtest";

    //关于app
    public static Anchor windowAnchor;
    public static boolean isPK = false;
    public static TXLivePlayer mTXLivePlayer;
    public static boolean isOpenWindow = false; //小窗口是否已经开启
    public static boolean isShowWindow = false; //关闭的时候是否打开小窗口
    public static boolean isAppInsideClick = false;//是否app内部点击

    public static class URL {
        //config-client 下面的接口 域名地址是不变的
        public static final String BASE = "/config-client/base";
        public static final String BASE_baseinfo_URL = BASE + "/baseInfo";        //基础信息
        public static final String BaseInfoWithToken=BASE+"/baseInfo/Auth";//基础信息需要验证
        public static final String BaseResource="/config-client/base/baseInfo/resource";//基础资源信息
        public static final String BASE_ossToken_URL = BASE + "/ossToken";        //oss上传凭证
        public static final String BASE_version_URL = BASE + "/version";          //版本信息
        public static final String USER_getbanklist_URL = "/config-client/config-client/base/userBankList";        // 得到银行信息
        public static final String USER_getrule_URL = "/config-client/config-client/base/withdrawRule";        // 提现规则接口
        public static final String USERACTIVITY_URL = BASE + "/userActivity"; //用户活动列表

        //给App下发基础配置接口
        public static final String Config = "config-client/config";
        public static final String Config_badge_URL = Config + "/badge";            // 获取徽章列表
        public static final String Config_cserver_URL = Config + "/cserver";        // 客服列表
        public static final String Config_notice_URL = Config + "/notice";          // 消息公告
        public static final String Config_systemNotice_URL =  "/config-client/notice/systemNoticesByType";          // 消息公告 首页banner等等
        public static final String Config_pay_URL = Config + "/pay";                // 支付渠道列表
        public static final String Config_prop_URL = Config + "/prop";              // 道具列表
        public static final String CONFIG_ADMISSION_URL = Config + "/level/prop";    // 入场动画
        public static final String Config_sysnotice_URL = Config + "/system/notice";// 获取官方公告列表
        public static final String Config_gfnotice_URL = "/center-client/live/systemLetter/list";// 获取系统公告列表
        public static final String Config_tag_URL = Config + "/tag";                // 主播标签
        public static final String Config_game_URL = Config + "/game";              // 游戏列表
        public static final String Config_indexlist_URL = Config + "/index/list";   // 首页游戏列表
        public static final String Config_taglist_URL = Config + "/tag/list";       // 开源游戏列表
        public static final String Config_detaillist_URL = Config + "/detail/list"; // 开源游戏列表 parentId 查询全部的时候parentId传-1
        public static final String CONFIG_CP_URL = Config + "/cp/list"; // 开源游戏列表 parentId 查询全部的时候parentId传-1
        public static final String CONFIG_VIP_URL = Config + "/vip";
        public static final String CONFIG_PATHS_URL = "/config-client/liveConfig/livePathConfig";

        //center-client 下面的接口 用户相关接口
        public static final String CenterClient = "/center-client";
        public static final String USER_RED_BAG_GET = CenterClient + "/red/grabRedPacket";       //红包雨=》抢红包
        public static final String USER_RED_BAG_RAIN_INFO = CenterClient + "/red/joinRoom";       //红包雨=》按钮展示
        public static final String USER_userInfo_URL = CenterClient + "/user/get/info";          // 获取用户信息
        public static final String USER_cardInfo_URL = CenterClient + "/sys/user/get/card/info";     // 获取用户名片信息
        public static final String USER_followlist_URL = CenterClient + "/user/follow/list";     // 我的关注列表
        public static final String USER_follow_URL = CenterClient + "/user/follow";     //关注
        public static final String USER_fanslist_URL = CenterClient + "/user/fans/list";         // 我的粉丝列表
        public static final String USER_modifyuserinfo_URL = CenterClient + "/user/modify/user/info";    // 修改用户信息
        public static final String USER_modifyusername_URL = CenterClient + "/user/modify/nickname";    // 修改用户昵称
        public static final String USER_reject_URL = CenterClient + "/sys/user/reject";              // 设置/取消 黑名单
        public static final String USER_rejectlist_URL = CenterClient + "/sys/user/reject/list";     // 黑名单列表
        public static final String USER_auth_URL = CenterClient + "/sys/user/user/auth";             // 用户认证
        public static final String USER_bindbank_URL = CenterClient + "/sys/user/user/bank";             // 用户绑定或修改银行卡
        public static final String USER_getbankinfo_URL = CenterClient + "/sys/user/user/bank/selected";        // 获取用户绑定银行卡信息
        public static final String USER_getcardinfo_URL = CenterClient + "/sys/user/bankList/Info";        // 获取用户已绑定银行卡信息
        public static final String USER_find_URL = CenterClient + "/sys/user/find";        // 搜索用户
        public static final String USER_VIPINFO_URL = CenterClient + "/sys/user/get/vip/info";        // 搜索用户
        public static final String DEL_MESSAGE_URL = CenterClient + "/live/del/letter";        // 删除私信
        public static final String CHANGE_BANKCARD_URL = CenterClient + "/sys/user/user/switchoverBank";        // 改变银行卡
        public static final String Net_Eas_VERIFY = CenterClient + "/sys/user/captcha";        // 获取网易云验证方式

        //授权相关接口
        public static final String CountryCodeURl=CenterClient +"/auth/area/list";//国家对应手机前缀号码
        public static final String Auth_Guest_Login=CenterClient +"/auth/tourists/login";//游客登录
        public static final String AUTH_PHONELOGIN_URL = CenterClient + "/auth/phone/login";  // 手机号密码登录
        public static final String AUTH_PHONEREQ_URL = CenterClient + "/auth/phone/reg/codeValidate";      // 验证码校验接口
        public static final String AUTH_SENDVCODE_URL = CenterClient + "/auth/send/vcode";    // 验证码发送
        public static final String AUTH_GUEST_BIND_PHONE=CenterClient+"/user/touristsBindPhone";//游客绑定手机号码
        public static final String AUTH_SENDVCODE_URL2 = CenterClient + "/user/cash/sendCashCode";    // 绑定银行卡验证码发送
        public static final String AUTH_VCODELOGIN_URL = CenterClient + "/auth/vcode/login";  // 验证码快捷登录
        public static final String AUTH_RESETPWD_URL = CenterClient + "/auth/reset/pwd";      // 忘记密码-密码重置
        public static final String AUTH_PHONEREGINFO_URL = CenterClient + "/auth/phone/reg/info"; //完善信息
        public static final String AUTH_CheckToken_URL = CenterClient + "/sys/auth/check/token"; //检查Token是否有效
        public static final String AUTH_DETERPWD_URL = CenterClient + "/sys/user/determine/pwd";//判断是否设置密码
        public static final String AUTH_ISREGISTE_URL = CenterClient + "/sys/user/phone/isRegiste";//判断手机号是否注册接口
        public static final String UPLOAD_USER_PGOTO_URL = "/config-client/file/uploadFileAvatar";//上传头像文件接口；
        public static final String UPLOAD_BG_LIVING_ROOM = "/config-client/file/uploadFileCover";//oss上传直播间封面；

        public static final String NOTICE_URL = "/config-client/notice/systemNoticesByType";
        public static final String ACT_URL = "/pk-client/activity/activityListByCategory";
        public static final String GurardOpen="/pk-client/guard/buyOrRenewalGuard";//购买、续费守护
        public static final String GurardAvailableList="/pk-client/guard/list";//查询可购买守护列表
        public static final String queryGuardListByAnchor="/pk-client/guard/liveGuardListByAid";//根据主播id查询守护列表、守护总人数


        //用户下的直播相关接口
        public static final String LIVE_blackUser_URL = CenterClient + "/live/blackUser";  //封号、封终端
        public static final String LIVE_follow_URL = CenterClient + "/live/follow";       //关注/取关
        public static final String LIVE_getfamily_URL = CenterClient + "/live/get/family";  //获取家族信息
        public static final String LIVE_isauth_URL = CenterClient + "/live/is/auth";        //主播状态
        public static final String LIVE_letter_URL = CenterClient + "/live/letter";     //用户私信
        public static final String READ_letter_URL = CenterClient + "/live/change/letterStatua";     //用户私信
        public static final String LIVE_letterlist_URL = CenterClient + "/live/userLetter/list";      //获取私信列表

        //支付密码
        public static final String setPaymentPassword = CenterClient + "/user/setPayPwd";     //设置支付密码
        public static final String resetPaymentPassword = CenterClient + "/user/resetPayPwd";     //重置密码
        public static final String modifyPaymentPassword = CenterClient + "/user/updatePayPwd";     //修改密码

        //用户道具相关接口
        public static final String USER_propbuyCar_URL = CenterClient + "/user/prop/buyCar";    //购买座驾
        public static final String USER_propcar_URL = CenterClient + "/user/prop/car";   //获取用户座驾信息
        public static final String USER_propsetShowCar_URL = CenterClient + "/user/prop/setShowCar";  //设置直播间展示座驾

        //用户提现相关接口
        public static final String USER_game_URL = CenterClient + "/user/game";       //刷新用户游戏额度
        public static final String USER_statement_URL = CenterClient + "/user/statement";     //查询玩家流水
        public static final String USER_withdraw_URL = CenterClient + "/user/withdraw";       //用户提现
        public static final String USER_withdrawlist_URL = CenterClient + "/user/withdraw/list";//提现记录列表
        public static final String USER_jzlist_URL = CenterClient + "/user/withdraw/family/list";//家族提现记录列表
        public static final String USER_subsidylist_URL = CenterClient + "/user/withdraw/subsidy";//主播明细
        //用户-游戏相关
        public static final String USER_autoUpdownBalance_URL = CenterClient + "/sys/user/autoUpdownBalance";   //设置用户金币自动转换 1 不自动 2自动
        public static final String USER_backAllGameCoin_URL = CenterClient + "/sys/user/backAllGameCoin";       //一键回收所有用户游戏金币/bggame-client/user/bGame/backAllGameCoin

        //用户资产相关接口
        public static final String USER_changecoin_URL = CenterClient + "/assets/change/coin";       //魅力值变成coin
        public static final String USER_changecoinlist_URL = CenterClient + "/assets/change/list";   //魅力值变成coin列表
        public static final String USER_liveUserAssets = CenterClient + "/assets/getLiveUserAssets"; //用户资产
        public static final String USER_chargeCenter= CenterClient + "/assets/recharge/centre"; //充值列表
        public static final String USER_chargeCenter_list= CenterClient + "/assets/recharge/channel/list"; //充值渠道
        public static final String USER_chargeCenter_type= CenterClient + "/assets/recharge/channel/type"; //充值类型
        public static final String USER_withDraw_type= CenterClient + "/assets/withdraw/channel/type"; //提现type
        public static final String USER_withDraw_list= CenterClient + "/assets/withdraw/channel/list"; //提现list
        public static final String USER_AssetsInfo= CenterClient + "/assets/info"; //获取用户金币余额、钻石余额、累计送出等信息
        public static final String USER_diamondList= CenterClient + "/assets/diamonds/list"; //钻石兑换列表
        public static final String USER_diamondExchange= CenterClient + "/assets/diamonds/redeem"; //钻石兑换
        public static final String USER_userBankInfo= CenterClient + "/user/user/bank/info"; //用户银行卡信息
        public static final String USER_userBankList= CenterClient + "/user/bankList/Info"; //用户银行卡信息列表
        public static final String USER_userAddBank= CenterClient + "/user/user/bank"; //用户添加银行卡
        public static final String USER_bankList= CenterClient + "/user/getBankList"; //银行名称列表
        public static final String USER_userStoreList= CenterClient + "/assets/store/list"; //商店列表
        public static final String USER_deleteBank = CenterClient + "/user/user/deleteBankCard"; //删除银行卡
        public static final String USER_setNorenBank = CenterClient + "/user/user/switchoverBank"; //设置默认银行卡
        public static final String USER_buyCar= CenterClient + "/assets/buy/car"; //买座驾
        public static final String USER_openCar= CenterClient + "/assets/mount/enable"; //启用座驾
        public static final String USER_closeCar= CenterClient + "/assets/mount/disable"; //禁用座驾
        public static final String USER_bagList= CenterClient + "/assets/backpack/list"; //我的背包
        public static final String USER_withDraw= CenterClient + "/user/withdraw"; //提现
        public static final String USER_withDrawRecord= CenterClient + "/assets/withdraw/record"; //提现记录
        public static final String USER_diamondRecord= CenterClient + "/assets/profiex/record"; //钻石收入记录
        public static final String USER_diamondRecordExpend= CenterClient + "/assets/consum/record"; //钻石支出记录
        public static final String USER_getNobleList =  "/vip/getNobleVipList"; //貴族列表
        public static final String USER_getMyNoble = "/pk-client/vip/getMyNobility"; // 我的貴族
        public static final String USER_buyNoble = "/pk-client/vip/buyOrRenewalVip"; // 购买续费貴族 /pk-client/vip/buyOrRenewalVip

        //live-client 直播相关接口
        public static final String amountOfSpeaker="/live-client/live/getSpeakersNumber";//喇叭数量
        public static final String BulletMessageList="/live-client/live/getBarrageList";//弹幕列表
        public static final String sendBulletMessage="/live-client/live/room/barrage/send";//发送弹幕消息
        public static final String ContributionDaily="/live-client/live/anchor/contribute/daily";//今日主播贡献榜: 日榜、
        public static final String GiftList="/live-client/live/queryGift";//礼物列表
        public static final String LiveClient = "/live-client";
        public static final String reportAnchor="/live-client/live/insertComplaintInfo";//举报主播
        public static final String vipOnlineList="/live-client/live/room/vip/online";//房间在线贵族列表(显示全部)
        public static final String SendGiftAmountList="/live-client/live/gift/setting/list";
        public static final String getAnchorCard="/live-client/live/getAnchorBusinessCard";//获取主播名片
        public static final String liveRecommendURL="/live-client/home/anchor/recommend/list";//推荐主播列表
        public static final String liveRoomRecommendList="/live-client/live/recommendLiveList";//直播间内右侧列表
        public static final String anchorfind_URL = LiveClient + "/live/StartRecordList";        // 搜索主播
        public static final String anchorfind_URL2 = LiveClient + "/live/StartRecordTotalList";        // 搜索主播2
        public static final String Live_banuser_URL = LiveClient + "/live/ban/user";           //直播间踢用户
        public static final String Live_blackchat_URL = LiveClient + "/live/black/chat";       //直播间禁言
        public static final String Live_chargeroom_URL = LiveClient + "/live/charge/room";     //计时房间付费
        public static final String Live_chargeroomchange_URL = LiveClient + "/live/charge/room/change"; //更改房间类型
        public static final String LIVE_CHECK_INTER_ROOM_URL = LiveClient + "/live/inter/roomPreview";     //获取直播间是否可预览
        public static final String Live_interroom_URL = LiveClient + "/live/inter/room";        //用户进房
        public static final String Live_kicklive_URL = LiveClient + "/live/kick/live";          //超管关播
        public static final String Live_leaveswitch_URL = LiveClient + "/live/leave/switch";   //主播离开状态更改
        public static final String Live_chat_URL = LiveClient + "/live/room/chat/send";              //直播间发言
        public static final String Live_heart_URL = LiveClient + "/live/heart";            //主播心跳 30s
        public static final String Live_list_URL = LiveClient + "/home/channel/list";              //开播列表
        public static final String Live_recordlist_URL = LiveClient + "/live/record/list";  //开播记录列表
        public static final String Live_start_URL = LiveClient + "/live/start";            //开播
        public static final String Live_stop_URL = LiveClient + "/live/stop";              //关播
        public static final String Live_outerroom_URL = LiveClient + "/live/outer/room";        //退房
        public static final String Live_roominfo_URL = LiveClient + "/live/room/info";          //房间人员列表
        public static final String Live_sendgift_URL = LiveClient + "/live/send/gift";          //直播间送礼
        public static final String Live_watchheart_URL = LiveClient + "/live/watch/heart";      //用户心跳 40s
        public static final String Live_anchorinfo_URL = LiveClient + "/live/room/anchor/base"; //主播信息
        public static final String Live_roomuserlist_URL = LiveClient + "/live/room/user/list"; //房间人员列表(显示全部)
        public static final String Live_toychange_URL = LiveClient + "/live/toy/change"; //更改玩具状态
        public static final String Live_roommanager_URL = LiveClient + "/live/roommanager"; //房管添加取消
        public static final String Live_rocketlist_URL = LiveClient + "/live/rocketlist"; //火箭位列表
        public static final String LIVE_VIPLIST_URL = LiveClient + "/live/room/user/viplist"; //房间人员列表(显示全部)
        public static final String LIVE_ROOM_PERMISSIONS_URL = LiveClient + "/live/charge/room/chLoginP"; //房间人员列表(显示全部)
        public static final String LIVE_AnchorCenter ="/live-client/live/getAnchorCenterInfo"; //获取主播联系方式

        /**
         * promotion-client  分享相关接口
         */
        public static final String PromotionClient = "/promotion-client";
        public static final String Promotion_exchange_URL = PromotionClient + "/user/exchange";        //兑换金币
        public static final String Promotion_index_URL = PromotionClient + "/user/index";              //各种余额以及分享人数(总收益,充值返利收益,邀请好友收益)
        public static final String Promotion_sharelog_URL = PromotionClient + "/user/share/log";       //查看邀请人记录
        public static final String Promotion_withdraw_URL = PromotionClient + "/user/withdraw";        //分享收益提现
        public static final String Promotion_withdrawlog_URL = PromotionClient + "/user/withdraw/log"; //查看提现记录
        public static final String Promotion_install_URL = "promotion-client/stat/install";         //第一次安装统计

        /**
         * fuse-client PK相关接口
         */
        public static final String LiveRecreation = "/live-recreation";
        public static final String PK_acceptlist_URL = LiveRecreation + "/pk/accept/list";
        public static final String PK_canclereq_URL = LiveRecreation + "/pk/cancle/req";
        public static final String PK_finish_URL = LiveRecreation + "/pk/finish";
        public static final String PK_mergestream_URL = LiveRecreation + "/pk/merge/stream";
        public static final String PK_sendreq_URL = LiveRecreation + "/pk/send/req";//随机pk请求
        public static final String PK_sendrsp_URL = LiveRecreation + "/pk/send/rsp";//接受或者拒绝pk请求
        public static final String PK_setPkStaus_URL = LiveRecreation + "/pk/setPkStaus";
        public static final String PK_status_URL = LiveRecreation + "/pk/status";
        public static final String USER_VIPHIDE_URL = LiveRecreation + "/vip/hide";        // 搜索用户
        public static final String USER_VIPBUY_URL = LiveRecreation + "/vip/buyVip";        // 搜索用户
        public static final String USER_RENEWVIP_URL = LiveRecreation + "/vip/reNewVip";        // 搜索用户
        public static final String USER_VIPUP_URL = LiveRecreation + "/vip/upVip";        // 搜索用户

        /**
         * pay-client 下面的接口
         */
        //游戏相关接口
        public static final String PayClient = "/pay-client";
        public static final String Pay_getgame_URL = PayClient + "/get/game";           //直播APP调起游戏大厅
        public static final String Pay_kickout_URL = PayClient + "/kickout";            //通知玩家退出
        public static final String Pay_betRank_URL = PayClient + "/betRank";            //投注流水排行
        public static final String Pay_rankdetail_URL = PayClient + "/rank/detail";     //投注流水排行

        /**
         * fuse-client 下面的接口
         */
        //排行榜相关接口
        public static final String FuseClient = "/fuse-client";
        public static final String RANK_anchorlist_URL = FuseClient + "/rank/anchor/list";       //主播个人榜单
        public static final String RANK_list_URL = FuseClient + "/rank/list";         //榜单列表
        public static final String RANK_detail_URL = FuseClient + "/rank/rank/detail";  //榜单详情

        /**
         * risk-client 下面的接口
         */
        //风控
        public static final String RiskClient = "/risk-client";
        public static final String Risk_blackUser_URL = RiskClient + "/risk/blackUser";       //封号、封终端

        /**
         * kygame-client 下面的接口
         */
        public static final String KyGameClient = "/kygame-client";
        public static final String KyGame_login_URL = KyGameClient + "/login";       //登录游戏
        public static final String KyGame_logout_URL = KyGameClient + "/logout";     //退出游戏
        //        public static final String KyGame_userinfo_URL = KyGameClient + "/userInfo"; //查询玩家分数
        public static final String KyGame_balance_URL = KyGameClient + "/user/balance"; //查询可下分 不必传参数，后台通过token直接获取当前用户
        public static final String KyGame_balanceDown_URL = KyGameClient + "/user/balanceDown";  //下分
        public static final String KyGame_balanceUp_URL = KyGameClient + "/user/balanceUp";      //上分

        /**
         * bggame-client 下面的接口
         */
        public static final String AgGameClient = "/bggame-client/user/bGame";
        public static final String AgGame_forwardGame_URL = AgGameClient + "/user/forwardGame";       //为前端返回跳转游戏的url
        public static final String AgGame_logout_URL = AgGameClient + "/user/logout";     //退出游戏
        public static final String AgGame_getbalance_URL = AgGameClient + "/getbalance";         //查询余额
        public static final String AgGame_balanceIn_URL = AgGameClient + "/creditIn";    //预备转出 代入
        public static final String AgGame_balanceOut_URL = AgGameClient + "/creditOut";  //预备转出 代出

        /**
         * Saba-client 下面的接口
         */
        public static final String SabaClient = "/sbgame-client/user/sbGame";
        public static final String Saba_forwardGame = SabaClient + "/forwardGame";       //为前端返回跳转游戏的url
        public static final String Saba_logout_URL = SabaClient + "/logout";     //退出游戏
        public static final String Saba_get_balance = SabaClient + "/getbalance";     //查询余额
        public static final String Saba_balanceIn_URL = SabaClient + "/creditIn";    // 代入
        public static final String Saba_balanceOut_URL = SabaClient + "/creditOut";  // 代出

        /**
         * 体育 下面的接口
         */
        public static final String TYGameClient = "/cmdgame-client/user/cmdGame";
        public static final String TYGame_forwardGame_URL = TYGameClient + "/forwardGame";       //为前端返回跳转游戏的url
        public static final String TYGame_logout_URL = TYGameClient + "/logout";     //退出游戏
        public static final String TYGame_getbalance_URL = TYGameClient + "/getbalance";         //查询余额
        public static final String TYGame_balanceIn_URL = TYGameClient + "/creditIn";    //预备转出 代入
        public static final String TYGame_balanceOut_URL = TYGameClient + "/creditOut";  //预备转出 代出

        /**
         * order 下面的接口
         */
        public static final String OrderClient = "/order";
        public static final String Order_payvipchannel_URL = OrderClient + "/pay/vip/channel";       //vip充值通道列表
        public static final String ORDER_AGENT_URL = OrderClient + "/pay/agent/list";//"代理充值列表";
        public static final String ORDER_BANK_URL = OrderClient + "/pay/bank/list1";//"银行卡
        public static final String ORDER_BANK_RECHARGE_URL = OrderClient + "/pay/bank/recharge";//"银行卡 转账";
        public static final String ORDER_USDT_RECHARGE_URL = OrderClient + "/pay/ustd/recharge";//"银行卡 转账";

        /**
         * cp 下面的接口
         */
//        public static final String CP_CLIENT = "/cp-client";
//        public static final String CP_GETISSUE = CP_CLIENT + "/cpuser/getissue";       //获得cp期号
//        public static final String CP_GETLOTTERY = CP_CLIENT + "/cpuser/getlottery";// 获取往期开奖情况
//        public static final String CP_GOPAY = CP_CLIENT + "/ssc/gopay";             // 时时彩下注
//        public static final String CP_geth5url = CP_CLIENT + "/ssc/geth5url";      // 获取cph5地址
//        public static final String CP_CLIENT = "/cp-client";
        public static final String CP_GETISSUE = "/lottery-client/lottery/getissue";       //获得cp期号
        public static final String CP_GETLOTTERY = "/lottery-client/lottery/getHistorLottery";// 获取往期开奖情况
        public static final String CP_GETHNLOTTERY = "/lottery-client/lottery/getHistorLottery_YN_HNCP";// 获取hn往期开奖情况
        public static final String CP_GOPAY = "/lottery-client/lottery/lotteryBet";             // 时时彩下注
        //        public static final String CP_GETISSUE = CP_CLIENT + "/cpuser/getissue";       //获得cp期号
//        public static final String CP_GETLOTTERY = CP_CLIENT + "/cpuser/getlottery";// 获取往期开奖情况
//        public static final String CP_GOPAY = CP_CLIENT + "/ssc/gopay";             // 时时彩下注
        public static final String CP_geth5url = "/lottery-client/h5/geth5url";      // 获取cph5地址  /ssc/geth5url
        public static final String CPLatestResult = "/lottery-client/lottery/getAllLotteryLatestResult";      //获取所有彩票最近一期开奖结果
        public static final String ResultHistoryByName = "/lottery-client/lottery/getLotteryResultHistoryByName";   //获取彩票开奖历史（根据彩票名称）
        public static final String BetHistorByUid = "/lottery-client/lottery/getBetHistorByUid";   //获取个人投注记录
        public static final String pushResultMsgFlag = "/lottery-client/lottery/setPushResultMsgFlag";//直播间设置彩票弹框
        public static final String lotteryResultHistory = "/lottery-client/lottery/getLotteryTypeHistory"; //获取用户彩票游戏统计记录（最多查询近七天数据 type： 0：今天，1：昨天，2：近七天）
        public static final String lotteryResultHistoryByName = "/lottery-client/lottery/getLotteryDetailsHistory";//获取用户彩票游戏记录（最多查询近七天数据 type： 0：今天，1：昨天，2：近七天,3：近三天：4：30天查询类型queryType 0: 全部 ,1:未开奖, 2:未中奖, 3：已中奖）


        /**
         * kygame-client 下面的接口
         */
        public static final String FwGameClient = "/fwgame-client";
        public static final String BgGameClient = "/bggame-client/user/bGame";
        public static final String FwGame_login_URL = FwGameClient + "/login";       //登录游戏
        public static final String BGGame_login_URL = BgGameClient + "/forwardGame";       //bg登录游戏
        public static final String FwGame_logout_URL = FwGameClient + "/logout";     //退出游戏
        public static final String BgGame_logout_URL = BgGameClient + "/logout";     //bg退出游戏
        public static final String FwGame_balance_URL = FwGameClient + "/user/balance"; //查询可下分 不必传参数，后台通过token直接获取当前用户
        public static final String FwGame_balanceDown_URL = FwGameClient + "/user/balanceDown";  //下分
        public static final String FwGame_balanceUp_URL = FwGameClient + "/user/balanceUp";      //上分

        //资产记录
        public static final String ASSET_TYPE = "/center-client/sys/user/asset/getAssetType";
        public static final String ASSET_TYPE_DETAIL = "/center-client/sys/user/asset/record";

    }

    /**
     * IM 消息
     */
    public static class MessageProtocol {

        public static final int PROTOCOL_SYSTEM = 0; //系统提消息

        public static final int PROTOCOL_MAINTAIN = 1; //维护消息

        public static final int PROTOCOL_LIVE_CLOSE = 2;//关播/强制关播消息

        public static final int PROTOCOL_ANCHOR_SWITCH = 3;  //主播离开或返回消息

        public static final int PROTOCOL_FOCUS = 4;  //关注

        public static final int PROTOCOL_AUDIENCE = 5;  //进房或退房

        public static final int PROTOCOL_LEVEL_UP = 6;  //主播或用户 升级

        public static final int PROTOCOL_RECEIVE_GIFT = 7;  //送礼消息

        public static final int PROTOCOL_SILENT = 8;  //禁言和取消禁言消息

        public static final int PROTOCOL_CHAT = 9;  //直播间聊天

        public static final int PROTOCOL_BROADCAST = 10;  //系统公告

        public static final int PROTOCOL_LETTER = 11;  //用户私信

        public static final int PROTOCOL_GAME = 12;  //游戏专用消息

        public static final int PROTOCOL_BALANCE_CHANGE = 12;  //金币变动消息

        public static final int PROTOCOL_LIVE_OPEN = 14;  //开播通知消息

        public static final int PROTOCOL_GAME_FLY = 15;  //游戏飘屏消息

        public static final int PROTOCOL_LIVE_BROADCAST = 13;  //直播间公告消息

        public static final int PROTOCOL_LIVE_CLOSELIVE = 14;  //强制关播

        public static final int PROTOCOL_LIVE_PAY_GUARD_SUC = 18;  //购买守护成功飘屏

        public static final int PROTOCOL_LIVE_SHARE_SUC = 19;  //分享成功消息

        public static final int PROTOCOL_LIVE_LUCK_GIFT_SUC = 20;  //幸运礼物中奖飘屏消息

        public static final int PROTOCOL_LIVE_UPDATE_GIFT = 16;  //更新礼物消息

        public static final int PROTOCOL_LIVE_UPDATE_ADMISSION = 60;   //更新入场动画

        public static final int PROTOCOL_LIVE_RECHARGE_SUC = 22;  //每日首充成功消息

        public static final int PROTOCOL_LIVE_SHARE_FLY = 23;  //分享假飘

        public static final int PROTOCOL_LIVE_KICK = 15;  //踢人消息

        public static final int PROTOCOL_LIVE_PROMISE = 25; //许愿成功飘屏

        /**
         * PK请求响应消息
         * type 0发起请求  1拒绝请求  2同意请求
         */
        public static final int PROTOCOL_LIVE_PK_MSG = 19;

        public static final int PROTOCOL_RoomPay_Change = 21;  //房间收费消息变动

        public static final int PROTOCOL_PK_RESULT = 23;  //PK结果消息

        public static final int PROTOCOL_CANCEL_PK = 22;  //取消PK

        public static final int PROTOCOL_PK_YUYAN = 30;  //用户预言消息

        public static final int PROTOCOL_PK_START_STOP = 18;  //PK发起开始结束消息

        public static final int PROTOCOL_PK_YUYAN_RESULT = 32; //预言结果消息

        public static final int PROTOCOL_PK_VALUE_CHANGE = 24;  //PK比分变动消息
        public static final int PROTOCOL_GAME_Noticition = 25;  //飘屏

        public static final int PROTOCOL_CpBets = 26;  //彩票下注消息

        public static final int PROTOCOL_CpWin = 27;  //彩票中奖消息

        public static final int PROTOCOL_RoomManager = 28;  //设置房管消息

        public static final int PROTOCOL_MyCoust = 99;  //直播间公告消息
        public static final int PROTOCOL_CP = 29;  //cp
        public static final int PROTOCOL_YL = 1129;  //切换推流

        public static final int RED_BAG_START = 61;  //红包雨开始
        public static final int RED_BAG_END = 62;  //红包雨结束
        public static final int RED_BAG_UPDATE = 63;  //红包雨更新

    }


    /**
     * 文件下载后存储位置
     */
    public final static String DEFAULT_DOWNLOAD_DIR = FileUtils.getExternalCardPath()
            + File.separator + BuildConfig.AppFlavor + File.separator;
    public final static String DEFAULT_DOWNLOAD_DIR_ADMISSION = "admission";

    /**
     * 隐藏文件目录 在目录下创建这个文件后 此目录下的图片都不会加入到系统相册中
     */
//    public final static String DEFAULT_RELATIVE_NOMEDIA = DEFAULT_DOWNLOAD_DIR + ".nomedia" + File.separator;

    /**
     * SP文件名
     */
    public static class SPNAME {
        public static final String UserInfo = "UserInfo";
        public static final String SYSTEM = "System";
    }

    public static class FragmentFlag {
        public static final String FLAG = "fragment_flag";
        public static final int ADROOM_FRAGMENT = 0X01;
        public static final int AD_FRAGMENT = 0X02;
        public static final int WEB_FRAGMENT = 0X03;
        public static final int LOGIN_FRAGMENT = 0X04;
        public static final int ABOUT_FRAGMENT = 0X05;
        public static final int FULL_ROOM_FRAGMENT = 0X06;
        public static final int SEARCH_FRAGMENT = 0X07;
        public static final int FriendCircle_FRAGMENT = 0X08;
        public static final int Message_FRAGMENT = 0X09;
        public static final int AddMoney_FRAGMENT = 0X10;
        public static final int AnchorADRoom_FRAGMENT = 0X11;
        public static final int ADROOM_Tecent_FRAGMENT = 0X12;
        public static final int Store_FRAGMENT = 0X13;
        public static final int Album_FRAGMENT = 0X14;
        public static final int JbRecord_FRAGMENT = 0X15;
        public static final int JzRecord_FRAGMENT = 0X16;
    }


    public static class Code {
        public static final int SUCCESS = 0;
    }

    /**
     * 语言类型
     */
    public static class MineClickType {
        public static final int USERINFO = 0X02;
        public static final int HISTORY = 0X03;
        public static final int BALANCE = 0X04;
        public static final int NOBILITY = 0X05;
        public static final int EARNINGS = 0X06;
        public static final int IDOL = 0X07;
        public static final int PROPS = 0X08;
        public static final int ABOUT = 0X09;
        public static final int SYSTEM = 0X0110;
        public static final int LANGUAGE = 0X111;
    }
}
