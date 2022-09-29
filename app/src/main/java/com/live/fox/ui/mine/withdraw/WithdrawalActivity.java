package com.live.fox.ui.mine.withdraw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.live.fox.AppConfig;
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
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;


/**
 * 提现
 */
public class WithdrawalActivity extends BaseBindingViewActivity {

    ActivityWithdrawalBinding mBind;

    public static void startActivity(Context context) {
        Intent i = new Intent(context, WithdrawalActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_withdrawal;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setActivityTitle(getString(R.string.account_withdrawal));
        getTvTitleRight().setText(getString(R.string.withdrawCards));
        getTvTitleRight().setTextColor(0xFFA800FF);


    }


}
