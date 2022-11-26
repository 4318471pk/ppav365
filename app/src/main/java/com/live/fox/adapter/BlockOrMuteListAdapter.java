package com.live.fox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.BlackOrMuteListItemBean;
import com.live.fox.entity.SearchAnchorBean;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.TimeUtils;
import com.live.fox.view.RankProfileView;

import java.util.List;

public class BlockOrMuteListAdapter extends BaseQuickAdapter<BlackOrMuteListItemBean, BlockOrMuteListAdapter.BlockAndMuteListHolder> {

    Context context;
    OnClickRemoveListener onClickRemoveListener;

    public BlockOrMuteListAdapter(Context context, List<BlackOrMuteListItemBean> data) {
        super(R.layout.item_block_mute_list, data);
        this.context=context;
    }

    public void setOnClickRemoveListener(OnClickRemoveListener onClickRemoveListener) {
        this.onClickRemoveListener = onClickRemoveListener;
    }

    @Override
    protected void convert(BlockAndMuteListHolder helper, BlackOrMuteListItemBean item) {

        SpanUtils spanUtils=new SpanUtils();
        if(ChatSpanUtils.appendSexIcon(spanUtils,item.getSex(),context, SpanUtils.ALIGN_CENTER))
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

        if(ChatSpanUtils.appendGuardIcon(spanUtils,item.getGuardLevel(),context))
        {
            spanUtils.append(" ");
        }

        helper.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickRemoveListener!=null)
                {
                    onClickRemoveListener.onClickFollow(item);
                }
            }
        });
        helper.tvNickName.setText(item.getNickname());
        helper.tvIcons.setText(spanUtils.create());
        if(item.getCreateTime()>0)
        {
            helper.tvTime.setText(TimeUtils.long2String(item.getCreateTime(),"yyyy.MM.dd HH:mm:ss"));
        }

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(item.getOperator()).append(" ").append(context.getString(R.string.handle));
        helper.tvAdminName.setText(stringBuilder.toString());
        helper.rpv.setIndex(RankProfileView.NONE, item.getVipLevel(),false);
        GlideUtils.loadCircleImage(context,item.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,helper.rpv.getProfileImage());
    }

    public static class BlockAndMuteListHolder extends BaseViewHolder
    {
        TextView tvRemove = null;
        RankProfileView rpv = null;
        TextView tvNickName = null;
        TextView tvIcons = null;
        TextView tvTime=null;
        TextView tvAdminName;
        public BlockAndMuteListHolder(View view) {
            super(view);
            tvTime=view.findViewById(R.id.tvTime);
            tvRemove = view.findViewById(R.id.tvRemove);
             rpv = view.findViewById(R.id.rpv);
             tvNickName = view.findViewById(R.id.tvNickName);
             tvIcons = view.findViewById(R.id.tvIcons);
            tvAdminName=view.findViewById(R.id.tvAdminName);
        }
    }

    public interface OnClickRemoveListener
    {
        void onClickFollow(BlackOrMuteListItemBean bean);
    }
}
