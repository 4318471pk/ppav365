package com.live.fox;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.live.fox.common.CommonApp;
import com.live.fox.db.DataBase;
import com.live.fox.entity.Letter;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.manager.SPManager;
import com.live.fox.manager.AppUserManger;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.tencent.imsdk.common.IMCallback;
import com.tencent.imsdk.group.GroupManager;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用聊天SDK管理
 */
public class AppIMManager {

    private final Handler mHandler;
    private Map<String, OnMessageReceivedListener> mListeners;
    private static boolean isIMInit = false;

    private AppIMManager() {
        mHandler = new Handler(Looper.myLooper());
    }

    private static class InstanceHolder {
        private static final AppIMManager instance = new AppIMManager();
    }

    public static AppIMManager ins() {
        return InstanceHolder.instance;
    }

    /**
     * 初始化IM SDK
     *
     * @param imSdkAppId 腾讯云SDK ID
     */
    public static void init(String imSdkAppId) {
        try {
            V2TIMSDKConfig config = new V2TIMSDKConfig();
            if (BuildConfig.DEBUG) {
                config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_DEBUG);
            } else {
                config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO);
            }
            int imSdk = Integer.parseInt(imSdkAppId);
            V2TIMManager.getInstance().addIMSDKListener(new V2TIMSDKListener() {
                // 5. 监听 V2TIMSDKListener 回调
                @Override
                public void onConnecting() {
                    // 正在连接到腾讯云服务器
                    LogUtils.i("IM->Connecting");
                }

                @Override
                public void onConnectSuccess() {
                    // 已经成功连接到腾讯云服务器
                    LogUtils.i("IM->Connect Success");
                    isIMInit = true;
                }

                @Override
                public void onConnectFailed(int code, String error) {
                    // 连接腾讯云服务器失败
                    LogUtils.e("IM->Connect Failed: code->" + code + "  error ->" + error);
                }

                @Override
                public void onKickedOffline() {
                    LogUtils.e("IM->KickedOffline : 当前用户被踢下线");
                    UserJump.jumpHelp();
                }

                @Override
                public void onUserSigExpired() {
                    LogUtils.e("IM->User Sig Expired : 登录票据已经过期");
                    UserJump.jumpHelp();
                }
            });

