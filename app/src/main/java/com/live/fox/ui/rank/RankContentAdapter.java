package com.live.fox.ui.rank;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.Rank;
import com.live.fox.manager.DataCenter;
import com.live.fox.utils.GlideUtils;
import com.live.fox.manager.AppUserManger;

/**
 * 直播排行榜RecycleView适配器
 */
public class RankContentAdapter extends BaseQuickAdapter<Rank, BaseViewHolder> {

    private final int rankType;

    public RankContentAdapter(int type) {
        super(R.layout.item_rank_content);
        rankType = type;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Rank rank) {

        baseViewHolder.setText(R.id.item_rank_number, String.valueOf(baseViewHolder.getLayoutPosition() + 3));
        ImageView avatar = baseViewHolder.getView(R.id.item_rank_avatar);
        GlideUtils.loadCircleImage(mContext, rank.getAvatar(), R.drawable.img_default, R.drawable.img_default, avatar);
        ImageView isLive = baseViewHolder.getView(R.id.item_rank_live_start);
        if (0 == rank.getLiveId() || 2 == rankType || 4 == rankType) {
            isLive.setVisibility(View.VISIBLE);
        } else {
            isLive.setVisibility(View.GONE);
        }

        if (baseViewHolder.getLayoutPosition() == 0) {
            baseViewHolder.getView(R.id.item_rank_bottom_line).setVisibility(View.GONE);
        }

        baseViewHolder.setText(R.id.item_rank_name, rank.getNickname());
        TextView gender = baseViewHolder.getView(R.id.item_rank_gender);
        Drawable drawable;

        //性别
        if (rank.getSex() == 1) {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_male);
        } else {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_female);
        }

        gender.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
        gender.setText(String.valueOf(rank.getUserLevel()));

        //主播级别
        ImageView level = baseViewHolder.getView(R.id.item_rank_level);
        level.setImageLevel(rank.getAnchorLevel());

        TextView focus = baseViewHolder.getView(R.id.item_rank_focus);

        //是否是用户自己
        if (rank.getUid() == DataCenter.getInstance().getUserInfo().getUser().getUid()) {
            focus.setVisibility(View.GONE);
            avatar.setBackgroundResource(R.drawable.shape_oval_stroke_3);
            ViewCompat.setElevation(avatar, 4);
            ViewCompat.setElevation(isLive, 4);
        }

        baseViewHolder.setText(R.id.item_rank_coin_number, String.valueOf(rank.getRankValue()));
        baseViewHolder.addOnClickListener(R.id.item_rank_focus);
    }
}
