package com.live.fox.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.live.fox.R;


/**
 * 在屏幕中间显示的Toast
 */

public class ToastUtils {



    public static void showShort(int res) {
//        showShort(Utils.getApp().getResources().getString(res));
        com.blankj.utilcode.util.ToastUtils.showShort(res);
    }

    public static void showShort(String s) {
//        if (TextUtils.isEmpty(s)) {
//            return;
//        }
//        long curTime = System.currentTimeMillis();
//        if (curTime - sLastTime > 2000) {
//            sLastTime = curTime;
//            sLastString = s;
//            sToast.setText(s);
//            sToast.show();
//        } else {
//            if (!s.equals(sLastString)) {
//                sLastTime = curTime;
//                sLastString = s;
//                sToast = makeToast();
//                sToast.setText(s);
//                sToast.show();
//            }
//        }

        com.blankj.utilcode.util.ToastUtils.showShort(s);
    }

}