            V2TIMManager.getInstance().initSDK(CommonApp.getInstance(), imSdk, config);
        } catch (NumberFormatException exception) {
            exception.getStackTrace();
            unInit();
        }
    }

    /**
     * 反初始化 IMSDK，释放资源
     */
    public static void unInit() {
        V2TIMManager.getInstance().unInitSDK();
    }

    /**
     * 连接IM
     *
     * @param callback 状态返回
     */
    public void connectIM(V2TIMCallback callback) {
        if (DataCenter.getInstance().getUserInfo().isLogin() && isIMInit) {
            String loginUser = V2TIMManager.getInstance().getLoginUser();
            if (TextUtils.isEmpty(loginUser)) {
                //当前IM无连接用户 则做连接IM操作
                User user = DataCenter.getInstance().getUserInfo().getUser();
                V2TIMManager.getInstance().login(String.valueOf(user.getUid()), user.getImToken(), callback);
            } else {
                if (loginUser.equals(String.valueOf(DataCenter.getInstance().getUserInfo().getUser().getUid()))) {
                    callback.onSuccess();
                }
            }
        }
    }

    /**
     * 退出IM
     */
    public void logout() {
        removeIMMessageListener();
        clearAllClassListener();
        isAddMessageListener = false;
        V2TIMManager.getInstance().logout(null);
    }

    /**
     * 一个类独自添加IM监听
     */
    public void addMessageListener(Class<?> cls, OnMessageReceivedListener mListener) {
        setOnMessageReceivedListener(cls, mListener);
        LogUtils.e("2222");
        addIMMessageListener();
    }

    /**
     * 添加IM消息监听
     */
    public boolean isAddMessageListener = false;

    public void addIMMessageListener() {
        if (!isAddMessageListener) {
            LogUtils.e("添加IM监听");
            isAddMessageListener = true;
            V2TIMManager.getInstance().addSimpleMsgListener(imListener);
        }
    }

    public void removeIMMessageListener() {
        V2TIMManager.getInstance().removeSimpleMsgListener(imListener);
    }

    V2TIMSimpleMsgListener imListener = new V2TIMSimpleMsgListener() {
        @Override
        public void onRecvC2CTextMessage(String msgID, V2TIMUserInfo sender, String text) {
            super.onRecvC2CTextMessage(msgID, sender, text);
            LogUtils.e("IM-> RevC2CTextMessage:" + text);
            receive(text);
        }

        @Override
        public void onRecvC2CCustomMessage(String msgID, V2TIMUserInfo sender, byte[] customData) {
            super.onRecvC2CCustomMessage(msgID, sender, customData);
            LogUtils.e("IM-> RevC2CCustomMessage:" + customData.toString());
        }

        @Override
        public void onRecvGroupTextMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, String text) {
            super.onRecvGroupTextMessage(msgID, groupID, sender, text);
            LogUtils.e("IM-> RevGroupTextMessage:" + text);
            receive(text);
        }

        @Override
        public void onRecvGroupCustomMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, byte[] customData) {
            super.onRecvGroupCustomMessage(msgID, groupID, sender, customData);
            LogUtils.e("IM-> RevGroupCustomMessage:" + customData.toString());
        }
    };

    /**
     * 退出聊天室
     *
     * @param groupId 群组id
     */
    public void loginOutGroup(String groupId) {
        if (isIMInit) {
            V2TIMManager.getInstance().quitGroup(groupId, new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    LogUtils.e("IM-LOGING PlayerActivity退出聊天失敗:liveId->"
                            + groupId + ",code " + code + ",des " + desc);
                }

                @Override
                public void onSuccess() {
                    SPUtils.getInstance("enterRoom").clear();
                    LogUtils.e("IM-LOGING PlayerActivity退出聊天成功:liveId->" + groupId);
                }
            });
        }
    }

    public void loginGroup(String groupId, String msg, V2TIMCallback callback) {
        GroupManager.getInstance().joinGroup(groupId, msg, new IMCallback<String>(callback) {
            @Override
            public void success(String data) {
                super.success(data);
            }

            @Override
            public void fail(int code, String errorMessage) {
                super.fail(code, errorMessage);
                callback.onError(code, errorMessage);
            }
        });
    }

    public void receive(final String msg) {
        try {
            JSONObject object = new JSONObject(msg);
            if (object.optInt("protocol") == Constant.MessageProtocol.PROTOCOL_LETTER) { //私信消息
                Letter letter = new Gson().fromJson(msg, Letter.class);
                User loginUser = DataCenter.getInstance().getUserInfo().getUser();
                if (loginUser != null) {
                    letter.setSendUid(object.optLong("uid"));
                    letter.setOtherUid(loginUser.getUid());
                    letter.setType(0);
                    letter.setUnReadCount(1);

                    DataBase db = DataBase.getDbInstance();
                    db.insertLetter(letter);

                    User otherUser = new User();
                    otherUser.setUid(object.optLong("uid"));
                    otherUser.setAvatar(letter.getAvatar());
                    otherUser.setNickname(letter.getNickname());
                    otherUser.setSex(letter.getSex());
                    otherUser.setUserLevel(letter.getUserLevel());

                    db.insertLetterList(otherUser, loginUser.getUid(), letter.getLetterId(),
                            letter.getContent(), System.currentTimeMillis(), false);
                    EventBus.getDefault().post(new MessageEvent(90, new Gson().toJson(letter)));
                }
            } else if (object.optInt("protocol") == Constant.MessageProtocol.PROTOCOL_BALANCE_CHANGE) { //12金币变动消息
                User user = DataCenter.getInstance().getUserInfo().getUser();
                if (user != null) {
                    long uid = object.optLong("uid", -1);
                    Double goldCoin = object.optDouble("goldCoin", -1);
                    if (uid == user.getUid()) {
                        user.setGoldCoin(goldCoin.floatValue());
                        SPManager.saveUserInfo(user);
                    }
                }
            }

            if (mListeners != null) {
                mHandler.post(() -> {
                    for (String context : mListeners.keySet()) {
                        mListeners.get(context).onIMReceived(object.optInt("protocol"), msg);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setOnMessageReceivedListener(Class<?> cls, OnMessageReceivedListener mListener) {
        if (mListeners == null) {
            mListeners = new HashMap<>();
        }
        if (mListener != null && !mListeners.containsKey(cls.getSimpleName())) {
            LogUtils.e("3333424");
            mListeners.put(cls.getSimpleName(), mListener);
        }
    }

    public void removeMessageReceivedListener(Class<?> cls) {
        if (mListeners == null) {
            mListeners = new HashMap<>();
        }
        mListeners.remove(cls.getSimpleName());
    }

    public void clearAllClassListener() {
        if (mListeners != null) {
            mListeners.clear();
            isAddMessageListener = false;
        }
    }

    public interface OnMessageReceivedListener {

        /**
         * ##
         *
         * @param protocol 协议
         * @param msg      信息
         */
        void onIMReceived(int protocol, String msg);
    }
}
