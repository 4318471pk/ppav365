package com.live.fox.adapter;


import android.annotation.SuppressLint;
import android.icu.text.NumberFormat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.TransactionEntity;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.TimeUtils;

/**
 * 交易记录
 * 适配器
 */
public class TransactionAdapter extends BaseQuickAdapter<TransactionEntity.CenterUserAssetsPlusVOSDTO, BaseViewHolder> {

    public TransactionAdapter() {
        super(R.layout.item_transaction);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransactionEntity.CenterUserAssetsPlusVOSDTO item) {
        String timeStr = TimeUtils.long2StringHHMMSSDDMMYY(item.getGmtCreate());
        String[] split = timeStr.split(" ");
        timeStr = split[0] + "\n" + split[1];

        helper.setText(R.id.item_transaction_time, timeStr);
        helper.setText(R.id.item_transaction_lottery, getString(item.getName()));
        TextView type = helper.getView(R.id.item_transaction_status_result_type);
        type.setText(RegexUtils.formatNumber((long) item.getGoldCoin()));

        int color;

        if (item.getIsIncrease() == 0) {
            color = mContext.getResources().getColor(R.color.colorGreen);
        } else {
            color = mContext.getResources().getColor(R.color.colorRed);
        }
        type.setTextColor(color);
    }

    @SuppressLint("NewApi")
    private String getString(Object item) {
        String str = "";
        if (item == null) {
            return str;
        }

        if (item instanceof Double) {
            if ((Double) item > 0) {
                str = "+" + ((Double) item).intValue();
            } else {
                str = NumberFormat.getInstance().format(item);
            }
        } else {
            str = String.valueOf(item);
        }
        return str;
    }
}
