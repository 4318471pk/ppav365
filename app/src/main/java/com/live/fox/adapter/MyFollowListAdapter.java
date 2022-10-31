package com.live.fox.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.LiveFinishActivity;
import com.live.fox.R;
import com.live.fox.entity.Follow;
import com.live.fox.utils.GlideUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MyFollowListAdapter extends BaseQuickAdapter<Follow, BaseViewHolder> {


    public MyFollowListAdapter(List data) {
        super(R.layout.item_follow_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Follow data) {
//        if (data == null) {
//            return;
//        }
        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(data.getNickname());
        ImageView ivSex = helper.getView(R.id.ivSex);
        if (data.getSex() == 1) {
            ivSex.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.men));
        } else if (data.getSex() == 2) {
            ivSex.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.women));
        }

        RoundedImageView ivHead = helper.getView(R.id.iv_head);
        GlideUtils.loadImage(mContext, data.getAvatar(), ivHead);

        ImageView ivNoble = helper.getView(R.id.iv_noble_level);

    }


}
