package com.live.fox.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.RankItemBean;
import com.live.fox.entity.SearchAnchorBean;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.RankProfileView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RankAdapter extends BaseQuickAdapter<RankItemBean, RankAdapter.RankViewHold> {

    Activity context;
    String templeText;
    String emptyPosition,followed,follow;
    OnClickFollowListener onClickFollowListener;

    public RankAdapter(Activity context, @Nullable List data) {
        super(R.layout.item_rank_profile, data);
        this.context=context;
        templeText=context.getResources().getString(R.string.tip10);
        emptyPosition=context.getResources().getString(R.string.emptyPosition);
        follow=context.getResources().getString(R.string.follow);
        followed=context.getResources().getString(R.string.followed);
        setHasStableIds(true);

    }

    public void setOnClickFollowListener(OnClickFollowListener onClickFollowListener) {
        this.onClickFollowListener = onClickFollowListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyData(List data)
    {
        this.replaceData(data);
    }

    @Override
    protected void convert(RankViewHold helper, RankItemBean item) {

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
                    if(onClickFollowListener!=null)
                    {
                        onClickFollowListener.onClickFollow(getData().get(tag),tag);
                    }
                }
            });

            SpanUtils spanUtils=new SpanUtils();
            spanUtils.append(item.getNickname());
            if(ChatSpanUtils.appendLevelIcon(spanUtils,item.getUserLevel(), context))
            {
                spanUtils.append(" ");
            }
            if(ChatSpanUtils.appendVipLevelRectangleIcon(spanUtils,item.getVipLevel(), context))
            {
                spanUtils.append(" ");
            }

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
                    if(onClickFollowListener!=null)
                    {
                        onClickFollowListener.onClickProfileImage(getData().get(tag),tag);
                    }
                }
            });
        }
        else
        {
            helper.tvIndex.setText(String.valueOf(helper.getLayoutPosition()+3));
            helper.tvFollow.setVisibility(View.INVISIBLE);
            helper.tvHuo.setVisibility(View.GONE);
            helper.rpv.setIndex(RankProfileView.NONE,RankProfileView.NONE,false);
            helper.tvNickName.setText(emptyPosition);
            helper.rpv.getProfileImage().setImageDrawable(context.getResources().getDrawable(R.mipmap.user_head_error));
        }


    }

    @Override
    public void onViewRecycled(@NonNull @NotNull RankViewHold holder) {
        super.onViewRecycled(holder);
        if(holder.rpv!=null)
        {
            holder.rpv.stopLivingAnimation();
        }
    }

    @Override
    public void onViewAttachedToWindow(RankViewHold holder) {
        super.onViewAttachedToWindow(holder);
        if(holder.rpv!=null)
        {
            holder.rpv.startLivingAnimation();
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

    public interface OnClickFollowListener
    {
        void onClickFollow(RankItemBean bean,int position);
        void onClickProfileImage(RankItemBean bean,int position);
    }
}
