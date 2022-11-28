package com.tencent.demo.avatar.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.tencent.demo.R;
import com.tencent.demo.XmagicApiWrapper;
import com.tencent.demo.avatar.AvatarResManager;
import com.tencent.demo.camera.gltexture.NoCameraGLTextureView;
import com.tencent.xmagic.XmagicApi;

import java.io.File;

public class TestActivity extends AppCompatActivity implements SensorEventListener {

    private static String TAG = TestActivity.class.getName();
    private XmagicApi mXmagicApi;
    //判断当前手机旋转方向，用于手机在不同的旋转角度下都能正常的识别人脸
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private NoCameraGLTextureView noCameraGLTextureView = null;

    private String avatarResName = AvatarResManager.AVATAR_RES_MALE;

    private NoCameraGLTextureView.GLTextureProcessListener glTextureProcessListener = null;


    private LinearLayout linearLayout = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_activity_layout);
        linearLayout = findViewById(R.id.layout);

        noCameraGLTextureView = findViewById(R.id.test_texture_view);
        this.glTextureProcessListener = new NoCameraGLTextureView.GLTextureProcessListener() {
            @Override
            public int onTextureCustomProcess(int textureId, int width, int height) {
                Log.e(TAG, "onTextureCustomProcess  " + Thread.currentThread().getName());
                if (mXmagicApi != null) {
                    return mXmagicApi.process(textureId, width, height);
                } else {
                    return textureId;
                }
            }

            @Override
            public void onTextureCreate() {
                Log.e(TAG, "GLTextureProcessListener onTextureCreate  " + Thread.currentThread().getName());
                initXMagic();
            }

            @Override
            public void onTextureDestroyed() {
                Log.e(TAG, "GLTextureProcessListener onTextureDestroyed  " + Thread.currentThread().getName());
                if (mXmagicApi != null) {
                    mXmagicApi.onDestroy();
                    mXmagicApi = null;
                }
            }
        };
        noCameraGLTextureView.setGlTextureProcessListener(this.glTextureProcessListener);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        findViewById(R.id.float_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFloatView();
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
        Log.e("testActivity", "onresume" + this.hashCode());
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
        Log.e("testActivity", "onPause" + this.hashCode());
        //取消传感器
        mSensorManager.unregisterListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("testActivity", "onDestroy" + this.hashCode());
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
        }
    }

    private String getAvatarConfig() {
        String avatarSaveData = null;
        avatarSaveData = com.tencent.xmagic.util.FileUtil.readFile(AvatarResManager.getAvatarConfigsDir() + File.separator + AvatarResManager.getAvatarConfigsFileName(avatarResName));

        return avatarSaveData;
    }


    private void showFloatView() {

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        int type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;
        int format = PixelFormat.RGBA_8888;

        layoutParams.type = type;
        layoutParams.format = format;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 200;
        layoutParams.height = 400;
        layoutParams.x = 300;
        layoutParams.y = 300;

        if (canDrawOverlays(this)) {
            noCameraGLTextureView.pause();
            linearLayout.removeAllViews();
            windowManager.addView(noCameraGLTextureView, layoutParams);
            noCameraGLTextureView.start();
        } else {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            this.startActivity(intent);
        }
    }


    public static boolean canDrawOverlays(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            return Settings.canDrawOverlays(context);
        } else {
            if (Settings.canDrawOverlays(context)) return true;
            try {
                WindowManager mgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                if (mgr == null) return false; //getSystemService might return null
                View viewToAdd = new View(context);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(0, 0, android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
                viewToAdd.setLayoutParams(params);
                mgr.addView(viewToAdd, params);
                mgr.removeView(viewToAdd);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }


}
