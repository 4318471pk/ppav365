package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.AppConfig;
import com.live.fox.R;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.RegexUtils;


/**
 * 兑换金币 提现确认框
 */
public class ExchangeMoneyFragment extends DialogFragment implements View.OnClickListener {

    TextView tvTitle;
    TextView tvMoney;

    int pageType; //1兑换金币  2提现  3.分享收益兑换
    long money;

    boolean isShow = false;
    private Dialog dialog;

    public static ExchangeMoneyFragment newInstance(int pageType, long money) {
        ExchangeMoneyFragment fragment = new ExchangeMoneyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pageType", pageType);
        bundle.putLong("money", money);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageType = bundle.getInt("pageType", 1);
            money = bundle.getLong("money", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.getAttributes().windowAnimations = R.style.DialogAnimation;
        }

        return inflater.inflate(R.layout.dialog_exchangemoney, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isShow = true;
        initView(view);
        refreshPage();
    }

    public void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tvMoney = view.findViewById(R.id.tv_money);

        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_sure).setOnClickListener(this);
    }

    public void refreshPage() {
        String moneyStr = AppConfig.getCurrencySymbol() + RegexUtils.westMoney(money);
        if (pageType == 1) {
            //1.金币提现
            tvTitle.setText(getString(R.string.exchangeProfit));
            tvMoney.setText(moneyStr);
        } else if (pageType == 2) {
            //2.收益提现
            tvTitle.setText(getString(R.string.exchangeProfit));
            tvMoney.setText(moneyStr);
        } else if (pageType == 4) {
            //4.兑换金币
            tvTitle.setText(getString(R.string.exchangeGold));
            tvMoney.setText(moneyStr);
        } else {
            //3.分享收益兑换
            tvTitle.setText(getString(R.string.exchangeMoney));
            tvMoney.setText(moneyStr);
        }
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sure:
                dismiss();
                if (btnSureClick != null) {
                    btnSureClick.onClick();
                }
                break;
        }
    }


    @Override
    public void dismiss() {
        isShow = false;
        super.dismiss();
    }

    OnBtnSureClick btnSureClick;

    public void setBtnSureClick(OnBtnSureClick btnClick) {
        this.btnSureClick = btnClick;
    }

    public interface OnBtnSureClick {
        void onClick();
    }

}
