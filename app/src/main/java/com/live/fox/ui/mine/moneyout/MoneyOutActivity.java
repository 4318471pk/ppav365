package com.live.fox.ui.mine.moneyout;

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
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
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
public class MoneyOutActivity extends BaseHeadActivity {

    private TextView tvMoney;
    private RoundTextView tvMoney2;
    private TextView tvCurrentStatement;
    private TextView tvNeedStatement;
    private LinearLayout layoutBottom;
    private TextView tvRule;
    private TextView tvAllStatement;
    private TextView tv_activityGoinCoin;
    private TextView tv_activityGoinCoinRecord;
    double quota = -1;

    GameStatement gameStatement;

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MoneyOutActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moneyout_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initView();
    }

    private void initView() {
        tvMoney = findViewById(R.id.tv_money);
        tvMoney2 = findViewById(R.id.tv_money2);
        tvCurrentStatement = findViewById(R.id.tv_currentStatement);
        tvNeedStatement = findViewById(R.id.tv_needStatement);
        layoutBottom = findViewById(R.id.ll_bottom);
        tvRule = findViewById(R.id.tv_rule);
        tvAllStatement = findViewById(R.id.tv_allStatement);
        tv_activityGoinCoin = findViewById(R.id.tv_activityGoinCoin);
        tv_activityGoinCoinRecord = findViewById(R.id.tv_activityGoinCoinRecord);
        findViewById(R.id.tv_moneyoutrecodre).setOnClickListener(this);
        findViewById(R.id.tv_moneyout).setOnClickListener(this);
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.goldTixian), true, true);
        tvMoney2.setVisibility(View.INVISIBLE);
        tvCurrentStatement.setVisibility(View.INVISIBLE);
        tvNeedStatement.setVisibility(View.INVISIBLE);
        layoutBottom.setVisibility(View.INVISIBLE);
        TextView proportion = findViewById(R.id.withdraw_proportion);
        proportion.setText(String.format(getString(R.string.exchangeRate),
                AppConfig.getExchangeRatio(), AppConfig.getCurrencySymbol()));
    }

    public void refreshPage() {
        User user = DataCenter.getInstance().getUserInfo().getUser();
        tvMoney.setText(RegexUtils.westMoney(user.getGoldCoin()));

        quota = gameStatement.isWithdraw() ? ArithUtil.mul(user.getGoldCoin(), Double.parseDouble(AppConfig.getExchangeRatio())) : 0;

        String withdrawal = String.format(getString(R.string.withdrawal_amount),
                RegexUtils.westMoney(quota), AppConfig.getCurrencySymbol());
        tvMoney2.setText(withdrawal);

        String currentBill = String.format(getString(R.string.currentBill),
                RegexUtils.westMoney((gameStatement.getAllNowStatement() *
                        Integer.parseInt(AppConfig.getExchangeRatio()))), AppConfig.getCurrencySymbol());
        tvCurrentStatement.setText(currentBill);

        String historyBill = String.format(getString(R.string.historyBill),
                RegexUtils.westMoney((long) (gameStatement.getAllStatement() *
                        Integer.parseInt(AppConfig.getExchangeRatio()))), AppConfig.getCurrencySymbol());
        tvAllStatement.setText(historyBill);

        double money = gameStatement.getNeedStatement() * Integer.parseInt(AppConfig.getExchangeRatio());
        String format = String.format(getString(R.string.withdraw_tip),
                RegexUtils.westMoney(money), AppConfig.getCurrencySymbol());
        tvNeedStatement.setText(format);

        String activityPrize = String.format(getString(R.string.activity_prize),
                RegexUtils.westMoney(gameStatement.getActivityGoinCoin()), getString(R.string.gold));
        tv_activityGoinCoin.setText(activityPrize);

        String turnover = String.format(getString(R.string.activity_prize_turnover),
                RegexUtils.westMoney(gameStatement.getActivityGoinCoinRecord()), getString(R.string.gold));
        tv_activityGoinCoinRecord.setText(turnover);

        tvMoney2.setVisibility(View.VISIBLE);
        tvCurrentStatement.setVisibility(View.VISIBLE);
        tvNeedStatement.setVisibility(View.VISIBLE);

        if (!StringUtils.isEmpty(gameStatement.getWithDrawContent())) {
            layoutBottom.setVisibility(View.VISIBLE);
            tvRule.setText(Html.fromHtml(gameStatement.getWithDrawContent()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog(getString(R.string.baseLoading), false, false);
        doGetInfo();
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_moneyoutrecodre: //提现记录
                FragmentContentActivity.startJbRecordActivity(MoneyOutActivity.this);
                break;
            case R.id.tv_moneyout: //提现
                if (gameStatement != null) {
                    MoneyOutToCardActivity.startActivity(MoneyOutActivity.this, 1,
                            quota, gameStatement.isWithdraw(), gameStatement.getContent());//金币提现
                }
                break;
        }
    }

    public void doGetInfo() {
        Api_User.ins().getStatement(new JsonCallback<GameStatement>() {
            @Override
            public void onSuccess(int code, String msg, GameStatement data) {
                hideLoadingDialog();
                if (code == 0) {
                    gameStatement = data;
                    refreshPage();
                } else {
                    ToastUtils.showShort(msg);
                    finish();
                }
            }
        });
    }


}
