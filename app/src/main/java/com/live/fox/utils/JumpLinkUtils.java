package com.live.fox.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.live.fox.ConstantValue;
import com.live.fox.MainActivity;
import com.live.fox.R;
import com.live.fox.entity.ActBean;
import com.live.fox.entity.HomeBanner;
import com.live.fox.entity.RoomListBean;
import com.live.fox.ui.h5.PublicWebActivity;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.mine.RechargeActivity;

import java.util.ArrayList;
import java.util.List;

public class JumpLinkUtils {

    public static void jumpHomeBannerLinks(Context context, HomeBanner homeBanner) {
        if (homeBanner.getJump() == 1) {

            //	跳转模块:1充值2直播3活动4游戏5链接url
            switch (homeBanner.getJumpCode()) {
                case 1:
                    RechargeActivity.startActivity(context, true);
                    break;
                case 2:
                    toLiving(context, homeBanner.getContent());
                    break;
                case 3:
                    ActivityManager.getInstance().finishToActivity(MainActivity.class);
                    RxBus.get().post(ConstantValue.JumpType, MainActivity.JumpType.Promo);
                    break;
                case 4:
                    break;
                case 5:
                    //跳转类型:1内链 2外链
                    if (TextUtils.isEmpty(homeBanner.getContent())) {
                        ToastUtils.showShort(context.getResources().getString(R.string.errorLiveIdAID));
                        return;
                    }
                    switch (homeBanner.getJumpType()) {
                        case 1:
                            ActivityManager.getInstance().finishToActivity(MainActivity.class);
                            PublicWebActivity.startActivity(context, homeBanner.getName(), homeBanner.getContent());
                            break;
                        case 2:
                            IntentUtils.toBrowser(context, homeBanner.getContent());
                            break;
                    }
                    break;
            }

        }
    }

    /**
     * 跳转到直播
     */
    public static void toLiving(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShort(context.getResources().getString(R.string.errorLiveIdAID));
            return;
        }

        //http://xxx.com?liveid=?&aid=?
        if (url.toLowerCase().startsWith("http:") || url.toLowerCase().startsWith("https:")) {
            String liveid = Strings.getValueByName(url, "liveid");
            String aid = Strings.getValueByName(url, "aid");
            if (!Strings.isDigitOnly(aid) || !Strings.isDigitOnly(liveid)) {
                ToastUtils.showShort(context.getResources().getString(R.string.errorLiveIdAID));
                return;
            } else {
                List<RoomListBean> roomListBeans = new ArrayList<>();
                RoomListBean roomListBean = new RoomListBean();
                roomListBean.setId(liveid);
                roomListBean.setAid(aid);
                roomListBeans.add(roomListBean);
                LivingActivity.startActivity(context, roomListBeans, 0);
            }
        } else {
            ToastUtils.showShort(context.getResources().getString(R.string.errorLiveIdAID));
        }

    }


    public static void jumpActivityLinks(Context context, ActBean actBean) {

        //	跳转模块:1充值2直播3活动4游戏5链接url
        switch (actBean.getJumpCodeType()) {
            case 1:
                RechargeActivity.startActivity(context, true);
                break;
            case 2:
                toLiving(context, actBean.getContent());
                break;
            case 3:
                ActivityManager.getInstance().finishToActivity(MainActivity.class);
                RxBus.get().post(ConstantValue.JumpType, MainActivity.JumpType.Promo);
                break;
            case 4:
                break;
            case 5:
                //跳转类型:1内链 2外链
                if (TextUtils.isEmpty(actBean.getContent())) {
                    ToastUtils.showShort(context.getResources().getString(R.string.errorLiveIdAID));
                    return;
                }
                switch (actBean.getJumpType()) {
                    case 1:
                        ActivityManager.getInstance().finishToActivity(MainActivity.class);
                        PublicWebActivity.startActivity(context, actBean.getActivityName(), actBean.getContent());
                        break;
                    case 2:
                        IntentUtils.toBrowser(context, actBean.getContent());
                        break;
                }
                break;
        }

    }
}
