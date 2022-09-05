package com.live.fox.receiver;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.tencent.rtmp.TXLivePlayer;

import java.lang.ref.WeakReference;

//电话状态监听
public class TXPhoneStateListener extends PhoneStateListener {

    WeakReference<TXLivePlayer> mPlayer;

    public TXPhoneStateListener(TXLivePlayer player) {
        mPlayer = new WeakReference<>(player);
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        TXLivePlayer player = mPlayer.get();
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:  //電話等待接聽
            case TelephonyManager.CALL_STATE_OFFHOOK://電話接聽
                if (player != null) {
                    player.setMute(true);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:   //電話挂機
                if (player != null) {
                    player.setMute(false);
                }
                break;
        }
    }
}
