package com.live.fox.ui.mine.activity.Setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.GestureLockView.GestureLockDisplayView;
import com.live.fox.view.GestureLockView.GestureLockLayout;

import java.util.List;

public class APPGestureLockActivity extends BaseHeadActivity {

    GestureLockLayout gesView;
    TextView tvHint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_gesturelock);
        setHead(getString(R.string.appLockSetting),true,false);

        gesView=findViewById(R.id.gesView);
        tvHint=findViewById(R.id.tvHint);
        setResult(ConstantValue.RESULT_CODE3);

        boolean status= getIntent().getBooleanExtra(ConstantValue.SwitchStatus,false);
        if(status)
        {
            String password= SPManager.getGesturePassword();
            if(StringUtils.isDigitOnly(password))
            {
                //验证旧密码
                verifyPassword(password);
            }
            else
            {
                //设置密码
                setPassword();
            }
        }
        else
        {
            //设置密码
            setPassword();
        }

    }

    private void verifyPassword(String password)
    {
        tvHint.setText(getString(R.string.plzInputVerify));
        tvHint.setTextColor(0xff404040);
        int array[]=new int[password.length()];
        for (int i = 0; i <password.length() ; i++) {
            array[i]=password.charAt(i)-'0';
        }
        gesView.setAnswer(array);
        gesView.setMode(GestureLockLayout.VERIFY_MODE);
        gesView.setTryTimes(10);
        gesView.setOnLockVerifyListener(new GestureLockLayout.OnLockVerifyListener() {
            @Override
            public void onGestureSelected(int id) {
            }

            @Override
            public void onGestureFinished(boolean isMatched) {
                if(isMatched)
                {
                    SPManager.setGesturePasswordStatus(false);
                    ToastUtils.showShort(getString(R.string.verifyPasswordSuccess));
                    setResult(ConstantValue.RESULT_CODE2);
                    finish();
                }
                else
                {
                    tvHint.setText(getString(R.string.wrongPasswordTryAgain));
                    tvHint.setTextColor(getResources().getColor(R.color.red));
                }
            }

            @Override
            public void onGestureTryTimesBoundary() {
                finish();
                ToastUtils.showShort(getString(R.string.tryTimesLimit));
            }
        });

    }

    private void setPassword()
    {
        tvHint.setText(getString(R.string.plzSetNewGesPassword));
        tvHint.setTextColor(0xff404040);
        gesView.setMode(GestureLockLayout.RESET_MODE);
        gesView.setOnLockResetListener(new GestureLockLayout.OnLockResetListener() {
            @Override
            public void onConnectCountUnmatched(int connectCount, int minCount) {
                tvHint.setText(getString(R.string.gesWrongConfirmPassword));
                tvHint.setTextColor(getResources().getColor(R.color.red));
                gesView.resetGesture();
            }

            @Override
            public void onFirstPasswordFinished(List<Integer> answerList) {
                tvHint.setText(getString(R.string.plzSetNewGesConfirmPassword));
                tvHint.setTextColor(0xff404040);
                gesView.resetPath(false);

            }

            @Override
            public void onSetPasswordFinished(boolean isMatched, List<Integer> answerList) {
                if(isMatched)
                {
                    tvHint.setText(getString(R.string.setGesPasswordSuccess));
                    tvHint.setTextColor(0xff404040);
                    StringBuilder sb=new StringBuilder();
                    for (int i = 0; i < answerList.size(); i++) {
                        sb.append(answerList.get(i));
                    }
                    SPManager.setGesturePassword(sb.toString());
                    gesView.resetGesture();
                    tvHint.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SPManager.setGesturePasswordStatus(true);
                            setResult(ConstantValue.RESULT_CODE1);
                            finish();
                        }
                    },1000);
                }
                else
                {
                    tvHint.setText(getString(R.string.gesWrongConfirmPassword));
                    tvHint.setTextColor(getResources().getColor(R.color.red));
                    gesView.resetPath(true);
                }
            }
        });

    }

}
