package com.live.fox.ui.mine.depositAndWithdrawHistory;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.ui.mine.diamondIncomeAndExpenses.DiamondIncomeAndExpensesActivity;

public class DepositAndWithdrawHistoryActivity extends BaseBindingViewActivity {

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context, DepositAndWithdrawHistoryActivity.class));
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_deposit_withdraw_history;
    }

    @Override
    public void initView() {
    }
}
