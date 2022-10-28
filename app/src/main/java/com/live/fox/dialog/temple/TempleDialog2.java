package com.live.fox.dialog.temple;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogTempleRoundBtnsBinding;
import com.live.fox.utils.SpanUtils;

public class TempleDialog2 extends BaseBindingDialogFragment {
    DialogTempleRoundBtnsBinding mBind;


    @Override
    public void onClickView(View view) {
        onClick(view);
    }

    public void onClick(View view){

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_temple_round_btns;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

    }
}
