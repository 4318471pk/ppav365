package com.live.fox.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.db.DataBase;
import com.live.fox.entity.Letter;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.User;
import com.live.fox.server.Api_User;
import com.live.fox.ui.chat.ChatAdapter;
import com.live.fox.ui.chat.WrapContentLinearLayoutManager;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


/**
 * 直播间私信
 */
public class LiveRoomLetterFragment extends DialogFragment implements View.OnClickListener {

    TextView tvName;
    EditText etMsgcontent;
    RecyclerView rvChat;

    ChatAdapter adapter;
    List<Letter> messageList;

    WrapContentLinearLayoutManager wcLinearLayoutManger;

    User otherUser;
    User loginUser;

    public boolean isShow = false;

    public static LiveRoomLetterFragment newInstance(User otherUser) {
        LiveRoomLetterFragment fragment = new LiveRoomLetterFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("otherUser", otherUser);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            otherUser = (User) bundle.getSerializable("otherUser");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_liveroom_letter);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消

        initView(dialog);

        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);

        return dialog;
    }


    public void initView(Dialog dialog) {
        isShow = true;
        loginUser = AppUserManger.getUserInfo();
        LogUtils.e(new Gson().toJson(otherUser));

        tvName = dialog.findViewById(R.id.tv_name);
        etMsgcontent = dialog.findViewById(R.id.et_);
        rvChat = dialog.findViewById(R.id.rv_letter);

        tvName.setText(otherUser.getNickname());

        dialog.findViewById(R.id.tv_send).setOnClickListener(this);
        dialog.findViewById(R.id.iv_close).setOnClickListener(this);

        DataBase db = DataBase.getDbInstance();
        int count = db.getUnReadCount(otherUser.getUid(), loginUser.getUid());
        EventBus.getDefault().post(new MessageEvent(88, (-count) + ""));
        db.userIsRead(otherUser.getUid(), loginUser.getUid());

        initRecycleView();
        getLetterList();
    }


    public void initRecycleView() {
        wcLinearLayoutManger = new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvChat.setLayoutManager(wcLinearLayoutManger);
        adapter = new ChatAdapter(messageList = new ArrayList<>());
        rvChat.setAdapter(adapter);
    }

    public void getLetterList() {
        DataBase db = DataBase.getDbInstance();
        List<Letter> letter = db.getLetterListByUid(otherUser.getUid(), loginUser.getUid());
        messageList.clear();
        for (Letter mess : letter) {
            if (AppUserManger.getUserInfo() != null && mess.getNickname().equals(AppUserManger.getUserInfo().getNickname())) {
                mess.setLayout(1);
            }
            messageList.add(mess);
        }
        adapter.notifyDataSetChanged();
        rvChat.scrollToPosition(messageList.size() - 1);
    }


    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_send:
                String content = etMsgcontent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                doSendLetterApi(content);
                etMsgcontent.setText("");
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }


    /**
     * 发送私信
     */
    public void doSendLetterApi(String content) {
        Api_User.ins().sendLetter(otherUser.getUid(), content, new JsonCallback<Integer>() {
            @Override
            public void onSuccess(int code, String msg, Integer data) {
                //0,success,"86"
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                if (code == 0) {
                    Letter letter = getChatInfoTo(data, content);
                    interLetterToDB(letter);
                    messageList.add(letter);
                    adapter.setNewData(messageList);
                    rvChat.scrollToPosition(messageList.size() - 1);
                } else {
                    ToastUtils.showShort(msg);
                }

            }
        });
    }

    public void interLetterToDB(Letter letter) {
        DataBase db = DataBase.getDbInstance();
        db.insertLetter(letter);
        db.insertLetterList(otherUser, loginUser.getUid(), letter.getLetterId(), letter.getContent(), System.currentTimeMillis(), true);
    }

    //在当前界面收到一条私信消息
    public void getOneLetter(Letter letter) {
        messageList.add(letter);
        adapter.setNewData(messageList);
        rvChat.scrollToPosition(messageList.size() - 1);
        DataBase db = DataBase.getDbInstance();
        db.userIsRead(otherUser.getUid(), loginUser.getUid());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        isShow = false;
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
        msg.setType(1);
        msg.setAvatar(loginUser.getAvatar());
        msg.setContent(message);
        msg.setLayout(1);
        msg.setNickname(loginUser.getNickname());
        msg.setSex(loginUser.getSex());
        msg.setUserLevel(loginUser.getUserLevel());
        msg.setTimestamp(System.currentTimeMillis());
        return msg;
    }

    OnRoomSetClick roomSetClick;

    public void setOnRoomSetClick(OnRoomSetClick btnClick) {
        this.roomSetClick = btnClick;
    }

    public interface OnRoomSetClick {
        void onClick();
    }

}
