package com.live.fox.helper.mvp;

import android.content.Context;
import android.view.View;

import com.live.fox.dialog.MMLoading;


/**
 * 定义BaseActivity、BaseFragment的一些常用、通用功能
 */
public interface IBaseView {

    Context getCtx();

    void showLoadingDialog();
    void showLoadingDialogWithNoBgBlack();
    MMLoading showLoadingDialog(String msg, boolean isCancelable, boolean isBgBlack);
    void hideLoadingDialog();

    void showToastTip(boolean isSuccess, String msg);

    void showLoadingView();
    void hideLoadingView();

    void showEmptyView(String tip);
    void showEmptyViewWithButton(String tip, String buttonText, View.OnClickListener listener);
    void hideEmptyView();


    void showErrorView(View.OnClickListener listener);
    void hideErrorView();


    void onTips(CharSequence tips);

    void onError(CharSequence errorMsg);

    void onShowProgress();

    void onHideProgress();


}
