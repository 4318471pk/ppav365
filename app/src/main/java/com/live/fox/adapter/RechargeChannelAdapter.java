package com.live.fox.adapter;


import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.RechargeChannel;
import com.luck.picture.lib.tools.ScreenUtils;


/**
 * 充值渠道
 */
public class RechargeChannelAdapter extends BaseQuickAdapter<RechargeChannel, BaseViewHolder> {

    public RechargeChannelAdapter() {
        super(R.layout.item_recharge_channel);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeChannel item) {
        ConstraintLayout itemRootView = helper.getView(R.id.ll_p);
        int screenWidth = (ScreenUtils.getScreenWidth(mContext) - ScreenUtils.dip2px(mContext, 70)) / 4;
        itemRootView.setLayoutParams(new ConstraintLayout.LayoutParams(screenWidth,
                ConstraintLayout.LayoutParams.WRAP_CONTENT));

        ImageView select = helper.getView(R.id.item_recharged_select);
        select.setVisibility(item.isCheck() ? View.VISIBLE : View.GONE);

        helper.setText(R.id.tv_name, item.getName());

        ImageView backFlag = helper.getView(R.id.iv_left);
        backFlag.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(item.getLogs())) {
            backFlag.setImageLevel(Integer.parseInt(item.getLogs()));
        }

        ImageView imageView = helper.getView(R.id.item_recharged_back_logo);
        if (TextUtils.isEmpty(item.getChannelImage())) {
            imageView.setImageLevel(item.getType());
        } else {
            Glide.with(mContext).load(item.getChannelImage()).into(imageView);
        }

        CheckBox checkStatus = helper.getView(R.id.item_recharge_bg);
        checkStatus.setChecked(item.isCheck());
    }
}
