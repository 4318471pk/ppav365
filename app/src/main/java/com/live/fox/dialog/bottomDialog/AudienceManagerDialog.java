package com.live.fox.dialog.bottomDialog;

import android.view.View;
import android.view.animation.Animation;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.DialogAudienceManagerBinding;
import com.live.fox.dialog.bottomDialog.livingRoomBlockAndMute.LivingRoomBlockAndMuteListDialog;

public class AudienceManagerDialog extends BaseBindingDialogFragment {

    DialogAudienceManagerBinding mBind;
    String liveId,anchorId;

    public static AudienceManagerDialog getInstance(String liveId,String anchorId)
    {
        AudienceManagerDialog audienceManagerDialog=new AudienceManagerDialog();
        audienceManagerDialog.liveId=liveId;
        audienceManagerDialog.anchorId=anchorId;
        return audienceManagerDialog;
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
            case R.id.rlBlockAndMute:
            case R.id.rlSetAdmin:
                showAnotherDialog(view.getId());
                break;
        }
    }

    private void showAnotherDialog(int id)
    {
        startAnimate(mBind.llContent, false, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (id)
                {
                    case R.id.rlBlockAndMute:
                        LivingRoomBlockAndMuteListDialog livingRoomBlockAndMuteListDialog=LivingRoomBlockAndMuteListDialog.getInstance(liveId,anchorId);
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getFragmentManager(),livingRoomBlockAndMuteListDialog);
                        break;
                    case R.id.rlSetAdmin:
                        AudienceAdminListDialog audienceAdminListDialog=AudienceAdminListDialog.getInstance(liveId,anchorId);
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getFragmentManager(),audienceAdminListDialog);
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
        return R.layout.dialog_audience_manager;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        startAnimate(mBind.llContent,true);
    }
}
