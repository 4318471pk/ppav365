package com.live.fox.view;

import android.content.Context;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StringUtils;

/**
 * Date: 2019/02/13
 */
public class ChatPanelView extends RelativeLayout implements View.OnClickListener {


    private Context context;
    private EditText et;
    private TextView tvSend;
    private ProgressBar progressSend;

    int pageType = 1; //1.直播预览界面的聊天  2.直播间的聊天
    String toNickName = ""; //空：普通聊天 不为空:@某人

    public ChatPanelView(Context context) {
        this(context, null);
    }

    public ChatPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void initView() {
        et = findViewById(R.id.et_input_message);
        progressSend = findViewById(R.id.progress_send);
        tvSend = findViewById(R.id.tv_send);
        tvSend.setOnClickListener(this);

//        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "fonts/xinhuakaiti.ttf");
//        et.setTypeface(iconfont);

        progressSend.setVisibility(View.GONE);
        tvSend.setVisibility(View.VISIBLE);
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
        switch (pageType) {
            case 1://直播预览界面的聊天
                et.setHint(context.getString(R.string.zcsrbczbdmpjg));
                et.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                break;
            case 2: //直播间的聊天
                et.setHint(context.getString(R.string.zcsrltnr));
                et.setMaxEms(160);
                et.setImeOptions(EditorInfo.IME_ACTION_SEND);
                et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            sendMsg();
                            return true;
                        }
                        return false;
                    }
                });
                tvSend.setText(context.getString(R.string.send));
                break;
        }
    }

    /**
     * nickName 空：普通聊天 不为空:@某人
     *
     * @param toNickName 用户有名称
     */
    public void setNickName(String toNickName) {
        if (StringUtils.isEmpty(toNickName)) {
            this.toNickName = "";
            et.setHint(context.getString(R.string.zcsrltnr));
        } else {
            this.toNickName = "@" + toNickName + " ";
            et.setHint(this.toNickName);
        }
    }


    //1.发送中  2.发送完成
    public void changeStata(int state) {
        switch (state) {
            case 1://1.发送中
                tvSend.setVisibility(View.INVISIBLE);
                progressSend.setVisibility(View.VISIBLE);
                break;
            case 2: //2.发送完成
                tvSend.setVisibility(View.VISIBLE);
                progressSend.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send: //发送聊天内容
                sendMsg();
                break;
        }
    }

    public void sendMsg() {
        String content = et.getText().toString().trim();
        LogUtils.e("发送聊天内容" + content);
        et.setText("");
        if (listener != null) {
            listener.sendChatMsg(toNickName + content);
        }
    }


    OnChatActionListener listener;

    public void setOnChatListener(ChatPanelView.OnChatActionListener listener) {
        this.listener = listener;
    }

    public interface OnChatActionListener {
        void sendChatMsg(String msg);
    }


}
