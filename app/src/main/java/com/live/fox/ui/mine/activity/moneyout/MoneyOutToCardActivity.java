package com.live.fox.ui.mine.activity.moneyout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.roundview.RoundLinearLayout;
import com.google.gson.Gson;
import com.live.fox.AppConfig;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.ExchangeMoneyFragment;
import com.live.fox.dialog.MyBankCardDialogFragment;
import com.live.fox.entity.BankInfo;
import com.live.fox.entity.User;
import com.live.fox.server.Api_Promotion;
import com.live.fox.server.Api_User;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.MoneyTextWatcher;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.EditTextMoney;
import com.live.fox.view.EditTextWithDel;

/**
 * 提现
 * 提现到银行卡
 */
public class MoneyOutToCardActivity extends BaseHeadActivity {

    private RoundLinearLayout layoutBindcard;
    private EditTextMoney etMoney;
    private EditTextWithDel et_password;
    private TextView tvMymoney;
    private TextView tvCardbank;
    private TextView tvCardtopno;
    private TextView tvCardendno;
    private TextView tvMoneyquota;
    private RelativeLayout layoutCard;
    private TextView tv_toBankCard;
    User myUserInfo;
    BankInfo bankInfo;
    private MoneyTextWatcher moneyTextWatcher;

    boolean isBandCard = false;
    int pageType = 1; //1金币提现到银行卡 2.魅力值提现到银行卡 3.分享收益提现到银行卡
    double moneyCount = 0;
    boolean withdraw;
    String content;
    private ImageView ivBankLogo;
    private long coin;

