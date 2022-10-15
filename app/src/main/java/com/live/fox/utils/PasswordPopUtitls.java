package com.live.fox.utils;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.roundview.RoundTextView;
import com.live.fox.R;

public class PasswordPopUtitls {

    Activity activity;
    PopupWindow popupWindow;
    View rootView;

    RoundTextView tvPwd1;
    RoundTextView tvPwd2;
    RoundTextView tvPwd3;
    RoundTextView tvPwd4;
    RoundTextView tvPwd5;
    RoundTextView tvPwd6;
    EditText etPwd;

    private String pwd = "";


    public PasswordPopUtitls(Context context, View rootView){
        this.activity = (Activity)context;
        this.rootView = rootView;

        setPopCharge();
    }


    private void setPopCharge(){
        View popupView = activity.getLayoutInflater().inflate(R.layout.pop_pay_password,null);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);// 设置同意在外点击消失


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //在dismiss中恢复透明度
                setRootAlpha(1f);
            }
        });
        popupWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);

        popupView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupView.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(pwd)) {
                    if (pwd.length() <6){
                        ToastUtils.showShort(activity.getString(R.string.please_input_pwd));
                        //Toast.makeText(activity, ,Toast.LENGTH_SHORT).show();
                    } else {
                        if (payPwdConfirm != null) {
                            payPwdConfirm.clickConfirm(pwd);
                            popupWindow.dismiss();
                        }
                    }
                }
            }
        });

        tvPwd1 = popupView.findViewById(R.id.tv_pwd_1);
        tvPwd2 = popupView.findViewById(R.id.tv_pwd_2);
        tvPwd3 = popupView.findViewById(R.id.tv_pwd_3);
        tvPwd4 = popupView.findViewById(R.id.tv_pwd_4);
        tvPwd5 = popupView.findViewById(R.id.tv_pwd_5);
        tvPwd6 = popupView.findViewById(R.id.tv_pwd_6);

        etPwd = popupView.findViewById(R.id.et_pwd);
        etPwd.setCursorVisible(false);
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (TextUtils.isEmpty(s)) {
                    tvPwd1.setText("");
                    pwd = "";
                    return;
                }
                pwd = s.toString();
                if (s.length() > 6){
                    pwd = s.toString().substring(0,6);
                }
                if (pwd.length() == 0) {
                    tvPwd1.setText("");
                } else if (pwd.length() == 1) {
                    tvPwd1.setText("*");
                    tvPwd2.setText("");
                } else if (s.length() == 2) {
                    tvPwd1.setText("*");
                    tvPwd2.setText("*");
                    tvPwd3.setText("");
                } else if (s.length() == 3) {
                    tvPwd1.setText("*");
                    tvPwd2.setText("*");
                    tvPwd3.setText("*");
                    tvPwd4.setText("");
                } else if (s.length() == 4) {
                    tvPwd1.setText("*");
                    tvPwd2.setText("*");
                    tvPwd3.setText("*");
                    tvPwd4.setText("*");
                    tvPwd5.setText("");
                } else if (s.length() == 5) {
                    tvPwd1.setText("*");
                    tvPwd2.setText("*");
                    tvPwd3.setText("*");
                    tvPwd4.setText("*");
                    tvPwd5.setText("*");
                    tvPwd6.setText("");
                } else if (pwd.length() >= 6){
                    tvPwd1.setText("*");
                    tvPwd2.setText("*");
                    tvPwd3.setText("*");
                    tvPwd4.setText("*");
                    tvPwd5.setText("*");
                    tvPwd6.setText("*");
                }



            }
        });


    }

    public void show(){
        pwd = "";
        etPwd.setText("");
        tvPwd1.setText("");
        tvPwd2.setText("");
        tvPwd3.setText("");
        tvPwd4.setText("");
        tvPwd5.setText("");
        tvPwd6.setText("");
        popupWindow.showAtLocation(rootView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        setRootAlpha(0.35f);
    }

    private void setRootAlpha(float al){
        WindowManager.LayoutParams lp=activity.getWindow().getAttributes();
        lp.alpha= al;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }

    public interface PayPwdConfirm{
        public void clickConfirm(String pwd);
    }

    private PayPwdConfirm payPwdConfirm;

    public void setPayPwdConfirm(PayPwdConfirm payPwdConfirm) {
        this.payPwdConfirm = payPwdConfirm;
    }

}
