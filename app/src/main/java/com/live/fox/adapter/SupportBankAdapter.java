package com.live.fox.adapter;


import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.SupportBankEntity;

/**
 * 支持的银行
 */
public class SupportBankAdapter extends BaseQuickAdapter<SupportBankEntity, BaseViewHolder> {

    public SupportBankAdapter() {
        super(R.layout.item_check_box);
    }

    @Override
    protected void convert(BaseViewHolder helper, SupportBankEntity item) {
        CheckBox checkBox = helper.getView(R.id.recharge_support_bank);
        checkBox.setChecked(item.isCheck());
        checkBox.setText(item.getBank());
    }
}
