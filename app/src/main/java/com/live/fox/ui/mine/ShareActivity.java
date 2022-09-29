package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.AppConfig;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.dialog.MyShareDialog;
import com.live.fox.entity.ShareUserInfo;
import com.live.fox.server.Api_Promotion;
import com.live.fox.ui.mine.withdraw.ExChangeMoneyActivity;
import com.live.fox.ui.mine.withdraw.MoneyOutRecordActivity;
import com.live.fox.ui.mine.withdraw.MoneyOutToCardActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.DeviceUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 分享
 */
public class ShareActivity extends BaseHeadActivity implements View.OnClickListener {

    private TextView tvProfit;
    private TextView tvSharecount;
    private RecyclerView rv;
    private TextView tv_tip;
    private TextView tv_sharerule;

    String pageInfo = "";
    List<ShareUserInfo> shareUserInfoList = new ArrayList<>();

    BaseQuickAdapter adapter;
    MyShareDialog shareDialog;
    long balance;

    public static void startActivity(@NonNull Context context) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, ShareActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.share), true, true);

        tvProfit = findViewById(R.id.tv_profit);
        tvSharecount = findViewById(R.id.tv_sharecount);
        rv = findViewById(R.id.invite_record_recycler);
        tv_tip = findViewById(R.id.tv_tip);
        tv_sharerule = findViewById(R.id.tv_sharerule);
        findViewById(R.id.btn_changejb).setOnClickListener(this);
        findViewById(R.id.btn_moneyout).setOnClickListener(this);
        findViewById(R.id.tv_moneyoutrecodre);
        findViewById(R.id.tv_tip).setOnClickListener(this);
        findViewById(R.id.btn_share).setOnClickListener(this);

        setRightImgId(R.drawable.head_share_ic);
        getIvRight().setOnClickListener(view -> showShareDialog());

        showLoadingDialog(getString(R.string.baseLoading), false, false);
        initRecycleView();
    }

    public void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShareActivity.this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_share, new ArrayList<ShareUserInfo>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                ShareUserInfo data = (ShareUserInfo) item;
                helper.setText(R.id.tv_name, data.getNickname());
                GlideUtils.loadDefaultCircleImage(mContext, data.getAvatar(), helper.getView(R.id.iv_head));
            }
        });
    }

    public void refreshPage() {
        if (!StringUtils.isEmpty(pageInfo)) {
            try {
                String shareProfit = "";
                String shareFee = "";
                String promotionTopupMin = "";//分享收益兑换金币最小值
                String promotionWithdrawMin = "";//分享收益提现最小值
                String promotionWithdrawFee = "";//分享收益提现手续费
                String allProfit = new JSONObject(pageInfo).opt("allProfit").toString();
                tvProfit.setText(RegexUtils.westMoney(Double.parseDouble(allProfit)));
                tvSharecount.setText(new JSONObject(pageInfo).opt("shareCount").toString());
                shareProfit = new JSONObject(pageInfo).opt("shareProfit").toString();
                shareFee = new JSONObject(pageInfo).opt("shareFee").toString();
                promotionTopupMin = new JSONObject(pageInfo).opt("promotionTopupMin").toString();
                promotionWithdrawFee = new JSONObject(pageInfo).opt("promotionWithdrawFee").toString();
                promotionWithdrawMin = new JSONObject(pageInfo).opt("promotionWithdrawMin").toString();
                String balanceString = new JSONObject(pageInfo).opt("balance").toString();
                if (!StringUtils.isEmpty(balanceString)) {
                    balance = Long.parseLong(balanceString);
                }
                if (!StringUtils.isEmpty(shareProfit)) {
                    shareProfit = RegexUtils.westMoney(Long.parseLong(shareProfit));
                }

                String str = getString(R.string.share_rule_text);
                str = str.replace("1111", shareProfit);
                str = str.replace("2222", AppConfig.getCurrencySymbol());
                str = str.replace("3333", shareFee);
                str = str.replace("4444", promotionTopupMin);
                str = str.replace("5555", AppConfig.getCurrencySymbol());
                str = str.replace("6666", promotionWithdrawFee);
                str = str.replace("7777", promotionWithdrawMin);
                str = str.replace("8888", AppConfig.getCurrencySymbol());
                tv_sharerule.setText(str);
                if (shareUserInfoList != null) {
                    adapter.setNewData(shareUserInfoList);
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetInfo();
        doGetInvertUserList();
    }

    public void doGetInfo() {
        Api_Promotion.ins().index(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                if (code == 0) {
                    pageInfo = data;
                    refreshPage();
                } else {
                    ToastUtils.showShort(msg);
                }

            }
        });
    }

    public void doGetInvertUserList() {
        Api_Promotion.ins().sharelog(0, new JsonCallback<List<ShareUserInfo>>() {
            @Override
            public void onSuccess(int code, String msg, List<ShareUserInfo> data) {
                //0,success,{"cardId":0,"type":0}
                if (data != null) LogUtils.e(code + "," + msg + "," + data);
                if (code == 0) {
                    shareUserInfoList.clear();
                    shareUserInfoList.addAll(data);
                    refreshPage();
                } else {
                    ToastUtils.showShort(msg);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.btn_changejb:
                long profit = balance >= 100 ? Long.parseLong((balance + "").substring(0, (balance + "").length() - 3)) : 0;
                ExChangeMoneyActivity.startActivity(ShareActivity.this, 3, profit);
                break;
            case R.id.btn_moneyout:
                long profit1 = balance >= 100 ? balance : 0;
                MoneyOutToCardActivity.startActivity(ShareActivity.this, 3, profit1);
                break;
            case R.id.tv_moneyoutrecodre:
                MoneyOutRecordActivity.startActivity(ShareActivity.this);
                break;
            case R.id.tv_tip:
                showPopDialog();
                break;
            case R.id.btn_share:
                showShareDialog();
                break;
        }
    }

    private void showShareDialog() {
        if (shareDialog == null) {
            shareDialog = new MyShareDialog(ShareActivity.this);
        }
        shareDialog.show();
    }

    PopupWindow mPopupWindow;

    public void showPopDialog() {
        View popupWindow = LayoutInflater.from(this).inflate(R.layout.dialog_sharetip, null);
        TextView money = popupWindow.findViewById(R.id.tv_yuan);
        TextView moneyTwo = popupWindow.findViewById(R.id.currency_symbol);
        money.setText(AppConfig.getCurrencySymbol());
        moneyTwo.setText(AppConfig.getCurrencySymbol());
        mPopupWindow = new PopupWindow(popupWindow, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int measuredWidth = popupWindow.getMeasuredWidth();
        int measuredHeight = popupWindow.getMeasuredHeight();
        int[] location = new int[2];
        tv_tip.getLocationOnScreen(location);

        int x = (location[0] + tv_tip.getWidth() / 2) - measuredWidth / 2 + DeviceUtils.dp2px(ShareActivity.this, 43);
        int y = location[1] - measuredHeight;

        mPopupWindow.showAtLocation(tv_tip, Gravity.NO_GRAVITY, x, y);

        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.update();

        try {
            if (pageInfo != null) {
                if (AppConfig.isThLive()) {
                    ((TextView) popupWindow.findViewById(R.id.tv_money1))
                            .setText(RegexUtils.westMoney(Long.parseLong(new JSONObject(pageInfo)
                                    .opt("investRebate").toString())));
                    ((TextView) popupWindow.findViewById(R.id.tv_money2))
                            .setText(RegexUtils.westMoney(Long.parseLong(new JSONObject(pageInfo)
                                    .opt("bindRebate").toString())));
                } else {
                    ((TextView) popupWindow.findViewById(R.id.tv_money1))
                            .setText(RegexUtils.westMoney(Long.parseLong(new JSONObject(pageInfo)
                                    .opt("investRebate").toString() + "000")));
                    ((TextView) popupWindow.findViewById(R.id.tv_money2))
                            .setText(RegexUtils.westMoney(Long.parseLong(new JSONObject(pageInfo)
                                    .opt("bindRebate").toString() + "000")));
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }
}
