package com.live.fox.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.FunctionItem;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_Risk;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
import com.live.fox.ui.mine.noble.NobleFragment;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * 直播间的
 * 主播信息
 * 用户信息弹框
 */
public class UserDetailForCardFragment extends DialogFragment implements View.OnClickListener {

    RoundedImageView siv_card_avatar;
    TextView tv_card_name;
    TextView tv_card_id;
    TextView tv_card_sign;
    TextView tv_card_follow_count;
    TextView tv_card_fans_count;
    TextView tv_card_black;
    TextView tv_gift_receive;
    TextView tv_gift_send;
    TextView tv_card_copy_id;
    LinearLayout ll_bottom;
    View ll_bottom_line;
    RelativeLayout dialog_avatar;
    TextView tv_card_follow;
    LinearLayout ll_card_letter;
    LinearLayout ll_card_black;
    TextView tv_card_report;
    TextView iv_live_in_follow;
    ImageView iv_vip;
    ImageView ivPre;
    TextView tv_usermain;
    TextView tv_at;
    TextView tv_letter;

    private User cardUserInfo;
    private User myUserInfo;
    boolean myIsAnchor = false; //打开这个卡片的人是否是主播
    boolean myIsRoomManager = false; //打开这个卡片的人是否是房管
    long anchorId = 0;
    public boolean isShow = false;

    long liveId = 0;

