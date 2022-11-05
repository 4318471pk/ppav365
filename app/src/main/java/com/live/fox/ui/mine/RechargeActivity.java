package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.live.fox.AppConfig;
import com.live.fox.AppIMManager;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.ChargeAdapter;
import com.live.fox.adapter.MoneyAdapter;
import com.live.fox.adapter.PayNameAdapter;
import com.live.fox.adapter.PayWayAdapter;
import com.live.fox.adapter.RechargeChannelAdapter;
import com.live.fox.adapter.SupportBankAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.AgentRechargeDialog;
import com.live.fox.dialog.AppTipDialog;
import com.live.fox.dialog.CommonDialog;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.entity.Advert;
import com.live.fox.entity.BankInfo;
import com.live.fox.entity.ChargeCoinBean;
import com.live.fox.entity.DiamondListBean;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.entity.RechargeChannel;
import com.live.fox.entity.RecharegPrice;
import com.live.fox.entity.RechargeTypeBean;
import com.live.fox.entity.RechargeTypeListBean;
import com.live.fox.entity.SupportBankEntity;
import com.live.fox.entity.User;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.entity.response.AgentInfoVO;
import com.live.fox.entity.response.BankRechargeVO;
import com.live.fox.helper.SimpleTextWatcher;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Order;
import com.live.fox.server.Api_Pay;
import com.live.fox.server.BaseApi;
import com.live.fox.ui.h5.H5Activity;
import com.live.fox.ui.h5.WxPayActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.ClipboardUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.MoneyTextWatcher;
import com.live.fox.utils.QRCodeUtil;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.view.EditTextMoney;
import com.marquee.dingrui.marqueeviewlib.MarqueeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 充值
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener,
        AppIMManager.OnMessageReceivedListener {

    private TextView tvMymoney;
    private RecyclerView rvChannel;
    private RecyclerView rvMoney;
    private ViewStub bankUsdt;
    private ViewStub ggk;
    private ConstraintLayout llRecharge;
    private EditText etMoneya;
    private MarqueeView adRoundTextView;
    private RelativeLayout reSupportBank;
    private RecyclerView supportBankList;
    private MoneyAdapter mMoneyAdapter;
    private View uninPayView;
    private View ggkView;
    private EditTextMoney backMoney;
    private EditText transferName;
    private EditText transferAccount;
    private EditText backupInfo;
    private TextView protocolTitle;
    private GridLayoutManager mGridLayoutManager;
    private TextView usdtAddress;
    private ImageView usdtQr;
    private EditText usdtEdit;
    private TextView usdtArriveTime;
    private TextView exchangeRate;
    private EditText payAddress;
    private TextView payNumber;
    private TextView attention;
    private RoundTextView rtGift;
    private TextView usdtDeposit;

    private TextView tvBalanca;
    private TextView tvDiamond;

    private LinearLayout layoutMoney;
    private TextView tvCharge;
    private ImageView ivMoney;
    private LinearLayout layoutDiamond;
    private TextView tvExDiamond;
    private ImageView ivDiamond;
    private GridView gvCharge;
    private TextView tvService;
    private View mView;
    private LinearLayout layoutConfirm;

    private LinearLayout layoutGold;
    private LinearLayout layoutBot;
    private TextView tvPayWay;

    ChargeAdapter chargeMoneyAdapter;
    ChargeAdapter chargeDiamondAdapter;
    List<DiamondListBean> chargeMoneyBeans = new ArrayList<>();
    List<DiamondListBean> chargeDiamondBeans = new ArrayList<>();

    RecyclerView rcPayName;
    RecyclerView rcPayWay;
    PayNameAdapter payNameAdapter;
    PayWayAdapter payWayAdapter;
    List<RechargeTypeBean> payNameList = new ArrayList<>();
    List<RechargeTypeListBean> payWayList = new ArrayList<>();
    HashMap<Integer, List<RechargeTypeListBean>> payWayMap = new HashMap<>();//


    BaseQuickAdapter<BankInfo, BaseViewHolder> bankAdapter;

    private RechargeChannelAdapter channelAdapter;
    private SupportBankAdapter supportBankAdapter;

    private final List<RechargeChannel> dataList = new ArrayList<>();  //充值渠道数据
    List<RechargeChannel> vipChannelList = new ArrayList<>();
    private final List<BankInfo> bankInfoList = new ArrayList<>();
    private int channelPosition = 0; //充值渠道选择位置
    int bankSelPos = 0;
    int moneySelPos = 0;
    private int supportPosition = 0; //银行或者USDT
    User user;
    private SupportBankEntity supportBank = new SupportBankEntity();
    private List<BankInfo> selectUsdtProtocol;
    private String rate;
    private int channelType;
    private CommonDialog commonDialog;
    private MoneyTextWatcher mMoneyWatcher;
    private MoneyTextWatcher mMoneyaWatcher;
    private String usdtInput = "0";
    private int nowPayId = 0; //现在选择的支付通道id

    private boolean isChargeMoney = true; //true:点击充值  false:点击兑换钻石

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, RechargeActivity.class);
        context.startActivity(i);
    }

    public static void startActivity(Context context, boolean isChargeMoney) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, RechargeActivity.class);
        i.putExtra("isChargeMoney", isChargeMoney);
        context.startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initView();
    }

    private void initView() {
        isChargeMoney = getIntent().getBooleanExtra("isChargeMoney", isChargeMoney);
        commonDialog = new CommonDialog();
       // tvMymoney = findViewById(R.id.tv_mymoney);
        rvChannel = findViewById(R.id.rv_channel);
        rvMoney = findViewById(R.id.rv_money);
        bankUsdt = findViewById(R.id.recharge_view_stub_bank_ustd);
        ggk = findViewById(R.id.ggk);
        llRecharge = findViewById(R.id.llRecharge);
        etMoneya = findViewById(R.id.recharge_input_edit);
        adRoundTextView = findViewById(R.id.adRoundTextView);
        reSupportBank = findViewById(R.id.reSupportBank);
        supportBankList = findViewById(R.id.rv_supportBank);

        mView = findViewById(R.id.mView);
        tvBalanca = findViewById(R.id.balance);
        tvDiamond = findViewById(R.id.diamond);
        layoutMoney = findViewById(R.id.layout_charge);
        tvCharge = findViewById(R.id.charge);
        ivMoney = findViewById(R.id.img_money);
        layoutDiamond= findViewById(R.id.layout_exchange_diamond);
        tvExDiamond = findViewById(R.id.exchange_diamond);
        ivDiamond = findViewById(R.id.img_diamond);
        gvCharge = findViewById(R.id.gv_charge);
        tvService = findViewById(R.id.tv_service);
        layoutConfirm = findViewById(R.id.confirm);
        layoutGold = findViewById(R.id.layoutGold);
        tvPayWay = findViewById(R.id.tvPayWay);
        layoutBot = findViewById(R.id.layoutBot);

        setTvService(tvService);

        chargeMoneyAdapter = new ChargeAdapter(this,chargeMoneyBeans, true);
        chargeDiamondAdapter = new ChargeAdapter(this,chargeDiamondBeans, false);
        gvCharge.setAdapter(chargeMoneyAdapter);
        gvCharge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isChargeMoney) {
                    for (int i = 0; i < chargeMoneyBeans.size(); i++) {
                        if (position == i) {
                            chargeMoneyBeans.get(position).setSelect(true);
                        } else {
                            chargeMoneyBeans.get(i).setSelect(false);
                        }
                    }
                    chargeMoneyAdapter.notifyDataSetChanged();
                } else {
//                    for (int i = 0; i < chargeDiamondBeans.size(); i++) {
//                        if (position == i) {
//                            chargeDiamondBeans.get(position).setSelect(true);
//                        } else {
//                            chargeDiamondBeans.get(i).setSelect(false);
//                        }
//                    }
//                    chargeDiamondAdapter.notifyDataSetChanged();
                    if (userAssetsBean.getGold() >= Integer.parseInt(chargeDiamondBeans.get(position).getValue())) { //金币兑换钻石
                        String s =  String.format(getString(R.string.confirm_exchange_diamond),
                                String.valueOf(chargeDiamondBeans.get(position).getValue()), ""+chargeDiamondBeans.get(position).getCode());
                        showMoneyDialog(chargeDiamondBeans.get(position).getValue(),s, false);
                    } else { //金币不足
                        showMoneyDialog(chargeDiamondBeans.get(position).getValue(),getString(R.string.no_money_go_to_charge), true);
                    }
                }
            }
        });

        rcPayName = findViewById(R.id.rcPayName);
        rcPayWay = findViewById(R.id.rcPayWay);

        payNameAdapter = new PayNameAdapter(payNameList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcPayName.setLayoutManager(layoutManager);
        rcPayName.setAdapter(payNameAdapter);
        payNameAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int temp = 0;
                for (int i = 0; i < payNameList.size() ; i++) {
                    if (payNameList.get(i).isSelect()) {
                        temp = i;
                        break;
                    }
                }
                if (temp != position) {
                    payNameList.get(temp).setSelect(false);
                    payNameList.get(position).setSelect(true);
                    payNameAdapter.notifyItemChanged(temp);
                    payNameAdapter.notifyItemChanged(position);
                    getChannelType(payNameList.get(position).getType());
                    nowPayId = payNameList.get(position).getType();
                }
            }
        });
        
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcPayWay.setLayoutManager(layoutManager2);
        payWayAdapter =new PayWayAdapter(payWayList);
        rcPayWay.setAdapter(payWayAdapter);

        payWayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int temp = 0;
                for (int i = 0; i < payWayMap.get(nowPayId).size() ; i++) {
                    if (payWayMap.get(nowPayId).get(i).isSelect()) {
                        temp = i;
                        break;
                    }
                }
                if (temp != position) {
                    payWayMap.get(nowPayId).get(temp).setSelect(false);
                    payWayMap.get(nowPayId).get(position).setSelect(true);
                    payWayAdapter.notifyItemChanged(temp);
                    payWayAdapter.notifyItemChanged(position);
                    //getChannelType(payNameList.get(position).getId());
                }
            }
        });


        layoutMoney.setOnClickListener(this);
        layoutDiamond.setOnClickListener(this);
        layoutConfirm.setOnClickListener(this);
        findViewById(R.id.tvaRecharge).setOnClickListener(this);

        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
