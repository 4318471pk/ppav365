package com.live.fox.dialog.bottomDialog;

import android.view.View;
import android.view.animation.Animation;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.DialogAnchorLivingSettingBinding;
import com.live.fox.dialog.temple.AnchorExitLivingDialog;
import com.live.fox.dialog.temple.TempleDialog2;

public class AnchorLivingSettingDialog extends BaseBindingDialogFragment {

    DialogAnchorLivingSettingBinding mBind;
    String liveId;

    public static AnchorLivingSettingDialog getInstance(String liveId)
    {
        AnchorLivingSettingDialog anchorLivingSettingDialog= new AnchorLivingSettingDialog();
        anchorLivingSettingDialog.liveId=liveId;
        return anchorLivingSettingDialog;
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.llContent,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.llContent,false);
                break;
            case R.id.gtvOffLiving:
                exitLivingDialog();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_anchor_living_setting;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);


        startAnimate(mBind.llContent,true);
    }

    private void exitLivingDialog()
    {
        startAnimate(mBind.llContent, false, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnchorExitLivingDialog anchorExitLivingDialog=AnchorExitLivingDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getFragmentManager(),anchorExitLivingDialog);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
