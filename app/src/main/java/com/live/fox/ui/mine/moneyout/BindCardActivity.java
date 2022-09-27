package com.live.fox.ui.mine.moneyout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.live.fox.AppConfig;
import com.live.fox.Constant;
import com.live.fox.MainActivity;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.ActionDialog;
import com.live.fox.entity.BankInfoList;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.verify.NetEaseVerify;
import com.live.fox.verify.SimpleCaptchaListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 绑定银行卡
 * /Center-client/user/bankList/Info   获取用户已绑定银行卡信息 接口
 */
public class BindCardActivity extends BaseHeadActivity {

    private EditText etCard;
    private TextView tvCardbank;
    private EditText etNickname;
    private LinearLayout llNickname;
    private EditText etCity;
    private EditText et_password;
    private TextView tv_phone;
    private TextView tv_getphonecode;
    private EditText et_phonecode;
    private LinearLayout layoutBankinfo;
    private Button btn;

    String cardnum;
    String bankCode;
    String cardNo = "";
    String bankName = "";
    String trueName = "";
    String bankProvince = "";
    String bankCity = "";
    String bankSub = "";
    String mobile = "";
    String phoneNum, bankCardNum;
    boolean isNewUser = false;

    public static void startActivity(Context context, String mobile) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, BindCardActivity.class);
        i.putExtra("mobile", mobile);
        context.startActivity(i);
    }

    public static void startActivity(Context context, String mobile, boolean isNewUser) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, BindCardActivity.class);
        i.putExtra("mobile", mobile);
        i.putExtra("new_user", isNewUser);
        context.startActivity(i);
    }

    public static void startActivity(Context context, String mobile, String trueName) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, BindCardActivity.class);
        i.putExtra("mobile", mobile);
        i.putExtra("trueName", trueName);
        context.startActivity(i);
    }

    public static void startActivity2(Context context, String cardNo, String bankName,
                                      String trueName, String bankProvince,
                                      String bankCity, String bankSub, String mobile) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, BindCardActivity.class);
        i.putExtra("cardNo", cardNo);
        i.putExtra("bankName", bankName);
        i.putExtra("trueName", trueName);
        i.putExtra("bankProvince", bankProvince);
        i.putExtra("bankCity", bankCity);
        i.putExtra("bankSub", bankSub);
        i.putExtra("mobile", mobile);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bindcard_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        Intent intent = getIntent();
        cardNo = intent.getStringExtra("cardNo");
        bankName = intent.getStringExtra("bankName");
        trueName = intent.getStringExtra("trueName");
        bankProvince = intent.getStringExtra("bankProvince");
        bankCity = intent.getStringExtra("bankCity");
        bankSub = intent.getStringExtra("bankSub");
        mobile = intent.getStringExtra("mobile");
        isNewUser = intent.getBooleanExtra("new_user", false);

        initView();

        if (AppConfig.isThLive()) {
            tvCardbank.setHint(getString(R.string.bank_card_choose));
        }

        if (isNewUser) {
            mobile = DataCenter.getInstance().getUserInfo().getUser().getPhone();
        }

        etNickname.setTransformationMethod(StringUtils.replacementTransformationMethod);
        phoneNum = mobile;

        if (!StringUtils.isEmpty(mobile)) {
            mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, 10);
            tv_phone.setText(mobile);
        }
        if (!StringUtils.isEmpty(trueName)) {
            etNickname.setText(trueName);
            etNickname.setFocusable(false);
        }

        if (!StringUtils.isEmpty(cardNo)) {
            bankCardNum = cardNo;
            cardNo = "******" + cardNo.substring(cardNo.length() - 4);
            etCard.setText(cardNo);
            etCard.setFocusable(false);
            tvCardbank.setText(bankName);
            etCity.setText(bankCity);
            etCity.setFocusable(false);
        }
    }

    private void initView() {


        etCard = findViewById(R.id.et_card);
        tvCardbank = findViewById(R.id.et_cardbank);
        etNickname = findViewById(R.id.et_nickname);
        llNickname = findViewById(R.id.ll_nickname);
        etCity = findViewById(R.id.et_city);
        et_password = findViewById(R.id.et_password);
        tv_phone = findViewById(R.id.tv_phone);
        tv_getphonecode = findViewById(R.id.tv_getphonecode);
        et_phonecode = findViewById(R.id.et_phonecode);
        layoutBankinfo = findViewById(R.id.layout_bankinfo);
        btn = findViewById(R.id.bind_card_submit);
        findViewById(R.id.tv_getphonecode).setOnClickListener(this);
        findViewById(R.id.bind_card_submit).setOnClickListener(this);
        findViewById(R.id.layout_bankinfo).setOnClickListener(this);

        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        if (isNewUser) {
            setHead(getString(R.string.bandBankcark), false, getString(R.string.ignore), true);
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setOnClickListener(view -> {
                MainActivity.startActivity(BindCardActivity.this);
                finish();
            });

        } else {
            setHead(getString(R.string.bandBankcark), true, true);
        }
    }

    PhoneCodeCountDowan phoneCodeCountDowan;

    public void startPhoneCodeCountDown() {
        if (phoneCodeCountDowan == null) {
            phoneCodeCountDowan = new PhoneCodeCountDowan(180 * 1000);
        }
        phoneCodeCountDowan.start();
    }

    class PhoneCodeCountDowan extends CountDownTimer {
        private static final int TIME_TASCK = 1000;

        public PhoneCodeCountDowan(long millisInFuture) {
            super(millisInFuture, TIME_TASCK);
        }

        @Override
        public void onFinish() {// 计时完毕
            tv_getphonecode.setText(getString(R.string.reGet));
            tv_getphonecode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            tv_getphonecode.setText((millisUntilFinished / TIME_TASCK) + "");
        }
    }

    public void doSendPhoneCodeApi() {
        Api_Auth.ins().sendPhoneCode2(phoneNum, 5, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(data);
                if (code != 0) {
                    phoneCodeCountDowan.cancel();
                    phoneCodeCountDowan.onFinish();
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getphonecode:

                if (!StringUtils.isEmpty(phoneNum) && phoneNum.length() == 10 && phoneNum.startsWith("0")) {
                    verifyExtracted();
                } else {
                    ToastUtils.showShort(getString(R.string.telWrongInput));
                }
                break;

            case R.id.bind_card_submit:
                cardnum = etCard.getText().toString().trim();
                if (!cardnum.contains("******")) {
                    bankCardNum = cardnum;
                }
                String name = etNickname.getText().toString().trim();
                String city = etCity.getText().toString().trim();
                String phonecode = et_phonecode.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (StringUtils.isEmpty(bankCardNum) || bankCardNum.length() < 5) {// || !checkBankCard(bankCardNum)
                    ToastUtils.showShort(getString(R.string.enterCorrectNumber));
                    return;
                }

                if (StringUtils.isEmpty(name)) {
                    ToastUtils.showShort(getString(R.string.enterName));
                    return;
                }
                if (StringUtils.isEmpty(city)) {
                    city = "unknow";
                }
                String cardbank = tvCardbank.getText().toString().trim();
                if (StringUtils.isEmpty(cardbank)) {
                    ToastUtils.showShort(getString(R.string.inputBank));
                    return;
                }
                if (StringUtils.isEmpty(phonecode)) {
                    ToastUtils.showShort(getString(R.string.enter_verification_code));
                    return;
                }
                if (StringUtils.isEmpty(password) || 6 != password.length()) {
                    ToastUtils.showShort(getString(R.string.qxixPassword));
                    return;
                }

                doBindBankApi(bankCardNum, cardbank, bankCode, name, city, "shen",
                        "ppenCardLocation", phonecode, password.replaceAll(" ", ""));
                break;

            case R.id.layout_bankinfo:
                showLoadingDialog();
                Api_User.ins().getBankList(new JsonCallback<List<BankInfoList>>() {
                    @Override
                    public void onSuccess(int code, String msg, List<BankInfoList> bankInfoList) {
                        hideLoadingDialog();
                        if (code == 0 && bankInfoList.size() > 0) {
                            ActionDialog dialog = new ActionDialog(BindCardActivity.this, 2);

                            List<ActionDialog.ActionItem> banks = new ArrayList<>();
                            for (BankInfoList bankInfo : bankInfoList) {
                                ActionDialog.ActionItem actionItem = new ActionDialog.ActionItem();
                                actionItem.title = bankInfo.getBankName();
                                actionItem.imgUrl = bankInfo.getImg();
                                banks.add(actionItem);
                            }

                            dialog.setActions(banks);

                            dialog.setEventListener(new ActionDialog.OnEventListener() {
                                @Override
                                public void onActionItemClick(ActionDialog dialog, ActionDialog.ActionItem item, int position) {
                                    tvCardbank.setText(bankInfoList.get(position).getBankName());
                                    bankCode = bankInfoList.get(position).getBankCode();
                                }

                                @Override
                                public void onCancelItemClick(ActionDialog dialog) {

                                }
                            });
                            dialog.show();
                        } else {
                            showToastTip(false, msg);
                        }
                    }
                });
                break;
        }
    }

    /**
     * 网易验证
     */
    private void verifyExtracted() {
        NetEaseVerify.getInstance().init(BindCardActivity.this, new SimpleCaptchaListener() {
            @Override
            public void onValidate(String result, String validate, String msg) {
                if (!TextUtils.isEmpty(validate)) {
                    runOnUiThread(() -> {
                        startPhoneCodeCountDown();
                        doSendPhoneCodeApi();
                        tv_getphonecode.setEnabled(false);
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.showShort(s);
            }
        });
    }


    /**
     * 我的绑定银行卡信息
     */
    public void doBindBankApi(String cardNo, String bankName, String bankCode, String trueName,
                              String bankCity, String bankProvince, String bankSub,
                              String vcode, String cashPassword) {
        showLoadingDialog();
        Api_User.ins().bindBank(cardNo, bankName, bankCode, trueName, bankCity,
                bankProvince, bankSub, phoneNum, vcode, cashPassword, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        hideLoadingDialog();
                        if (code == 0) {
                            if (isNewUser) {
                                MainActivity.startActivity(BindCardActivity.this);
                            }
                            finish();
                        } else {
                            showToastTip(false, msg);
                        }
                    }
                });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


    /**
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     * 校验银行卡卡号
     */
//    public static boolean checkBankCard(String bankCard) {
//        if (bankCard.length() < 15 || bankCard.length() > 19) {
//            return false;
//        }
//        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
//        if (bit == 'N') {
//            return false;
//        }
//        return bankCard.charAt(bankCard.length() - 1) == bit;
//    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
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
