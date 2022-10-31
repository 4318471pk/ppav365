package com.live.fox.dialog.bottomDialog;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogAddNewbankCardBinding;
import com.live.fox.entity.UserBankBean;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;

public class AddNewBankCardDialog extends BaseBindingDialogFragment {

    DialogAddNewbankCardBinding mBind;

    BankListDialog bankListDialog;

    String bankValue;

    public static AddNewBankCardDialog getInstance()
    {
        return new AddNewBankCardDialog();
    }
    @Override
    public void onClickView(View view) {
        if(view.getId()==mBind.rlBG.getId()) {
            dismissAllowingStateLoss();
        } else if (view.getId() == mBind.gtCommit.getId()) {
            if (TextUtils.isEmpty(mBind.etCreateBankCardName.getText().toString().trim())) {
                ToastUtils.showShort(getString(R.string.please_input_pwd));
                return;
            }
            if (TextUtils.isEmpty(mBind.etCreateBankCardAccount.getText().toString().trim())) {
                ToastUtils.showShort(getString(R.string.please_input_bank));
                return;
            }
            if (TextUtils.isEmpty(bankValue)) {
                ToastUtils.showShort(getString(R.string.please_choice_bank_name));
                return;
            }
            if (TextUtils.isEmpty(mBind.etBankzhi.getText().toString().trim())) {
                ToastUtils.showShort(getString(R.string.please_input_bank_zhi));
                return;
            }
            addBankCard();
        } else if (view.getId() == mBind.etBankName.getId()){
            DialogFramentManager.getInstance().showDialog(this.getActivity().getSupportFragmentManager(),
                    bankListDialog);
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
        bankListDialog = BankListDialog.getInstance();
        bankListDialog.getBankList(false);
        bankListDialog.setAddNewBankCardSuc(new BankListDialog.BankListChoice() {
            @Override
            public void choice(String value, String name) {
                mBind.etBankName.setText(name);
                bankValue = value;
            }
        });

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

    private void addBankCard(){
        showLoadingDialog();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        commonParams.put("trueName",mBind.etCreateBankCardName.getText().toString().trim());
        commonParams.put("cardNo",mBind.etCreateBankCardAccount.getText().toString().trim());
        commonParams.put("bankSub",bankValue);//支行名称
        commonParams.put("bankCode", mBind.etBankzhi.getText().toString().trim()); //银行编码
        Api_Order.ins().getUserAddBank(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                dismissLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    if (addNewBankCardSuc != null) {
                        addNewBankCardSuc.addSuc();
                    }
                    dismissAllowingStateLoss();
                    ToastUtils.showShort(getString(R.string.add_suc));
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);


    }

    public interface AddNewBankCardSuc {
        public void addSuc();
    }

    private AddNewBankCardSuc addNewBankCardSuc = null;

    public void setAddNewBankCardSuc(AddNewBankCardSuc addNewBankCardSuc) {
        this.addNewBankCardSuc = addNewBankCardSuc;
    }

}
