package com.live.fox.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.fox.AppConfig;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.MainActivity;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.LoginmodeselActivityBinding;
import com.live.fox.dialog.KickOutByAnotherLoginDialog;
import com.live.fox.entity.CountryCode;
import com.live.fox.entity.RegisterEntity;
import com.live.fox.manager.DataCenter;
import com.live.fox.svga.BetCartDataManager;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Auth;
import com.live.fox.server.Api_User;
import com.live.fox.ui.language.MultiLanguageActivity;
import com.live.fox.ui.living.LivingFragment;
import com.live.fox.ui.mine.kefu.ServicesActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.BlankController;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.InputMethodUtils;
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
 * ????????????
 * ????????????
 */
public class LoginModeSelActivity extends BaseBindingViewActivity  {

    LoginmodeselActivityBinding mBind;

    //????????????????????????
    MediaPlayer mediaPlayer;
    private boolean flag = true;
    boolean isKickOut=false,hasGuestLogin;
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
                        mBind.sendVerifyCode.setText(sb.toString());
                        mBind.sendVerifyCode.setEnabled(false);
                        sendEmptyMessageDelayed(0,1000);
                    }
                    else
                    {
                        remainSecond=60;
                        mBind.sendVerifyCode.setText(getString(R.string.get_verification_code));
                        mBind.sendVerifyCode.setEnabled(true);
                    }
                    break;
            }
        }
    };


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, LoginModeSelActivity.class));
    }

    public static void startActivity(Context context,boolean hasGuestLogin) {
        Intent intent=new Intent(context, LoginModeSelActivity.class);
        intent.putExtra(ConstantValue.hasGuestLogin,hasGuestLogin);
        context.startActivity(intent);
    }

    public static void startActivity(Context context,boolean hasGuestLogin, boolean isKickOut) {
        Intent intent = new Intent(context, LoginModeSelActivity.class);
        intent.putExtra(ConstantValue.isKickOut, isKickOut);
        intent.putExtra(ConstantValue.hasGuestLogin,hasGuestLogin);
        context.startActivity(intent);
    }

    @Override
    public void onClickView(View view) {

        if (ClickUtil.isFastDoubleClick()) return;
        String phone = String.valueOf(mBind.etUsername.getText()).trim();
        String password = String.valueOf(mBind.etPassword.getText()).trim();
        switch (view.getId()) {
            case R.id.iv_voice:
                if (flag) {
                    mediaPlayer.setVolume(0f, 0f);
                    mBind.ivVoice.setImageResource(R.drawable.close);
                    flag = false;
                } else {
                    mediaPlayer.setVolume(1f, 1f);
                    mBind.ivVoice.setImageResource(R.drawable.open_voice);
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
                doLoginByVCodeApi(phone, password,mBind.tvCountrySelector.getText().toString());
                break;
            case R.id.llCountrySelector:
                if(countryCodes==null || countryCodes.size()==0)
                {
                    ToastUtils.showShort(getResources().getString(R.string.downloadingError));
                    return;
                }

                InputMethodUtils.hideSoftInput(this);
                if(dropDownWindowsOfCountry==null)
                {
                    int width= ScreenUtils.getScreenWidth(context)- com.luck.picture.lib.tools.ScreenUtils.dip2px(context,140);
                    dropDownWindowsOfCountry=new DropDownWindowsOfCountry(this,mBind.underLineofPhone.getWidth());
                    dropDownWindowsOfCountry.setOutsideTouchable(true);
                    dropDownWindowsOfCountry.setFocusable(true);
                    dropDownWindowsOfCountry.setBackgroundDrawable(new ColorDrawable(0));
                    dropDownWindowsOfCountry.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            rotateView(mBind.ivArrow,false);
                        }
                    });
                    dropDownWindowsOfCountry.setOnClickItemListener(new DropDownWindowsOfCountry.OnClickItemListener() {
                        @Override
                        public void onClickItemListener(CountryCode countryCode) {
                            rotateView(mBind.ivArrow,!dropDownWindowsOfCountry.isShowing());
                            dropDownWindowsOfCountry.dismiss();
                            mBind.tvCountrySelector.setText(countryCode.getAreaCode());
                            mBind.tvCountrySelector.setTag(countryCode);
                        }
                    });
                }
                dropDownWindowsOfCountry.setData(countryCodes);

                rotateView(mBind.ivArrow,!dropDownWindowsOfCountry.isShowing());
                if(!dropDownWindowsOfCountry.isShowing())
                {
                    dropDownWindowsOfCountry.showAsDropDown(mBind.underLineofPhone,0,10);
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

    @Override
    public boolean isHasHeader() {
        return false;
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.loginmodesel_activity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setWindowsFlag();
        countryCodes=SPManager.getCountryCode();
        getCountryCode();

        hasGuestLogin= getIntent().getBooleanExtra(ConstantValue.hasGuestLogin,true);
        isKickOut= getIntent().getBooleanExtra(ConstantValue.isKickOut,false);

        if(isKickOut)
        {
            DataCenter.getInstance().getUserInfo().setToken("");
            mBind.layoutBack.setVisibility(View.GONE);
            KickOutByAnotherLoginDialog dialog=KickOutByAnotherLoginDialog.getInstance();
            DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),dialog);
        }
        mBind.guestLogin.setVisibility(hasGuestLogin?View.VISIBLE:View.INVISIBLE);
        int screenWidth=ScreenUtils.getScreenWidth(this);

        LinearLayout titleBox = findViewById(R.id.login_title_box);
        int barHeight = BarUtils.getStatusBarHeight();
        titleBox.setPadding(0, barHeight, 0, 0);


        VideoView videoView = findViewById(R.id.videoView);
        if (!AppConfig.isMultiLanguage()) {
            mBind.homeLanguage.setVisibility(View.GONE);
        }

        mBind.etPassword.setFilters(new InputFilter[]{new BlankController()});
        AppIMManager.ins().logout();

        setLoginInfo();

        unBingTencentPush();
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName()+"/"+ R.raw.login));
        videoView.start();
        videoView.setOnPreparedListener(mp -> {
            mediaPlayer = mp;
            mp.start();
            mp.setLooping(true);
        });

        FixImageSize.setImageSizeOnWidthWithSRC(mBind.loginLogo, (int) (screenWidth * 0.44));
        mBind.guestLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }

    /**
     * ??????????????????
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
     * ???????????????????????????
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
            mBind.ivVoice.setImageResource(R.drawable.open_voice);
        } else {
            mBind.ivVoice.setImageResource(R.drawable.close);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        flag = true;
    }

    /**
     * ????????????
     *
     * @param phoneNum ????????????
     */
    public void isRegisterApi(String phoneNum) {
        Api_Auth.ins().isRegister(phoneNum, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 && "1".equals(data)) {//?????????
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

    //?????????????????????????????????????????????????????????
    public void onLoginSuccess(String token) {
        DataCenter.getInstance().getUserInfo().setToken(token);
        Api_User.ins().getUserInfo(-1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String userJson) {
                if (code == 0) {
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
        if(TextUtils.isEmpty(mBind.etUsername.getText().toString()))
        {
            ToastUtils.showShort(getString(R.string.tipsPhoneNumFormatWrong));
            return;
        }
        showLoadingDialog();
        RegisterEntity registerEntity=new RegisterEntity();
        registerEntity.setName(mBind.etUsername.getText().toString());
        registerEntity.setType("4");
        registerEntity.setVersion(AppUtils.getAppVersionName());
        registerEntity.setArea(mBind.tvCountrySelector.getText().toString());
        Api_Auth.ins().sendPhoneCode(registerEntity, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                handler.sendEmptyMessage(0);
            }
        });
    }


    private void rotateView(ImageView view,boolean isShow)
    {
        float start=isShow?0:180;
        float end=isShow?180:0;
        Animation rotate  = new RotateAnimation(start, end,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotate.setInterpolator(new AccelerateInterpolator());
        rotate.setDuration(100);//????????????????????????
//        rotate.setRepeatCount(1);//??????????????????
        rotate.setFillAfter(true);//???????????????????????????????????????????????????

        view.startAnimation(rotate);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN)
        {

            return isKickOut;
        }
        return super.dispatchKeyEvent(event);
    }
}
