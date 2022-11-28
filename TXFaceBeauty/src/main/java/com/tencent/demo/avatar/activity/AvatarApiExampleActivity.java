package com.tencent.demo.avatar.activity;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.demo.R;
import com.tencent.demo.XmagicApiWrapper;
import com.tencent.demo.activity.CameraActivity;
import com.tencent.demo.avatar.AvatarResManager;
import com.tencent.demo.avatar.AvatarResManager.AvatarBgType;
import com.tencent.demo.avatar.view.AvatarApiExamplePanel;
import com.tencent.demo.camera.camerax.CustomTextureProcessor;
import com.tencent.demo.camera.camerax.GLCameraXView;
import com.tencent.demo.utils.PermissionHandler;
import com.tencent.xmagic.AvatarAction;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.avatar.AvatarData;
import com.tencent.xmagic.listener.UpdateAvatarListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AvatarApiExampleActivity extends AppCompatActivity implements SensorEventListener , UpdateAvatarListener {

    private static final String TAG = AvatarApiExampleActivity.class.getName();

    private XmagicApi mXmagicApi;
    private GLCameraXView cameraXView;
    private AvatarApiExamplePanel avatarPanel;


    private boolean mIsResumed = false;
    private boolean mIsPermissionGranted = false;

    public static final String IS_BACK_CAMERA = "isBackCamera";

    private int expectCameraPreviewWidth;
    private int expectCameraPreviewHeight;

    //判断当前手机旋转方向，用于手机在不同的旋转角度下都能正常的识别人脸
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;


    //模型文件的名字
    private String avatarResName = AvatarResManager.AVATAR_RES_MALE;
    private AvatarBgType currentAvatarType = AvatarBgType.VIRTUAL_BG;

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
        setContentView(R.layout.activity_avatar_api_example_layout);
        expectCameraPreviewWidth = getIntent().getIntExtra(CameraActivity.CameraPreviewConfig.KEY_PREVIEW_WIDTH, 960);
        expectCameraPreviewHeight = getIntent().getIntExtra(CameraActivity.CameraPreviewConfig.KEY_PREVIEW_HEIGHT, 720);

        avatarPanel = findViewById(R.id.avatar_panel);
        cameraXView = findViewById(R.id.camera_view);
        cameraXView.setIsBackCamera(false);
        cameraXView.setCameraSize(new Size(expectCameraPreviewHeight, expectCameraPreviewWidth));

        findViewById(R.id.back_btn).setOnClickListener(v -> finish());
        findViewById(R.id.change_avatar).setOnClickListener(v -> changeAvatarBgType());

        setCustomProcessor();
        // 检查和请求系统权限  check or request camera permission
        mPermissionHandler.start();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        avatarPanel.setAvatarUpdateCallback(avatarConfigUI -> {
            if (mXmagicApi != null && avatarConfigUI != null) {
                AvatarData avatarConfig = avatarConfigUI.avatarData;
                List<AvatarData> avatarConfigList = new ArrayList<>();
                avatarConfigList.add(avatarConfig);
                mXmagicApi.updateAvatar(avatarConfigList,this);
                if(avatarConfigUI.bindAvatarDataList!=null){
                    mXmagicApi.updateAvatar(avatarConfigUI.bindAvatarDataList,this);
                }
            }
        });

        avatarPanel.initViews();
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
                if (mXmagicApi != null) {
                    return mXmagicApi.process(textureId, textureWidth, textureHeight);
                } else {
                    return textureId;
                }
            }

            @Override
            public void onGLContextDestroy() {
                if (mXmagicApi != null) {
                    mXmagicApi.onDestroy();
                    mXmagicApi = null;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsResumed = true;
        tryResume();
        //注册传感器
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void tryResume() {
        if (mIsResumed && mIsPermissionGranted) {
            cameraXView.startPreview();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mIsResumed = false;
        pause();
        //取消传感器
        mSensorManager.unregisterListener(this);
    }

    private void pause() {
        cameraXView.stopPreview();
        if (mXmagicApi != null) {
            mXmagicApi.onPause();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mXmagicApi != null) {
            mXmagicApi.sensorChanged(event, mAccelerometer);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onDestroy() {
        if (cameraXView != null) {
            cameraXView.release();
        }
        super.onDestroy();
    }


    private void initXMagic() {
        if (mXmagicApi == null) {
            String avatarSaveData = com.tencent.xmagic.util.FileUtil.readFile(AvatarResManager.getAvatarConfigsDir() + File.separator + AvatarResManager.getAvatarConfigsFileName(avatarResName));
            mXmagicApi = XmagicApiWrapper.createXmagicApi(getApplicationContext(), false, null);
            AvatarResManager.getInstance().loadAvatarRes(mXmagicApi, avatarResName,avatarSaveData);
            setAvatarType();
        } else {
            mXmagicApi.onResume();
        }
    }


    /**
     * 背景切换
     */
    private void changeAvatarBgType() {
        if (currentAvatarType == AvatarBgType.VIRTUAL_BG) {
            currentAvatarType = AvatarBgType.REAL_BG;
        } else {
            currentAvatarType = AvatarBgType.VIRTUAL_BG;
        }
        setAvatarType();
    }

    private void setAvatarType() {
        AvatarData avatarData = getAvatarTypeConfig(currentAvatarType);
        if (mXmagicApi != null ) {
            List<AvatarData> avatarConfigs = new ArrayList<>();
            avatarConfigs.add(avatarData);
            mXmagicApi.updateAvatar(avatarConfigs,this);
        }
    }


    private AvatarData getAvatarTypeConfig(AvatarBgType avatarBgType) {
        AvatarData avatarData = new AvatarData();
        avatarData.entityName = "plane";
        avatarData.action = AvatarAction.REPLACE.description;
        if (avatarBgType == AvatarBgType.VIRTUAL_BG) {
            avatarData.value = new Gson().fromJson("{\"meshResourceKey\":\"custom_configs/resources/background_plane/1549ef04ca0c5110389a684dfe95ad9a/plane.fmesh\",\"position\":{\"x\":0,\"y\":0.03545668721199035,\"z\":7.872966103936807e-18},\"rotation\":{\"x\":0,\"y\":0,\"z\":0},\"scale\":{\"x\":1,\"y\":1,\"z\":1},\"subMeshConfigs\":[{\"materialResourceKeys\":[\"custom_configs/resources/background_plane/1549ef04ca0c5110389a684dfe95ad9a/plane.fmaterial\"]}]}", JsonObject.class);
        }
        return avatarData;
    }

    @Override
    public void onAvatarDataInvalid(List<AvatarData> failedAvatarDataList) {
        Log.e(TAG,new Gson().toJson(failedAvatarDataList));
    }
}