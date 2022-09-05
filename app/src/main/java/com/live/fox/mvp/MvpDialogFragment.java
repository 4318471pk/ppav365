package com.live.fox.mvp;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.R;
import com.live.fox.base.AbsBasePresenter;
import com.live.fox.dialog.MMLoading;
import com.live.fox.dialog.MMToast;
import com.live.fox.helper.mvp.IBaseView;


public class MvpDialogFragment<P extends AbsBasePresenter> extends DialogFragment implements IBaseView {
    protected P presenter;
    private MMLoading loadingDialog;

    public MvpDialogFragment() {
        //暂时放在此处初始化，开发中有需求再调整
        presenter = MvpUtils.instantiatePresenter(this);
        if (presenter != null) {
            presenter.onFragmentCreateView(this);
            presenter.setView(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (presenter == null){
            presenter = MvpUtils.instantiatePresenter(this);
        }
        if (presenter != null) {
            presenter.onFragmentCreateView(this);
            presenter.setView(this);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onTips(CharSequence tips) {

    }

    @Override
    public void onError(CharSequence errorMsg) {
        if (!TextUtils.isEmpty(errorMsg))
            showToastTip(false, errorMsg.toString());
    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }


    /**
     * 等待Dialog
     */
    public void showLoadingDialog() {

        showLoadingDialog(getString(R.string.baseLoading), false, true);
    }

    @Override
    public void showLoadingDialogWithNoBgBlack() {

    }

    /**
     * 等待Dialog
     */
    public MMLoading showLoadingDialog(String msg, boolean isCancelable, boolean isBgBlack) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (getActivity() == null) return null;
        MMLoading.Builder builder = new MMLoading.Builder(getActivity())
                .setMessage(msg);
        if (isCancelable) {
            builder.setCancelable(true).setCancelOutside(true);
        } else {
            builder.setCancelable(false).setCancelOutside(false);
        }
        loadingDialog = builder.create();
        loadingDialog.setIsBgBlack(isBgBlack);
        loadingDialog.show();
        return loadingDialog;
    }


    public void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public Context getCtx() {
        return requireActivity();
    }

    /**
     * 成功或失败的Toast提示
     */
    MMToast mmToast;

    public void showToastTip(boolean isSuccess, String msg) {
        if (mmToast != null) {
            mmToast.cancel();
        }
        if (getActivity() == null) return;
        MMToast.Builder builder = new MMToast.Builder(requireContext())
                .setMessage(msg)
                .setSuccess(isSuccess);
        mmToast = builder.create(Toast.LENGTH_SHORT);
        mmToast.show();
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showEmptyView(String tip) {

    }

    @Override
    public void showEmptyViewWithButton(String tip, String buttonText, View.OnClickListener listener) {

    }

    @Override
    public void hideEmptyView() {

    }

    @Override
    public void showErrorView(View.OnClickListener listener) {

    }

    @Override
    public void hideErrorView() {

    }
}
