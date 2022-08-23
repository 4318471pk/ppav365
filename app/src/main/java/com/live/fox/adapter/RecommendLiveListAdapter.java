package com.live.fox.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.Anchor;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;


/**
 * 直播界面
 * 推荐列表适配器
 */
public class RecommendLiveListAdapter extends BaseQuickAdapter<Anchor, BaseViewHolder> {

    public RecommendLiveListAdapter() {
        super(R.layout.item_recommend_live);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Anchor recommendLiveEntity) {
        ImageView cover = baseViewHolder.getView(R.id.recommend_live_cover);
        LogUtils.i("Avatar " + recommendLiveEntity.getAvatar());
        GlideUtils.loadDefaultRoundedImage(mContext, recommendLiveEntity.getAvatar(), cover);

        baseViewHolder.setText(R.id.recommend_live_name, recommendLiveEntity.getNickname());

        baseViewHolder.setText(R.id.recommend_live_audience, String.valueOf(recommendLiveEntity.getRq()));
    }
}
