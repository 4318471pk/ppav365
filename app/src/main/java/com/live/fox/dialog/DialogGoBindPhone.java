package com.live.fox.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogGobindPhoneBinding;
import com.live.fox.ui.mine.setting.PhoneBindingActivity;

public class DialogGoBindPhone extends BaseBindingDialogFragment {

    DialogGobindPhoneBinding mBind;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsKeyCanback=false;
        mIsOutCanback=false;
    }

    public static DialogGoBindPhone getInstance()
    {
        return new DialogGoBindPhone();
    }

    @Override
    public void onClickView(View view) {
        dismissAllowingStateLoss();
        switch (view.getId())
        {
            case R.id.gtCancel:
                getActivity().finish();
                break;
            case R.id.gtCommit:
                getActivity().startActivity(new Intent(getContext(), PhoneBindingActivity.class));
                getActivity().finish();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_gobind_phone;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setDialogGoBind(this);
    }
}
