package com.live.fox.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.live.fox.AppConfig;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.GameGoinChangeDialog;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_AgGame;
import com.live.fox.server.Api_FwGame;
import com.live.fox.server.Api_KyGame;
import com.live.fox.server.Api_TYGame;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;

import org.json.JSONObject;


/**
 * 我的余额
 */
public class MyBalanceActivity extends BaseHeadActivity implements
        AppIMManager.OnMessageReceivedListener, View.OnClickListener {

    private TextView tvMymoney;
    private TextView tvKymoney;
    private TextView tvAgmoney;
    private TextView tvTYmoney;
    private TextView tvFw;
    private TextView sabaMoney;

    private ImageView iv_autochange;

    private long mKyBalance; //ky
    private long mFwBalance;
    private long mAgBalance;
    private long mTYBalance;
    private long sabaBalance;

    private int gameType;

    User user;

    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, MyBalanceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamemoneychange_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

        initView();
    }

    public void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);

        setHead(getString(R.string.accountBalance), true, true);
        setHeadElevation(0);

        tvMymoney = findViewById(R.id.tv_mymoney);
        tvKymoney = findViewById(R.id.tv_kymoney);
        tvAgmoney = findViewById(R.id.tv_agmoney);
        tvTYmoney = findViewById(R.id.tv_tymoney);
        tvFw = findViewById(R.id.tvFw);
        TextView textView = findViewById(R.id.balance_transform_title);
        String format;
        if (AppConfig.isThLive()) {
            format = "MP";
        } else {
            format = "V8";
        }

        textView.setText(String.format(getString(R.string.balance_transform_title), format));
        iv_autochange = findViewById(R.id.iv_autochange);

        findViewById(R.id.iv_autochange).setOnClickListener(this);
        findViewById(R.id.tv_allback).setOnClickListener(this);
        findViewById(R.id.iv_kyqprefresh).setOnClickListener(this);
        findViewById(R.id.tv_kychange).setOnClickListener(this);
        findViewById(R.id.iv_agrefresh).setOnClickListener(this);
        findViewById(R.id.ivRereshFw).setOnClickListener(this);
        findViewById(R.id.rtvFw).setOnClickListener(this);
        findViewById(R.id.tv_agchange).setOnClickListener(this);
        findViewById(R.id.tv_tychange).setOnClickListener(this);
        findViewById(R.id.iv_saba_refresh).setOnClickListener(this);
        findViewById(R.id.tv_saba_change).setOnClickListener(this);

        AppIMManager.ins().addMessageListener(MyBalanceActivity.class, this);

        user = DataCenter.getInstance().getUserInfo().getUser();

        tvMymoney.setText(RegexUtils.westMoney(user.getGoldCoin()));

        refreshAutoChangeView();
        tvKymoney.setText("0");
        tvAgmoney.setText("0");
        tvTYmoney.setText("0");
        sabaMoney = findViewById(R.id.tv_saba_money);
        TextView moneyUnit = findViewById(R.id.money_unit);
        moneyUnit.setText(AppConfig.getCurrencySymbol());
        showLoadingDialog();
        doGetKyBalanceApi();
        doGetAgBalanceApi();
        doGetTYBalanceApi();
        requestBalanceApi(7);
    }

    @Override
    public void onIMReceived(int protocol, String msg) {
        try {
            JSONObject message = new JSONObject(msg);
            if (protocol == Constant.MessageProtocol.PROTOCOL_BALANCE_CHANGE) { //12.金币变动消息
                long uid = message.optLong("uid", -1);
                Double goldCoin = message.optDouble("goldCoin", -1);
                if (uid == user.getUid()) {
                    user.setGoldCoin(goldCoin.floatValue());
                    SPManager.saveUserInfo(user);
                    tvMymoney.setText(RegexUtils.westMoney(goldCoin));
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void refreshAutoChangeView() {
        //1 不自动 ；2 自动
        iv_autochange.setImageResource(user.getAutoUpdownBalance() ==
                2 ? R.drawable.ic_autochangemoney_open
                : R.drawable.ic_autochangemoney_close);
    }

    public void refreshKyBalance(long balance) {
        mKyBalance = balance;
        LogUtils.e("mBalance: " + mKyBalance);
        tvKymoney.setText(String.valueOf(balance));
    }

    public void refreshFwBalance(long balance) {
        mFwBalance = balance;
        LogUtils.e("mBalance: " + mFwBalance);
        tvFw.setText(String.valueOf(balance));
    }

    public void refreshAgBalance(long balance) {
        mAgBalance = balance;
        LogUtils.e("mBalance: " + mAgBalance);
        tvAgmoney.setText(String.valueOf(balance));
    }

    public void refreshTYBalance(long balance) {
        mTYBalance = balance;
        tvTYmoney.setText(String.valueOf(balance));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_autochange:
                showLoadingDialogWithNoBgBlack();
                doAutoUpdownBalanceApi();
                break;
            case R.id.tv_allback:
                showLoadingDialog();
                doBackAllGameCoinApi();
                break;
            case R.id.iv_kyqprefresh:
                showLoadingDialog();
                doGetKyBalanceApi();
                break;
            case R.id.iv_agrefresh:
                showLoadingDialog();
                doGetAgBalanceApi();
                break;
            case R.id.iv_tyrefresh:
                showLoadingDialog();
                doGetTYBalanceApi();
                break;
            case R.id.tv_kychange:
                gameType = 1;
                showConfirmDialog();
                break;
            case R.id.tv_agchange:
                gameType = 2;
                GameGoinChangeDialog gameGoinChangeDialog2 =
                        GameGoinChangeDialog.newInstance(user.getGoldCoin(), 2, mAgBalance);
                gameGoinChangeDialog2.show(getSupportFragmentManager(), "dialogFragment");
                gameGoinChangeDialog2.setBtnSureClick((type, coin) -> {
                    if (type == 1) {
                        //转出 bg游戏币转为金币
                        showLoadingDialogWithNoBgBlack();
                        doAgBalanceDownApi(coin);
                    } else {
                        //转入 自己的金币转为bg游戏币
                        showLoadingDialogWithNoBgBlack();
                        doAgBalanceUpApi(coin);
                    }
                });
                break;
            case R.id.tv_tychange:
                gameType = 6;
                GameGoinChangeDialog gameGoinChangeDialog6 =
                        GameGoinChangeDialog.newInstance(user.getGoldCoin(), 6, mTYBalance);
                gameGoinChangeDialog6.show(getSupportFragmentManager(), "dialogFragment");
                gameGoinChangeDialog6.setBtnSureClick((type, coin) -> {
                    if (type == 1) {
                        //转出 bg游戏币转为金币
                        showLoadingDialogWithNoBgBlack();
                        doTYBalanceDownApi(coin);
                    } else {
                        //转入 自己的金币转为bg游戏币
                        showLoadingDialogWithNoBgBlack();
                        doTYBalanceUpApi(coin);
                    }
                });
                break;
            case R.id.ivRereshFw:
                showLoadingDialog();
                doGetFwBalanceApi();
                break;

            case R.id.rtvFw:
                gameType = 4;
                GameGoinChangeDialog gameGoinChangeDialog4 =
                        GameGoinChangeDialog.newInstance(user.getGoldCoin(), 4, mFwBalance);
                gameGoinChangeDialog4.show(getSupportFragmentManager(), "dialogFragment");
                gameGoinChangeDialog4.setBtnSureClick((type, coin) -> {
                    if (type == 1) {
                        //转出 fw游戏币转为金币
                        showLoadingDialogWithNoBgBlack();
                        doFwBalanceDownApi(coin);
                    } else {
                        //转入 自己的金币转为fw游戏币
                        showLoadingDialogWithNoBgBlack();
                        doFwBalanceUpApi(coin);
                    }
                });
                break;

            case R.id.tv_saba_change: //saba转账
                gameType = 7;
                showConfirmDialog();
                break;

            case R.id.iv_saba_refresh://saba 查询余额
                gameType = 7;
                requestBalanceApi(7);
                break;
        }
    }

    /**
     * 显示转入转出弹窗
     */
    private void showConfirmDialog() {
        long balance = 0;
        switch (gameType) {
            case 1:
                balance = mKyBalance;
                break;

            case 2:
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;
            case 6:
                break;
            case 7:
                balance = sabaBalance;
                break;
        }

        GameGoinChangeDialog dialog =
                GameGoinChangeDialog.newInstance(user.getGoldCoin(), gameType, balance);
        dialog.show(getSupportFragmentManager(), "dialogFragment");
        dialog.setBtnSureClick((type, coin) -> {
            if (type == 1) {
                transferOut(coin);
            } else {
                transferIn(coin);
            }
        });
    }

    /**
     * 转出
     *
     * @param money 金额
     */
    private void transferOut(long money) {
        showLoadingDialogWithNoBgBlack();
        Api_KyGame.ins().transferOut(gameType, money, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshBalance(money, gameType);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 转入
     *
     * @param money 金额
     */
    private void transferIn(long money) {
        showLoadingDialogWithNoBgBlack();
        Api_KyGame.ins().transferIn(gameType, money, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshBalance(money, gameType);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }


    /**
     * 查询Ky游戏玩家总分
     */
    public void doGetKyBalanceApi() {
        Api_KyGame.ins().getUserBalance(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                if (code == 0) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshKyBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    hideLoadingDialog();
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    public void doGetFwBalanceApi() {
        Api_FwGame.ins().getUserBalance(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshFwBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    hideLoadingDialog();
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 查询Bg游戏玩家总分
     */
    public void doGetAgBalanceApi() {
        Api_AgGame.ins().getUserBalance(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                hideLoadingDialog();
                if (code == 0) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshAgBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    hideLoadingDialog();
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 查询TY游戏玩家总分
     */
    public void doGetTYBalanceApi() {
        Api_TYGame.ins().getUserBalance(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                hideLoadingDialog();
                if (code == 0) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshTYBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    hideLoadingDialog();
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 设置自动额度转换
     */
    public void doAutoUpdownBalanceApi() {
        Api_User.ins().autoUpdownBalance(user.getAutoUpdownBalance() == 1 ? 2 : 1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                hideLoadingDialog();
                if (code == 0) {
                    user.setAutoUpdownBalance(user.getAutoUpdownBalance() == 1 ? 2 : 1);
                    SPManager.saveUserInfo(user);
                    refreshAutoChangeView();
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 一键回收所有金币
     */
    public void doBackAllGameCoinApi() {
        Api_User.ins().backAllGameCoin(user.getUid(), new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    if (!StringUtils.isEmpty(msg)) {
                        showToastTip(true, getString(R.string.tab_change_success));
                    }
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("kyBalance")) {
                            long kyMoney = message.getLong("kyBalance");
                            refreshKyBalance(kyMoney);
                        }
                        if (message.has("agBalance")) {
                            long agMoney = message.getLong("agBalance");
                            refreshAgBalance(agMoney);
                        }
                        if (message.has("cmdBalance")) {//?
                            long agMoney = message.getLong("cmdBalance");
                            refreshTYBalance(agMoney);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * Ky上分
     */
    public void doKyBalanceUpApi(long money) {
        Api_KyGame.ins().balanceUp(money, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                hideLoadingDialog();
                if (code == 0) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshKyBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 转入注册
     */
    public void doKyBalanceDownApi(long money) {
        showLoadingDialogWithNoBgBlack();
        Api_KyGame.ins().transferOut(gameType, money, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshBalance(money, gameType);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }


    /**
     * Ag上分
     */
    public void doAgBalanceUpApi(long money) {
        Api_AgGame.ins().balanceUp(money, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshAgBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }


    /**
     * Ag下分
     */
    public void doAgBalanceDownApi(long money) {
        Api_AgGame.ins().balanceDown(money, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshAgBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 体育上分
     */
    public void doTYBalanceUpApi(long money) {
        Api_TYGame.ins().balanceUp(money, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshTYBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }


    /**
     * 体育下分
     */
    public void doTYBalanceDownApi(long money) {
        Api_TYGame.ins().balanceDown(money, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshTYBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * Ky上分
     */
    public void doFwBalanceUpApi(long money) {
        Api_FwGame.ins().balanceUp(money, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshFwBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }


    /**
     * Ky下分
     */
    public void doFwBalanceDownApi(long money) {
        Api_FwGame.ins().balanceDown(money, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0 && !TextUtils.isEmpty(data)) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshFwBalance(money);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }


    /**
     * 查询游戏玩家总分
     */
    public void requestBalanceApi(int gameType) {
        Api_KyGame.ins().requestBalance(gameType, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0) {
                    try {
                        JSONObject message = new JSONObject(data);
                        if (message.has("money")) {
                            long money = message.getLong("money");
                            refreshBalance(money, gameType);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                } else {
                    hideLoadingDialog();
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 刷新余额
     *
     * @param balance 余额
     */
    private void refreshBalance(long balance, int gameType) {
        switch (gameType) {
            case 1:
                mKyBalance = balance;
                tvKymoney.setText(String.valueOf(balance));
                break;

            case 7:
                sabaBalance = balance;
                sabaMoney.setText(String.valueOf(balance));
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppIMManager.ins().removeMessageReceivedListener(MyBalanceActivity.class);
    }
}
