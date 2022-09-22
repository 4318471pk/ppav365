package com.live.fox.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.GestureLockView.GestureLockLayout;

public class APPScreenLockActivity extends BaseActivity {

    TextView tvHint;
    GestureLockLayout gesView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appscreen_lock);

        tvHint=findViewById(R.id.tvHint);
        gesView=findViewById(R.id.gesView);
        verifyPassword(SPManager.getGesturePassword());
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
                    setResult(ConstantValue.REQUEST_CODE2);
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
}
