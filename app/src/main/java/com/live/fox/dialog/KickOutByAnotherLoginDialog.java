package com.live.fox.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogKickoutByAnotherLoginBinding;

/**
 * 您的账号已在其他手机登陆
 */
public class KickOutByAnotherLoginDialog extends BaseBindingDialogFragment {

    DialogKickoutByAnotherLoginBinding mBind;

    public static KickOutByAnotherLoginDialog getInstance()
    {
        KickOutByAnotherLoginDialog dialog=new KickOutByAnotherLoginDialog();
        return dialog;
    }
    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.tvConfirm:
                startAnimate(mBind.rlMain,false);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_kickout_by_another_login;
    }

    @Override
    public void initView(View rootView) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        startAnimate(mBind.rlMain,true);
    }

}
