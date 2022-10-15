package com.live.fox.ui.mine.withdraw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.flyco.roundview.RoundTextView;
import com.live.fox.AppConfig;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityWithdrawalBinding;
import com.live.fox.entity.GameStatement;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_User;
import com.live.fox.utils.ArithUtil;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.PasswordPopUtitls;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 提现
 */
public class WithdrawalActivity extends BaseBindingViewActivity {

    ActivityWithdrawalBinding mBind;

    PasswordPopUtitls passwordPopUtitls;

    public static void startActivity(Context context) {
        Intent i = new Intent(context, WithdrawalActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClickView(View view) {
        if (view == mBind.gtCommit) {
            passwordPopUtitls.show();
        }



    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_withdrawal;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        mBind.setClick(this);
        passwordPopUtitls = new PasswordPopUtitls(this, mBind.getRoot());
        setActivityTitle(getString(R.string.account_withdrawal));
        getTvTitleRight().setText(getString(R.string.withdrawCards));
        getTvTitleRight().setTextColor(0xFFA800FF);
        getTvTitleRight().setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                startActivityForResult(new Intent(WithdrawalActivity.this,BankCardListActivity.class), ConstantValue.REQUEST_CODE1);
            }
        });

        String string = "<font color='#B8B2C8'> " +getResources().getString(R.string.contact_service)+ "</font>";
        string = string +"<a href='http://www.baidu.com'>" + getResources().getString(R.string.online_service)+"</a><br/>" ;
        mBind.tvService.setText(Html.fromHtml(string));
        mBind.tvService.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString spannedString=new SpannableString(mBind.tvMax.getText().toString());
        spannedString.setSpan(new UnderlineSpan(),0,spannedString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBind.tvMax.setText(spannedString);

        passwordPopUtitls.setPayPwdConfirm(new PasswordPopUtitls.PayPwdConfirm() {
            @Override
            public void clickConfirm(String pwd) {

            }
        });
    }


}
