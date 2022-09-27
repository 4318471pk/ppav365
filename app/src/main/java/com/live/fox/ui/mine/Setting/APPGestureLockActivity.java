package com.live.fox.ui.mine.Setting;

import android.view.View;

import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityAppGesturelockBinding;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.screenlock.GestureLockLayout;

import java.util.List;

public class APPGestureLockActivity extends BaseBindingViewActivity {

    ActivityAppGesturelockBinding mBind;

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_app_gesturelock;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setActivityTitle(R.string.appLockSetting);

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
        mBind.tvHint.setText(getString(R.string.plzInputVerify));
        mBind.tvHint.setTextColor(0xff404040);
        int array[]=new int[password.length()];
        for (int i = 0; i <password.length() ; i++) {
            array[i]=password.charAt(i)-'0';
        }
        mBind.gesView.setAnswer(array);
        mBind.gesView.setMode(GestureLockLayout.VERIFY_MODE);
        mBind.gesView.setTryTimes(10);
        mBind.gesView.setOnLockVerifyListener(new GestureLockLayout.OnLockVerifyListener() {
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
                    mBind.tvHint.setText(getString(R.string.wrongPasswordTryAgain));
                    mBind.tvHint.setTextColor(getResources().getColor(R.color.red));
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
        mBind.tvHint.setText(getString(R.string.plzSetNewGesPassword));
        mBind.tvHint.setTextColor(0xff404040);
        mBind.gesView.setMode(GestureLockLayout.RESET_MODE);
        mBind.gesView.setOnLockResetListener(new GestureLockLayout.OnLockResetListener() {
            @Override
            public void onConnectCountUnmatched(int connectCount, int minCount) {
                mBind.tvHint.setText(getString(R.string.gesWrongConfirmPassword));
                mBind.tvHint.setTextColor(getResources().getColor(R.color.red));
                mBind.gesView.resetGesture();
            }

            @Override
            public void onFirstPasswordFinished(List<Integer> answerList) {
                mBind.tvHint.setText(getString(R.string.plzSetNewGesConfirmPassword));
                mBind.tvHint.setTextColor(0xff404040);
                mBind.gesView.resetPath(false);

            }

            @Override
            public void onSetPasswordFinished(boolean isMatched, List<Integer> answerList) {
                if(isMatched)
                {
                    mBind.tvHint.setText(getString(R.string.setGesPasswordSuccess));
                    mBind.tvHint.setTextColor(0xff404040);
                    StringBuilder sb=new StringBuilder();
                    for (int i = 0; i < answerList.size(); i++) {
                        sb.append(answerList.get(i));
                    }
                    SPManager.setGesturePassword(sb.toString());
                    mBind.gesView.resetGesture();
                    mBind.tvHint.postDelayed(new Runnable() {
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
                    mBind.tvHint.setText(getString(R.string.gesWrongConfirmPassword));
                    mBind.tvHint.setTextColor(getResources().getColor(R.color.red));
                    mBind.gesView.resetPath(true);
                }
            }
        });

    }

}
