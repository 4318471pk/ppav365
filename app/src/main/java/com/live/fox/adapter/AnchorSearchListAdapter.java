package com.live.fox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.SearchAnchorBean;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.RankProfileView;

import java.util.List;

public class AnchorSearchListAdapter extends BaseQuickAdapter<SearchAnchorBean, BaseViewHolder> {

    String followed,follow;
    Context context;
    OnClickFollowListener onClickFollowListener;

    public AnchorSearchListAdapter(Context context, @Nullable @org.jetbrains.annotations.Nullable List<SearchAnchorBean> data) {
        super(R.layout.item_search_anchor, data);
        this.context=context;
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

    @Override
    protected void convert(BaseViewHolder helper, SearchAnchorBean item) {
        helper.setText(R.id.tvName,item.getNickname());
        helper.setText(R.id.tvSignature,item.getSignature());

        RankProfileView rpvView=helper.itemView.findViewById(R.id.rpvView);
        TextView tvFollow=helper.itemView.findViewById(R.id.tvFollow);
        TextView tvIcons=helper.itemView.findViewById(R.id.tvIcons);

        tvFollow.setVisibility(View.VISIBLE);
        tvFollow.setSelected(item.isFollow());
        tvFollow.setText(item.isFollow()?followed:follow);
        tvFollow.setEnabled(!item.isFollow());
        tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickFollowListener!=null)
                {
                    onClickFollowListener.onClickFollow(item);
                }
            }
        });

        SpanUtils spanUtils=new SpanUtils();

        if(ChatSpanUtils.appendSexIcon(spanUtils,item.getUserLevel(), context, SpanUtils.ALIGN_CENTER))
        {
            spanUtils.append(" ");
        }

        if(ChatSpanUtils.appendLevelIcon(spanUtils,item.getUserLevel(), context))
        {
            spanUtils.append(" ");
        }

        if(ChatSpanUtils.appendVipLevelRectangleIcon(spanUtils,item.getVipLevel(), context))
        {
            spanUtils.append(" ");
        }
        tvIcons.setText(spanUtils.create());

        rpvView.setIndex(RankProfileView.NONE,item.getVipLevel(),false);
        GlideUtils.loadCircleImage(context,item.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,rpvView.getProfileImage());

    }

    public interface OnClickFollowListener
    {
        void onClickFollow(SearchAnchorBean bean);
        void onClickProfileImage(SearchAnchorBean bean);
    }
}
