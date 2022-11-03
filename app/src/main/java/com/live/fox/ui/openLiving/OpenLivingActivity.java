package com.live.fox.ui.openLiving;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.live.fox.AnchorLiveActivity;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.LiveFinishActivity;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityOpenLivingBinding;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.dialog.TipDialog;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Pay;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;
import com.lovense.sdklibrary.Lovense;
import com.luck.picture.lib.permissions.RxPermissions;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.tencent.rtmp.TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;

public class OpenLivingActivity extends BaseBindingViewActivity implements ITXLivePushListener {


    ActivityOpenLivingBinding mBind;
    private TXLivePusher mLivePusher;                    // SDK 推流类
    private TXLivePushConfig mLivePushConfig;                // SDK 推流 config
    List<BaseBindingFragment> fragments=new ArrayList<>();
    boolean stopCameraPreview=false;
    TXPhoneStateListener mPhoneListener;
    TipDialog errorDialog;
    String mPushUrl="rtmp://push1.tencentlive.xyz/live/781100?txSecret=391d80fdddc4be2c5db0122a9e9c79c6&txTime=6364EE70";

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,OpenLivingActivity.class));
    }

    @Override
    public boolean isHasHeader() {
        return false;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_open_living;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        int paddingTop=StatusBarUtil.getStatusBarHeight(this);
        mBind.frameLayout.setPadding(0,paddingTop,0,0);
        setWindowsFlag();

        fragments.add(new PreparingLivingFragment());
        fragments.add(new StartLivingFragment());

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        for (int i = 0; i <fragments.size() ; i++) {
            fragmentTransaction.add(R.id.frameLayout,fragments.get(i));
        }

        for (int i = 0; i <fragments.size() ; i++) {
            fragmentTransaction.hide(fragments.get(i));
        }
        fragmentTransaction.commitAllowingStateLoss();

        initPusher();
        initListener();
        startCameraPreview();
        showPreParingFragment();

    }

    private void showPreParingFragment()
    {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragments.get(0));
        fragmentTransaction.hide(fragments.get(1));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showStartLiving()
    {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments.get(0));
        fragmentTransaction.show(fragments.get(1));
        fragmentTransaction.commitAllowingStateLoss();
    }



    /**
     * 开启摄像头预览
     */
    @SuppressLint("CheckResult")
    public void startCameraPreview() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(granted -> {
                    if (granted) {
                        stopCameraPreview = false;
                        mLivePusher.startCameraPreview(mBind.txVideoView);
                        startRTMPPush();
                    } else {
                        // 有的权限被拒绝或被勾选不再提示
                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setMessage(getString(R.string.permitionRun))
                                .setPositiveButton(getString(R.string.see), (dialog, which) -> finish())
                                .show();
                    }
                });
    }


    /**
     * 初始化电话监听
     */
    private void initListener() {
        mPhoneListener = new TXPhoneStateListener(mLivePusher);
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 开始推流
     */
    public void startRTMPPush() {
        LogUtils.e("startPublishImpl mPushUrl : " + mPushUrl);
        if (TextUtils.isEmpty(mPushUrl) || (!mPushUrl.trim().toLowerCase().startsWith("rtmp://"))) {
            ToastUtils.showShort(getString(R.string.rtmp));
            return;
        }
        // 显示本地预览的View
        mBind.txVideoView.setVisibility(View.VISIBLE);

        // 添加播放回调
        mLivePusher.setPushListener(this);

        // 添加后台垫片推流参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_last_push_stream, options);
        //设置垫片推流的图片素材。1080 × 1920。
        mLivePushConfig.setPauseImg(bitmap);
        //设置垫片的帧率与最长持续时间。 调用 TXLivePusher 的 pausePush() 接口，
        // 会暂停摄像头采集并进入垫片推流状态，如果该状态一直保持， 可能会消耗主播过多的手机流量，
        // 本字段用于指定垫片推流的最大持续时间，超过后即断开与云服务器的连接。
        mLivePushConfig.setPauseImg(300, 5);
        //设置后台推流的选项。
        //PAUSE_FLAG_PAUSE_VIDEO 表示暂停推流时，采用 TXLivePushConfig#setPauseImg(Bitmap)
        // 传入的图片作为画面推流，声音不做暂停，继续录制麦克风或 custom 音频发送
        //PAUSE_FLAG_PAUSE_VIDEO | PAUSE_FLAG_PAUSE_AUDIO 表示暂停推流时，推送暂停图片和静音数据。
        mLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO
                | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);// 设置暂停时，只停止画面采集，不停止声音采集。

        // 设置推流分辨率
        mLivePushConfig.setVideoResolution(VIDEO_RESOLUTION_TYPE_360_640);
        //在码率一定的情况下，分辨率与清晰度成反比关系：分辨率越高，图像越不清晰，分辨率越低，图像越清晰。
        //用于控制是手动对焦还是自动对焦
//        mLivePushConfig.setTouchFocus(true);

        // 设置美颜
        if (Constant.isOpenBeauty) {
            mLivePushConfig.setBeautyFilter(9, 9, 5);
//            mLivePusher.setBeautyFilter();
        }

        // 开启麦克风推流相关 true：静音；false：不静音。
        mLivePusher.setMute(false);
//        mLivePusher.setFocusPosition(); 设置焦点
        // 是否开启观众端镜像观看
//        mLivePusher.setMirror(mirror);

        //設置視頻質量：高清
//        if (temphd) {
        mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, false, false);
