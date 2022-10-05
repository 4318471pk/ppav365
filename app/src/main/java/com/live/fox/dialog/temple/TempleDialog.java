package com.live.fox.dialog.temple;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogTempleBinding;

public class TempleDialog extends BaseBindingDialogFragment {

    DialogTempleBinding mBind;

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_temple;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);
    }

    public DialogTempleBinding getBind() {
        return mBind;
    }

}
