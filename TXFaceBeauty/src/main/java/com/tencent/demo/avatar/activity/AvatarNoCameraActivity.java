package com.tencent.demo.avatar.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.demo.R;
import com.tencent.demo.XMagicImpl;
import com.tencent.demo.XmagicApiWrapper;
import com.tencent.demo.avatar.AvatarResManager;
import com.tencent.demo.avatar.CaptureAvatarDataManager;
import com.tencent.demo.avatar.model.AvatarItem;
import com.tencent.demo.avatar.model.MainTab;
import com.tencent.demo.avatar.model.SubTab;
import com.tencent.demo.avatar.view.AvatarPageInf;
import com.tencent.demo.avatar.view.AvatarPanel;
import com.tencent.demo.avatar.view.AvatarPanelCallBack;
import com.tencent.demo.camera.gltexture.NoCameraGLTextureView;
import com.tencent.demo.download.AvatarDownloadUtil;
import com.tencent.demo.download.FileUtil;
import com.tencent.demo.utils.OnDownloadListener;
import com.tencent.xmagic.AvatarAction;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.avatar.AvatarData;
import com.tencent.xmagic.listener.UpdateAvatarListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class AvatarNoCameraActivity extends AppCompatActivity implements SensorEventListener, UpdateAvatarListener {

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
    private AvatarPanel avatarPanel;
    private TextView switchTxtView;
    private ImageView genderImg;


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
        setContentView(R.layout.activity_avatar_nocamera_layout);
        Intent intent = getIntent();

        bodyMod = intent.getBooleanExtra("bodyMod", false);
        isCaptureMod = intent.getBooleanExtra(CaptureActivity.IS_CAPTURE_KEY, false);

        avatarPanel = findViewById(R.id.avatar_panel);
        noCameraGLTextureView = findViewById(R.id.avatar_texture_view);

//        Bitmap processBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_girl);
//        noCameraTextureView.setProcessBitmap(processBitmap);

        findViewById(R.id.api_btn).setOnClickListener(v -> startActivity(new Intent(this, AvatarApiExampleActivity.class)));
        findViewById(R.id.back_btn).setOnClickListener(v -> finish());
        findViewById(R.id.save_btn).setOnClickListener(v -> saveAvatarConfigs());

        genderImg = findViewById(R.id.gender_change_btn);

        this.initParameter();

        setCustomProcessor();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        avatarPanel.setAvatarPanelCallBack(new AvatarPanelCallBack() {
            @Override
            public void onItemChecked(MainTab mainTab, AvatarItem avatarItem) {
                if (avatarItem.avatarData == null && URLUtil.isNetworkUrl(avatarItem.downloadUrl)) {  //此处表示要进行动态下载
                    downloadAvatarData(avatarItem, () -> updateConfig(avatarItem));
                } else {
                    updateConfig(avatarItem);
                    List<AvatarData> bindAvatarData = AvatarResManager.getAvatarDataByBindData(avatarItem.bindData);
                    mXmagicApi.updateAvatar(bindAvatarData, AvatarNoCameraActivity.this);
                }
            }

            @Override
            public void onItemValueChange(AvatarItem avatarItem) {
                updateConfig(avatarItem);
            }

            @Override
            public boolean onShowPage(AvatarPageInf avatarPageInf, SubTab subTab) {
                if (subTab != null && subTab.items != null && subTab.items.size() > 0) {
                    AvatarItem avatarItem = subTab.items.get(0);
                    if (avatarItem.type == AvatarData.TYPE_SLIDER && avatarItem.avatarData == null && URLUtil.isNetworkUrl(avatarItem.downloadUrl)) {  //此处表示要进行动态下载
                        downloadAvatarData(avatarItem, () -> {
                            if (avatarPageInf != null) {
                                avatarPageInf.refresh();
                            }
                        });
                        return false;
                    }
                }
                return true;
            }

            private void updateConfig(AvatarItem avatarItem) {
                if (mXmagicApi != null && avatarItem != null) {
                    List<AvatarData> avatarConfigList = new ArrayList<>();
                    avatarConfigList.add(avatarItem.avatarData);
                    mXmagicApi.updateAvatar(avatarConfigList, AvatarNoCameraActivity.this);
                }
            }
        });

        AvatarResManager.getInstance().getAvatarData(avatarResName, getAvatarConfig(),  allData -> {
            //设置数据给捏脸面板
            avatarPanel.initView(allData);
        });

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
            findViewById(R.id.api_btn).setVisibility(View.GONE);
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
            getLastAvatarTypeConfig();
            findViewById(R.id.change_avatar).setOnClickListener(v -> changeAvatarBgType());
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
                initXMagic();
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
            AvatarResManager.getInstance().loadAvatarRes(mXmagicApi, avatarResName, getAvatarConfig());
            setAvatarPlaneType();
        }
    }

    /**
     * 保存用户设置的属性或者默认属性值
     */
    private void saveAvatarConfigs() {
        if (mXmagicApi != null && avatarPanel != null) {
            List<AvatarData> avatarDataList = AvatarResManager.getUsedAvatarData(avatarPanel.getMainTabList());
            new Thread(() -> {
                String content = XmagicApi.exportAvatar(avatarDataList);
                boolean result = FileUtil.writeContentToFile(AvatarResManager.getAvatarConfigsDir(), AvatarResManager.getAvatarConfigsFileName(avatarResName), content);
                String tip = result ? "save avatar data successfully " : "Save avatar data failed";
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_LONG).show());
            }).start();
        }
    }


    /**
     * 背景切换
     */
    private void changeAvatarBgType() {
        if (currentAvatarType == AvatarResManager.AvatarBgType.VIRTUAL_BG) {
            currentAvatarType = AvatarResManager.AvatarBgType.REAL_BG;
        } else {
            currentAvatarType = AvatarResManager.AvatarBgType.VIRTUAL_BG;
        }
        saveCurrentAvatarType();
        setAvatarPlaneType();
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
        avatarPanel.cleanView();
        //加载新的素材资源
        AvatarResManager.getInstance().loadAvatarRes(mXmagicApi, avatarResName, getAvatarConfig());
        //面板加载新数据
        AvatarResManager.getInstance().getAvatarData(avatarResName, getAvatarConfig(),  allData -> {
            avatarPanel.initView(allData);
        });
    }


    /**
     * 用于下载avatar配置信息
     *
     * @param avatarItem
     */
    private void downloadAvatarData(AvatarItem avatarItem, onDownloadAvatarDataCallback downloadAvatarDataCallback) {
        Toast.makeText(XMagicImpl.applicationContext, "start download", Toast.LENGTH_LONG).show();
        String[] names = avatarItem.downloadUrl.split("/");
        String fileName = names[names.length - 1];
        AvatarDownloadUtil.download(avatarItem.downloadUrl, fileName, new OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String directory) {
                Pair<Integer, List<AvatarData>> result = XmagicApi.addAvatarResource(AvatarResManager.getInstance().getAvatarResPath() + avatarResName, avatarItem.category, directory + File.separator + fileName);
                if (result.first == XmagicApi.AvatarActionErrorCode.OK) {
                    List<AvatarData> avatarDataList = result.second;
                    if (avatarDataList != null && !avatarDataList.isEmpty()) {
                        AvatarData newAvatarData = getAvatarData(avatarDataList, avatarItem.id, avatarItem.avatarData);
                        if (newAvatarData != null) {
                            avatarItem.selected = newAvatarData.selected;
                            avatarItem.type = newAvatarData.type;
                            avatarItem.avatarData = newAvatarData;
                            downloadAvatarDataCallback.onSuccess();
                            return;
                        }
                    }
                    Toast.makeText(XMagicImpl.applicationContext, "error parsing the download file ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(XMagicImpl.applicationContext, "add downloadFile error  " + result, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onDownloadFailed(int errorCode) {
                Toast.makeText(XMagicImpl.applicationContext, "download failed，errorCode = " + errorCode, Toast.LENGTH_LONG).show();
            }
        });
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

    //保存背景类型数据
    private void saveCurrentAvatarType() {
        getSharedPreferences().edit().putBoolean("isVirtual", AvatarResManager.AvatarBgType.VIRTUAL_BG == currentAvatarType).commit();
    }

    //获取性别类型数据
    private void getLastGenderConfig() {
        boolean isMale = getSharedPreferences().getBoolean("isMale", true);
        currentAvatarGender = isMale ? AvatarResManager.AvatarGender.MALE : AvatarResManager.AvatarGender.FEMALE;
    }

    //获取背景类型数据
    private void getLastAvatarTypeConfig() {
        boolean isVirtual = getSharedPreferences().getBoolean("isVirtual", true);
        currentAvatarType = isVirtual ? AvatarResManager.AvatarBgType.VIRTUAL_BG : AvatarResManager.AvatarBgType.REAL_BG;
    }

}