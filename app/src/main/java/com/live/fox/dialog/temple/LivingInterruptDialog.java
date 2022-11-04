package com.live.fox.dialog.temple;

import android.view.View;

import com.live.fox.R;

public class LivingInterruptDialog extends TempleDialog2{

    OnClickButtonsListener onClickButtonsListener;

    public static LivingInterruptDialog getInstance(OnClickButtonsListener onClickButtonsListener)
    {
        LivingInterruptDialog dialog=new LivingInterruptDialog();
        dialog.onClickButtonsListener=onClickButtonsListener;
        return dialog;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.gtCancel:
                onClickButtonsListener.onClick(false,this);
                break;
            case R.id.gtCommit:
                onClickButtonsListener.onClick(true,this);
                break;
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mBind.gtCancel.setText(getString(R.string.cancel));
        mBind.gtCommit.setText(getString(R.string.confirm));
        mBind.tvTitle.setVisibility(View.GONE);
        mBind.tvContent.setText(getString(R.string.livingInterrupt));

    }

    public interface OnClickButtonsListener
    {
        void onClick(boolean isConfirm,LivingInterruptDialog dialog);
    }
}
