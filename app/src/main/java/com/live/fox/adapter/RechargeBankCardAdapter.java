package com.live.fox.adapter;


import android.text.Html;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.AppConfig;
import com.live.fox.R;
import com.live.fox.entity.BankInfo;
import com.live.fox.manager.AppUserManger;
import com.live.fox.manager.DataCenter;

/**
 * 支持的银行卡片
 */
public class RechargeBankCardAdapter extends BaseQuickAdapter<BankInfo, BaseViewHolder> {

    public RechargeBankCardAdapter() {
        super(R.layout.item_bank);
    }

    @Override
    protected void convert(BaseViewHolder helper, BankInfo item) {
        CheckBox checkBox = helper.getView(R.id.recharge_support_bank);
        checkBox.setChecked(item.isCheck());

        if (item.isCheck()) {
            helper.getView(R.id.rlBankInfo).setBackgroundResource(
                    R.drawable.shape_white_stroke_red_corners_10);
            helper.setText(R.id.tvCardNo,
                    Html.fromHtml(mContext.getString(R.string.bank_card_number) +
                            "：<font><b>" + item.getCardNo() + "</b></font>"));
            helper.setText(R.id.tvBankName, Html.fromHtml(
                    mContext.getString(R.string.kaihuBank) + "：<font><b>" +
                            item.getBankName() + "</b></font>"));
            if (!AppConfig.isThLive()) {
                helper.setText(R.id.tvUid, Html.fromHtml(
                        mContext.getString(R.string.remittance_message) + "：<font><b>" +
                                DataCenter.getInstance().getUserInfo().getUser().getUid() + "</b></font>"));
            }
            helper.setText(R.id.tvRecieptName, Html.fromHtml(
                    mContext.getString(R.string.beneficiaryName) +
                            "：<font><b>" + item.getTrueName() + "</b></font>"));
            helper.setText(R.id.tvBankFullName,
                    Html.fromHtml(mContext.getString(R.string.account_opening_bank)
                            + "：" + item.getBankSub()));
        } else {
            helper.getView(R.id.rlBankInfo).setBackgroundResource(
                    R.drawable.shape_white_stroke_gray_corners_10);
            helper.setText(R.id.tvCardNo, mContext.getString(R.string.bank_card_number) + "：******");
            helper.setText(R.id.tvBankName, mContext.getString(R.string.kaihuBank) + "：******");
            if (!AppConfig.isThLive()) {
                helper.setText(R.id.tvUid, Html.fromHtml(mContext.getString(R.string.remittance_message) + "：******"));
            }
            helper.setText(R.id.tvBankFullName, mContext.getString(R.string.account_opening_bank) + "：******");
            helper.setText(R.id.tvRecieptName, mContext.getString(R.string.beneficiaryName) + "：******");
        }
        helper.addOnClickListener(R.id.tvCopy1);
        helper.addOnClickListener(R.id.tvCopy2);
        helper.addOnClickListener(R.id.tvCopy3);
        helper.addOnClickListener(R.id.tvCopy4);
    }
}
