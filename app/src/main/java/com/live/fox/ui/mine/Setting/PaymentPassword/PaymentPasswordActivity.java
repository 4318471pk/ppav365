package com.live.fox.ui.mine.Setting.PaymentPassword;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityPaymentPasswordBinding;

public class PaymentPasswordActivity extends BaseBindingViewActivity {

    ActivityPaymentPasswordBinding mBind;

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,PaymentPasswordActivity.class));
    }
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

        showSettingView();
    }

    private void showSettingView()
    {
        PaymentPasswordFragment fragment=PaymentPasswordFragment.getInstance();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout,fragment);
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }
}
