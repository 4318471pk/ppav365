package com.live.fox.model;


import com.live.fox.Constant;
import com.live.fox.contract.BetCartContract;
import com.live.fox.manager.SPManager;
import com.lzy.okgo.callback.Callback;

import java.util.Map;


public class BetCartModel extends BetCartContract.Model {

    @Override
    public void doPushCart(Map map, Callback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.CP_GOPAY;
        doPost6(map, url, callback);
    }

    @Override
    public void doGetGamePeriodInfo(Map map, Callback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.CP_GETISSUE;
        doPost(map, url, callback);
    }
}
