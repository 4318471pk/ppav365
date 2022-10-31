package com.live.fox.dialog.bottomDialog;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.core.content.FileProvider;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogProfileImageBinding;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.LogUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ToastManage;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class EditProfileImageDialog extends BaseBindingDialogFragment {

    DialogProfileImageBinding mBind;

    public static EditProfileImageDialog getInstance()
    {
        return new EditProfileImageDialog();
    }

    @Override
    public void onClickView(View view) {
        if(ClickUtil.isClickWithShortTime(view.getId(),1000))
        {
            return;
        }

        switch (view.getId())
        {
            case R.id.takePhoto:
                onTakePhoto();
                break;
            case R.id.selectInGallery:
                openPhoto();
                dismissAllowingStateLoss();
                break;
        }

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_profile_image;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        Animation animation= new TranslateAnimation(Animation.ABSOLUTE,0,
                Animation.ABSOLUTE,0
                ,Animation.RELATIVE_TO_PARENT,1f
                ,Animation.RELATIVE_TO_PARENT,0f);
        animation.setDuration(300);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBind.rlMain.setBackgroundColor(0x88000000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBind.llMain.startAnimation(animation);
    }

    public void openPhoto() {
        Constant.isAppInsideClick = true;
        PictureSelector.create(getActivity())
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
//                .enableCrop(false)// 是否裁剪
//                .compress(true)// 是否壓縮
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
                .forResult(PictureConfig.REQUEST_CAMERA);//結果回調onActivityResult code
    }

    public void onTakePhoto() {
        // 启动相机拍照,先判断手机是否有拍照权限
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    startOpenCamera();
                } else {
                    ToastManage.s(getActivity(), getString(com.luck.picture.lib.R.string.picture_camera));
                }
                dismissAllowingStateLoss();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

//    protected void closeActivity() {
//        finish();
//        if (config.camera) {
//            overridePendingTransition(0, com.luck.picture.lib.R.anim.fade_out);
//        } else {
//            overridePendingTransition(0, com.luck.picture.lib.R.anim.a3);
//        }
//    }

    public void startOpenCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                Uri imageUri = parUri(new File(PictureFileUtils.getPicturePath(getActivity())));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getActivity().startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }

    }

    /**
     * 生成uri
     *
     * @param cameraFile
     * @return
     */
    private Uri parUri(File cameraFile) {
        Uri imageUri;
        String authority = getActivity().getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(getActivity(), authority, cameraFile);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        LogUtils.e("返回图片：" , imageUri + "");
        return imageUri;
    }
}
