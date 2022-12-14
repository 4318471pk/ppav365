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
import android.os.PowerManager;
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
import com.live.fox.BuildConfig;
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
import com.live.fox.utils.OnClickFrequentlyListener;
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
    private V2TXLivePusherImpl mLivePusher;                    // SDK ?????????
    List<BaseBindingFragment> fragments=new ArrayList<>();
    boolean stopCameraPreview=false;
    TXPhoneStateListener mPhoneListener;
    boolean isCameraInitFinish=false;
    boolean isFrontCamera = true; //?????????????????????
    public List<LivingGiftBean> giftListData=new ArrayList<>();//????????????;
    public List<LivingGiftBean> vipGiftListData=new ArrayList<>();//??????????????????;
    public List<SendGiftAmountBean> sendGiftAmountBeans;//?????????????????????
    public String pushUrl="",roomTitle,liveId,liveConfigId,imageURL;//???????????? ???????????? ??????ID ????????????ID ??????????????????
    String fixRoomType;//?????????????????? ????????????????????????
    int roomType=0;//???????????? 0?????? 1 ???????????? 2 ????????????
    int roomPrice=0;//????????????

    String contactAccount=null;//???????????????????????????
    int contactCostDiamond=0;//??????????????????????????????????????????
    int contactType=-1;//??????????????????  ?????? qq ??????
    LotteryCategoryOfBeforeLiving lotteryCategoryOfBeforeLiving;

    boolean isSupportBeautyFace=false;
    XMagicImpl xMagicImpl;//??????API
    V2TXLivePusherObserver observer;//???????????????

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
                    // ????????????????????????
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if(selectList!=null && selectList.size()>0)
                    {
                        LocalMedia localMedia = selectList.get(0);
                        LogUtils.e("??????-----???" + localMedia.getPath());
                        EditProfileImageActivity.startActivity(this,EditProfileImageActivity.Square,localMedia.getPath());
                    }
                    break;
                case ConstantValue.REQUEST_CROP_PIC://????????????????????????????????????
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
        if (xMagicImpl != null) {
            xMagicImpl.onResume();
        }
        if(mLivePusher!=null)
        {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if(powerManager.isScreenOn())
            {
                mLivePusher.stopVirtualCamera();
                mLivePusher.startPush(pushUrl);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLivePusher!=null)
        {
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if(!powerManager.isScreenOn())
            {
                mLivePusher.startVirtualCamera(BitmapFactory.decodeResource(getResources(),R.mipmap.stopliving_backgroud));
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

        initPusher();//??????????????????
        initListener();//????????????
        startCameraPreview();//???????????????
        SVGAParser.Companion.shareParser().init(this);//SVGA??????????????????
        getGiftList();//??????????????????
        getVipGiftList();//??????????????????
        getAmountListOfGift();//????????????????????????????????????

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
     * ?????????????????????
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
                        mLivePusher.setRenderMirror(V2TXLiveDef.V2TXLiveMirrorType.V2TXLiveMirrorTypeEnable);
                        mLivePusher.startCamera(isFrontCamera);
                        mLivePusher.startMicrophone();
                    } else {
                        // ?????????????????????????????????????????????
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
     * ?????????????????????
     */
    private void initListener() {
        mPhoneListener = new TXPhoneStateListener(mLivePusher);
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * ????????????
     */
    public void startRTMPPush() {
        LogUtils.e("startPublishImpl mPushUrl : " + pushUrl);
        if (TextUtils.isEmpty(pushUrl) || (!pushUrl.trim().toLowerCase().startsWith("rtmp://"))) {
            ToastUtils.showShort(getString(R.string.rtmp));
            return;
        }
        // ?????????????????????View
        mBind.txVideoView.setVisibility(View.VISIBLE);

        // ??????????????????
        mLivePusher.setObserver(observer);
//        // ??????????????????????????????
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_anchor_loading, options);
//        //????????????????????????????????????1080 ?? 1920???
//        mLivePushConfig.setPauseImg(bitmap);
//        //????????????????????????????????????????????? ?????? TXLivePusher ??? pausePush() ?????????
//        // ???????????????????????????????????????????????????????????????????????????????????? ?????????????????????????????????????????????
//        // ??????????????????????????????????????????????????????????????????????????????????????????????????????
//        mLivePushConfig.setPauseImg(300, 5);
//        //??????????????????????????????
//        //PAUSE_FLAG_PAUSE_VIDEO ?????????????????????????????? TXLivePushConfig#setPauseImg(Bitmap)
//        // ????????????????????????????????????????????????????????????????????????????????? custom ????????????
//        //PAUSE_FLAG_PAUSE_VIDEO | PAUSE_FLAG_PAUSE_AUDIO ????????????????????????????????????????????????????????????
//        mLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO
//                | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);// ??????????????????????????????????????????????????????????????????
//
//        // ?????????????????????
//        mLivePushConfig.setVideoResolution(VIDEO_RESOLUTION_TYPE_360_640);
//        //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//        //?????????????????????????????????????????????
////        mLivePushConfig.setTouchFocus(true);
//
//        // ????????????
//        if (Constant.isOpenBeauty) {
//            mLivePushConfig.setBeautyFilter(9, 9, 5);
////            mLivePusher.setBeautyFilter();
//        }

//        mLivePusher.setFocusPosition(); ????????????
        // ?????????????????????????????????
//        mLivePusher.setMirror(mirror);

        //???????????????????????????
        mLivePusher.setVideoQuality(new V2TXLiveDef.V2TXLiveVideoEncoderParam(V2TXLiveDef.V2TXLiveVideoResolution.V2TXLiveVideoResolution1920x1080));


        //????????????
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
     * ????????? SDK ?????????
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
                    if(BuildConfig.hasBeautyFace)
                    {
                        createXMagic();
                    }
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
//        mLivePushConfig.setTouchFocus(false);//false????????????????????????
//        mLivePushConfig.setVideoResolution(VIDEO_RESOLUTION_TYPE_360_640);
//        mLivePusher.set(mLivePushConfig);
//        mLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_SMOOTH, 5, 3, 2);

//        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//        mLivePusher.setVideoProcessListener(new TXLivePusher.VideoCustomProcessListener() {
//            /**
//             * ???OpenGL??????????????????????????????????????????????????????????????????
//             * @param i  ??????ID
//             * @param i1      ???????????????
//             * @param i2     ???????????????
//             * @return ?????????SDK?????????
//             * ?????????SDK??????????????????????????????GLES20.GL_TEXTURE_2D??????????????????SDK???????????????????????????GLES20.GL_TEXTURE_2D
//             */
//            @Override
//            public int onTextureCustomProcess(int i, int i1, int i2) {
//                return i;
//            }
//
//            /**
//             * ???????????????????????????
//             * @param floats   ????????????????????????????????????????????????P???X,Y????????????[0.f, 1.f]
//             */
//            @Override
//            public void onDetectFacePoints(float[] floats) {
//                LogUtils.e("VideoProcess: onDetectFacePoints" );
//            }
//
//            /**
//             * ???OpenGL????????????????????????????????????????????????OpenGL??????
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

        // >????????????
        if (event == TXLiveConstants.PUSH_EVT_CONNECT_SUCC) {
            // 1001 ?????????????????????????????????????????????
        } else if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
            // 1002 ????????????????????????,?????????????????????????????????
        } else if (event == TXLiveConstants.PUSH_EVT_OPEN_CAMERA_SUCC) {
            // 1003 ?????????????????????????????????

        } else if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            ToastUtils.showShort(getString(R.string.hardCoding));
//            mLivePushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
//            mLivePusher.setConfig(mLivePushConfig);
        } else if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT
                || event == TXLiveConstants.PUSH_ERR_INVALID_ADDRESS
                || event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL
                || event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {
            stopRTMPPush();
            // ????????????????????????????????????
            showErrorDialog(getString(R.string.failRetry));
        } else if (event == TXLiveConstants.PUSH_EVT_START_VIDEO_ENCODER) {
            //1008,????????????
        }

        // >????????????
        if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {
            // -1301 ?????????????????????
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL || event
                == TXLiveConstants.PUSH_ERR_MIC_RECORD_FAIL) {
            // -1302 ?????????????????????
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        } else if (event == TXLiveConstants.PUSH_ERR_VIDEO_ENCODE_FAIL) {
            // -1303 ??????????????????
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        } else if (event == TXLiveConstants.PUSH_ERR_AUDIO_ENCODE_FAIL) {
            // -1304 ??????????????????
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        } else if (event == TXLiveConstants.PUSH_ERR_UNSUPPORTED_RESOLUTION) {
            // -1305 ???????????????????????????
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        } else if (event == TXLiveConstants.PUSH_ERR_UNSUPPORTED_SAMPLERATE) {
            // -1306 ???????????????????????????
            showErrorDialog(bundle.getString(TXLiveConstants.EVT_DESCRIPTION));
        }

        /// >????????????
        if (event == TXLiveConstants.PUSH_WARNING_NET_BUSY) {
            // 1101 ????????????????????????????????????????????????????????????
            ToastUtils.showShort(getString(R.string.netBad));
        } else if (event == TXLiveConstants.PUSH_WARNING_RECONNECT) {
            // 1102 ????????????, ????????????????????? (?????????????????????????????????????????????)
        } else if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            // 1103 ???????????????????????????????????????
        } else if (event == TXLiveConstants.PUSH_WARNING_DNS_FAIL) {
            // 3001 RTMP -DNS???????????????????????????????????????
        } else if (event == TXLiveConstants.PUSH_WARNING_SEVER_CONN_FAIL) {
            // 3002 RTMP????????????????????????????????????????????????
        } else if (event == TXLiveConstants.PUSH_WARNING_SHAKE_FAIL) {
            // 3003 RTMP????????????????????????????????????????????????
        } else if (event == TXLiveConstants.PUSH_WARNING_SERVER_DISCONNECT) {
            // 3004 RTMP??????????????????????????????????????????????????????
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
     * ????????????
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
                //??????????????????
                case TelephonyManager.CALL_STATE_RINGING:
                //????????????
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (pusher != null)
                    {
                        pusher.pauseAudio();
                        pusher.pauseAudio();
                    }
                    break;
                //????????????
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
            // ?????????????????????View
//            mPusherView.setVisibility(View.GONE);
            // ??????????????????
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

        stopRTMPPush(); // ????????????
        if (mBind.txVideoView != null) {
            mBind.txVideoView.onDestroy(); // ?????? View
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
     * ???????????????
     * ??????????????????????????????????????????
     */
    public void closeLiveRoom() {
        if (KeyboardUtils.isSoftInputVisible(this)) {
            KeyboardUtils.toggleSoftInput();
        }
//        LiveFinishActivity.startActivity(this, anchor, finishTip, isKick);
//        isCloseRooming = true;
        stopRTMPPush();
//        heartHandler.removeMessages(1);
        //????????????
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
        //???????????????????????????????????????
        closeLiveRoom();
        finish();
    }
    /**
     * ????????????
     */
    public void switchCamera() {
        if (mLivePusher != null) {
            isFrontCamera = !isFrontCamera;
            mLivePusher.getDeviceManager().switchCamera(isFrontCamera);
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
                    if ("video_segmentation_transparent_bg".equals(xmagicProperty.id)) {  //??????????????????????????? on click transparent bg
//                        if (selectedSurfaceViewId != FLOAT_SURFACE_VIEW_ID) {
//                            changeViewSize(true);
//                            selectedSurfaceViewId = FLOAT_SURFACE_VIEW_ID;
//                        }
                    } else if ("video_segmentation_blur_75".equals(xmagicProperty.id)) {    //????????????-???  on click blurred background-severe
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
                //????????????????????????????????????????????????????????????
//                LogUtils.d(TAG, "onFaceDataUpdated");
            }
        });

        xMagicImpl.mXmagicApi.setYTDataListener(new XmagicApi.XmagicYTDataListener() {
            @Override
            public void onYTDataUpdate(String s) {
                //?????????????????????
            }
        });
    }

    public void showBeautyLayout()
    {
        if(BuildConfig.IsAnchorClient)
        {
            mBind.rlPanelMain.setVisibility(View.VISIBLE);
            mBind.rlPanelMain.setOnClickListener(new OnClickFrequentlyListener() {
                @Override
                public void onClickView(View view) {
                    mBind.rlPanelMain.setVisibility(View.GONE);
                }
            });
        }
        else
        {
            ToastUtils.showShort("?????????????????????????????????");
        }
    }
}
