package com.live.fox.server;

import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.AdmissionEntity;
import com.live.fox.entity.Advert;
import com.live.fox.entity.AppUpdate;
import com.live.fox.entity.Badge;
import com.live.fox.entity.ConfigPathsBean;
import com.live.fox.entity.Game;
import com.live.fox.entity.GameColumn;
import com.live.fox.entity.GameListItem;
import com.live.fox.entity.Gift;
import com.live.fox.entity.HomeColumn;
import com.live.fox.entity.HongdongBean;
import com.live.fox.entity.Kefu;
import com.live.fox.entity.LiveColumn;
import com.live.fox.entity.OssToken;
import com.live.fox.entity.RechargeChannel;
import com.live.fox.entity.SysNotice;
import com.live.fox.entity.VipInfo;
import com.live.fox.entity.response.ChipsVO;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;
import java.util.List;

public class Api_Config extends BaseApi {
    private Api_Config() {
    }

    private static class InstanceHolder {
        private static final Api_Config instance = new Api_Config();
    }

    public static Api_Config ins() {
        return InstanceHolder.instance;
    }


    public static final String getBaseConfig = "getBaseConfig";
    public static final String getAppVersion = "getAppVersion";
    public static final String getOssToken = "getOssToken";
    public static final String getAdVert = "getAdVert";

    public static final String getCserver = "getCserver";

    /**
     * 获取基础信息
     */
    public void getBaseConfig(String domain, JsonCallback callback) {
        String url = domain + Constant.URL.BASE_baseinfo_URL + "?os=" + Constant.OS;
        OkGoHttpUtil.getInstance().doGet(getBaseConfig, url).execute(callback);
    }


    /**
     * 获取版本信息
     */
    public void getAppVersion(JsonCallback<AppUpdate> callback) {
        String url = getBaseServerDomain() + Constant.URL.BASE_version_URL + "?os=" + Constant.OS;
        doGetHeaders(url, callback);
    }

    /**
     * 用户活动列表
     * 获取推广广告
     *
     * @param callback 返回监听
     */
    public void getPromote(JsonCallback<List<HongdongBean>> callback) {
        String url = getBaseServerDomain() + Constant.URL.USERACTIVITY_URL + "?uid=" + AppUserManger.getUserInfo().getUid();
        doGetHeaders(url, callback);
    }

    /**
     * 获取oss上传凭证
     */
    public void getOssToken(JsonCallback<OssToken> callback) {
        String url = getBaseServerDomain() + Constant.URL.BASE_ossToken_URL;
        doGetHeaders(url, callback);
    }


    /**
     * 广告列表
     */
    public void getAdVert(JsonCallback<List<Advert>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_advert_URL;
        callback.setUrlTag("advert");
        doGetHeaders(url, callback);
    }


    /**
     * 栏目列表
     */
    public void getColumn(JsonCallback<List<HomeColumn>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_column_URL;
        callback.setUrlTag("/column");
        doGetHeaders(url, callback);
    }

    /**
     * 客服列表
     */
    public void getServer(JsonCallback<List<Kefu>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_cserver_URL;
        callback.setUrlTag("cserver");
        doGetHeaders(url, callback);
    }


    /**
     * 消息公告
     */
    public void getNotice(JsonCallback<String> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_notice_URL;
        doGetHeaders(url, callback);
    }

    /**
     * 支付渠道列表
     */
    public void getPayChannel(JsonCallback<List<RechargeChannel>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_pay_URL + "?uid=" + AppUserManger.getUserInfo().getUid();
        callback.setUrlTag("/pay?uid=");
        doGetHeaders(url, callback);
    }

    /**
     * 道具列表
     */
    public void getProp(JsonCallback<List<Gift>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_prop_URL;
        callback.setUrlTag("/prop");
        doGetHeaders(url, callback);
    }

    /**
     * 获取系统公告列表
     */
    public void getSysNotice(JsonCallback<List<SysNotice>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_sysnotice_URL;
        callback.setUrlTag("system/notice");
        doGetHeaders(url, callback);
    }

    /**
     * 主播标签
     */
    public void getTag(JsonCallback<List<LiveColumn>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_tag_URL;
        doGetHeaders(url, callback);
    }


    /**
     * 获取礼物下载列表
     */
    public void getGiftList(JsonCallback<List<Gift>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_prop_URL;
        callback.setUrlTag("prop");
        doGetHeaders(url, callback);
    }

    /**
     * 获取徽章下载列表
     */
    public void getBadgeList(JsonCallback<List<Badge>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_badge_URL;
        doGetHeaders(url, callback);
    }

    /**
     * 获取游戏列表
     */
    public void getGameList(JsonCallback<List<Game>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_game_URL;
        doGetHeaders(url, callback);
    }

    /**
     * 获取游戏列表
     */
    public void getKYGameList(JsonCallback<List<GameListItem>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_taglist_URL;
        doGetHeaders(url, callback);
    }


    /**
     * 获取游戏列表
     */
    public void getKYGameDetailList(long parentId, JsonCallback<List<GameColumn>> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_detaillist_URL + "?parentId=" + parentId;
        doGetHeaders(url, callback);
    }


    /**
     * 获取首页游戏列表x
     */
    public void getHomeGameList(JsonCallback<GameColumn> callback) {
        String url = getBaseServerDomain() + Constant.URL.Config_indexlist_URL;
        callback.setUrlTag("index/list");
        doGetHeaders(url, callback);
    }

    /**
     * 获取入场动画下载列表
     */
    public void getAdmissionList(JsonCallback<List<AdmissionEntity>> callback) {
        String url = getBaseServerDomain() + Constant.URL.CONFIG_ADMISSION_URL;
        doGetHeaders(url, callback);
    }


    /**
     * 获取cp列表
     */
    public void getCpList(JsonCallback<List<ChipsVO>> callback) {
        String url = getBaseServerDomain() + Constant.URL.CONFIG_CP_URL;
        callback.setUrlTag("cp/list");
        doGetHeaders(url, callback);
    }

    public void doVipInfo(JsonCallback<List<VipInfo>> callback) {
        String url = getBaseServerDomain() + Constant.URL.CONFIG_VIP_URL;
        doGetHeaders(url, callback);
    }

    /**
     * 获取直播路线配置接口
     */
    public void getConfigPaths(long uid, JsonCallback<List<ConfigPathsBean>> callback) {
        String url = getBaseServerDomain() + Constant.URL.CONFIG_PATHS_URL + "?uid=" + uid;
        doGetHeaders(url, callback);
    }

    /**
     * 设置请求头的参数
     *
     * @param url      url
     * @param callback 请求回调
     * @param <T>      数据
     */
    public <T> void doGetHeaders(String url, JsonCallback<T> callback) {
        HashMap<String, Object> params = getCommonParams();
        Long times = (Long) params.get("timestamp");
        if (times != null) {
            OkGoHttpUtil.getInstance().doGet("", url,
                    getCommonHeaders(times)).execute(callback);
        } else {
            LogUtils.e("GetHeaders : --> 获取时间戳失败");
        }
    }

}
