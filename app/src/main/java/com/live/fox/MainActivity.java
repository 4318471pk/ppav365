package com.live.fox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.MMToast;
import com.live.fox.dialog.NotificationDialog;
import com.live.fox.dialog.SelectLoginTypeDialog;
import com.live.fox.dialog.UpdateFragmentBinding;
import com.live.fox.dialog.WebViewDialog;
import com.live.fox.entity.AppUpdate;
import com.live.fox.entity.CountryCode;
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
import com.live.fox.ui.chat.ChatListFragment;
import com.live.fox.ui.game.GameFragment;
import com.live.fox.ui.home.ActivityFragment;
import com.live.fox.ui.home.AgencyCenterFragment;
import com.live.fox.ui.home.HomeFragment;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.ui.mine.MineFragment;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.CleanUtils;
import com.live.fox.utils.FileUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.RadioButtonWithAnim;
import com.tencent.imsdk.v2.V2TIMCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends BaseActivity  {

    public enum JumpType {
        Home(0),
        Game(1),
        Agent(2),
        Promo(3),
        Mine(4);

        final int type;

        JumpType(int type) {
            this.type = type;
        }
    }

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
    private RadioButtonWithAnim rbHome;
    private RadioButtonWithAnim rbGame;
    private RadioButtonWithAnim rbAgent;
    private RadioButtonWithAnim rbAct;
    private RadioButtonWithAnim rbMine;

    //TAG标识
    private int TAG;

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


    /**
     * 跳转到其他页面
     */
    @Subscribe(tags = {@Tag(ConstantValue.JumpType)})
    public void skipRegister(MainActivity.JumpType jumpType) {
        switch (jumpType.type)
        {
            case 0:
                rbHome.performClick();
                break;
            case 1:
                rbGame.performClick();
                break;
            case 2:
                rbAgent.performClick();
                break;
            case 3:
                rbAct.performClick();
                break;
            case 4:
                rbMine.performClick();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (CommonApp.isNotificationClicked) {
            finish();
            return;
        }
        setContentView(R.layout.main_activity);

        rbHome=findViewById(R.id.rbHome);
        rbGame=findViewById(R.id.rbGame);
        rbAgent=findViewById(R.id.rbAgent);
        rbAct=findViewById(R.id.rbAct);
        rbHome=findViewById(R.id.rbHome);

        setWindowsFlag();
        initView();
        AppConfig.initSmartRefreshLayout(this);
        checkNotifications();
        if (getIntent() != null) {
            whichPage = getIntent().getIntExtra(LAUNCH_PAGE_KEY, 0);
            isCloseNotice = getIntent().getBooleanExtra(CLOSE_NOTICE_KEY, false);
        }

        initData();

        FragmentManager fragmentManager = getSupportFragmentManager();
        //savedInstanceState不为空
        //在onCreate()中恢复数据：
        //因为使用add()的原因，当Activity被系统回收时，内存中还保存着Fragment相关信息，所有导致再次启动应用时，会出现Fragment重叠现象。
        if (savedInstanceState != null) {
            homeFragment = (HomeFragment) fragmentManager.findFragmentByTag("homeFragment");
            playFragment = (ChatListFragment) fragmentManager.findFragmentByTag("playFragment");
            gameFragment = (GameFragment) fragmentManager.findFragmentByTag("gameFragment");
            mineFragment = (MineFragment) fragmentManager.findFragmentByTag("mineFragment");
            agencyCenterFragment = (AgencyCenterFragment) fragmentManager.findFragmentByTag("agencyCenterFragment");
            activityFragment = (ActivityFragment) fragmentManager.findFragmentByTag("activityFragment");
            //打开关闭前的Fragment
            int index = savedInstanceState.getInt("TAG");
//            change(index);
            switch (index)
            {
                case 0:
                    rbHome.performClick();
                    break;
                case 1:
                    rbGame.performClick();
                    break;
                case 2:
                    rbAgent.performClick();
                    break;
                case 3:
                    rbAct.performClick();
                    break;
                case 4:
                    rbMine.performClick();
                    break;
            }

        } else {

        }

        startPeriodsCountDown();
    }

    public void setWindowsFlag()
    {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        setFullscreen(true, true);
    }

    private void initData() {
        if (!NotificationManager.getInstance().isRegister()) {
            NotificationManager.getInstance().register(CommonApp.getInstance());
        }
        if (DataCenter.getInstance().getUserInfo().isLogin()) {
            if (!NotificationManager.getInstance().isBindingUser()) {
                NotificationManager.getInstance().registerUserID(CommonApp.getInstance());
            }
        }

        getCountryCode();
        initTable();
        if(TextUtils.isEmpty(DataCenter.getInstance().getUserInfo().getToken()))
        {
            SelectLoginTypeDialog selectLoginTypeDialog=SelectLoginTypeDialog.getInstance();
            selectLoginTypeDialog.setOnSelectLoginTypeListener(new SelectLoginTypeDialog.OnSelectLoginTypeListener() {
                @Override
                public void onSelectLoginType(int type,SelectLoginTypeDialog dialog) {
                    switch (type)
                    {
                        case 0://手机登录
                            LoginModeSelActivity.startActivity(MainActivity.this);
                            break;
                        case 1://游客登录
                            dialog.dismissWithAnimate();
                            doLoginGuest();
                            break;
                    }
                }
            });
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),selectLoginTypeDialog);
        }
        else
        {
            //刷新个人资料 初始化IM
            onLoginSuccess();
        }

    }

    public void doLoginGuest() {
        showLoadingDialogWithNoBgBlack();
        if(!TextUtils.isEmpty(DataCenter.getInstance().getUserInfo().getToken()))
        {
            onLoginSuccess();
            return;
        }
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
                        DataCenter.getInstance().getUserInfo().setToken(token);
                        onLoginSuccess();
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
    public void onLoginSuccess() {
        Api_User.ins().getUserInfo(-1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String userJson) {
                hideLoadingDialog();
                if (code == 0) {

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
                    Type type = new TypeToken<List<CountryCode>>() {}.getType();
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

        if (!isShowNotification) {
            doCheckUpdate();
            doCheckAppNotice();
        }
    }

    private void initTable() {
        radioGroup = findViewById(R.id.main_tab_radio_group);
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            switch (i) {
                case R.id.rbHome:  //首页
                    showHomeFragment();
                    break;
                case R.id.rbGame: //游戏
                    if (!DataCenter.getInstance().getUserInfo().isLogin()) {
                        radioGroup.clearCheck();
                        LoginModeSelActivity.startActivity(context);
                        return;
                    }
                    showLiveFragment();
                    break;

                case R.id.rbAgent:
//                    if (!DataCenter.getInstance().getUserInfo().isLogin()) {
//                        radioGroup.clearCheck();
//                        LoginModeSelActivity.startActivity(context);
//                        return;
//                    }
                    showAgencyFragment();
                    break;
                case R.id.rbAct: //活动
//                    if (!DataCenter.getInstance().getUserInfo().isLogin()) {
//                        radioGroup.clearCheck();
//                        LoginModeSelActivity.startActivity(context);
//                        return;
//                    }
                    showActFragment();
                   // showGameFragment();
                    break;
                case R.id.rbMine: //个人中心
                    if (!DataCenter.getInstance().getUserInfo().isLogin()) {
                        radioGroup.clearCheck();
                        LoginModeSelActivity.startActivity(context);
                        return;
                    }
                    showMineFragment();
                    break;
            }
        });

        radioGroup.check(R.id.rbHome);

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
     * 關閉直播間
     *
     * @param anchorId 主播id
     * @param liveId   直播id
     */
    public void closeLive(String anchorId, String liveId) {
        LogUtils.e("有未关闭的直播间， 關閉直播間");
        Api_Live.ins().liveStop(anchorId, liveId,
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

    public void showHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, homeFragment,"homeFragment");
        }
        showSpecialFragment(fragmentTransaction, homeFragment);
    }

    public void showLiveFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (playFragment == null) {
            playFragment = ChatListFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, playFragment,"playFragment");
        }
        showSpecialFragment(fragmentTransaction, playFragment);
    }

    public void showGameFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (gameFragment == null) {
            gameFragment = GameFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, gameFragment,"gameFragment");
        }
        showSpecialFragment(fragmentTransaction, gameFragment);
    }

    public void showMineFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mineFragment == null) {
            mineFragment = MineFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, mineFragment,"mineFragment");
        }
        showSpecialFragment(fragmentTransaction, mineFragment);
    }


    public void showAgencyFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (agencyCenterFragment == null) {
            agencyCenterFragment = AgencyCenterFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, agencyCenterFragment,"agencyCenterFragment");
        }
        showSpecialFragment(fragmentTransaction, agencyCenterFragment);
    }

    public void showActFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (activityFragment == null) {
            activityFragment = ActivityFragment.newInstance();
            fragmentTransaction.add(R.id.fl_main, activityFragment,"activityFragment");
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
        cancelPeriodsCountDown();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelPeriodsCountDown();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    protected void onNewIntent(Intent intent) {
        LogUtils.i("MainActivity......", "onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent);
        String position = getIntent().getStringExtra("position");
        if (position != null) {

        }


    }
    /*
     * 保存TAB选中状态
     * */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //如果用以下这种做法则不保存状态，再次进来的话会显示默认tab
        //总是执行这句代码来调用父类去保存视图层的状态
        //保存tab选中的状态;
        super.onSaveInstanceState(outState);
        outState.putInt("TAG", TAG);
    }

    private CountDownTimer periodsCountDown = null;
    private long time=10*60;//10分钟
    /**
     * 取消倒计时
     */
    public void cancelPeriodsCountDown() {
        if (null != periodsCountDown) {
            periodsCountDown.cancel();
            periodsCountDown = null;
        }
    }
    /**
     * 开启倒计时
     */
    public void startPeriodsCountDown() {
        cancelPeriodsCountDown();
        periodsCountDown = new CountDownTimer(time * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {



            }

            @Override
            public void onFinish() {
                startPeriodsCountDown();

                RxBus.get().post(ConstantValue.refreshLive,"1");

                LogUtils.i("MainActivity......", "startPeriodsCountDown onFinish");
            }
        };
        periodsCountDown.start();
    }
}
