package com.tencent.demo.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Size;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tencent.demo.PointsView;
import com.tencent.demo.R;
import com.tencent.demo.XMagicImpl;
import com.tencent.demo.camera.camerax.CustomTextureProcessor;
import com.tencent.demo.camera.camerax.GLCameraXView;
import com.tencent.demo.log.LogUtils;
import com.tencent.demo.panel.XmagicPanelDataManager;
import com.tencent.demo.utils.PermissionHandler;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.XmagicProperty;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class CameraXActivity extends AppCompatActivity implements XMagicImpl.XmagicImplCallback {

    private static final String TAG = "CameraXActivity";

    private XMagicImpl mXmagicImpl;
    private GLCameraXView cameraXView;
    private PointsView facePointsView;
    private RelativeLayout panelLayout = null;
    private CheckBox mCheckBox = null;

    private boolean mIsResumed = false;
    private boolean mIsPermissionGranted = false;
    private boolean isSupportBeauty;

    private int selectedSurfaceViewId = COMMON_SURFACE_VIEW_ID;   //1表示全屏的surfaceView 2表示非全屏surfaceView
    private static final int COMMON_SURFACE_VIEW_ID = 1;
    private static final int FLOAT_SURFACE_VIEW_ID = 2;
    public static final String EXTRA_IS_BACK_CAMERA = "extra_is_back_camera";

    private int expectCameraPreviewWidth;
    private int expectCameraPreviewHeight;
    private int floatGLSurfaceViewPreviewWidth = 0;
    private int floatGLSurfaceViewPreviewHeight = 0;

    private final PermissionHandler mPermissionHandler = new PermissionHandler(this) {

        @Override
        protected void onAllPermissionGranted() {
            mIsPermissionGranted = true;
            tryResume();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camerax_activity_layout);
        boolean isBackCamera = getIntent().getBooleanExtra(EXTRA_IS_BACK_CAMERA, true);
        expectCameraPreviewWidth = getIntent().getIntExtra(CameraActivity.CameraPreviewConfig.KEY_PREVIEW_WIDTH, 1280);
        expectCameraPreviewHeight = getIntent().getIntExtra(CameraActivity.CameraPreviewConfig.KEY_PREVIEW_HEIGHT, 720);
        floatGLSurfaceViewPreviewWidth = getResources().getDisplayMetrics().widthPixels / 2;
        floatGLSurfaceViewPreviewHeight = getResources().getDisplayMetrics().heightPixels / 2;

        panelLayout = findViewById(R.id.panel_layout);
        facePointsView = findViewById(R.id.face_points_view);
        cameraXView = findViewById(R.id.camera_view);
        cameraXView.setIsBackCamera(isBackCamera);
        cameraXView.setCameraSize(new Size(expectCameraPreviewHeight, expectCameraPreviewWidth));
        mCheckBox = findViewById(R.id.checkbox_use_back_camera);
        mCheckBox.setChecked(isBackCamera);
        mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cameraXView.switchCamera();
        });
        findViewById(R.id.back_btn).setOnClickListener(v -> finish());
        setCustomProcessor();
        // 检查和请求系统权限  check or request camera permission
        mPermissionHandler.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);// 必须有这个调用, mPermissionHandler 才能正常工作
    }


    private int mTextureWidth, mTextureHeight;

    private void setCustomProcessor() {
        cameraXView.setCustomTextureProcessor(new CustomTextureProcessor() {
            @Override
            public void onGLContextCreated() {
                new Handler(Looper.getMainLooper()).post(() -> createXmagic());
            }

            @Override
            public int onCustomProcessTexture(int textureId, int textureWidth, int textureHeight) {
                if (mIsResumed && isSupportBeauty && mXmagicImpl != null) {
                    mTextureWidth = textureWidth;
                    mTextureHeight = textureHeight;
                    return mXmagicImpl.process(textureId, textureWidth, textureHeight);
                } else {
                    return textureId;
                }
            }

            @Override
            public void onGLContextDestroy() {
                if (mXmagicImpl != null) {
                    mXmagicImpl.onDestroy();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsResumed = true;
        tryResume();
    }

    private void tryResume() {
        if (mIsResumed && mIsPermissionGranted) {
            cameraXView.startPreview();
            if (mXmagicImpl != null) {
                mXmagicImpl.onResume();
            }
        }
    }


    @Override
    public boolean onUpdateProperty(XmagicProperty xmagicProperty) {
        if (xmagicProperty == null) {
            return true;
        }
        if (xmagicProperty.category == XmagicProperty.Category.SEGMENTATION) {
            if ("video_segmentation_transparent_bg".equals(xmagicProperty.id)) {  //点击了透明背景素材 on click transparent bg
                if (selectedSurfaceViewId != FLOAT_SURFACE_VIEW_ID) {
                    changeViewSize(true);
                    selectedSurfaceViewId = FLOAT_SURFACE_VIEW_ID;
                }
            } else if ("video_segmentation_blur_75".equals(xmagicProperty.id)) {    //背景模糊-强  on click blurred background-severe
                if (selectedSurfaceViewId != COMMON_SURFACE_VIEW_ID) {
                    changeViewSize(false);
                    selectedSurfaceViewId = COMMON_SURFACE_VIEW_ID;
                }
            }
        }
        return false;
    }


    private void changeViewSize(boolean toSmall) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) cameraXView.getLayoutParams();
        if (toSmall) {
            layoutParams.width = floatGLSurfaceViewPreviewWidth;
            layoutParams.height = floatGLSurfaceViewPreviewHeight;
            layoutParams.topMargin = 200;
        } else {
            layoutParams.topMargin = 0;
            layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        }
        cameraXView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mXmagicImpl != null) {
            mXmagicImpl.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onPause() {
        mIsResumed = false;
        pause();
        super.onPause();
    }

    private void pause() {
        if (mXmagicImpl != null) {
            mXmagicImpl.onPause();
        }
        cameraXView.stopPreview();
        if (isFinishing()) {
            //由于在某些手机上会出现一种情况：在此页面快速退出又进入的时候，退出的页面的ondestroy还没有执行，新的页面已经打开了，所以将清理的方法放从onDestroy提前至onPause方法中
            XmagicPanelDataManager.getInstance().clearData();
        }
    }


    private void createXmagic(){
        mXmagicImpl = new XMagicImpl(this, panelLayout, this);
        checkSupportBeauty();

        mXmagicImpl.mXmagicApi.setAIDataListener(new XmagicApi.XmagicAIDataListener() {
            @Override
            public void onBodyDataUpdated(Object bodyDataList) {
//                LogUtils.d(TAG, "onBodyDataUpdated");
            }

            @Override
            public void onHandDataUpdated(Object handDataList) {
//                LogUtils.d(TAG, "onHandDataUpdated");
            }

            @Override
            public void onFaceDataUpdated(Object faceDataList) {
                //日志太多，影响性能，注释掉，需要时再打开
//                LogUtils.d(TAG, "onFaceDataUpdated");
            }
        });

        mXmagicImpl.mXmagicApi.setYTDataListener(new XmagicApi.XmagicYTDataListener() {
            @Override
            public void onYTDataUpdate(String data) {
//                LogUtils.d(TAG, "onYTDataUpdate，data=" + data);
                //LogUtils太长，需要时请打开下面的开关，写入文件，便于测试
//                    writeFaceData(data);

                //绘制人脸点位信息，暂时屏蔽，有需要再打开
//                    List<Float> points = parseFacePoints(data);
//                    facePointsView.setPoints(points);
//                    facePointsView.postInvalidate();
            }

            private List<Float> parseFacePoints(String data) {
                if (data == null) {
                    return null;
                }
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(data, JsonObject.class);
                JsonArray array = jsonObject.getAsJsonArray("face_info");
                if (array == null || array.size() == 0) {
                    return null;
                }
                JsonObject face0 = array.get(0).getAsJsonObject();
                JsonArray arrayPoint = face0.getAsJsonArray("face_256_point");
                if (arrayPoint == null || arrayPoint.size() == 0) {
                    return null;
                }

                if (mTextureWidth == 0) {
                    return null;
                }
                //ratioW 和 ratioH 是一样的，用任意一个都可以
                float ratioW = cameraXView.getWidth() * 1.0f / mTextureWidth;
//                    float ratioH = cameraXView.getHeight() * 1.0f / mTextureHeight;
                List<Float> points = new ArrayList<>();
                for (int i = 0; i < arrayPoint.size(); i++) {
                    points.add(arrayPoint.get(i).getAsFloat() * ratioW);
                }
                return points;
            }

            private void writeFaceData(String data) {
                data += "\n";
                String filePath = "/sdcard/onYTDataUpdate.txt";
                LogUtils.d(TAG, "writeFaceData: " + filePath);
                try {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        File dir = new File(file.getParent());
                        dir.mkdirs();
                        file.createNewFile();

                    }
                    FileOutputStream outStream = new FileOutputStream(file, true);
                    outStream.write(data.getBytes());
                    outStream.flush();
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        cameraXView.release();
        super.onDestroy();
    }


    private void checkSupportBeauty() {
        isSupportBeauty = mXmagicImpl.isSupportBeauty();
        if (!isSupportBeauty) {
            findViewById(R.id.tv_not_support_beauty).setVisibility(View.VISIBLE);
        }
    }
}