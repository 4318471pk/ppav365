package com.live.fox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.ContributionRankItemBean;
import com.live.fox.entity.RankItemBean;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.RankProfileView;

import java.util.List;

public class ContributionRankAdapter extends BaseQuickAdapter<ContributionRankItemBean, ContributionRankAdapter.RankViewHold> {

    Context context;
    String templeText;
    String emptyPosition,followed,follow;
    OnClickItemListener onClickItemListener;

    public ContributionRankAdapter(Context context,List<ContributionRankItemBean> data) {
        super(R.layout.item_contribution_rank_profile, data);
        this.context=context;
        templeText=context.getResources().getString(R.string.tip10);
        emptyPosition=context.getResources().getString(R.string.emptyPosition);
        follow=context.getResources().getString(R.string.follow);
        followed=context.getResources().getString(R.string.followed);

    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    @Override
    protected void convert(RankViewHold helper, ContributionRankItemBean item) {
        if(item!=null)
        {
            helper.tvFollow.setVisibility(View.VISIBLE);
            helper.tvFollow.setSelected(item.isFollow());
            helper.tvFollow.setText(item.isFollow()?followed:follow);
            helper.tvFollow.setEnabled(!item.isFollow());
            helper.tvFollow.setTag(helper.getAdapterPosition()-getHeaderLayoutCount());
            helper.tvFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag=(int)v.getTag();
                    if(onClickItemListener!=null)
                    {
                        onClickItemListener.onClickFollow(getData().get(tag),tag);
                    }
                }
            });

            SpanUtils spanUtils=new SpanUtils();
            spanUtils.append(item.getNickname());
            ChatSpanUtils.appendLevelIcon(spanUtils,item.getUserLevel(), context);
            helper.tvNickName.setText(spanUtils.create());
            helper.tvHuo.setText(String.format(templeText,item.getRankValue()+""));
            helper.tvHuo.setVisibility(View.VISIBLE);
            helper.rpv.setIndex(RankProfileView.NONE,RankProfileView.NONE,false);
            GlideUtils.loadCircleImage(context,item.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,helper.rpv.getProfileImage());
            helper.tvIndex.setText(String.valueOf(helper.getLayoutPosition()+3));

            helper.rpv.setTag(helper.getAdapterPosition()-getHeaderLayoutCount());
            helper.rpv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag=(int)v.getTag();
                    if(onClickItemListener!=null)
                    {
                        onClickItemListener.onClickProfileImage(getData().get(tag),tag);
                    }
                }
            });
        }
        else
        {
            helper.tvIndex.setText(String.valueOf(helper.getLayoutPosition()+3));
            helper.tvFollow.setVisibility(View.INVISIBLE);


            helper.tvFollow.setSelected(false);
            helper.tvFollow.setText(follow);
            helper.tvFollow.setEnabled(false);


            helper.tvHuo.setVisibility(View.GONE);
            helper.rpv.setIndex(RankProfileView.NONE,RankProfileView.NONE,false);
            helper.tvNickName.setText(emptyPosition);
            helper.rpv.getProfileImage().setImageDrawable(context.getResources().getDrawable(R.mipmap.user_head_error));
        }
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

    public interface OnClickItemListener
    {
        void onClickFollow(ContributionRankItemBean bean, int position);
        void onClickProfileImage(ContributionRankItemBean bean,int position);
    }
}
