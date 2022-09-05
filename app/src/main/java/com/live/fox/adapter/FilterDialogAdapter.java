package com.live.fox.adapter;

import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.FilterItemEntity;


public class FilterDialogAdapter extends BaseQuickAdapter<FilterItemEntity, BaseViewHolder> {

    public FilterDialogAdapter() {
        super(R.layout.item_transaction_dialog);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilterItemEntity item) {
        CheckBox checkBox = helper.getView(R.id.item_transaction_dialog_filter);
        checkBox.setText(item.getName());
        checkBox.setChecked(item.isSelect());
    }
}
