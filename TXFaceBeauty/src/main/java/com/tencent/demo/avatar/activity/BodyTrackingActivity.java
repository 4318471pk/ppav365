package com.tencent.demo.avatar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Size;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.demo.R;
import com.tencent.demo.XmagicApiWrapper;
import com.tencent.demo.activity.CameraActivity;
import com.tencent.demo.camera.camerax.CustomTextureProcessor;
import com.tencent.demo.camera.camerax.GLCameraXView;
import com.tencent.demo.module.XmagicResParser;
import com.tencent.demo.utils.PermissionHandler;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.XmagicProperty;
import com.tencent.xmagic.XmagicProperty.Category;


public class BodyTrackingActivity extends AppCompatActivity {

    private static final String TAG = "BodyTrackingActivity";

    private XmagicApi xmagicApi;
    private GLCameraXView cameraXView;
    private CheckBox mCheckBox = null;

    private volatile boolean mIsResumed = false;
    private boolean mIsPermissionGranted = false;

    public static final String EXTRA_IS_BACK_CAMERA = "extra_is_back_camera";

    private int expectCameraPreviewWidth;
    private int expectCameraPreviewHeight;

    private String avatarResName;

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
        setContentView(R.layout.body_tracking_activity_layout);
        boolean isBackCamera = getIntent().getBooleanExtra(EXTRA_IS_BACK_CAMERA, false);
        expectCameraPreviewWidth = getIntent().getIntExtra(CameraActivity.CameraPreviewConfig.KEY_PREVIEW_WIDTH, 1280);
        expectCameraPreviewHeight = getIntent().getIntExtra(CameraActivity.CameraPreviewConfig.KEY_PREVIEW_HEIGHT, 720);
        avatarResName = getIntent().getStringExtra("avatar_res_name");

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


    private void setCustomProcessor() {
        cameraXView.setCustomTextureProcessor(new CustomTextureProcessor() {
            @Override
            public void onGLContextCreated() {
                new Handler(Looper.getMainLooper()).post(() -> initXMagic());
            }

            @Override
            public int onCustomProcessTexture(int textureId, int textureWidth, int textureHeight) {
                if (mIsResumed && xmagicApi != null) {
                    return xmagicApi.process(textureId, textureWidth, textureHeight);
                } else {
                    return textureId;
                }
            }

            @Override
            public void onGLContextDestroy() {
                if (xmagicApi != null) {
                    xmagicApi.onDestroy();
                    xmagicApi = null;
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
        }
    }

    @Override
    protected void onPause() {
        mIsResumed = false;
        pause();
        super.onPause();
    }

    private void pause() {
        if (xmagicApi != null) {
            xmagicApi.onPause();
        }
        cameraXView.stopPreview();
    }

    @Override
    protected void onDestroy() {
        cameraXView.release();
        super.onDestroy();
    }


    private void initXMagic() {
        if (xmagicApi == null) {
            xmagicApi = XmagicApiWrapper.createXmagicApi(getApplicationContext(), false, (errorMsg, code) -> {
                Toast.makeText(BodyTrackingActivity.this, "asset error:" + code + "," + errorMsg, Toast.LENGTH_LONG).show();
            });
            XmagicProperty<?> property = new XmagicProperty<>(Category.MOTION, avatarResName,
//                    XmagicResParser.getResPath() + "MotionRes/avatarRes/ch_0715_body_driven", null, null);
                    XmagicResParser.getResPath() + "MotionRes/avatarRes/" + avatarResName, null, null);
            xmagicApi.updateProperty(property);
        } else {
            xmagicApi.onResume();
        }
    }
}