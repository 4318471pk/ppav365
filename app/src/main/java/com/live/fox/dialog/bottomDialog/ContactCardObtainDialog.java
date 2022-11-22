package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogContactCardObtainBinding;

import org.jetbrains.annotations.NotNull;

public class ContactCardObtainDialog extends BaseBindingDialogFragment {

    DialogContactCardObtainBinding mBind;
    OnContactCardListener onContactCardListener;

    public static ContactCardObtainDialog getInstance()
    {
        return new ContactCardObtainDialog();
    }

    public void setOnContactCardListener(OnContactCardListener onContactCardListener) {
        this.onContactCardListener = onContactCardListener;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

//        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setWindowsFlag();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.rllContent,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                if(onContactCardListener!=null)
                {
                    int type=-1;
                    boolean isAvailable=false;
                    String account=mBind.etAccount.getText().toString();
                    int diamondAmount=Integer.valueOf(mBind.etAmountDiamond.getText().toString());
                    for (int i = 0; i < mBind.rgType.getChildCount(); i++) {
                       RadioButton radioButton=(RadioButton) mBind.rgType.getChildAt(i);
                       if(radioButton.isChecked())
                       {
                           type=(int)radioButton.getTag();
                       }
                    }

                    if(type>-1 && diamondAmount>0 && !TextUtils.isEmpty(account))
                    {
                        isAvailable=true;
                    }

                    onContactCardListener.onContactCard(isAvailable,account,diamondAmount,type);
                }
                startAnimate(mBind.rllContent,false);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_contact_card_obtain;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        for (int i = 0; i <mBind.rgType.getChildCount() ; i++) {
            mBind.rgType.getChildAt(i).setTag(i);
        }
        RadioButton radioButton=(RadioButton) mBind.rgType.getChildAt(0);
        radioButton.setChecked(true);
        startAnimate(mBind.rllContent,true);
    }

    public interface OnContactCardListener
    {
        void onContactCard(boolean isAvailable,String account,int diamondAmount,int type);
    }

}
