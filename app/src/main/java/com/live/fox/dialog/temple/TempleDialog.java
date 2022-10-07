package com.live.fox.dialog.temple;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogTempleBinding;

public class TempleDialog extends BaseBindingDialogFragment {

    DialogTempleBinding mBind;
    OnCreateDialogListener onCreateDialogListener;

    public static TempleDialog getInstance()
    {
        return new TempleDialog();
    }

    public void setOnCreateDialogListener(OnCreateDialogListener onCreateDialogListener) {
        this.onCreateDialogListener = onCreateDialogListener;
    }

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

        mBind.rlMain.setVisibility(View.INVISIBLE);
        if(onCreateDialogListener!=null)
        {
            onCreateDialogListener.onCreate(this);
        }
        mBind.rlMain.setVisibility(View.VISIBLE);
    }

    public DialogTempleBinding getBind() {
        return mBind;
    }


    public interface OnCreateDialogListener
    {
        void onCreate(TempleDialog dialog);
    }
}
