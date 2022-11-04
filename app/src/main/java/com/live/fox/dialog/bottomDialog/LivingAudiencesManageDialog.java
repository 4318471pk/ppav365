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
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_living_fans_manage;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
    }
}
