package com.live.fox.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class AgencyGameDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public AgencyGameDetailAdapter(List data) {
        super(R.layout.item_agency_game_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {

        int pos = helper.getLayoutPosition();
        if (pos % 2 == 0) {
            helper.getView(R.id.mView).setBackgroundColor(mContext.getResources().getColor(R.color.colorFAFAFA));
        } else {
            helper.getView(R.id.mView).setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

    }

}
