package com.live.fox;

import static com.tencent.rtmp.TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.faceunity.beautycontrolview.BeautyControlView;
import com.faceunity.beautycontrolview.EffectEnum;
import com.faceunity.beautycontrolview.FURenderer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.dialog.LiveRoomChargeSettingFragment;
import com.live.fox.dialog.LiveRoomSettingFragment;
import com.live.fox.dialog.LiveRoomVerify;

import com.live.fox.dialog.PKInvertDialog;
import com.live.fox.dialog.PKInvertListDialog;
import com.live.fox.dialog.PKInvertLoadingDialog;
import com.live.fox.dialog.PKSettingDialog;
import com.live.fox.dialog.RoomModeForPwdFragment;
import com.live.fox.dialog.RoomModeForTimingFragment;
import com.live.fox.dialog.TipDialog;
import com.live.fox.dialog.ToyListDialog;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.LushToy;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_LiveRecreation;
import com.live.fox.server.Api_Pay;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.manager.AppUserManger;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.PlayerUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.lovense.sdklibrary.Lovense;
import com.lovense.sdklibrary.LovenseToy;
import com.lovense.sdklibrary.callBack.LovenseError;
import com.lovense.sdklibrary.callBack.OnConnectListener;
import com.luck.picture.lib.permissions.RxPermissions;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 主播直播的直播间
 * 准备开始直播的界面
 */

