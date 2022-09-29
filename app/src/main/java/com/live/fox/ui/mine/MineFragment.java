package com.live.fox.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.ConstantValue;
import com.live.fox.MainActivity;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.MineFragmentBinding;
import com.live.fox.db.DataBase;
import com.live.fox.dialog.DialogGoBindPhone;
import com.live.fox.dialog.DialogGoBindPhoneOnWithdrawal;
import com.live.fox.entity.Letter;
import com.live.fox.entity.LetterList;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_User;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.ui.mine.setting.PhoneBindingActivity;
import com.live.fox.ui.mine.setting.SettingActivity;
import com.live.fox.ui.mine.withdraw.WithdrawalActivity;
import com.live.fox.ui.mine.noble.NobleActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;


/**
 * 用户中心
 */
public class MineFragment extends BaseBindingFragment implements AppIMManager.OnMessageReceivedListener {

    private MineFragmentBinding mBind;
    private User userinfo;
    String headUrl = "";
    private boolean isNoble;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantValue.REQUEST_CODE1 && resultCode==ConstantValue.GUEST_BINDPHONE)
        {
            mBind.tvLinkedPhone.setText(getStringWithoutContext(R.string.identificationDone));
            mBind.tvLinkedPhone.setTextColor(0xff1FC478);
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.mine_fragment;
    }

    @Override
    public void initView(View view) {
        EventBus.getDefault().register(this);
        mBind=getViewDataBinding();
        mBind.setClick(this);

        refreshUserinfo(false);
        AppIMManager.ins().addMessageListener(MineFragment.class, this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) return;

        StatusBarUtil.setStatusBarFulAlpha(requireActivity());
        BarUtils.setStatusBarVisibility(requireActivity(), true);
        BarUtils.setStatusBarLightMode(requireActivity(), false);
    }

    public void refreshUserinfo(boolean flag) {
        userinfo = DataCenter.getInstance().getUserInfo().getUser();

        if (!userinfo.getAvatar().equals(headUrl)) {
            headUrl = userinfo.getAvatar();
            GlideUtils.loadCircleOnePxRingImage(requireActivity(), userinfo.getAvatar(),
                    Color.parseColor("#979797"),
                    R.color.transparent, R.drawable.img_default, mBind.ivHeadimg);
        }

        mBind.balanceMoneyTv.setText(RegexUtils.westMoney(userinfo.getGoldCoin()));
        mBind.tvNickname.setText(userinfo.getNickname());
        mBind.tvSex.setText(ChatSpanUtils.ins().getUserInfoSpan(userinfo, requireActivity()));
        String format = String.format(getString(R.string.colon_number), getString(R.string.identity_id), userinfo.getUid());
        mBind.tvIdnum.setText(format);
        mBind.tvCirclenum.setText("0");
        mBind.tvFollownum.setText(String.valueOf(userinfo.getFollows()));
        mBind.tvFansnum.setText(String.valueOf(userinfo.getFans()));
        mBind.tvNickname.setVisibility(View.VISIBLE);
        mBind.layoutId.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(userinfo.getPhone()))
        {
            mBind.tvLinkedPhone.setText(getStringWithoutContext(R.string.identificationDone));
            mBind.tvLinkedPhone.setTextColor(0xff1FC478);
        }
        else
        {
            mBind.tvLinkedPhone.setText(getStringWithoutContext(R.string.notYetIdentification));
            mBind.tvLinkedPhone.setTextColor(0xff404040);
        }

        if (flag) {
            mBind.ivRightdes.setVisibility(View.GONE);
            doGetLetterListApi();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        doGetUserInfoApi();
    }

    @Override
    public void onIMReceived(int protocol, String msg) {
        try {
            JSONObject message = new JSONObject(msg);
            if (protocol == Constant.MessageProtocol.PROTOCOL_BALANCE_CHANGE) { //12.金币变动消息
                long uid = message.optLong("uid", -1);
                Double goldCoin = message.optDouble("goldCoin", -1);
                if (uid == userinfo.getUid()) {
                    userinfo.setGoldCoin(goldCoin.floatValue());
                    DataCenter.getInstance().getUserInfo().updateUser(userinfo);
                    mBind.balanceMoneyTv.setText(RegexUtils.westMoney(goldCoin));
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    /**
     * 获取用户信息
     */
    public void doGetUserInfoApi() {
        Api_User.ins().getUserInfo(-1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (ActivityUtils.getTopActivity() instanceof MainActivity) {
                    if (code == 0) {
                        refreshUserinfo(true);
                    } else if (code == 2008) { //用户不存在
                        LoginModeSelActivity.startActivity(requireActivity());
                    }
                } else if (code == 2008) {   //用户不存在
                    LoginModeSelActivity.startActivity(requireActivity());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        AppIMManager.ins().removeMessageReceivedListener(MineFragment.class);
        super.onDestroy();
    }



    public void doGetLetterListApi() {
        if (userinfo == null) return;
        showLoadingDialog();
        DataBase db = DataBase.getDbInstance();
        Letter lastLetter = db.getLastLetter(userinfo.getUid());
        List<LetterList> list = db.getLetterList(userinfo.getUid());
        if (list != null && list.size() > 0 && list.get(0).getUnReadCount() > 0) {
            mBind.ivRightdes.setVisibility(View.VISIBLE);
        }

        Api_User.ins().getLetterList(lastLetter == null ? 0 : lastLetter.getLetterId(), new JsonCallback<List<Letter>>() {//
            @Override
            public void onSuccess(int code, String msg, List<Letter> data) {
                dismissLoadingDialog();
                if (code == 0) {
                    if (data == null) return;
                    for (Letter letter : data) {
                        User otherUser = new User();
                        otherUser.setUid(letter.getOtherUid());
                        otherUser.setAvatar(letter.getAvatar());
                        if (StringUtils.isEmpty(letter.getNickname()))
                            return;
                        otherUser.setNickname(letter.getNickname());
                        otherUser.setSex(letter.getSex());
                        otherUser.setUserLevel(letter.getUserLevel());
                        if (1 != letter.getStatua() && letter.getSendUid() != userinfo.getUid()) {
                            mBind.ivRightdes.setVisibility(View.VISIBLE);
                        }
                        db.insertLetterList(otherUser, userinfo.getUid(),
                                letter.getLetterId(), letter.getContent(), letter.getTimestamp(),
                                1 == letter.getStatua() || letter.getSendUid() == userinfo.getUid());

                        if (letter.getSendUid() == letter.getOtherUid()) {
                            letter.setOtherUid(userinfo.getUid());
                        }
                        db.insertLetter(letter);
                    }
                } else {
                    LogUtils.e("私信列表获取失败: " + msg);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent msg) {
        if (msg.getType() == 90) { //私信
            mBind.ivRightdes.setVisibility(View.VISIBLE);
        }
    }

//    @Override
//    public void onItemClick(View view, int position) {
//        if (view.getTag() == null) return;
//        if (ClickUtil.isFastDoubleClick()) return;
//        if (columnListBean != null) {
//            switch (columnListBean.getType()) {
//                case 1: //我的余额
//                    MyBalanceActivity.startActivity(requireActivity());
//                    break;
//
//                case 2: //交易记录 资产记录 http://8.210.80.72:8103/swagger-ui.html#/user-controller
//                    TransactionActivity.launch(requireActivity());
//                    break;
//
//                case 3://游戏记录
//                    MyGameRecordActivity.startActivity(getActivity(), userinfo.getUid());
//                    break;
//
//                case 4: //贵族
//                    MyNobleActivity.startActivity(getActivity());
//                    break;
//
//                case 5: //直播收入
//                    LiveProfitActivity.startActivity(getActivity());
//                    break;
//
//                case 6: //偶像管理
//                    if (userinfo.isFamilyManager()) {
//                        MyAncListActivity.startActivity(getActivity(), userinfo.getUid());
//                    } else {
//                        ZblbActivity.startActivity(getActivity(), userinfo.getUid());
//                    }
//                    break;
//
//                case 7: //我的道具
//                    MyPronActivity.startActivity(requireActivity());
//                    break;
//
//                case 8: //关于
//                    IntentUtils.toBrowser(requireActivity(), AppConfig.getLandingPage());
//                    break;
//
//                case 9: //系统设置
//                    SettingActivity.startActivity(getActivity());
//                    break;
//            }
//        }
//    }

    @Override
    public void onClickView(View view) {
        if (ClickUtil.isClickWithShortTime(view.getId(),800)) return;
        switch (view.getId()) {
            case R.id.mine_set:
                SettingActivity.startActivity(getActivity());
                break ;
            case R.id.iv_geren: //个人信息
                EditUserInfoActivity.startActivity(requireActivity(), userinfo.getPhone());
                break;
            case R.id.mine_message: //我的消息
                MessageActivity.startActivity(requireActivity());
                break;
            case R.id.ll_zhanghuan:
                MyBalanceActivity.startActivity(requireActivity());
                break;
            case R.id.iv_headimg: //点击头像
                UserDetailActivity.startActivity(requireActivity(), userinfo.getUid());
                break;
            case R.id.iv_share: //分享
                ShareActivity.startActivity(requireActivity());
                break;
            case R.id.layout_circle: //动态
                break;
            case R.id.layout_follow: //我的关注
                MyFollowActivity.startActivity(requireActivity());
                break;
            case R.id.layout_fans: //我的粉丝
                MyFansActivity.startActivity(requireActivity());
                break;
            case R.id.tv_copyid: //复制用户id
                ClipboardUtils.copyText(userinfo.getUid() + "");
                ToastUtils.showShort(getStringWithoutContext(R.string.userCopy));
                break;
            case R.id.ll_recharge: //充值
                RechargeActivity.startActivity(requireActivity());
                break;
            case R.id.ll_moneyout: //提现
                if(TextUtils.isEmpty(DataCenter.getInstance().getUserInfo().getUser().getPhone()))
                {
                    DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), DialogGoBindPhoneOnWithdrawal.getInstance(this));
                    return;
                }
                WithdrawalActivity.startActivity(requireActivity());
                break;
            case R.id.ll_shop: //商城
                ShopActivity.startActivity(requireActivity());
                break;
            case R.id.ll_vip: //贵族
                NobleActivity.startActivity(requireActivity());
                break;
            case R.id.llLinkedPhone:
                if(TextUtils.isEmpty(DataCenter.getInstance().getUserInfo().getUser().getPhone()))
                {
                    this.startActivityForResult(new Intent(getContext(),PhoneBindingActivity.class), ConstantValue.REQUEST_CODE1);
                }
                break;
//            case R.id.btn_yjzh: //一键回收
//                showLoadingDialog();
//                doBackAllGameCoinApi();
//                break;
        }
    }
}

