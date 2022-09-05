package com.live.fox.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.User;
import com.live.fox.utils.GlideUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class PkGiftAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    Context mContext;

    public PkGiftAdapter(@Nullable List<User> data, Context mContext) {
        super(R.layout.adapter_pkgift_item, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        RoundedImageView rivA = helper.getView(R.id.rivA);
        if (item.chatHide == 1) {
            rivA.setImageResource(R.drawable.ic_shenmi);
        } else {
            GlideUtils.loadDefaultCircleImage(mContext, item.getAvatar(), rivA);
        }

    }


}