public class AnchorLiveActivity extends BaseActivity implements
        AppIMManager.OnMessageReceivedListener, ITXLivePushListener {

    public int heartSpace = 30 * 1000;  //心跳间隔
    private static final String VERIFY_DIALOG_KEY = "verify dialog key";

    TXCloudVideoView mPusherView;
    TXCloudVideoView mTXCloudVideoView2;
    FrameLayout frameLayout_pk;
    ImageView iv_pkloading;
    ImageView iv_userb_pkbg;

    BeautyControlView beautyControlView;

    private TXLivePusher mLivePusher;                    // SDK 推流类
    private TXLivePushConfig mLivePushConfig;                // SDK 推流 config
    private PhoneStateListener mPhoneListener = null;         // 当前电话监听Listener

    protected String mPushUrl;

    private int moneyRank; //总榜值
    int liveId = -1; //开播标识，用来发心跳
    boolean mirror = true; //镜像xtg
    boolean isFrontCarame = true; //是否前置摄像头

    int roomType = 0; //0普通房间 1计时房间 2单次房间 3.密码房间
    int roomPrice = -1;
    int roomDcPrice = -1;
    String roomPwd = "";
    String liveConfigId;
    ArrayList<String> lotteryNames = new ArrayList<>();
    public ArrayList<String> names = new ArrayList<>();
    public Anchor anchor;

    PreviewFragment previewFragment;
    private LiveControlFragment liveControlFragment;
    TipDialog errorDialog;
    private int heartApiFailCount = 0;
    private int pushStateFailTime = 0;

    private boolean isLiving = false;
    protected Handler mHandler = new Handler();

    boolean isCloseRooming = false;
    boolean stopCameraPreview = false;

    TXLivePlayer mTXLivePlayer;

    private FURenderer mFURenderer;

    //是否在PK中
    public boolean isPking = false;
    //是否在PK惩罚阶段
    public boolean isPkCFing = false;

    //是否静音
    public boolean isMute = false;

    public boolean isAcceptPk = true; //是否接受PK(开播默认打开)

    // 初始化调用一次 onCreate
    private boolean mOnFirstCreate = true;

    OnConnectListener onConnectListener;

    public LovenseToy mLovenseToy;
    public int cpFlag = 0;
    private LiveRoomVerify liveRoomVerify;

    private int changeType = -1;  //类型
    private String checkInfo;

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, AnchorLiveActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//                WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //保持屏幕常亮
        setContentView(R.layout.anchorlive_activity);
        initView();
        isMute = BuildConfig.DEBUG;

        beautyControlView = findViewById(R.id.faceunity_control);
        StatusBarUtil.setStatusBarFulAlpha(AnchorLiveActivity.this);
        BarUtils.setStatusBarVisibility(AnchorLiveActivity.this, true);
        BarUtils.setStatusBarLightMode(AnchorLiveActivity.this, true);

        if (DataCenter.getInstance().getUserInfo().getUser() != null) {
            anchor = DataCenter.getInstance().getUserInfo().getUser().covertToAnchor();
        }

        initBeauty();              // 初始化 美颜
        initPusher();              // 初始化 SDK 推流器
        initListener();            // 初始化电话监听
        addPreviewFragment();      // 添加预览设置界面
        startCameraPreview();      // 开启摄像机预览
        AppIMManager.ins().addMessageListener(AnchorLiveActivity.class, this);

        //校验弹窗
        liveRoomVerify = new LiveRoomVerify();
        liveRoomVerify.setRoomConfirmListener(strText -> {
            if (TextUtils.isEmpty(strText)) {
                ToastUtils.showShort(getString(R.string.verify_error_password));
            } else {
                Api_Live.ins().checkPermissions(strText, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        checkInfo = strText;
                        if (!TextUtils.isEmpty(data) && data.equals("1")) {
                            liveRoomVerify.dismiss();
                            switch (changeType) {
                                case 1:  //改为普通房间
                                    setRoomToNormal();
                                    break;

                                case 2:   //设置为计时房间
                                    setRoomToTiming();
                                    break;

                                case 3: //设置为单次收费
                                    setRoomToSingleTimes();
                                    break;

                                case 4:  //设置为密码房间
                                    setRoomToPassword();
                                    break;
                            }
                        } else {
                            ToastUtils.showShort(getString(R.string.verify_error));
                        }
                    }
                });
            }
        });

        //聊天框弹起监听
        KeyboardUtils.setSoftInputChangedListener(AnchorLiveActivity.this,
                (isOpen, height) -> {
                    LogUtils.e(isOpen + ", " + height);
                    if (isOpen) {
                        if (!StringUtils.isEmpty(mPushUrl)) {
                            //此时是直播界面
                            if (roomModeForPwdFragment == null || !roomModeForPwdFragment.isShow()) {
                                liveControlFragment.onKeyBoardShow(height);
                            }
                        }
                    } else {
                        if (!StringUtils.isEmpty(mPushUrl)) {
                            //此时是直播界面
                            if (roomModeForPwdFragment == null || !roomModeForPwdFragment.isShow()) {
                                liveControlFragment.onKeyBoardHide(height);
                            }
                        }
                    }
                });
    }

    private void initView() {
        mPusherView = findViewById(R.id.video_view);
        mTXCloudVideoView2 = findViewById(R.id.video_view_pk);
        frameLayout_pk = findViewById(R.id.loading_background_pk);
        iv_pkloading = findViewById(R.id.loading_imageview_pk);
        iv_userb_pkbg = findViewById(R.id.iv_userb_pkbg);
    }

    RoomModeForPwdFragment roomModeForPwdFragment;

    /**
     * 显示房间类型设置面板
     *
     * @param isStartLive 是否是主播开播
     */
    public void showLiveRoomTypeSetDialog(boolean isStartLive) {
        LiveRoomChargeSettingFragment liveRoomChargeSettingFragment =
                LiveRoomChargeSettingFragment.newInstance(roomType);
        liveRoomChargeSettingFragment.show(getSupportFragmentManager(), "");
        liveRoomChargeSettingFragment.setBtnClick(id -> {
            switch (id) {
                case R.id.rb_type1: //普通房间
                    if (isStartLive) {
                        setRoomToNormal();
                    } else {
                        changeType = 1;
                    }
                    break;

                case R.id.rb_type2: //计时房间
                    if (isStartLive) {
                        setRoomToTiming();
                    } else {
                        changeType = 2;
                    }
                    break;

                case R.id.rb_type3: //单次收费
                    if (isStartLive) {
                        setRoomToSingleTimes();
                    } else {
                        changeType = 3;
                    }
                    break;

                case R.id.rb_type4:  //密码房间
                    if (isStartLive) {
                        setRoomToPassword();
                    } else {
                        changeType = 4;
                    }
                    break;
            }

            if (!isStartLive) {
                liveRoomVerify.show(getSupportFragmentManager(), VERIFY_DIALOG_KEY);
            }
        });
    }

    /**
     * 设置房间为密码房间
     */
    private void setRoomToPassword() {
        roomModeForPwdFragment = RoomModeForPwdFragment.newInstance(2);
        if (!StringUtils.isEmpty(roomPwd))
            roomModeForPwdFragment.setTvRightdes(roomPwd + "");
        roomModeForPwdFragment.show(getSupportFragmentManager(), "");
        roomModeForPwdFragment.setBtnSureClick(txt -> {
            if (previewFragment != null) {
                //此时在开播预览界面
                previewFragment.refreshRoomType(getString(R.string.passwordRoom) + txt);
                roomType = 3;
                roomPwd = txt;
            } else {
                //此时在开播界面
                doChangeRoomTypeApi(3, -1, txt);
            }
        });
    }

    /**
     * 设置房间为单词收费
     */
    private void setRoomToSingleTimes() {
        roomModeForPwdFragment = RoomModeForPwdFragment.newInstance(1);
        if (roomDcPrice > 0)
            roomModeForPwdFragment.setTvRightdes(roomDcPrice + "");
        roomModeForPwdFragment.show(getSupportFragmentManager(), "");
        roomModeForPwdFragment.setBtnSureClick(txt -> {
            if (previewFragment != null) {
                //此时在开播预览界面
                previewFragment.refreshRoomType(getString(R.string.onceRoom) + txt + getString(R.string.goldOnce));
                roomType = 2;
                roomDcPrice = Integer.parseInt(txt);
            } else {
                //此时在开播界面
                doChangeRoomTypeApi(2, Integer.parseInt(txt), "");
            }
        });
    }

    /**
     * 设置房间为计时房间
     */
    private void setRoomToTiming() {
        RoomModeForTimingFragment roomModeForTimingFragment = RoomModeForTimingFragment.newInstance();
        roomModeForTimingFragment.show(getSupportFragmentManager(), "");
        roomModeForTimingFragment.setBtnClick((selPos, money) -> {
            LogUtils.e("222");
            if (previewFragment != null) {
                //此时在开播预览界面
                previewFragment.refreshRoomType(getString(R.string.timeRoom) + money + getString(R.string.goldMinute));
                roomType = 1;
                roomPrice = money;
            } else {
                //此时在开播界面
                doChangeRoomTypeApi(1, money, "");
            }
        });
    }

    /**
     * 设置房间类型为普通房间
     */
    private void setRoomToNormal() {
        if (previewFragment != null) { //此时在开播预览界面
            previewFragment.refreshRoomType("");
            roomType = 0;
        } else {  //此时在开播界面
            doChangeRoomTypeApi(0, -1, "");
        }
    }

    /**
     * 修改房间类型
     *
     * @param type     房间类型
     * @param price    价格
     * @param password 密码
     */
    public void doChangeRoomTypeApi(int type, int price, String password) {
        Api_Live.ins().changeRoomType(anchor.getLiveId(), type, price, password, checkInfo, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    switch (type) {
                        case 0:
                            roomType = 0;
                            showToastTip(true, getString(R.string.modifyAsFree));
                            break;
                        case 1:
                            roomType = 1;
                            roomPrice = price;
                            showToastTip(true, getString(R.string.modifyAsTime) + roomPrice + getString(R.string.goldMinute));
                            break;
                        case 2:
                            roomType = 2;
                            roomDcPrice = price;
                            showToastTip(true, getString(R.string.modifyAsTimes) + roomDcPrice + getString(R.string.goldOnce));
                            break;
                        case 3:
                            roomType = 3;
                            roomPwd = password;
                            showToastTip(true, getString(R.string.modifyAsPw));
                            break;
                    }
                } else {
                    showToastTip(false, msg);
                }
                if (liveRoomSettingFragment != null) {
                    liveRoomSettingFragment.dismiss();
                }
            }
        });
    }

    LiveRoomSettingFragment liveRoomSettingFragment;

    /**
     * 直播间设置
     */
    public void showLiveSetDialog() {
        liveRoomSettingFragment = LiveRoomSettingFragment.newInstance(roomType, roomPrice, roomPwd, mirror, isFrontCarame);
        liveRoomSettingFragment.show(getSupportFragmentManager(), "");
        liveRoomSettingFragment.setOnRoomSetClick(type -> {
            switch (type) {
                case 1://房间设置
                    showLiveRoomTypeSetDialog(false);
                    break;
                case 2://美颜设置
                    toggleBeautySetting();
                    break;
            }
        });
    }


    RxPermissions rxPermissions;

    public void searchJoy() {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(AnchorLiveActivity.this);
        }
        LogUtils.e("connentJoy");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(AnchorLiveActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(AnchorLiveActivity.this,
                    Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                rxPermissions.request(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH)
                        .subscribe(granted -> {
                            if (granted) {
                                showToyListDialog();
                            } else {
                                // 有的权限被拒绝或被勾选不再提示
                                LogUtils.e("有的权限被拒绝");
                                new AlertDialog.Builder(AnchorLiveActivity.this)
                                        .setCancelable(false)
                                        .setMessage(getString(R.string.GPSpermition))
                                        .setPositiveButton(getString(R.string.see),
                                                (dialog, which) -> LogUtils.e("权限被拒绝"))
                                        .show();
                            }
                        });
            } else {
                showToyListDialog();
            }
        } else {
            showToyListDialog();
        }
    }


    ToyListDialog toyListDialog;

    public void showToyListDialog() {
        toyListDialog = ToyListDialog.newInstance();
        toyListDialog.show(getSupportFragmentManager(), "");
    }

    public void dimissToyListDialog() {
        if (toyListDialog != null && toyListDialog.isShow) {
            toyListDialog.dismiss();
        }
    }

    public void connectToy(LovenseToy lovenseToy) {
        this.mLovenseToy = lovenseToy;
        if (!Lovense.getInstance(getApplication()).isConnected(lovenseToy.getToyId())) {
            showLoadingDialog(getString(R.string.connecting), false, false);
            onConnectListener = new OnConnectListener() {
                @Override
                public void onConnect(String toyId, String status) {
                    switch (status) {
                        case LovenseToy.STATE_CONNECTING:
                            break;
                        case LovenseToy.STATE_CONNECTED:
                            hideLoadingDialog();
                            //连接成功
//                            EventBus.getDefault().post(new ToyConnectEvent(1, toyId));
                            Api_Live.ins().toychange(1, new JsonCallback<String>() {
                                @Override
                                public void onSuccess(int code, String msg, String data) {

                                }
                            });
                            ToastUtils.showShort("Kết nối thành công");
                            if (previewFragment != null) {
                                //此时在开播预览界面
                                previewFragment.changeToyState(mLovenseToy);
                            } else {
                                //此时在开播界面
                            }
                            break;
                        case LovenseToy.STATE_FAILED:
                            hideLoadingDialog();
                            ToastUtils.showShort("Kết nối đồ chơi không thành công");
                            Api_Live.ins().toychange(0, new JsonCallback<String>() {
                                @Override
                                public void onSuccess(int code, String msg, String data) {

                                }
                            });
//                            EventBus.getDefault().post(new ToyConnectEvent(-1, toyId));
                            break;
                        case LovenseToy.SERVICE_DISCOVERED:
                            hideLoadingDialog();
                            Lovense.getInstance(getApplication()).sendCommand(toyId, LovenseToy.COMMAND_GET_DEVICE_TYPE);
                            Lovense.getInstance(getApplication()).sendCommand(toyId, LovenseToy.COMMAND_GET_BATTERY);
                            break;
                    }
                }

                @Override
                public void onError(LovenseError error) {
                    hideLoadingDialog();
                    try {
                        String msg = error.getMessage();
//                        Toast.makeText(ToyActivity.this, msg, Toast.LENGTH_SHORT).show();
                        ToastUtils.showShort(msg);
                        final androidx.appcompat.app.AlertDialog.Builder normalDialog =
                                new androidx.appcompat.app.AlertDialog.Builder(AnchorLiveActivity.this);
                        normalDialog.setTitle(getString(R.string.picture_prompt));
                        normalDialog.setMessage(msg);
                        normalDialog.setPositiveButton(getString(R.string.see),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //...To-do
                                        mLovenseToy = null;
                                    }
                                });
                        normalDialog.setNegativeButton(getString(R.string.retry),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //...To-do
                                        Lovense.getInstance(getApplication()).connectToy(lovenseToy.getToyId(), onConnectListener);
                                    }
                                });
                        normalDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };
            Lovense.getInstance(getApplication()).connectToy(lovenseToy.getToyId(), onConnectListener);
        } else {
            Lovense.getInstance(getApplication()).sendCommand(lovenseToy.getToyId(), LovenseToy.COMMAND_GET_DEVICE_TYPE);
            Lovense.getInstance(getApplication()).sendCommand(lovenseToy.getToyId(), LovenseToy.COMMAND_GET_BATTERY);
            if (previewFragment != null) {
                //此时在开播预览界面
                previewFragment.changeToyState(mLovenseToy);
            }  //此时在开播界面

        }

    }

    PKSettingDialog pKSettingDialog;

    public void showPkDialog() {
        LogUtils.e("isPking============" + isPking);
        if (isPking) {
            DialogFactory.showTwoBtnDialog(this, getString(R.string.PKearlyEnd), new TipDialog.DialogButtonOnClickListener() {
                @Override
                public void onClick(View button, TipDialog dialog) {
                    dialog.dismiss();
                }
            }, new TipDialog.DialogButtonOnClickListener() {
                @Override
                public void onClick(View button, TipDialog dialog) {
                    dialog.dismiss();
                    if (liveRoomSettingFragment != null && liveRoomSettingFragment.isShow) {
                        liveRoomSettingFragment.dismiss();
                    }
                    Api_LiveRecreation.ins().finishPk(new JsonCallback<String>() {
                        @Override
                        public void onSuccess(int code, String msg, String data) {
                            if (data != null) LogUtils.e(data);
                            if (code == 0) {

                            } else {
                                ToastUtils.showShort(msg);
                            }
                        }
                    });
                }
            });
        } else if (isPkCFing) {
            DialogFactory.showTwoBtnDialog(this, getString(R.string.publishEarlyEnd), new TipDialog.DialogButtonOnClickListener() {
                @Override
                public void onClick(View button, TipDialog dialog) {
                    dialog.dismiss();
                }
            }, (button, dialog) -> {
                dialog.dismiss();
                if (liveRoomSettingFragment != null && liveRoomSettingFragment.isShow) {
                    liveRoomSettingFragment.dismiss();
                }
                Api_LiveRecreation.ins().finishPk(new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        if (data != null) LogUtils.e(data);
                        if (code == 0) {

                        } else {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
            });
        } else {
            pKSettingDialog = PKSettingDialog.newInstance(isAcceptPk);
            pKSettingDialog.show(getSupportFragmentManager(), "");
        }
    }

    PKInvertListDialog pKInvertListDialog;

    /**
     * PK好友列表弹框
     */
    public void showPkFriendListDialog(ArrayList<User> data) {
        if (liveRoomSettingFragment != null && liveRoomSettingFragment.isShow) {
            liveRoomSettingFragment.dismiss();
        }
        if (pKSettingDialog != null && pKSettingDialog.isShow) {
            pKSettingDialog.dismiss();
        }
        pKInvertListDialog = PKInvertListDialog.newInstance(data);
        pKInvertListDialog.show(getSupportFragmentManager(), "");
    }

    PKInvertLoadingDialog pKInvertLoadingDialog;

    /**
     * PK邀请等待框
     */
    public void showPKInvertLoadingDialog(long inverUid, String invertPkUserName) {
        pKInvertLoadingDialog = PKInvertLoadingDialog.newInstance(inverUid, invertPkUserName);
        pKInvertLoadingDialog.show(getSupportFragmentManager(), "");
    }


    public void toggleBeautySetting() {
        if (beautyControlView.getVisibility() == View.VISIBLE) {
            beautyControlView.setVisibility(View.GONE);
        } else {
            beautyControlView.setVisibility(View.VISIBLE);
        }
    }

    public void hideBeauty() {
        if (beautyControlView.getVisibility() == View.VISIBLE) {
            beautyControlView.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化 美颜
     */
    private void initBeauty() {
        mFURenderer = new FURenderer
                .Builder(this)
                .inputTextureType(0)
                .createEGLContext(false)
                .needReadBackImage(false)
                .setNeedFaceBeauty(true)
                .defaultEffect(EffectEnum.EffectNone.effect())
                .build();
        beautyControlView.setOnFaceUnityControlListener(mFURenderer);

    }

    /**
     * 初始化 SDK 推流器
     */
    private void initPusher() {
        mLivePusher = new TXLivePusher(this);

//        mLivePusher.setZoom(5);//21.1.10
        mLivePushConfig = new TXLivePushConfig();
        mLivePushConfig.setVideoEncodeGop(3);
        mLivePushConfig.setTouchFocus(false);//false：不开启手动对焦
//        mLivePushConfig.setVideoResolution(VIDEO_RESOLUTION_TYPE_360_640);
        mLivePusher.setConfig(mLivePushConfig);
//        mLivePusher.setBeautyFilter(TXLiveConstants.BEAUTY_STYLE_SMOOTH, 5, 3, 2);

        // 设置自定义视频处理回调，在主播预览及编码前回调出来，用户可以用来做自定义美颜或者增加视频特效
        mLivePusher.setVideoProcessListener(new TXLivePusher.VideoCustomProcessListener() {
            /**
             * 在OpenGL线程中回调，在这里可以进行采集图像的二次处理
             * @param i  纹理ID
             * @param i1      纹理的宽度
             * @param i2     纹理的高度
             * @return 返回给SDK的纹理
             * 说明：SDK回调出来的纹理类型是GLES20.GL_TEXTURE_2D，接口返回给SDK的纹理类型也必须是GLES20.GL_TEXTURE_2D
             */
            @Override
            public int onTextureCustomProcess(int i, int i1, int i2) {
                if (mOnFirstCreate) {
                    mFURenderer.onSurfaceCreated();
                    mOnFirstCreate = false;
                }
                return mFURenderer.onDrawFrameSingleInputTex(i, i1, i2);
            }

            /**
             * 增值版回调人脸坐标
             * @param floats   归一化人脸坐标，每两个值表示某点P的X,Y值。值域[0.f, 1.f]
             */
            @Override
            public void onDetectFacePoints(float[] floats) {

            }

            /**
             * 在OpenGL线程中回调，可以在这里释放创建的OpenGL资源
             */
            @Override
            public void onTextureDestoryed() {
                LogUtils.e("onTextureDestoryed: t:" + Thread.currentThread().getId());
                mFURenderer.onSurfaceDestroyed();
                mOnFirstCreate = true;
            }
        });
    }


    /**
     * 开启摄像头预览
     */
    public void startCameraPreview() {
        RxPermissions rxPermissions = new RxPermissions(AnchorLiveActivity.this);
        rxPermissions.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(granted -> {
                    if (granted) {
                        stopCameraPreview = false;
                        mLivePusher.startCameraPreview(mPusherView);
                    } else {
                        // 有的权限被拒绝或被勾选不再提示
                        new AlertDialog.Builder(AnchorLiveActivity.this)
                                .setCancelable(false)
                                .setMessage(getString(R.string.permitionRun))
                                .setPositiveButton(getString(R.string.see), (dialog, which) -> finish())
                                .show();
                    }
                });
    }


    private void addPreviewFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, previewFragment = PreviewFragment.newInstance())
                .commitAllowingStateLoss();
        previewFragment.setLotteryClick((lotteryNs, name) -> {
            lotteryNames.clear();
            names.clear();
            if (BuildConfig.AppFlavor.equals("QQLive")){
                lotteryNames.addAll(lotteryNs);
                names.addAll(lotteryNs);
            }else {
                lotteryNames.addAll(name);
                names.addAll(name);
            }
        });

        previewFragment.setOnLineClick(line -> liveConfigId = line);
    }

    /**
     * 直播间公告消息
     *
     * @param content 公告内容
     */
    public void sendLiveRoomNotice(String content) {
        int protocol = Constant.MessageProtocol.PROTOCOL_LIVE_BROADCAST;
        Map<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("liveId", anchor.getLiveId());
        map.put("protocol", protocol);
        onIMReceived(protocol, new Gson().toJson(map));
    }

    private void replaceLiveControlFragment() {
        if (liveControlFragment != null) {
            LogUtils.e("送礼消息" + "liveControllFragment不为空");
            return;
        }
        liveControlFragment = LiveControlFragment.newInstance(anchor);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, liveControlFragment)
                .commitAllowingStateLoss();
        previewFragment = null;
        liveControlFragment.sendRoomBulletin(); //发送直播间公告
    }

    /**
     * 开始直播 开启直播
     */
    public void startLive() {
        String lotteryName;
        if (1 == lotteryNames.size()) {
            lotteryName = lotteryNames.get(0);
        } else if (2 == lotteryNames.size()) {
            lotteryName = lotteryNames.get(0) + "," + lotteryNames.get(1);
        } else if (0 == lotteryNames.size()) {
            ToastUtils.showShort(getString(R.string.caiNunber));
            hideLoadingDialog();
            return;
        } else {
            ToastUtils.showShort(getString(R.string.caiNunbera));
            hideLoadingDialog();
            return;
        }

        Api_Live.ins().startLive(liveConfigId, lotteryName, roomType,
                roomType == 1 ? roomPrice : roomDcPrice, roomPwd, new JsonCallback<Anchor>() {
                    @Override
                    public void onSuccess(int code, String msg, Anchor data) {
                        hideLoadingDialog();
                        if (data == null) {
                            ToastUtils.showShort(msg);
                            return;
                        }
                        if (code == 0) {
                            isLiving = true;
                            mPushUrl = data.getPushStreamUrl();
                            liveId = data.getLiveId();
                            roomType = data.getType();
                            moneyRank = data.getZb();
                            roomPrice = data.getPrice();
                            anchor.setLiveId(liveId);
                            anchor.setZb(moneyRank);
                            anchor.setLiveStartLottery(data.getLiveStartLottery());
                            SPUtils.getInstance("liveforanchor").put("liveId", liveId + "");
                            SPUtils.getInstance("liveforanchor").put("anchorId", anchor.getAnchorId() + "");

                            startRTMPPush();
                            //替换业务视图
                            replaceLiveControlFragment();
                            //joinIMGroup();
                            checkAndJoinIM();
                            //开始发送心跳
                            heartHandler.removeMessages(1);
                            heartHandler.sendEmptyMessageDelayed(1, heartSpace);
                            liveControlFragment.isAnchor = true;
                        } else {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
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
        mPusherView.setVisibility(View.VISIBLE);

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
        mLivePusher.setMute(isMute);
//        mLivePusher.setFocusPosition(); 设置焦点
        // 是否开启观众端镜像观看
        mLivePusher.setMirror(mirror);

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
            mLivePusher.startCameraPreview(mPusherView);
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
     * 播放事件
     * <p>
     * 开播前预览流程：
     * 1003,打开摄像头成功
     * 1007,首帧画面采集完成
     * 1008,启动软编
     * <p>
     * 推流流程
     * 1008,启动软编
     * 1001,已经连接rtmp服务器
     * 1002,rtmp开始推流
     * 切换摄像头流程
     * 1003,打开摄像头成功
     * 1007,首帧画面采集完成
     * <p>
     * 网络断开/波动，然后重连流程
     * 3005,Q通道发送失败 返回码[-1] 通道ID[18059384742658963384] 流ID[5] 错误码[27]
     * 3005,Q通道发送失败 返回码[-1] 通道ID[18059384742658963384] 流ID[5] 错误码[27]
     * 1102,启动网络重连
     * 1001,已经连接rtmp服务器
     * 1002,rtmp开始推流
     */
    @Override
    public void onPushEvent(int event, Bundle bundle) {
        LogUtils.e("onPushEvent event : " + event + "," + bundle.getString(TXLiveConstants.EVT_DESCRIPTION));

        if (mPusherView != null) {
            mPusherView.setLogText(null, bundle, event);
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

    /**
     * 开播心跳
     */
    private final Handler heartHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (ActivityUtils.getTopActivity() instanceof AnchorLiveActivity) {
                if (mLivePusher != null && !mLivePusher.isPushing()) {
                    LogUtils.e("mTXLivePusher.isPushing()");
                    // TODO: 2021/12/10 推流失败的处理
                    /**
                     pushStateFailTime++;
                     if (pushStateFailTime > 3) {
                     mLivePusher.stopCameraPreview(false);
                     stopCameraPreview = true;
                     LogUtils.e("您的網絡已斷開,請檢查網絡");
                     stopRTMPPush();
                     showErrorDialog(getString(R.string.netDisconnect));
                     } else {
                     mLivePusher.resumePusher();
                     }
                     */
                    mLivePusher.resumePusher();
                } else {
                    pushStateFailTime = 0;
                }

                Api_Live.ins().liveHeart(anchor.getLiveId(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        LogUtils.e("liveHeart result : " + result + "," + code + "," + msg);
                        if (code == 0) {
                            heartApiFailCount = 0;
                        } else if (code == 3001) { //关闭房间
                            closeLiveRoom(getString(R.string.roomClose), false);
                        } else {
                            heartApiFailCount++;
                            if (heartApiFailCount > 3) {
                                LogUtils.e("您的網絡已斷開,請檢查網絡");
                                stopRTMPPush();
                                showErrorDialog(getString(R.string.netDisconnect));
                            }
                        }
                    }
                });

                heartHandler.removeMessages(1);
                heartHandler.sendEmptyMessageDelayed(1, heartSpace);
            }
            return false;
        }
    });


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

    public void showFinishLiveDialog() {
        if (errorDialog == null) {
            errorDialog = new TipDialog(this);
        }
        errorDialog.setCanceledOnTouchOutside(false);
        errorDialog.setPromptTitle(getString(R.string.sureEndLive));
        errorDialog.setButton1(getString(R.string.picture_quit_audio), (button, dialog) -> {
            dialog.dismiss();
            isLiving = false;
            closeLiveRoom(getString(R.string.endLive), false);
        });

        errorDialog.setButton2(getString(R.string.cancel), (button, dialog) -> dialog.dismiss());
        errorDialog.show();
        errorDialog.setCancelable(false);
    }

    @Override
    public void onNetStatus(Bundle bundle) {
        if (mPusherView != null) {
            mPusherView.setLogText(bundle, null, 0);
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


    /**
     * 关闭直播间
     * 是否是管理员或者后台强制关播
     */
    public void closeLiveRoom(String finishTip, boolean isKick) {
        if (KeyboardUtils.isSoftInputVisible(AnchorLiveActivity.this)) {
            KeyboardUtils.toggleSoftInput();
        }
        LiveFinishActivity.startActivity(AnchorLiveActivity.this, anchor, finishTip, isKick);
        isCloseRooming = true;
        stopRTMPPush();
        heartHandler.removeMessages(1);
        //游戏退出
        Api_Pay.ins().kickout(DataCenter.getInstance().getUserInfo().getUser().getUid() + "", new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {

            }
        });
        finish();
    }

    public void checkAndJoinIM() {
        String imLoginUser = V2TIMManager.getInstance().getLoginUser();
        //加入群聊前 先判断用户是否连接IM 如果未连接 则先连接IM后再加入IM群聊
        if (TextUtils.isEmpty(imLoginUser)) {
            sendSystemMsgToChat(getString(R.string.chatRetry));
            //当前IM未连接用户 则先让用户连接IM后
            AppIMManager.ins().connectIM(new V2TIMCallback() {
                @Override
                public void onSuccess() {
                    //连接IM成功后 加入群聊
                    sendSystemMsgToChat(getString(R.string.connectedJoin));
                    mHandler.postDelayed(() ->
                            sendSystemMsgToChat(getString(R.string.liveSuccess)), 1600);
                }

                @Override
                public void onError(int code, String desc) {
                    LogUtils.e("IM->onError:" + code + "，" + desc);
                    if (6017 == code && "sdk not initialized".equals(desc)) {
                        finish();
                    } else if (code == 9506) { //9506调用频率限制，最大每秒发起5次请求。
                        closeLiveRoom(desc, false);
                    } else if (code == 9520) {
                        closeLiveRoom(desc, false);
                    } else if (code == 6012) {
                        sendSystemMsgToChat(getString(R.string.chatRetrying) + code);
                        checkAndJoinIM();
                    } else {
                        closeLiveRoom(desc, false);
                    }
                }
            });
        } else {
            mHandler.postDelayed(() ->
                    sendSystemMsgToChat(getString(R.string.liveSuccess)), 1600);
        }
    }


    //开始PK
    public void startPK(String pkStreamUrl, long pkReqUid, String pkAvatar, String pkNickName) {
        LogUtils.e("PK 开始pk");
        isPking = true;
        isPkCFing = false;
        //第一，将主播画面变小放置左边
        adjustFullScreenVideoView(false);
        //显示PK的一些View
        mTXCloudVideoView2.setVisibility(View.VISIBLE);
        //加载动画
        showPKLoadingAnimation(true);
        //开始播放对方的流
        startPlayPKStream(pkStreamUrl, pkReqUid);
        //显示Pk界面
        liveControlFragment.setPkView(0, -1, null);

    }

    //结束pk
    public void finishPK() {
        if (!isPking && !isPkCFing) return;
        LogUtils.e("PK 结束pk");
        isPking = false;
        isPkCFing = false;

        liveControlFragment.setPkView(1, -1, null);
        //将主播画面布局还原成最大
        adjustFullScreenVideoView(true);
        //加载动画
        showPKLoadingAnimation(false);
        //隐藏之前小主播的小窗体
        mTXCloudVideoView2.setVisibility(View.GONE);
        //结束播放对方的流
        stopPlayPKStream();
    }

    /**
     * 主播PK：结束播放对方的流
     */
    public void stopPlayPKStream() {
        // 1 结束视频播放
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(true);
            mTXCloudVideoView2.onDestroy();
        }

        // 2 取消自己的和对方主播的混流
        delPKVideoStream(bigAnchorId);
        delPKVideoStream(smallAnchorId);

        // 3 调整推流参数，切换为直播模式
        if (mLivePusher != null && mLivePusher.isPushing()) {
            mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION,
                    false, false);
            TXLivePushConfig config = mLivePusher.getConfig();
            config.enableAEC(false);
            config.setVideoEncodeGop(5);
            mLivePusher.setConfig(config);
        }

    }


    public void adjustFullScreenVideoView(boolean fullScreen) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout_push);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) frameLayout.getLayoutParams();
        if (fullScreen) {
            LogUtils.e("全屏");
            layoutParams.removeRule(RelativeLayout.BELOW);
            layoutParams.removeRule(RelativeLayout.ABOVE);
            layoutParams.removeRule(RelativeLayout.LEFT_OF);
        } else {
            LogUtils.e("PK 小屏");
            layoutParams.addRule(RelativeLayout.BELOW, R.id.frame_layout_pross);
            layoutParams.addRule(RelativeLayout.ABOVE, R.id.view_center);
            layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.view_center);
        }
        frameLayout.setLayoutParams(layoutParams);
    }

    public void startPlayPKStream(String pKStreamUrl, long pkReqUid) {
        LogUtils.e("playUrl  " + pKStreamUrl);
        // 1 调整推流参数，切换为连麦模式
        if (mLivePusher != null && mLivePusher.isPushing()) {
            mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER,
                    true, true);
            TXLivePushConfig config = mLivePusher.getConfig();
            config.setVideoResolution(VIDEO_RESOLUTION_TYPE_360_640);
            config.enableAEC(true);
            config.setAutoAdjustBitrate(false);
            config.setVideoBitrate(800);
            //设置视频编码GOP
//            config.setVideoEncodeGop(1);
            mLivePusher.setConfig(config);
        }

        // 2 播放加速流地址，拉取视频
        if (!PlayerUtils.checkPlayUrl(pKStreamUrl, this)) {
            return;
        }


        mTXLivePlayer = new TXLivePlayer(this);
        mTXCloudVideoView2.setVisibility(View.VISIBLE);
        mTXLivePlayer.setPlayerView(mTXCloudVideoView2);
        mTXLivePlayer.enableHardwareDecode(true);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);

        mTXLivePlayer.setPlayListener(new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(final int event, final Bundle param) {
                if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    LogUtils.e("播放 开始播放");
                    //开始混流
                    addPKVideoStream(anchor.getAnchorId() + "", pkReqUid + "");
                    showPKLoadingAnimation(false);
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END ||
                        event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
//                    callbackOnThread(callback, "onError", event, "[LivePlayer] 播放异常[" +
//                    param.getString(TXLiveConstants.EVT_DESCRIPTION) + "]");
                    LogUtils.e("播放 异常 " + event + "," + param.getString(TXLiveConstants.EVT_DESCRIPTION));
                    if (isFinishing() || isDestroyed()) return;
                    DialogFactory.showOneBtnDialog(AnchorLiveActivity.this,
                            getString(R.string.failMore), getString(R.string.good), new TipDialog.DialogButtonOnClickListener() {
                                @Override
                                public void onClick(View button, TipDialog dialog) {
                                    dialog.dismiss();
                                    if (liveRoomSettingFragment != null && liveRoomSettingFragment.isShow) {
                                        liveRoomSettingFragment.dismiss();
                                    }
                                    Api_LiveRecreation.ins().finishPk(new JsonCallback<String>() {
                                        @Override
                                        public void onSuccess(int code, String msg, String data) {
                                            if (data != null) LogUtils.e(data);
                                            if (code == 0) {

                                            } else {
                                                ToastUtils.showShort(msg);
                                            }
                                        }
                                    });
                                }
                            });
                } else {
                    LogUtils.e("播放 异常 " + event + "," + param.getString(TXLiveConstants.EVT_DESCRIPTION));
//                    callbackOnThread(callback, "onEvent", event, param);
                }
            }

            @Override
            public void onNetStatus(Bundle status) {

            }
        });

        //PLAY_TYPE_LIVE_RTMP
