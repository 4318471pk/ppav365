package com.live.fox.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.live.fox.base.AbsBasePresenter;
import com.live.fox.base.BaseFragment;
import com.live.fox.helper.mvp.IBaseView;


/**
 * Created   on 2017/9/1.
 */

public abstract class MvpBaseFragment<P extends AbsBasePresenter> extends BaseFragment implements IBaseView {
    protected P presenter;
    public MvpBaseFragment() {
        //暂时放在此处初始化，开发中有需求再调整
        presenter = MvpUtils.instantiatePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (presenter != null) {
            presenter.onFragmentCreateView(this);
            presenter.setView(this);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (presenter != null) {
            presenter.onFragmentAttach(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (presenter != null) {
            presenter.onFragmentDetach(this);
        }
    }

    @Override
    public Context getCtx() {
        return getActivity();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (presenter != null) {
            presenter.onFragmentHidden(this, hidden);
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

}
