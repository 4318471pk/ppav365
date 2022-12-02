package com.live.fox.ui.openLiving;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityOpenLivingBinding;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.dialog.LoadingBindingDialogFragment;
import com.live.fox.dialog.TipDialog;
import com.live.fox.dialog.temple.LivingInterruptDialog;
import com.live.fox.dialog.temple.TempleDialog2;
import com.live.fox.entity.LivingGiftBean;
import com.live.fox.entity.LotteryCategoryOfBeforeLiving;
import com.live.fox.entity.SendGiftAmountBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_Pay;
import com.live.fox.server.Api_User;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.mine.CenterOfAnchorActivity;
import com.live.fox.ui.mine.editprofile.EditProfileImageActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.opensource.svgaplayer.SVGAParser;
import com.tencent.demo.XMagicImpl;
import com.tencent.demo.utils.PermissionHandler;
import com.tencent.live2.V2TXLiveDef;
import com.tencent.live2.V2TXLivePusherObserver;
import com.tencent.live2.impl.V2TXLivePusherImpl;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.xmagic.XmagicApi;
import com.tencent.xmagic.XmagicProperty;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.tencent.live2.V2TXLiveDef.V2TXLiveBufferType.V2TXLiveBufferTypeTexture;
import static com.tencent.live2.V2TXLiveDef.V2TXLivePixelFormat.V2TXLivePixelFormatTexture2D;
import static com.tencent.rtmp.TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;

public class OpenLivingActivity extends BaseBindingViewActivity  {

    private static final String Title="Title";
    private static final String LiveID="LiveID";
    private static final String LiveConfigId="LiveConfigId";
    private static final String BGOfLiving="BGOfLiving";
    private static final String FixRoomType="FixRoomType";


    ActivityOpenLivingBinding mBind;
    private V2TXLivePusherImpl mLivePusher;                    // SDK 推流类
    List<BaseBindingFragment> fragments=new ArrayList<>();
    boolean stopCameraPreview=false;
    TXPhoneStateListener mPhoneListener;
    boolean isCameraInitFinish=false;
    boolean isFrontCamera = true; //是否前置摄像头
    public List<LivingGiftBean> giftListData=new ArrayList<>();//礼物列表;
    public List<LivingGiftBean> vipGiftListData=new ArrayList<>();//特权礼物列表;
    public List<SendGiftAmountBean> sendGiftAmountBeans;//礼物可发送列表
    public String pushUrl="",roomTitle,liveId,liveConfigId,imageURL;//推流地址 房间名称 直播ID 直播线路ID 封面图片地址
    String fixRoomType;//固定房间类型 只展示没其他作用
    int roomType=0;//房间类型 0免费 1 按时收费 2 按场收费
    int roomPrice=0;//房间单价

    String contactAccount=null;//主播联系方式的号码
    int contactCostDiamond=0;//主播联系方式的号码需要的钻石
    int contactType=-1;//主播联系方式  微信 qq 电话
    LotteryCategoryOfBeforeLiving lotteryCategoryOfBeforeLiving;

