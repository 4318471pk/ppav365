package com.live.fox.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.fox.AppConfig;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.MainActivity;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.CountryCode;
import com.live.fox.entity.RegisterEntity;
import com.live.fox.svga.BetCartDataManager;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_User;
import com.live.fox.ui.language.MultiLanguageActivity;
import com.live.fox.ui.mine.activity.kefu.ServicesActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.BlankController;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.ImageUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.DropDownWindowsOfCountry;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 登录界面
 * 用户登录
 */
public class LoginModeSelActivity extends BaseActivity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etPassword;
    private ImageView ivVoice;
    private TextView guestLogin;
    private TextView sendVerifyCode;
    TextView tvCountrySelector;
    LinearLayout llCountrySelector;
    ImageView ivArrow;

    //是否显示一段实体
    MediaPlayer mediaPlayer;
    private String showTip;
    private boolean flag = true;
    DropDownWindowsOfCountry dropDownWindowsOfCountry;
    List<CountryCode> countryCodes;
    int remainSecond=60;
    Handler handler =new Handler(Looper.myLooper()){

        @Override
        public void dispatchMessage(@NonNull @NotNull Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what)
            {
                case 0:
                    remainSecond--;
                    if(remainSecond>0)
                    {
                        StringBuilder sb=new StringBuilder();
                        sb.append(String.valueOf(remainSecond)).append("s").append(getString(R.string.reGet));
                        sendVerifyCode.setText(sb.toString());
                        sendVerifyCode.setEnabled(false);
                        sendEmptyMessageDelayed(0,1000);
                    }
                    else
                    {
                        remainSecond=60;
                        sendVerifyCode.setText(getString(R.string.get_verification_code));
                        sendVerifyCode.setEnabled(true);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BarUtils.setStatusBarAlpha(this);

        setContentView(R.layout.loginmodesel_activity);
        initData(getIntent());
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }

    public void initData(Intent intent) {
        if (intent != null) {
            showTip = getIntent().getStringExtra("showTip");
            if (StringUtils.isEmpty(showTip)) showTip = "";
        }
        countryCodes=SPManager.getCountryCode();
        getCountryCode();
    }

    public void initView() {
        int screenWidth=ScreenUtils.getScreenWidth(this);
        ActivityUtils.finishOtherActivities(LoginModeSelActivity.class);
        LinearLayout titleBox = findViewById(R.id.login_title_box);
        int barHeight = BarUtils.getStatusBarHeight();
        titleBox.setPadding(0, barHeight, 0, 0);

        etUsername = findViewById(R.id.et_username);

        VideoView videoView = findViewById(R.id.videoView);
        llCountrySelector=findViewById(R.id.llCountrySelector);
        etPassword = findViewById(R.id.et_password);
        ivVoice = findViewById(R.id.iv_voice);
        guestLogin=findViewById(R.id.guestLogin);
        sendVerifyCode=findViewById(R.id.sendVerifyCode);
        ivArrow=findViewById(R.id.ivArrow);
        tvCountrySelector=findViewById(R.id.tvCountrySelector);
        findViewById(R.id.layout_back).setOnClickListener(this);
        findViewById(R.id.iv_kefu).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_resetpwd).setOnClickListener(this);
        findViewById(R.id.btn_login_by_pass).setOnClickListener(this);
        findViewById(R.id.iv_voice).setOnClickListener(this);
        llCountrySelector.setOnClickListener(this);
        guestLogin.setOnClickListener(this);
        sendVerifyCode.setOnClickListener(this);
        ImageView language = findViewById(R.id.home_language);
        if (!AppConfig.isMultiLanguage()) {
            language.setVisibility(View.GONE);
        }

        language.setOnClickListener(this);

        etPassword.setFilters(new InputFilter[]{new BlankController()});
        AppIMManager.ins().logout();
        AppUserManger.loginOut();

        setLoginInfo();

        unBingTencentPush();
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/login_video"));
        videoView.start();
        videoView.setOnPreparedListener(mp -> {
            mediaPlayer = mp;
            mp.start();
            mp.setLooping(true);
        });

        //如果是被顶号的跳转,则跳出顶号的提示框
        if (!StringUtils.isEmpty(showTip)) {
            LogUtils.e(showTip);
            ToastUtils.showShort(showTip);
        }

        ImageView applogo=(ImageView)findViewById(R.id.login_logo);
        FixImageSize.setImageSizeOnWidthWithSRC(applogo, (int) (screenWidth * 0.192));
        guestLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }

    /**
     * 设置登录信息
     */
    private void setLoginInfo() {
        ImageView login = findViewById(R.id.login_logo);
        TextView domain = findViewById(R.id.login_domain_name);
        if (AppConfig.getBaseInfo() != null) {
            if (!TextUtils.isEmpty(AppConfig.getBaseInfo().getLoginUrl())) {
                Glide.with(this).load(AppConfig.getBaseInfo().getLoginUrl()).into(login);
            }

            if (!TextUtils.isEmpty(AppConfig.getBaseInfo().getFloorUrl())) {
                domain.setVisibility(View.VISIBLE);
                domain.setText(AppConfig.getBaseInfo().getFloorUrl().replace("https://", ""));
            }
        }
    }

    /**
     * 解绑腾讯云推送账号
     */
    private void unBingTencentPush() {
        XGIOperateCallback xgiOperateCallback = new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                LogUtils.i("unBingTencentPush", "onSuccess, data:" + data + ", flag:" + flag);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtils.w("unBingTencentPush", "onFail, data:" + data + ", code:" + errCode + ", msg:" + msg);
            }
        };

        XGPushManager.clearAccounts(getApplicationContext());
        Set<Integer> accountTypeSet = new HashSet<>();
        accountTypeSet.add(XGPushManager.AccountType.CUSTOM.getValue());
        accountTypeSet.add(XGPushManager.AccountType.IMEI.getValue());
        XGPushManager.delAccounts(context, accountTypeSet, xgiOperateCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag) {
            ivVoice.setImageResource(R.drawable.open_voice);
        } else {
            ivVoice.setImageResource(R.drawable.close);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        flag = true;
    }

    /**
     * 找回密码
     *
     * @param phoneNum 电话号码
     */
    public void isRegisterApi(String phoneNum) {
        Api_Auth.ins().isRegister(phoneNum, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 && "1".equals(data)) {//已注册
                    LoginActivity.startActivity(LoginModeSelActivity.this, LoginPageType.ResetPwd, phoneNum);
                } else {
                    ToastUtils.showShort(getString(R.string.toast_tip_phone_number_can_use));
                }
            }
        });
    }

    public void doLoginGuest() {
        showLoadingDialogWithNoBgBlack();
        Api_Auth.ins().guestLogin( new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                try {
                    hideLoadingDialog();
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

    public void getCountryCode() {

        if(countryCodes==null || countryCodes.size()==0)
        {
            showLoadingDialogWithNoBgBlack();
        }

        Api_Auth.ins().countryCodeList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                try {
                    hideLoadingDialog();
                    Type type = new TypeToken<List<CountryCode>>() {}.getType();
                    countryCodes=new Gson().fromJson(data,type);
                    if(countryCodes!=null && countryCodes.size()>0)
                    {
                        SPManager.setCountryCode(data);
                    }

                } catch (Exception e) {
                    ToastUtils.showShort(e.getMessage());
                }
            }
        });
    }

    public void doLoginByVCodeApi(String phone, String vCode,String areaCode) {
        showLoadingView();
        Api_Auth.ins().phoneCodeLogin(phone, vCode,areaCode, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                try {
                    hideLoadingView();
                    if (code == 0) {
                        JSONObject jsonObject = new JSONObject(data);
                        String token = jsonObject.optString("token", "");
                        if (StringUtils.isEmpty(token)) {
                            ToastUtils.showShort(getString(R.string.tokenFail));
                            return;
                        }
                        onLoginSuccess(token);
                    } else {
                        if (!StringUtils.isEmpty(msg) && msg.trim().equals(getString(R.string.noSetPassword))) {
                            msg = getString(R.string.noSetPasswordFind);
                        }
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
        SPManager.saveToken(token);
        Api_User.ins().getUserInfo(-1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String userJson) {
                if (code == 0) {
                    AppUserManger.initUser(userJson);
                    ActivityUtils.finishOtherActivities(LoginModeSelActivity.class);
                    MainActivity.startActivity(LoginModeSelActivity.this);
                    finish();
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
    }

    public void doSendPhoneCodeApi() {
        if(TextUtils.isEmpty(etUsername.getText().toString()))
        {
            ToastUtils.showShort(getString(R.string.tipsPhoneNumFormatWrong));
            return;
        }
        showLoadingDialog();
        RegisterEntity registerEntity=new RegisterEntity();
        registerEntity.setName(etUsername.getText().toString());
        registerEntity.setType("4");
        registerEntity.setVersion(AppUtils.getAppVersionName());
        registerEntity.setArea(tvCountrySelector.getText().toString());
        Api_Auth.ins().sendPhoneCode(registerEntity, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                handler.sendEmptyMessage(0);
            }
        });
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, LoginModeSelActivity.class));
    }

    public static void startActivity(Context context, String showTip) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, LoginModeSelActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        //是否被顶号
        intent.putExtra("showTip", showTip);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        String phone = String.valueOf(etUsername.getText()).trim();
        String password = String.valueOf(etPassword.getText()).trim();
        switch (view.getId()) {
            case R.id.iv_voice:
                if (flag) {
                    mediaPlayer.setVolume(0f, 0f);
                    ivVoice.setImageResource(R.drawable.close);
                    flag = false;
                } else {
                    mediaPlayer.setVolume(1f, 1f);
                    ivVoice.setImageResource(R.drawable.open_voice);
                    flag = true;
                }
                break;
            case R.id.tv_register:
                LoginActivity.startActivity(LoginModeSelActivity.this, LoginPageType.LoginByPhone, phone);
                break;

            case R.id.tv_resetpwd:
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort(getString(R.string.inputAccout));
                    return;
                }
//                if (!phone.startsWith("0") || phone.length() <11) {
//                    ToastUtils.showShort(getString(R.string.toast_tip_phone_number));
//                    return;
//                }
                isRegisterApi(phone);
                break;
            case R.id.layout_back:
                MainActivity.startActivity(LoginModeSelActivity.this);
                finish();
                break;
            case R.id.iv_kefu:
                ServicesActivity.startActivity(LoginModeSelActivity.this);
                break;

            case R.id.home_language:
                MultiLanguageActivity.launch(this);
                break;
            case R.id.btn_login_by_pass:
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort(getString(R.string.inputAccout));
                    return;
                }

