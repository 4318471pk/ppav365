package com.tencent.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tencent.demo.module.XmagicResParser;
import com.tencent.demo.panel.XmagicPanelDataManager;
import com.tencent.demo.panel.XmagicPanelView;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.XmagicConstant;
import com.tencent.xmagic.XmagicConstant.FeatureName;
import com.tencent.xmagic.XmagicProperty;
import com.tencent.xmagic.telicense.TELicenseCheck;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class XMagicImpl implements SensorEventListener {

    private static final String TAG = "XMagicImpl";

    public static final String mXMagicKey = "588574635d12a182671b999030910209" ;
    public static final String mXMagicLicenceUrl = "https://license.vod-control.com/license/v2/1313381501_1/v_cube.license" ;


    public XmagicApi mXmagicApi;
    //判断当前手机旋转方向，用于手机在不同的旋转角度下都能正常的识别人脸
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private boolean isOpenXmagic = true;

    private XmagicPanelView xmagicPanelView = null;

    private boolean isOpenPhotoAlbum = false;

    public static Context applicationContext = null;

    private XmagicImplCallback xmagicImplCallback = null;

    private boolean isOpenBeauty = true;

    /**
     * 使用美颜之前必须先调用此方法
     *
     * @param context
     */
    public static void init(Context context) {
        applicationContext = context.getApplicationContext();
    }

    /**
     * 进行美颜授权检验,注：调用此方法之前要保证ini方法已被调用
     *
     * @param teLicenseCheckListener
     */
    public static void checkAuth(TELicenseCheck.TELicenseCheckListener teLicenseCheckListener, boolean callbackOnMainThread) {                         //
        if (applicationContext == null) {
            throw new RuntimeException("please init XMagicImpl init()");
        }
        if (teLicenseCheckListener == null) {
            TELicenseCheck.getInstance().setTELicense(applicationContext, mXMagicLicenceUrl, mXMagicKey, null);
        } else {
            if (callbackOnMainThread) {
                TELicenseCheck.getInstance().setTELicense(applicationContext, mXMagicLicenceUrl, mXMagicKey, (i, s) -> new Handler(Looper.getMainLooper()).post(() -> {
                    teLicenseCheckListener.onLicenseCheckFinish(i, s);
                }));
            } else {
                TELicenseCheck.getInstance().setTELicense(applicationContext, mXMagicLicenceUrl, mXMagicKey, teLicenseCheckListener);
            }
        }
    }

    /**
     * 创建美颜对象
     *
     * @param activity
     * @param panelLayout
     */
    public XMagicImpl(Activity activity, RelativeLayout panelLayout, XmagicImplCallback callback) {
        xmagicImplCallback = callback;
        mXmagicApi = XmagicApiWrapper.createXmagicApi(activity.getApplicationContext(), true, (errorMsg, code) -> {
            //加载特效异常
            activity.runOnUiThread(() -> {
                if (xmagicPanelView != null) {
                    xmagicPanelView.showErrorMsg(code + ":" + errorMsg);
                } else {
                    Toast.makeText(activity.getApplicationContext(), code + ":" + errorMsg, Toast.LENGTH_LONG).show();
                }
            });
        });
        XmagicApiWrapper.checkBeautyAuth(mXmagicApi, () -> {
            if (activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            initPropertyUiPanel(activity, panelLayout);
        });
        //是否开启animoji表情检测开关，默认关。
        //mXmagicApi.setFeatureEnableDisable(FeatureName.ANIMOJI_52_EXPRESSION, true);
        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void initPropertyUiPanel(Activity activity, RelativeLayout panelLayout) {
        if (xmagicPanelView != null) {
            return;
        }
        xmagicPanelView = new XmagicPanelView(panelLayout.getContext());
        xmagicPanelView.setOnUserUpdatePropertyListener(new XmagicPanelView.PanelViewCallBack() {
            @Override
            public void onUserUpdateProperty(XmagicProperty<?> xmagicProperty) {
                updateProperty(xmagicProperty);
            }

            @Override
            public void onClickCustomSegItem() {
                isOpenPhotoAlbum = true;
                XmagicPanelView.openPhotoAlbum(activity);
            }

            @Override
            public void onRevertBtnClick() {
                if (mXmagicApi != null) {
                    mXmagicApi.updateProperties(XmagicPanelDataManager.getInstance().getRevertXmagicData());
                }
                if (xmagicPanelView != null) {
                    xmagicPanelView.revertMenuList();
                }
            }

            @Override
            public void onBeautySwitchCheckedChanged(boolean isChecked) {
                isOpenXmagic = isChecked;
                if (mXmagicApi != null) {
                    if (isOpenXmagic) {
                        mXmagicApi.onResume();
                    } else {
                        mXmagicApi.onPause();
                    }
                }
            }

            @Override
            public void onBeautyCloseOrOpen(boolean isOpen) {
                isOpenBeauty = isOpen;
                List<XmagicProperty<?>> updateList = null;
                if (!isOpen) {
                    updateList = XmagicPanelDataManager.getInstance().getCloseBeautyItems();
                } else {
                    updateList = XmagicPanelDataManager.getInstance().getOpenBeautyItems();
                }
                if (mXmagicApi != null && updateList != null && updateList.size() > 0) {
                    mXmagicApi.updateProperties(updateList);
                }
            }

        });
        panelLayout.addView(xmagicPanelView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 处理 视频/摄像头 每一帧数据
     *
     * @param textureId
     * @param width
     * @param height
     * @return
     */
    public int process(int textureId, int width, int height) {
        if (mXmagicApi != null && isOpenXmagic) {
            return mXmagicApi.process(textureId, width, height);
        }
        return textureId;
    }


    private void updateProperty(XmagicProperty<?> xmagicProperty) {
        if (xmagicImplCallback != null) {
            if (xmagicImplCallback.onUpdateProperty(xmagicProperty)) {
                return;
            }
        }
        if (mXmagicApi != null && xmagicProperty != null) {
            mXmagicApi.updateProperty(xmagicProperty);
        }
    }


    /**
     * 用于恢复美颜效果使用
     */
    public void onResume() {
        if (mXmagicApi == null) {
            mXmagicApi = XmagicApiWrapper.createXmagicApi(applicationContext, false, null);
            mXmagicApi.updateProperties(XmagicPanelDataManager.getInstance().getUsedProperty());
            List<XmagicProperty<?>> updateList = null;
            if (isOpenBeauty) {
                updateList = XmagicPanelDataManager.getInstance().getOpenBeautyItems();
            } else {
                updateList = XmagicPanelDataManager.getInstance().getCloseBeautyItems();
            }
            if (mXmagicApi != null && updateList != null && updateList.size() > 0) {
                mXmagicApi.updateProperties(updateList);
            }
        } else {
            mXmagicApi.onResume();
        }
        //注册传感器
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 用于暂停美颜效果
     */
    public void onPause() {
        if (mXmagicApi != null) {
            mXmagicApi.onPause();
        }
        //取消传感器
        mSensorManager.unregisterListener(this);
    }

    /**
     * 当页面效果时调用，销毁美颜
     * 需要在gl线程创建
     */
    public void onDestroy() {
        if (mXmagicApi != null) {
            mXmagicApi.onDestroy();
            mXmagicApi = null;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isOpenPhotoAlbum && xmagicPanelView != null) {
            isOpenPhotoAlbum = false;
            xmagicPanelView.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 设置模型数据
     */
//    public static void setBundleToLightEngine(XmagicApi xmagicApi){
//        if (xmagicApi != null) {
//            String resDir = XmagicResParser.getResPath();
//            Map<String, String> map = new HashMap<>();
//            map.put(XmagicConstant.AgentType.BG_SEG_AGENT, resDir + "light_assets/models/LightSegmentBody.bundle");
//            map.put(XmagicConstant.AgentType.HAIR_SEG_AGENT, resDir + "light_assets/models/LightSegmentHair.bundle");
//            map.put(XmagicConstant.AgentType.HEAD_SEG_AGENT, resDir + "light_assets/models/LightSegmentHead.bundle");
//            map.put(XmagicConstant.AgentType.HAND_AGENT, resDir + "light_assets/models/LightHandModel.bundle");
//            map.put(XmagicConstant.AgentType.BODY_AGENT, resDir + "light_assets/models/LightBodyModel.bundle");
//            map.put(XmagicConstant.AgentType.ACE_3D_AGENT, resDir + "light_assets");
//            xmagicApi.setBundleToLightEngine(map);
//        }
//    }


    public boolean isSupportBeauty() {
        return mXmagicApi.isSupportBeauty();
    }


    public interface XmagicImplCallback {
        boolean onUpdateProperty(XmagicProperty xmagicProperty);
    }
}
