package com.live.fox.contract;

import com.live.fox.base.AbsBasePresenter;
import com.live.fox.helper.mvp.IBaseView;
import com.live.fox.mvp.AbsBaseModel;


public interface NobleContract {

    interface View extends IBaseView {
        void onBuyVip(String data);
    }

    abstract class Presenter extends AbsBasePresenter<Model, View> {
        public abstract void doBuyVip(int leveId);
    }

    abstract class Model extends AbsBaseModel {
    }
}
