package com.live.fox.dialog.bottomDialog;

import android.view.View;
import android.view.animation.Animation;

import androidx.fragment.app.FragmentManager;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.DialogAnchorLivingRoomSettingBinding;

public class AnchorLivingRoomSettingDialog extends BaseBindingDialogFragment {

    DialogAnchorLivingRoomSettingBinding mBind;

    public static AnchorLivingRoomSettingDialog getInstance()
    {
        return new AnchorLivingRoomSettingDialog();
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                startAnimate(mBind.llContent,false);
                break;
            case R.id.tvGift:
            case R.id.tvAudienceManage:
            case R.id.tvLivingSetting:
                onOPenAnotherDialog(view.getId());
                break;
        }
    }

    private void onOPenAnotherDialog(int id)
    {
        startAnimate(mBind.rlMain, false, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (id)
                {
                    case R.id.tvGift:
                        TreasureBoxDialog treasureBoxDialog=TreasureBoxDialog.getInstance();
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getParentFragmentManager(),treasureBoxDialog);
                        break;
                    case R.id.tvAudienceManage:
                        AudienceManagerDialog audienceManagerDialog=AudienceManagerDialog.getInstance();
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getParentFragmentManager(),audienceManagerDialog);
                        break;
                    case R.id.tvLivingSetting:

                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_anchor_living_room_setting;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        startAnimate(mBind.llContent,true);
    }


}
