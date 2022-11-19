package com.live.fox.ui.mine;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.live.fox.AnchorLiveActivity;
import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityCenterAnchorBinding;
import com.live.fox.dialog.bottomDialog.EditProfileImageDialog;
import com.live.fox.entity.ConfigPathsBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.editprofile.EditProfileImageActivity;
import com.live.fox.ui.openLiving.OpenLivingActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.rtmp.TXLiveBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class CenterOfAnchorActivity extends BaseBindingViewActivity {

    ActivityCenterAnchorBinding mBind;
    List<ConfigPathsBean> configPathsBeans;
    String liveId;

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
                case ConstantValue.REQUEST_CROP_PIC://头像上传到文件服务器成功
                    if(data!=null && data.getStringExtra(ConstantValue.pictureOfUpload)!=null)
                    {
                        File file=new File(data.getStringExtra(ConstantValue.pictureOfUpload));
                        if(file!=null && file.exists())
                        {
                            uploadBGOfLivingRoom(file);
                        }
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
        Drawable leftIcon = getResources().getDrawable(R.mipmap.icon_small_live);
        getTvTitleRight().setCompoundDrawablesRelativeWithIntrinsicBounds(leftIcon, null, null, null);
        getTvTitleRight().setCompoundDrawablePadding(ScreenUtils.dp2px(this, 3));
        getTvTitleRight().setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                if(mBind.gtvTitleOfRoom.getText().toString().length()==0)
                {
                    ToastUtils.showShort(getString(R.string.plsFillTitleOfRoom));
                    return;
                }

                if(configPathsBeans!=null && configPathsBeans.size()>0)
                {
                    TXLiveBase.getInstance().setLicence(CommonApp.getInstance(),
                            configPathsBeans.get(0).getLicenceUrl(),configPathsBeans.get(0).getLicenceKey()
                    );
                    //目前写死 看以后怎么拿
                    openLive("84");
                }
            }
        });

        mBind.gtvTitleOfRoom.setText(DataCenter.getInstance().getUserInfo().getUser().getNickname());
        getLineList();
        getCenterData();
    }

    private void openLive(String liveConfigId) {
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

                            String roomType=mBind.gtvTypeOfRoom.getText().toString();
                            if(!TextUtils.isEmpty(liveId) && !TextUtils.isEmpty(roomType))
                            {
                                getTvTitleRight().setEnabled(false);
                                String roomTitle=mBind.gtvTitleOfRoom.getText().toString();
                                String icon=mBind.ivRoomIcon.getTag()==null?"":(String)mBind.ivRoomIcon.getTag();
                                OpenLivingActivity.startActivity(CenterOfAnchorActivity.this,icon,roomTitle,liveId,liveConfigId,roomType);
                                getTvTitleRight().setEnabled(true);
                            }
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
            String roomType=mBind.gtvTypeOfRoom.getText().toString();
            if(!TextUtils.isEmpty(liveId) && !TextUtils.isEmpty(roomType))
            {
                getTvTitleRight().setEnabled(false);
                String roomTitle=mBind.gtvTitleOfRoom.getText().toString();
                String icon=mBind.ivRoomIcon.getTag()==null?"":(String)mBind.ivRoomIcon.getTag();
                OpenLivingActivity.startActivity(CenterOfAnchorActivity.this,icon,roomTitle,liveId,liveConfigId,roomType);
                getTvTitleRight().setEnabled(true);
            }
        }
    }

    private void getLineList()
    {
        showLoadingDialogWithNoBgBlack();
        Api_Config.ins().getConfigPaths(DataCenter.getInstance().getUserInfo().getUser().getUid(), new JsonCallback<List<ConfigPathsBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<ConfigPathsBean> data) {
                hideLoadingDialog();
                if (code == Constant.Code.SUCCESS) {
                    if(data!=null && data.size()>0)
                    {
                        configPathsBeans=data;
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void uploadBGOfLivingRoom(File file)
    {
        showLoadingDialogWithNoBgBlack();
        Api_User.ins().uploadLivingRoomPicture(file, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == Constant.Code.SUCCESS) {
                    GlideUtils.loadImage(CenterOfAnchorActivity.this, data, mBind.ivRoomIcon);
                    mBind.ivRoomIcon.setTag(data);
                    showToastTip(true, getString(R.string.modifySuccess));
                    //Log.e("uploadBGOfLivingRoom",data);
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void getCenterData()
    {
        showLoadingDialogWithNoBgBlack();
        //{"roomId":null,"icon":null,"title":null,"type":null}
        Api_Live.ins().getAnchorCenterInfo(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if(code==0)
                {
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        liveId= jsonObject.optString("roomId","");
                        String icon= jsonObject.optString("icon","");
                        String title= jsonObject.optString("title","");
                        String type= jsonObject.optString("type","");
                        GlideUtils.loadDefaultImage(CenterOfAnchorActivity.this,icon,R.mipmap.user_head_error,R.mipmap.user_head_error,mBind.ivRoomIcon);
                        if(!TextUtils.isEmpty(icon))
                        {
                            mBind.ivRoomIcon.setTag(icon);
                        }

                        if(Strings.isDigitOnly(type))
                        {
                            int index=Integer.valueOf(type);
                            if(index>-1 && index<3)
                            {
                                String str[]=getResources().getStringArray(R.array.typeOfRoom);
                                mBind.gtvTypeOfRoom.setText(str[index]);
                            }
                        }

                        mBind.gtvTitleOfRoom.setText(title);
//                        switch (type)
//                        {
//
//                        }
//                        mBind.gtvTypeOfRoom.setText();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }
}
