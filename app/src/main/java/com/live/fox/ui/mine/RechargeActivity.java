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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
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
import com.live.fox.entity.DiamondListBean;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.entity.PayBean;
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


/**
 * 充值
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener,
        AppIMManager.OnMessageReceivedListener {

    private RecyclerView rvChannel;
    private MarqueeView adRoundTextView;

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

    private final List<RechargeChannel> dataList = new ArrayList<>();  //充值渠道数据
    List<RechargeChannel> vipChannelList = new ArrayList<>();
    private final List<BankInfo> bankInfoList = new ArrayList<>();
    private int channelPosition = 0; //充值渠道选择位置
    int bankSelPos = 0;
    int moneySelPos = 0;
    private int supportPosition = 0; //银行或者USDT
    User user;
    private UserAssetsBean userAssetsBean = null;
    private SupportBankEntity supportBank = new SupportBankEntity();
    private List<BankInfo> selectUsdtProtocol;
    private CommonDialog commonDialog;
    private String usdtInput = "0";
    private int nowPayId = 0; //现在选择的支付通道id

    private boolean isChargeMoney = true; //true:点击充值  false:点击兑换钻石

    private String channelCode;
    private int mType;

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
        rvChannel = findViewById(R.id.rv_channel);
        adRoundTextView = findViewById(R.id.adRoundTextView);

        tvBalanca = findViewById(R.id.balance);
        tvDiamond = findViewById(R.id.diamond);
        layoutMoney = findViewById(R.id.layout_charge);
        tvCharge = findViewById(R.id.charge);
        ivMoney = findViewById(R.id.img_money);
        layoutDiamond = findViewById(R.id.layout_exchange_diamond);
        tvExDiamond = findViewById(R.id.exchange_diamond);
        ivDiamond = findViewById(R.id.img_diamond);
        gvCharge = findViewById(R.id.gv_charge);
        tvService = findViewById(R.id.tv_service);
        layoutConfirm = findViewById(R.id.confirm);
        layoutGold = findViewById(R.id.layoutGold);
        tvPayWay = findViewById(R.id.tvPayWay);
        layoutBot = findViewById(R.id.layoutBot);

        setTvService(tvService);

        chargeMoneyAdapter = new ChargeAdapter(this, chargeMoneyBeans, true);
        chargeDiamondAdapter = new ChargeAdapter(this, chargeDiamondBeans, false);
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
                        String s = String.format(getString(R.string.confirm_exchange_diamond),
                                String.valueOf(chargeDiamondBeans.get(position).getValue()), "" + chargeDiamondBeans.get(position).getCode());
                        showMoneyDialog(chargeDiamondBeans.get(position).getValue(), s, false);
                    } else { //金币不足
                        showMoneyDialog(chargeDiamondBeans.get(position).getValue(), getString(R.string.no_money_go_to_charge), true);
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
                for (int i = 0; i < payNameList.size(); i++) {
                    if (payNameList.get(i).isSelect()) {
                        temp = i;
                        break;
                    }
                }
                if (temp != position) {
                    payNameAdapter.getData().get(temp).setSelect(false);
                    payNameAdapter.getData().get(position).setSelect(true);
                    payNameAdapter.notifyDataSetChanged();
                    getChannelType(payNameList.get(position).getType());
                    nowPayId = payNameList.get(position).getType();
                }
            }
        });

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcPayWay.setLayoutManager(layoutManager2);
        payWayAdapter = new PayWayAdapter(payWayList);
        rcPayWay.setAdapter(payWayAdapter);

        payWayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int temp = 0;
                for (int i = 0; i < payWayMap.get(nowPayId).size(); i++) {
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

                channelCode = payWayList.get(position).getChannelCode();
                mType = payWayList.get(position).getType();
            }
        });

        layoutMoney.setOnClickListener(this);
        layoutDiamond.setOnClickListener(this);
        layoutConfirm.setOnClickListener(this);

        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        user = DataCenter.getInstance().getUserInfo().getUser();

        AppIMManager.ins().addMessageListener(RechargeActivity.class, this);

        changeChargeUi();
        initChannelRv();
        showLoadingDialogWithNoBgBlack();
        getChargeCenter();
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

            channel.setCheck(true);

            channelPosition = position;
            channelAdapter.notifyDataSetChanged();
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

                }
                break;
            case R.id.iv_head_left:
                finish();
                break;
        }

    }

    private void changeChargeUi() {
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

    private void setRootAlpha(float al) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = al;
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

    private void exchangeDiamond(String yuan) {
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

    private void getAsset() {
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

    private void getChargeCenter() {
        showLoadingDialog();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();

        getAsset();

        Api_Order.ins().getChargeType(new JsonCallback<List<RechargeTypeBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<RechargeTypeBean> data) {

                if (isFinishing() || isDestroyed()) {
                    return;
                }
                if (code == 0) {
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
                hideLoadingDialog();
            }
        }, commonParams);


        Api_Order.ins().getDiamondList(new JsonCallback<List<DiamondListBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DiamondListBean> data) {
                if (code == 0) {
                    if (data != null && data.size() > 0) {
                        chargeDiamondBeans.clear();
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
        if (payWayMap.get(type) != null) {
            payWayList.clear();
            payWayList.addAll(payWayMap.get(type));
            payWayAdapter.notifyDataSetChanged();
            for (int i = 0; i < payWayList.size(); i++) {
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
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    if (code == 0) {
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

    private void setMoneyList(int pos) {
        chargeMoneyBeans.clear();
        String[] arr = payWayList.get(pos).getAmountList().split(",");
        for (int i = 0; i < arr.length; i++) {
            DiamondListBean bean = new DiamondListBean();
            bean.setValue(arr[i]);
//            if (i==0) {
//                bean.setSelect(true);
//            }
            chargeMoneyBeans.add(bean);
        }
        chargeMoneyAdapter.notifyDataSetChanged();


        channelCode = payWayList.get(pos).getChannelCode();
        mType = payWayList.get(pos).getType();
    }

    private void setMoneyColor(TextView tv) {//#FFD14F
        int[] colors = {Color.parseColor("#FBFFC9"), Color.parseColor("#fff000")};
        float[] position = {0f, 1.0f};
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * tv.getText().length(), 0, colors, position, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(mLinearGradient);
        tv.invalidate();
    }

    private void dealEditZero(EditText editText) {
        String str = editText.getText().toString().replace(",", "");
        if (str.length() > 0 && Long.parseLong(str) > 0 && str.indexOf(0) == 0) {
            editText.setText(String.valueOf(Long.valueOf(str)));
        }
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

    private void setTvService(TextView tv) {
        String string = "<font color='#B8B2C8'> " + getResources().getString(R.string.contact_service) + "</font>";
        string = string + "<a href='http://www.baidu.com'>" + getResources().getString(R.string.online_service) + "</a><br/>";
        tv.setText(Html.fromHtml(string));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}


