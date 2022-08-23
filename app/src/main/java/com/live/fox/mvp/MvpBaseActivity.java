package com.live.fox.mvp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.live.fox.base.AbsBasePresenter;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.helper.mvp.IBaseView;


/**
 * Created  on 2017/9/5.
 */

public class MvpBaseActivity<P extends AbsBasePresenter> extends BaseHeadActivity implements IBaseView {
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = MvpUtils.instantiatePresenter(this);
        if (presenter != null) {
            presenter.setView(this);
            presenter.onActivityCreate(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.onActivityResume(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.onActivityPause(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onActivityDestroy(this);
        }
    }

    @Override
    public void onTips(CharSequence tips) {
    }

    @Override
    public void onError(CharSequence errorMsg) {
        showToastTip(false, errorMsg.toString());
    }

    @Override
    public void onShowProgress() {
        showLoadingDialog();
    }

    @Override
    public void onHideProgress() {
        hideLoadingDialog();
    }

    @Override
    public Context getCtx() {
        return this;
    }
}
