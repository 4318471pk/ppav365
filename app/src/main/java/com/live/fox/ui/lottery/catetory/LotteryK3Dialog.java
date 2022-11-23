package com.live.fox.ui.lottery.catetory;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogLotteryK3Binding;

public class LotteryK3Dialog extends BaseBindingDialogFragment {

    DialogLotteryK3Binding mBind;
    int historySelectedStatus=0;//当前选中的是投注记录还是开奖记录

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.tvBetHistory:

                break;
            case R.id.tvLotteryResultHistory:

                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_lottery_k3;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);


    }
}
