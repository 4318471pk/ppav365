package com.live.fox.base;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.live.fox.helper.mvp.IBaseView;
import com.live.fox.mvp.AbsBaseModel;

public abstract class AbsBasePresenter<M extends AbsBaseModel, V extends IBaseView> {
    protected M mModel;

    protected V mView;

    public AbsBasePresenter() {
    }

    /**
     * 此方法在使用 presenter 前一定要先调用，为 mView 设值
     *
     * @param mView 谁实现了 mView 接口就传谁
     */
    public void setView(V mView) {
        this.mView = mView;
    }

    public void onActivityCreate(Activity activity) {
        //NONE
    }

    public void onActivityResume(Activity activity) {
        //NONE
    }

    public void onActivityPause(Activity activity) {
        //NONE
    }

    public void onActivityDestroy(Activity activity) {
        //NONE
    }

    public void onFragmentAttach(Fragment fragment) {
        //NONE
    }

    public void onFragmentDetach(Fragment fragment) {
        //NONE
    }

    public void onFragmentHidden(Fragment fragment, boolean hidden) {
        //NONE
    }

    public void onFragmentCreateView(Fragment fragment) {
        //NONE
    }

}
