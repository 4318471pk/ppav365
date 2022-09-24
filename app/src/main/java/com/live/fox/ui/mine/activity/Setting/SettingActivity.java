package com.live.fox.ui.mine.activity.Setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.live.fox.App;
import com.live.fox.AppConfig;
import com.live.fox.BuildConfig;
import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.manager.SPManager;
import com.live.fox.ui.h5.UserIndexActivity;
import com.live.fox.ui.language.MultiLanguageActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.ui.mine.activity.BlackLIstActivity;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.CleanUtils;
import com.live.fox.utils.FileUtils;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.AppUserManger;
import com.live.fox.view.SwitchView;


/**
 * 系统设置
 */
public class SettingActivity extends BaseHeadActivity implements View.OnClickListener,SwitchView.OnStateChangedListener {

    private TextView tvCache;
    private TextView tvVersion;
    private TextView tvIdentification;
    private ImageView iv_permision;
    private SwitchView switchView;

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, SettingActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.system_setting), true, true);

        TextView language = findViewById(R.id.setting_language);
        if (AppConfig.isThLive()
                && !BuildConfig.DEBUG
                && !AppConfig.isMultiLanguage()
        ) {
            language.setVisibility(View.GONE);
        }

        language.setOnClickListener(view ->
                MultiLanguageActivity.launch(SettingActivity.this));
        tvCache = findViewById(R.id.tv_cache);
        tvVersion = findViewById(R.id.tv_version);
        iv_permision = findViewById(R.id.iv_permision);
        tvIdentification=findViewById(R.id.tvIdentification);
        findViewById(R.id.layout_privacysetting).setOnClickListener(this);
        findViewById(R.id.layout_black).setOnClickListener(this);
        findViewById(R.id.layout_clearcache).setOnClickListener(this);
        findViewById(R.id.layout_about).setOnClickListener(this);
        findViewById(R.id.tv_loginout).setOnClickListener(this);
        findViewById(R.id.layout_permisionsetting).setOnClickListener(this);
        findViewById(R.id.layout_permisionsetting).setOnClickListener(this);
        tvIdentification.setOnClickListener(this);
        switchView=findViewById(R.id.switchView);
        switchView.setOpened(SPManager.getGesturePasswordStatus());
        switchView.setOnStateChangedListener(this);

        String des = Constant.isPublish ? " " : getString(R.string.csb);
        des = String.format(getString(R.string.currentBan), AppUtils.getAppVersionName()) + des;
        tvVersion.setText(des);

        TextView about = findViewById(R.id.setting_about);
        String aboutStr = getString(R.string.aboutUs) + " " + getString(R.string.app_name);
        about.setText(aboutStr);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(getCtx())) {
            iv_permision.setImageResource(R.drawable.permisionoff);
        } else {
            iv_permision.setImageResource(R.drawable.permisionon);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && !Settings.canDrawOverlays(getCtx())) {
                iv_permision.setImageResource(R.drawable.permisionoff);
            } else {
                iv_permision.setImageResource(R.drawable.permisionon);
            }
        }

        if(requestCode==ConstantValue.REQUEST_CODE1)
        {
            if(resultCode==ConstantValue.RESULT_CODE1)
            {
                switchView.setOpened(true);
            }
            else
            {
                switchView.setOpened(false);
            }
        }

        if(requestCode==ConstantValue.REQUEST_CODE2)
        {
            if(resultCode==ConstantValue.RESULT_CODE2)
            {
                switchView.setOpened(false);
            }
            else
            {
                switchView.setOpened(true);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_permisionsetting: //小窗口权限
                startActivityForResult(
                        new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getCtx().getPackageName())), 0);

                break;
            case R.id.layout_privacysetting: //隐私设置
                startActivity(IntentUtils.getLaunchAppDetailsSettingsIntent(App.getInstance().getPackageName()));
                break;
            case R.id.layout_black:  //黑名单
                BlackLIstActivity.startActivity(SettingActivity.this);
                break;
            case R.id.layout_clearcache: //清除缓存
                CleanUtils.cleanInternalCache();
                updateCache();
                break;
            case R.id.layout_about: //关于我们
                String fileUrl = "";
                String language = MultiLanguageUtils.getRequestHeader();
                switch (language) {
                    case "YN":
                        fileUrl = "file:///android_asset/link_vi.html";
                        break;

                    case "THAI":
                        fileUrl = "file:///android_asset/link_thi.html";
                        break;

                    case "TW":
                        fileUrl = "file:///android_asset/link_tw.html";
                        break;

                    case "CN":
                        fileUrl = "file:///android_asset/link_cn.html";
                        break;

                    case "EN":
                        fileUrl = "file:///android_asset/link_en.html";
                        break;
                }
                if (!AppConfig.isThLive() && !AppConfig.isMMLive()) {
                    fileUrl = "file:///android_asset/link.html";
                }
                UserIndexActivity.start(SettingActivity.this, getString(R.string.userAgreement), fileUrl, false);
                break;
            case R.id.tv_loginout: //退出登录
                DialogFactory.showTwoBtnDialog(this, getString(R.string.sureExit),
                        (button, dialog) -> {
                            dialog.dismiss();
                        },

                        (button, dialog) -> {
                            dialog.dismiss();
                            AppUserManger.loginOut();
                            LoginModeSelActivity.startActivity(SettingActivity.this);
                        });
                break;
            case R.id.tvIdentification:
                startActivityForResult(new Intent(this,PhoneBindingActivity.class),ConstantValue.REQUEST_CODE1);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCache();
    }

    public void updateCache() {
        String totalSize = "0(MB)";
        totalSize = FileUtils.getInnerCacheSize(SettingActivity.this);
        tvCache.setText(totalSize);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void toggleToOn(SwitchView view) {
        Intent intent=null;
        switch (view.getId())
        {
            case R.id.switchView:
                intent=new Intent(this,APPGestureLockActivity.class);
                intent.putExtra(ConstantValue.SwitchStatus,false);
                startActivityForResult(intent,ConstantValue.REQUEST_CODE1);
                break;
        }
    }

    @Override
    public void toggleToOff(SwitchView view) {

        Intent intent=null;
        switch (view.getId())
        {
            case R.id.switchView:
                intent=new Intent(this,APPGestureLockActivity.class);
                intent.putExtra(ConstantValue.SwitchStatus,true);
                startActivityForResult(intent,ConstantValue.REQUEST_CODE2);
                break;
        }
    }
}
