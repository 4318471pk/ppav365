package com.live.fox.dialog.temple;

import android.view.View;

import com.live.fox.R;
import com.live.fox.databinding.DialogTempleRoundBtnsBinding;

public class BalanceInsufficientDialog extends TempleDialog2 {


    public static BalanceInsufficientDialog getInstance()
    {
        return new BalanceInsufficientDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gtCancel:
                break;
            case R.id.gtCommit:
                break;
        }
        dismissAllowingStateLoss();
    }

    @Override
    public void initView(View view) {
        mBind.tvContent.setText(getResources().getString(R.string.InsufficientBalance2));
    }


}

