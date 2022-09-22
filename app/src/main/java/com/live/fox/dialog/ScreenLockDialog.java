package com.live.fox.dialog;

import android.view.View;
import android.widget.TextView;

import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseDialogFragment;
import com.live.fox.manager.SPManager;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.GestureLockView.GestureLockLayout;

public class ScreenLockDialog extends BaseDialogFragment {

    TextView tvHint;
    GestureLockLayout gesView;
    onScreenLockUnlockListener onScreenLockUnlockListener;

    public static ScreenLockDialog getInstance()
    {
        return new ScreenLockDialog();
    }

    public ScreenLockDialog setOnScreenLockUnlockListener(ScreenLockDialog.onScreenLockUnlockListener onScreenLockUnlockListener) {
        this.onScreenLockUnlockListener = onScreenLockUnlockListener;
        return this;
    }

    @Override
    protected int getViewId() {
        return R.layout.dialog_appscreen_lock;
    }

    @Override
    protected void onCreateView(View view) {

    }

    @Override
    protected void initViews(View view) {
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
                    if(onScreenLockUnlockListener!=null)
                    {
                        onScreenLockUnlockListener.onScreenLockUnlock();
                    }
                }
                else
                {
                    tvHint.setText(getString(R.string.wrongPasswordTryAgain));
                    tvHint.setTextColor(getResources().getColor(R.color.red));
                }
            }

            @Override
            public void onGestureTryTimesBoundary() {
                getActivity().finish();
                ToastUtils.showShort(getString(R.string.tryTimesLimit));
            }
        });
    }

    public interface onScreenLockUnlockListener
    {
        void onScreenLockUnlock();
    }
}
