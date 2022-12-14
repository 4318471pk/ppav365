package com.live.fox.server;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.CountDownBean;
import com.live.fox.entity.LiveRoomGameDetailBean;
import com.live.fox.entity.LivingLotteryListBean;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.lzy.okgo.callback.Callback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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



    public void lotteryBet(Map params,  Callback<String>  callback) {

        String url =  SPManager.getServerDomain() + Constant.URL.lotteryBet;

        OkGoHttpUtil.getInstance().doJsonPost("", url, getCommonHeaders(Long.parseLong(params.get("timestamp").toString())),
                new Gson().toJson(params)).execute(callback);
    }

    /**
     *  获取直播间游戏列表
     */
    public void getLiveRoomGameDetail(String gameCode,JsonCallback<List<LiveRoomGameDetailBean>> callback) {

        //TODO
        gameCode="yfks";


        StringBuilder sb=new StringBuilder();
        sb.append(SPManager.getServerDomain());
        sb.append(Constant.URL.LiveRoomGameDetail).append("?");
        sb.append("gameCode=").append(gameCode);
        // + "?language=" + MultiLanguageUtils.getRequestHeader() ;
        OkGoHttpUtil.getInstance().doGet(
                "",
                sb.toString(),
                getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }

    public void countDown(String gameCode,JsonCallback<CountDownBean> callback) {

        //TODO
        gameCode="yfks";


        StringBuilder sb=new StringBuilder();
        sb.append(SPManager.getServerDomain());
        sb.append(Constant.URL.countDown).append("?");
        sb.append("gameCode=").append(gameCode);
        // + "?language=" + MultiLanguageUtils.getRequestHeader() ;
        OkGoHttpUtil.getInstance().doGet(
                        "",
                        sb.toString(),
                        getCommonHeaders(System.currentTimeMillis()))
                .execute(callback);
    }


    /**
     *  查询开播前游戏列表
     */
    public void getLiveBeforeGames(JsonCallback<String> callback) {
        String url =  SPManager.getServerDomain() + Constant.URL.liveBeforeGames;
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