    public static UserDetailForCardFragment newInstance(User cardUser) {
        UserDetailForCardFragment fragment = new UserDetailForCardFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("cardUser", cardUser);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.LoadingDialog);
        Bundle bundle = getArguments();
        if (bundle != null) {
            cardUserInfo = (User) bundle.getSerializable("cardUser");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dialog_liveroom_userinfo, container, false);
    }

    //如果需要修改大小 修改显示动画 重写此方法
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        getDialog().setCanceledOnTouchOutside(true);
        window.setGravity(Gravity.CENTER);  //设置Dialog的显示位置
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.BottomToCenterDialog); // 进出动画效果
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.dimAmount = 0f;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        isShow = true;
        initView(view);
        refreshPage(false, cardUserInfo, liveId, anchorId, myIsAnchor, false);
    }


    public void initView(View view) {
//        rl_card_container = dialog.findViewById(R.id.rl_card_container);
        siv_card_avatar = view.findViewById(R.id.siv_card_avatar);
        siv_card_avatar.setOnClickListener(this);
        iv_live_in_follow = view.findViewById(R.id.iv_live_in_follow);
        ivPre = view.findViewById(R.id.ivPre);
        iv_live_in_follow.setOnClickListener(this);
        tv_card_name = view.findViewById(R.id.tv_card_name);
        tv_card_id = view.findViewById(R.id.tv_card_id);
        tv_card_copy_id = view.findViewById(R.id.tv_card_copy_id);
        tv_card_sign = view.findViewById(R.id.tv_card_sign);
        tv_card_follow_count = view.findViewById(R.id.tv_focus_num);
        tv_card_fans_count = view.findViewById(R.id.fans_num_tv);
        tv_gift_receive = view.findViewById(R.id.tv_gift_receive);
        tv_gift_send = view.findViewById(R.id.tv_gift_send);
        tv_card_black = view.findViewById(R.id.tv_card_black);
        ll_bottom = view.findViewById(R.id.ll_bottom);
        tv_card_follow = view.findViewById(R.id.tv_card_follow);
        ll_card_letter = view.findViewById(R.id.ll_card_letter);
        ll_card_black = view.findViewById(R.id.ll_card_black);
        tv_card_report = view.findViewById(R.id.tv_card_report);
        iv_vip = view.findViewById(R.id.iv_vip);
        ll_bottom_line = view.findViewById(R.id.ll_bottom_line);
        dialog_avatar = view.findViewById(R.id.dialog_avatar);

        tv_usermain = view.findViewById(R.id.tv_usermain);
        tv_at = view.findViewById(R.id.tv_at);
        tv_letter = view.findViewById(R.id.tv_letter);

        tv_card_copy_id.setOnClickListener(this);
        tv_card_report.setOnClickListener(this);
        tv_usermain.setOnClickListener(this);
        tv_at.setOnClickListener(this);
        tv_letter.setOnClickListener(this);
        ll_card_black.setOnClickListener(this);
    }


    /**
     * isAfterApi 是否是刷新接口后加载
     */
    public void refreshPage(boolean isAfterApi, User cardUserInfo, long liveId, long anchorId, boolean myIsAnchor, boolean myIsRoomManager) {
        myUserInfo = DataCenter.getInstance().getUserInfo().getUser();
        this.cardUserInfo = cardUserInfo;
        this.liveId = liveId;
        this.anchorId = anchorId;
        this.myIsAnchor = myIsAnchor;
        this.myIsRoomManager = myIsRoomManager;
        LogUtils.e("自己的信息:" + new Gson().toJson(myUserInfo));
        LogUtils.e("Card用户的信息:" + new Gson().toJson(cardUserInfo));

        //如果是自己的信息 则隐藏底部功能栏
        if (cardUserInfo.getUid() == myUserInfo.getUid()) {
            ll_bottom.setVisibility(View.GONE);
            ll_bottom_line.setVisibility(View.GONE);
            ll_card_black.setVisibility(View.GONE);
        } else {
            ll_bottom.setVisibility(View.VISIBLE);
            ll_bottom_line.setVisibility(View.VISIBLE);
            ll_card_black.setVisibility(View.VISIBLE);
        }


        //设置卡片信息
        tv_card_name.setText(ChatSpanUtils.ins().getAnchorNickNameSpan(cardUserInfo, requireContext()));

        if (cardUserInfo.getBadgeList() != null) {
            FunctionItem mNobleRes = null;
            if (cardUserInfo.getBadgeList().contains(6)) {
                mNobleRes = NobleFragment.getNobleRes(requireContext(), Constant.LEVEL1);
            } else if (cardUserInfo.getBadgeList().contains(7)) {
                mNobleRes = NobleFragment.getNobleRes(requireContext(), Constant.LEVEL2);

            } else if (cardUserInfo.getBadgeList().contains(8)) {
                mNobleRes = NobleFragment.getNobleRes(requireContext(), Constant.LEVEL3);

            } else if (cardUserInfo.getBadgeList().contains(9)) {
                mNobleRes = NobleFragment.getNobleRes(requireContext(), Constant.LEVEL4);

            } else if (cardUserInfo.getBadgeList().contains(10)) {
                mNobleRes = NobleFragment.getNobleRes(requireContext(), Constant.LEVEL5);
            }

            if (mNobleRes != null) {
                ivPre.setImageResource(mNobleRes.getResSmall());
                dialog_avatar.setBackgroundResource(mNobleRes.circleBg);
                tv_card_id.setTextColor(Color.parseColor(mNobleRes.colorRes));
            }
            GlideUtils.loadDefaultCircleImage(getActivity(), cardUserInfo.getAvatar(), siv_card_avatar);

        }

        if (cardUserInfo.vipUid != null) {
            tv_card_id.setText(getString(R.string.goodName) + cardUserInfo.vipUid);

        } else {
            //String.format(getString(R.string.colon),getString(R.string.id_number),cardUserInfo.getUid());
            tv_card_id.setText(getString(R.string.identity_id) + cardUserInfo.getUid());
        }
        tv_card_sign.setText(cardUserInfo.getSignature());


        if (isAfterApi) {
            tv_gift_receive.setText(RegexUtils.westMoney(cardUserInfo.getReceiveCoin()));
            tv_gift_send.setText(RegexUtils.westMoney(cardUserInfo.getSendCoin()));
            updateFollow();
            updateBlack();
            if (cardUserInfo.isVip()) {
                iv_vip.setVisibility(View.VISIBLE);
            } else {
                iv_vip.setVisibility(View.GONE);
            }

            //头部左按钮设置
            if (myUserInfo.getUid() == cardUserInfo.getUid()) {
                tv_card_report.setVisibility(View.INVISIBLE);
            } else {
                tv_card_report.setVisibility(View.VISIBLE);
                if (myUserInfo.getManage() == 1) { //超管
                    tv_card_report.setVisibility(View.VISIBLE);
                    tv_card_report.setText(getString(R.string.manager));
                } else if (myUserInfo.getManage() == 0) { //普通人
                    if (myIsAnchor || (myIsRoomManager && anchorId != cardUserInfo.getUid())) {
                        //如果是主播或者是房管
                        tv_card_report.setVisibility(View.VISIBLE);
                        tv_card_report.setText(getString(R.string.manager));
                    } else {
                        tv_card_report.setVisibility(View.INVISIBLE);
                        tv_card_report.setText(getString(R.string.report));
                    }

                }
            }
        }

    }

    /**
     * 成功或失败的Toast提示
     */
    MMToast mmToast;

    public void showToastTip(boolean isSuccess, String msg) {
        if (mmToast != null) {
            mmToast.cancel();
        }
        MMToast.Builder builder = new MMToast.Builder(getActivity())
                .setMessage(msg)
                .setSuccess(isSuccess);
        mmToast = builder.create(Toast.LENGTH_SHORT);
        mmToast.show();
    }

    public void updateFollow() {
        if (cardUserInfo.isFollow()) {
            iv_live_in_follow.setTextColor(Color.LTGRAY);
            iv_live_in_follow.setText(getString(R.string.focused));
        } else {
            iv_live_in_follow.setTextColor(Color.parseColor("#EF32A5"));
            iv_live_in_follow.setText(getString(R.string.focus));
        }

        tv_card_follow_count.setText(RegexUtils.westMoney(cardUserInfo.getFollows()));
        tv_card_fans_count.setText(RegexUtils.westMoney(cardUserInfo.getFans()));
    }


    public void updateBlack() {
        if (cardUserInfo.isReject()) {
            tv_card_black.setTextColor(Color.LTGRAY);
            tv_card_black.setText(getString(R.string.blacked));
        } else {
            tv_card_black.setTextColor(Color.BLACK);
            tv_card_black.setText(getString(R.string.black));
        }
    }

    @Override
    public void onClick(View v) {
        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_card_copy_id:
                ClipboardUtils.copyText(cardUserInfo.getUid() + "");
                showToastTip(true, getString(R.string.userCopy));
                break;
            case R.id.siv_card_avatar:

                break;
            case R.id.tv_card_report:
                LogUtils.e("tv_card_report " + myUserInfo.getManage());
                if (myUserInfo.getManage() == 0) { //打开卡片的人不是超管
                    if (myIsAnchor) {
                        //主播点击观众卡片的管理
                        tv_card_report.setVisibility(View.VISIBLE);
                        tv_card_report.setText(getString(R.string.manager));

                        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                        android.view.Menu menu_more = popupMenu.getMenu();
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 0,
                                0, cardUserInfo.isBlackChat() ? getString(R.string.liftBan) : getString(R.string.forbiddenWords));
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 1, 1, getString(R.string.kicking));
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 2, 2,
                                cardUserInfo.getBadgeList().contains(5) ? getString(R.string.cancelManagement) : getString(R.string.setAsManagement));
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(item -> {
                            if (item.getItemId() == android.view.Menu.FIRST + 0) {
                                //禁言
                                dpBlackChatApi();
                            } else if (item.getItemId() == android.view.Menu.FIRST + 1) {
                                //踢人
                                dpBanUserApi();
                            } else if (item.getItemId() == android.view.Menu.FIRST + 2) {
                                boolean isSetRoomManager = true;
                                if (cardUserInfo.getBadgeList() == null) {
                                    isSetRoomManager = true;
                                } else {
                                    isSetRoomManager = cardUserInfo.getBadgeList().contains(5) ? false : true;
                                }
                                //设置为房管
                                doRoomManagerApi(isSetRoomManager);
                            }
                            return true;
                        });
                    } else if (myIsRoomManager && anchorId != cardUserInfo.getUid()) {
                        LogUtils.e("打开的人是房管");
                        //房管点击观众卡片的管理
                        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                        android.view.Menu menu_more = popupMenu.getMenu();
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 0, 0, cardUserInfo.isBlackChat() ? getString(R.string.liftBan) : getString(R.string.forbiddenWords));
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 1, 1, getString(R.string.kicking));
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(item -> {
                            if (item.getItemId() == android.view.Menu.FIRST + 0) {
                                //禁言
                                dpBlackChatApi();
                            } else if (item.getItemId() == android.view.Menu.FIRST + 1) {
                                //踢人
                                dpBanUserApi();
                            }
                            return true;
                        });
                    } else {

                    }
                } else if (myUserInfo.getManage() == 1) { //打开卡片的人是超管
                    if (anchorId == cardUserInfo.getUid()) {
                        //超管点击主播卡片的管理
                        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                        android.view.Menu menu_more = popupMenu.getMenu();
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 0, 0, getString(R.string.closeLive));
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 1, 1, getString(R.string.blackNumber));
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 2, 2, getString(R.string.blackPhone));
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(item -> {
                            if (item.getItemId() == android.view.Menu.FIRST + 0) {
                                //超管关播
                                Api_Live.ins().kicklive(liveId, new JsonCallback<String>() {
                                    @Override
                                    public void onSuccess(int code, String msg, String data) {
                                        LogUtils.e("blackUser result : " + data);
                                        if (code == 0) {
                                            dismiss();
                                            ToastUtils.showShort(getString(R.string.closeLiveSuccess));
                                        } else {
                                            ToastUtils.showShort(getString(R.string.closeLiveFail) + msg);
                                        }
                                    }
                                });
                            } else if (item.getItemId() == android.view.Menu.FIRST + 1) {
                                //封号
                                dpBlackUserApi(1);
                            } else if (item.getItemId() == android.view.Menu.FIRST + 2) {
                                //封机
                                dpBlackUserApi(2);
                            }
                            return true;
                        });
                    } else {
                        //超管点击普通用户卡片的管理
                        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                        android.view.Menu menu_more = popupMenu.getMenu();
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 0, 0, cardUserInfo.isBlackChat() ? getString(R.string.liftBan) : getString(R.string.forbiddenWords));
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 1, 1, getString(R.string.kicking));
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 2, 2, getString(R.string.blackNumber));
                        menu_more.add(android.view.Menu.NONE, android.view.Menu.FIRST + 3, 3, getString(R.string.blackPhone));
                        popupMenu.show();
                        popupMenu.setOnMenuItemClickListener(item -> {
                            if (item.getItemId() == android.view.Menu.FIRST + 0) {
                                //禁言
                                dpBlackChatApi();
                            } else if (item.getItemId() == android.view.Menu.FIRST + 1) {
                                //踢人
                                dpBanUserApi();
                            } else if (item.getItemId() == android.view.Menu.FIRST + 2) {
                                //封号
                                dpBlackUserApi(1);
                            } else if (item.getItemId() == android.view.Menu.FIRST + 3) {
                                //封机
                                dpBlackUserApi(2);
                            }
                            return true;
                        });
                    }
                }
                break;
            case R.id.tv_card_close:
            case R.id.ll_card_follow:
                dismiss();
                break;
            case R.id.tv_letter: //私信
                dismiss();
                EventBus.getDefault().post(new MessageEvent(3, new Gson().toJson(cardUserInfo)));
                break;
            case R.id.tv_at: //@Ta
                dismiss();
                EventBus.getDefault().post(new MessageEvent(4, new Gson().toJson(cardUserInfo)));
                break;
            case R.id.tv_usermain: //主页
                UserDetailActivity.startActivity(getActivity(), cardUserInfo.getUid());
                break;
            case R.id.iv_live_in_follow: //关注
                iv_live_in_follow.setEnabled(false);
                Api_User.ins().follow(cardUserInfo.getUid(), !cardUserInfo.isFollow(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        if (result != null) LogUtils.e("follow result : " + result);
                        iv_live_in_follow.setEnabled(true);
                        if (code == 0 && result != null) {
                            cardUserInfo.setFollow(!cardUserInfo.isFollow());
                            cardUserInfo.setFans(cardUserInfo.isFollow() ? cardUserInfo.getFans() + 1 : cardUserInfo.getFans() - 1);
                            ToastUtils.showShort(cardUserInfo.isFollow() ? getString(R.string.successFocus) : getString(R.string.cancelFocus));
                            updateFollow();
                            EventBus.getDefault().post(new MessageEvent(5, new Gson().toJson(cardUserInfo)));
                        }

                    }
                });
                break;
            case R.id.ll_card_black:
                Api_User.ins().setBlack(cardUserInfo.getUid(), !cardUserInfo.isReject(), new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        if (result != null) LogUtils.e("black result : " + result);
                        tv_card_black.setEnabled(true);
                        if (code == 0 && result != null) {
                            cardUserInfo.setReject(!cardUserInfo.isReject());
                            ToastUtils.showShort(cardUserInfo.isReject() ? getString(R.string.blackSuccess) : getString(R.string.cancelBlack));
                            updateBlack();
                        }

                    }
                });
                break;
        }

    }

    //禁言
    public void dpBlackChatApi() {
        Api_Live.ins().blackChat(liveId+"", cardUserInfo.getUid()+"", !cardUserInfo.isBlackChat(), new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                LogUtils.e("blackChat result : " + data);
                if (code == 0) {
                    cardUserInfo.setBlackChat(!cardUserInfo.isBlackChat());
                    ToastUtils.showShort(cardUserInfo.isBlackChat() ? getString(R.string.forbiddenWordsSucceed) : getString(R.string.banLiftedSuccessfully));
                } else {
                    ToastUtils.showShort(getString(R.string.tabooFailed) + msg);
                }
            }
        });
    }

    //踢人
    public void dpBanUserApi() {
        Api_Live.ins().banuser(liveId+"", cardUserInfo.getUid()+"", new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                LogUtils.e("blackUser result : " + data);
                if (code == 0) {
                    ToastUtils.showShort(getString(R.string.kickingSuccess));
                } else {
                    ToastUtils.showShort(getString(R.string.kickingFail) + msg);
                }
            }
        });
    }



    //1封号、2封机
    public void dpBlackUserApi(int type) {
        LogUtils.e(cardUserInfo.getUid());
        Api_Risk.ins().blackUser(cardUserInfo.getUid(), type, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                LogUtils.e("blackUser result : " + data);
                if (code == 0) {
                    ToastUtils.showShort(type == 1 ? getString(R.string.blackNumberSuccess) : getString(R.string.blackPhoneSuccess));
                } else {
                    ToastUtils.showShort(type == 1 ? getString(R.string.blackNumberSuccess) : getString(R.string.blackPhoneSuccess) + msg);
                }
            }
        });
    }

    //设置/取消 房管
    public void doRoomManagerApi(boolean isSetManager) {
        Api_Live.ins().roomManager(cardUserInfo.getUid()+"", anchorId+"", isSetManager, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                LogUtils.e("roomManager result : " + data);
                if (code == 0) {
                    if (isSetManager) {
                        if (cardUserInfo == null) {
                            cardUserInfo.setBadgeList(new ArrayList<>());
                        }
                        cardUserInfo.getBadgeList().add(5);
                        tv_card_name.setText(ChatSpanUtils.ins().getAnchorNickNameSpan(cardUserInfo, requireContext()));
                        ToastUtils.showShort(getString(R.string.setSuccess));
                    } else {
                        if (cardUserInfo.getBadgeList() != null) {
                            ArrayList<Integer> temp = new ArrayList<>();
                            for (int i = 0; i < cardUserInfo.getBadgeList().size(); i++) {
                                if (cardUserInfo.getBadgeList().get(i) != 5) {
                                    temp.add(cardUserInfo.getBadgeList().get(i));
                                }
                            }
                            cardUserInfo.setBadgeList(temp);
                        }
                        tv_card_name.setText(ChatSpanUtils.ins().getAnchorNickNameSpan(cardUserInfo, requireContext()));
                        ToastUtils.showShort(getString(R.string.managementCancelled));
                    }
                } else {
                    ToastUtils.showShort(getString(R.string.setFail) + msg);
                }
            }
        });
    }


    @Override
    public void dismiss() {
        isShow = false;
        super.dismiss();
    }

    OnBtnSureClick btnSureClick;

    public void setBtnSureClick(OnBtnSureClick btnClick) {
        this.btnSureClick = btnClick;
    }

    public interface OnBtnSureClick {
        void onClick(int index);
    }

}
