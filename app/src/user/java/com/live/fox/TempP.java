package com.live.fox;

import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.live.fox.entity.Kefu;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 临时
 */
public class TempP implements LifecycleObserver {

    public void test1() {
        LogUtils.e("test1");
        ToastUtils.showShort("test1");
    }


    public void test2(View v) {
        LogUtils.e("test2");
        ToastUtils.showShort("test2");

    }

    public void test3(View view, Kefu kefu) {
        ToastUtils.showShort("test3");
        LogUtils.e(", " + kefu.getNickname());
    }

    //
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void test331() {
        LogUtils.e("test331");
        ToastUtils.showShort("test331");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void test33() {
        LogUtils.e("test33");

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void test44() {
        LogUtils.e("test44");
        ToastUtils.showShort("test2");

    }


}
