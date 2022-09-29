package com.live.fox.dialog;

import android.content.Intent;
import android.view.View;

import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.DialogGobindPhoneOnwithdrawBinding;
import com.live.fox.ui.mine.MineFragment;
import com.live.fox.ui.mine.setting.PhoneBindingActivity;
import com.live.fox.ui.mine.withdraw.WithdrawalActivity;

public class DialogGoBindPhoneOnWithdrawal extends BaseBindingDialogFragment {

    DialogGobindPhoneOnwithdrawBinding mBind;

    BaseBindingFragment mine;

    public static DialogGoBindPhoneOnWithdrawal getInstance()
    {
        return new DialogGoBindPhoneOnWithdrawal();
    }

    public static DialogGoBindPhoneOnWithdrawal getInstance(BaseBindingFragment mine)
    {
        DialogGoBindPhoneOnWithdrawal dialogGoBindPhoneOnWithdrawal=new DialogGoBindPhoneOnWithdrawal();
        dialogGoBindPhoneOnWithdrawal.mine=mine;
        return dialogGoBindPhoneOnWithdrawal;
    }

    @Override
    public void onClickView(View view) {
        dismissAllowingStateLoss();
        switch (view.getId())
        {
            case R.id.gtCancel:
                getActivity().startActivity(new Intent(getContext(), WithdrawalActivity.class));
                break;
            case R.id.gtCommit:
                if(mine!=null && (mine instanceof MineFragment))
                {
                    mine.startActivityForResult(new Intent(getContext(), PhoneBindingActivity.class), ConstantValue.REQUEST_CODE1);
                }
                else
                {
                    getActivity().startActivity(new Intent(getContext(), PhoneBindingActivity.class));
                }

                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_gobind_phone_onwithdraw;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setDialogGoBind(this);


    }
}
