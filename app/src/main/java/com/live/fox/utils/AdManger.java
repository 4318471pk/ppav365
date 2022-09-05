package com.live.fox.utils;

import com.live.fox.entity.Advert;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告管理
 */
public class AdManger {

    private List<Advert> bannerList; //1.主页顶部的横幅广告
    private List<Advert> banner2List;//2.次级横幅广告
    private List<Advert> gonggaoTxtList;//3.首页飘屏文字广告
    private List<Advert> liveroomTxtList;//4.直播间文字广告
    private List<Advert> liveroomPPAdList;//5.直播间飘屏广告
    private List<Advert> liveroomImageList;//6.房间活动图片广告
    private List<Advert> liveroomBannerList;//7.直播间左下角的广告
    private List<Advert> gameBannerList;    //8.游戏广告
    private List<Advert> tixianList;    //8.提现广告

    private static final class AppAd {
        private static final AdManger adManger = new AdManger();

    }

    public static AdManger Instance() {
        return AppAd.adManger;
    }

    public List<Advert> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<Advert> bannerList) {
        this.bannerList = bannerList;
    }

}
