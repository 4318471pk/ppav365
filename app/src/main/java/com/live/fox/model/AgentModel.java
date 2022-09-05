package com.live.fox.model;

import com.live.fox.Constant;
import com.live.fox.contract.AgentContract;
import com.live.fox.manager.SPManager;
import com.lzy.okgo.callback.Callback;

import java.util.Map;

public class AgentModel extends AgentContract.Model {
    @Override
    public void doGetAgentInfo(Map map, Callback callback) {
        String url = SPManager.getServerDomain() + Constant.URL.ORDER_AGENT_URL;
        doPost(map, url, callback);
    }
}
