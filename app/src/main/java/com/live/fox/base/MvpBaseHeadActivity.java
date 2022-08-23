package com.live.fox.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.helper.mvp.AbsBaseMvpPresenter;


/**
 * MvpActicty的基础类
 * 1.封装了绑定Presenter流程
 * 2.将Activity与Presenter生命周期进行绑定
 * 3.当Activity在onDestroy时，取消所有网络请求及弹框，并让Presenter与View解绑，防止内存泄漏
 */
public abstract class MvpBaseHeadActivity<P extends AbsBaseMvpPresenter> extends BaseHeadActivity {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //创建Presenter 很多地方的写法是bindPresenter(this),把View定死为Activity了，
        //有时候View也可以为Fragment，所以把此类开放会更灵活
        if (mPresenter == null) {
            mPresenter = bindPresenter();
        }

        if (mPresenter == null) {
            throw new NullPointerException(getString(R.string.exception_throw));
        }

        mPresenter.onActivityCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onActivityResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onActivityPause(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onActivityDestroy(this);
        }
        /**
         * 在生命周期结束时，将 presenter 与 view 之间的联系断开，防止出现内存泄露
         */
        if (mPresenter != null) {
            mPresenter.cancelAllHttpAndDialog();
            mPresenter.detachView();
        }
    }

    /**
     * 初始化Presenter，并将View与Presenter进行绑定
     */
    protected abstract P bindPresenter();


    public P getPresenter() {
        return mPresenter;
    }


}