    private boolean mIsResumed = false;
    private boolean mIsPermissionGranted = false;
    boolean isSupportBeautyFace=false;
    XMagicImpl xMagicImpl;//美颜API
    V2TXLivePusherObserver observer;//推流观察器
    private final PermissionHandler mPermissionHandler = new PermissionHandler(this) {
        @Override
        protected void onAllPermissionGranted() {
            mIsPermissionGranted = true;
            tryResume();
        }
    };

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String mPushUrl) {
        this.pushUrl = mPushUrl;
    }

    public static void startActivity(Context context,String imageURL, String roomTitle, String liveID,String liveConfigId,String fixRoomType)
    {
        Intent intent=new Intent(context,OpenLivingActivity.class);
        intent.putExtra(Title,roomTitle);
        intent.putExtra(LiveID,liveID);
        intent.putExtra(LiveConfigId,liveConfigId);
        intent.putExtra(BGOfLiving,imageURL);
        intent.putExtra(FixRoomType,fixRoomType);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    try {
                        String url=  PictureFileUtils.getPicturePath(this);
                        File file=new File(url);
                        if(file!=null && file.exists())
                        {
                            EditProfileImageActivity.startActivity(this,EditProfileImageActivity.Square,url);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 圖片選擇結果回調
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if(selectList!=null && selectList.size()>0)
                    {
                        LocalMedia localMedia = selectList.get(0);
                        LogUtils.e("图片-----》" + localMedia.getPath());
                        EditProfileImageActivity.startActivity(this,EditProfileImageActivity.Square,localMedia.getPath());
                    }
                    break;
                case ConstantValue.REQUEST_CROP_PIC://头像上传到文件服务器成功
                    if(data!=null && data.getStringExtra(ConstantValue.pictureOfUpload)!=null)
                    {
                        File file=new File(data.getStringExtra(ConstantValue.pictureOfUpload));
                        if(file!=null && file.exists())
                        {
                            uploadBGOfLivingRoom(file);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsResumed = true;
        tryResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);// 必须有这个调用, mPermissionHandler 才能正常工作
    }

    private void tryResume() {
        if (mIsResumed && mIsPermissionGranted) {
            if (xMagicImpl != null) {
                xMagicImpl.onResume();
            }
        }
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

        roomTitle=getIntent().getStringExtra(Title);
        liveId=getIntent().getStringExtra(LiveID);
        liveConfigId=getIntent().getStringExtra(LiveConfigId);
        imageURL=getIntent().getStringExtra(BGOfLiving);
        fixRoomType=getIntent().getStringExtra(FixRoomType);

        int paddingTop=StatusBarUtil.getStatusBarHeight(this);
        mBind.frameLayout.setPadding(0,paddingTop,0,0);
        setWindowsFlag(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        keepScreenLongLight(true);

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

        mPermissionHandler.start();// 检查和请求系统权限
        initPusher();//初始化推流器
        initListener();//设定监听
        startCameraPreview();//打开摄像头
        SVGAParser.Companion.shareParser().init(this);//SVGA解析器初始化
        getGiftList();//请求获取礼物
        getVipGiftList();//请求特权礼物
        getAmountListOfGift();//请求获取发送礼物数量列表

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

    public void startAcceptMessage()
    {
        AppIMManager.ins().addMessageListener(OpenLivingActivity.class, new AppIMManager.OnMessageReceivedListener(){
            @Override
            public void onIMReceived(int protocol, String msg) {
                StartLivingFragment startLivingFragment=(StartLivingFragment) fragments.get(1);
                startLivingFragment.onReceivedMessage(protocol,msg);
            }
        });
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
                        mLivePusher.setRenderView(mBind.txVideoView);
                        mLivePusher.startCamera(isFrontCamera);
                        mLivePusher.startMicrophone();
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

    private void uploadBGOfLivingRoom(File file)
    {
        showLoadingDialogWithNoBgBlack();
        Api_User.ins().uploadLivingRoomPicture(file, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == Constant.Code.SUCCESS) {
                    imageURL=data;
                    PreparingLivingFragment preparingLivingFragment=(PreparingLivingFragment) fragments.get(0);
                    preparingLivingFragment.setImage(data);
                    showToastTip(true, getString(R.string.modifySuccess));
                    //Log.e("uploadBGOfLivingRoom",data);
                } else {
                    ToastUtils.showShort(msg);
                }
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
        LogUtils.e("startPublishImpl mPushUrl : " + pushUrl);
        if (TextUtils.isEmpty(pushUrl) || (!pushUrl.trim().toLowerCase().startsWith("rtmp://"))) {
            ToastUtils.showShort(getString(R.string.rtmp));
            return;
        }
        // 显示本地预览的View
        mBind.txVideoView.setVisibility(View.VISIBLE);

        // 添加播放回调
        mLivePusher.setObserver(observer);

//        // 添加后台垫片推流参数
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_anchor_loading, options);
//        //设置垫片推流的图片素材。1080 × 1920。
//        mLivePushConfig.setPauseImg(bitmap);
//        //设置垫片的帧率与最长持续时间。 调用 TXLivePusher 的 pausePush() 接口，
//        // 会暂停摄像头采集并进入垫片推流状态，如果该状态一直保持， 可能会消耗主播过多的手机流量，
//        // 本字段用于指定垫片推流的最大持续时间，超过后即断开与云服务器的连接。
//        mLivePushConfig.setPauseImg(300, 5);
//        //设置后台推流的选项。
//        //PAUSE_FLAG_PAUSE_VIDEO 表示暂停推流时，采用 TXLivePushConfig#setPauseImg(Bitmap)
//        // 传入的图片作为画面推流，声音不做暂停，继续录制麦克风或 custom 音频发送
//        //PAUSE_FLAG_PAUSE_VIDEO | PAUSE_FLAG_PAUSE_AUDIO 表示暂停推流时，推送暂停图片和静音数据。
//        mLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO
//                | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);// 设置暂停时，只停止画面采集，不停止声音采集。
//
//        // 设置推流分辨率
//        mLivePushConfig.setVideoResolution(VIDEO_RESOLUTION_TYPE_360_640);
//        //在码率一定的情况下，分辨率与清晰度成反比关系：分辨率越高，图像越不清晰，分辨率越低，图像越清晰。
//        //用于控制是手动对焦还是自动对焦
////        mLivePushConfig.setTouchFocus(true);
//
//        // 设置美颜
//        if (Constant.isOpenBeauty) {
//            mLivePushConfig.setBeautyFilter(9, 9, 5);
////            mLivePusher.setBeautyFilter();
//        }

//        mLivePusher.setFocusPosition(); 设置焦点
        // 是否开启观众端镜像观看
//        mLivePusher.setMirror(mirror);

        //設置視頻質量：高清
        mLivePusher.setVideoQuality(new V2TXLiveDef.V2TXLiveVideoEncoderParam(V2TXLiveDef.V2TXLiveVideoResolution.V2TXLiveVideoResolution1920x1080));


        //開始推流
        int result = mLivePusher.startPush(pushUrl);
        LogUtils.e("startPublishImpl result : " + result);
        if (result == -5) {
            DialogFactory.showOneBtnDialog(this, getString(R.string.verificationFailed), new TipDialog.DialogButtonOnClickListener() {
                @Override
                public void onClick(View button, TipDialog dialog) {
                    dialog.dismiss();
                }
            });
            return;
        }


    }

    /**
     * 初始化 SDK 推流器
     */
    private void initPusher() {

        LoadingBindingDialogFragment fragment=LoadingBindingDialogFragment.getInstance(LoadingBindingDialogFragment.purple);
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),fragment);
        mLivePusher = new V2TXLivePusherImpl(this, V2TXLiveDef.V2TXLiveMode.TXLiveMode_RTMP);
        mLivePusher.enableCustomVideoProcess(true, V2TXLivePixelFormatTexture2D, V2TXLiveBufferTypeTexture);
        mLivePusher.setObserver(new V2TXLivePusherObserver() {
            @Override
            public void onGLContextCreated() {
                Log.e("mLivePusher","onGLContextCreated");
                if(!isCameraInitFinish)
                {
                    isCameraInitFinish=true;
                    fragment.dismissAllowingStateLoss();
                    showPreParingFragment();
                    createXMagic();
                }

            }

            @Override
            public int onProcessVideoFrame(V2TXLiveDef.V2TXLiveVideoFrame srcFrame, V2TXLiveDef.V2TXLiveVideoFrame dstFrame) {
                if (xMagicImpl != null) {
                    dstFrame.texture.textureId = xMagicImpl.process(srcFrame.texture.textureId, srcFrame.width, srcFrame.height);
                }
                return srcFrame.texture.textureId;
            }

            @Override
            public void onGLContextDestroyed() {
                if (xMagicImpl != null) {
                    xMagicImpl.onDestroy();
                }
            }
        });

//        mLivePusher.setZoom(5);//21.1.10
//        mLivePushConfig = new TXLivePushConfig();
//        mLivePushConfig.setVideoEncodeGop(3);
//        mLivePushConfig.setTouchFocus(false);//false：不开启手动对焦
//        mLivePushConfig.setVideoResolution(VIDEO_RESOLUTION_TYPE_360_640);
//        mLivePusher.set(mLivePushConfig);
//        mLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_SMOOTH, 5, 3, 2);

//        // 设置自定义视频处理回调，在主播预览及编码前回调出来，用户可以用来做自定义美颜或者增加视频特效
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
//                return i;
//            }
//
//            /**
//             * 增值版回调人脸坐标
//             * @param floats   归一化人脸坐标，每两个值表示某点P的X,Y值。值域[0.f, 1.f]
//             */
//            @Override
//            public void onDetectFacePoints(float[] floats) {
//                LogUtils.e("VideoProcess: onDetectFacePoints" );
//            }
//
//            /**
//             * 在OpenGL线程中回调，可以在这里释放创建的OpenGL资源
//             */
//            @Override
//            public void onTextureDestoryed() {
//                LogUtils.e("VideoProcess: onTextureDestoryed" );
//            }
//        });
    }

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
//            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
//            mLivePusher.setConfig(mLivePushConfig);
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

//    @Override
//    public void onNetStatus(Bundle bundle) {
//        Log.e("onNetStatus",new Gson().toJson(bundle));
//        if (mBind.txVideoView != null) {
//            mBind.txVideoView.setLogText(bundle, null, 0);
//        }
//
//    }

    /**
     * 电话监听
     */
    private static class TXPhoneStateListener extends PhoneStateListener {
        WeakReference<V2TXLivePusherImpl> mPusher;

        public TXPhoneStateListener(V2TXLivePusherImpl pusher) {
            mPusher = new WeakReference<V2TXLivePusherImpl>(pusher);
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            V2TXLivePusherImpl pusher = mPusher.get();
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (pusher != null)
                    {
                        pusher.pauseAudio();
                        pusher.pauseAudio();
                    }
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (pusher != null)
                    {
                        pusher.resumeAudio();
                        pusher.resumeVideo();
                    }
                    break;
            }
        }
    }

    public void stopRTMPPush() {
        if (mLivePusher != null) {
            mLivePusher.stopPush();
            mLivePusher.stopCamera();
            stopCameraPreview = true;
            mLivePusher.setObserver(null);
            // 隐藏本地预览的View
//            mPusherView.setVisibility(View.GONE);
            // 移除垫片图像
//            mLivePushConfig.setPauseImg(null);
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

        AppIMManager.ins().removeMessageReceivedListener(OpenLivingActivity.class);

        stopRTMPPush(); // 停止推流
        if (mBind.txVideoView != null) {
            mBind.txVideoView.onDestroy(); // 销毁 View
        }

        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
    }


    public void showErrorDialog(String tipMes) {

        LivingInterruptDialog dialog= LivingInterruptDialog.getInstance(new LivingInterruptDialog.OnClickButtonsListener() {
            @Override
            public void onClick(boolean isConfirm, LivingInterruptDialog dialog) {
                dialog.dismissAllowingStateLoss();
                if(isConfirm)
                {
                    startRTMPPush();
                }
                else
                {
                    closeLiveRoom();
                }
            }
        });
        DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),dialog);
    }


    /**
     * 关闭直播间
     * 是否是管理员或者后台强制关播
     */
    public void closeLiveRoom() {
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

    private void getAmountListOfGift()
    {
        Api_Live.ins().getGiftAmountList( new JsonCallback<List<SendGiftAmountBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<SendGiftAmountBean> data) {
                if(code==0)
                {
                    OpenLivingActivity.this.sendGiftAmountBeans=data;
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void getVipGiftList()
    {
        Api_Live.ins().getGiftList(1, new JsonCallback<List<LivingGiftBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<LivingGiftBean> data) {
                if(code==0)
                {
                    if(data!=null)
                    {
                        for (int i = 0; i < data.size(); i++) {
                            LivingGiftBean livingGiftBean=data.get(i);
                            livingGiftBean.setItemName(livingGiftBean.getName());
                            livingGiftBean.setSelected(false);
                            livingGiftBean.setItemId(livingGiftBean.getId()+"");
                            livingGiftBean.setImgUrl(livingGiftBean.getGitficon());
                            livingGiftBean.setCostDiamond(livingGiftBean.getNeeddiamond());
                            vipGiftListData.add(data.get(i));
                        }
                    }
                }
                else
                {
                    ToastUtils.showShort(msg);
                }

            }
        });
    }

    private void getGiftList()
    {
        Api_Live.ins().getGiftList(0, new JsonCallback<List<LivingGiftBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<LivingGiftBean> data) {
                if(code==0)
                {
                    if(data!=null)
                    {
                        for (int i = 0; i < data.size(); i++) {
                            LivingGiftBean livingGiftBean=data.get(i);
                            livingGiftBean.setItemName(livingGiftBean.getName());
                            livingGiftBean.setSelected(false);
                            livingGiftBean.setItemId(livingGiftBean.getId()+"");
                            livingGiftBean.setImgUrl(livingGiftBean.getGitficon());
                            livingGiftBean.setCostDiamond(livingGiftBean.getNeeddiamond());
                            giftListData.add(data.get(i));
                        }
                    }
                }
                else
                {
                    ToastUtils.showShort(msg);
                }

            }
        });
    }

    public void onAnchorExitLiving()
    {
        //当主播主动点击退出直播弹窗
        closeLiveRoom();
        finish();
    }
    /**
     * 旋转相机
     */
    public void switchCamera() {
        if (mLivePusher != null) {
            isFrontCamera = !isFrontCamera;
            mLivePusher.stopCamera();
            if(mLivePusher.isPushing()==1)
            {
                mLivePusher.stopPush();
            }
            mLivePusher.startCamera(isFrontCamera);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    private void createXMagic() {
        xMagicImpl = new XMagicImpl(this, mBind.panelLayout, new XMagicImpl.XmagicImplCallback() {
            @Override
            public boolean onUpdateProperty(XmagicProperty xmagicProperty) {
                if (xmagicProperty == null) {
                    return true;
                }
                if (xmagicProperty.category == XmagicProperty.Category.SEGMENTATION) {
                    if ("video_segmentation_transparent_bg".equals(xmagicProperty.id)) {  //点击了透明背景素材 on click transparent bg
//                        if (selectedSurfaceViewId != FLOAT_SURFACE_VIEW_ID) {
//                            changeViewSize(true);
//                            selectedSurfaceViewId = FLOAT_SURFACE_VIEW_ID;
//                        }
                    } else if ("video_segmentation_blur_75".equals(xmagicProperty.id)) {    //背景模糊-强  on click blurred background-severe
//                        if (selectedSurfaceViewId != COMMON_SURFACE_VIEW_ID) {
//                            changeViewSize(false);
//                            selectedSurfaceViewId = COMMON_SURFACE_VIEW_ID;
//                        }
                    }
                }
                return false;
            }
        });
        isSupportBeautyFace=xMagicImpl.isSupportBeauty();

        xMagicImpl.mXmagicApi.setAIDataListener(new XmagicApi.XmagicAIDataListener() {
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

        xMagicImpl.mXmagicApi.setYTDataListener(new XmagicApi.XmagicYTDataListener() {
            @Override
            public void onYTDataUpdate(String s) {
                //不知道是什么鬼
            }
        });
    }
}
