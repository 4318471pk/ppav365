package com.live.fox.ui.login;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.databinding.ModifyuserinfoFragmentBinding;
import com.live.fox.utils.BlankController;
import com.live.fox.utils.LogUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.disposables.Disposable;


/**
 * 完善用户资料
 */
public class ModifyUserInfoFragment extends BaseFragment {

    String phone;

    LoginViewModel loginViewModel;
    ModifyuserinfoFragmentBinding mBinding;

    public static ModifyUserInfoFragment newInstance(String phone) {
        ModifyUserInfoFragment fragment = new ModifyUserInfoFragment();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null) {
            mBinding = DataBindingUtil.inflate(inflater, R.layout.modifyuserinfo_fragment, container, false);
            rootView = mBinding.getRoot();
            loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
            mBinding.setViewModel(loginViewModel);
            mBinding.setLifecycleOwner(requireActivity());
            initData(getArguments());
            setView(rootView);
        }
        return rootView;
    }


    public void initData(Bundle bundle) {
        if (bundle != null) {
            phone = bundle.getString("phone");
        }
    }


    public void setView(View view) {
        loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
        loginViewModel.getUsername().setValue(phone);
        mBinding.etPwd.setFilters(new InputFilter[]{new BlankController()});
        mBinding.etCpwd.setFilters(new InputFilter[]{new BlankController()});
        mBinding.rbSex.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rv_sex1) {
                loginViewModel.setSex(1);
            } else {
                loginViewModel.setSex(2);
            }
        });

        mBinding.ivHead.setOnClickListener(view1 -> selPhoto());
    }


    public void selPhoto() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        Disposable subscribe = rxPermissions.request(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        // 所有权限都同意
                        // 进入相册 以下是例子：不需要的api可以不写
                        Constant.isAppInsideClick = true;
                        PictureSelector.create(this)
                                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                                .maxSelectNum(1)// 最大图片选择数量
                                .minSelectNum(1)// 最小选择数量
                                .imageSpanCount(4)// 每行显示个数
                                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                                .previewImage(false)// 是否可预览图片
                                .previewVideo(true)// 是否可预览视频
                                .enablePreviewAudio(true) // 是否可播放音频
                                .isCamera(true)// 是否显示拍照按钮
                                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                                .enableCrop(false)// 是否裁剪
                                .compress(true)// 是否压缩
                                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                                //.compressSavePath(getPath())//压缩图片保存地址
                                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                                .isGif(false)// 是否显示gif图片
                                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                                .openClickSound(false)// 是否开启点击声音
//                        .selectionMedia(gridImageAdapter.getList())// 是否传入已选图片
                                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                                .minimumCompressSize(100)// 小于100kb的图片不压缩
                                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                                //.rotateEnabled(true) // 裁剪是否可旋转图片
                                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                                //.videoQuality()// 视频录制质量 0 or 1
                                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                                //.recordVideoSecond()//录制视频秒数 默认60s
                                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                    } else {
                        // 有的权限被拒绝或被勾选不再提示
                        LogUtils.e("有的权限被拒绝");
                        new AlertDialog.Builder(getActivity())
                                .setCancelable(false)
                                .setMessage(getString(R.string.notePermission))
                                .setPositiveButton(getString(R.string.see), null)
                                .show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
                // 图片选择结果回调
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                for (LocalMedia media : selectList) {
                    LogUtils.e("图片-----》" + media.getPath());
                }

                LocalMedia localMedia = selectList.get(0);
                String localImgPath = localMedia.isCompressed() ? localMedia.getCompressPath() : localMedia.getPath();
                loginViewModel.setLocalMedia(localMedia);
                loginViewModel.setLocalImgPath(localImgPath);

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.white)
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(requireContext())
                        .load(localMedia.getPath())
                        .apply(options)
                        .into(mBinding.ivHead);
                mBinding.ivMinicamera.setVisibility(View.GONE);
                //获取OssToken 开始上传头像
                loginViewModel.getOssTokenAndUploadImg(requireContext());
            }
        }
    }
}

