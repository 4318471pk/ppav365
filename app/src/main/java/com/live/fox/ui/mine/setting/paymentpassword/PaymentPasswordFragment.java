package com.live.fox.ui.mine.setting.paymentpassword;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentPaymentPasswordBinding;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_User;
import com.live.fox.utils.ToastUtils;

public class PaymentPasswordFragment extends BaseBindingFragment {

    FragmentPaymentPasswordBinding mBind;
    public static PaymentPasswordFragment getInstance()
    {
        return new PaymentPasswordFragment();
    }

    @Override
    public void onClickView(View view) {
        if(view.getId()==mBind.gtCommit.getId())
        {
            setPassword();
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_payment_password;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

    }


    private void setPassword()
    {
        showLoadingDialog();
        String pwd=mBind.etPaymentPassword.getText().toString();
        String conPwd=mBind.etConfirmPassword.getText().toString();
        String fCode=mBind.etFindBackCode.getText().toString();

        Api_User.ins().setPaymentPassword(pwd, conPwd, fCode, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                dismissLoadingDialog();
                if(code==0)
                {
                    User user=new User();
                    user.setHasPayPwd(1);
                    DataCenter.getInstance().getUserInfo().updateUser(user);
                    ToastUtils.showShort(getStringWithoutContext(R.string.setPaymentPasswordSuccess));
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
