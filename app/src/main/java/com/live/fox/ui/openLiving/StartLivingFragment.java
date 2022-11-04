package com.live.fox.ui.openLiving;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;

import com.live.fox.AnchorLiveActivity;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.FragmentStartLivingBinding;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.dialog.TipDialog;
import com.live.fox.dialog.bottomDialog.AnchorLivingRoomSettingDialog;
import com.live.fox.dialog.bottomDialog.AnchorProtectorListDialog;
import com.live.fox.dialog.bottomDialog.ContributionRankDialog;
import com.live.fox.dialog.bottomDialog.LivingProfileBottomDialog;
import com.live.fox.dialog.bottomDialog.OnlineNobilityAndUserDialog;
import com.live.fox.dialog.bottomDialog.SetRoomTypeDialog;
import com.live.fox.utils.CountTimerUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;
import com.luck.picture.lib.permissions.RxPermissions;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;

import static com.tencent.rtmp.TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;

public class StartLivingFragment extends BaseBindingFragment {

    FragmentStartLivingBinding mBind;

    @Override
    public void onClickView(View view) {

        OpenLivingActivity activity=(OpenLivingActivity)getActivity();

        switch (view.getId())
        {
            case R.id.ivCameraChangeSide:
                activity.switchCamera();
                break;
            case R.id.ivRoomType:
                SetRoomTypeDialog setRoomTypeDialog=SetRoomTypeDialog.getInstance(true);
                setRoomTypeDialog.setOnSelectRoomTypeListener(new SetRoomTypeDialog.OnSelectRoomTypeListener() {
                    @Override
                    public void onSelect(int position) {

                    }
                });
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),setRoomTypeDialog);
                break;
            case R.id.rivProfileImage:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), LivingProfileBottomDialog.getInstance(LivingProfileBottomDialog.AnchorSelf));
                break;
            case R.id.gtvOnlineAmount:
                OnlineNobilityAndUserDialog onlineNobilityAndUserDialog=OnlineNobilityAndUserDialog.getInstance(mBind.gtvOnlineAmount.getText().toString());
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),onlineNobilityAndUserDialog);
                break;
            case R.id.gtvProtection:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), AnchorProtectorListDialog.getInstance());
                break;
            case R.id.gtvContribution:
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(), ContributionRankDialog.getInstance());
                break;
            case R.id.ivSetting:
                AnchorLivingRoomSettingDialog anchorLivingRoomSettingDialog=AnchorLivingRoomSettingDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getChildFragmentManager(),anchorLivingRoomSettingDialog);
                break;

        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_start_living;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        mBind.llTopView.setVisibility(View.GONE);
        CountTimerUtil.getInstance().start(mBind.rlMain, new CountTimerUtil.OnAnimationFinishListener() {
            @Override
            public void onFinish() {
                mBind.llTopView.setVisibility(View.VISIBLE);
                OpenLivingActivity openLivingActivity=(OpenLivingActivity)getActivity();
                openLivingActivity.startRTMPPush();
            }
        });
    }


}