    public static void startActivity(Context context, int pageType) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MoneyOutToCardActivity.class);
        i.putExtra("pageType", pageType);
        context.startActivity(i);
    }

    public static void startActivity(Context context, int pageType, double moneyCount) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MoneyOutToCardActivity.class);
        i.putExtra("pageType", pageType);
        i.putExtra("moneyCount", moneyCount);
        context.startActivity(i);
    }

    public static void startActivity(Context context, int pageType, double moneyCount, boolean withdraw, String content) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MoneyOutToCardActivity.class);
        i.putExtra("pageType", pageType);
        i.putExtra("moneyCount", moneyCount);
        i.putExtra("withdraw", withdraw);
        i.putExtra("content", content);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_card);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

        pageType = getIntent().getIntExtra("pageType", 1);
        moneyCount = getIntent().getDoubleExtra("moneyCount", 0);
        withdraw = getIntent().getBooleanExtra("withdraw", false);
        content = getIntent().getStringExtra("content");
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.tixian), true, true);

        setTitle(pageType == 1 ? getString(R.string.goldTixian) : pageType ==
                2 ? getString(R.string.souyijixian) : getString(R.string.sharedIncomea));
        layoutBindcard = findViewById(R.id.layout_bindcard);
        etMoney = findViewById(R.id.withdraw_edit_text_money);
        et_password = findViewById(R.id.et_password);
        tvMymoney = findViewById(R.id.tv_mymoney);
        tvCardbank = findViewById(R.id.tv_cardbank);
        tvCardtopno = findViewById(R.id.tv_cardtopno);
        tvCardendno = findViewById(R.id.tv_cardendno);
        tvMoneyquota = findViewById(R.id.tv_moneyquota);
        layoutCard = findViewById(R.id.layout_card);
        tv_toBankCard = findViewById(R.id.tv_toBankCard);
        ivBankLogo = findViewById(R.id.iv_bank_logo);
        findViewById(R.id.layout_bindcard).setOnClickListener(this);
        findViewById(R.id.tv_allout).setOnClickListener(this);
        findViewById(R.id.withdraw_submit).setOnClickListener(this);
        findViewById(R.id.tv_toBankCard).setOnClickListener(this);
        findViewById(R.id.layout_card).setOnClickListener(this);
        moneyTextWatcher = new MoneyTextWatcher(etMoney);
        etMoney.addTextChangedListener(moneyTextWatcher);
    }

    public void refreshPage() {
        TextView symbol = findViewById(R.id.withdraw_currency_symbol);
        symbol.setText(AppConfig.getCurrencySymbol());
        myUserInfo = AppUserManger.getUserInfo();

        if (pageType == 1) {
            tvMoneyquota.setVisibility(View.GONE);
            tvMoneyquota.setText("(" + getString(R.string.currentGold) + RegexUtils.westMoney(myUserInfo.getGoldCoin()) + ")");
            tvMymoney.setText(getString(R.string.withdraw_balances) + RegexUtils.westMoney(moneyCount) + AppConfig.getCurrencySymbol());
        } else if (pageType == 2) {
            tvMoneyquota.setVisibility(View.GONE);
            moneyCount = myUserInfo.getAnchorCoin() * Double.parseDouble(AppConfig.getExchangeRatio());
            tvMymoney.setText(getString(R.string.withdraw_balances) + RegexUtils.westMoney(moneyCount) + AppConfig.getCurrencySymbol());
        } else {
            tvMoneyquota.setVisibility(View.GONE);
            tvMymoney.setText(getString(R.string.withdraw_balances) + RegexUtils.westMoney(moneyCount) + AppConfig.getCurrencySymbol());
        }

        if (bankInfo == null) {
            isBandCard = false;
        } else {
            isBandCard = !StringUtils.isEmpty(bankInfo.getCardNo()) && String.valueOf(bankInfo.getCardNo()).length() >= 5;
        }

        if (isBandCard) {
            layoutCard.setVisibility(View.VISIBLE);
            layoutBindcard.setVisibility(View.GONE);
            tvCardbank.setText(bankInfo.getBankName());
            tvCardtopno.setText(bankInfo.getCardNo().substring(0, 4));
            tvCardendno.setText(bankInfo.getCardNo().substring(bankInfo.getCardNo().length() - 4));
            Glide.with(this).load(bankInfo.getLogs()).into(ivBankLogo);
        } else {
            layoutCard.setVisibility(View.GONE);
            layoutBindcard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_bindcard:
                if (bankInfo != null) {
                    BindCardActivity.startActivity(MoneyOutToCardActivity.this, bankInfo.getMobile());
                }
                break;

            case R.id.tv_allout: //全部提现
                if (moneyCount >= 0) {
                    moneyTextWatcher.setNeedSplicing(false);
                    etMoney.setText(RegexUtils.westMoney(moneyCount));
                    Editable etext = etMoney.getText();
                    Selection.setSelection(etext, RegexUtils.westMoney(moneyCount).length());
                }
                break;

            case R.id.withdraw_submit:
                String coinStr = etMoney.getText().toString().trim();
                if (!isBandCard) {
                    showToastTip(false, getString(R.string.bandFirst));
                    return;
                }
                if (StringUtils.isEmpty(coinStr)) {
                    ToastUtils.showShort(getString(R.string.qInputJiner));
                    return;
                }
                if (1 == pageType && !withdraw) {
                    showToastTip(false, content);
                    return;
                }

                coin = Long.parseLong(coinStr.replaceAll(",", ""));//钱数

                String password = String.valueOf(et_password.getText()).trim();
                if (StringUtils.isEmpty(password) || 6 != password.length()) {
                    ToastUtils.showShort(getString(R.string.qSixtiPassword));
                    return;
                }
                if (pageType == 2) {
                    LogUtils.e(coin + "," + myUserInfo.getAnchorCoin());
                    if (coin > myUserInfo.getAnchorCoin() * 1000) {
                        showToastTip(false, getString(R.string.duihuangBigger));
                        return;
                    }
                }

                KeyboardUtils.hideSoftInput(MoneyOutToCardActivity.this);
                ExchangeMoneyFragment exchangeMoneyFragment = ExchangeMoneyFragment.newInstance(pageType, coin);
                exchangeMoneyFragment.show(getSupportFragmentManager(), "");
                exchangeMoneyFragment.setBtnSureClick(() -> {
                    if (pageType == 3) {
                        doWithdrawByShareApi(coin, password);
                    } else {
                        doWithdrawApi(coin, password);
                    }
                });
                break;

            case R.id.tv_toBankCard:
                if (bankInfo != null && bankInfo.getCardNo() != null) {
                    BindCardActivity.startActivity2(MoneyOutToCardActivity.this, bankInfo.getCardNo(),
                            bankInfo.getBankName(), bankInfo.getTrueName(),
                            bankInfo.getBankProvince(), bankInfo.getBankCity(),
                            bankInfo.getBankSub(), bankInfo.getMobile());
                }
                break;

            case R.id.layout_card:
                if (bankInfo != null && bankInfo.getCardNo() != null) {
                    MyBankCardDialogFragment myBankCardDialogFragment
                            = MyBankCardDialogFragment.newInstance(bankInfo.getCardNo());
                    if (!myBankCardDialogFragment.isAdded()) {
                        myBankCardDialogFragment.show(getSupportFragmentManager(), "");
                        myBankCardDialogFragment.setOnItemClickListener(this::doGetBankInfoApi);
                    }
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        doGetBankInfoApi();
    }

    /**
     * 我的绑定银行卡信息
     */
    public void doGetBankInfoApi() {
        bankInfo = null;
        Api_User.ins().getBankInfo(new JsonCallback<BankInfo>() {
            @Override
            public void onSuccess(int code, String msg, BankInfo data) {
                if (code == 0) {
                    bankInfo = data;
                    refreshPage();
                    if (0 == data.getIshaveCashPwd() && !StringUtils.isEmpty(data.getCardNo())) {
                        tv_toBankCard.setVisibility(View.VISIBLE);
                        et_password.setVisibility(View.GONE);
                    } else {
                        tv_toBankCard.setVisibility(View.GONE);
                        et_password.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 提现到银行卡
     * 1金币提现
     * 2魅力提现
     */
    public void doWithdrawApi(long coin, String cashPassword) {
        showLoadingDialog();
        Api_User.ins().withdraw(bankInfo.getCardId(), 1, coin, cashPassword, pageType, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0) {
                    if (pageType == 1) {
                        myUserInfo.setGoldCoin(myUserInfo.getGoldCoin() - coin / Double.parseDouble(AppConfig.getExchangeRatio()));
                    } else {
                        myUserInfo.setAnchorCoin(myUserInfo.getAnchorCoin() - coin / Double.parseDouble(AppConfig.getExchangeRatio()));
                    }
                    AppUserManger.initUser(new Gson().toJson(myUserInfo));
                    showToastTip(true, getString(R.string.tixianOver));
                    finish();
                } else {
                    showToastTip(false, msg);
                }
            }
        });
    }

    /**
     * 提现到银行卡
     */
    public void doWithdrawByShareApi(long coin, String cashPassword) {
        showLoadingDialog();
        Api_Promotion.ins().withdraw(coin, cashPassword, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0) {
                    showToastTip(true, getString(R.string.tixianSuccess));
                    finish();
                } else {
                    showToastTip(false, msg);
                }
            }
        });
    }


    /**
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if (bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}
