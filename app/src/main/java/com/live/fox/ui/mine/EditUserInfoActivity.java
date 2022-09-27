package com.live.fox.ui.mine;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.ActionDialog;
import com.live.fox.dialog.MMLoading;
import com.live.fox.entity.OssToken;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_User;
import com.live.fox.ui.login.LoginActivity;
import com.live.fox.ui.login.LoginPageType;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.Utils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;

import java.io.File;
import java.util.List;


public class EditUserInfoActivity extends BaseHeadActivity implements View.OnClickListener {

    private ImageView ivHead;
    private TextView tvName;
    private TextView tvSex;
    private LinearLayout layoutPassword;
    private TextView tvQianming;
    private TextView modifyPs;
    String dataString = "";
    User user;
    String phone;

    public static void startActivity(@NonNull Context context, String phone) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, EditUserInfoActivity.class);
        i.putExtra("phone", phone);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edituserinfo_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        getIntentData();
        initView();
    }

    public void getIntentData() {
        if (getIntent() != null) {
            phone = getIntent().getStringExtra("phone");
        }
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.modifyPersonalMessage), true, true);



        ivHead = findViewById(R.id.iv_head);
        tvName = findViewById(R.id.tv_name);
        tvSex = findViewById(R.id.tv_sex);
        layoutPassword = findViewById(R.id.layout_password);
        tvQianming = findViewById(R.id.tv_qianming);
        modifyPs = findViewById(R.id.modifyPs);
        findViewById(R.id.iv_head).setOnClickListener(this);
        findViewById(R.id.rl_name).setOnClickListener(this);
        findViewById(R.id.rl_sex).setOnClickListener(this);
        findViewById(R.id.rl_qianming).setOnClickListener(this);
        findViewById(R.id.layout_password).setOnClickListener(this);

        getDeterminePwd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPage();
    }

    private void getDeterminePwd() {
        Api_Auth.ins().determinepPwd(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    try {
                        dataString = data;
                        if ("succ".equals(dataString)) {//没设置密码
                            modifyPs.setText(getString(R.string.noSetPassword));
                        } else if ("err".equals(dataString)) {//已经设置密码
                            modifyPs.setText(getString(R.string.password_change));
                        } else {
                            layoutPassword.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            }
        });
    }

    public void refreshPage() {
        user = DataCenter.getInstance().getUserInfo().getUser();

        GlideUtils.loadCircleOnePxRingImage(EditUserInfoActivity.this, user.getAvatar(), Color.parseColor("#979797"),
                R.color.transparent, R.drawable.img_default, ivHead);

        tvName.setText(user.getNickname());
        if (user.getSex() <= 0) {
            tvSex.setText(getString(R.string.unknownSex));
        } else {
            tvSex.setText(user.getSex() == 1 ? getString(R.string.boy) : getString(R.string.girl));
        }
        tvQianming.setText(StringUtils.isEmpty(user.getSignature()) ? "" : user.getSignature());
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.iv_head:
                RxPermissions rxPermissions = new RxPermissions(EditUserInfoActivity.this);
                rxPermissions.request(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                openPhoto();
                            } else { // 有的权限被拒绝或被勾选不再提示
                                LogUtils.e("222");
                                new AlertDialog.Builder(EditUserInfoActivity.this)
                                        .setMessage(getString(R.string.permissionRefusePic))
                                        .setPositiveButton(getString(R.string.see), (dialogInterface, i) -> {
                                        }).show();
                            }
                        });
                break;
            case R.id.rl_name:
                EditorNameActivity.start(EditUserInfoActivity.this, user.getNickname());
                break;
            case R.id.rl_sex:
                String sex[] = {getString(R.string.boy), getString(R.string.girl)};
                ActionDialog dialog = new ActionDialog(this, 1);
                dialog.setActions(sex);
                dialog.setEventListener(new ActionDialog.OnEventListener() {
                    @Override
                    public void onActionItemClick(ActionDialog dialog, ActionDialog.ActionItem item, int position) {
                        doUpdateUserInfoApi(user.getAvatar(), position + 1, 3);
                    }

                    @Override
                    public void onCancelItemClick(ActionDialog dialog) {

                    }
                });
                dialog.show();
                break;
            case R.id.rl_qianming:
                EditorMarkActivity.startActivity(EditUserInfoActivity.this, user.getSignature());
                break;
            case R.id.layout_password: //登录密码
                if ("succ".equals(dataString)) {//没设置密码
                    LoginActivity.startActivity(EditUserInfoActivity.this, LoginPageType.SetPwd, phone);
                } else if ("err".equals(dataString)) {//已经设置密码
                    LoginActivity.startActivity(EditUserInfoActivity.this, LoginPageType.ModifyPwd, phone);
                }
                break;
        }
    }

    public void doUpdateUserInfoApi(String headImageUrl, int sex, int type) {
        User user = DataCenter.getInstance().getUserInfo().getUser();
        if (!StringUtils.isEmpty(headImageUrl)) user.setAvatar(headImageUrl);
        if (sex >= 0) user.setSex(sex);
        Api_User.ins().modifyUserInfo(user, type, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (!StringUtils.isEmpty(data)) LogUtils.e("modifyUserInfo result : " + data);
                hideLoadingDialog();
                if (code == 0) {
                    if (!StringUtils.isEmpty(headImageUrl)) user.setAvatar(headImageUrl);
                    if (sex >= 0) user.setSex(sex);
                    SPManager.saveUserInfo(user);
                    refreshPage();
                    showToastTip(true, getString(R.string.modifySuccess));
                } else {
                    showToastTip(false, msg);
                }

            }
        });
    }


    public void openPhoto() {
        Constant.isAppInsideClick = true;
        PictureSelector.create(EditUserInfoActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、圖片.ofImage()、視頻.ofVideo()、音頻.ofAudio()
                .theme(R.style.picture_default_style)// 主題樣式設置 具體參考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(9)// 最大圖片選擇數量
                .minSelectNum(1)// 最小選擇數量
                .imageSpanCount(4)// 每行顯示個數
                .selectionMode(PictureConfig.SINGLE)// 多選 or 單選
                .previewImage(true)// 是否可預覽圖片
                .previewVideo(true)// 是否可預覽視頻
                .enablePreviewAudio(true) // 是否可播放音頻
                .isCamera(true)// 是否顯示拍照按鈕
                .isZoomAnim(true)// 圖片列表點擊 縮放效果 默認true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存圖片格式後綴,默認jpeg
                //.setOutputCameraPath("/CustomPath")// 自定義拍照保存路徑
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否壓縮
                .synOrAsy(true)//同步true或異步false 壓縮 默認同步
                //.compressSavePath(getPath())//壓縮圖片保存地址
                //.sizeMultiplier(0.5f)// glide 加載圖片大小 0~1之間 如設置 .glideOverride()無效
                .glideOverride(160, 160)// glide 加載寬高，越小圖片列表越流暢，但會影響列表圖片瀏覽的清晰度
//                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定義
//                .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否顯示uCrop工具欄，默認不顯示
                .isGif(false)// 是否顯示gif圖片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圓形裁剪
//                .showCropFrame(cb_showCropFrame.isChecked())// 是否顯示裁剪矩形邊框 圓形裁剪時建議設為false
//                .showCropGrid(cb_showCropGrid.isChecked())// 是否顯示裁剪矩形網格 圓形裁剪時建議設為false
                .openClickSound(false)// 是否開啟點擊聲音
                .selectionMedia(null)// 是否傳入已選圖片
                //.isDragFrame(false)// 是否可拖動裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 預覽圖片時 是否增強左右滑動圖片體驗(圖片滑動壹半即可看到上壹張是否選中)
                //.cropCompressQuality(90)// 裁剪壓縮質量 默認100
                .minimumCompressSize(100)// 小於100kb的圖片不壓縮
                //.cropWH()// 裁剪寬高比，設置如果大於圖片本身寬高則無效
                //.rotateEnabled(true) // 裁剪是否可旋轉圖片
                //.scaleEnabled(true)// 裁剪是否可放大縮小圖片
                //.videoQuality()// 視頻錄制質量 0 or 1
                //.videoSecond()//顯示多少秒以內的視頻or音頻也可適用
                //.recordVideoSecond()//錄制視頻秒數 默認60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//結果回調onActivityResult code
    }

    MMLoading loadingDialog;

    LocalMedia localMedia;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 圖片選擇結果回調
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 裏面返回三種path
                    // 1.media.getPath(); 為原圖path
                    // 2.media.getCutPath();為裁剪後path，需判斷media.isCut();是否為true
                    // 3.media.getCompressPath();為壓縮後path，需判斷media.isCompressed();是否為true
                    // 如果裁剪並壓縮了，已取壓縮路徑為準，因為是先裁剪後壓縮的
                    for (LocalMedia media : selectList) {
                        LogUtils.e("圖片-----》" + media.getPath());
                    }
                    localMedia = selectList.get(0);
                    LogUtils.e("图片-----》" + localMedia.getPath());
//                    GlideTools.loadImage(EditUserInfoActivity.this, localMedia.getPath(),ivHead);
//                    RequestOptions options = new RequestOptions()
//                            .centerCrop()
//                            .placeholder(R.color.white)
//                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                    loadingDialog = showLoadingDialog(getString(R.string.pictureUploading), false, true);
                    Api_Config.ins().getOssToken(new JsonCallback<OssToken>() {
                        @Override
                        public void onSuccess(int code, String msg, OssToken ossToken) {
                            if (ossToken != null) LogUtils.e(ossToken.toString());
                            if (code == 0) {
                                if (ossToken != null) {
                                    //开启线程
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            uploadImage(ossToken);
                                        }
                                    }).start();
                                } else {
                                    hideLoadingDialog();
                                    showToastTip(false, getString(R.string.configurationInformation));
                                }
                            } else {
                                showToastTip(false, getString(R.string.fuwuInformation));
                                hideLoadingDialog();
                            }
                        }
                    });
                    break;
            }
        }
    }

    OSS oss;
    String uploadImgUrl;

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

            LogUtils.e(localFilePath + "," + ossToken.getBucketName() + "," + uploadFileServerName);

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
                    requestApi();

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

    //调用修改用户信息接口
    public void requestApi() {
        // 開啓動畫，因爲定時原因，所以可能是在子線程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    loadingDialog.setText(getString(R.string.interfaceCalling));
                } else {
                    loadingDialog = showLoadingDialog(getString(R.string.interfaceCalling), false, true);
                }
            }
        });
        doUpdateUserInfoApi(uploadImgUrl, user.getSex(), 1);
    }
}
