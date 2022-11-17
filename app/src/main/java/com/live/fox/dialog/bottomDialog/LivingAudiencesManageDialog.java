package com.live.fox.dialog.bottomDialog;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogLivingFansManageBinding;

public class LivingAudiencesManageDialog extends BaseBindingDialogFragment {

    DialogLivingFansManageBinding mBind;

    public static LivingAudiencesManageDialog getInstance()
    {
        return new LivingAudiencesManageDialog();
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.llContent,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
            case R.id.gtvCancel:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.llContent,false);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_living_fans_manage;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        startAnimate(mBind.llContent,true);
    }
}
