package com.live.fox.contract;

import com.live.fox.base.AbsBasePresenter;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.helper.mvp.IBaseView;
import com.live.fox.mvp.AbsBaseModel;
import com.lzy.okgo.callback.Callback;

import java.util.Map;


public interface BetCartContract {
    interface View extends IBaseView {
        void onPushCart();

        void onGetGamePeriodInfo(GamePeriodInfoVO result);

    }

    abstract class Presenter extends AbsBasePresenter<Model, View> {
        public abstract void doPushCart(Map<String, Object> map);

        public abstract void doGetGamePeriodInfo(Map<String, Object> map);

    }

    abstract class Model extends AbsBaseModel {
        public abstract void doPushCart(Map map, Callback callback);

        public abstract void doGetGamePeriodInfo(Map map, Callback callback);
    }
}
