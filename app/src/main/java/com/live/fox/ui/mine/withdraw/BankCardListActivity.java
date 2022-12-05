package com.live.fox.ui.mine.withdraw;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityBankcardListBinding;
import com.live.fox.dialog.bottomDialog.AddNewBankCardDialog;
import com.live.fox.entity.UserBankBean;
import com.live.fox.entity.WithdrawChannelTypeBean;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.ui.agency.PromoMaterialActivity;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.GradientTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BankCardListActivity extends BaseBindingViewActivity {

    ActivityBankcardListBinding mBind;

    private List<UserBankBean> bankList = new ArrayList<>();

    AddNewBankCardDialog addNewBankCardDialog;

    private View bankView1;
    private View bankView2;
    private View bankView3;

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_bankcard_list;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        mBind.setClick(this);
        setActivityTitle(getString(R.string.withdrawCards));

        Drawable drawable=getResources().getDrawable(R.mipmap.bg_empty_card);
       // addEmptyCard(drawable);
       // addBankCard();
        addTips(drawable.getIntrinsicHeight());

        addNewBankCardDialog = AddNewBankCardDialog.getInstance();
        addNewBankCardDialog.setAddNewBankCardSuc(new AddNewBankCardDialog.AddNewBankCardSuc() {
            @Override
            public void addSuc() {
                getNetData();
            }
        });
        getNetData();
    }



    private void addEmptyCard(Drawable drawable)
    {
        ImageView imageView=new ImageView(this);
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.topMargin=ScreenUtils.getDip2px(this,10);
        imageView.setLayoutParams(ll);
        imageView.setImageDrawable(drawable);
        imageView.setAdjustViewBounds(true);
        imageView.setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), addNewBankCardDialog);
            }
        });
        mBind.llCardList.addView(imageView);
    }

    private void addBankCard(int pos, UserBankBean bean)
    {
        View view=getLayoutInflater().inflate(R.layout.item_bankcard,mBind.llCardList,false);
        TextView tvBankName = view.findViewById(R.id.tvBankName);
        TextView tvBankAccount = view.findViewById(R.id.tvBankAccount);
        GradientTextView tvMoren1 = view.findViewById(R.id.tvMoren1);
        TextView tvMoren2 = view.findViewById(R.id.tvMoren2);
        ImageView ivBG = view.findViewById(R.id.ivBG);

        if (bean.getIsDefault() == 1){
            tvMoren1.setVisibility(View.VISIBLE);
            tvMoren2.setVisibility(View.GONE);
        } else {
            tvMoren1.setVisibility(View.GONE);
            tvMoren2.setVisibility(View.VISIBLE);
        }
        tvBankName.setText(bean.getBankName());
        if (bean.getCardNo().length() > 4) {
            tvBankAccount.setText("****   " +  "****   " + "****   " + bean.getCardNo().substring(bean.getCardNo().length()-4));
        } else {
            tvBankAccount.setText(bean.getCardNo());

        }
        if (pos == 1) {
            ivBG.setImageDrawable(getResources().getDrawable(R.mipmap.tixian_card2));
            bankView1 = view;
        } else if (pos == 2) {
            ivBG.setImageDrawable(getResources().getDrawable(R.mipmap.tixian_card3));
            bankView2 = view;
        } else {
            bankView3 = view;
        }

        view.findViewById(R.id.tvMoren2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                norenBank(bean.getId());
            }
        });

        view.findViewById(R.id.tvDetele).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBank(bean.getId());
            }
        });

        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.topMargin=ScreenUtils.getDip2px(this,10);
        mBind.llCardList.addView(view,ll);
    }

    private void addTips(int viewHeight)
    {
        TextView textView=new TextView(this);
        textView.setTextColor(0xffB8B2C8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        LinearLayout.LayoutParams ll= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.topMargin=(viewHeight+ScreenUtils.getDip2px(this,10))*3+ScreenUtils.getDip2px(this,20);
        textView.setLayoutParams(ll);
        textView.setGravity(Gravity.CENTER);
        textView.setText(getResources().getString(R.string.max3Cards));
        mBind.rlMain.addView(textView);
    }



    private void getNetData(){
        showLoadingDialog();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        Api_Order.ins().getUserBankList(new JsonCallback<List<UserBankBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<UserBankBean> data) {
                if(isFinishing() || isDestroyed())
                {
                    return;
                }

                hideLoadingDialog();
                if (code == 0 ) {
                    if (data != null ) {
                        bankList.clear();
                        bankList.addAll(data);
                        mBind.llCardList.removeAllViews();
                        for (int i =0; i< bankList.size(); i++) {
                            addBankCard(i,bankList.get(i));
                        }
                        if (bankList.size() <3) {
                            Drawable drawable=getResources().getDrawable(R.mipmap.bg_empty_card);
                            addEmptyCard(drawable);
                        }

                    } else {
                        Drawable drawable=getResources().getDrawable(R.mipmap.bg_empty_card);
                        addEmptyCard(drawable);
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);

    }

    private void deleteBank(long id) {
        showLoadingDialog();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        commonParams.put("bankId",id);
        Api_Order.ins().deleteBank(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(isFinishing() || isDestroyed())
                {
                    return;
                }

                if (code == 0 ) {
                    getNetData();
                    ToastUtils.showShort(getString(R.string.delete_suc));
                } else {
                    ToastUtils.showShort(msg);
                }
                hideLoadingDialog();
            }
        }, commonParams);
    }

    private void norenBank(long id) {
        showLoadingDialog();
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        commonParams.put("bankId",id);
        Api_Order.ins().setMorenBank(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    getNetData();
                    ToastUtils.showShort(getString(R.string.set_suc));
                } else {
                    ToastUtils.showShort(msg);
                }
                hideLoadingDialog();
            }
        }, commonParams);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(this, WithdrawalActivity.class);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}
