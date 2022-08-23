package com.live.fox.ui.login;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.databinding.LoginBypwdFragmentBinding;
import com.live.fox.entity.RegisterEntity;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.verify.NetEaseVerify;


/**
 * 短信验证码注册
 */
public class LoginByPwdFragment extends BaseFragment {

    String phoneNum = "";

    LoginViewModel loginViewModel;
    LoginBypwdFragmentBinding loginBypwdFragmentBinding;

    public static LoginByPwdFragment newInstance(String phoneNum) {
        LoginByPwdFragment fragment = new LoginByPwdFragment();
        Bundle args = new Bundle();
        args.putString("phoneNum", phoneNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            loginBypwdFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.login_bypwd_fragment, container, false);
            rootView = loginBypwdFragmentBinding.getRoot();
            loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
            loginBypwdFragmentBinding.setViewModel(loginViewModel);
            loginBypwdFragmentBinding.setLifecycleOwner(getActivity());
            initData(getArguments());
            setView();
        }
        return rootView;
    }


    public void initData(Bundle bundle) {
        if (bundle != null) {
            phoneNum = bundle.getString("phoneNum");
        }
    }

    public void setView() {
        if (!StringUtils.isEmpty(phoneNum)) {
            loginBypwdFragmentBinding.tvNum.setText(getString(R.string.verify_code_send_to) + phoneNum);
            loginViewModel.startPhoneCodeCountDown();
            RegisterEntity register = new RegisterEntity();
            register.setType("1");
            register.setVerification(NetEaseVerify.getInstance().getVerificationNo());
            register.setVersion(AppUtils.getAppVersionName());
            register.setVerify(NetEaseVerify.getInstance().getSetVerify());
            register.setName(phoneNum);
            loginViewModel.doSendPhoneCodeApi(register);
        }

        loginViewModel.getSmsCodeIsFinish().observe(this, isFinish -> {
            if (isFinish) {
                loginBypwdFragmentBinding.tvGetphonecode.setText(getString(R.string.getCaptcha));
                loginBypwdFragmentBinding.tvGetphonecode.setClickable(true);
                loginBypwdFragmentBinding.tvGetphonecode.setEnabled(true);
            } else {
                loginBypwdFragmentBinding.tvGetphonecode.setClickable(false);
                loginBypwdFragmentBinding.tvGetphonecode.setEnabled(false);
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

