package com.live.fox.ui.mine;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.live.fox.AnchorLiveActivity;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityCenterAnchorBinding;
import com.live.fox.dialog.bottomDialog.EditProfileImageDialog;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.openLiving.OpenLivingActivity;
import com.live.fox.ui.mine.editprofile.EditProfileImageActivity;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class CenterOfAnchorActivity extends BaseBindingViewActivity {

    ActivityCenterAnchorBinding mBind;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, CenterOfAnchorActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    try {
                        String url=  PictureFileUtils.getPicturePath(this);
                        File file=new File(url);
                        if(file!=null && file.exists())
                        {
                            EditProfileImageActivity.startActivity(this,EditProfileImageActivity.Square,url);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 圖片選擇結果回調
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if(selectList!=null && selectList.size()>0)
                    {
                        LocalMedia localMedia = selectList.get(0);
                        LogUtils.e("图片-----》" + localMedia.getPath());
                        EditProfileImageActivity.startActivity(this,EditProfileImageActivity.Square,localMedia.getPath());
                    }
                    break;
            }
        }
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlChangeRoomPic:
                EditProfileImageDialog dialog= EditProfileImageDialog.getInstance();
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),dialog);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_center_anchor;
    }

    @Override
    public void initView() {
        setWindowsFlag();
        mBind = getViewDataBinding();
        mBind.setClick(this);

        setActivityTitle(getString(R.string.centerOfAnchor));
        getTvTitleRight().setText(getResources().getString(R.string.goOpenSteam));
        getTvTitleRight().setTextColor(0xffFF008A);
        Drawable leftIcon = getDrawable(R.mipmap.icon_small_live);
        getTvTitleRight().setCompoundDrawablesRelativeWithIntrinsicBounds(leftIcon, null, null, null);
        getTvTitleRight().setCompoundDrawablePadding(ScreenUtils.dp2px(this, 3));
        getTvTitleRight().setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                openLive();
            }
        });

        mBind.gtvTitleOfRoom.setText(DataCenter.getInstance().getUserInfo().getUser().getNickname());
    }

    private void openLive() {
        boolean careraPermission = false;
        boolean mircPermission = false;
        if (ContextCompat.checkSelfPermission(CenterOfAnchorActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            careraPermission = true;
        }

        if (ContextCompat.checkSelfPermission(CenterOfAnchorActivity.this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            mircPermission = true;
        }

        if (!careraPermission || !mircPermission) {
            RxPermissions rxPermissions = new RxPermissions(CenterOfAnchorActivity.this);
            Disposable subscribe = rxPermissions.request(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO)
                    .subscribe(granted -> {
                        if (granted) {
                            checkAuth();
                        } else { // 有的权限被拒绝或被勾选不再提示
                            LogUtils.e("有的权限被拒绝");
                            new AlertDialog.Builder(CenterOfAnchorActivity.this)
                                    .setCancelable(false)
                                    .setMessage(getString(R.string.notePermission))
                                    .setPositiveButton(getString(R.string.see), (dialog, which) -> LogUtils.e("权限被拒绝"))
                                    .show();
                        }
                    });
        } else {
            checkAuth();
        }
    }

    /**
     * 开始直播
     */
    public void checkAuth() {
//        OpenLivingActivity.startActivity(this);
        Intent intent = new Intent(CenterOfAnchorActivity.this, AnchorLiveActivity.class);
        startActivity(intent);
        Api_Live.ins().getAnchorAuth(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 && data != null) {
                    Log.e("checkAuth",data);
//                    try {
//                        JSONObject jb = new JSONObject(data);
//                        int auth = jb.optInt("auth");
//                        User user = DataCenter.getInstance().getUserInfo().getUser();
//                        if (user == null) {
//                            LogUtils.e("主播状态：" + "开启直播出错，用户信息失败");
//                            return;
//                        }
//
//                        user.setAuth(auth);
//                        DataCenter.getInstance().getUserInfo().updateUser(user);
//                        if (auth == 2 && BuildConfig.IsAnchorClient) {
//                            Constant.isAppInsideClick = true;
//                            Intent intent = new Intent(CenterOfAnchorActivity.this, AnchorLiveActivity.class);
//                            startActivity(intent);
//                        } else if (auth == 1) { //1待审核
//                            showToastTip(false, getString(R.string.certificating));
//                        } else { //未认证
//                            DialogFactory.showTwoBtnDialog(CenterOfAnchorActivity.this,
//                                    getString(R.string.certiGo), getString(R.string.cancel),
//                                    getString(R.string.goCerti), (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
//                                        dialog.dismiss();
//                                        AuthActivity.startActivity(CenterOfAnchorActivity.this);
//                                    });
//                        }
//                    } catch (Exception e) {
//                        e.getStackTrace();
//                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }
}
