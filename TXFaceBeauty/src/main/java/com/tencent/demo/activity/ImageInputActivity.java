package com.tencent.demo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tencent.demo.R;
import com.tencent.demo.XMagicImpl;
import com.tencent.demo.XmagicApiWrapper;
import com.tencent.demo.gles.SimpleGLThread;
import com.tencent.demo.panel.XmagicPanelDataManager;
import com.tencent.demo.panel.XmagicPanelView;
import com.tencent.demo.utils.PermissionHandler;
import com.tencent.demo.utils.UriUtils;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.XmagicProperty;

import java.util.List;

public class ImageInputActivity extends AppCompatActivity {

    private static final String TAG = "ImageInputActivity";
    private XmagicApi mXmagicApi;
    private XmagicPanelView xmagicPanelView;
    private SimpleGLThread mHandler;
    private volatile Bitmap bitmapResult;
    private Bitmap bitmapOri;
    private boolean isSupportBeauty;
    private boolean mIsResumed = false;
    private boolean mIsPermissionGranted = false;
    private boolean isOpenXmagic = true;
    private ImageView img_input;
    private RelativeLayout panelLayout = null;

    private final PermissionHandler mPermissionHandler = new PermissionHandler(this) {
        @Override
        protected void onAllPermissionGranted() {
            mIsPermissionGranted = true;
            tryResume();
        }
    };


    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_input);
        initViews();
        initData();
    }

    private void initViews() {
        panelLayout = findViewById(R.id.panel_layout);
        img_input = findViewById(R.id.image_input_img);
        findViewById(R.id.icon_head).setOnClickListener(v -> {
            Object tag = v.getTag();
            if (tag instanceof String && "head".equals(tag)) {
                bitmapOri = BitmapFactory.decodeResource(getResources(), R.mipmap.test_girl);
                v.setTag("body");
                ((ImageView) v).setImageResource(R.mipmap.xmagic_body_icon);
            } else {
                bitmapOri = BitmapFactory.decodeResource(getResources(), R.mipmap.model_whole_body);
                v.setTag("head");
                ((ImageView) v).setImageResource(R.mipmap.xmagic_head_icon);
            }
            bitmapResult = null;
            revertBeauty();
        });
        findViewById(R.id.open_img).setOnClickListener(v -> pickImg());
        findViewById(R.id.back_btn).setOnClickListener(v -> finish());
    }

    private void processAndShowBitmap(){
        if (bitmapOri == null) {
            return;
        }
        mHandler.postJob(() -> {
            if (isOpenXmagic) {
                bitmapResult = mXmagicApi.process(bitmapOri, true);
            }
            runOnUiThread(() -> {
                img_input.setImageBitmap(isOpenXmagic ? bitmapResult : bitmapOri);
            });
        });
    }


    private void initData() {
        initOpenGL();
        if (bitmapOri == null) {
            bitmapOri = BitmapFactory.decodeResource(getResources(), R.mipmap.test_girl);
            // 检查和请求系统权限    check or request permission
            mPermissionHandler.start();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);// 必须有这个调用, mPermissionHandler 才能正常工作
    }


    @Override
    protected void onResume() {
        super.onResume();
        mIsResumed = true;
        tryResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mXmagicApi != null) {
            mXmagicApi.onPause();
        }
        mIsResumed = false;
    }

    private void tryResume() {
        if (mIsResumed && mIsPermissionGranted) {
            initXmagicApi();
            mXmagicApi.onResume();
            processAndShowBitmap();
        } else {
            Log.d(TAG, "tryResume: mIsResumed=" + mIsResumed + ",mIsPermissionGranted=" + mIsPermissionGranted);
        }
    }

    private void initOpenGL() {
        mHandler = new SimpleGLThread(null, "OffscreenRender", 720, 1280, null);
        mHandler.waitDone();
    }

    private XmagicApi.OnXmagicPropertyErrorListener xmagicListener = (errorMsg, code) -> {
        Log.d(TAG, "OnXmagicPropertyErrorListener,code=" + code + ",msg=" + errorMsg);
    };

    private void initXmagicApi() {
        if (mXmagicApi != null) {
            return;
        }
        mXmagicApi = XmagicApiWrapper.createXmagicApi(this.getApplicationContext(),true, xmagicListener);
        mXmagicApi.setAudioMute(true);
        isSupportBeauty = mXmagicApi.isSupportBeauty();
        if (!isSupportBeauty) {
            findViewById(R.id.tv_not_support_beauty).setVisibility(View.VISIBLE);
        }
        mXmagicApi.setXmagicStreamType(XmagicApi.PROCESS_TYPE_PICTURE_DATA);
        XmagicApiWrapper.checkBeautyAuth(mXmagicApi, () -> initPropertyUiPanel());
    }

    private void initPropertyUiPanel() {
        if (xmagicPanelView == null) {
            xmagicPanelView = new XmagicPanelView(this);
            xmagicPanelView.setOnUserUpdatePropertyListener(new XmagicPanelView.PanelViewCallBack() {
                @Override
                public void onUserUpdateProperty(XmagicProperty<?> xmagicProperty) {
                    mXmagicApi.updateProperty(xmagicProperty);
                    processAndShowBitmap();
                }

                @Override
                public void onClickCustomSegItem() {
                    XmagicPanelView.openPhotoAlbum(ImageInputActivity.this);
                }

                @Override
                public void onRevertBtnClick() {
                   revertBeauty();
                }

                @Override
                public void onBeautySwitchCheckedChanged(boolean isChecked) { //美颜总开关
                    isOpenXmagic = isChecked;
                    if (isOpenXmagic) {
                        mXmagicApi.onResume();
                        processAndShowBitmap();
                    } else {
                        mXmagicApi.onPause();
                        img_input.setImageBitmap(bitmapOri);
                    }
                }

                @Override
                public void onBeautyCloseOrOpen(boolean isOpen) { //美颜开关
                    List<XmagicProperty<?>> updateList = null;
                    if (!isOpen) {
                        updateList = XmagicPanelDataManager.getInstance().getCloseBeautyItems();
                    } else {
                        updateList = XmagicPanelDataManager.getInstance().getOpenBeautyItems();
                    }
                    if (mXmagicApi != null && updateList != null && updateList.size() > 0) {
                        mXmagicApi.updateProperties(updateList);
                    }
                    processAndShowBitmap();
                }
            });
            panelLayout.addView(xmagicPanelView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadImgFromActivityResult(requestCode, resultCode, data);
        if (xmagicPanelView != null) {
            xmagicPanelView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {

        if (mHandler != null) {
            mHandler.destroy(() -> {
                if (mXmagicApi != null) {
                    mXmagicApi.onPause();
                    mXmagicApi.onDestroy();
                }
            });
            mHandler.waitDone();
        }

        XmagicPanelDataManager.getInstance().clearData();
        super.onDestroy();
    }


    private void pickImg() {
        //通过系统的文件浏览器选择一个文件，pick image
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, REQUEST_CODE); // 打开相册，选择图片
    }


    private void loadImgFromActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (RESULT_OK == resultCode && requestCode == REQUEST_CODE) {
            if (data != null) {
                Uri uri = data.getData();     // 获取选择文件Uri   get uri
                String path = UriUtils.getFilePathByUri(this,uri);
                new Thread(() -> {
                    bitmapOri = BitmapFactory.decodeFile(path);
                    runOnUiThread(() -> {
                        bitmapResult = null;
                        revertBeauty();
                    });
                }).start();
            }
        }
    }

    /**
     * 还原美颜设置
     * revert beauty settings
     */
    private void revertBeauty(){
        if(mXmagicApi!=null){
            List<XmagicProperty<?>> data =XmagicPanelDataManager.getInstance().getRevertXmagicData();
            mXmagicApi.updateProperties(data);
            xmagicPanelView.revertMenuList();
            processAndShowBitmap();
        }
    }





}