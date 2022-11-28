package com.tencent.demo.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.tencent.demo.R;
import com.tencent.demo.camera.CameraPreviewManager;
import com.tencent.demo.utils.PermissionHandler;


/**
 * 没有加载美颜的相机预览界面
 */
public class PreviewOnlyActivity extends AppCompatActivity {

    private static final String TAG = "PreviewOnlyActivity";


    private CameraPreviewManager mPreviewMgr;
    private GLSurfaceView mGlSurfaceView;

    private boolean mIsResumed = false;
    private boolean mIsPermissionGranted = false;
    private boolean isOpenXmagic = true;
    //判断当前手机旋转方向，用于手机在不同的旋转角度下都能正常的识别人脸
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_layout);

        // 设置 SurfaceView 宽高比
        mGlSurfaceView = findViewById(R.id.camera_gl_surface_view);
        mPreviewMgr = new CameraPreviewManager();
        mPreviewMgr.onCreate(mGlSurfaceView);
        mPreviewMgr.setCustomTextureProcessor((textureId, textureWidth, textureHeight) -> {
            return textureId;

        });

        // 检查和请求系统权限
        mPermissionHandler.start();
        findViewById(R.id.image_beauty_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpenXmagic = !isOpenXmagic;
            }
        });
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        findViewById(R.id.image_beauty_switch).setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);// 必须有这个调用, mPermissionHandler 才能正常工作
    }

    private final PermissionHandler mPermissionHandler = new PermissionHandler(this) {

        @Override
        protected void onAllPermissionGranted() {
            mIsPermissionGranted = true;
            tryResume();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        mIsResumed = true;
        tryResume();
        //注册传感器
    }

    private void tryResume() {
        if (mIsResumed && mIsPermissionGranted) {
            int expectCameraPreviewWidth = getIntent().getIntExtra(CameraPreviewConfig.KEY_PREVIEW_WIDTH, 1280);
            int expectCameraPreviewHeight = getIntent().getIntExtra(CameraPreviewConfig.KEY_PREVIEW_HEIGHT, 720);
            mPreviewMgr.onResume(this, expectCameraPreviewWidth, expectCameraPreviewHeight);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsResumed = false;
        mPreviewMgr.onPause();
        //取消传感器
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreviewMgr != null) {
            mPreviewMgr.releaseCamera();
        }
    }


    public static class CameraPreviewConfig {

        public static final String KEY_PREVIEW_WIDTH = "KEY_PREVIEW_WIDTH";
        public static final String KEY_PREVIEW_HEIGHT = "KEY_PREVIEW_HEIGHT";
    }
}