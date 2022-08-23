package com.live.fox.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.live.fox.App;
import com.live.fox.Constant;
import com.live.fox.MainActivity;
import com.live.fox.R;
import com.live.fox.SplashActivity;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.CommonApp;
import com.live.fox.common.JsonCallback;
import com.live.fox.db.DataBase;
import com.live.fox.entity.Letter;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.User;
import com.live.fox.server.Api_User;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.AppUserManger;
import com.tencent.android.tpush.TpnsActivity;
import com.tencent.android.tpush.XGPushManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.andy.qpopuwindow.QPopuWindow;

/**
 * 用户私信聊条界面
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rvChat;
    private EditText etMsgcontent;
    private TextView tvSendmsg;
    private LinearLayout layoutParent;
    Toolbar toolbar;
    DataBase db;
    ChatAdapter adapter;
    List<Letter> letters = new ArrayList<>();
    LinearLayoutManager wcLinearLayoutManger;

    User otherUser;
    User loginUser;
    private int rawX;
    private int rawY;

    private static final String OTHER_USER_KEY = "otherUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        EventBus.getDefault().register(this);

        initData(getIntent());

        setView();
        etMsgcontent.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMsg();
                return true;
            }
            return false;
        });
    }

    /**
     * 从通知栏点击跳转的只能获取发消息，没有其他信息
     * 这里从数据数据库中读取完善发消息人的信息
     *
     * @param intent 获取喜喜的意图
     */
    public void initData(Intent intent) {
        if (intent != null) {
            otherUser = (User) intent.getSerializableExtra(OTHER_USER_KEY);
            loginUser = AppUserManger.getUserInfo();
            db = DataBase.getDbInstance();
            if (TextUtils.isEmpty(otherUser.getNickname()) && db != null) {  //为发送消息的人创建通知渠道
                String channel = String.valueOf(otherUser.getUid());
                XGPushManager.createNotificationChannel(getApplicationContext(), channel, "message", true, true, true, null);
                List<Letter> letterList = db.getLetterListByUid(otherUser.getUid(), loginUser.getUid());
                for (Letter letter : letterList) {
                    if (letter.getSendUid() == otherUser.getUid()) {
                        otherUser.setAvatar(letter.getAvatar());
                        otherUser.setNickname(letter.getNickname());
                        otherUser.setSex(letter.getSex());
                        otherUser.setUserLevel(letter.getUserLevel());
                        break;
                    }
                }
            }
        }
    }

    public void setView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);

        rvChat = findViewById(R.id.rv_chat);
        etMsgcontent = findViewById(R.id.et_msgcontent);
        tvSendmsg = findViewById(R.id.tv_sendmsg);
        layoutParent = findViewById(R.id.layout_parent);
        findViewById(R.id.tv_sendmsg).setOnClickListener(this);

        initHead();

        initRecycleView();
        getLetterList();
        setRead();
    }

    private void setRead() {
        Api_User.ins().readLetter(otherUser.getUid(), new JsonCallback<Integer>() {
            @Override
            public void onSuccess(int code, String msg, Integer data) {
                if (code == 0) {
                    db.userIsRead(otherUser.getUid(), loginUser.getUid());
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 设置Title
     */
    public void initHead() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.head_back_sel);
            //设置ToolBar的标题不显示
            supportActionBar.setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(view -> {
                goBack();
            });

            supportActionBar.setDisplayHomeAsUpEnabled(true);
            //设置标题
            ((TextView) findViewById(R.id.tv_head_title)).setText(otherUser.getNickname());
        }
    }

    /**
     * 返回处理
     */
    private void goBack() {
        Constant.isAppInsideClick = true;
        if (CommonApp.isNotificationClicked) {
            Activity activity = CommonApp.topActivity.get();
            if (activity == null
                    || activity instanceof MainActivity
                    || activity instanceof TpnsActivity
                    || activity instanceof SplashActivity) {
                MainActivity.startActivityWithPosition(this, 5);
            }
            CommonApp.isNotificationClicked = false;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    public void initRecycleView() {
        wcLinearLayoutManger = new LinearLayoutManager(ChatActivity.this, LinearLayoutManager.VERTICAL, false);
        rvChat.setLayoutManager(wcLinearLayoutManger);
        adapter = new ChatAdapter(new ArrayList<Letter>());
        rvChat.setAdapter(adapter);

        //底部布局弹出,聊天列表上滑
        rvChat.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                rvChat.post(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount() > 0) {
                            rvChat.smoothScrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                });
            }
        });

        rvChat.setOnTouchListener((view, motionEvent) -> {
            if (KeyboardUtils.isSoftInputVisible(ChatActivity.this)) {
                LogUtils.e("onTouch ");
                KeyboardUtils.hideSoftInput(ChatActivity.this);
            }
            return false;
        });

        adapter.setOnItemLongClickListener((adapter1, view, position) -> {
            QPopuWindow.getInstance(ChatActivity.this).builder
                    .bindView(view, position)
                    .setPopupItemList(new String[]{"xóa bỏ", "Sao chép"})
                    .setTextDrawableRes(new Integer[]{R.drawable.message_del, R.drawable.copy_message})
                    .setPointers(rawX, rawY)
                    .setOnPopuListItemClickListener((anchorView, anchorViewPosition, position1) -> {
                        switch (position1) {
                            case 0:
                                deleteMessage(anchorViewPosition);
                                break;
                            case 1:
                                String copyString = adapter.getData().get(anchorViewPosition).getContent();
                                copyToClipboard(copyString);
                                break;
                        }
                    }).show();
            return false;
        });
    }


    private void deleteMessage(int anchorViewPosition){
        Api_User.ins().deleteLetter(adapter.getData().get(anchorViewPosition).getLetterId(),
                2, new JsonCallback<Integer>() {
                    @Override
                    public void onSuccess(int code, String msg, Integer data) {
                        if (code == 0) {
                            db.deleteSingleChatMessage(letters.get(anchorViewPosition).getOtherUid(), letters.get(anchorViewPosition).getSendUid(),
                                    letters.get(anchorViewPosition).getLetterId(), anchorViewPosition == (adapter.getItemCount() - 1), letters.get(anchorViewPosition).getSendUid() == loginUser.getUid());
                            letters.clear();
                            letters.addAll(db.getLetterListByUid(otherUser.getUid(), loginUser.getUid()));
                            for (int i = 0; i < letters.size(); i++) {
                                letters.get(i).setLayout(letters.get(i).getSendUid() == loginUser.getUid() ? 1 : 0);//信息类型 0收消息 1发消息
                            }
                            adapter.notifyDataSetChanged();
                            rvChat.scrollToPosition(adapter.getItemCount() - 1);
                        } else {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        rawX = (int) ev.getRawX();
        rawY = (int) ev.getRawY();
        return super.dispatchTouchEvent(ev);
    }

    public void getLetterList() {
        letters.clear();
        letters.addAll(db.getLetterListByUid(otherUser.getUid(), loginUser.getUid()));
        if (letters != null) {
            LogUtils.e(new Gson().toJson(letters));
            for (int i = 0; i < letters.size(); i++) {
                letters.get(i).setLayout(letters.get(i).getSendUid() == loginUser.getUid() ? 1 : 0);//信息类型 0收消息 1发消息
            }

            adapter.setNewData(letters);
            rvChat.scrollToPosition(adapter.getItemCount() - 1);
        }
    }


    public void sendMsg() {
        String content = etMsgcontent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        doSendLetterApi(content);
        etMsgcontent.setText("");
    }

    /**
     * 发送私信
     */
    public void doSendLetterApi(String content) {
        Api_User.ins().sendLetter(otherUser.getUid(), content, new JsonCallback<Integer>() {
            @Override
            public void onSuccess(int code, String msg, Integer data) {
                //0,success,"86"
                if (data != null) LogUtils.e(code + "," + msg + "," + new Gson().toJson(data));
                if (code == 0 && data != null) {
                    Letter letter = getChatInfoTo(data, content);
                    LogUtils.e(new Gson().toJson(letter));
                    interLetterToDB(letter);
                    adapter.addData(letter);
                    rvChat.scrollToPosition(adapter.getItemCount() - 1);
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    public void interLetterToDB(Letter letter) {
        db.insertLetter(letter);
        db.insertLetterList(otherUser, loginUser.getUid(), letter.getLetterId(),
                letter.getContent(), System.currentTimeMillis(), true);
    }

    /**
     * 发送的信息
     * from为收到的消息，to为自己发送的消息
     */
    private Letter getChatInfoTo(int letterId, String message) {
        Letter msg = new Letter();
        msg.setLetterId(letterId);
        msg.setSendUid(Long.parseLong(loginUser.getUid() + ""));
        msg.setOtherUid(Long.parseLong(otherUser.getUid() + ""));
        msg.setLayout(1);
        msg.setAvatar(loginUser.getAvatar());
        msg.setContent(message);
        msg.setNickname(loginUser.getNickname());
        msg.setSex(loginUser.getSex());
        msg.setUserLevel(loginUser.getUserLevel());
        msg.setTimestamp(System.currentTimeMillis());
        return msg;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent msg) {
        LogUtils.e("在当前界面收到一条私信消息");
        switch (msg.getType()) {
            case 90: //在当前界面收到一条私信消息
                Letter letter = new Gson().fromJson(msg.getMessage(), Letter.class);
                LogUtils.e(new Gson().toJson(letter));
                adapter.addData(letter);
                rvChat.scrollToPosition(adapter.getItemCount() - 1);
                db.userIsRead(otherUser.getUid(), loginUser.getUid());
                break;
        }
    }

    public static void startActivityNotification(Context context, User otherUser) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(OTHER_USER_KEY, otherUser);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (context == null) {
            CommonApp.getInstance().startActivity(intent);
        } else {
            context.startActivity(intent);
        }
    }

    public static void startActivity(Context context, User otherUser) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(OTHER_USER_KEY, otherUser);
        context.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.getInstance().isAppRunningForeground()) {
            LogUtils.dTag("isAppRunningForeground", "ture");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (App.getInstance().isAppRunningForeground()) {
            LogUtils.dTag("isAppRunningForeground", "false");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_sendmsg) {
            sendMsg();
        }
    }
}
