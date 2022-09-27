package com.live.fox.ui.mine.activity.Setting;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityPaymentPasswordBinding;
import com.live.fox.databinding.ActivityPhoneBindingBinding;

public class PaymentPasswordActivity extends BaseBindingViewActivity {

    ActivityPaymentPasswordBinding mBind;

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_payment_password;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setActivityTitle(getString(R.string.paymentPasswordSetting));


    }
}
