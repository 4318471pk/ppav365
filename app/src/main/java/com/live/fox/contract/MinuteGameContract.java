package com.live.fox.contract;

import com.live.fox.base.AbsBasePresenter;
import com.live.fox.entity.response.CpGameResultInfoVO;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.helper.mvp.IBaseView;
import com.live.fox.mvp.AbsBaseModel;
import com.lzy.okgo.callback.Callback;

import java.util.List;
import java.util.Map;

public interface MinuteGameContract {
    interface View extends IBaseView {
        void onGetGamePeriodInfo(GamePeriodInfoVO result);

        void onGetLastGameResult(List<CpGameResultInfoVO> cpGameResultInfoVO);

    }

    abstract class Presenter extends AbsBasePresenter<Model, View> {
        public abstract void doGetGamePeriodInfo(Map map);

        public abstract void doGetLastGameResult(Map map);

    }

    abstract class Model extends AbsBaseModel {
        public abstract void doGetGamePeriodInfo(Map map, Callback callback);

        public abstract void doGetLastGameResult(Map map, Callback callback);

    }
}
