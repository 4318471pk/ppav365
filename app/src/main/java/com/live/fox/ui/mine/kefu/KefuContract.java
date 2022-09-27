package com.live.fox.ui.mine.kefu;

import android.content.Context;

import com.live.fox.entity.Kefu;
import com.live.fox.helper.mvp.AbsBaseMvpPresenter;
import com.live.fox.helper.mvp.IBaseView;

import java.util.List;

/**
 * Created by appleMac on 2020/2/28.
 */
public interface KefuContract {

    abstract class Presenter extends AbsBaseMvpPresenter<View> {
        abstract void doGetServiceList();

        abstract void toBrowserByKefu(String url);

        abstract void openQQ(String qq, Context context);
    }

    interface View extends IBaseView {
        void onGetServiceListSuccess(List<Kefu> data);
    }
}
