package com.live.fox.base;

import com.live.fox.Constant;
import com.live.fox.common.JsonCallback;
import com.live.fox.contract.AgentContract;
import com.live.fox.entity.response.AgentInfoVO;
import com.live.fox.model.AgentModel;

import java.util.List;
import java.util.Map;

public class AgentPresenter extends AgentContract.Presenter {
    public AgentPresenter() {
        mModel = new AgentModel();
    }

    @Override
    public void doGetAgentInfo(Map map) {
        mModel.doGetAgentInfo(map, new JsonCallback<List<AgentInfoVO>>() {
            @Override
            public void onSuccess(int code, String msg, List<AgentInfoVO> result) {
                if (code == Constant.Code.SUCCESS) {
                    mView.onGetAgentInfo(result);
                } else {
                    mView.onError(msg);
                }

            }
        });
    }

}
