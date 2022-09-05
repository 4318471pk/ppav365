package com.live.fox.ui.mine.activity;

import static com.live.fox.entity.TransactionEntity.CenterUserAssetsPlusVOSDTO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.ui.mine.activity.kefu.ServicesActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.TimeUtils;

/**
 * 交易记录详情
 * 交易记录的详情页面
 */
public class TransactionDetailActivity extends BaseActivity {

    private static final String TRANSACTION_DETAIL_KEY = "Transaction Detail Key";
    private CenterUserAssetsPlusVOSDTO assetsPlus;

    public static void launch(Context context, CenterUserAssetsPlusVOSDTO assetsPlus) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, TransactionDetailActivity.class);
        intent.putExtra(TRANSACTION_DETAIL_KEY, assetsPlus);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarLightMode(this, false);
        setContentView(R.layout.activity_transaction_detail);

        TextView title = findViewById(R.id.common_title_title);
        title.setText(getString(R.string.transaction_title));
        findViewById(R.id.common_title_back).setOnClickListener(view -> finish());

        if (getIntent() != null) {
            assetsPlus = getIntent().getParcelableExtra(TRANSACTION_DETAIL_KEY);
            if (assetsPlus != null) {
                setData();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void setData() {
        TextView money = findViewById(R.id.transaction_detail_money);
        TextView name = findViewById(R.id.transaction_detail_name);
        TextView type = findViewById(R.id.transaction_detail_type_status);
        TextView order = findViewById(R.id.transaction_order_name);
        TextView time = findViewById(R.id.transaction_detail_order_time);

        TextView typeTitle = findViewById(R.id.transaction_detail_type_title);

        Group group = findViewById(R.id.transaction_detail_bet);

        TextView betMoney = findViewById(R.id.transaction_detail_bet_result);

        if (!TextUtils.isEmpty(assetsPlus.getExpect())) {
            type.setText(assetsPlus.getExpect());  //订单号
            typeTitle.setText(getString(R.string.phase_number));
            group.setVisibility(View.VISIBLE);
            String moneyStr = RegexUtils.westMoney(assetsPlus.getBetAmoney());
            betMoney.setText(moneyStr);
        }

        View copy = findViewById(R.id.transaction_order_copy);

        String string = getString(assetsPlus.getTrn());
        if (TextUtils.isEmpty(string)) {
            copy.setVisibility(View.GONE);
        }

        copy.setOnClickListener(view -> {
            copyToClipboard(String.valueOf(assetsPlus.getTrn()));
        });

        money.setText(RegexUtils.westMoney(assetsPlus.getGoldCoin()));
        name.setText(getString(assetsPlus.getName()));
        order.setText(getString(assetsPlus.getTrn()));
        time.setText(TimeUtils.long2String(assetsPlus.getGmtCreate()));

        findViewById(R.id.transaction_detail_question).setOnClickListener(view -> {
            ServicesActivity.startActivity(this);
        });
    }

    @SuppressLint("NewApi")
    private String getString(Object str) {
        String temp = "";
        if (str == null) {
            return temp;
        }

        if (str instanceof Double) {
            if ((Double) str > 0) {
                return "+" + ((Double) str).intValue();
            } else {
                return NumberFormat.getInstance().format(str);
            }
        }
        return String.valueOf(str);
    }
}