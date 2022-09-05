package com.live.fox.helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.live.fox.helper.mvp.AbsBaseMvpPresenter;


/**
 * @author ywy
 * @date 2016/5/30
 */
public interface IBaseLazyViewPager {

    /**
     * get a present instance
     *
     * @return present instance if use mvp framework
     */
    AbsBaseMvpPresenter getPresenter();

    /**
     * init it when the view created
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view when this created
     */
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * do everything about ui in this method
     *
     * @param savedInstanceState
     * @return
     */
    public void bindView(Bundle savedInstanceState);

    /**
     * get this view if need
     *
     * @return this view
     */
    public View getView();

    /**
     * set layout in this method
     *
     * @return this layout id
     */
    public int getContentLayout();
}
