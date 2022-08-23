package com.live.fox.contract;

import com.live.fox.entity.Noble;
import com.live.fox.helper.mvp.AbsBaseMvpPresenter;
import com.live.fox.helper.mvp.IBaseView;


public interface MyNobleContract {

    abstract class Presenter extends AbsBaseMvpPresenter<View> {
        public abstract void getVipInnfo();

        public abstract void doVipHide(Noble mNoble);

        public abstract void doVipUp(int levelId);

        public abstract void doVipPay(int levelId);
    }

    interface View extends IBaseView {
        void onVipInfo(Noble mNoble);

        void onVipHide(String mNoble);

        void onVipUp(String mNoble);

        void onVipPay(String mNoble);
    }
}
