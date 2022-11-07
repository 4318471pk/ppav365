package com.live.fox;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.dialog.MMToast;
import com.live.fox.dialog.NotificationDialog;
import com.live.fox.dialog.UpdateFragmentBinding;
import com.live.fox.dialog.WebViewDialog;
import com.live.fox.entity.AppUpdate;
import com.live.fox.entity.CountryCode;
import com.live.fox.entity.User;
import com.live.fox.entity.WebViewDialogEntity;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.NotificationManager;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_LiveRecreation;
import com.live.fox.server.Api_User;
import com.live.fox.ui.AuthActivity;
import com.live.fox.ui.chat.ChatListFragment;
import com.live.fox.ui.game.GameFragment;
import com.live.fox.ui.home.ActivityFragment;
import com.live.fox.ui.home.AgencyCenterFragment;
import com.live.fox.ui.home.HomeFragment;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.ui.mine.MineFragment;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.CleanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.FileUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.ToastViewUtils;
import com.live.fox.view.RadioButtonWithAnim;
import com.live.fox.windowmanager.WindowUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.v2.V2TIMCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private HomeFragment homeFragment;
    private ChatListFragment playFragment;
    private GameFragment gameFragment;
    private MineFragment mineFragment;
    private AgencyCenterFragment agencyCenterFragment;
    private ActivityFragment activityFragment;
    private int whichPage;
    private boolean isShowNotification;
    private boolean isCloseNotice;
    private static final String CLOSE_NOTICE_KEY = "close_notice_key";
    private static final String LAUNCH_PAGE_KEY = "page";
    private RadioGroup radioGroup;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void startActivity(Context context, boolean closeNote) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(CLOSE_NOTICE_KEY, closeNote);
        context.startActivity(intent);
    }

    public static void startActivityWithPosition(Context context, int position) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(LAUNCH_PAGE_KEY, position);
        context.startActivity(intent);
    }

    public void setWindowsFlag()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        setFullscreen(true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (CommonApp.isNotificationClicked) {
            finish();
            return;
        }
        setContentView(R.layout.main_activity);

        setWindowsFlag();
        initView();
        AppConfig.initSmartRefreshLayout(this);
        checkNotifications();
        if (getIntent() != null) {
            whichPage = getIntent().getIntExtra(LAUNCH_PAGE_KEY, 0);
            isCloseNotice = getIntent().getBooleanExtra(CLOSE_NOTICE_KEY, false);
        }

        initData();
    }

    private void initData() {
        if (!NotificationManager.getInstance().isRegister()) {
            NotificationManager.getInstance().register(CommonApp.getInstance());
        }

        doLoginGuest();//无论有没有token都给他登录刷新到最新到token

        if (DataCenter.getInstance().getUserInfo().isLogin()) {
            getCountryCode();
            if (!NotificationManager.getInstance().isBindingUser()) {
                NotificationManager.getInstance().registerUserID(CommonApp.getInstance());
            }
        }

    }

    public void doLoginGuest() {
        showLoadingDialogWithNoBgBlack();
        Api_Auth.ins().guestLogin( new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                try {
                    if (code == 0) {
                        JSONObject jsonObject = new JSONObject(data);
                        String token = jsonObject.optString("token", "");
                        if (StringUtils.isEmpty(token)) {
                            ToastUtils.showShort(getString(R.string.tokenFail));
                            return;
                        }
                        onLoginSuccess(token);
                    } else {
                        ToastUtils.showShort(msg);
                    }
                } catch (Exception e) {
                    ToastUtils.showShort(e.getMessage());
                }
            }
        });
    }

    //登录成功、完善用户信息成功后的统一处理
    public void onLoginSuccess(String token) {
        DataCenter.getInstance().getUserInfo().setToken(token);
        Api_User.ins().getUserInfo(-1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String userJson) {
                hideLoadingDialog();
                if (code == 0) {
                    initTable();
                } else {
                    SPManager.clearUserInfo();
                    if (code == 993) {
                        ToastUtils.showShort(getString(R.string.accountStop));
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });

        //获取基础信息 IM token appid之类的东西
        Api_User.ins().getBaseInfoWithToken( new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String Str) {
                if (code == 0) {
                    JSONObject jsonObject= null;
                    try {
                        jsonObject = new JSONObject(Str);
                        JSONArray jsonArray= jsonObject.optJSONArray("configSystemBaseList");
                        if(jsonArray!=null)
                        {
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject json= jsonArray.optJSONObject(i);
                                String mCode=json.optString("code","");
                                String value=json.optString("value","");
                                switch (mCode)
                                {
                                    case "tencent.im.sdkappid":
                                        //腾讯im APPID
                                        AppIMManager.init(value);
                                        connectIM();
                                        break;
                                    case "tencent.im.identifier":
                                        break;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    ToastUtils.showShort("IM TOKEN ERROR");
                }
            }
        });


    }

    public void getCountryCode() {
        Api_Auth.ins().countryCodeList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                try {
                    Type type = new TypeToken<List<CountryCode>>() {
                    }.getType();
                    List countryCodes = new Gson().fromJson(data, type);
                    if (countryCodes != null && countryCodes.size() > 0) {
                        SPManager.setCountryCode(data);
                    }

                } catch (Exception e) {
                    ToastUtils.showShort(e.getMessage());
                }
            }
        });
    }

    /**
     * 检测是否开启了通知权限
     */
    private void checkNotifications() {
        boolean notificationEnable = NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled();
        boolean dialogIsShowed = SPUtils.getInstance().getBoolean(ConstantValue.NOTIFICATION_IS_SHOWED, false);
        if (!notificationEnable && !dialogIsShowed) {
            isShowNotification = true;
            NotificationDialog notificationDialog = new NotificationDialog();
            notificationDialog.show(getSupportFragmentManager(), "notification dialog");
        }
    }

    public void initView() {
        //findViewById(R.id.layout_openlive).setOnClickListener(this);

        if (!isShowNotification) {
            doCheckUpdate();
            doCheckAppNotice();
        }
        //檢查並做一些未完成的工作
        doCheckUnfinishedWork();
    }

    private void initTable() {
        radioGroup = findViewById(R.id.main_tab_radio_group);
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            switch (i) {
                case R.id.main_rb_home:  //首页
                    showHomeFragment();
                    break;
                case R.id.main_rb_live_list: //直播列表
                    if (!DataCenter.getInstance().getUserInfo().isLogin()) {
                        radioGroup.clearCheck();
                        LoginModeSelActivity.startActivity(context);
                        return;
                    }
                    showLiveFragment();
                    break;

                case R.id.layout_openlive:
//                    if (!DataCenter.getInstance().getUserInfo().isLogin()) {
//                        radioGroup.clearCheck();
//                        LoginModeSelActivity.startActivity(context);
//                        return;
//                    }
                    showAgencyFragment();
                    break;
                case R.id.main_rb_game: //游戏
//                    if (!DataCenter.getInstance().getUserInfo().isLogin()) {
//                        radioGroup.clearCheck();
//                        LoginModeSelActivity.startActivity(context);
//                        return;
//                    }
                    showActFragment();
                   // showGameFragment();
                    break;
                case R.id.main_rb_mine: //个人中心
                    if (!DataCenter.getInstance().getUserInfo().isLogin()) {
                        radioGroup.clearCheck();
                        LoginModeSelActivity.startActivity(context);
                        return;
                    }
                    showMineFragment();
                    break;
            }
        });

        radioGroup.check(R.id.main_rb_home);

        // TODO: 12/14/2021 我不知为什么这里多语言偶尔不能切换
        RadioButtonWithAnim home = findViewById(R.id.main_rb_home);
        RadioButtonWithAnim live = findViewById(R.id.main_rb_live_list);
        RadioButtonWithAnim game = findViewById(R.id.main_rb_game);
        RadioButtonWithAnim mine = findViewById(R.id.main_rb_mine);
        String lang = MultiLanguageUtils.getRequestHeader();
        switch (lang) {
            case "vi":
                home.setText("Trang chủ");
                live.setText("Trực tiếp");
                game.setText("Trò chơi");
                mine.setText("Của tôi");
                break;
            case "th":
                home.setText("หน้าแรก");
                live.setText("ถ่ายทอดสด");
                game.setText("เกม");
                mine.setText("ของฉัน");
                break;
            case "zh":
                break;
            case "en":
                break;
        }
    }

    /**
     * 如果缓存大小超过 50M 就清理一下
     */
    public void clearCacheIfOver() {
        String totalSize;
        try {
            boolean isClear = false;
            totalSize = FileUtils.getInnerCacheSize(MainActivity.this);
            if (totalSize.contains("GB") || totalSize.contains("TB")) {
                isClear = true;
            }
            if (totalSize.contains("MB")) {
                //endIndex 到指定的 endIndex-1处结束
                float total = Float.parseFloat(totalSize.substring(0, totalSize.length() - 4));
                if (total >= 50) {
                    isClear = true;
                }
            }
            if (isClear) {
                LogUtils.e("超过50M，清理缓存");
                CleanUtils.cleanInternalCache();
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
 //       if (view.getId() == R.id.layout_openlive) { //开启直播
//            if (ClickUtil.isFastDoubleClick(5000)) return;
//
//            if (DataCenter.getInstance().getUserInfo().isLogin()) {
//                boolean careraPermission = false;
//                boolean mircPermission = false;
//                if (ContextCompat.checkSelfPermission(MainActivity.this,
//                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                    careraPermission = true;
//                }
//
//                if (ContextCompat.checkSelfPermission(MainActivity.this,
//                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
//                    mircPermission = true;
//                }
//
//                if (!careraPermission || !mircPermission) {
//                    RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
//                    Disposable subscribe = rxPermissions.request(
//                            Manifest.permission.CAMERA,
//                            Manifest.permission.RECORD_AUDIO)
//                            .subscribe(granted -> {
//                                if (granted) {
//                                    checkAuth();
//                                } else { // 有的权限被拒绝或被勾选不再提示
//                                    LogUtils.e("有的权限被拒绝");
//                                    new AlertDialog.Builder(MainActivity.this)
//                                            .setCancelable(false)
//                                            .setMessage(getString(R.string.notePermission))
//                                            .setPositiveButton(getString(R.string.see), (dialog, which) -> LogUtils.e("权限被拒绝"))
//                                            .show();
//                                }
//                            });
//                } else {
//                    checkAuth();
//                }
//            }
//            else
//            {
//                LoginModeSelActivity.startActivity(context);
//            }
 //       }
    }

    MMToast mmToast;  //成功或失败的Toast提示

    public void showToastTip(boolean isSuccess, String msg) {
        if (mmToast != null) {
            mmToast.cancel();
        }
        MMToast.Builder builder = new MMToast.Builder(this)
                .setMessage(msg)
                .setSuccess(isSuccess);
        mmToast = builder.create(Toast.LENGTH_SHORT);
        mmToast.show();
    }

    /**
     * 链接链接IM
     */
    private void connectIM() {
        AppIMManager.ins().connectIM(new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                ToastUtils.showShort(getString(R.string.IMljsb) + code + "，" + desc);
                LogUtils.e("IM-> connect , Error: code->" + code + "    desc:" + desc);
            }

            @Override
            public void onSuccess() {
                AppIMManager.ins().addIMMessageListener();
                LogUtils.e("IM-> connect: Success");
            }
        });
    }

    /**
     * 检查是否有新版本更新
     * 如果展示过一次就不再展示 App重新启动时会重置
     */
    public void doCheckUpdate() {
        if (!isCloseNotice) {
            if (SPManager.getIsShownAppUpdate()) return;
            Api_Config.ins().getAppVersion(new JsonCallback<AppUpdate>() {
                @Override
                public void onSuccess(int code, String msg, AppUpdate data) {
                    if (code == 0 && data != null) {
                        if (StringUtils.isEmpty(data.getVersion()) || StringUtils.isEmpty(AppUtils.getAppVersionName())) {
                            return;
                        }
                        //判断版本
                        if (StringUtils.compareVersion(data.getVersion(), AppUtils.getAppVersionName()) > 0 && !BuildConfig.DEBUG) {
                            SPManager.saveIsShownAppUpdate(true);
                            if (getSupportFragmentManager().isStateSaved()) return;
                            //有版本更新
                            UpdateFragmentBinding updateFragment = UpdateFragmentBinding.newInstance(data.getVersion(), data.getDescript(), data.getDownUrl(),
                                    data.getIsUpdate() == 1);
                            updateFragment.show(getSupportFragmentManager(), "show up date apk");
                        }
                    }
                }
            });
        }
    }

    /**
     * 检查是否有App公告
     * 如果展示过一次就不再展示 App重新启动时会重置
     */
    public void doCheckAppNotice() {
        if (!isCloseNotice) {
            if (SPManager.getIsShownAppNotice()) return;
            Api_Config.ins().getNotice(new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String data) {
                    if (code == 0 && data != null) {
                        SPManager.saveIsShownAppNotice(true);
                        if (getSupportFragmentManager().isStateSaved()) return;
                        showAppNotice(data);
                    }
                }
            });
        }
    }

    /**
     * 显示系统通知
     *
     * @param data 通知url
     */
    private void showAppNotice(String data) {
        WebViewDialog dialog = new WebViewDialog();
        Bundle args = new Bundle();
        WebViewDialogEntity entity = new WebViewDialogEntity();
        entity.setTitle(getString(R.string.message));
        entity.setWebUrl(data);
        args.putParcelable(WebViewDialog.WEB_VIEW_DIALOG_KEY, entity);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "web view dialog");
    }

    /**
     * 做一些檢查工作 異常退出時完成的工作
     */
    public void doCheckUnfinishedWork() {

        //2.檢查是否有未成功關閉直播間的情況
        doCheckLiveUnFinish();

        //3.檢查是否有觀衆未成功退房的情況
        doCheckQuitoRomUnFinish();
    }


    //檢查是否有未成功關閉直播間的情況
    public void doCheckLiveUnFinish() {
        if (SPUtils.getInstance("liveforanchor").contains("liveId")) {
            closeLive(SPUtils.getInstance("liveforanchor").getString("anchorId"),
                    SPUtils.getInstance("liveforanchor").getString("liveId"));
        }
    }

    //檢查是否有觀衆未成功退房的情況
    public void doCheckQuitoRomUnFinish() {
        if (SPUtils.getInstance("enterRoom").contains("liveId")) {
            AppIMManager.ins().loginOutGroup(SPUtils.getInstance("enterRoom").getString("liveId"));
        }
    }

    /**
     * 關閉直播間
     *
     * @param anchorId 主播id
     * @param liveId   直播id
     */
    public void closeLive(String anchorId, String liveId) {
        LogUtils.e("有未关闭的直播间， 關閉直播間");
        Api_Live.ins().liveStop(Long.parseLong(anchorId), Integer.parseInt(liveId),
                false, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        LogUtils.e("liveStop result : " + msg);
                        if (code == 0 && msg != null) {
                            SPUtils.getInstance("liveforanchor").clear();
                        }
                        AppIMManager.ins().loginOutGroup(liveId);
                    }
                });
    }

    //取消混流
    public void delPKVideoStream(String userId) {
        if (StringUtils.isEmpty(userId)) return;
        final JSONObject requestParam = cancelPKRequestParam(Long.parseLong(userId));
        if (requestParam == null) {
            return;
        }

        internalCancelSendRequest(1, requestParam.toString());
    }

    /**
     * 将混流参数转为json字符串
     *
     * @param retryIndex   位置
     * @param requestParam 请求参数
     */
    private void internalCancelSendRequest(final int retryIndex, final String requestParam) {
        Api_LiveRecreation.ins().mergestream(requestParam, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(data);
                if (code == 0) {
                    if (Integer.parseInt(data) == 0) {
                        SPUtils.getInstance("pkhunliu").clear();
                    } else {
                        int tempRetryIndex = retryIndex - 1;
                        LogUtils.e("sendPkRsp1 : " + tempRetryIndex);
                        if (tempRetryIndex > 0) {
                            LogUtils.e("sendPkRsp2 : " + tempRetryIndex);
                            internalCancelSendRequest(tempRetryIndex, requestParam);
                        }
                    }
                }
            }
        });
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

    public void showHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, homeFragment);
        }
        showSpecialFragment(fragmentTransaction, homeFragment);
    }

    public void showLiveFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (playFragment == null) {
            playFragment = ChatListFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, playFragment);
        }
        showSpecialFragment(fragmentTransaction, playFragment);
    }

    public void showGameFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (gameFragment == null) {
            gameFragment = GameFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, gameFragment);
        }
        showSpecialFragment(fragmentTransaction, gameFragment);
    }

    public void showMineFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mineFragment == null) {
            mineFragment = MineFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, mineFragment);
        }
        showSpecialFragment(fragmentTransaction, mineFragment);
    }


    public void showAgencyFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (agencyCenterFragment == null) {
            agencyCenterFragment = AgencyCenterFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, agencyCenterFragment);
        }
        showSpecialFragment(fragmentTransaction, agencyCenterFragment);
    }

    public void showActFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (activityFragment == null) {
            activityFragment = ActivityFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, activityFragment);
        }
        showSpecialFragment(fragmentTransaction, activityFragment);
    }


    //显示指定的Fragment
    public void showSpecialFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if (homeFragment != null) fragmentTransaction.hide(homeFragment);
        if (playFragment != null) fragmentTransaction.hide(playFragment);
        if (gameFragment != null) fragmentTransaction.hide(gameFragment);
        if (mineFragment != null) fragmentTransaction.hide(mineFragment);
        if (agencyCenterFragment != null) fragmentTransaction.hide(agencyCenterFragment);
        if (activityFragment != null) fragmentTransaction.hide(activityFragment);

        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearCacheIfOver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
