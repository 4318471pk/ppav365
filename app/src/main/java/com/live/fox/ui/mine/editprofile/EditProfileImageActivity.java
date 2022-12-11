package com.live.fox.ui.mine.editprofile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.OssToken;
import com.live.fox.entity.User;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_User;
import com.live.fox.server.BaseApi;
import com.live.fox.ui.agency.PromoMaterialActivity;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.Utils;
import com.live.fox.utils.okgo.OkGoHttpUtil;
import com.live.fox.view.LikeQQCropView;
import com.live.fox.view.PictureCropView;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.io.File;

import static com.live.fox.server.BaseApi.getCommonHeaders;

public class EditProfileImageActivity extends BaseActivity {

    public static final String picUrl="picPath";
    public static final String Shape="Shape";
    public static final String Circle="Circle";
    public static final String Square="Square";
    LikeQQCropView pictureCropView;


    public static void startActivity(Activity context,String url)
    {
        Intent intent=new Intent(context,EditProfileImageActivity.class);
        intent.putExtra(picUrl,url);
        context.startActivityForResult( intent,ConstantValue.REQUEST_CROP_PIC);
    }

    public static void startActivity(Activity context,String shape,String url)
    {
        Intent intent=new Intent(context,EditProfileImageActivity.class);
        intent.putExtra(picUrl,url);
        intent.putExtra(Shape,shape);
        context.startActivityForResult( intent,ConstantValue.REQUEST_CROP_PIC);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url=getIntent().getStringExtra(picUrl);
        String shape=getIntent().getStringExtra(Shape);
        if(TextUtils.isEmpty(shape))
        {
            shape=Circle;
        }

        if(TextUtils.isEmpty(url))
        {
            ToastUtils.showShort(getString(R.string.uploadProfileImgTips));
            finish();
            return;
        }

        LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_header_layout,null);

        pictureCropView=new LikeQQCropView(this);

        int mRadius=0;
        int screenWidth=ScreenUtils.getScreenWidth(this);
        int dip10=ScreenUtils.dip2px(this,10);
        switch (shape)
        {
            case Circle:
                mRadius=screenWidth/2-dip10;
                pictureCropView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth,screenWidth));
                break;
            case Square:
                mRadius=0;
                pictureCropView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, screenWidth));
                break;
        }


        pictureCropView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        pictureCropView.setBitmap(url,ScreenUtils.getScreenWidth(this),ScreenUtils.getScreenWidth(this));
        pictureCropView.setRadius(mRadius);

        view.addView(pictureCropView);
        setContentView(view);

        ImageView ivHeadLeft = findViewById(R.id.ivHeadLeft);
        TextView tvHeadTitle = findViewById(R.id.tvHeadTitle);
        TextView tvTitleRight=findViewById(R.id.tvTitleRight);
        ivHeadLeft.setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                finish();
            }
        });

        tvHeadTitle.setText(getString(R.string.uploadProfileImg));
        tvTitleRight.setText(getString(R.string.save));
        tvTitleRight.setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                Bitmap bitmap= pictureCropView.clip();
                if(bitmap!=null)
                {
                    String state = Environment.getExternalStorageState();
                    File rootDir = state.equals(Environment.MEDIA_MOUNTED) ?
                            Environment.getExternalStorageDirectory() : getCacheDir();

                    File folderDir = new File(rootDir.getAbsolutePath() + PictureFileUtils.CAMERA_PATH);
                    if(folderDir!=null)
                    {
                        folderDir.mkdirs();
                    }
                    File pic=new File(folderDir.getPath()+"/"+System.currentTimeMillis()+".jpg");

                    PictureFileUtils.saveBitmapToJPGFile(bitmap,pic,100);
                    LogUtils.e("上传头像地址：" + pic.getAbsolutePath());

                    Intent intent=new Intent();
                    intent.putExtra(ConstantValue.pictureOfUpload,pic.toString());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

//    public void uploadImage(OssToken ossToken) {
//        if (oss == null) {
//            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossToken.getKey(), ossToken.getSecret(), ossToken.getToken());
//            oss = new OSSClient(Utils.getApp(), ossToken.getEndpoint(), credentialProvider, new ClientConfiguration());
//        }
//
//        if (user != null) {
//            // 上传文件名
//            String uploadFileServerName = user.getUid() + "_" + System.currentTimeMillis() / 1000 + "_avatar.png";
//            String localFilePath = "";
//            if (localMedia.isCompressed()) {
//                LogUtils.e("compress image result:" + new File(localMedia.getCompressPath()).length() / 1024 + "k");
//                localFilePath = localMedia.getCompressPath();
//            } else {
//                localFilePath = localMedia.getPath();
//            }
//
//            LogUtils.e(localFilePath + "," + ossToken.getBucketName() + "," + uploadFileServerName);
//
//            // 構造上傳請求
//            PutObjectRequest put = new PutObjectRequest(ossToken.getBucketName(), uploadFileServerName, localFilePath);
//            // 異步上傳時可以設置進度回調
//            put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//                @Override
//                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                    LogUtils.e("currentSize: " + currentSize + " totalSize: " + totalSize);
//                }
//            });
//
//            //上传图片有时候会卡主很长时间 这里程序判断上传图片8秒内没返回就作为失败处理
//            uploadIsFinish = false;
//            uploadIsFinishHandler.sendEmptyMessageDelayed(1, 20 * 1000);
//            oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//                @Override
//                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                    LogUtils.e("UploadSuccess");
//                    if (uploadIsFinish) {
//                        //说明已经处理 这里就不在处理了
//                        return;
//                    }
//                    uploadIsFinish = true;
//                    uploadIsFinishHandler.removeMessages(1);
//                    uploadImgUrl = oss.presignPublicObjectURL(ossToken.getBucketName(), uploadFileServerName);
//
//                    LogUtils.e("调用成功 云服务器图片地址：imgUrl : " + uploadImgUrl);
//                    uploadImgUrl = uploadImgUrl.replace(ossToken.getBucketName() + "." + ossToken.getEndpoint(), SPManager.getDomain());
//                    LogUtils.e("调用成功 服务器图片地址：imgUrl : " + uploadImgUrl);
//
//                    //已经全部传好 调用接口
//                    requestApi();
//
//                }
//
//                @Override
//                public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                    hideLoadingDialog();
//                    if (uploadIsFinish) {
//                        //说明已经处理 这里就不在处理了
//                        return;
//                    }
//                    uploadIsFinish = true;
//                    uploadIsFinishHandler.removeMessages(1);
//
//                    // 請求異常
//                    if (clientExcepion != null) {
//                        // 本地異常如網絡異常等
//                        showToastTip(false, getString(R.string.netWorkException));
//                    }
//                    if (serviceException != null) {
//                        showToastTip(false, getString(R.string.netWorkException));
//                        // 服務異常
//                        LogUtils.e(serviceException.getErrorCode());
//                        LogUtils.e(serviceException.getRequestId());
//                        LogUtils.e(serviceException.getHostId());
//                        LogUtils.e(serviceException.getRawMessage());
//                    }
//                }
//            });
//        }
//    }
}
