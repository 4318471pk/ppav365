package com.live.fox.ui.mine.setting.paymentpassword;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentPayPwdModifyBinding;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_User;
import com.live.fox.utils.ToastUtils;

public class PaymentPasswordModifyFragment extends BaseBindingFragment {

    FragmentPayPwdModifyBinding mBinding;
    public static PaymentPasswordModifyFragment getInstance()
    {
        return new PaymentPasswordModifyFragment();
    }

    @Override
    public void onClickView(View view) {
        if(view.getId()==mBinding.gtCommit.getId())
        {
            modifyPassword();
        }

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_pay_pwd_modify;
    }

    @Override
    public void initView(View view) {
        mBinding=getViewDataBinding();
        mBinding.setClick(this);
    }

    private void modifyPassword()
    {
        String oldPwd=mBinding.etOldPassword.getText().toString();
        String newPwd=mBinding.etNewPassword.getText().toString();
        String conPwd=mBinding.etConfirmPassword.getText().toString();


        Api_User.ins().modifyPaymentPassword(oldPwd, newPwd, conPwd, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(code==0)
                {
                    ToastUtils.showShort(getStringWithoutContext(R.string.modifyPaymentPasswordSuccess));
                    getActivity().finish();
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });

    }
}
