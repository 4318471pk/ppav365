package com.live.fox.ui.login;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.LoginByphoneFragmentBinding;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.server.Api_Auth;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.verify.NetEaseVerify;
import com.live.fox.verify.SimpleCaptchaListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * 注册
 * 创建账户
 */
public class LoginByPhoneFragment extends BaseFragment {

    String phoneNum = "";

    LoginViewModel loginViewModel;
    LoginByphoneFragmentBinding mBinding;

    public static LoginByPhoneFragment newInstance(String phoneNum) {
        LoginByPhoneFragment fragment = new LoginByPhoneFragment();
        Bundle args = new Bundle();
        args.putString("phoneNum", phoneNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.login_byphone_fragment, container, false);
            rootView = mBinding.getRoot();
            loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
            mBinding.setViewModel(loginViewModel);
            mBinding.setLifecycleOwner(getActivity());
            initData(getArguments());
            setView(rootView);
        }
        return rootView;
    }

    public void initData(Bundle bundle) {
        if (bundle != null) {
            phoneNum = bundle.getString("phoneNum");
        }
    }

    public void setView(View view) {
        if (!StringUtils.isEmpty(phoneNum)) {
            mBinding.etPhone.setText(phoneNum);
            Editable etext = mBinding.etPhone.getText();
            if (etext != null) {
                Selection.setSelection(etext, etext.length());
            }
        }

        mBinding.btnLogin.setOnClickListener(v -> {
            if (mBinding.etPhone.getText() != null) {
                phoneNum = mBinding.etPhone.getText().toString().trim();
                if (phoneNum.length() != 10 || !phoneNum.startsWith("0")) {
                    ToastUtils.showShort(getString(R.string.telWrongInput));
                    return;
                }
                isRegisterApi(phoneNum);
            }
        });
    }

    /**
     * 出册验证接口
     *
     * @param phoneNum 电话号码
     */
    public void isRegisterApi(String phoneNum) {
        Api_Auth.ins().isRegister(phoneNum, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(data);
                if (code == 0 && "0".equals(data)) {
                    DialogFactory.showTwoBtnDialog(getCtx(), phoneNum,
                            (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                                if (!phoneNum.startsWith("0") || phoneNum.length() != 10) {
                                    ToastUtils.showShort(getString(R.string.toast_phone_number));
                                    return;
                                }

                                NetEaseVerify.getInstance().init(requireActivity(), new SimpleCaptchaListener() {
                                    @Override
                                    public void onValidate(String result, String validate, String msg) {
                                        if (!TextUtils.isEmpty(validate)) {
                                            NetEaseVerify.getInstance().setSetVerify(validate);
                                            requireActivity().runOnUiThread(LoginByPhoneFragment.this::doCheckPermissions);
                                        } else {
                                            if (!TextUtils.isEmpty(msg) && msg.equals("验证失败")) {
                                                msg = getString(R.string.verify_error);
                                            }
                                            ToastUtils.showShort(msg);
                                        }
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        ToastUtils.showShort(s);
                                    }

                                });
                                dialog.dismiss();
                            }).setTextDes(getString(R.string.confirm_phone_number));
                } else {
                    ToastUtils.showShort(getString(R.string.toast_phone_number_used));
                }
            }
        });
    }

    public void doCheckPermissions() {
        RxPermissions rxPermissions = new RxPermissions(requireActivity());
        rxPermissions.request(
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        // 所有权限都同意
                        loginViewModel.changePage(LoginPageType.LoginByPwd, phoneNum, requireActivity());
                    } else {
                        // 有的权限被拒绝或被勾选不再提示
                        new AlertDialog.Builder(getActivity())
                                .setCancelable(false)
                                .setMessage(getString(R.string.notePermission))
                                .setPositiveButton(getString(R.string.immiPer), (dialog, which) -> doCheckPermissions())
                                .show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (loginViewModel.getPhoneCodeCountDowan() != null) {
            loginViewModel.getPhoneCodeCountDowan().cancel();
        }
    }
}

