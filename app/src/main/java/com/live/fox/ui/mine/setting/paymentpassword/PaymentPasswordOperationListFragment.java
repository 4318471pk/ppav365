package com.live.fox.ui.mine.setting.paymentpassword;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentPayPwdListBinding;

public class PaymentPasswordOperationListFragment extends BaseBindingFragment {

    FragmentPayPwdListBinding mBind;
    public static PaymentPasswordOperationListFragment getInstance()
    {
        return new PaymentPasswordOperationListFragment();
    }

    @Override
    public void onClickView(View view) {
        PaymentPasswordActivity activity=(PaymentPasswordActivity)getActivity();
        switch (view.getId())
        {
            case R.id.rlPasswordReset:
                activity.showResetView();
                break;
            case R.id.rlPasswordModify:
                activity.showModifyView();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_pay_pwd_list;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);
    }
}
