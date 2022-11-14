package com.live.fox.ui.lottery.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;

import java.util.List;

public class LotteryNameAdapter extends BaseQuickAdapter<Boolean, BaseViewHolder> {


    public LotteryNameAdapter(List data) {
        super(R.layout.item_lottery_name, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Boolean data) {

        if (data) {
            helper.getView(R.id.viewBott).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.viewBott).setVisibility(View.GONE);
        }

    }

}
