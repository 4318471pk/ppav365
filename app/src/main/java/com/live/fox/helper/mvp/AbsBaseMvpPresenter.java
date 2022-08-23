package com.live.fox.helper.mvp;


import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.live.fox.utils.LogUtils;

/**
 * 1.Mvp中Presenter的基础类，定义了一些常用的方法及一些待实现的方法
 * 2.待实现方法的调用流程在MvpBaseActivity已经封装好了，子类只需要实现就好了
 */
public abstract class AbsBaseMvpPresenter<V extends IBaseView> {

    /**
     * 初始化后开始加载数据
     * 注：需在Activity/Fragment 初始化控件后 主动手动调用
     * 为了让程序主流程在Presenter控制
     */
    public abstract void loadData();

    /**
     * 取消所有的网络请求及弹框
     * 注：调用流程已经封装在MvpBaseActivity中，会在onDestoty时主动调用
     * Presenter中只需实现就行了
     */
    public abstract void cancelAllHttpAndDialog();


    protected V mView;

    public void attachView(V view) {
        this.mView = view;
    }

    public void detachView() {
        LogUtils.e("detachView");
        if (mView != null) {
            mView = null;
        }
    }

    public boolean isViewAttach() {
        return mView != null;
    }

    public V getView() {
        return mView;
    }


    /**
     * Activity的生命周期，如果子类需要实现，则复写此方法就行了
     */
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

    /**
     * Fragment的生命周期，如果子类需要实现，则复写此方法就行了
     */
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
