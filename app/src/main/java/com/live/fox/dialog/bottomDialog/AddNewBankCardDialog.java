package com.live.fox.dialog.bottomDialog;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogAddNewbankCardBinding;

public class AddNewBankCardDialog extends BaseBindingDialogFragment {

    DialogAddNewbankCardBinding mBind;

    public static AddNewBankCardDialog getInstance()
    {
        return new AddNewBankCardDialog();
    }
    @Override
    public void onClickView(View view) {
        if(view.getId()==mBind.rlBG.getId())
        {
            dismissAllowingStateLoss();
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_add_newbank_card;
    }

    @Override
    public void initView(View view) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mBind=getViewDataBinding();
        mBind.setClick(this);

        Animation animation= new TranslateAnimation(Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0
                ,Animation.RELATIVE_TO_PARENT,1f
                ,Animation.RELATIVE_TO_PARENT,0f);
        animation.setDuration(300);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBind.rlBG.setBackgroundColor(0x88000000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBind.llMain.startAnimation(animation);
    }
}
