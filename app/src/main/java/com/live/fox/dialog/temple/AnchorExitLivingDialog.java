package com.live.fox.dialog.temple;

import android.view.View;

import com.live.fox.R;
import com.live.fox.ui.openLiving.OpenLivingActivity;

public class AnchorExitLivingDialog extends TempleDialog2{



    public static AnchorExitLivingDialog getInstance()
    {
        AnchorExitLivingDialog dialog=new AnchorExitLivingDialog();
        return dialog;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.gtCancel:
                dismissAllowingStateLoss();
                break;
            case R.id.gtCommit:
                dismissAllowingStateLoss();
                if(getActivity() instanceof OpenLivingActivity)
                {
                    OpenLivingActivity openLivingActivity=(OpenLivingActivity)getActivity();
                    openLivingActivity.onAnchorExitLiving();
                }
                break;
        }

    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mBind.tvTitle.setText(getStringWithoutContext(R.string.dialog_words));
        mBind.gtCancel.setText(getStringWithoutContext(R.string.cancel));
        mBind.gtCommit.setText(getStringWithoutContext(R.string.confirm));
        mBind.tvContent.setText(getStringWithoutContext(R.string.offLivingContent));
    }

}
