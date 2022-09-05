package com.live.fox.ui.rank;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.Rank;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.AppUserManger;

/**
 * Rank 排行榜
 * 顶部前三名数据适配器
 */
public class RankTop3Adapter extends BaseQuickAdapter<Rank, BaseViewHolder> {

    public RankTop3Adapter() {
        super(R.layout.item_rank_top_3);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Rank rank) {
        View top3Bg = baseViewHolder.getView(R.id.item_rank_top_bg);
        ImageView avatar = baseViewHolder.getView(R.id.item_rank_top_avatar);
        ImageView avatarDecoration = baseViewHolder.getView(R.id.item_rank_top_avatar_decoration);
        baseViewHolder.setText(R.id.item_rank_top_name, rank.getNickname());

        TextView gander = baseViewHolder.getView(R.id.item_rank_top_gender);
        ImageView level = baseViewHolder.getView(R.id.item_rank_top_level);
        baseViewHolder.setText(R.id.item_coin_number, String.valueOf(rank.getRankValue()));

        View bottom = baseViewHolder.getView(R.id.item_rank_top_3_bottom);
        avatarDecoration.setImageLevel(baseViewHolder.getLayoutPosition());
        GlideUtils.loadDefaultImage(mContext, rank.getAvatar(), avatar);

        setAvatarDecoration(baseViewHolder.getLayoutPosition(), avatarDecoration, bottom);

        //是否是自己
        if (rank.getUid() == AppUserManger.getUserInfo().getUid()) {
            top3Bg.setBackgroundResource(R.drawable.shape_white_round_10_stock_3);
        }

        //性别
        Drawable drawable;
        if (rank.getSex() == 1) {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_female);
        } else {
            drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_male);
        }
        gander.setText(String.valueOf(rank.getUserLevel()));
        gander.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);

        //主播级别
        level.setImageLevel(rank.getAnchorLevel());
    }

    /**
     * 设置背景装饰
     *
     * @param position 位置
     * @param bottom   底部边距
     */
    private void setAvatarDecoration(int position, ImageView avatarDecoration, View bottom) {
        int bgRes;
        switch (position) {
            case 0://亚金
                bgRes = R.drawable.ic_runner_up;
                break;
            case 1: //冠军
                bgRes = R.drawable.ic_champion;
                bottom.setVisibility(View.VISIBLE);
                break;
            case 2://季军
                bgRes = R.drawable.ic_third_runner_up;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        avatarDecoration.setBackgroundResource(bgRes);
    }
}
