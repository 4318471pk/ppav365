package com.live.fox.ui.mine.withdraw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundTextView;
import com.live.fox.AppConfig;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityWithdrawalBinding;
import com.live.fox.entity.GameStatement;
import com.live.fox.entity.RechargeTypeListBean;
import com.live.fox.entity.User;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.entity.UserBankBean;
import com.live.fox.entity.WithdrawChannelTypeBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Order;
import com.live.fox.server.Api_User;
import com.live.fox.server.BaseApi;
import com.live.fox.utils.ArithUtil;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.PasswordPopUtitls;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 提现
 */
public class WithdrawalActivity extends BaseBindingViewActivity {

    ActivityWithdrawalBinding mBind;

    PasswordPopUtitls passwordPopUtitls;

    UserBankBean userBankBean;

    private int lastWayPos = 0;
    private DrawWayAdapter drawWayAdapter;
    List<WithdrawChannelTypeBean> wayList = new ArrayList<>();

    public static void startActivity(Context context) {
        Intent i = new Intent(context, WithdrawalActivity.class);
        context.startActivity(i);
    }



    @Override
    public void onClickView(View view) {
        if (view == mBind.gtCommit) {
            if (TextUtils.isEmpty(mBind.edWithdrawAmount.getText().toString().trim())) {
                ToastUtils.showShort(getString(R.string.input_withdraw_money));
                return;
            }
            if (wayList.get(lastWayPos).getCode().equals("usdt")) {
                if (TextUtils.isEmpty(mBind.etUsdtAddress.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.input_withdraw_money));
                    return;
                }
            }
            passwordPopUtitls.show();
        }

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_withdrawal;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        mBind.setClick(this);
        passwordPopUtitls = new PasswordPopUtitls(this, mBind.getRoot());
        setActivityTitle(getString(R.string.account_withdrawal));
        getTvTitleRight().setText(getString(R.string.withdrawCards));
        getTvTitleRight().setTextColor(0xFFA800FF);
        getTvTitleRight().setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                startActivityForResult(new Intent(WithdrawalActivity.this,BankCardListActivity.class), ConstantValue.REQUEST_CODE1);
            }
        });

        //mBind.llRadioBtns.addView();
        String string = "<font color='#B8B2C8'> " +getResources().getString(R.string.contact_service)+ "</font>";
        string = string +"<a href='http://www.baidu.com'>" + getResources().getString(R.string.online_service)+"</a><br/>" ;
        mBind.tvService.setText(Html.fromHtml(string));
        mBind.tvService.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString spannedString=new SpannableString(mBind.tvMax.getText().toString());
        spannedString.setSpan(new UnderlineSpan(),0,spannedString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mBind.tvMax.setText(spannedString);


        passwordPopUtitls.setPayPwdConfirm(new PasswordPopUtitls.PayPwdConfirm() {
            @Override
            public void clickConfirm(String pwd) {
                withDraw(pwd);
            }
        });

        drawWayAdapter = new DrawWayAdapter(wayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBind.rcWay.setLayoutManager(layoutManager);
        mBind.rcWay.setAdapter(drawWayAdapter);

        getNetData();
    }

    private void getUserBank(){
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        Api_Order.ins().getUserBank(new JsonCallback<UserBankBean>() {
            @Override
            public void onSuccess(int code, String msg, UserBankBean data) {
                hideLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    userBankBean = data;
                } else {
                    ToastUtils.showShort(msg);
                    startActivityForResult(new Intent(WithdrawalActivity.this
                            ,BankCardListActivity.class), ConstantValue.REQUEST_CODE1);
                }
            }
        }, commonParams);

    }

    private void getNetData(){
        showLoadingDialog();
        getAss();
        getUserBank();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();

        Api_Order.ins().getWithDrawType(new JsonCallback<List<WithdrawChannelTypeBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<WithdrawChannelTypeBean> data) {
                hideLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    if (data != null & data.size() > 0) {
//                        for (int i =0 ; i < data.size(); i++) {
//                            //getList(data.get(i).getId());
//                        }
                        wayList.addAll(data);
                        wayList.get(0).setSelect(true);
                        if (wayList.get(0).getCode().equals("usdt")) {
                            mBind.layoutUsdt.setVisibility(View.VISIBLE);
                        }
                        drawWayAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);


    }


    private void withDraw(String pwd){
        showLoadingDialog();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        commonParams.put("withdrawType" ,wayList.get(lastWayPos).getCode());
        commonParams.put("cash" ,mBind.edWithdrawAmount.getText().toString().trim());
        commonParams.put("cashPassword" , pwd);
        commonParams.put("coin" , "rmb");
        if (userBankBean !=null) {
            commonParams.put("cardId" ,userBankBean.getId());
        }

        Api_Order.ins().getWithDraw(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    ToastUtils.showShort(getString(R.string.withdraw_suc));
                    getAss();
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);
    }

    private void getAss(){
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        Api_Order.ins().getAssets(new JsonCallback<UserAssetsBean>() {
            @Override
            public void onSuccess(int code, String msg, UserAssetsBean data) {
                hideLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    mBind.balanceMoneyTv.setText(data.getGold() + "");
                    mBind.diamondTv.setText(data.getDiamond() + data.getVipDiamond() + "");
                    mBind.tvMax.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBind.edWithdrawAmount.setText(data.getGold() +"");
                        }
                    });
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantValue.REQUEST_CODE1) {
            getUserBank();
        }
    }



    class DrawWayAdapter extends BaseQuickAdapter<WithdrawChannelTypeBean, BaseViewHolder> {

        public DrawWayAdapter(List data) {
            super(R.layout.item_withdraw_way, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, WithdrawChannelTypeBean data) {
            TextView tvName = helper.getView(R.id.tvName);
            ImageView iv = helper.getView(R.id.iv);
            LinearLayout mView = helper.getView(R.id.mView);
            if (data.isSelect()) {
                mView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_raido_a800ff_d689ff));
                tvName.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                mView.setBackground(mContext.getResources().getDrawable(R.drawable.bg_stroke_a800ff));
                tvName.setTextColor(mContext.getResources().getColor(R.color.colorA800FF));
            }
            tvName.setText(data.getName());
            if (data.getCode().equals("usdt")) {
                iv.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_usdt));
            } else {
                iv.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_unionpay));
            }

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < wayList.size(); i++ ) {
                        wayList.get(i).setSelect(false);
                    }
                    wayList.get(helper.getLayoutPosition()).setSelect(true);
                    if (wayList.get(helper.getLayoutPosition()).getCode().equals("usdt")) {
                        mBind.layoutUsdt.setVisibility(View.VISIBLE);
                    } else {
                        mBind.layoutUsdt.setVisibility(View.GONE);
                    }
                    lastWayPos = helper.getLayoutPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

}
