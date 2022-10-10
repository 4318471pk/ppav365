package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.db.DataBase;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.entity.Letter;
import com.live.fox.entity.LetterList;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.SysNotice;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_User;
import com.live.fox.ui.chat.ChatActivity;
import com.live.fox.ui.chat.GfNoticeActivity;
import com.live.fox.ui.chat.SysNoticeActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息页面
 */
public class MessageActivity extends BaseHeadActivity {

    SwipeMenuRecyclerView rv;

    View headView, gfggView;
    TextView tvSysNoticeContent, tvgfggContent,
            tvSysNoticeTime, tvgfggTime;

    BaseQuickAdapter<LetterList, BaseViewHolder> sysAdapter;
    BaseQuickAdapter<LetterList, BaseViewHolder> emailAdapter;
    List<LetterList> sysData = new ArrayList<>();
    List<LetterList> emailData = new ArrayList<>();

    List<LetterList> list = new ArrayList<>();
    List<SysNotice> sysNoticeList = new ArrayList<>();//官方数据
    List<Letter> gfggList = new ArrayList<>();//系统通知
    User loginUser;
    DataBase db;

    boolean isSys = true;
    private TextView TvSysNotice;
    private RoundTextView tvNoReadSys;
    private View viewSys;
    private TextView TvEmailNotice;
    private RoundTextView tvNoReadEmail;
    private View viewEmail;


    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MessageActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.msg_center), true, true);
        setRightText(getString(R.string.edit), getResources().getColor(R.color.colorA800FF));
        showBottomView(false);
        TvSysNotice= findViewById(R.id.sys_notice);
        tvNoReadSys = findViewById(R.id.tvNoReadSys);
        viewSys = findViewById(R.id.viewSys);
        TvEmailNotice= findViewById(R.id.email_notice);
        tvNoReadEmail= findViewById(R.id.tvNoReadEmail);
        viewEmail = findViewById(R.id.viewEamil);

        rv = findViewById(R.id.message_list_recycler);
        initRecycleView();
        doGetSysNoticeApi();

        findViewById(R.id.layoutSys).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSys = true;
                changeUi();
            }
        });

        findViewById(R.id.layoutEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSys = false;
                changeUi();
            }
        });

    }

    private void changeUi(){
        if (isSys) {
            viewSys.setVisibility(View.VISIBLE);
            viewEmail.setVisibility(View.GONE);
            rv.setAdapter(sysAdapter);
        } else {
            viewSys.setVisibility(View.GONE);
            viewEmail.setVisibility(View.VISIBLE);
            rv.setAdapter(emailAdapter);
        }
    }

    public void refreshPage() {
        loginUser = DataCenter.getInstance().getUserInfo().getUser();
        db = DataBase.getDbInstance();
        list.clear();
        list.addAll(db.getLetterList(loginUser.getUid()));
        if (list != null) {
            LogUtils.e(new Gson().toJson(list));
        }
        sysAdapter.setNewData(list);
    }

    public void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MessageActivity.this);
        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new RecyclerSpace(1));
        rv.setSwipeMenuCreator(swipeMenuCreator);
        rv.setSwipeMenuItemClickListener(mMenuItemClickListener);
        rv.setSwipeItemClickListener((itemView, position) -> {
            //用户私信聊天
            LetterList letterList = sysAdapter.getData().get(position);
            User user = new User();
            user.setUid(letterList.getOtherUid());
            user.setAvatar(letterList.getAvatar());
            user.setNickname(letterList.getNickname());
            user.setSex(letterList.getSex());
            user.setUserLevel(letterList.getUserLevel());
            ChatActivity.startActivity(MessageActivity.this, user);
        });

        sysAdapter = new BaseQuickAdapter<LetterList, BaseViewHolder>(R.layout.item_msg_list, new ArrayList<>()) {
            @Override
            protected void convert(BaseViewHolder helper, LetterList item) {
//                helper.setText(R.id.tv_nickname, item.getNickname());
//                helper.setText(R.id.tv_content, item.getContent());
//                helper.setText(R.id.tv_time, TimeUtils.convertShortTime(item.getTimestamp()));
//                int unreadCount = item.getUnReadCount();
//                if (unreadCount <= 0) {
//                    helper.getView(R.id.tv_unreadmsg).setVisibility(View.GONE);
//                } else {
//                    helper.getView(R.id.tv_unreadmsg).setVisibility(View.VISIBLE);
//                    if (unreadCount <= 99) {
//                        helper.setText(R.id.tv_unreadmsg, unreadCount + "");
//                    } else {
//                        helper.setText(R.id.tv_unreadmsg, "99+");
//                    }
//                }
//                GlideUtils.loadDefaultCircleImage(mContext, item.getAvatar(), helper.getView(R.id.message_avatar));
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MsgDetailActivity.startActivity(MessageActivity.this, isSys);
                    }
                });

            }
        };



        emailAdapter = new BaseQuickAdapter<LetterList, BaseViewHolder>(R.layout.item_msg_list, new ArrayList<>()) {
            @Override
            protected void convert(BaseViewHolder helper, LetterList item) {

            }
        };

        rv.setAdapter(sysAdapter);
        headView = getLayoutInflater().inflate(R.layout.chatlist_head, rv, false);//系统通知
        gfggView = getLayoutInflater().inflate(R.layout.gfgg_head, rv, false);//官方公告
        TextView official = gfggView.findViewById(R.id.message_title_system);
        String str = getText(R.string.app_name) + " " + getString(R.string.message);
        official.setText(str);

        tvSysNoticeContent = headView.findViewById(R.id.tv_des);
        tvgfggContent = gfggView.findViewById(R.id.tv_des);
        tvSysNoticeTime = headView.findViewById(R.id.tv_time_sys);
        tvgfggTime = gfggView.findViewById(R.id.tv_time_gfgg);
        rv.addHeaderView(gfggView);
        rv.addHeaderView(headView);
        headView.setOnClickListener(view -> {
            SysNoticeActivity.startActivity(MessageActivity.this);//系统通知列表
        });

        gfggView.setOnClickListener(view -> {
            GfNoticeActivity.startActivity(MessageActivity.this);
        });
    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private final SwipeMenuCreator swipeMenuCreator = (swipeLeftMenu, swipeRightMenu, position) -> {
        int width = DeviceUtils.dp2px(this, 20);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(MessageActivity.this)
                    .setBackground(R.drawable.selector_red)
                    .setImage(R.drawable.message_del)
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
        }
    };

    private final SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                DialogFactory.showTwoBtnDialog(getCtx(), getString(R.string.delete_message_tips_confirm), (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                    Api_User.ins().deleteLetter1(list.get(position).getOtherUid(), 2, new JsonCallback<Integer>() {
                        @Override
                        public void onSuccess(int code, String msg, Integer data) {
                            if (code == 0) {
                                if (list.get(position).getOtherUid() > 0 && list.get(position).getLoginUid() > 0) {
                                    db.deleteChatMessage(list.get(position).getOtherUid(), list.get(position).getLoginUid());
                                }
                                list.clear();
                                list.addAll(db.getLetterList(loginUser.getUid()));
                                sysAdapter.notifyDataSetChanged();
                            } else {
                                ToastUtils.showShort(msg);
                            }
                        }
                    });
                    dialog.dismiss();
                }).setTextDes(getString(R.string.delete_message_tips));
            }
        }
    };

    /**
     * 官方公告system/notice
     */
    public void doGetSysNoticeApi() {
        Api_Config.ins().getSysNotice(new JsonCallback<List<SysNotice>>() {
            @Override
            public void onSuccess(int code, String msg, List<SysNotice> data) {
                if (code == 0) {
                    if (data != null && data.size() > 0) {
                        sysNoticeList.addAll(data);
                        SPUtils.getInstance("sysnotice").put("data", new Gson().toJson(data));
                        tvgfggContent.setText(LanguageUtilsEntity.getLanguage(data.get(data.size() - 1).getContent()));
                        tvgfggTime.setText(TimeUtils.convertShortTime(data.get(data.size() - 1).getCreateTime()));
                    } else {
                        tvgfggContent.setText(getString(R.string.nothing));
                    }
                } else {
                    LogUtils.e("官方公告获取失败: " + msg);
                }
            }
        });

        //系统通知 center-client/live/systemLetter/list
        Api_User.ins().getGfNotice(new JsonCallback<List<Letter>>() {
            @Override
            public void onSuccess(int code, String msg, List<Letter> data) {
                if (code == 0) {
                    if (data != null && data.size() > 0) {
                        gfggList.addAll(data);
                        SPUtils.getInstance("gfsjnotice").put("data", new Gson().toJson(data));
                        tvSysNoticeContent.setText(data.get(data.size() - 1).getContent());
                        tvSysNoticeTime.setText(TimeUtils.convertShortTime(data.get(data.size() - 1).getTimestamp()));
                    } else {
                        tvSysNoticeContent.setText(getString(R.string.nothing));
                    }
                } else {
                    LogUtils.e("系统通知获取失败: " + msg);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent msg) {
        if (msg.getType() == 90) { //私信
            refreshPage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPage();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
