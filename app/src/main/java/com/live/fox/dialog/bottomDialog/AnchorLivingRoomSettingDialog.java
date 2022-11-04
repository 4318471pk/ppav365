package com.live.fox.dialog.bottomDialog;

import android.view.View;

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
            case R.id.tvGift:
                TreasureBoxDialog treasureBoxDialog=TreasureBoxDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),treasureBoxDialog);
                break;
            case R.id.tvAudienceManage:

                break;
            case R.id.tvLivingSetting:

                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_anchor_living_room_setting;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);


    }


}
