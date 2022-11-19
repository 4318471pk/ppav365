package com.live.fox.dialog.bottomDialog;

import android.view.View;
import android.view.animation.Animation;

import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.DialogAnchorLivingRoomSettingBinding;
import com.live.fox.ui.openLiving.OpenLivingActivity;
import com.live.fox.ui.openLiving.StartLivingFragment;

import static android.view.View.VISIBLE;

public class AnchorLivingRoomSettingDialog extends BaseBindingDialogFragment {

    DialogAnchorLivingRoomSettingBinding mBind;
    String liveId,uid;

    public static AnchorLivingRoomSettingDialog getInstance(String liveId,String uid)
    {
        AnchorLivingRoomSettingDialog anchorLivingRoomSettingDialog= new AnchorLivingRoomSettingDialog();
        anchorLivingRoomSettingDialog.liveId=liveId;
        anchorLivingRoomSettingDialog.uid=uid;
        return anchorLivingRoomSettingDialog;
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
                        if(getParentFragment()!=null && getParentFragment() instanceof StartLivingFragment)
                        {
                            StartLivingFragment startLivingFragment=(StartLivingFragment)getParentFragment();
                            startLivingFragment.mBind.rlBotView.setVisibility(View.GONE);
                            startLivingFragment.mBind.llMessages.setVisibility(View.GONE);
                        }

                        if(getActivity()!=null && getActivity() instanceof OpenLivingActivity)
                        {
                            OpenLivingActivity openLivingActivity=(OpenLivingActivity)getActivity();
                            TreasureBoxDialog treasureBoxDialog=TreasureBoxDialog.getInstance(liveId,uid);
                            treasureBoxDialog.setVipGiftListData(openLivingActivity.vipGiftListData);
                            treasureBoxDialog.setGiftListData(openLivingActivity.giftListData);
                            treasureBoxDialog.setSendGiftAmountBeans(openLivingActivity.sendGiftAmountBeans);
                            treasureBoxDialog.setOnDismissListener(new BaseBindingDialogFragment.OnDismissListener() {
                                @Override
                                public void onDismiss(BaseBindingDialogFragment fragment) {
                                    if(openLivingActivity.isFinishing() || openLivingActivity.isDestroyed())
                                    {
                                        return;
                                    }

                                    if(fragment.getParentFragment()!=null && fragment.getParentFragment() instanceof StartLivingFragment)
                                    {
                                        StartLivingFragment startLivingFragment=(StartLivingFragment)fragment.getParentFragment();
                                        startLivingFragment.mBind.rlBotView.setVisibility(VISIBLE);
                                        startLivingFragment.mBind.llMessages.setVisibility(VISIBLE);
                                    }

                                    //去掉选中状态
                                    if(openLivingActivity.giftListData!=null)
                                    {
                                        for (int i = 0; i < openLivingActivity.giftListData.size(); i++) {
                                            openLivingActivity.giftListData.get(i).setSelected(false);
                                        }
                                    }
                                    if(openLivingActivity.vipGiftListData!=null)
                                    {
                                        for (int i = 0; i < openLivingActivity.vipGiftListData.size(); i++) {
                                            openLivingActivity.vipGiftListData.get(i).setSelected(false);
                                        }
                                    }

                                }
                            });
                            DialogFramentManager.getInstance().showDialogAllowingStateLoss(getParentFragmentManager(),treasureBoxDialog);
                        }

                        break;
                    case R.id.tvAudienceManage:
                        AudienceManagerDialog audienceManagerDialog=AudienceManagerDialog.getInstance();
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getParentFragmentManager(),audienceManagerDialog);
                        break;
                    case R.id.tvLivingSetting:
                        AnchorLivingSettingDialog anchorLivingSettingDialog=AnchorLivingSettingDialog.getInstance();
                        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getParentFragmentManager(),anchorLivingSettingDialog);
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
