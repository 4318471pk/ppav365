package com.live.fox.adapter;

import android.app.Activity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.ui.agency.AgencyGameDetailActivity;

import java.util.List;

public class AgencyGameRecordAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public AgencyGameRecordAdapter(List data) {
        super(R.layout.item_agency_game_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgencyGameDetailActivity.startActivity((Activity)mContext);
            }
        });

    }


}