//        } else {
//            mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, false, false);
//        }

        //设置就近选路
        mLivePushConfig.enableNearestIP(true);
        //设置是否开启码率自适应 默认值：false
        mLivePushConfig.setAutoAdjustBitrate(true);
        //设置视频帧率 默认值：20
        mLivePushConfig.setVideoFPS(25);
        //设置动态调整码率的策略
        mLivePushConfig.setAutoAdjustStrategy(5);

        // 设置推流配置
        mLivePusher.setConfig(mLivePushConfig);
        // 设置本地预览View
        if (stopCameraPreview) {
            mLivePusher.startCameraPreview(mBind.txVideoView);
        }

        //開始推流
        int result = mLivePusher.startPusher(mPushUrl);
        if (result == -5) {
            DialogFactory.showOneBtnDialog(this, getString(R.string.verificationFailed), new TipDialog.DialogButtonOnClickListener() {
                @Override
                public void onClick(View button, TipDialog dialog) {
                    dialog.dismiss();
                }
            });
            return;
        }

        LogUtils.e("startPublishImpl result : " + result);
    }

    /**
     * 初始化 SDK 推流器
     */
    private void initPusher() {

        TXLiveBase.getInstance().setLicence(CommonApp.getInstance(),
                "https://license.vod-control.com/license/v2/1313381501_1/v_cube.license",
                "588574635d12a182671b999030910209");


        mLivePusher = new TXLivePusher(this);

//        mLivePusher.setZoom(5);//21.1.10
        mLivePushConfig = new TXLivePushConfig();
        mLivePushConfig.setVideoEncodeGop(3);
        mLivePushConfig.setTouchFocus(false);//false：不开启手动对焦
//        mLivePushConfig.setVideoResolution(VIDEO_RESOLUTION_TYPE_360_640);
        mLivePusher.setConfig(mLivePushConfig);
//        mLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_SMOOTH, 5, 3, 2);

        // 设置自定义视频处理回调，在主播预览及编码前回调出来，用户可以用来做自定义美颜或者增加视频特效
//        mLivePusher.setVideoProcessListener(new TXLivePusher.VideoCustomProcessListener() {
//            /**
//             * 在OpenGL线程中回调，在这里可以进行采集图像的二次处理
//             * @param i  纹理ID
//             * @param i1      纹理的宽度
//             * @param i2     纹理的高度
//             * @return 返回给SDK的纹理
//             * 说明：SDK回调出来的纹理类型是GLES20.GL_TEXTURE_2D，接口返回给SDK的纹理类型也必须是GLES20.GL_TEXTURE_2D
//             */
//            @Override
//            public int onTextureCustomProcess(int i, int i1, int i2) {
//                if (mOnFirstCreate) {
//                    mFURenderer.onSurfaceCreated();
//                    mOnFirstCreate = false;
//                }
//                return mFURenderer.onDrawFrameSingleInputTex(i, i1, i2);
//            }
//
//            /**
//             * 增值版回调人脸坐标
//             * @param floats   归一化人脸坐标，每两个值表示某点P的X,Y值。值域[0.f, 1.f]
//             */
//            @Override
//            public void onDetectFacePoints(float[] floats) {
//
//            }
//
//            /**
//             * 在OpenGL线程中回调，可以在这里释放创建的OpenGL资源
//             */
//            @Override
//            public void onTextureDestoryed() {
//                LogUtils.e("onTextureDestoryed: t:" + Thread.currentThread().getId());
//            }
//        });
    }

    @Override
    public void onPushEvent(int event, Bundle bundle) {
        LogUtils.e("onPushEvent event : " + event + "," + bundle.getString(TXLiveConstants.EVT_DESCRIPTION));

        if (mBind.txVideoView != null) {
            mBind.txVideoView.setLogText(null, bundle, event);
        }

        // >常規事件
        if (event == TXLiveConstants.PUSH_EVT_CONNECT_SUCC) {
            // 1001 已經成功連接到騰訊雲推流服務器
        } else if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
            // 1002 與服務器握手完畢,一切正常，准備開始推流
        } else if (event == TXLiveConstants.PUSH_EVT_OPEN_CAMERA_SUCC) {
            // 1003 推流器已成功打開攝像頭

        } else if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            ToastUtils.showShort(getString(R.string.hardCoding));
            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
            mLivePusher.setConfig(mLivePushConfig);
        } else if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT
                || event == TXLiveConstants.PUSH_ERR_INVALID_ADDRESS
                || event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL
                || event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
            stopRTMPPush();
            // 遇到以上错误，则停止推流
            showErrorDialog(getString(R.string.failRetry));
        } else if (event == TXLiveConstants.PUSH_EVT_START_VIDEO_ENCODER) {
            //1008,启动软编
        }

        // >錯誤通知
        if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {
            // -1301 打開攝像頭失敗
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL || event
                == TXLiveConstants.PUSH_ERR_MIC_RECORD_FAIL) {
            // -1302 打開麥克風失敗
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        } else if (event == TXLiveConstants.PUSH_ERR_VIDEO_ENCODE_FAIL) {
            // -1303 視頻編碼失敗
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        } else if (event == TXLiveConstants.PUSH_ERR_AUDIO_ENCODE_FAIL) {
            // -1304 音頻編碼失敗
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        } else if (event == TXLiveConstants.PUSH_ERR_UNSUPPORTED_RESOLUTION) {
            // -1305 不支持的視頻分辨率
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        } else if (event == TXLiveConstants.PUSH_ERR_UNSUPPORTED_SAMPLERATE) {
            // -1306 不支持的音頻采樣率
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        }

        /// >警告事件
        if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
            // 1101 網絡狀況不佳：上行帶寬太小，上傳數據受阻
            ToastUtils.showShort(getString(R.string.netBad));
        } else if (event == TXLiveConstants.PUSH_WARNING_RECONNECT) {
            // 1102 網絡斷連, 已啓動自動重連 (自動重連連續失敗超過三次會放棄)
        } else if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            // 1103 硬編碼啓動失敗，采用軟編碼
        } else if (event == TXLiveConstants.PUSH_WARNING_DNS_FAIL) {
            // 3001 RTMP -DNS解析失敗（會觸發重試流程）
        } else if (event == TXLiveConstants.PUSH_WARNING_SEVER_CONN_FAIL) {
            // 3002 RTMP服務器連接失敗（會觸發重試流程）
        } else if (event == TXLiveConstants.PUSH_WARNING_SHAKE_FAIL) {
            // 3003 RTMP服務器握手失敗（會觸發重試流程）
        } else if (event == TXLiveConstants.PUSH_WARNING_SERVER_DISCONNECT) {
            // 3004 RTMP服務器主動斷開連接（會觸發重試流程）
        }

    }

    @Override
    public void onNetStatus(Bundle bundle) {
        Log.e("onNetStatus",new Gson().toJson(bundle));
        if (mBind.txVideoView != null) {
            mBind.txVideoView.setLogText(bundle, null, 0);
        }

    }

    /**
     * 电话监听
     */
    private static class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<TXLivePusher> mPusher;

        public TXPhoneStateListener(TXLivePusher pusher) {
            mPusher = new WeakReference<>(pusher);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            TXLivePusher pusher = mPusher.get();
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (pusher != null) pusher.pausePusher();
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (pusher != null) pusher.pausePusher();
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (pusher != null) pusher.resumePusher();
                    break;
            }
        }
    }

    private void stopRTMPPush() {
        if (mLivePusher != null) {
            // 停止BGM
            mLivePusher.stopBGM();
            // 停止本地预览
            mLivePusher.stopCameraPreview(false);
            stopCameraPreview = true;
            // 移除监听
            mLivePusher.setPushListener(null);
            // 停止推流
            mLivePusher.stopPusher();
            // 隐藏本地预览的View
//            mPusherView.setVisibility(View.GONE);
            // 移除垫片图像
            mLivePushConfig.setPauseImg(null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        lushToyList.clear();
//        isPlayToy = false;
//        toyHandler.removeMessages(1);
//        if (mLovenseToy != null) {
//            Lovense.getInstance(getApplication()).disconnect(mLovenseToy.getToyId());
//        }
//
//        heartHandler.removeMessages(1);

        AppIMManager.ins().removeMessageReceivedListener(AnchorLiveActivity.class);

        stopRTMPPush(); // 停止推流
        if (mBind.txVideoView != null) {
            mBind.txVideoView.onDestroy(); // 销毁 View
        }

        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
    }


    public void showErrorDialog(String tipMes) {
        if (errorDialog == null) {
            errorDialog = new TipDialog(this);
        }
        errorDialog.setCanceledOnTouchOutside(false);
        errorDialog.setPromptTitle(tipMes);
        errorDialog.setButton1(getString(R.string.picture_quit_audio), (button, dialog) -> {
            dialog.dismiss();
            closeLiveRoom(getString(R.string.endLive), false);
        });
        errorDialog.setButton2(getString(R.string.retryConnect), (button, dialog) -> {
            dialog.dismiss();
            startRTMPPush();
        });
        if (isDestroyed() || isFinishing()) {
            return;
        }
        errorDialog.show();
        errorDialog.setCancelable(false);
    }


    /**
     * 关闭直播间
     * 是否是管理员或者后台强制关播
     */
    public void closeLiveRoom(String finishTip, boolean isKick) {
        if (KeyboardUtils.isSoftInputVisible(this)) {
            KeyboardUtils.toggleSoftInput();
        }
//        LiveFinishActivity.startActivity(this, anchor, finishTip, isKick);
//        isCloseRooming = true;
        stopRTMPPush();
//        heartHandler.removeMessages(1);
        //游戏退出
//        Api_Pay.ins().kickout(DataCenter.getInstance().getUserInfo().getUser().getUid() + "", new JsonCallback<String>() {
//            @Override
//            public void onSuccess(int code, String msg, String data) {
//
//            }
//        });
        finish();
    }
}
