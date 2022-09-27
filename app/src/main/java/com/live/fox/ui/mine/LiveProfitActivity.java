package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.flyco.roundview.RoundTextView;
import com.live.fox.AppConfig;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.ViewPagerFragmentAdapter;
import com.live.fox.base.BaseFragment;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.RuleDialog;
import com.live.fox.entity.RuleBean;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.moneyout.ExChangeMoneyActivity;
import com.live.fox.ui.mine.moneyout.MoneyOutRecord3Fragment;
import com.live.fox.ui.mine.moneyout.MoneyOutToCardActivity;
import com.live.fox.ui.mine.moneyout.MoneyoutRecord2Fragment;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.tab.SimpleTabLayout;


/**
 * 直播收益
 */
public class LiveProfitActivity extends BaseHeadActivity implements View.OnClickListener {

    private TextView tvMl;
    private RoundTextView tvMoney;

    ViewPagerFragmentAdapter<BaseFragment> adapter;

    BaseFragment[] fragments = new BaseFragment[2];

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, LiveProfitActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_profit);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.liveProfit), true, true);

        tvMl = findViewById(R.id.tv_ml);
        tvMoney = findViewById(R.id.tv_money);
        SimpleTabLayout tabLayout = findViewById(R.id.tabLayout_);
        ViewPager viewPager = findViewById(R.id.viewpager_);
        findViewById(R.id.rl_txgz).setOnClickListener(this);
        findViewById(R.id.tv_convertmoney).setOnClickListener(this);
        findViewById(R.id.lie_profit_withdraw).setOnClickListener(this);
        findViewById(R.id.rl_dhgz).setOnClickListener(this);

        fragments[0] = MoneyoutRecord2Fragment.newInstance(2);
        fragments[1] = MoneyOutRecord3Fragment.newInstance();
        adapter = new ViewPagerFragmentAdapter<>(getSupportFragmentManager());
        //遍历栏目列表 设置Fragment
        String[] titles = {getString(R.string.duihuangjinbi), getString(R.string.xianjintixiazn)};
        for (int i = 0; i < titles.length; i++) {
            adapter.addFragment(fragments[i], titles[i]);
        }
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }

    public void refreshPage() {
        User user = DataCenter.getInstance().getUserInfo().getUser();
        TextView lowest = findViewById(R.id.withdraw_lowest_money);
        TextView proportion = findViewById(R.id.withdraw_live_proportion);
        proportion.setText(String.format(getString(R.string.liveProfitExchangeRate),
                AppConfig.getExchangeRatio(), AppConfig.getCurrencySymbol()));
        lowest.setText(String.format(getString(R.string.minimumCashAmount),
                AppConfig.getWithdrawLowest(), AppConfig.getCurrencySymbol()));
        tvMl.setText(RegexUtils.westMoney(user.getAnchorCoin()));
        double balance = user.getAnchorCoin() * Integer.parseInt(AppConfig.getExchangeRatio());
        String format = String.format(getString(R.string.currentBalance),
                RegexUtils.westMoney(balance), AppConfig.getCurrencySymbol());
        tvMoney.setText(format);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPage();
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.rl_txgz: //右
                getRuleApi(1);
                break;
            case R.id.tv_convertmoney: //兑换金币
                ExChangeMoneyActivity.startActivity(LiveProfitActivity.this, 4);
                break;
            case R.id.lie_profit_withdraw: //提现
                MoneyOutToCardActivity.startActivity(LiveProfitActivity.this, 2);
                break;
            case R.id.rl_dhgz://左
                getRuleApi(2);
                break;
        }
    }

    private void getRuleApi(int type) {
        showLoadingView();
        Api_User.ins().getRule(type, new JsonCallback<RuleBean>() {
            @Override
            public void onSuccess(int code, String msg, RuleBean data) {
                hideLoadingView();
                if (code == 0) {
                    if (data == null) {
                        ToastUtils.showShort(getString(R.string.noData));
                    } else {
                        RuleDialog ruleDialog;
                        if (type == 1) {
                            ruleDialog = RuleDialog.newInstance(getString(R.string.tixianRule), data.getContent());
                        } else {
                            ruleDialog = RuleDialog.newInstance(getString(R.string.gold_exchange_in_rules), data.getContent());
                        }
                        ruleDialog.show(getSupportFragmentManager(), "");
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }
}
