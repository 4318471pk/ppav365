package com.live.fox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.RankProfileView;

import java.util.List;

public class BlockOrMuteListAdapter extends BaseQuickAdapter<String, BlockOrMuteListAdapter.BlockAndMuteListHolder> {

    Context context;

    public BlockOrMuteListAdapter(Context context, List<String> data) {
        super(R.layout.item_block_mute_list, data);
        this.context=context;
    }

    @Override
    protected void convert(BlockAndMuteListHolder helper, String item) {

        SpanUtils spanUtils = new SpanUtils();
        spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(48, context));

        helper.tvNickName.setText("名字");
        helper.tvIcons.setText(spanUtils.create());
        helper.rpv.setIndex(RankProfileView.NONE, 48 % 7,false);
    }

    public static class BlockAndMuteListHolder extends BaseViewHolder
    {
        TextView tvRemove = null;
        RankProfileView rpv = null;
        TextView tvNickName = null;
        TextView tvIcons = null;
        public BlockAndMuteListHolder(View view) {
            super(view);

            tvRemove = view.findViewById(R.id.tvRemove);
             rpv = view.findViewById(R.id.rpv);
             tvNickName = view.findViewById(R.id.tvNickName);
             tvIcons = view.findViewById(R.id.tvIcons);
        }
    }
}
