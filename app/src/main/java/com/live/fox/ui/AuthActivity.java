package com.live.fox.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;


/**
 * 主播认证
 */
public class AuthActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);
        initView();
    }


    public void initView(){
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        findViewById(R.id.layout_back).setOnClickListener(this);
        findViewById(R.id.btn_).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_:
                doAuthApi();
                break;
        }
    }


    private void doAuthApi() {
        showLoadingDialog();
        Api_User.ins().ins().userAuth(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(data!=null) LogUtils.e(data);
                hideLoadingDialog();
                if (code == 0) {
                    showToastTip(true, getString(R.string.ninjijiao));
                    finish();
                } else {
                    showToastTip(false, msg);
                }
            }
        });
    }

    public static void startActivity(Context context) {
        Constant.isAppInsideClick=true;
        Intent intent = new Intent(context, AuthActivity.class);
        context.startActivity(intent);
    }

}
