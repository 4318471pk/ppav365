package com.tencent.demo.avatar.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.demo.R;
import com.tencent.demo.XmagicApiWrapper;
import com.tencent.demo.avatar.AvatarResManager;
import com.tencent.demo.avatar.CaptureAvatarDataManager;
import com.tencent.demo.avatar.audio.AudioCapturer;
import com.tencent.demo.camera.gltexture.NoCameraGLTextureView;
import com.tencent.demo.module.XmagicResParser;
import com.tencent.demo.utils.AppUtils;
import com.tencent.xmagic.AvatarAction;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.XmagicProperty;
import com.tencent.xmagic.audio2exp.Audio2ExpApi;
import com.tencent.xmagic.avatar.AvatarData;
import com.tencent.xmagic.listener.UpdateAvatarListener;
import com.tencent.xmagic.listener.UpdatePropertyListener;
import com.tencent.xmagic.util.FileUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Audio2ExpActivity extends AppCompatActivity implements SensorEventListener, UpdateAvatarListener,
        AudioCapturer.OnAudioFrameCapturedListener{

    private static final String TAG = "AvatarActivity";

    //是否全身场景
    private boolean bodyMod = false;
    private boolean isCaptureMod = false;

    //处理触模事件，旋转角色
    private float touchStartX = 0;
    //默认从0度开始旋转，如果素材修改了角度，这里也要对应修改
    private int avatarRotationX = 0;
    //全身：false，半身：true
    private boolean zoomCamera = false;

    private XmagicApi mXmagicApi;
    private NoCameraGLTextureView noCameraGLTextureView;
    private TextView switchTxtView;
    private ImageView genderImg;
    private Button btnRecord;
    private AudioCapturer mAudioCapturer;


    //判断当前手机旋转方向，用于手机在不同的旋转角度下都能正常的识别人脸
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private final Gson gson = new Gson();

    //模型文件的名字
    private String avatarResName = "";
    private AvatarResManager.AvatarBgType currentAvatarType = AvatarResManager.AvatarBgType.VIRTUAL_BG;

    private AvatarResManager.AvatarGender currentAvatarGender = AvatarResManager.AvatarGender.MALE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio2exp_layout);
        Intent intent = getIntent();

        bodyMod = intent.getBooleanExtra("bodyMod", false);
        isCaptureMod = intent.getBooleanExtra(CaptureActivity.IS_CAPTURE_KEY, false);

        noCameraGLTextureView = findViewById(R.id.avatar_texture_view);

        findViewById(R.id.back_btn).setOnClickListener(v -> finish());

        genderImg = findViewById(R.id.gender_change_btn);

        this.initParameter();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btnRecord = findViewById(R.id.btn_record);
        btnRecord.setOnClickListener(v -> onRecordClicked());
        mAudioCapturer = new AudioCapturer();
        mAudioCapturer.setOnAudioFrameCapturedListener(this);

        initAudio2ExpResource();

        setCustomProcessor();
    }

    //把模型文件从assets目录copy到私有目录（XmagicResParser.getResPath() + audio2exp）。 copy model files from assets to APP's private directory(XmagicResParser.getResPath() + audio2exp)
    //
    private void initAudio2ExpResource() {
        String appVersionName = AppUtils.getAppVersionName(this);

        String assetsCopiedVersion = this.getSharedPreferences("demo_settings", MODE_PRIVATE).getString("audio2exp_assets_copied", "");
        if (assetsCopiedVersion != appVersionName) {
            boolean copySuccess = FileUtil.copyAssets(this, "audio2exp", XmagicResParser.getResPath() + "audio2exp");
            if (copySuccess) {
                this.getSharedPreferences("demo_settings", MODE_PRIVATE).edit().putString("audio2exp_assets_copied", appVersionName).commit();
            }
        }
    }

    private void dynamicLoadLibs() {
        //如果so是动态下载的，请在此动态加载so
        // If your .so files are downloaded from internet, please load it here with downloaded file path
        if (isCpuV8a()) {
            loadLib(XmagicResParser.getResPath() + "audio2exp/arm64-v8a/", "YTCommonXMagic");
            loadLib(XmagicResParser.getResPath() + "audio2exp/arm64-v8a/", "audio2exp");
        } else {
            loadLib(XmagicResParser.getResPath() + "audio2exp/armeabi-v7a/", "YTCommonXMagic");
            loadLib(XmagicResParser.getResPath() + "audio2exp/armeabi-v7a/", "audio2exp");
        }
    }

    private boolean isCpuV8a() {
        String cpuType = null;
        try {
            cpuType = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(
                    "getprop ro.product.cpu.abi").getInputStream())).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cpuType != null && cpuType.contains("arm64-v8a")) {
            return true;
        }
        return false;
    }

    private static boolean loadLib(String path, String libName) {
        boolean loadSuccess;
        try {
            System.load(path + "lib"+libName+".so");
            loadSuccess = true;
        } catch (UnsatisfiedLinkError error){
            loadSuccess = false;
        }
        if(loadSuccess){
            return true;
        }

        try {
            System.loadLibrary(libName);
            loadSuccess = true;
        } catch (UnsatisfiedLinkError error){
            loadSuccess = false;
        }
        return loadSuccess;
    }

    private boolean audio2ExpInited = false;
    private Audio2ExpApi audio2Exp = null;
    private void onRecordClicked() {
        if (mXmagicApi == null) {
            return;
        }
        if (audio2Exp == null) {
            audio2Exp = new Audio2ExpApi();
        }
        //如果so是内置在APK里的，在new Audio2ExpApi()时，就会加载语音转表情的so
        //如果so是动态下载，则调用 dynamicLoadLibs() 加载so
//        dynamicLoadLibs();
        if (mAudioCapturer.isCaptureStarted()) {
            mAudioCapturer.stopCapture();
            btnRecord.setText(getResources().getString(R.string.avatar_voice_start));
            audio2Exp.release();
            audio2ExpInited = false;
        } else {
            if (!audio2ExpInited) {
                int initResult = audio2Exp.init(XmagicResParser.getResPath());
                if (initResult != 0) {
                    Toast.makeText(this, "init audio2expression failed，errorCode="+initResult, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(this, "init audio2expression success", Toast.LENGTH_SHORT).show();
                }
                audio2ExpInited = true;
            }
            boolean startSuccess = mAudioCapturer.startCapture();
            if (startSuccess) {
                btnRecord.setText(getResources().getString(R.string.avatar_voice_stop));
            } else {
                Toast.makeText(this, "start capture failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAudioFrameCaptured(float[] audioData) {
        if (mXmagicApi != null) {
            float[] audioExpressionData = audio2Exp.parseAudio(audioData);
            mXmagicApi.updateAvatarByExpression(audioExpressionData);
        }
    }

    private String getAvatarConfig() {
        String avatarSaveData = null;
        if (isCaptureMod) {
            avatarSaveData = CaptureAvatarDataManager.getInstance().getMatchData();
        } else {
            avatarSaveData = com.tencent.xmagic.util.FileUtil.readFile(AvatarResManager.getAvatarConfigsDir() + File.separator + AvatarResManager.getAvatarConfigsFileName(avatarResName));
        }
        return avatarSaveData;
    }


    private void initParameter() {
        if (bodyMod) {
            avatarResName = AvatarResManager.AVATAR_RES_BODY;
            //全身场景，切换按钮改为全身和半身的切换
            switchTxtView = findViewById(R.id.change_avatar_text);
            switchTxtView.setText(zoomCamera ? getString(R.string.whole_body) : getString(R.string.half_body));
            findViewById(R.id.change_avatar).setOnClickListener(v -> zoomCamera());
            //暂时隐藏切换性别按钮
            genderImg.setVisibility(View.GONE);
        } else {
            if (isCaptureMod) {
                genderImg.setVisibility(View.GONE);
                avatarResName = getIntent().getStringExtra(CaptureActivity.AVATAR_RES_NAME);
            } else {
                getLastGenderConfig();
                changeGenderUI();
                genderImg.setOnClickListener(v -> {
                    if (currentAvatarGender == AvatarResManager.AvatarGender.MALE) {
                        currentAvatarGender = AvatarResManager.AvatarGender.FEMALE;
                        avatarResName = AvatarResManager.AVATAR_RES_FEMALE;
                    } else {
                        currentAvatarGender = AvatarResManager.AvatarGender.MALE;
                        avatarResName = AvatarResManager.AVATAR_RES_MALE;
                    }
                    changeGenderUI();
                    saveCurrentAvatarGender();
                    onChangeAvatarGender();
                });
                avatarResName = currentAvatarGender == AvatarResManager.AvatarGender.MALE ? AvatarResManager.AVATAR_RES_MALE : AvatarResManager.AVATAR_RES_FEMALE;
            }
        }
    }

    //修改界面上的男女图标
    private void changeGenderUI() {
        if (currentAvatarGender == AvatarResManager.AvatarGender.MALE) {
            genderImg.setImageResource(R.mipmap.avatar_female_icon);
        } else {
            genderImg.setImageResource(R.mipmap.avatar_male_icon);
        }
    }

    //调整角色X轴旋转角度
    private void updateAvatarRotation(float delta) {
        if (mXmagicApi != null) {
            //每次最大旋转15度
            if (delta > 15) {
                delta = 15;
            }
            if (delta < -15) {
                delta = -15;
            }
            avatarRotationX += delta;
            if (avatarRotationX > 360 || avatarRotationX < -360) {
                avatarRotationX = 0;
            }
            AvatarData avatarData = createAvatarData("avatar", AvatarAction.BASIC_TRANSFORM, "{\"rotation\": {\"x\": 0,\"y\": " + avatarRotationX + ",\"z\": 0}}");
            List<AvatarData> avatarConfigList = new ArrayList<>();
            avatarConfigList.add(avatarData);
            mXmagicApi.updateAvatar(avatarConfigList, this);
        }
    }

    /*
     * 半身/全身切换，调整相机位置
     * */
    private void zoomCamera() {
        if (mXmagicApi != null) {
            zoomCamera = !zoomCamera;
            float y = zoomCamera ? 1.7f : 1.2f;
            float z = zoomCamera ? -0.465f : 1.0f;

            AvatarData avatarData = createAvatarData("Camera3D", AvatarAction.BASIC_TRANSFORM, "{\"position\": {\"x\": 0,\"y\":" + y + ",\"z\": " + z + "}}");
            List<AvatarData> avatarConfigList = new ArrayList<>();
            avatarConfigList.add(avatarData);
            mXmagicApi.updateAvatar(avatarConfigList, this);
            switchTxtView.setText(zoomCamera ? getString(R.string.whole_body) : getString(R.string.half_body));
        }
    }


    private AvatarData createAvatarData(String entityName, AvatarAction avatarAction, String jsonValue) {
        AvatarData avatarData = new AvatarData();
        avatarData.entityName = entityName;
        avatarData.action = avatarAction.description;
        avatarData.value = gson.fromJson(jsonValue, JsonObject.class);
        return avatarData;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (bodyMod) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchStartX = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float currentX = event.getX();
                    float touchDeltaX = event.getX() - touchStartX;
                    touchStartX = currentX;
                    //TODO 转到事件队列处理?
                    updateAvatarRotation(touchDeltaX);
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(TAG, "ACTION_UP");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.i(TAG, "ACTION_CANCEL");
                    break;
            }
        }
        return true;
    }


    private void setCustomProcessor() {
        noCameraGLTextureView.setGlTextureProcessListener(new NoCameraGLTextureView.GLTextureProcessListener() {
            @Override
            public int onTextureCustomProcess(int textureId, int width, int height) {
                if (mXmagicApi != null) {
                    return mXmagicApi.process(textureId, width, height);
                } else {
                    return textureId;
                }
            }

            @Override
            public void onTextureCreate() {
                new Handler(Looper.getMainLooper()).post(()->{initXMagic();});
            }

            @Override
            public void onTextureDestroyed() {
                Log.e(TAG, "onTextureDestroyed  " + Thread.currentThread().getName());
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
        if (mXmagicApi != null) {
            mXmagicApi.onResume();
        }
        noCameraGLTextureView.start();
        //注册传感器
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mAudioCapturer.isCaptureStarted()) {
            mAudioCapturer.stopCapture();
            audio2Exp.release();
            btnRecord.setText(getResources().getString(R.string.avatar_voice_start));
            audio2ExpInited = false;
        }
        noCameraGLTextureView.pause();
        if (mXmagicApi != null) {
            mXmagicApi.onPause();
        }
        //取消传感器
        mSensorManager.unregisterListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noCameraGLTextureView.release();
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


    private void initXMagic() {
        if (mXmagicApi == null) {
            mXmagicApi = XmagicApiWrapper.createXmagicApi(getApplicationContext(), false, null);
            AvatarResManager.getInstance().loadAvatarRes(mXmagicApi, avatarResName, getAvatarConfig(), new UpdatePropertyListener() {
                @Override
                public void onAvatarCustomConfigParsingFailed(List<XmagicProperty<?>> parsingFailedList) {

                }

                @Override
                public void onPropertyInvalid(List<XmagicProperty<?>> invalidList) {

                }

                @Override
                public void onPropertyNotSupport(List<XmagicProperty<?>> notSupportList) {

                }

                @Override
                public void onAvatarDataInvalid(List<AvatarData> failedAvatarDataList) {

                }

                @Override
                public void onAssetLoadFinish(String resPath, boolean success) {
                   new Handler(Looper.getMainLooper()).post(() -> {
                       if (bodyMod) {
                           zoomCamera();
                       }
                   });
                }
            });
            setAvatarPlaneType();
        }
    }

    /**
     * 设置模型背景类型
     */
    private void setAvatarPlaneType() {
        AvatarData avatarData = AvatarResManager
                .getInstance().getAvatarPlaneTypeConfig(avatarResName, currentAvatarType);
        if (mXmagicApi != null && avatarData != null) {
            List<AvatarData> avatarDataList = new ArrayList<>();
            avatarDataList.add(avatarData);
            mXmagicApi.updateAvatar(avatarDataList, this);
        }
    }

    /**
     * 切换性别,因为切换性别相当于是切换素材
     */
    private void onChangeAvatarGender() {
        //加载新的素材资源
        AvatarResManager.getInstance().loadAvatarRes(mXmagicApi, avatarResName, getAvatarConfig());
    }


    private AvatarData getAvatarData(List<AvatarData> avatarDataList, String id, AvatarData oldAvatarData) {
        AvatarData newAvatarData = null;
        for (AvatarData avatarData : avatarDataList) {
            if (avatarData.id.equals(id)) {
                newAvatarData = avatarData;
                break;
            }
        }
        if (newAvatarData == null) {
            return null;
        }
        if (newAvatarData.type == AvatarData.TYPE_SLIDER) {
            if (newAvatarData.value == null) {
                return newAvatarData;
            }
            //需要将久的value设置过来
            if (oldAvatarData != null && oldAvatarData.value != null) {
                Set<String> keySet = oldAvatarData.value.keySet();
                for (String key : keySet) {
                    newAvatarData.value.addProperty(key, oldAvatarData.value.get(key).getAsFloat());
                }
            }
        }
        return newAvatarData;
    }

    @Override
    public void onAvatarDataInvalid(List<AvatarData> failedAvatarDataList) {
        Log.e(TAG, gson.toJson(failedAvatarDataList));
    }

    public interface onDownloadAvatarDataCallback {
        void onSuccess();
    }


    private SharedPreferences getSharedPreferences() {
        return getSharedPreferences("avatar_activity_setting", Context.MODE_PRIVATE);
    }

    //保存性别类型数据
    private void saveCurrentAvatarGender() {
        getSharedPreferences().edit().putBoolean("isMale", AvatarResManager.AvatarGender.MALE == currentAvatarGender).commit();
    }



    //获取性别类型数据
    private void getLastGenderConfig() {
        boolean isMale = getSharedPreferences().getBoolean("isMale", true);
        currentAvatarGender = isMale ? AvatarResManager.AvatarGender.MALE : AvatarResManager.AvatarGender.FEMALE;
    }



}