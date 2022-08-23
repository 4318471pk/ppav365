package com.live.fox.model;

import com.live.fox.Constant;
import com.live.fox.contract.MinuteGameContract;
import com.live.fox.manager.SPManager;
import com.lzy.okgo.callback.Callback;

import java.util.Map;


public class MinuteGameModel extends MinuteGameContract.Model {
    @Override
    public void doGetGamePeriodInfo(Map map, Callback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.CP_GETISSUE;
        doPost(map, url, callback);
    }

    @Override
    public void doGetLastGameResult(Map map, Callback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.CP_GETLOTTERY;
        doPost(map, url, callback);
    }
}
