package com.live.fox.manager;


import com.live.fox.BuildConfig;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.common.CommonApp;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.tencent.android.tpush.XGBasicPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知管理器
 */

public class NotificationManager {

    private static boolean isRegister;
    private static boolean isInit;
    private static boolean isBindUserId;

    private NotificationManager() {
    }

    private static final class NotificationInner {
        private static final NotificationManager notification = new NotificationManager();
    }

    public static NotificationManager getInstance() {
        return NotificationInner.notification;
    }

    /**
     * 初始换应用
     *
     * @param app app context
     */
    public void init(CommonApp app) {

        String ids[]=app.getImACCESS_ID();
        String accessSaved = SPUtils.getInstance().getString(ConstantValue.ACCESS_ID, ids[0]);
        String accessKeySaved = SPUtils.getInstance().getString(ConstantValue.ACCESS_KEY, ids[1]);

        XGPushConfig.init(app);
        XGPushConfig.setAccessId(app, Long.parseLong(accessSaved));
        XGPushConfig.setAccessKey(app, accessKeySaved);

        XGPushConfig.enableDebug(app, BuildConfig.DEBUG);  //腾讯云 TPNS 初始化
        XGPushConfig.enableOtherPush(app, true);

        //设置应用通知图标
        XGBasicPushNotificationBuilder builder = new XGBasicPushNotificationBuilder();
        builder.setSmallIcon(R.mipmap.ic_launcher);
        XGPushManager.setDefaultNotificationBuilder(app, builder);
        isInit = true;
        register(app);
    }

    /**
     * 注册通知
     *
     * @param app context
     */
    public void register(CommonApp app) {
        if (!isInit) {
            init(app);
            return;
        }

        new Thread(() -> XGPushManager.registerPush(app, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                LogUtils.d("App TPNS" + "onSuccess token:" + data);
                isRegister = true;
                registerUserID(app);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                isRegister = false;
                LogUtils.d("App TPNS" + "onFail token: " + data + ", errCode: " + errCode + ", msg: " + msg);
            }
        })).start();
    }

    /**
     * 注册腾讯云推送
     * 将用户ID和推送账号绑定
     * 用户登录，用没有绑定
     */
    public void registerUserID(CommonApp app) {
        if (DataCenter.getInstance().getUserInfo().isLogin()) {
            XGIOperateCallback xgiOperateCallback = new XGIOperateCallback() {
                @Override
                public void onSuccess(Object data, int flag) {
                    LogUtils.i("registerXGPush", "onSuccess, data:" + data + ", flag:" + flag);
                    isBindUserId = true;
                }

                @Override
                public void onFail(Object data, int errCode, String msg) {
                    LogUtils.w("registerXGPush", "onFail, data:" + data + ", code:" + errCode + ", msg:" + msg);
                    isBindUserId = false;
                }
            };

            List<XGPushManager.AccountInfo> accountInfoList = new ArrayList<>();
            accountInfoList.add(new XGPushManager.AccountInfo(XGPushManager.AccountType.UNKNOWN.getValue(),
                    String.valueOf(DataCenter.getInstance().getUserInfo().getUser().getUid())));
            XGPushManager.upsertAccounts(app, accountInfoList, xgiOperateCallback);
        }
    }

    public boolean isRegister() {
        return isRegister;
    }

    public boolean isBindingUser() {
        return isBindUserId;
    }
}
