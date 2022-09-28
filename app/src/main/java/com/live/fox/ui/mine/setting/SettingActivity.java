package com.live.fox.ui.mine.setting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.SettingActivityBinding;
import com.live.fox.manager.SPManager;
import com.live.fox.ui.language.MultiLanguageActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.ui.mine.setting.paymentpassword.PaymentPasswordActivity;
import com.live.fox.utils.AppUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.CleanUtils;
import com.live.fox.utils.FileUtils;
import com.live.fox.view.SwitchView;


/**
 * 系统设置
 */
public class SettingActivity extends BaseBindingViewActivity implements SwitchView.OnStateChangedListener {

    SettingActivityBinding mBind;
    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, SettingActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId()) {
//            case R.id.layout_permisionsetting: //小窗口权限
//                startActivityForResult(
//                        new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                                Uri.parse("package:" + getCtx().getPackageName())), 0);
//
//                break;
            case R.id.rlPaymentPassword:
                PaymentPasswordActivity.startActivity(this);
                break;
            case R.id.settingLanguage:
                MultiLanguageActivity.launch(SettingActivity.this);
                break;
            case R.id.layout_clearcache: //清除缓存
                CleanUtils.cleanInternalCache();
                updateCache();
                break;
            case R.id.tv_loginout: //退出登录
                LoginModeSelActivity.startActivity(SettingActivity.this,false);
                break;
            case R.id.gvCheckVersion:

                break;
            case R.id.rlAccountSecurity:
                startActivityForResult(new Intent(this,PhoneBindingActivity.class),ConstantValue.REQUEST_CODE1);
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.setting_activity;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        BarUtils.setStatusBarLightMode(this, false);
        setActivityTitle(getString(R.string.setting));

        mBind.switchViewAPPLock.setOpened(SPManager.getGesturePasswordStatus());
        mBind.switchViewAPPLock.setOnStateChangedListener(this);
        mBind.tvVersion.setText("v"+AppUtils.getAppVersionName());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                    && !Settings.canDrawOverlays(getCtx())) {
//                iv_permision.setImageResource(R.drawable.permisionoff);
//            } else {
//                iv_permision.setImageResource(R.drawable.permisionon);
//            }
//        }

        if(requestCode==ConstantValue.REQUEST_CODE1)
        {
            if(resultCode==ConstantValue.RESULT_CODE1)
            {
                mBind.switchViewAPPLock.setOpened(true);
            }
            else
            {
                mBind.switchViewAPPLock.setOpened(false);
            }
        }

        if(requestCode==ConstantValue.REQUEST_CODE2)
        {
            if(resultCode==ConstantValue.RESULT_CODE2)
            {
                mBind.switchViewAPPLock.setOpened(false);
            }
            else
            {
                mBind.switchViewAPPLock.setOpened(true);
            }
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
        mBind.tvCache.setText(totalSize);
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
            case R.id.switchViewAPPLock:
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
            case R.id.switchViewAPPLock:
                intent=new Intent(this,APPGestureLockActivity.class);
                intent.putExtra(ConstantValue.SwitchStatus,true);
                startActivityForResult(intent,ConstantValue.REQUEST_CODE2);
                break;
        }
    }
}
