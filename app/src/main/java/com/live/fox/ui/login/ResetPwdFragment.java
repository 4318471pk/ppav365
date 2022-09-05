package com.live.fox.ui.login;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.databinding.LoginResetpwdFragmentBinding;
import com.live.fox.utils.BlankController;
import com.live.fox.utils.LogUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * 设置新密码
 * 修改密码
 */
public class ResetPwdFragment extends BaseFragment {

    String phoneNum;
    int pageNum;
    LoginViewModel loginViewModel;
    LoginResetpwdFragmentBinding mBinding;

    public static ResetPwdFragment newInstance(String phoneNum, int pageNum) {
        ResetPwdFragment fragment = new ResetPwdFragment();
        Bundle args = new Bundle();
        args.putString("phoneNum", phoneNum);
        args.putInt("pageNum", pageNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            LogUtils.e("onCreateView");
            mBinding = DataBindingUtil.inflate(inflater, R.layout.login_resetpwd_fragment, container, false);
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
            phoneNum = bundle.getString("phoneNum", "");
            pageNum = bundle.getInt("pageNum", 0);
        }
    }

    public void setView(View view) {
        mBinding.etPwd.setFilters(new InputFilter[]{new BlankController()});
        mBinding.etCpwd.setFilters(new InputFilter[]{new BlankController()});
        loginViewModel.getSmsCodeTxt().setValue(view.getContext().getString(R.string.get_verification_code));

        mBinding.tvTelphone.setText(view.getContext().getString(R.string.verify_code_send_to) + phoneNum);

        loginViewModel.getDoCheckPermissionForResetpwd().observe(this, aBoolean -> {
            if (aBoolean) {
                doCheckPermissions();
            }
        });

        loginViewModel.getSmsCodeIsFinish().observe(this, isFinish -> {
            if (isFinish) {
                mBinding.tvGetphonecode.setText(view.getContext().getString(R.string.get_verification_code));
                mBinding.tvGetphonecode.setClickable(true);
                mBinding.tvGetphonecode.setEnabled(true);
            } else {
                mBinding.tvGetphonecode.setClickable(false);
                mBinding.tvGetphonecode.setEnabled(false);
            }
        });
    }

    /**
     * 检查权限
     */
    public void doCheckPermissions() {
        RxPermissions rxPermissions = new RxPermissions(requireActivity());
        rxPermissions.request(
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        // 所有权限都同意
                        loginViewModel.doResetPwdApi(requireContext());
                    } else {
                        // 有的权限被拒绝或被勾选不再提示
                        LogUtils.e("有的权限被拒绝");
                        new AlertDialog.Builder(requireActivity())
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
        LogUtils.e("onDestroyView");
        if (loginViewModel.getPhoneCodeCountDowan() != null) {
            loginViewModel.getPhoneCodeCountDowan().cancel();
        }
    }
}

