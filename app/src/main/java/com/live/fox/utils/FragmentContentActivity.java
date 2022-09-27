package com.live.fox.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;


import com.live.fox.SplashActivity;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.CommonApp;
import com.live.fox.Constant;
import com.live.fox.MainActivity;
import com.live.fox.ui.circle.WebFragment;
import com.live.fox.ui.mine.moneyout.WithdrawalsRecordFragment;
import com.tencent.android.tpush.TpnsActivity;

import static android.R.id.widget_frame;


/**
 * @since 2017/2/20
 */
public class FragmentContentActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(widget_frame);
        setContentView(frameLayout);

        switchFragment(getIntent());
    }

    public void switchFragment(Intent intent) {
        int fragmentKey = intent.getIntExtra(Constant.FragmentFlag.FLAG, 0);

        switch (fragmentKey) {
            case Constant.FragmentFlag.WEB_FRAGMENT:
                String url = getIntent().getStringExtra("url");
                replaceFragment(widget_frame, WebFragment.newInstance(url, true));
                break;
            case Constant.FragmentFlag.JbRecord_FRAGMENT:
                replaceFragment(widget_frame, WithdrawalsRecordFragment.newInstance(1));
                break;
            case Constant.FragmentFlag.JzRecord_FRAGMENT:
                replaceFragment(widget_frame, WithdrawalsRecordFragment.newInstance(4));
                break;
        }
    }

    //改成这种
    public static void startWebActivity(Context context, String title, String url) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, FragmentContentActivity.class);
        i.putExtra(Constant.FragmentFlag.FLAG, Constant.FragmentFlag.WEB_FRAGMENT);
        i.putExtra("title", title);
        i.putExtra("url", url);
        context.startActivity(i);
    }

    //金币提现记录
    public static void startJbRecordActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, FragmentContentActivity.class);
        i.putExtra(Constant.FragmentFlag.FLAG, Constant.FragmentFlag.JbRecord_FRAGMENT);
        context.startActivity(i);
    }

    //家族明细
    public static void startJzRecordActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, FragmentContentActivity.class);
        i.putExtra(Constant.FragmentFlag.FLAG, Constant.FragmentFlag.JzRecord_FRAGMENT);
        context.startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Constant.isAppInsideClick = true;
        if (CommonApp.isNotificationClicked) {
            CommonApp.isNotificationClicked = false;
            Activity activity = CommonApp.topActivity.get();
            if (activity == null
                    || activity instanceof MainActivity
                    || activity instanceof TpnsActivity
                    || activity instanceof SplashActivity) {
                MainActivity.startActivityWithPosition(this, 0);
            }
        }
        finish();
    }
}
