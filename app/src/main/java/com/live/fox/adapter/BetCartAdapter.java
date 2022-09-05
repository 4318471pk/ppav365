package com.live.fox.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.response.MinuteTabItem;
import com.live.fox.utils.RegexUtils;

import java.util.List;

/**
 * 下注列表
 */
public class BetCartAdapter extends BaseQuickAdapter<MinuteTabItem, BaseViewHolder> {
    public BetCartAdapter(@Nullable List<MinuteTabItem> data) {
        super(R.layout.adapter_minute_cart, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MinuteTabItem item) {
        helper.setText(R.id.tvBigOrSmall, item.getTitle());
        helper.setText(R.id.tvBetMoney, RegexUtils.westMoney(Double.parseDouble(item.betMoney)) + "X" + item.mutiple);
        helper.setText(R.id.tvRatio, 1 + mContext.getString(R.string.stakes) + item.mutiple);
        helper.setText(R.id.tvBetName, item.type_text_show);
        helper.addOnClickListener(R.id.ivTrash);
        helper.addOnClickListener(R.id.rrl);
    }

}
