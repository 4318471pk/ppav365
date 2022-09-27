package com.live.fox.dialog;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.FragmentDialogLoadingBinding;

public class LoadingBindingDialogFragment extends BaseBindingDialogFragment {

    FragmentDialogLoadingBinding mBind;

    public static LoadingBindingDialogFragment getInstance()
    {
        return new LoadingBindingDialogFragment();
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_dialog_loading;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsKeyCanback=false;
        mIsOutCanback=false;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
    }


}
