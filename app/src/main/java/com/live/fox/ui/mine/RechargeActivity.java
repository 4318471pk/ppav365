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
import com.live.fox.utils.Strings;
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

    User user;
    private CommonDialog commonDialog;
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

        mView = findViewById(R.id.mView);
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
                for (int i = 0; i < payNameList.size(); i++) {
                    payNameAdapter.getData().get(i).setSelect(false);
                }

                payNameAdapter.getData().get(position).setSelect(true);
                payNameAdapter.notifyDataSetChanged();
                getChannelType(payNameList.get(position).getType());
                nowPayId = payNameList.get(position).getType();
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
                for (int i = 0; i < payWayMap.get(nowPayId).size(); i++) {
                    payWayMap.get(nowPayId).get(i).setSelect(false);
                }
                payWayMap.get(nowPayId).get(position).setSelect(true);
                payWayAdapter.notifyDataSetChanged();

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
        getChargeCenter();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppIMManager.ins().removeMessageReceivedListener(RechargeActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (payWayMap != null) {
            payWayMap.clear();
        }

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
                if (isChargeMoney && chargeMoneyBeans.size() > 0) {
                    for (int i = 0; i < chargeMoneyBeans.size(); i++) {
                        if (chargeMoneyBeans.get(i).isSelect()) {
                            topUpNow(chargeMoneyBeans.get(i).getValue());
                        }
                    }
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

    private void setPopCharge() {
        View popupView = getLayoutInflater().inflate(R.layout.pop_charge, null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
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


    private UserAssetsBean userAssetsBean = null;


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

    private void setMoneyColor(TextView tv) {//#FFD14F
        int[] colors = {Color.parseColor("#FBFFC9"), Color.parseColor("#fff000")};
        float[] position = {0f, 1.0f};
        LinearGradient mLinearGradient = new LinearGradient(0, 0, tv.getPaint().getTextSize() * tv.getText().length(), 0, colors, position, Shader.TileMode.CLAMP);
        tv.getPaint().setShader(mLinearGradient);
        tv.invalidate();
    }

    private void getChargeCenter() {
        showLoadingDialogWithNoBgBlack();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();

        getAsset();

        Api_Order.ins().getChargeType(new JsonCallback<List<RechargeTypeBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<RechargeTypeBean> data) {
                hideLoadingDialog();
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


                        if (payNameList.size() > 0) {
                            getChannelType(payNameList.get(0).getType());
                            nowPayId = payNameList.get(0).getType();
                        }

                    }

                } else {
                    ToastUtils.showShort(msg);
                }
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


        if (arr == null || arr.length == 0) {

        }
    }


//    {
//        "msg":{
//        "mode":"url",
//                "rebate":"15",
//                "sign":"ebff42db5b12c2de8520beaae272b9b7",
//                "payUrl":"http://119.23.250.92:8090/api/page/pay.html?sign=90594C56516621CDB713DD29F534755C5AED9B44001B340D9B900B9B8ED375D4",
//                "oid":"WNSY120901190659649910"
//    },
//        "code":200
//    }
    public void topUpNow(String amount) {
        showLoadingDialogWithNoBgBlack();
        Api_Pay.ins().pay(amount, channelCode, mType + "", new JsonCallback<PayBean>() {
            @Override
            public void onSuccess(int code, String msg, PayBean result) {
                hideLoadingDialog();
                if (code == 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg);
                        String mode = jsonObject.optString("mode","");
                        String payUrl = jsonObject.optString("payUrl","");
                        if(!TextUtils.isEmpty(mode) && mode.toLowerCase().equals("url") && !TextUtils.isEmpty(payUrl))
                        {
                            IntentUtils.toBrowser(RechargeActivity.this, payUrl);
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

    private void setTvService(TextView tv) {
        String string = "<font color='#B8B2C8'> " + getResources().getString(R.string.contact_service) + "</font>";
        string = string + "<a href='http://www.baidu.com'>" + getResources().getString(R.string.online_service) + "</a><br/>";
        tv.setText(Html.fromHtml(string));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}


