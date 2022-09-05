package com.live.fox.ui.mine.activity.moneyout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.ExchangeMoneyFragment;
import com.live.fox.entity.User;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Promotion;
import com.live.fox.server.Api_User;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.view.EditTextWithDel;


/**
 * 兑换金币
 */
public class ExChangeMoneyActivity extends BaseHeadActivity {

    private EditTextWithDel etMoney;
    private TextView tvMymoney;

    User myUserInfo;

    int pageType = 4; //4.魅力值兑换金币  3.分享金币兑换金币
    long moneyCount = 0;

    public static void startActivity(Context context, int pageType) {
        Constant.isAppInsideClick=true;
        Intent i = new Intent(context, ExChangeMoneyActivity.class);
        i.putExtra("pageType", pageType);
        context.startActivity(i);
    }

    public static void startActivity(Context context, int pageType, long moneyCount) {
        Constant.isAppInsideClick=true;
        Intent i = new Intent(context, ExChangeMoneyActivity.class);
        i.putExtra("pageType", pageType);
        i.putExtra("moneyCount", moneyCount);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchangemoney_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        pageType = getIntent().getIntExtra("pageType", 1);
        moneyCount = getIntent().getLongExtra("moneyCount", 0);

        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.proceedsForGold), true, true);

        setTitle(getString(R.string.proceedsForGold));
        etMoney = findViewById(R.id.et_money);
        tvMymoney = findViewById(R.id.tv_mymoney);
         findViewById(R.id.tv_allout).setOnClickListener(this);
        findViewById(R.id.btn_).setOnClickListener(this);
    }

    public void refreshPage() {
        myUserInfo = AppUserManger.getUserInfo();
        if(pageType==4){
            tvMymoney.setText(getString(R.string.currentProfit)+ RegexUtils.westMoney( myUserInfo.getAnchorCoin()));
        }else if(pageType==3){
            tvMymoney.setText(getString(R.string.currentProfit) + RegexUtils.westMoney( moneyCount));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPage();
    }

    long coin;

    @Override
    public void onClick(View view) {
        if(ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_allout:
                long tempCoin = 0;
                if(pageType==4){
                    tempCoin = (long) myUserInfo.getAnchorCoin();
                }else if(pageType==3){
                    tempCoin = moneyCount;
                }
                if(tempCoin>=0) {
                    etMoney.setText(tempCoin + "");
                    Editable etext = etMoney.getText();
                    Selection.setSelection(etext, etext.length());
                }
                break;
            case R.id.btn_:
                String coinStr = etMoney.getText().toString().trim();
                if(StringUtils.isEmpty(coinStr)) return;
                coin = Long.parseLong(coinStr);
                if(coin<=0) return;
                if(pageType==4) {
                    if (coin > myUserInfo.getAnchorCoin()) {
                        showToastTip(false, getString(R.string.biggerdui));
                        return;
                    }
                }else if(pageType==3){
                    if (coin > moneyCount) {
                        showToastTip(false, getString(R.string.biggerdui));
                        return;
                    }
                }

                KeyboardUtils.hideSoftInput(etMoney);
                ExchangeMoneyFragment exchangeMoneyFragment = ExchangeMoneyFragment.newInstance(pageType, coin);
                exchangeMoneyFragment.show(getSupportFragmentManager(), "");
                exchangeMoneyFragment.setBtnSureClick(() -> {
                    if(pageType==4) {
                        doExChangeCoinApi(coin);
                    }else {
                        doExChangeCoinByShareApi(coin);
                    }
                });
                break;
        }
    }

    /**
     * 魅力值兑换金币
     */
    public void doExChangeCoinApi(long coin) {
        showLoadingDialog();
        Api_User.ins().changeCoin(coin, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                hideLoadingDialog();
                if (code == 0) {
                    myUserInfo.setAnchorCoin(myUserInfo.getAnchorCoin()-coin);
                    SPManager.saveUserInfo(myUserInfo);
                    showToastTip(true, getString(R.string.duiSuccess));
                    etMoney.setText("");
                    if(myUserInfo.getAnchorCoin()>0) {
                        refreshPage();
                    }else {
                        finish();
                    }
                } else {
                    showToastTip(false, msg);
                }
            }
        });
    }

    /**
     * 分享收益兑换金币
     */
    public void doExChangeCoinByShareApi(long coin) {
        showLoadingDialog();
        Api_Promotion.ins().exchange(coin, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                hideLoadingDialog();
                if (code == 0) {
                    moneyCount = moneyCount-coin;
                    showToastTip(true, getString(R.string.goldDuiSuccess));
                    etMoney.setText("");
                    if(moneyCount>0) {
                        refreshPage();
                    }else {
                        finish();
                    }
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
        if (v != null && (v instanceof EditText)) {
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
}
