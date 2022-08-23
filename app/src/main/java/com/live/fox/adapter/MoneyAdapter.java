package com.live.fox.adapter;

import static com.live.fox.entity.RecharegPrice.VIEW_OFFICAL_RECHARGE;
import static com.live.fox.entity.RecharegPrice.VIEW_UNOFFICAL_RECHARGE;

import android.graphics.Color;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.AppConfig;
import com.live.fox.R;
import com.live.fox.entity.RecharegPrice;
import com.live.fox.utils.RegexUtils;

import java.util.List;

public class MoneyAdapter extends BaseMultiItemQuickAdapter<RecharegPrice, BaseViewHolder> {
    private int moneySelPos = 0;

    public MoneyAdapter(@Nullable List<RecharegPrice> data) {
        super(data);
        //设置当传入的itemType为某个常量显示不同的item
        addItemType(VIEW_UNOFFICAL_RECHARGE, R.layout.item_rechargemoney);
        addItemType(VIEW_OFFICAL_RECHARGE, R.layout.item_rechargemoney);

    }

    @Override
    protected void convert(BaseViewHolder helper, RecharegPrice item) {
        if (moneySelPos == helper.getLayoutPosition()) {
            helper.setImageResource(R.id.iv_bg, R.drawable.shape_gradual_orange_light_corners_10);
            helper.setTextColor(R.id.tv_money1, Color.WHITE);
            helper.setTextColor(R.id.tv_money2, Color.WHITE);
        } else {
            helper.setImageResource(R.id.iv_bg, R.drawable.shape_white_stroke_gray_corners_10);
            helper.setTextColor(R.id.tv_money1, Color.parseColor("#EB4A81"));
            helper.setTextColor(R.id.tv_money2, Color.BLACK);

        }
        helper.setText(R.id.tv_money1, RegexUtils.westMoney(item.getUserRmb() / 100) + AppConfig.getCurrencySymbol());
        helper.setText(R.id.tv_money2, RegexUtils.westMoney(item.getGoldCoin()));
    }

    public void setMoneySelPos(int moneySelPos) {
        this.moneySelPos = moneySelPos;
    }
}