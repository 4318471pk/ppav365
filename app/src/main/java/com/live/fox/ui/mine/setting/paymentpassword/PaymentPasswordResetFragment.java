package com.live.fox.ui.mine.setting.paymentpassword;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentPayPwdResetBinding;
import com.live.fox.entity.RegisterEntity;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_User;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.ToastUtils;

import org.jetbrains.annotations.NotNull;

public class PaymentPasswordResetFragment extends BaseBindingFragment {

    int remainSecond=60;

    Handler handler = new Handler(Looper.myLooper()) {

        @Override
        public void dispatchMessage(@NonNull @NotNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    if(getActivity()==null || getActivity().isFinishing() || getActivity().isDestroyed())return;
                    remainSecond--;
                    if (remainSecond > 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(String.valueOf(remainSecond)).append("s");
                        SpannableString spannedString=new SpannableString(sb.toString());
                        spannedString.setSpan(new UnderlineSpan(),0,spannedString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mBind.tvSendVerifyCode.setText(spannedString);
                        mBind.tvSendVerifyCode.setEnabled(false);
                        sendEmptyMessageDelayed(0, 1000);
                    } else {
                        remainSecond = 60;
                        SpannableString spannedString=new SpannableString(getString(R.string.sendVerifyCodeAgain));
                        spannedString.setSpan(new UnderlineSpan(),0,spannedString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mBind.tvSendVerifyCode.setText(spannedString);
                        mBind.tvSendVerifyCode.setEnabled(true);
                    }
                    break;
            }
        }
    };

    FragmentPayPwdResetBinding mBind;
    public static PaymentPasswordResetFragment getInstance()
    {
        return new PaymentPasswordResetFragment();
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.tvSendVerifyCode:
                doSendPhoneCodeApi();
                break;
            case R.id.gtCommit:
                resetPassword();
                break;
        }

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_pay_pwd_reset;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        User user= DataCenter.getInstance().getUserInfo().getUser();
        mBind.tvPhoneNum.setText(user.getArea()+" "+user.getPhone());

        SpannableString spannedString=new SpannableString(mBind.tvSendVerifyCode.getText().toString());
        spannedString.setSpan(new UnderlineSpan(),0,spannedString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBind.tvSendVerifyCode.setText(spannedString);
    }

    public void doSendPhoneCodeApi() {
        User user= DataCenter.getInstance().getUserInfo().getUser();
        String phoneNum=user.getPhone();
        String area=user.getArea();
        if(TextUtils.isEmpty(phoneNum))
        {
            ToastUtils.showShort(getString(R.string.tipsPhoneNumFormatWrong));
            return;
        }
        showLoadingDialog();
        RegisterEntity registerEntity=new RegisterEntity();
        registerEntity.setName(phoneNum);
        registerEntity.setType("2");
        registerEntity.setVersion(AppUtils.getAppVersionName());
        registerEntity.setArea(area);
        Api_Auth.ins().sendPhoneCode(registerEntity, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                dismissLoadingDialog();
                handler.sendEmptyMessage(0);
                mBind.tvSendVerifyCode.setEnabled(false);
            }
        });
    }

    private void resetPassword()
    {
        User user= DataCenter.getInstance().getUserInfo().getUser();
        String phoneNum=user.getPhone();
        String area=user.getArea();
        String vCode=mBind.etVerifyCode.getText().toString();
        String findBackCode=mBind.etFindBackCode.getText().toString();
        String newPwd=mBind.etNewPassword.getText().toString();
        String conPwd=mBind.etConfirmPassword.getText().toString();

        showLoadingDialog();
        Api_User.ins().resetPaymentPassword(area, phoneNum,vCode,newPwd, conPwd,findBackCode, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                dismissLoadingDialog();
                if(code==0)
                {
                    ToastUtils.showShort(getStringWithoutContext(R.string.resetPaymentPasswordSuccess));
                    getActivity().finish();
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(handler!=null)
        {
            handler.removeMessages(0);
            handler=null;
        }
    }

}
