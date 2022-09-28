package com.live.fox.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.google.android.material.imageview.ShapeableImageView;
import com.live.fox.AppConfig;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.MainActivity;
import com.live.fox.R;
import com.live.fox.adapter.columnlistadapter.ColumnListAdapter;
import com.live.fox.adapter.columnlistadapter.ColumnListBean;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.db.DataBase;
import com.live.fox.entity.Advert;
import com.live.fox.entity.Letter;
import com.live.fox.entity.LetterList;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.User;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_User;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.ui.mine.setting.SettingActivity;
import com.live.fox.ui.mine.moneyout.MoneyOutActivity;
import com.live.fox.ui.mine.noble.MyNobleActivity;
import com.live.fox.ui.mine.noble.NobleActivity;
import com.live.fox.utils.ActivityUtils;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 用户中心
 */
public class MineFragment extends BaseFragment implements ColumnListAdapter.OnItemClickListener,
        AppIMManager.OnMessageReceivedListener, View.OnClickListener {

    private NestedScrollView scrollView;
    private RecyclerView rv;
    private ShapeableImageView ivHeadimg;
    private TextView tvNickname;
    private TextView tvSex;
    private TextView tvIdnum;
    private TextView tvCirclenum;
    private TextView tvFollownum;
    private TextView tvFansnum;
    private LinearLayout layout_id;
    private TextView balanceMoneyTv;
    private RelativeLayout iv_rightdes;
    private ConvenientBanner<Advert> convenientBanner;

    private User userinfo;
    ColumnListAdapter adapter;
    List<ColumnListBean> dataList = new ArrayList<>();
    List<ColumnListBean> userAssetItemData = new ArrayList<>();  //用户资产数据
    String headUrl = "";
    private boolean isNoble;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.mine_fragment, container, false);
            EventBus.getDefault().register(this);
            setView(rootView);
        }
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) return;

        StatusBarUtil.setStatusBarFulAlpha(requireActivity());
        BarUtils.setStatusBarVisibility(requireActivity(), true);
        BarUtils.setStatusBarLightMode(requireActivity(), false);
    }

    public void setView(View bindSource) {
        scrollView = bindSource.findViewById(R.id.layout_root);
        rv = bindSource.findViewById(R.id.mine_click_item);
        ivHeadimg = bindSource.findViewById(R.id.iv_headimg);
        tvNickname = bindSource.findViewById(R.id.tv_nickname);
        tvSex = bindSource.findViewById(R.id.tv_sex);
        tvIdnum = bindSource.findViewById(R.id.tv_idnum);
        tvCirclenum = bindSource.findViewById(R.id.tv_circlenum);
        tvFollownum = bindSource.findViewById(R.id.tv_follownum);
        tvFansnum = bindSource.findViewById(R.id.tv_fansnum);
        layout_id = bindSource.findViewById(R.id.layout_id);
        balanceMoneyTv = bindSource.findViewById(R.id.balanceMoneyTv);
        iv_rightdes = bindSource.findViewById(R.id.iv_rightdes);
        convenientBanner = bindSource.findViewById(R.id.banner);

        bindSource.findViewById(R.id.iv_geren).setOnClickListener(this);
        bindSource.findViewById(R.id.mine_message).setOnClickListener(this);
        bindSource.findViewById(R.id.ll_zhanghuan).setOnClickListener(this);
        bindSource.findViewById(R.id.iv_headimg).setOnClickListener(this);
        bindSource.findViewById(R.id.iv_share).setOnClickListener(this);
        bindSource.findViewById(R.id.layout_circle).setOnClickListener(this);
        bindSource.findViewById(R.id.layout_follow).setOnClickListener(this);
        bindSource.findViewById(R.id.layout_fans).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_copyid).setOnClickListener(this);
        bindSource.findViewById(R.id.ll_recharge).setOnClickListener(this);
        bindSource.findViewById(R.id.ll_moneyout).setOnClickListener(this);
        bindSource.findViewById(R.id.ll_shop).setOnClickListener(this);
        bindSource.findViewById(R.id.ll_vip).setOnClickListener(this);
        bindSource.findViewById(R.id.mine_set).setOnClickListener(this);
      //  bindSource.findViewById(R.id.btn_yjzh).setOnClickListener(this);

        refreshUserinfo(false);
        AppIMManager.ins().addMessageListener(MineFragment.class, this);
        setRecycleView();
    }

    /**
     * 解决scrollView与recycleview滑动冲突的问题
     */
    private void setRecycleView() {
        rv.setAdapter(adapter = new ColumnListAdapter(requireActivity(), dataList));
        adapter.setOnItemClickListener(this);

        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.requestFocus();

        rv.setFocusable(false);
        rv.setNestedScrollingEnabled(false);

        rv.setLayoutManager(new LinearLayoutManager(requireActivity()));
        setData();
    }

    private void setData() {
        ArrayList<Integer> mBadgeList = DataCenter.getInstance().getUserInfo().getUser().getBadgeList();
        if (mBadgeList != null) {
            if (mBadgeList.contains(6)) {
                isNoble = true;
            } else if (mBadgeList.contains(7)) {
                isNoble = true;
            } else if (mBadgeList.contains(8)) {
                isNoble = true;
            } else if (mBadgeList.contains(9)) {
                isNoble = true;
            } else if (mBadgeList.contains(10)) {
                isNoble = true;
            }
        }

        userAssetItemData.clear();
        ColumnListBean balance = new ColumnListBean(41, R.drawable.myyue_ic, getString(R.string.accountBalance));
        balance.setType(1);  //账户余额
        userAssetItemData.add(balance);

        ColumnListBean transaction = new ColumnListBean(41, R.drawable.souzhimingxi, getString(R.string.transaction_title));
        transaction.setType(2);  //交易记录
        userAssetItemData.add(transaction);

        ColumnListBean report = new ColumnListBean(41, R.drawable.youxijulu, getString(R.string.mine_item_game));
        report.setType(3);   //游戏记录
        userAssetItemData.add(report);

        if (isNoble) {
            ColumnListBean nobility = new ColumnListBean(41, R.drawable.ic_my_noble, getString(R.string.myNovel));
            nobility.setType(4); //贵族
            userAssetItemData.add(nobility);
        }

        if (Constant.OS != 1) { //安卓用户端屏蔽这两个栏目
            ColumnListBean earnings = new ColumnListBean(41, R.drawable.liveprofit_ic, getString(R.string.liveProfit));
            earnings.setType(5); //直播收入
            userAssetItemData.add(earnings);
            ColumnListBean idol = new ColumnListBean(41, R.drawable.zbmx_ic, getString(R.string.anchorDetails));
            idol.setType(6); //偶像管理
            userAssetItemData.add(idol);
        }

        ColumnListBean props = new ColumnListBean(41, R.drawable.myporp_ic, getString(R.string.myProps));
        props.setType(7); //我的道具
        userAssetItemData.add(props);

        ColumnListBean about = new ColumnListBean(41, R.drawable.about_ic, getString(R.string.about) + " " +
                getString(R.string.app_name).replace("IDOL", ""));
        about.setType(8); //关于
        userAssetItemData.add(about);

        ColumnListBean appSetting = new ColumnListBean(41, R.drawable.setting_ic, getString(R.string.system_setting));
        appSetting.setType(9);
        userAssetItemData.add(appSetting);

        dataList.clear();
        dataList.addAll(ColumnListBean.convertToOneList(userAssetItemData));
        adapter.notifyDataSetChanged();
    }

    public void refreshUserinfo(boolean flag) {
        userinfo = DataCenter.getInstance().getUserInfo().getUser();

        if (!userinfo.getAvatar().equals(headUrl)) {
            headUrl = userinfo.getAvatar();
            GlideUtils.loadCircleOnePxRingImage(requireActivity(), userinfo.getAvatar(),
                    Color.parseColor("#979797"),
                    R.color.transparent, R.drawable.img_default, ivHeadimg);
        }

        balanceMoneyTv.setText(RegexUtils.westMoney(userinfo.getGoldCoin()));
        tvNickname.setText(userinfo.getNickname());
        tvSex.setText(ChatSpanUtils.ins().getUserInfoSpan(userinfo, requireActivity()));
        String format = String.format(getString(R.string.colon_number), getString(R.string.identity_id), userinfo.getUid());
        tvIdnum.setText(format);
        tvCirclenum.setText("0");
        tvFollownum.setText(String.valueOf(userinfo.getFollows()));
        tvFansnum.setText(String.valueOf(userinfo.getFans()));
        tvNickname.setVisibility(View.VISIBLE);
        layout_id.setVisibility(View.VISIBLE);

        if (flag) {
            iv_rightdes.setVisibility(View.GONE);
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
                    balanceMoneyTv.setText(RegexUtils.westMoney(goldCoin));
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * 一键回收所有金币
     */
    public void doBackAllGameCoinApi() {
        Api_User.ins().backAllGameCoin(userinfo.getUid(), new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0 && msg.equals("success")) {
                    showToastTip(true, getString(R.string.tab_change_success));
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
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
            iv_rightdes.setVisibility(View.VISIBLE);
        }

        Api_User.ins().getLetterList(lastLetter == null ? 0 : lastLetter.getLetterId(), new JsonCallback<List<Letter>>() {//
            @Override
            public void onSuccess(int code, String msg, List<Letter> data) {
                hideLoadingDialog();
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
                            iv_rightdes.setVisibility(View.VISIBLE);
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
            iv_rightdes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
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
                showToastTip(true, getString(R.string.userCopy));
                break;
            case R.id.ll_recharge: //充值
                RechargeActivity.startActivity(requireActivity());
                break;
            case R.id.ll_moneyout: //提现
                MoneyOutActivity.startActivity(requireActivity());
                break;
            case R.id.ll_shop: //商城
                ShopActivity.startActivity(requireActivity());
                break;
            case R.id.ll_vip: //贵族
                NobleActivity.startActivity(requireActivity());
                break;
//            case R.id.btn_yjzh: //一键回收
//                showLoadingDialog();
//                doBackAllGameCoinApi();
//                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getTag() == null) return;
        if (ClickUtil.isFastDoubleClick()) return;
        ColumnListBean columnListBean = userAssetItemData.get(position);
        if (columnListBean != null) {
            switch (columnListBean.getType()) {
                case 1: //我的余额
                    MyBalanceActivity.startActivity(requireActivity());
                    break;

                case 2: //交易记录 资产记录 http://8.210.80.72:8103/swagger-ui.html#/user-controller
                    TransactionActivity.launch(requireActivity());
                    break;

                case 3://游戏记录
                    MyGameRecordActivity.startActivity(getActivity(), userinfo.getUid());
                    break;

                case 4: //贵族
                    MyNobleActivity.startActivity(getActivity());
                    break;

                case 5: //直播收入
                    LiveProfitActivity.startActivity(getActivity());
                    break;

                case 6: //偶像管理
                    if (userinfo.isFamilyManager()) {
                        MyAncListActivity.startActivity(getActivity(), userinfo.getUid());
                    } else {
                        ZblbActivity.startActivity(getActivity(), userinfo.getUid());
                    }
                    break;

                case 7: //我的道具
                    MyPronActivity.startActivity(requireActivity());
                    break;

                case 8: //关于
                    IntentUtils.toBrowser(requireActivity(), AppConfig.getLandingPage());
                    break;

                case 9: //系统设置
                    SettingActivity.startActivity(getActivity());
                    break;
            }
        }
    }
}