//        setHead(getString(R.string.charge), true, true);
//        setRightImgId(R.drawable.kefua);
//
//        getIvRight().setOnClickListener(view -> ServicesActivity.startActivity(RechargeActivity.this));
        user = DataCenter.getInstance().getUserInfo().getUser();
//        if (user != null) {
//            tvMymoney.setText(RegexUtils.westMoney(user.getGoldCoin()));
//        }

        protocolTitle = findViewById(R.id.recharge_protocol);

        TextView money = findViewById(R.id.recharge_input_symbol);
        money.setText(AppConfig.getCurrencySymbol());

        TextView exchangeRatio = findViewById(R.id.recharge_exchange_ratio);
        exchangeRatio.setText(String.format(getString(R.string.chongfuPayStr), AppConfig.getExchangeRatio(), AppConfig.getCurrencySymbol()));

        AppIMManager.ins().addMessageListener(RechargeActivity.class, this);

        changeChargeUi();


        initChannelRv();
        initSupportRv();
        initMoneyRv();
        initGongGao();
        showLoadingDialogWithNoBgBlack();
        //doGetVipChannelApi();
        //getUserAsset();
        getChargeCenter();
    }

    private void initGongGao() {
        String content = SPUtils.getInstance("tixianAd").getString("content", "");
        if (!StringUtils.isEmpty(content)) {
            List<Advert> advertList = GsonUtil.getObjects(content, Advert[].class);
            String jsonStr = advertList.get(0).getContent();
            if (jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
                jsonStr = LanguageUtilsEntity.getLanguage(new Gson().fromJson(jsonStr, LanguageUtilsEntity.class));
            }
            adRoundTextView.setContent(jsonStr);
        }
    }

    public void initChannelRv() {
        channelAdapter = new RechargeChannelAdapter();

        rvChannel.setNestedScrollingEnabled(false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvChannel.setLayoutManager(layoutManager);
        rvChannel.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(this, 6)));
        rvChannel.setAdapter(channelAdapter);

        //切换充值渠道
        channelAdapter.setOnItemClickListener((adapter, view, position) -> {
            KeyboardUtils.hideForceSoftInput(this);
            if (position == channelPosition) {
                return;
            }

            dataList.get(channelPosition).setCheck(false);

            RechargeChannel channel = dataList.get(position);
            if (channel.getType() == 7) {//银联
                llRecharge.setVisibility(View.GONE);
            }
            channel.setCheck(true);

            mGridLayoutManager.setSpanCount(3);
            channelPosition = position;
            changePriceByChannel(channelPosition, channel.getType());
            channelAdapter.notifyDataSetChanged();
        });
    }

    public void initSupportRv() {
        supportBankAdapter = new SupportBankAdapter();
        supportBankList.setNestedScrollingEnabled(false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false);
        supportBankList.setLayoutManager(layoutManager);
        supportBankList.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(this, 6)));
        supportBankList.setAdapter(supportBankAdapter);

        supportBankAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (supportPosition == position) {
                return;
            }

            supportBankAdapter.getData().get(supportPosition).setCheck(false);
            supportBankAdapter.getData().get(position).setCheck(true);
            supportPosition = position;
            if (channelType == 29) {
                supportBank = supportBankAdapter.getData().get(position);
                setUsdtDetail(selectUsdtProtocol.get(supportPosition));
            }
            supportBankAdapter.notifyDataSetChanged();
        });
    }

    /**
     * 切换充值方式
     *
     * @param channelSelPos 渠道位置
     * @param type          渠道类型
     */
    public void changePriceByChannel(int channelSelPos, int type) {
        channelType = type;
        moneySelPos = 0;
        GridLayoutManager layoutManager = new GridLayoutManager(this,
                2, GridLayoutManager.VERTICAL, false);
        switch (type) {
            case 29: //USDT
                layoutManager = new GridLayoutManager(this, 3,
                        GridLayoutManager.VERTICAL, false);
                protocolTitle.setText(getString(R.string.recharge_money_pay_channel));
                bankAndUsdt();
                rvMoney.setVisibility(View.GONE);
                initUnionPay(type);
                rate = RegexUtils.westMoney(dataList.get(channelSelPos).getRate());
                supportBankList.setVisibility(View.VISIBLE);
                break;

            case 7:  //银行卡充值
                protocolTitle.setText(getString(R.string.choose_bank));
                supportBankList.setVisibility(View.GONE);
                reSupportBank.setVisibility(View.GONE);
                bankAndUsdt();
                initUnionPay(type);
                break;

            case 19:
                if (uninPayView != null) uninPayView.setVisibility(View.GONE);
                initGgk();
                llRecharge.setVisibility(View.GONE);
                rvMoney.setVisibility(View.VISIBLE);
                break;

            default:
                rvMoney.setVisibility(View.VISIBLE);
                supportBankList.setVisibility(View.VISIBLE);
                layoutManager = new GridLayoutManager(this, 2,
                        GridLayoutManager.VERTICAL, false);
                protocolTitle.setText(getString(R.string.choose_bank));
                if (dataList.get(channelSelPos).getSizeStatus() == 1) {
                    llRecharge.setVisibility(View.VISIBLE);
                    if (mMoneyaWatcher == null) {
                        mMoneyaWatcher = new MoneyTextWatcher(etMoneya);
                        if (needAppend()) {
                            etMoneya.addTextChangedListener(mMoneyaWatcher);
                        }
                    }

                    String format = String.format(getString(R.string.tip_hint_range),
                            RegexUtils.westMoney(dataList.get(channelSelPos).getLowest()),
                            RegexUtils.westMoney(dataList.get(channelSelPos).getHighest()),
                            AppConfig.getCurrencySymbol());

                    etMoneya.setHint(format);
                } else {
                    llRecharge.setVisibility(View.GONE);
                }

                if (uninPayView != null) uninPayView.setVisibility(View.GONE);
                if (ggkView != null) ggkView.setVisibility(View.GONE);
                break;
        }

        if (StringUtils.isEmpty(dataList.get(channelSelPos).getSupportBank())) {
            reSupportBank.setVisibility(View.GONE);
            supportBankList.setVisibility(View.GONE);
        } else {
            if (type != 7 && type != 29) {
                String[] splitString = dataList.get(channelSelPos).getSupportBank().split(",");
                List<SupportBankEntity> bankList = new LinkedList<>();
                for (String name : splitString) {
                    SupportBankEntity bank = new SupportBankEntity();
                    bank.setBank(name);
                    bankList.add(bank);
                }
                bankList.get(0).setCheck(true);
                supportBankAdapter.setNewData(bankList);
            }
        }
        mMoneyAdapter.setMoneySelPos(moneySelPos);
        mMoneyAdapter.setNewData(dataList.get(channelSelPos).getProducts());
        supportBankList.setLayoutManager(layoutManager);
    }

    /**
     * 银行和Usdt
     */
    private void bankAndUsdt() {
        if (ggkView != null) ggkView.setVisibility(View.GONE);
        llRecharge.setVisibility(View.GONE);
    }

    public void initMoneyRv() {
        rvMoney.setNestedScrollingEnabled(false);
        mGridLayoutManager = new GridLayoutManager(RechargeActivity.this,
                3, GridLayoutManager.VERTICAL, false);
        rvMoney.setLayoutManager(mGridLayoutManager);
        rvMoney.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(this, 6)));
        mMoneyAdapter = new MoneyAdapter(new ArrayList<>());
        rvMoney.setAdapter(mMoneyAdapter);

        mMoneyAdapter.setOnItemClickListener((adapter, view, position) -> {
            moneySelPos = position;
            RechargeChannel rechargeItem = dataList.get(channelPosition);
            RecharegPrice recharegPrice = rechargeItem.getProducts().get(position);
            if (rechargeItem.getType() == 6) {//代理充值
                AgentRechargeDialog dialog = new AgentRechargeDialog();
                Bundle bundle = new Bundle();
                bundle.putDouble("goldCoin", recharegPrice.getGoldCoin());
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), AgentRechargeDialog.class.getSimpleName());
                dialog.setBtnClick((agentType, agentInfoVO) -> {
                    dialog.dismiss();
                    showRechargeTipDialog(agentType, agentInfoVO);
                });
            } else if (rechargeItem.getType() == 7) {//银联
                backMoney.setText(RegexUtils.formatNumber(recharegPrice.getUserRmb() / 100));
            } else if (rechargeItem.getType() != 19) {
                String tip = getString(R.string.sureCost) +
                        RegexUtils.westMoney(recharegPrice.getUserRmb() / 100d) +
                        getString(R.string.yuanCharge) + RegexUtils.westMoney(recharegPrice.getGoldCoin()) + getString(R.string.gold);
                DialogFactory.showTwoBtnDialog(RechargeActivity.this, tip,
                        (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                            dialog.dismiss();
                            toRecharge(!StringUtils.isEmpty(dataList.get(channelPosition).getSupportBank()));
                        });
            }  //ggk
            mMoneyAdapter.setMoneySelPos(moneySelPos);
            mMoneyAdapter.notifyDataSetChanged();
        });
    }

    public void showRechargeTipDialog(int agentType, AgentInfoVO agentInfoVO) {
        String title = getString(R.string.substituteReminder);
        String title2 = getString(R.string.paySubmit);
        String content = getString(R.string.thirdStep);
        AppTipDialog tipDialog = AppTipDialog.newInstance(title, title2, content);
        tipDialog.show(getSupportFragmentManager(), "AppTipDialog");
        tipDialog.setBtnSureClick(() -> {
            if (agentType == 1) { //QQ
                if (isQQClientAvailable()) {
                    ClipboardUtils.copyText(agentInfoVO.getQq());
                    openQQApp(agentInfoVO.getQq());
                } else {
                    showToastTip(false, getString(R.string.pinstallQQ));
                }
            } else if (agentType == 2) { //微信
                if (isWeixinAvilible()) {
                    ClipboardUtils.copyText(agentInfoVO.getWechat());
                    showToastTip(true, getString(R.string.copyWechat));
                    new Handler().postDelayed(mRunnable, 2000);
                } else {
                    showToastTip(false, getString(R.string.pInstallWechat));
                }
            }
        });
    }

    private final Runnable mRunnable = () -> {
        Intent intent;
        intent = RechargeActivity.this.getPackageManager()
                .getLaunchIntentForPackage("com.tencent.mm");
        startActivity(intent);
    };

    public List<String> getQQPakName() {
        List<String> qqPakName = new ArrayList<>();
        //QQ
        qqPakName.add("com.tencent.mobileqq");
        //TIM
        qqPakName.add("com.tencent.tim");
        //QQ國際版
        qqPakName.add("com.tencent.mobileqqi");
        //QQ 輕聊版
        qqPakName.add("com.tencent.qqlite");
        //QQ HD
        qqPakName.add("com.tencent.minihd.qq");
        //QQ日本版
        qqPakName.add("com.tencent.qq.kddi");
        return qqPakName;
    }

    public void openQQApp(String qq) {
        List<String> qqPakName = getQQPakName();
        for (String pkg : qqPakName) {
            PackageManager packageManager = RechargeActivity.this.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(pkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qq + "&version=1")));
            return;
        }
    }

    //判断有没有安装微信
    public boolean isWeixinAvilible() {
        final PackageManager packageManager = RechargeActivity.this.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        for (int i = 0; i < pinfo.size(); i++) {
            String pn = pinfo.get(i).packageName;
            if (pn.equals("com.tencent.mm")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断qq是否可用
     */
    public boolean isQQClientAvailable() {
        final PackageManager packageManager = RechargeActivity.this.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            String pn = pinfo.get(i).packageName;
            if (pn.equals("com.tencent.mobileqq")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onIMReceived(int protocol, String msg) {
        try {
            JSONObject message = new JSONObject(msg);
            if (protocol == Constant.MessageProtocol.PROTOCOL_BALANCE_CHANGE) { //12.金币变动消息
                long uid = message.optLong("uid", -1);
                Double goldCoin = message.optDouble("goldCoin", -1);
                if (uid == user.getUid()) {
                    user.setGoldCoin(goldCoin.floatValue());
                    DataCenter.getInstance().getUserInfo().updateUser(user);
                  //  tvMymoney.setText(RegexUtils.westMoney(goldCoin));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 充值
     */
    public void toRecharge(boolean flag) {
        if (dataList == null || dataList.size() == 0) {
            return;
        }

        showLoadingDialog();

        RechargeChannel recharegChanll = dataList.get(channelPosition);
        String url = recharegChanll.getSubmitUrl();
        String code = recharegChanll.getProducts().get(moneySelPos).getCode();
        if (!flag) {
            supportBank = null;
        }

        Api_Pay.ins().payFour(url, code, supportBank.getBank(), new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String result) {
                hideLoadingDialog();
                if (code == 0 && result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String data = jsonObject.optString("payHtml");
                        LogUtils.e("PayHtml", data);
                        //1URL 2HTML 3qrcode 4外部浏览器
                        if (recharegChanll.getCallbackType() == 1) {
                            LogUtils.e("方式一");
                            H5Activity.start(RechargeActivity.this, getString(R.string.money_pay), data, false);
                        } else if (recharegChanll.getCallbackType() == 2) {
                            LogUtils.e("方式二");//isWeixinAvilible
                            H5Activity.start(RechargeActivity.this, getString(R.string.money_pay), data);
                        } else if (recharegChanll.getCallbackType() == 4) {
                            LogUtils.e("方式四");
                            IntentUtils.toBrowser(RechargeActivity.this, data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 支付渠道列表
     */
    public void doGetVipChannelApi() {
        Api_Order.ins().getVipChannel(new JsonCallback<List<RechargeChannel>>() {
            @Override
            public void onSuccess(int code, String msg, List<RechargeChannel> data) {
                if (code == 0) {
                    if (data != null) {
                        vipChannelList.clear();
                        vipChannelList.addAll(data);
                    }
                    doGetPayChannelApi();
                } else {
                    hideLoadingDialog();
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 支付渠道列表
     */
    public void doGetPayChannelApi() {
        Api_Config.ins().getPayChannel(new JsonCallback<List<RechargeChannel>>() {
            @Override
            public void onSuccess(int code, String msg, List<RechargeChannel> data) {
                hideLoadingDialog();
                if (code == 0) {
                    if (data != null && data.size() > 0) {
                        dataList.addAll(data);
                        if (vipChannelList.size() > 0) {
                            dataList.addAll(dataList.size() - 1, vipChannelList);
                        }
                        dataList.get(0).setCheck(true);
                        channelAdapter.setNewData(dataList);
                        changePriceByChannel(0, dataList.get(0).getType());
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppIMManager.ins().removeMessageReceivedListener(RechargeActivity.class);
    }

    String type;
    String serial;
    String pin;

    public void initGgk() {
        if (ggkView == null) {
            ggkView = ggk.inflate();
            EditText etXuleihao = findViewById(R.id.etXuleihao);
            EditText etGgkNum = findViewById(R.id.etGgkNum);
            RadioGroup rgroup = findViewById(R.id.rgroup);
            RoundTextView tvKSure = findViewById(R.id.tvKSure);
            TextView tv_ggk_tip = findViewById(R.id.tv_ggk_tip);
            tv_ggk_tip.setText(dataList.get(channelPosition).getRemark());
            rgroup.setOnCheckedChangeListener((group, checkedId) -> {
                switch (checkedId) {
                    case R.id.ch1:
                        type = "VIETTEL";
                        break;
                    case R.id.ch2:
                        type = "MOBIFONE";
                        break;
                    case R.id.ch3:
                        type = "VINAPHONE";
                        break;
                }
            });

            tvKSure.setOnClickListener(v -> {
                serial = etXuleihao.getText().toString().trim();
                pin = etGgkNum.getText().toString().trim();
                if (StringUtils.isEmpty(serial)) {
                    showToastTip(false, getString(R.string.recharge_money_tip));
                    return;
                }
                if (StringUtils.isEmpty(pin)) {
                    showToastTip(false, getString(R.string.recharge_money_tip_pin));
                    return;
                }
                if (StringUtils.isEmpty(type)) {
                    showToastTip(false, getString(R.string.recharge_money_tip_type));
                    return;
                }
                toRecharge(dataList.get(channelPosition).getSubmitUrl(),
                        dataList.get(channelPosition).getProducts().get(moneySelPos).getUserRmb() + "", type, serial, pin);
            });
        } else {
            ggkView.setVisibility(View.VISIBLE);
        }
    }

    private void toRecharge(String url, String amount, String type, String serial, String pin) {
        Api_Pay.ins().payFoura(url, amount, type, serial, pin, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String result) {
                if (StringUtils.isEmpty(result)) {
                    return;
                }
                ToastUtils.showShort(result);
            }
        });
    }

    public void initUnionPay(int type) {
        if (uninPayView == null) {
            uninPayView = bankUsdt.inflate();
            changePayWay(type);

            TextView tvRecharge = uninPayView.findViewById(R.id.tvRecharge);
            TextView money = uninPayView.findViewById(R.id.tvSysmol);
            money.setText(AppConfig.getCurrencySymbol());
            TextView tvDes = uninPayView.findViewById(R.id.tvDes);
            rtGift = uninPayView.findViewById(R.id.rtGift);
            TextView tvLowHigh = uninPayView.findViewById(R.id.tvLowHigh);
            backMoney = uninPayView.findViewById(R.id.recharge_back_money);
            transferName = uninPayView.findViewById(R.id.recharge_transfer_money_name);
            transferAccount = uninPayView.findViewById(R.id.recharge_transfer_account);
            backupInfo = uninPayView.findViewById(R.id.recharge_transfer_backup);

            setAward("0");

            tvRecharge.setOnClickListener(this);
            long highest = dataList.get(channelPosition).getHighest();
            if (mMoneyWatcher == null) {
                mMoneyWatcher = new MoneyTextWatcher(backMoney, this::setAward);
                mMoneyWatcher.setMaxMoney(String.valueOf(highest));
                backMoney.addTextChangedListener(mMoneyWatcher);
            }

            String str = String.format(getString(R.string.recharge_money_scope),
                    RegexUtils.westMoney(dataList.get(channelPosition).getLowest()),
                    RegexUtils.westMoney(dataList.get(channelPosition).getHighest()));

            tvLowHigh.setText(str);
            String append = "";
            if (!AppConfig.isThLive()) {
                append = getString(R.string.recharge_step_detail_tip);
            }
            String format = String.format(getString(R.string.recharge_step_detail), append);
            String html = "<h3>" + getString(R.string.recharge_step_title) + "</h3>\n" + format;
            tvDes.setText(Html.fromHtml(html));
            formatBankCard();
        } else {
            uninPayView.setVisibility(View.VISIBLE);
            changePayWay(type);
        }
      //  doBankInfo(type);
    }

    /**
     * 设置奖励
     */
    private void setAward(String award) {
        double reward = dataList.get(channelPosition).getReward();
        if (TextUtils.isEmpty(award)) {
            award = "0";
        }

        award = RegexUtils.westMoney((reward / 100) * Double.parseDouble(award.replace(",", "")));

        String strGift = getString(R.string.rechargeReward) + (int) reward +
                getString(R.string.nijianghuode) + award + AppConfig.getCurrencySymbol();
        rtGift.setText(strGift);
    }

    /**
     * 格式化银行卡
     */
    private void formatBankCard() {
        RecyclerView rvBank = uninPayView.findViewById(R.id.rvBank);
        rvBank.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RechargeActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvBank.setLayoutManager(layoutManager);
        rvBank.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(this, 6)));
        rvBank.setAdapter(bankAdapter = new BaseQuickAdapter(R.layout.item_bank, new ArrayList<BankInfo>()) {

            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                BankInfo bankInfo = (BankInfo) item;
                if (bankSelPos == helper.getLayoutPosition()) {
                    helper.getView(R.id.rlBankInfo).setBackgroundResource(
                            R.drawable.shape_white_stroke_red_corners_10);
                    helper.setText(R.id.tvCardNo,
                            Html.fromHtml(getString(R.string.bank_card_number) +
                                    "：<font><b>" + bankInfo.getCardNo() + "</b></font>"));
                    helper.setText(R.id.tvBankName, Html.fromHtml(
                            getString(R.string.kaihuBank) + "：<font><b>" +
                                    bankInfo.getBankName() + "</b></font>"));
                    if (!AppConfig.isThLive()) {
                        helper.setText(R.id.tvUid, Html.fromHtml(
                                getString(R.string.remittance_message) + "：<font><b>" +
                                        user.getUid() + "</b></font>"));
                    }
                    helper.setText(R.id.tvRecieptName, Html.fromHtml(
                            getString(R.string.beneficiaryName) +
                                    "：<font><b>" + bankInfo.getTrueName() + "</b></font>"));
                    helper.setText(R.id.tvBankFullName,
                            Html.fromHtml(getString(R.string.account_opening_bank)
                                    + "：" + bankInfo.getBankSub()));
                } else {
                    helper.getView(R.id.rlBankInfo).setBackgroundResource(
                            R.drawable.shape_white_stroke_gray_corners_10);
                    helper.setText(R.id.tvCardNo, getString(R.string.bank_card_number) + "：******");
                    helper.setText(R.id.tvBankName, getString(R.string.kaihuBank) + "：******");
                    if (!AppConfig.isThLive()) {
                        helper.setText(R.id.tvUid, Html.fromHtml(getString(R.string.remittance_message) + "：******"));
                    }
                    helper.setText(R.id.tvBankFullName, getString(R.string.account_opening_bank) + "：******");
                    helper.setText(R.id.tvRecieptName, getString(R.string.beneficiaryName) + "：******");
                }
                helper.addOnClickListener(R.id.tvCopy1);
                helper.addOnClickListener(R.id.tvCopy2);
                helper.addOnClickListener(R.id.tvCopy3);
                helper.addOnClickListener(R.id.tvCopy4);
            }
        });

        bankAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (bankSelPos == position)
                return;
            bankSelPos = position;
            bankAdapter.notifyDataSetChanged();
        });

        bankAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (ClickUtil.isFastDoubleClick()) return;
            BankInfo bankInfo = (BankInfo) adapter.getData().get(position);
            if (view.getId() == R.id.tvCopy1) {
                if (position == bankSelPos) {
                    ClipboardUtils.copyText(bankInfo.getCardNo());
                    showToastTip(true, getString(R.string.cancel));
                } else {
                    showToastTip(false, String.format(getString(R.string.copy_select), getString(R.string.bank_card_number)));
                }
            } else if (view.getId() == R.id.tvCopy2) {
                if (position == bankSelPos) {
                    ClipboardUtils.copyText(bankInfo.getTrueName());
                    showToastTip(true, getString(R.string.cancel));
                } else {
                    showToastTip(false, String.format(getString(R.string.copy_select), getString(R.string.receipt_name)));
                }
            } else if (view.getId() == R.id.tvCopy3) {
                if (position == bankSelPos) {
                    ClipboardUtils.copyText(bankInfo.getBankName());
                    showToastTip(true, getString(R.string.cancel));
                } else {
                    showToastTip(false, String.format(getString(R.string.copy_select), getString(R.string.Account_back_name)));
                }
            } else if (view.getId() == R.id.tvCopy4) {
                if (position == bankSelPos) {
                    ClipboardUtils.copyText(user.getUid() + "");
                    showToastTip(true, getString(R.string.cancel));
                } else {
                    showToastTip(false, String.format(getString(R.string.copy_select), getString(R.string.Account_back_name)));
                }
            }
        });
    }

    private void changePayWay(int type) {
        Group bank = findViewById(R.id.recharge_bank);
        Group bankThi = findViewById(R.id.recharge_bank_thi);
        Group usdt = findViewById(R.id.recharge_usdt);

        if (type == 29) {  //Usdt 充值方式
            bank.setVisibility(View.GONE);
            usdt.setVisibility(View.VISIBLE);
            if (usdtAddress == null) {
                usdtAddress = findViewById(R.id.recharge_usdt_address);
                usdtQr = findViewById(R.id.recharge_usdt_address_qr);
                usdtEdit = findViewById(R.id.recharge_usdt_address_order_edit);
                usdtArriveTime = findViewById(R.id.recharge_usdt_about);
                exchangeRate = findViewById(R.id.recharge_usdt_exchange_rate);
                payAddress = findViewById(R.id.recharge_usdt_pay_address_detail);
                payNumber = findViewById(R.id.recharge_usdt_pay_order_number);
                attention = findViewById(R.id.recharge_usdt_attention_detail);
                usdtDeposit = findViewById(R.id.recharge_usdt_deposit);
                findViewById(R.id.recharge_usdt_address_copy).setOnClickListener(view -> {
                    ClipboardUtils.copyText(usdtAddress.getText().toString());
                    showToastTip(true, getString(R.string.copySuccess));
                });
            }
        } else {
            bank.setVisibility(View.VISIBLE);
            usdt.setVisibility(View.GONE);
            if (AppConfig.isThLive()) {
                bankThi.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        //super.onClick(v);
        switch (v.getId()){
            case R.id.layout_charge:
                if (!isChargeMoney) {
                    isChargeMoney = true;
                    changeChargeUi();
                }
                break;
            case R.id.layout_exchange_diamond:
                if (isChargeMoney) {
                    isChargeMoney = false;
                    changeChargeUi();
                }
                break;
            case R.id.confirm:
                if (isChargeMoney) {
                   //setPopCharge();
                }
                break;
            case R.id.iv_head_left:
                finish();
                break;
            case R.id.tvRecharge: //转账充值
                if (channelType == 29) {
                    usdtRecharge();
                } else {
                    bankRecharge();
                }
                break;
            case R.id.tvaRecharge:
                dealEditZero(etMoneya);
                if (dataList.get(channelPosition).getType() == 6) {//代理充值
                    AgentRechargeDialog dialog = new AgentRechargeDialog();
                    double reward = dataList.get(channelPosition).getReward();
                    long tip;
                    String moneyString = etMoneya.getText().toString();
                    if (StringUtils.isEmpty(moneyString)) {
                        showToastTip(false, getString(R.string.recharge_usdt_empty));
                        return;
                    }
                    long money = Long.parseLong(moneyString);
                    if (money < dataList.get(channelPosition).getLowest() || money > dataList.get(channelPosition).getHighest()) {
                        showToastTip(false, getString(R.string.recharge_usdt_deposit_tip));
                        return;
                    }
                    if (reward > 0) {
                        tip = new BigDecimal(money * (10 + 0.1 * reward)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                    } else {
                        tip = money * 10;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putLong("goldCoin", tip);
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), AgentRechargeDialog.class.getSimpleName());
                    dialog.setBtnClick((agentType, agentInfoVO) -> {
                        dialog.dismiss();
                        showRechargeTipDialog(agentType, agentInfoVO);
                    });
                } else {
                    double reward = dataList.get(channelPosition).getReward();
                    String tip;
                    String moneyString = etMoneya.getText().toString().replace(",", "");
                    if (StringUtils.isEmpty(moneyString)) {
                        showToastTip(false, getString(R.string.recharge_usdt_empty));
                        return;
                    }
                    long money = Long.parseLong(moneyString);
                    if (money < dataList.get(channelPosition).getLowest()
                            || money > dataList.get(channelPosition).getHighest()) {
                        showToastTip(false, getString(R.string.recharge_usdt_deposit_tip));
                        return;
                    }
                    long sectionGold;
                    if (reward > 0) {
                        sectionGold = new BigDecimal(money * (10 + 0.1 * reward))
                                .setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                    } else {
                        sectionGold = money * 10;
                    }
                    tip = getString(R.string.sureCost) + RegexUtils.westMoney(money) +
                            getString(R.string.yuanCharge) +
                            RegexUtils.westMoney(sectionGold) + getString(R.string.gold);
                    DialogFactory.showTwoBtnDialog(RechargeActivity.this, tip, (button,
                                                                                dialog) -> dialog.dismiss(),
                            (button, dialog) -> {
                                dialog.dismiss();
                                toRechargea(dataList.get(channelPosition).getSubmitUrl(), money, sectionGold);
                            });
                }
                break;
        }

    }

    private void changeChargeUi(){
        if (isChargeMoney) {
            layoutMoney.setBackground(this.getResources().getDrawable(R.mipmap.golden_button_left));
            tvCharge.setTextColor(this.getResources().getColor(R.color.color533888));
            ivMoney.setImageDrawable(this.getResources().getDrawable(R.mipmap.rmb_b));
            layoutDiamond.setBackground(this.getResources().getDrawable(R.drawable.shape_ffeab1));

            tvExDiamond.setTextColor(this.getResources().getColor(R.color.colorFFEAB1));
            ivDiamond.setImageDrawable(this.getResources().getDrawable(R.mipmap.diamonds_y));
            gvCharge.setAdapter(chargeMoneyAdapter);
            tvService.setVisibility(View.VISIBLE);
            layoutConfirm.setVisibility(View.VISIBLE);

            layoutGold.setVisibility(View.VISIBLE);
            layoutBot.setBackgroundColor(getResources().getColor(R.color.white));
            tvPayWay.setText(getResources().getString(R.string.choice_pay_money));
        } else {
            layoutMoney.setBackground(this.getResources().getDrawable(R.drawable.shape_ffeab1_2));
            tvCharge.setTextColor(this.getResources().getColor(R.color.colorFFEAB1));
            ivMoney.setImageDrawable(this.getResources().getDrawable(R.mipmap.rmb_y));
            layoutDiamond.setBackground(this.getResources().getDrawable(R.mipmap.golden_button_right));
            tvExDiamond.setTextColor(this.getResources().getColor(R.color.color533888));
            ivDiamond.setImageDrawable(this.getResources().getDrawable(R.mipmap.diamonds_b));
            gvCharge.setAdapter(chargeDiamondAdapter);
            tvService.setVisibility(View.GONE);
            layoutConfirm.setVisibility(View.GONE);
            layoutGold.setVisibility(View.GONE);
            layoutBot.setBackgroundColor(getResources().getColor(R.color.colorF5F1F8));
            tvPayWay.setText(getResources().getString(R.string.exchange_diamond));
        }
    }

    private void setPopCharge(){
        View popupView = getLayoutInflater().inflate(R.layout.pop_charge,null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);// 设置同意在外点击消失
        TextView tvSer = popupView.findViewById(R.id.tv_service);
        setTvService(tvSer);
        setRootAlpha(0.5f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //在dismiss中恢复透明度
                setRootAlpha(1f);
            }
        });
        popupWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
        popupWindow.showAtLocation(mView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private void setRootAlpha(float al){
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha= al;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    /**
     * 显示弹窗
     */
    private void showMoneyDialog(String yuan, String content, boolean isMoney) {
        commonDialog.setDialogContent(
                getString(R.string.dialogWordsWithSpace),
                content,
                getString(R.string.button_cancel),
                isMoney ? getString(R.string.go_to_charge) : getString(R.string.sure),
                view -> commonDialog.dismiss(),
                view -> {
                    commonDialog.dismiss();
                    if (isMoney) { //充值金币
                        isChargeMoney = true;
                        changeChargeUi();
                    } else { //调用兑换钻石接口
                        exchangeDiamond(yuan);
                    }
                    //doRecharge();
                });
        commonDialog.show(getSupportFragmentManager(), "bank dialog");
    }

    private void exchangeDiamond(String yuan){
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        commonParams.put("goldCoin", yuan);
        Api_Order.ins().getDiamondExchange(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    ToastUtils.showShort(getString(R.string.duiSuccess));
                    getAsset();
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);
    }



    private UserAssetsBean userAssetsBean = null;


    private void getAsset(){
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        Api_Order.ins().getAssets(new JsonCallback<UserAssetsBean>() {
            @Override
            public void onSuccess(int code, String msg, UserAssetsBean data) {
                hideLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    userAssetsBean = data;
                   tvBalanca.setText(data.getGold() + "");
                    tvDiamond.setText(data.getDiamond() + data.getVipDiamond() + "");
                    setMoneyColor(tvBalanca);
                    setMoneyColor(tvDiamond);
                   // setTextViewStyles(tvBalanca);
                    //setTextViewStyles(tvDiamond);
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);
    }

    private void getChargeCenter(){
        showLoadingDialog();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();

        getAsset();

//        Api_Order.ins().getChargeCoin(new JsonCallback<ChargeCoinBean>() {
//            @Override
//            public void onSuccess(int code, String msg, ChargeCoinBean data) {
//                hideLoadingDialog();
//                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
//                    if (data.getRechargeOptionalList() != null && data.getRechargeOptionalList().size() >0) {
//                        chargeMoneyBeans.addAll(data.getRechargeOptionalList());
//                        chargeDiamondBeans.addAll(data.getRechargeOptionalList());
//                        chargeMoneyAdapter.notifyDataSetChanged();
//                        chargeDiamondAdapter.notifyDataSetChanged();
//                    }
//                } else {
//                    ToastUtils.showShort(msg);
//                }
//            }
//        }, commonParams);

        Api_Order.ins().getChargeType(new JsonCallback<List<RechargeTypeBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<RechargeTypeBean> data) {
                hideLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    if (data != null && data.size() > 0) {
                        payNameList.addAll(data);
                        payNameList.get(0).setSelect(true);
                        nowPayId = payNameList.get(0).getType();
                        for (int i = 0; i < data.size(); i++) {
                            getChannelType(payNameList.get(i).getType());
                        }
                        payNameAdapter.notifyDataSetChanged();
                    }

                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);


        Api_Order.ins().getDiamondList(new JsonCallback<List<DiamondListBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DiamondListBean> data) {
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    if (data != null && data.size() >0) {
                        chargeDiamondBeans.addAll(data);
                        chargeDiamondAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);

    }


    private void getChannelType(int type) {
        if(payWayMap.get(type) != null) {
            payWayList.clear();
            payWayList.addAll(payWayMap.get(type));
            payWayAdapter.notifyDataSetChanged();
            for (int i = 0; i< payWayList.size(); i++) {
                if (payWayList.get(i).isSelect()) {
                    setMoneyList(i);
                    break;
                }
            }
        } else {
           // showLoadingDialog();
            Api_Order.ins().getChargeList(new JsonCallback<List<RechargeTypeListBean>>() { //
                @Override
                public void onSuccess(int code, String msg, List<RechargeTypeListBean> data) {
                   // hideLoadingDialog();
                    if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                        if (data != null && data.size() > 0) {
                            payWayMap.put(type, data);
                            payWayList.clear();
                            payWayList.addAll(data);
                            payWayList.get(0).setSelect(true);
                            payWayAdapter.notifyDataSetChanged();
                            setMoneyList(0);
                        }
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            }, type);
        }
    }

    private void setMoneyList(int pos){
        chargeMoneyBeans.clear();
        String[] arr = payWayList.get(pos).getAmountList().split(",");
        for (int i= 0; i < arr.length; i++) {
            DiamondListBean bean = new DiamondListBean();
            bean.setValue(arr[i]);
//            if (i==0) {
//                bean.setSelect(true);
//            }
            chargeMoneyBeans.add(bean);
        }
        chargeMoneyAdapter.notifyDataSetChanged();
    }

    private void setMoneyColor(TextView tv){//#FFD14F
        int[] colors = {Color.parseColor("#FBFFC9"), Color.parseColor("#fff000")};
        float[] position = {0f, 1.0f};
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * tv.getText().length(), 0, colors, position, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(mLinearGradient);
        tv.invalidate();
    }

    /**
     * 字体颜色渐变
     * @param textView
     */
    private void setTextViewStyles(TextView textView) {
        float x1=textView.getPaint().measureText(textView.getText().toString());//测量文本 宽度
        float y1=textView.getPaint().getTextSize();//测量文本 高度
        int c1=Color.parseColor("#FFE65A");//初始颜色值
        int c2= Color.parseColor("#FBFFC9");//结束颜色值

        LinearGradient leftToRightLG = new LinearGradient(0, 0, x1, 0,c1, c2, Shader.TileMode.CLAMP);//从左到右渐变
        LinearGradient topToBottomLG = new LinearGradient(0, 0, 0, y1,c1, c2, Shader.TileMode.CLAMP);//从上到下渐变

        textView.getPaint().setShader(leftToRightLG);
        textView.invalidate();
    }

    private void dealEditZero(EditText editText) {
        String str = editText.getText().toString().replace(",", "");
        if (str.length() > 0 && Long.parseLong(str) > 0 && str.indexOf(0) == 0) {
            editText.setText(String.valueOf(Long.valueOf(str)));
        }
    }

    /**
     * usdt充值
     */
    private void usdtRecharge() {
        String usdt = usdtEdit.getText().toString();
        if (TextUtils.isEmpty(usdt)) {
            showToastTip(false, getString(R.string.deposit_amount_hint));
            usdtEdit.requestFocus();
            return;
        }
        RechargeChannel bean = dataList.get(channelPosition);
        double money = Double.parseDouble(usdt);
        if (money < bean.getLowest() || money > bean.getHighest()) {
            showToastTip(false, getString(R.string.recharge_usdt_deposit_tip));
            return;
        }
        dealEditZero(usdtEdit);
        if (TextUtils.isEmpty(payAddress.getText().toString())) {
            showToastTip(false, getString(R.string.recharge_usdt_address_empty));
            payAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(payNumber.getText().toString())) {
            showToastTip(false, getString(R.string.recharge_usdt_address_empty_number));
            payNumber.requestFocus();
            return;
        }

        showDialog();
    }

    /**
     * 银行充值
     */
    private void bankRecharge() {
        if (TextUtils.isEmpty(backMoney.getText().toString())) {
            showToastTip(false, getString(R.string.deposit_amount_hint));
            backMoney.getResources();
            return;
        }

        dealEditZero(backMoney);
        String moneyString = backMoney.getText().toString();
        if (moneyString.contains(",")) {
            moneyString = moneyString.replaceAll(",", "");
        }

        long lowest = dataList.get(channelPosition).getLowest();
        if (Long.parseLong(moneyString) < lowest) {
            String format = String.format(getString(R.string.recharge_money_pay_less),
                    String.valueOf(lowest), AppConfig.getCurrencySymbol());
            showToastTip(false, format);
            backMoney.setText(RegexUtils.formatNumber(dataList.get(channelPosition).getLowest()));
            return;
        }

        //最多充值
        long highest = dataList.get(channelPosition).getHighest();
        if (Long.parseLong(moneyString) > highest) {
            String format = String.format(getString(R.string.recharge_money_pay_most),
                    String.valueOf(highest), AppConfig.getCurrencySymbol());
            showToastTip(false, format);
            backMoney.setText(RegexUtils.formatNumber(highest));
            return;
        }

        if (AppConfig.isThLive()) { //泰国盘
            String accountStr = String.valueOf(transferAccount.getText());
            if (TextUtils.isEmpty(accountStr)) {
                showToastTip(false, getString(R.string.recharge_account_empty),
                        Toast.LENGTH_LONG);
                transferAccount.requestFocus();
                return;
            }

            String transferNameStr = String.valueOf(transferName.getText());
            if (TextUtils.isEmpty(transferNameStr)) {
                showToastTip(false, getString(R.string.recharge_name_empty),
                        Toast.LENGTH_LONG);
                transferName.requestFocus();
                return;
            }
        }
        showDialog();
    }

    /**
     * 显示弹窗
     */
    private void showDialog() {
        commonDialog.setDialogContent(
                getString(R.string.recharge_usdt_submit_dialog_title),
                getString(R.string.recharge_usdt_submit_dialog_content),
                getString(R.string.recharge_usdt_submit_dialog_cancel),
                getString(R.string.recharge_usdt_submit_dialog_confirm),
                view -> commonDialog.dismiss(),
                view -> {
                    commonDialog.dismiss();
                    doRecharge();
                });
        commonDialog.show(getSupportFragmentManager(), "bank dialog");
    }

    /**
     * 银行卡充值
     */
    private void doRecharge() {
        showLoadingDialog();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        if (bankInfoList.size() > 0) {
            commonParams.put("bankId", bankInfoList.get(bankSelPos).getBankId());
        } else {
            hideLoadingDialog();
            return;
        }

        if (channelType == 29) {
            commonParams.put("cardNoCostomer", payAddress.getText().toString());
            commonParams.put("rechargeMoneyUsdt", usdtEdit.getText().toString());
            commonParams.put("thirdOrderNo", payNumber.getText().toString());
        } else {
            String moneyString = backMoney.getText().toString();
            if (moneyString.contains(",")) {
                moneyString = moneyString.replaceAll(",", "");
            }

            commonParams.put("rechargeMoney", moneyString);

            if (AppConfig.isThLive()) {
                commonParams.put("name", transferName.getText().toString());
                commonParams.put("accountNumber", transferAccount.getText().toString());
                commonParams.put("notice", backupInfo.getText().toString());
            }
        }

        Api_Order.ins().doBankRecharge(new JsonCallback<BankRechargeVO>() {
            @Override
            public void onSuccess(int code, String msg, BankRechargeVO data) {
                hideLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    String tip = getString(R.string.tfCustom);
                    commonDialog.setDialogContent(
                            null,
                            tip,
                            getString(R.string.cancel),
                            getString(R.string.see),
                            view -> {
                                commonDialog.dismiss();
                                backMoney.setText(null);
                            },
                            view -> {
                                commonDialog.dismiss();
                                backMoney.setText(null);
                            });

                    commonDialog.show(getSupportFragmentManager(), "bank dialog");
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams, channelType);
    }

    /**
     * 获取 银行卡信息
     */
    private void doBankInfo(int type) {
        Api_Order.ins().doBankCardInfo(new JsonCallback<List<BankInfo>>() {
            @Override
            public void onSuccess(int code, String msg, List<BankInfo> data) {
                if (code == 0) {
                    if (data != null) {
                        bankInfoList.clear();
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getType() == type) {
                                bankInfoList.add(data.get(i));
                            }
                        }

                        if (bankInfoList.size() > 0) {
                            bankAdapter.setNewData(bankInfoList);
                            reSupportBank.setVisibility(View.GONE);
                            rvMoney.setVisibility(View.VISIBLE);
                            initReward();
                            if (type == 29) {
                                setUsdtInfo();
                            }
                        } else {
                            ToastUtils.showShort(getString(R.string.noData));
                        }
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    /**
     * 设置USDT 信息
     */
    private void setUsdtInfo() {
        rvMoney.setVisibility(View.GONE);
        List<SupportBankEntity> usdtList = new ArrayList<>();
        for (BankInfo bank : bankInfoList) {
            SupportBankEntity entity = new SupportBankEntity();
            entity.setBank(bank.getBankName());
            usdtList.add(entity);
        }

        usdtList.get(0).setCheck(true);
        supportBankAdapter.setNewData(usdtList);
        selectUsdtProtocol = bankInfoList;
        setUsdtDetail(selectUsdtProtocol.get(0));
        supportBank = usdtList.get(0);
        String format = String.format(getString(R.string.recharge_usdt_deposit),
                String.valueOf(dataList.get(channelPosition).getLowest()),
                String.valueOf(dataList.get(channelPosition).getHighest()));
        usdtDeposit.setText(format);
        setUsdtAttention();
    }

    /**
     * 文件存儲根目錄
     */
    private static String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 设置USDT充值信息
     *
     * @param bankInfo 协议内容
     */
    private void setUsdtDetail(BankInfo bankInfo) {
        reSupportBank.setVisibility(View.VISIBLE);
        usdtAddress.setText(bankInfo.getCardNo());
        final String filePath = getFileRoot(this) + File.separator + "qr_usdt" + System.currentTimeMillis() + ".jpg";
        int qrWidget = DeviceUtils.dp2px(context, 138);
        new Thread(() -> {
            boolean success = QRCodeUtil.createQRImage(bankInfo.getCardNo(), qrWidget, qrWidget, null, filePath);
            if (success) {
                Bitmap codebm = BitmapFactory.decodeFile(filePath);
                runOnUiThread(() -> usdtQr.setImageBitmap(codebm));
            } else {
                runOnUiThread(() -> ToastUtils.showShort(context.getString(R.string.QRFailed)));
            }
        }).start();

        String format = String.format(getString(R.string.recharge_usdt_exchange_rate), rate);
        exchangeRate.setText(format);
        setUsdtAttention();

        usdtEdit.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)) {
                    usdtInput = editable.toString();
                    double result = (Double.parseDouble(editable.toString()) * Double.parseDouble(rate.replace(",", "")));
                    String aboutMoney = String.format(getString(R.string.recharge_usdt_about_arrive),
                            String.valueOf(Math.round(result)));
                    usdtArriveTime.setText(aboutMoney);
                    usdtInput = editable.toString();
                    setUsdtAttention();
                } else {
                    String aboutMoney = String.format(getString(R.string.recharge_usdt_about_arrive), String.valueOf(0));
                    usdtArriveTime.setText(aboutMoney);
                }
            }
        });
    }

    private void setUsdtAttention() {
        String htmlSrt = String.format(getString(R.string.recharge_usdt_noti_info),
                supportBank.getBank(), supportBank.getBank(), usdtInput);
        attention.setText(Html.fromHtml(htmlSrt));
    }


    private void toRechargea(String url, long money, long sectionGold) {
        Api_Pay.ins().payFoura(url, money, sectionGold, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String result) {
                if (result != null) LogUtils.e(code + "," + msg + "," + result);
                hideLoadingDialog();
                if (code == 0 && !TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String data = jsonObject.optString("payHtml");
                    LogUtils.e(data);
                    //1URL 2HTML 3qrcode 4外部浏览器
                    if (dataList.get(channelPosition).getCallbackType() == 1) {
                        LogUtils.e("方式一");
                        H5Activity.start(RechargeActivity.this, getString(R.string.money_pay), data, false);
                    } else if (dataList.get(channelPosition).getCallbackType() == 2) {
                        LogUtils.e("方式二");//isWeixinAvilible
                        H5Activity.start(RechargeActivity.this, getString(R.string.money_pay), data);
                    } else if (dataList.get(channelPosition).getCallbackType() == 3) {
                        LogUtils.e("方式三");
                        WxPayActivity.start(RechargeActivity.this,
                                dataList.get(channelPosition).getType() == 1,
                                data, dataList.get(channelPosition).getProducts()
                                        .get(moneySelPos).getUserRmb() / 100);
                    } else if (dataList.get(channelPosition).getCallbackType() == 4) {
                        LogUtils.e("方式四");
                        IntentUtils.toBrowser(RechargeActivity.this, data);
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void initReward() {
        reSupportBank.setVisibility(View.GONE);
        if (!mMoneyAdapter.getData().isEmpty()) {
            RecharegPrice vo = mMoneyAdapter.getData().get(0);
            backMoney.setText(RegexUtils.formatNumber(vo.getUserRmb() / 100));
        }
    }

    private boolean needAppend() {
        return !AppConfig.isThLive();
    }




    private void setTvService(TextView tv){
        String string = "<font color='#B8B2C8'> " +getResources().getString(R.string.contact_service)+ "</font>";
        string = string +"<a href='http://www.baidu.com'>" + getResources().getString(R.string.online_service)+"</a><br/>" ;
        tv.setText(Html.fromHtml(string));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}