//                if (!phone.startsWith("0") || phone.length() != 10) {
//                    ToastUtils.showShort(getString(R.string.toast_tip_phone_number));
//                    return;
//                }

                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showShort(getString(R.string.toast_tip_password));
                    return;
                }
                BetCartDataManager.betGameIndex = 0;
                doLoginByVCodeApi(phone, password,tvCountrySelector.getText().toString());
                break;
            case R.id.llCountrySelector:
                if(countryCodes==null || countryCodes.size()==0)
                {
                    ToastUtils.showShort(getResources().getString(R.string.downloadingError));
                    return;
                }

                if(dropDownWindowsOfCountry==null)
                {
                    dropDownWindowsOfCountry=new DropDownWindowsOfCountry(this);
                    dropDownWindowsOfCountry.setOutsideTouchable(true);
                    dropDownWindowsOfCountry.setFocusable(true);
                    dropDownWindowsOfCountry.setBackgroundDrawable(new ColorDrawable(0));
                    dropDownWindowsOfCountry.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            rotateView(ivArrow,false);
                        }
                    });
                    dropDownWindowsOfCountry.setOnClickItemListener(new DropDownWindowsOfCountry.OnClickItemListener() {
                        @Override
                        public void onClickItemListener(CountryCode countryCode) {
                            rotateView(ivArrow,!dropDownWindowsOfCountry.isShowing());
                            dropDownWindowsOfCountry.dismiss();
                            tvCountrySelector.setText(countryCode.getAreaCode());
                            tvCountrySelector.setTag(countryCode);
                        }
                    });
                }
                dropDownWindowsOfCountry.setData(countryCodes);

                rotateView(ivArrow,!dropDownWindowsOfCountry.isShowing());
                if(!dropDownWindowsOfCountry.isShowing())
                {
                    View line=findViewById(R.id.underLineofPhone);
                    dropDownWindowsOfCountry.showAsDropDown(line,0,10);
                }
                else
                {
                    dropDownWindowsOfCountry.dismiss();
                }

                break ;
            case R.id.sendVerifyCode:
                doSendPhoneCodeApi();
                break;
            case R.id.guestLogin:
                doLoginGuest();
                break;
        }
    }

    private void rotateView(ImageView view,boolean isShow)
    {
        float start=isShow?0:180;
        float end=isShow?180:0;
        Animation rotate  = new RotateAnimation(start, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setInterpolator(new AccelerateInterpolator());
        rotate.setDuration(100);//设置动画持续周期
//        rotate.setRepeatCount(1);//设置重复次数
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态

        view.startAnimation(rotate);
    }
}
