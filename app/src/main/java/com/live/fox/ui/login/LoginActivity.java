package com.live.fox.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.databinding.LoginActivityBinding;
import com.live.fox.ui.mine.moneyout.BindCardActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.FragmentUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;

/**
 * 注册页面
 */
public class LoginActivity extends BaseActivity {

    LoginActivityBinding mBinding;
    LoginViewModel loginViewModel;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mBinding.setViewModel(loginViewModel);
        mBinding.setLifecycleOwner(this);
        initView();
    }

    public void initView() {
        BarUtils.setStatusBarLightMode(LoginActivity.this, false);
        LoginPageType currentPage = (LoginPageType) getIntent().getSerializableExtra("pageType");
        if (currentPage != null) {
            phone = getIntent().getStringExtra("phone");

            if (StringUtils.isEmpty(phone)) phone = "";
            loginViewModel.loadData(currentPage, phone, this);

            mBinding.layoutBack.setOnClickListener(view -> onPageBack());

            loginViewModel.getmExceptionHint().observe(this, this::showExceptionHint);

            loginViewModel.getmSuccessHint().observe(this, this::showSuccessHint);

            loginViewModel.getmWarningHint().observe(this, this::showWarningHint);

            loginViewModel.getShowLoadingDialog().observe(this, aBoolean -> {
                if (aBoolean) {
                    showLoadingDialog();
                } else {
                    hideLoadingDialog();
                }
            });

            loginViewModel.getPageBack().observe(this, aBoolean -> {
                if (aBoolean) {
                    onPageBack();
                }
            });

            loginViewModel.getToMainActivity().observe(this, aBoolean -> {
                if (aBoolean) {
                    ActivityUtils.finishOtherActivities(LoginActivity.class);
                    BindCardActivity.startActivity(this, phone, true);
                    finish();
                }
            });

            //显示不同类型的Fragment
            loginViewModel.getCurrentFragment().observe(this, pageType -> {
                LogUtils.e(String.valueOf(pageType));
                if (pageType != null) {
                    switch (pageType) {
                        case LoginByPhone:  //注册
                            FragmentUtils.replace(
                                    getSupportFragmentManager(),
                                    LoginByPhoneFragment.newInstance(loginViewModel.getUsername().getValue()),
                                    R.id.login_frame_layout);
                            break;
                        case LoginByPwd:
                            FragmentUtils.replace(
                                    getSupportFragmentManager(),
                                    LoginByPwdFragment.newInstance(loginViewModel.getUsername().getValue()),
                                    R.id.login_frame_layout);
                            break;
                        case ResetPwd:  //重置密码
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.slide_right_in, R.anim.slide_left_out,
                                            R.anim.slide_left_in, R.anim.slide_right_out)
                                    .replace(R.id.login_frame_layout,
                                            ResetPwdFragment.newInstance(loginViewModel.getUsername().getValue(), 0))
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                            break;
                        case SetPwd:   //设置密码
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.slide_right_in, R.anim.slide_left_out,
                                            R.anim.slide_left_in, R.anim.slide_right_out)
                                    .replace(R.id.login_frame_layout,
                                            ResetPwdFragment.newInstance(loginViewModel.getUsername().getValue(), 1))
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                            break;
                        case ModifyPwd:  //找回密码
                            LogUtils.e("ResetPwd");
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.slide_right_in, R.anim.slide_left_out,
                                            R.anim.slide_left_in, R.anim.slide_right_out)
                                    .replace(R.id.login_frame_layout,
                                            ResetPwdFragment.newInstance(loginViewModel.getUsername().getValue(), 2))
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                            break;
                        case ModifyUserinfo:  //完善资料
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(
                                            R.anim.slide_right_in, R.anim.slide_left_out,
                                            R.anim.slide_left_in, R.anim.slide_right_out)
                                    .replace(R.id.login_frame_layout,
                                            ModifyUserInfoFragment.newInstance(loginViewModel.getUsername().getValue()))
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                            break;
                    }
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (keyCode == KeyEvent.KEYCODE_BACK && count > 0) {
            LogUtils.e("back to list " + count);
            onPageBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onPageBack() {
        LogUtils.e("onPageBack()");
        Constant.isAppInsideClick = true;
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (count > 0) {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        switch (loginViewModel.getCurrentPage()) {
            case LoginByPwd: //1.手机验证码登录
            case ResetPwd: //4.在 重置密码界面 点击返回
            case LoginByPhone: //2.密码登录
            case SetPwd: //设置密码
            case ModifyPwd: //修改密码
                finish();
                break;
            case ModifyUserinfo: //5.在 完善资料 点击返回
                switch (loginViewModel.getTheToModifyPageNext()) {
                    case LoginByPhone: //1.手机验证码登录
                        loginViewModel.changePageTop(LoginPageType.LoginByPhone, this);
                        break;
                    case LoginByPwd: //2.密码登录
                        loginViewModel.changePageTop(LoginPageType.LoginByPwd, this);
                        break;
                    case ResetPwd: //4.重置密码
                        loginViewModel.changePageTop(LoginPageType.ResetPwd, this);
                        break;
                }
                break;
        }
    }


    public void showSuccessHint(String hint) {
        ToastUtils.showShort(hint);
    }

    public void showWarningHint(String hint) {

        ToastUtils.showShort(hint);
    }

    public void showExceptionHint(String hint) {
        ToastUtils.showShort(hint);
    }

    public static void startActivity(Context context, LoginPageType pageType, String phone) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("pageType", pageType);
        intent.putExtra("phone", phone);
        context.startActivity(intent);
    }

}


