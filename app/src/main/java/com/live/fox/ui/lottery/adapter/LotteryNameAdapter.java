package com.live.fox.ui.lottery.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.LivingLotteryListBean;

import java.util.List;

public class LotteryNameAdapter extends BaseQuickAdapter<LivingLotteryListBean.ItemsBean, BaseViewHolder> {


    public LotteryNameAdapter(List data) {
        super(R.layout.item_lottery_name, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LivingLotteryListBean.ItemsBean data) {

        TextView tv = helper.getView(R.id.name);
        tv.setText(data.getName());
        if (data.isSelect()) {
            helper.getView(R.id.viewBott).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.viewBott).setVisibility(View.GONE);
        }

    }

}
