package com.live.fox.ui.mine.setting.paymentpassword;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityPaymentPasswordBinding;
import com.live.fox.dialog.temple.DialogGoBindPhone;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_User;
import com.live.fox.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class PaymentPasswordActivity extends BaseBindingViewActivity {

    ActivityPaymentPasswordBinding mBind;
    List<BaseBindingFragment> fragments;

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
        fragments=new ArrayList<>();
        fragments.add(PaymentPasswordFragment.getInstance());
        fragments.add(PaymentPasswordOperationListFragment.getInstance());
        fragments.add(PaymentPasswordModifyFragment.getInstance());
        fragments.add(PaymentPasswordResetFragment.getInstance());

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        for (int i = 0; i <fragments.size() ; i++) {
            fragmentTransaction.add(R.id.frameLayout,fragments.get(i));
        }

        for (int i = 0; i <fragments.size() ; i++) {
            fragmentTransaction.hide(fragments.get(i));
        }
        fragmentTransaction.commitAllowingStateLoss();
        getShowStatus();
    }

    private void showSettingView()
    {
        setActivityTitle(getString(R.string.paymentPasswordSetting));
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragments.get(0));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showModifyView()
    {
        setActivityTitle(getString(R.string.password_change));
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragments.get(2));
        fragmentTransaction.hide(fragments.get(1));
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void showOperateListView()
    {
        setActivityTitle(getString(R.string.resetPassword));
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragments.get(1));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showResetView()
    {
        setActivityTitle(getString(R.string.resetPassword));
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragments.get(3));
        fragmentTransaction.hide(fragments.get(1));
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void getShowStatus()
    {
        User user= DataCenter.getInstance().getUserInfo().getUser();
        if(TextUtils.isEmpty(user.getPhone()) || user.getHasPayPwd()==null || user.getHasPayPwd()!=1)
        {
            doGetUserInfoByUidApi();
        }
        else
        {
            showOperateListView();
        }

    }

    /**
     * 获取用户信息
     */
    public void doGetUserInfoByUidApi() {
        showLoadingDialog();
        User user = DataCenter.getInstance().getUserInfo().getUser();
        Api_User.ins().getUserInfo(user.getUid(), new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0) {
                    User user= DataCenter.getInstance().getUserInfo().getUser();
                    if(TextUtils.isEmpty(user.getPhone()))
                    {
                        showSettingView();
                        DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), DialogGoBindPhone.getInstance());
                    }
                    else
                    {
                        if(user.getHasPayPwd()==null || user.getHasPayPwd()==null || user.getHasPayPwd()!=1)
                        {
                            showSettingView();
                        }
                        else
                        {
                            showOperateListView();
                        }
                    }
                } else {
                    ToastUtils.showShort(msg);
                    finish();
                }
            }
        });
    }
}
