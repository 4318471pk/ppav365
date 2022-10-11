package com.live.fox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.ContributionRankIndexBean;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.RankProfileView;

import java.util.List;

public class ContributionRankAdapter extends BaseQuickAdapter<ContributionRankIndexBean, ContributionRankAdapter.RankViewHold> {

    Context context;

    public ContributionRankAdapter(Context context,List<ContributionRankIndexBean> data) {
        super(R.layout.item_contribution_rank_profile, data);
        this.context=context;

    }

    @Override
    protected void convert(RankViewHold helper, ContributionRankIndexBean item) {
        helper.tvFollow.setSelected(item.isFollow());
        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(item.getNickName());
        spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(item.getLevel(), context));
        helper.tvNickName.setText(spanUtils.create());
        helper.tvHuo.setText(item.getHuo());
        helper.rpv.setIndex(-1,item.getLevel()%7);
        helper.tvIndex.setText(String.valueOf(helper.getLayoutPosition()+3));
    }

    public class RankViewHold extends BaseViewHolder
    {
        TextView tvFollow;
        RankProfileView rpv;
        TextView tvNickName;
        TextView tvHuo;
        TextView tvIndex;

        public RankViewHold(View view) {
            super(view);
            tvFollow=view.findViewById(R.id.tvFollow);
            rpv=view.findViewById(R.id.rpv);
            tvNickName=view.findViewById(R.id.tvNickName);
            tvHuo=view.findViewById(R.id.tvHuo);
            tvIndex=view.findViewById(R.id.tvIndex);
        }
    }
}
