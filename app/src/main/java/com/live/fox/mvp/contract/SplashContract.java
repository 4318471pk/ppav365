package com.live.fox.mvp.contract;

import com.live.fox.helper.mvp.AbsBaseMvpPresenter;
import com.live.fox.helper.mvp.IBaseView;


public interface SplashContract {

    abstract class Presenter extends AbsBaseMvpPresenter<View> {
    }

    interface View extends IBaseView {
        void showAdView(String path, String openScreenUrl);

        void finishActivity();
    }
}
