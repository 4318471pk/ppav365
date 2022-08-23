package com.live.fox.receiver;

import static com.live.fox.Constant.SPUtilKey.IM_SDK_APP_ID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.common.CommonApp;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.NotificationLiveEntity;
import com.live.fox.entity.NotificationMessageEntity;
import com.live.fox.entity.NotificationUserEntity;
import com.live.fox.entity.User;
import com.live.fox.entity.XGNotification;
import com.live.fox.ui.chat.ChatActivity;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.tencent.android.tpush.NotificationAction;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageReceiver extends XGPushBaseReceiver {

    public static final String UPDATE_LISTVIEW_ACTION = "com.qq.xgdemo.activity.UPDATE_LISTVIEW";
    public static final String TEST_ACTION = "com.qq.xgdemo.activity.TEST_ACTION";
    public static final String LogTag = "xg.test";

    public boolean isShow = true;

    /**
     * 消息透传处理
     * 开发者在前台下发消息，需要 App 继承 XGPushBaseReceiver 重载 onTextMessage 方法接收，成功接收后，再根据特有业务场景进行处理。
     *
     * @param context Context
     * @param message 解析自定义的 JSON
     */
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        String text = "收到消息:" + message.toString();
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    String value = obj.getString("key");
                    Log.d(LogTag, "get custom value:" + value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //APP自主处理消息的过程...
        Log.d(LogTag, text);
    }

    /**
     * 通知展示
     *
     * @param context         Context
     * @param notifiShowedRlt 包含通知的内容
     */
    @Override
    public void onNotificationShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }

        XGNotification notific = new XGNotification();
        notific.setMsg_id(notifiShowedRlt.getMsgId());
        notific.setTitle(notifiShowedRlt.getTitle());
        notific.setContent(notifiShowedRlt.getContent());
        // notificationActionType==1为Activity，2为url，3为intent
        notific.setNotificationActionType(notifiShowedRlt
                .getNotificationActionType());
        // Activity,url,intent都可以通过getActivity()获得
        notific.setActivity(notifiShowedRlt.getActivity());
        notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(Calendar.getInstance().getTime()));

        //NotificationService.getInstance(context).save(notific);
        Intent testIntent = new Intent(TEST_ACTION);
        if (notifiShowedRlt.getTitle().equals(Constant.LOCAL_NOTIFICATION_TITLE)) {
            testIntent.putExtra("step", Constant.TEST_LOCAL_NOTIFICATION);
        } else {
            testIntent.putExtra("step", Constant.TEST_NOTIFICATION);
        }
        context.sendBroadcast(testIntent);

        Intent viewIntent = new Intent(UPDATE_LISTVIEW_ACTION);
        context.sendBroadcast(viewIntent);
        Log.d(LogTag, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString() + ", PushChannel:" + notifiShowedRlt.getPushChannel());
    }

    /**
     * 注册回调
     *
     * @param context   Context
     * @param errorCode 0 为成功，其它为错误码
     */
    @Override
    public void onRegisterResult(Context context, int errorCode, XGPushRegisterResult message) {
        if (context == null || message == null) {
            return;
        }
        String text;
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            //在这里拿token
            String token = message.getToken();
            text = "注册成功1. token：" + token;
        } else {
            text = message + "注册失败，错误码：" + errorCode;
        }
        Log.d(LogTag, text);
    }

    /**
     * 反注册回调
     *
     * @param context   Context
     * @param errorCode 0 为成功，其它为错误码
     */
    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text;
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        Log.d(LogTag, text);
    }

    /**
     * 设置标签回调
     *
     * @param context   Context
     * @param errorCode 0 为成功，其它为错误码
     * @param tagName   设置的 TAG
     */
    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text;
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);

        Intent testIntent = new Intent(TEST_ACTION);
        testIntent.putExtra("step", Constant.TEST_SET_TAG);
        context.sendBroadcast(testIntent);
    }

    /**
     * 删除标签的回调
     *
     * @param context   Context
     * @param errorCode 0 为成功，其它为错误码
     * @param tagName   设置的 TAG
     */
    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text;
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);

        Intent testIntent = new Intent(TEST_ACTION);
        testIntent.putExtra("step", Constant.TEST_DEL_TAG);
        context.sendBroadcast(testIntent);
    }

    /**
     * 设置账号回调
     *
     * @param context   Context
     * @param errorCode 0 为成功，其它为错误码
     * @param account   设置的账号
     */
    @Override
    public void onSetAccountResult(Context context, int errorCode, String account) {
        Intent testIntent = new Intent(TEST_ACTION);
        testIntent.putExtra("step", Constant.TEST_SET_ACCOUNT);
        context.sendBroadcast(testIntent);
    }

    /**
     * 删除账号回调
     *
     * @param context   Context
     * @param errorCode 0 为成功，其它为错误码
     * @param account   设置的账号
     */
    @Override
    public void onDeleteAccountResult(Context context, int errorCode, String account) {
        Intent testIntent = new Intent(TEST_ACTION);
        testIntent.putExtra("step", Constant.TEST_DEL_ACCOUNT);
        context.sendBroadcast(testIntent);
    }

    @Override
    public void onSetAttributeResult(Context context, int i, String s) {

    }

    @Override
    public void onQueryTagsResult(Context context, int errorCode, String data, String operateName) {
        Log.i(LogTag, "QueryTags - onQueryTagsResult, errorCode:" + errorCode + ", operateName:" + operateName + ", data: " + data);
    }

    @Override
    public void onDeleteAttributeResult(Context context, int i, String s) {

    }

    /**
     * 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
     *
     * @param context Context
     * @param message 包含被点击通知的内容
     */
    @Override
    public void onNotificationClickedResult(Context context, XGPushClickedResult message) {
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (message.getActionType() == NotificationAction.clicked.getType()) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            if (!TextUtils.isEmpty(message.getCustomContent())) {
                notificationClickHandle(context, message.getCustomContent());
            }

            text = "通知被打开 :" + message;
        } else if (message.getActionType() == NotificationAction.delete.getType()) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :" + message;
        }
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    String value = obj.getString("key");
                    Log.d(LogTag, "get custom value:" + value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // APP自主处理的过程。。。
        Log.d(LogTag, text);
    }

    /**
     * 通知点击事件处理
     *
     * @param jsonMessage 后台传输的自定义消息
     */
    private void notificationClickHandle(Context context, String jsonMessage) {
        try {
            Activity activity = ActivityUtils.getTopActivity();
            if (activity != null) {
                CommonApp.topActivity = new WeakReference<>(activity);
            }

            CommonApp.isNotificationClicked = true;
            JSONObject message = new JSONObject(jsonMessage);
            Object type = message.get("type");
            String typeStr = type + "";

            LogUtils.e("jsonMessage==" + jsonMessage);

            switch (typeStr) {
                case "0": //主播开播
                    NotificationLiveEntity entity = GsonUtil.getObject(jsonMessage, NotificationLiveEntity.class);
                    if (entity != null) {
                        Anchor anchor = new Anchor();
                        anchor.setLiveId(entity.getBody());
                        anchor.setFromMessage(true);
                        Constant.isAppInsideClick = true;
                        Intent intent = new Intent(context, PlayLiveActivity.class);
                        intent.putExtra("currentAnchor", anchor);
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        if (context == null) {
                            CommonApp.getInstance().startActivity(intent);
                        } else {
                            context.startActivity(intent);
                        }
                    }
                    break;

                case "1":  //用户私信
                    String imSdkAppId = SPUtils.getInstance().getString(IM_SDK_APP_ID);
                    AppIMManager.init(imSdkAppId);
                    NotificationMessageEntity messageEntity = GsonUtil.getObject(jsonMessage, NotificationMessageEntity.class);
                    String body = messageEntity.getBody();
                    if (!TextUtils.isEmpty(body)) {
                        NotificationUserEntity userEntity = GsonUtil.getObject(body, NotificationUserEntity.class);
                        User user = new User();
                        user.setUid(userEntity.getSrcUid());
                        user.setDestUid(userEntity.getDestUid());
                        ChatActivity.startActivityNotification(context, user);
                    }

                    Log.e("JsonBody", body);
                    break;

                case "2": //跳转活动详情
                    String activityJson = (String) message.get("body");
                    JSONObject jsonObject = new JSONObject(activityJson);
                    Intent resultIntent = new Intent(context, FragmentContentActivity.class);
                    resultIntent.setAction(Intent.ACTION_MAIN);
                    resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    resultIntent.putExtra("url", jsonObject.getString("activityDetail"));
                    resultIntent.putExtra(Constant.FragmentFlag.FLAG, Constant.FragmentFlag.WEB_FRAGMENT);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(resultIntent);
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
