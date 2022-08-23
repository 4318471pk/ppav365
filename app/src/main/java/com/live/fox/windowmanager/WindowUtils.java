package com.live.fox.windowmanager;

import android.app.Service;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.common.CommonApp;

public class WindowUtils {

    public static void closeWindowResource(Class<?> cls) {
        if (cls != null) {
            AppIMManager.ins().removeMessageReceivedListener(cls);
            if (Constant.mTXLivePlayer != null) {
                Constant.mTXLivePlayer.stopPlay(false);
                Constant.mTXLivePlayer.setPlayerView(null);
                Constant.mTXLivePlayer.setPlayListener(null);
            }

            Constant.windowAnchor = null;
            TelephonyManager tm = (TelephonyManager) CommonApp.getInstance().getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(CommonApp.mPhoneListener, PhoneStateListener.LISTEN_NONE);
            CommonApp.mPhoneListener = null;
        }
    }
}