//        pKStreamUrl = "rtmp://pull2.nv23p.cn/live/2";
        //PLAY_TYPE_LIVE_RTMP_ACC
//        pKStreamUrl = "rtmp://pull5.nv23p.cn/live/290210172?txSecret=3310a9728a5b2340119202e15c327c64&txTime=5DD12284";

        LogUtils.e("playUrl " + pKStreamUrl);
        int result = mTXLivePlayer.startPlay(pKStreamUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC);
        if (result != 0) {
            ToastUtils.showShort(getString(R.string.playFail));
        }
        LogUtils.e("result " + result);


    }

    private void showPKLoadingAnimation(boolean show) {
        frameLayout_pk.setVisibility(show ? View.VISIBLE : View.GONE);
        iv_userb_pkbg.setVisibility(show ? View.VISIBLE : View.GONE);
        iv_pkloading.setVisibility(show ? View.VISIBLE : View.GONE);

        iv_pkloading.setImageResource(R.drawable.linkmic_loading);
        AnimationDrawable ad = (AnimationDrawable) iv_pkloading.getDrawable();
        if (show) {
            LogUtils.e("aaaaaa5");
            ad.start();
        } else {
            LogUtils.e("aaaaaa6");
            ad.stop();
        }
    }

    String bigAnchorId;
    String smallAnchorId;

    public void addPKVideoStream(String bigAnchorId, String smallAnchorId) {
        this.bigAnchorId = bigAnchorId;
        this.smallAnchorId = smallAnchorId;
        final JSONObject requestParam = createPKRequestParam();
        if (requestParam == null) {
            return;
        }
        //将混流参数转为json字符串
        internalSendRequest(5, true, requestParam.toString());
    }

    public void delPKVideoStream(String uid) {
        if (StringUtils.isEmpty(uid)) return;

        final JSONObject requestParam = cancelPKRequestParam(Long.parseLong(uid));
        if (requestParam == null) {
            return;
        }
        //将混流参数转为json字符串
        internalCancelSendRequest(1, true, requestParam.toString());
    }


    private void internalSendRequest(final int retryIndex, final boolean runImmediately, final String requestParam) {
        new Thread() {
            @Override
            public void run() {
                if (runImmediately == false) {
                    try {
                        sleep(2000, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Api_LiveRecreation.ins().mergestream(requestParam, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        if (data != null) LogUtils.e(data);
                        if (code == 0) {
                            if (Integer.parseInt(data) == 0) {
                                SPUtils.getInstance("pkhunliu").put("uid", bigAnchorId);
                                SPUtils.getInstance("pkhunliu").put("pkUid", smallAnchorId);
                            } else {
                                int tempRetryIndex = retryIndex - 1;
                                LogUtils.e("sendPkRsp1 : " + tempRetryIndex);
                                if (tempRetryIndex > 0) {
                                    LogUtils.e("sendPkRsp2 : " + tempRetryIndex);
                                    internalSendRequest(tempRetryIndex, false, requestParam);
                                } else if (tempRetryIndex == 0) {
                                    //五次请求混流不成功后，提示大小主播结束PK
                                    LogUtils.e("sendPkRsp3 : " + tempRetryIndex);
                                    ToastUtils.showShort(getString(R.string.PKfailRetry));
                                    Api_LiveRecreation.ins().cancelPk(anchor.getAnchorId(), new JsonCallback<String>() {
                                        @Override
                                        public void onSuccess(int code, String msg, String data) {
                                            if (data != null) LogUtils.e(data);
                                            if (code == 0) {
                                                //如果请求成功，后台服务会推送消息31走关闭PK流程
                                            } else {
                                                //如果请求不成功，将强制调用关闭PK流程
                                                finishPK();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        }.start();
    }

    private void internalCancelSendRequest(final int retryIndex, final boolean runImmediately, final String requestParam) {
        new Thread() {
            @Override
            public void run() {
                if (!runImmediately) {
                    try {
                        sleep(2000, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Api_LiveRecreation.ins().mergestream(requestParam, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        if (code == 0 && data != null) {
                            if (Integer.parseInt(data) == 0) {
                                SPUtils.getInstance("pkhunliu").clear();
                            } else {
                                int tempRetryIndex = retryIndex - 1;
                                LogUtils.e("sendPkRsp1 : " + tempRetryIndex);
                                if (tempRetryIndex > 0) {
                                    LogUtils.e("sendPkRsp2 : " + tempRetryIndex);
                                    internalCancelSendRequest(tempRetryIndex, false, requestParam);
                                }
                            }
                        }
                    }
                });
            }
        }.start();
    }


    private JSONObject createPKRequestParam() {
        if (StringUtils.isEmpty(bigAnchorId) || StringUtils.isEmpty(smallAnchorId)) {
            return null;
        }
        String mMainStreamId = bigAnchorId;
        String mPKStreamId = smallAnchorId;

        if (mMainStreamId == null || mMainStreamId.length() == 0) {
            return null;
        }

        JSONObject requestParam = null;

        try {
            // input_stream_list
            JSONArray inputStreamList = new JSONArray();

            if (mPKStreamId != null && mPKStreamId.length() > 0) {
                // 画布
                {
                    JSONObject layoutParam = new JSONObject();
                    layoutParam.put("image_layer", 1);
                    layoutParam.put("input_type", 3);
                    layoutParam.put("image_width", 720);
                    layoutParam.put("image_height", 640);

                    JSONObject canvasStream = new JSONObject();
                    canvasStream.put("input_stream_id", mMainStreamId);
                    canvasStream.put("layout_params", layoutParam);

                    inputStreamList.put(canvasStream);
                }

                // mainStream
                {
                    JSONObject layoutParam = new JSONObject();
                    layoutParam.put("image_layer", 2);
                    layoutParam.put("image_width", 360);
                    layoutParam.put("image_height", 640);
                    layoutParam.put("location_x", 0);
                    layoutParam.put("location_y", 0);

                    JSONObject mainStream = new JSONObject();
                    mainStream.put("input_stream_id", mMainStreamId);
                    mainStream.put("layout_params", layoutParam);

                    inputStreamList.put(mainStream);
                }

                // subStream
                {
                    JSONObject layoutParam = new JSONObject();
                    layoutParam.put("image_layer", 3);
                    layoutParam.put("image_width", 360);
                    layoutParam.put("image_height", 640);
                    layoutParam.put("location_x", 360);
                    layoutParam.put("location_y", 0);

                    JSONObject mainStream = new JSONObject();
                    mainStream.put("input_stream_id", mPKStreamId);
                    mainStream.put("layout_params", layoutParam);

                    inputStreamList.put(mainStream);
                }
            } else {
                JSONObject layoutParam = new JSONObject();
                layoutParam.put("image_layer", 1);

                JSONObject canvasStream = new JSONObject();
                canvasStream.put("input_stream_id", mMainStreamId);
                canvasStream.put("layout_params", layoutParam);

                inputStreamList.put(canvasStream);
            }

            // para
            JSONObject para = new JSONObject();
            para.put("app_id", Constant.LiveAppId);
            para.put("interface", "mix_streamv2.start_mix_stream_advanced");
            para.put("mix_stream_session_id", mMainStreamId);
            para.put("output_stream_id", mMainStreamId);
            para.put("input_stream_list", inputStreamList);

            // interface
            JSONObject interfaceObj = new JSONObject();
            interfaceObj.put("interfaceName", "Mix_StreamV2");
            interfaceObj.put("para", para);

            // requestParam
            requestParam = new JSONObject();
            requestParam.put("timestamp", System.currentTimeMillis() / 1000);
            requestParam.put("eventId", System.currentTimeMillis() / 1000);
            requestParam.put("interface", interfaceObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParam;

    }

    private JSONObject cancelPKRequestParam(long bigAnchorId) {

        String mMainStreamId = bigAnchorId + "";
        if (mMainStreamId.length() == 0) {
            return null;
        }
        JSONObject requestParam = null;
        try {
            JSONObject para = new JSONObject();
            para.put("app_id", Constant.LiveAppId);
            para.put("interface", "mix_streamv2.cancel_mix_stream");
            para.put("mix_stream_session_id", mMainStreamId);
            para.put("output_stream_id", mMainStreamId);

            // interface
            JSONObject interfaceObj = new JSONObject();
            interfaceObj.put("interfaceName", "Mix_StreamV2");
            interfaceObj.put("para", para);

            // requestParam
            requestParam = new JSONObject();
            requestParam.put("timestamp", System.currentTimeMillis() / 1000);
            requestParam.put("eventId", System.currentTimeMillis() / 1000);
            requestParam.put("interface", interfaceObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestParam;
    }

    /**
     * 旋转相机
     */
    public void switchCamera() {
        if (mLivePusher != null) {
            isFrontCarame = !isFrontCarame;
            mLivePusher.switchCamera();
            mFURenderer.onCameraChange(isFrontCarame ? Camera.CameraInfo.CAMERA_FACING_FRONT :
                    Camera.CameraInfo.CAMERA_FACING_BACK, 0);
        }
    }

    /**
     * 设置镜像
     */
    public void setJx(boolean isMirror) {
        if (mLivePusher != null) {
            this.mirror = isMirror;
            mLivePusher.setMirror(isMirror);
        }
    }

    /**
     * 设置镜像
     */
    public void switchJx() {
        mirror = !mirror;
        setJx(mirror);
    }

    /**
     * 设置声音
     */
    public void setAnchorMute(boolean ismute) {
        this.isMute = ismute;
        if (mLivePusher != null) {
            mLivePusher.setMute(ismute);
        }
    }

    /**
     * 更改高清和标清
     */
    public void updateHd(boolean isHd) {
        if (mLivePusher != null) {
            if (isHd) {
                mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION, false, false);
            } else {
                mLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, false, false);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPusherView != null) {
            mPusherView.onResume();
        }

        if (mTXCloudVideoView2 != null) {
            mTXCloudVideoView2.onResume();
        }

        LogUtils.e("onResume");

        if (mLivePusher != null) {
            LogUtils.e("mLivePusher");
            mLivePusher.resumePusher();
            mLivePusher.resumeBGM();
        }

        //hbHandler.sendEmptyMessage(0);
        if (anchor.getAnchorId() != 0 && isLiving) {
            LogUtils.e("leaveSwitch");
            Api_Live.ins().leaveSwitch(anchor.getLiveId(), false, new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    LogUtils.e("leaveSwitch result : " + data);
                    if (code == 3001) {
//                        stopPublish();
//                        //退出聊天室
//                        exitRommChat();
                        closeLiveRoom(getString(R.string.roomClose), false);
                    } else {
                        heartHandler.removeMessages(1);
                        heartHandler.sendEmptyMessage(1);
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPusherView != null) {
            mPusherView.onPause();
        }
        if (mLivePusher != null) {
            mLivePusher.pausePusher();
            mLivePusher.pauseBGM();
        }

//        hbHandler.removeMessages(0);
        if (anchor.getAnchorId() != 0 && isLiving) {
            Api_Live.ins().leaveSwitch(anchor.getLiveId(), true, new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {

                }
            });
        }
    }

    //显示一条系统消息在聊天框里
    public void sendSystemMsgToChat(String content) {
        int protocol = Constant.MessageProtocol.PROTOCOL_MyCoust;
        Map<String, Object> map = new HashMap<>();
        map.put("content", content);
        map.put("liveId", anchor.getLiveId());
        map.put("protocol", protocol);
        onIMReceived(protocol, new Gson().toJson(map));
    }

    PKInvertDialog pkInvertDialog;

    List<LushToy> lushToyList = new ArrayList<>();

    boolean isPlayToy = false;

    @Override
    public void onIMReceived(int protocol, String msg) {
        try {
            JSONObject message = new JSONObject(msg);
            switch (protocol) {
                case Constant.MessageProtocol.PROTOCOL_LIVE_CLOSE: //2.关播/强制关播消息
                    //2 关播消息
                    long liveId = message.optLong("liveId");
                    if (liveId == this.liveId && !isCloseRooming) {
                        boolean isKick = message.optBoolean("isKick");
                        closeLiveRoom(isKick ? getString(R.string.forceOfficeCloseLive) : getString(R.string.endLive), isKick);
                        //取消自己混流
                        delPKVideoStream(bigAnchorId);
                        delPKVideoStream(smallAnchorId);
                    }
                    break;
                case Constant.MessageProtocol.PROTOCOL_RECEIVE_GIFT: //7.送礼消息
                    int lushType = message.optInt("lushType", -1);
                    if (lushType != -1) {  //说明有跳蛋玩具
                        if (mLovenseToy != null) {
                            int lushLevel = message.optInt("lushLevel", 1);
                            int lushTime = message.optInt("lushTime", 1);
                            if (!isPlayToy) {
                                isPlayToy = true;
                                Lovense.getInstance(getApplication()).sendCommand(mLovenseToy.getToyId(),
                                        LovenseToy.COMMAND_VIBRATE, lushLevel);
                                toyHandler.removeMessages(1);
                                toyHandler.sendEmptyMessageDelayed(1, lushTime * 1000L);
                            } else {
                                lushToyList.add(new LushToy(lushType, lushLevel, lushTime));
                            }
                        }
                    }
                    break;
                case Constant.MessageProtocol.PROTOCOL_LIVE_CLOSELIVE:  //14 强制关播消息
                    if (message.optLong("liveId") == this.liveId && !isCloseRooming) {
                        closeLiveRoom(getString(R.string.forceCloseLive), true);
                        //取消自己混流
                        delPKVideoStream(bigAnchorId);
                        delPKVideoStream(smallAnchorId);
                    }
                    break;
                case Constant.MessageProtocol.PROTOCOL_LIVE_PK_MSG:  //19 PK请求响应消息 type 0发起请求  1拒绝请求  2同意请求
                    int type = message.optInt("type", -1);
                    if (type < 0 || type > 2) return;
                    String pullSource = message.optString("pullSource", "");
                    long pkReqUid = message.optLong("pkReqUid", 0);
                    String pkReqAvatar = message.optString("pkReqAvatar", "");
                    String pknickName = message.optString("pknickName", "");
                    switch (type) {
                        case 0: //收到PK请求
                            if (liveRoomSettingFragment != null && liveRoomSettingFragment.isShow) {
                                liveRoomSettingFragment.dismiss();
                            }
                            if (pKSettingDialog != null && pKSettingDialog.isShow) {
                                pKSettingDialog.dismiss();
                            }
                            if (pkInvertDialog != null && pkInvertDialog.isShow) {
                                pkInvertDialog.dismiss();
                            }
                            if (pKInvertListDialog != null && pKInvertListDialog.isShow) {
                                pKInvertListDialog.dismiss();
                            }
                            pkInvertDialog = PKInvertDialog.newInstance(pullSource, pkReqUid, pkReqAvatar, pknickName);
                            pkInvertDialog.show(getSupportFragmentManager(), "");
                            break;
                        case 1: //对方拒绝PK
                            if (pKInvertLoadingDialog != null && pKInvertLoadingDialog.isShow) {
                                pKInvertLoadingDialog.dismiss();
                            }
                            if (pKSettingDialog != null && pKSettingDialog.isShow) {
                                pKSettingDialog.dismiss();
                            }
                            if (pKInvertListDialog != null && pKInvertListDialog.isShow) {
                                pKInvertListDialog.dismiss();
                            }
                            DialogFactory.showTwoBtnDialog(AnchorLiveActivity.this,
                                    getString(R.string.refusePKinvited), getString(R.string.see), getString(R.string.reChoose),
                                    (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                                        dialog.dismiss();
                                        showPkDialog();
                                    });
                            break;
                        case 2: //对方同意PK
                            if (pKInvertLoadingDialog != null && pKInvertLoadingDialog.isShow) {
                                pKInvertLoadingDialog.dismiss();
                            }
                            if (pKSettingDialog != null && pKSettingDialog.isShow) {
                                pKSettingDialog.dismiss();
                            }
                            startPK(pullSource, pkReqUid, pkReqAvatar, pknickName);
                            break;
                    }
                    break;
                case Constant.MessageProtocol.PROTOCOL_PK_START_STOP:  //18 PK结束
                    int status = message.optInt("status", 2);
                    LogUtils.e("PK开启关闭消息 " + status);
                    if (status == 1) {
                        //直播间的主播开始PK
                        SPUtils.getInstance("pk").put("start", true);
                    } else {
                        SPUtils.getInstance("pk").put("start", false);
                        //直播间的主播结束PK
                        finishPK();
                    }
                    break;
                case Constant.MessageProtocol.PROTOCOL_CANCEL_PK:  //22 对方取消PK匹配
                    isPking = false;
                    isPkCFing = false;
                    if (pkInvertDialog != null && pkInvertDialog.isShow) {
                        ToastUtils.showShort(getString(R.string.cancelPKinvite));
                        pkInvertDialog.dismiss();
                    }
                    break;
                case Constant.MessageProtocol.PROTOCOL_PK_RESULT:  //23 PK结果消息
                    int result = message.optInt("result", 0);
                    if (isPking) {
                        isPking = false;
                        isPkCFing = true;
                        liveControlFragment.setPkView(2, result, null);
                        if (liveRoomSettingFragment != null && liveRoomSettingFragment.isShow) {
                            liveRoomSettingFragment.updatePk();
                        }
                    }
                    break;
                case Constant.MessageProtocol.PROTOCOL_PK_VALUE_CHANGE:  //24 PK比分变动消息
                    long anchorA = message.optLong("anchorA", 0);
                    long scoreA = message.optLong("scoreA", 0);
                    long anchorB = message.optLong("anchorB", 0);
                    long scoreB = message.optLong("scoreB", 0);

                    JSONArray mA = message.optJSONArray("listA");
                    JSONArray mB = message.optJSONArray("listB");
                    List<User> mListA = new Gson().fromJson(mA.toString(), new TypeToken<ArrayList<User>>() {
                    }.getType());
                    List<User> mListB = new Gson().fromJson(mB.toString(), new TypeToken<ArrayList<User>>() {
                    }.getType());

                    if (isPking) {
                        liveControlFragment.refreshPkScore(scoreA, scoreB, mListA, mListB);
                    }
                    break;

            }
            if (liveControlFragment != null) {
                liveControlFragment.onReceived(protocol, message);
            } else {
                LogUtils.e("liveControllFragment为空");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e("onReceived Exception : " + e.toString());
        }
    }


    private final Handler toyHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (lushToyList.size() > 0) {
                LushToy lushToy = lushToyList.get(0);
                lushToyList.remove(0);
                isPlayToy = true;
                Lovense.getInstance(getApplication()).sendCommand(mLovenseToy.getToyId(), LovenseToy.COMMAND_VIBRATE, lushToy.getLushLevel());
                toyHandler.removeMessages(1);
                toyHandler.sendEmptyMessageDelayed(1, lushToy.getLushTime() * 1000L);
            } else {
                isPlayToy = false;
                toyHandler.removeMessages(1);
                if (mLovenseToy != null) {
                    Lovense.getInstance(getApplication()).sendCommand(mLovenseToy.getToyId(), LovenseToy.COMMAND_VIBRATE, 0);
                }
            }
            return false;
        }
    });

    /**
     * 初始化电话监听
     */
    private void initListener() {
        mPhoneListener = new TXPhoneStateListener(mLivePusher);
        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
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


    @Override
    public void onBackPressed() {
        if (liveControlFragment != null && liveControlFragment.isAdded()) {
            mHandler.post(this::showFinishLiveDialog);
        } else {
            finish();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        lushToyList.clear();
        isPlayToy = false;
        toyHandler.removeMessages(1);
        if (mLovenseToy != null) {
            Lovense.getInstance(getApplication()).disconnect(mLovenseToy.getToyId());
        }

        heartHandler.removeMessages(1);

        AppIMManager.ins().removeMessageReceivedListener(AnchorLiveActivity.class);

        stopRTMPPush(); // 停止推流
        if (mPusherView != null) {
            mPusherView.onDestroy(); // 销毁 View
        }

        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);
    }

}
