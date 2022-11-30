package com.live.fox.ui.mine.editprofile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.EdituserinfoActivityBinding;
import com.live.fox.dialog.ActionDialog;
import com.live.fox.dialog.MMLoading;
import com.live.fox.dialog.bottomDialog.AreaListSelectorDialog;
import com.live.fox.dialog.bottomDialog.EditProfileImageDialog;
import com.live.fox.dialog.bottomDialog.EditProfileImageDialog2;
import com.live.fox.dialog.bottomDialog.SimpleSelectorDialog;
import com.live.fox.dialog.bottomDialog.TimePickerDialog;
import com.live.fox.dialog.temple.EditNickNameConfirmDialog;
import com.live.fox.entity.OssToken;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_User;
import com.live.fox.ui.login.LoginActivity;
import com.live.fox.ui.login.LoginPageType;
import com.live.fox.ui.mine.CenterOfAnchorActivity;
import com.live.fox.ui.mine.EditorMarkActivity;
import com.live.fox.ui.mine.EditorNameActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.Utils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class EditUserInfoActivity extends BaseBindingViewActivity{

    String dataString = "";
    User user;
    EdituserinfoActivityBinding mBind;
    List<String> listJob = new ArrayList<>();
    List<String> listGq = new ArrayList();
    OSS oss;
    String uploadImgUrl;

    public static void startActivity(@NonNull Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, EditUserInfoActivity.class);
        context.startActivity(i);
    }



    @Override
    public int onCreateLayoutId() {
        return R.layout.edituserinfo_activity;
    }


    @Override
    public void initView() {
        setWindowsFlag();
        setActivityTitle(getString(R.string.edit_info));
        mBind=getViewDataBinding();
        mBind.setClick(this);

        listJob.add(getString(R.string.job_1));listJob.add(getString(R.string.job_2));listJob.add(getString(R.string.job_3));
        listJob.add(getString(R.string.job_4));listJob.add(getString(R.string.job_5));listJob.add(getString(R.string.job_6));
        listJob.add(getString(R.string.job_7));listJob.add(getString(R.string.job_8));listJob.add(getString(R.string.job_9));
        listJob.add(getString(R.string.job_10));listJob.add(getString(R.string.job_11));listJob.add(getString(R.string.job_12));
        listJob.add(getString(R.string.job_13));listJob.add(getString(R.string.job_14));

        listGq.add(getString(R.string.loving_1));
        listGq.add(getString(R.string.loving_2));
        listGq.add(getString(R.string.loving_3));
        listGq.add(getString(R.string.loving_4));
        listGq.add(getString(R.string.privacyStr));

        refreshPage();
    }


    @Override
    public void onClickView(View view) {
        if (ClickUtil.isClickWithShortTime(view.getId(),1000)) return;

        switch (view.getId()) {
            case R.id.iv_head:
                RxPermissions rxPermissions = new RxPermissions(EditUserInfoActivity.this);
                rxPermissions.request(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
//                                openPhoto();

                                EditProfileImageDialog2 dialog = EditProfileImageDialog2.getInstance();
                                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(), dialog);

                            } else { // 有的权限被拒绝或被勾选不再提示
                                LogUtils.e("222");
                                new AlertDialog.Builder(EditUserInfoActivity.this)
                                        .setMessage(getString(R.string.permissionRefusePic))
                                        .setPositiveButton(getString(R.string.see), (dialogInterface, i) -> {
                                        }).show();
                            }
                        });
                break;
            case R.id.rl_job:
                SimpleSelectorDialog dialogOccupation=SimpleSelectorDialog.getInstance(new SimpleSelectorDialog.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index,SimpleSelectorDialog dialog) {
                        User user=new User();
                        user.setJob(index);
                        modifyUser(user, 7,dialog);
                    }
                });

                dialogOccupation.setData(listJob);
                dialogOccupation.setTitle(getString(R.string.occupation));
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), dialogOccupation);
                break;
            case R.id.rl_birthday:
                TimePickerDialog timePickerDialog = new TimePickerDialog();
                timePickerDialog.setOnSelectedListener(new TimePickerDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(int year, int month, int date, long time,TimePickerDialog fragment) {
                        String s = year + "-" + month + "-" + date;
                        User user=new User();
                        user.setBirthday(s);
                        modifyUser(user, 6,fragment);

                    }
                });
                DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),timePickerDialog);
                break;
            case R.id.rl_emo:
                SimpleSelectorDialog dialogRelationshipStatus=SimpleSelectorDialog.getInstance(new SimpleSelectorDialog.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index,SimpleSelectorDialog dialog) {
                        User user=new User();
                        index ++;
                        user.setEmotionalState(index);
                        modifyUser(user, 5,dialog);
                    }
                });

                dialogRelationshipStatus.setData(listGq);
                dialogRelationshipStatus.setTitle(getString(R.string.relationshipStatus2));
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), dialogRelationshipStatus);
                break;
            case R.id.rl_home:
                AreaListSelectorDialog areaListSelectorDialog =new AreaListSelectorDialog();
                areaListSelectorDialog.setOnCityConfirm(new AreaListSelectorDialog.OnCityConfirm() {
                    @Override
                    public void onSelect(String province, String city,AreaListSelectorDialog dialog) {
                        User user = new User();
                        user.setCity(city);
                        user.setProvince(province);
                        modifyUser(user, 8,dialog);
                    }
                });
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), areaListSelectorDialog);
                break;
            case R.id.rl_sex:
                SimpleSelectorDialog dialog =  SimpleSelectorDialog.getInstance(new SimpleSelectorDialog.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index,SimpleSelectorDialog dialog) {
                        User user=new User();

                        if (index == 0) {
                            user.setSex(1);
                        } else {
                            user.setSex(2);
                        }
                        modifyUser(user, 3,dialog);
                    }
                });
                List<String> list = new ArrayList();
                list.add(getString(R.string.boy));
                list.add(getString(R.string.girl));
                dialog.setData(list);
                dialog.setTitle(getString(R.string.selectGender));
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), dialog);
                break;
            case R.id.rl_name:
                //改名需要等级达到2级。
                if(DataCenter.getInstance().getUserInfo().getUser().getUserLevel()>1)
                {
                    DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), EditNickNameConfirmDialog.getInstance());
                }
                else
                {
                    ToastUtils.showShort(getString(R.string.editNameLimit));
                }
                break;
            case R.id.rl_qianming:
                EditorMarkActivity.startActivity(EditUserInfoActivity.this, user.getSignature());
                break;
        }
    }

    public void refreshPage() {
        user = DataCenter.getInstance().getUserInfo().getUser();

        GlideUtils.loadCircleOnePxRingImage(EditUserInfoActivity.this, user.getAvatar(), Color.parseColor("#979797"),
                R.color.transparent, R.mipmap.user_head_error, mBind.ivHead);

        mBind.tvName.setText(user.getNickname());
        mBind.tvAccount.setText(user.getUid()+"");
        if (user.getSex() <= 0) {
            mBind.tvSex.setText(getString(R.string.unknownSex));
        } else {
            mBind.tvSex.setText(user.getSex() == 1 ? getString(R.string.boy) : getString(R.string.girl));
        }
        mBind.tvQianming.setText(StringUtils.isEmpty(user.getSignature()) ? "" : user.getSignature());
        if (TextUtils.isEmpty(user.getBirthday())) {
            mBind.tvBirthday.setText(getString(R.string.privacyStr));
        } else {
            mBind.tvBirthday.setText(user.getBirthday());
        }
        //mBind.tvAge.setText(getString(R.string.privacyStr));
        mBind.tvHome.setText(TextUtils.isEmpty(user.getCity())?
                getString(R.string.privacyStr):user.getProvince() + "-" + user.getCity());
        setGq();
        //  mBind.tvRelationshipStatus.setText(getString(R.string.privacyStr));
        if (user.getJob() == -1) {
            mBind.tvJob.setText(getString(R.string.privacyStr));
        } else {
            if (user.getJob() < listJob.size()) {
                mBind.tvJob.setText(listJob.get(user.getJob()));}
        }
    }

    LocalMedia localMedia;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    try {
                        String url = PictureFileUtils.getPicturePath(this);
                        File file = new File(url);
                        if (file != null && file.exists()) {
                            EditProfileImageActivity.startActivity(this, EditProfileImageActivity.Square, url);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 圖片選擇結果回調
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList != null && selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        LogUtils.e("图片-----》" + localMedia.getPath());
                        EditProfileImageActivity.startActivity(this, EditProfileImageActivity.Square, localMedia.getPath());
                    }
                    break;
                case ConstantValue.REQUEST_CROP_PIC://头像上传到文件服务器成功
                    if (data != null && data.getStringExtra(ConstantValue.pictureOfUpload) != null) {
                        File file = new File(data.getStringExtra(ConstantValue.pictureOfUpload));
                        if (file != null && file.exists()) {
                            updateFile(file);
                        }
                    }
                    break;
            }
        }
    }

    private void updateFile(File file) {
        showLoadingDialog();
        Api_User.ins().uploadUserPhoto(file, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0 && data != null) {
                    user.setAvatar(data);
                    modifyUser(user, 1,null);
                } else {
                    showToastTip(true, msg);
                }

            }
        });

    }

    public void uploadImage(OssToken ossToken) {
        if (oss == null) {
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossToken.getKey(), ossToken.getSecret(), ossToken.getToken());
            oss = new OSSClient(Utils.getApp(), ossToken.getEndpoint(), credentialProvider, new ClientConfiguration());
        }

        if (user != null) {
            // 上传文件名
            String uploadFileServerName = user.getUid() + "_" + System.currentTimeMillis() / 1000 + "_avatar.png";
            String localFilePath = "";
            if (localMedia.isCompressed()) {
                LogUtils.e("compress image result:" + new File(localMedia.getCompressPath()).length() / 1024 + "k");
                localFilePath = localMedia.getCompressPath();
            } else {
                localFilePath = localMedia.getPath();
            }

            LogUtils.e("上传文件名：" + localFilePath + "," + ossToken.getBucketName() + "," + uploadFileServerName);

            // 構造上傳請求
            PutObjectRequest put = new PutObjectRequest(ossToken.getBucketName(), uploadFileServerName, localFilePath);
            // 異步上傳時可以設置進度回調
            put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    LogUtils.e("currentSize: " + currentSize + " totalSize: " + totalSize);
                }
            });

            //上传图片有时候会卡主很长时间 这里程序判断上传图片8秒内没返回就作为失败处理
            uploadIsFinish = false;
            uploadIsFinishHandler.sendEmptyMessageDelayed(1, 20 * 1000);
            oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    LogUtils.e("UploadSuccess");
                    if (uploadIsFinish) {
                        //说明已经处理 这里就不在处理了
                        return;
                    }
                    uploadIsFinish = true;
                    uploadIsFinishHandler.removeMessages(1);
                    uploadImgUrl = oss.presignPublicObjectURL(ossToken.getBucketName(), uploadFileServerName);

                    LogUtils.e("调用成功 云服务器图片地址：imgUrl : " + uploadImgUrl);
                    uploadImgUrl = uploadImgUrl.replace(ossToken.getBucketName() + "." + ossToken.getEndpoint(), SPManager.getDomain());
                    LogUtils.e("调用成功 服务器图片地址：imgUrl : " + uploadImgUrl);

                    //已经全部传好 调用接口
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                    hideLoadingDialog();
                    if (uploadIsFinish) {
                        //说明已经处理 这里就不在处理了
                        return;
                    }
                    uploadIsFinish = true;
                    uploadIsFinishHandler.removeMessages(1);

                    // 請求異常
                    if (clientExcepion != null) {
                        // 本地異常如網絡異常等
                        showToastTip(false, getString(R.string.netWorkException));
                    }
                    if (serviceException != null) {
                        showToastTip(false, getString(R.string.netWorkException));
                        // 服務異常
                        LogUtils.e(serviceException.getErrorCode());
                        LogUtils.e(serviceException.getRequestId());
                        LogUtils.e(serviceException.getHostId());
                        LogUtils.e(serviceException.getRawMessage());
                    }
                }
            });
        }
    }


    //上传图片是否已回调
    boolean uploadIsFinish = false;
    //上传图片有时候会卡主很长时间 这里程序判断上传图片8秒内没返回就作为失败处理
    private final Handler uploadIsFinishHandler = new Handler(msg -> {
        if (!uploadIsFinish) {
            uploadIsFinish = true;
            //超过5秒 上传图片还没返回消息
            hideLoadingDialog();
            showToastTip(false, getString(R.string.networkBad));
            return false;
        }
        return false;
    });



    private void modifyUser(User userTemp, int type, BaseBindingDialogFragment fragment){
        showLoadingDialog();
        Api_User.ins().modifyUserInfo(userTemp, type, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (fragment != null) {
                    fragment.dismissAllowingStateLoss();
                }

                if(code==0) {
                    showToastTip(true, getString(R.string.modifySuccess));

                    if (type == 3) {
                        user.setSex(userTemp.getSex());
                        mBind.tvSex.setText(user.getSex() == 1? getString(R.string.boy): getString(R.string.girl));
                    }
                    else if(type==1)
                    {
                        GlideUtils.loadImage(EditUserInfoActivity.this, user.getAvatar(),mBind.ivHead);
                        showToastTip(true, getString(R.string.modifySuccess));
                    }
                    else if (type == 5) {
                        user.setEmotionalState(userTemp.getEmotionalState());
                        setGq();
                    } else if (type == 6) {
                        mBind.tvBirthday.setText(userTemp.getBirthday());
                    } else if (type == 7){
                        mBind.tvJob.setText(listJob.get(userTemp.getJob()));
                    } else if (type == 8){
                        mBind.tvHome.setText(userTemp.getProvince() + "-" + userTemp.getCity());
                    }
                    DataCenter.getInstance().getUserInfo().updateUser(user);
                } else {
                    showToastTip(true, msg);
                }
            }

        });

    }

    private void setGq(){
        if (user.getEmotionalState() == 1) {
            mBind.tvEmo.setText(getString(R.string.loving_1));
        } else if (user.getEmotionalState() == 2) {
            mBind.tvEmo.setText(getString(R.string.loving_2));
        } else if (user.getEmotionalState() == 3) {
            mBind.tvEmo.setText(getString(R.string.loving_3));
        } else if (user.getEmotionalState() == 4) {
            mBind.tvEmo.setText(getString(R.string.loving_4));
        } else if (user.getEmotionalState() == 5) {
            mBind.tvEmo.setText(getString(R.string.privacyStr));
        }

    }
}
