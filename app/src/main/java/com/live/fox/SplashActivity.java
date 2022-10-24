package com.live.fox;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.flyco.roundview.RoundRelativeLayout;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.CommonApp;
import com.live.fox.dialog.ScreenLockBindingDialog;
import com.live.fox.manager.SPManager;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {

    private TextView tvTime;
    private RoundRelativeLayout layoutSkip;
    private ImageView ivAd;

    private final long adTime = 1000;
    private int adTotalTime = 5;
    private CountDownTimer adDownTimer;
    private SplashPresenter splashPresenter;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (CommonApp.isNotificationClicked) {
            finish();
            return;
        }
        BarUtils.setStatusBarVisibility(this, false);

        fitNotch();
        setContentView(R.layout.activity_splash);

        splashPresenter = new SplashPresenter(this);
        initView();

        if (BuildConfig.DEBUG) {
            adTotalTime = 1;
        }

        adDownTimer = new CountDownTimer(adTotalTime * adTime, adTime) {
            @Override
            public void onTick(long l) {
                tvTime.setText(String.format(getString(R.string.tools_splash), String.valueOf(adTotalTime--)));
            }

            @Override
            public void onFinish() {
                goToMain();
            }
        };
    }

    private void initView() {
        tvTime = findViewById(R.id.tv_time);
        layoutSkip = findViewById(R.id.layout_skip);
        ivAd = findViewById(R.id.splash_iv_ad);
        TextView tvEnter = findViewById(R.id.tvEnter);

        splashPresenter.loadData();

        String splashTips;
        // TODO: 12/14/2021 我不知为什么这里多语言偶尔不能切换
        if (AppConfig.isThLive()) {
            if (!BuildConfig.DEBUG && !AppConfig.isMultiLanguage()) {
                splashTips = "หากคุณไม่สามารถเข้าสู่แอปได้, โปรด" + "<font color='#05a4ff'>" +
                        "【คลิกที่นี่】" + "</font>" + "เพื่อดาวน์โหลดเวอร์ชันล่าสุด";
            } else {
                splashTips = getSplashTips();
            }
        } else {
            splashTips = getSplashTips();
        }

        tvEnter.setText(Html.fromHtml(splashTips));
        tvEnter.setOnClickListener(v -> IntentUtils.toBrowser(SplashActivity.this, AppConfig.getLandingPage()));
    }

    /**
     * 多语言这里不能是现实的暂时处理
     *
     * @return 返回拼接之后的字符
     */
    private String getSplashTips() {
        String splicing;
        if (!AppConfig.isThLive()) {
            splicing = "Nếu bạn không thể vào APP,vui lòng" +
                    "<font color='#05a4ff'>" +
                    "【nhấp vào đây】" + "</font>" +
                    "để tải xuống phiên bản mới nhất";
        } else {
            splicing = getString(R.string.splash_app_tips_start) + "<font color='#05a4ff'>" +
                    getString(R.string.splash_app_tips_middle) + "</font>" +
                    getString(R.string.splash_app_tips_end);
        }
        return splicing;
    }


    public void showAdView(String path, String openScreenUrl) {
        GlideUtils.loadImage(SplashActivity.this, path, ivAd, new CenterCrop());
        ivAd.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.VISIBLE);
        layoutSkip.setVisibility(View.VISIBLE);

        ivAd.setOnClickListener(view -> IntentUtils.toBrowser(SplashActivity.this, openScreenUrl));

        tvTime.setOnClickListener(view -> {
            adDownTimer.cancel();
            goToMain();
        });

        adDownTimer.start();
    }

    public void goToMain() {
//        String gesPassword=SPManager.getGesturePassword();
//        boolean gesStatus=SPManager.getGesturePasswordStatus();
//        if(StringUtils.isDigitOnly(gesPassword) && gesStatus)
//        {
//            ScreenLockBindingDialog screenLockDialog= ScreenLockBindingDialog.getInstance();
//            screenLockDialog.setOnScreenLockUnlockListener(new ScreenLockBindingDialog.onScreenLockUnlockListener() {
//                @Override
//                public void onScreenLockUnlock() {
//                    MainActivity.startActivity(SplashActivity.this);
//                    finish();
//                }
//            });
//            DialogFramentManager.getInstance().showDialogAllowingStateLoss(getSupportFragmentManager(),screenLockDialog);
//        }
//        else
//        {
//            MainActivity.startActivity(SplashActivity.this);
//            finish();
//        }

        LivingActivity.startActivity(this);

    }

    /**
     * 适配屏幕
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void fitNotch() {
        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(attributes);
        } catch (NoSuchFieldError error) {
            LogUtils.e("fitNotch", "手机不是凉棚屏幕");
        }
    }
}
