package com.live.fox.contract;

import com.live.fox.base.AbsBasePresenter;
import com.live.fox.entity.response.AgentInfoVO;
import com.live.fox.helper.mvp.IBaseView;
import com.live.fox.mvp.AbsBaseModel;
import com.lzy.okgo.callback.Callback;

import java.util.List;
import java.util.Map;


public interface AgentContract {
    interface View extends IBaseView {
        void onGetAgentInfo(List<AgentInfoVO> result);
    }

    abstract class Presenter extends AbsBasePresenter<Model, View> {
        public abstract void doGetAgentInfo(Map map);
    }

    abstract class Model extends AbsBaseModel {
        public abstract void doGetAgentInfo(Map map, Callback callback);
    }
}
