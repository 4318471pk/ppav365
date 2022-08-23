package com.live.fox.base;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.CommonApp;
import com.live.fox.dialog.MMLoading;
import com.live.fox.dialog.MMToast;
import com.live.fox.helper.mvp.IBaseView;
import com.live.fox.language.LanguageSp;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;

import java.util.Locale;


/**
 * 1.不建议在Base里封装流程，仅仅是省几行代码，会使得程序变复杂，将来在看代码或者给别人看时更难懂
 * 2.建议Base只是封装一些常用的或者通用的功能
 */
public class BaseActivity extends AppCompatActivity implements IBaseView {

    protected Context context = this;

    /**
     * 界面中的加载、空、错误时显示的布局。
     */
    View errorView;
    View emptyView;
    View loadingView;

    @Override
    protected void attachBaseContext(Context newBase) {
        String language = LanguageSp.getString(newBase, MultiLanguageUtils.LANGUAGE);
        String country = LanguageSp.getString(newBase, MultiLanguageUtils.COUNTRY);
        if (!TextUtils.isEmpty(language) && !TextUtils.isEmpty(country)) {  //强制修改应用语言
            if (MultiLanguageUtils.isSameWithSetting(newBase)) {
                Locale locale = new Locale(language, country);
                MultiLanguageUtils.changeAppLanguage(newBase, locale, true);
            }
        }
        super.attachBaseContext(newBase);
    }

    @Override
    public Context getCtx() {
        return this;
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
        if (loadingDialog != null && loadingDialog.isShowing()) {
            return loadingDialog;
        }
        MMLoading.Builder builder = new MMLoading.Builder(this)
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
        if (StringUtils.isEmpty(msg))
            return;
        MMToast.Builder builder = new MMToast.Builder(this)
                .setMessage(msg)
                .setSuccess(isSuccess);
        mmToast = builder.create(Toast.LENGTH_SHORT);
        mmToast.show();
    }

    public void showToastTip(boolean isSuccess, String msg, int type) {
        if (mmToast != null) {
            mmToast.cancel();
        }
        if (StringUtils.isEmpty(msg))
            return;
        MMToast.Builder builder = new MMToast.Builder(this)
                .setMessage(msg)
                .setSuccess(isSuccess);
        mmToast = builder.create(type);
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
        ViewStub viewStub = findViewById(R.id.loadingView);
        if (viewStub != null) {
            loadingView = viewStub.inflate();
        }
    }

    /**
     * 显示空界面
     * 界面xml需添加<include layout="@layout/view_replace_placeholder" />
     */
    @Override
    public void showEmptyView(String tip) {
        if (emptyView != null) {
            emptyView.setVisibility(View.VISIBLE);
            return;
        }

        ViewStub viewStub = findViewById(R.id.emptyView);
        if (viewStub != null) {
            emptyView = viewStub.inflate();
            TextView noContentText = emptyView.findViewById(R.id.tv_empty);
            noContentText.setText(tip);
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
        ViewStub viewStub = findViewById(R.id.emptyViewWithButton);
        if (viewStub != null) {
            emptyView = viewStub.inflate();
            TextView noContentText = emptyView.findViewById(R.id.tv_empty);
            Button noContentButton = emptyView.findViewById(R.id.btn_empty);
            noContentText.setText(tip);
            noContentButton.setText(buttonText);
            noContentButton.setOnClickListener(listener);
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
        ViewStub viewStub = findViewById(R.id.errorView);
        if (viewStub != null) {
            errorView = viewStub.inflate();
            View badNetworkRootView = errorView.findViewById(R.id.ll_error);
            badNetworkRootView.setOnClickListener(listener);
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
        new Handler().postDelayed(this::finish, time);
    }

    //初始化Slidr 让其拖动左边有结束当前界面的效果
    public void setCanFinishBySlidr() {
//        SlidrConfig config=new SlidrConfig.Builder()
//                .position(SlidrPosition.LEFT)//滑动起始方向
//                .edge(true)
//                .edgeSize(0.18f)//距离左边界占屏幕大小的18%
//                .build();
//        Slidr.attach(this,config);
    }


    public void startShake(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
    }


    //设置头部padding出StatusBarH的高度 沉浸式布局时需要用得到 比如状态栏
    public void setTopPaddingStatusBarHeight(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height += BarUtils.getStatusBarHeight();
        view.setLayoutParams(layoutParams);
        view.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
    }

    public void replaceFragment(@IdRes int resId, Fragment fragment) {
        replaceFragment(resId, fragment, false);
    }

    public void replaceFragment(@IdRes int resId, Fragment fragment, boolean isBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(resId, fragment);
        if (isBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    /**
     * 复制到剪切板
     */
    public void copyToClipboard(String copyStr) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("", copyStr);
        clip.setPrimaryClip(myClip);
        ToastUtils.showShort(context.getString(R.string.copySuccess));
    }

    /**
     * -- 设置App字体大小不随系统设置而变化，解决调节系统字体大小引起系统布局混乱的问题 ----------------------
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * --------------------------------------------------------------------------------------------
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    public void onBack() {
        Constant.isAppInsideClick = true;
        if (CommonApp.isNotificationClicked) {
            Activity activity = CommonApp.topActivity.get();
            if (activity != null) {
                Intent intent = new Intent(activity, activity.getClass());
                startActivity(intent);
            }
            CommonApp.isNotificationClicked = false;
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addToWindow(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        addToWindow(false);
    }

    public void addToWindow(boolean add) {
        if (add) {
            if (Constant.isShowWindow && Constant.windowAnchor != null) {
                CommonApp.getInstance().getFloatView().addToWindow(true, this);
            }
        } else {
            if (Constant.isShowWindow && !Constant.isAppInsideClick) {
                CommonApp.getInstance().getFloatView().addToWindow(false, this);
            } else {
                Constant.isAppInsideClick = false;
            }
        }
    }


}
