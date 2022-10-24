package com.live.fox.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class AgencyMoneyRecordAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public AgencyMoneyRecordAdapter(List data) {
        super(R.layout.item_agency_money_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {


    }


}
