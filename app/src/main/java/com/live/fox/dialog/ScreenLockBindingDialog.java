package com.live.fox.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogAppscreenLockBinding;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.screenlock.GestureLockLayout;

public class ScreenLockBindingDialog extends BaseBindingDialogFragment {

//    TextView tvHint;
//    GestureLockLayout gesView;
    DialogAppscreenLockBinding mBind;
    onScreenLockUnlockListener onScreenLockUnlockListener;

    public static ScreenLockBindingDialog getInstance()
    {
        return new ScreenLockBindingDialog();
    }

    public ScreenLockBindingDialog setOnScreenLockUnlockListener(ScreenLockBindingDialog.onScreenLockUnlockListener onScreenLockUnlockListener) {
        this.onScreenLockUnlockListener = onScreenLockUnlockListener;
        return this;
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
                    if(onScreenLockUnlockListener!=null)
                    {
                        onScreenLockUnlockListener.onScreenLockUnlock();
                        mBind.gesView.resetGesture();
                    }
                }
                else
                {
                    mBind.tvHint.setText(getString(R.string.wrongPasswordTryAgain));
                    mBind.tvHint.setTextColor(getResources().getColor(R.color.red));
                }
            }

            @Override
            public void onGestureTryTimesBoundary() {
                getActivity().finish();
                ToastUtils.showShort(getString(R.string.tryTimesLimit));
            }
        });
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_appscreen_lock;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsOutCanback=false;
        mIsKeyCanback=false;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        verifyPassword(SPManager.getGesturePassword());
    }

    public interface onScreenLockUnlockListener
    {
        void onScreenLockUnlock();
    }
}
