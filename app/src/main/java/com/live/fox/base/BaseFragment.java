package com.live.fox.base;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.live.fox.R;
import com.live.fox.dialog.MMLoading;
import com.live.fox.dialog.MMToast;
import com.live.fox.helper.mvp.IBaseView;
import com.live.fox.language.LanguageSp;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.utils.BarUtils;

import java.util.Locale;



/**
 * 封装了一些常用的方法和功能
 */
public class BaseFragment extends Fragment implements IBaseView {

    protected View rootView;

    /**
     * 界面中的加载、空、错误时显示的布局。
     */
    View errorView;
    View emptyView;
    View loadingView;

    @Override
    public void onAttach(Context context) {
        String language = LanguageSp.getString(context, MultiLanguageUtils.LANGUAGE);
        String country = LanguageSp.getString(context, MultiLanguageUtils.COUNTRY);
        if (!TextUtils.isEmpty(language) && !TextUtils.isEmpty(country)) {  //强制修改应用语言
            if (MultiLanguageUtils.isSameWithSetting(context)) {
                Locale locale = new Locale(language, country);
                MultiLanguageUtils.changeAppLanguage(context, locale, false);
            }
        }
        super.onAttach(context);
    }

    @Override
    public Context getCtx() {
        return getActivity();
    }

    @Override
    public void showLoadingDialog() {
        showLoadingDialog(getString(R.string.baseLoading), false, true);
    }

    /**
     * 等待Dialog 背景不变黑
     */
    @Override
    public void showLoadingDialogWithNoBgBlack() {
        showLoadingDialog(getString(R.string.baseLoading), false, false);
    }

    /**
     * 显示 等待Dialog
     */
    private MMLoading loadingDialog;

    @Override
    public MMLoading showLoadingDialog(String msg, boolean isCancelable, boolean isBgBlack) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
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

    /**
     * 隐藏 等待Dialog
     */
    @Override
    public void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 成功或失败的Toast提示
     */
    MMToast mmToast;

    @Override
    public void showToastTip(boolean isSuccess, String msg) {
        if (mmToast != null) {
            mmToast.cancel();
        }
        MMToast.Builder builder = new MMToast.Builder(getActivity())
                .setMessage(msg)
                .setSuccess(isSuccess);
        mmToast = builder.create(Toast.LENGTH_SHORT);
        mmToast.show();
    }

    /**
     * 显示加载界面
     * 界面xml需添加<include layout="@layout/view_replace_placeholder" />
     */
    @Override
    public void showLoadingView() {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
            return;
        }
        if (rootView != null) {
            ViewStub viewStub = rootView.findViewById(R.id.loadingView);
            if (viewStub != null) {
                loadingView = viewStub.inflate();
            }
        }
    }

    /**
     * 显示空界面
     * 界面xml需添加<include layout="@layout/view_replace_placeholder" />
     */
    @Override
    public void showEmptyView(String tip) {
        if (rootView != null) {
            ViewStub viewStub = rootView.findViewById(R.id.emptyView);
            if (viewStub != null) {
                emptyView = viewStub.inflate();
                emptyView.setVisibility(View.VISIBLE);
                TextView noContentText = emptyView.findViewById(R.id.tv_empty);
                noContentText.setText(tip);
            }
        }
    }

    /**
     * 显示空+按钮界面
     * 界面xml需添加<include layout="@layout/view_replace_placeholder" />
     */
    @Override
    public void showEmptyViewWithButton(String tip, String buttonText, View.OnClickListener listener) {
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
            return;
        }
        if (rootView != null) {
            ViewStub viewStub = rootView.findViewById(R.id.emptyViewWithButton);
            if (viewStub != null) {
                emptyView = viewStub.inflate();
                TextView noContentText = emptyView.findViewById(R.id.tv_empty);
                Button noContentButton = emptyView.findViewById(R.id.btn_empty);
                noContentText.setText(tip);
                noContentButton.setText(buttonText);
                noContentButton.setOnClickListener(listener);
            }
        }
    }

    /**
     * 显示错误界面
     * 界面xml需添加<include layout="@layout/view_replace_placeholder" />
     */
    @Override
    public void showErrorView(View.OnClickListener listener) {
        if (errorView != null) {
            errorView.setVisibility(View.VISIBLE);
            return;
        }
        if (rootView != null) {
            ViewStub viewStub = rootView.findViewById(R.id.errorView);
            if (viewStub != null) {
                errorView = viewStub.inflate();
                View badNetworkRootView = errorView.findViewById(R.id.ll_error);
                badNetworkRootView.setOnClickListener(listener);
            }
        }
    }

    @Override
    public void hideLoadingView() {
        if (loadingView != null)
            loadingView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        if (emptyView != null)
            emptyView.setVisibility(View.GONE);
    }

    @Override
    public void hideErrorView() {
        if (errorView != null)
            errorView.setVisibility(View.GONE);
    }

    @Override
    public void onTips(CharSequence tips) {

    }

    @Override
    public void onError(CharSequence errorMsg) {

    }

    @Override
    public void onShowProgress() {

    }

    @Override
    public void onHideProgress() {

    }


    public void finishDelayed(long time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requireActivity().finish();
            }
        }, time);
    }


    public void startShake(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.shake));
    }


    //设置头部padding出StatusBarH的高度 沉浸式布局时需要用得到 比如状态栏
    public void setTopPaddingStatusBarHeight(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height += BarUtils.getStatusBarHeight();
        view.setLayoutParams(layoutParams);
        view.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideLoadingDialog();
    }
}