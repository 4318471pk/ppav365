package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.LivingLotteryListBean;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.okgo.OkGoHttpUtil;

import java.util.HashMap;


public class Api_Living_Lottery extends BaseApi {
    private Api_Living_Lottery() {
    }

    private static class InstanceHolder {
        private static final Api_Living_Lottery instance = new Api_Living_Lottery();
    }

    public static Api_Living_Lottery ins() {
        return Api_Living_Lottery.InstanceHolder.instance;
    }


    /**
     *  获取直播间游戏列表
     */
    public void getLivingGameList(JsonCallback<String> callback) {
        String url =  SPManager.getServerDomain() + Constant.URL.LiveRoomGames;
               // + "?language=" + MultiLanguageUtils.getRequestHeader() ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    /**
     *  首页游戏列表
     */
    public void getGameList(JsonCallback<String> callback) {
        String url =  SPManager.getServerDomain() + Constant.URL.GameModuleGames
                + "?language=" + MultiLanguageUtils.getRequestHeader() ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                url,
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

}

