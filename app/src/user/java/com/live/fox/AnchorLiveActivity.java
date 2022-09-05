package com.live.fox;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.live.fox.base.BaseActivity;

/**
 * 主播直播的直播间
 * 准备开始直播的界面
 */

public class AnchorLiveActivity extends BaseActivity {

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, AnchorLiveActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anchorlive_activity);
    }

    public void sendLiveRoomNotice(String ad) {

    }

}
