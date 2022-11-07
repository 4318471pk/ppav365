package com.live.fox.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.entity.RankIndexBean;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.RankProfileView;

import java.util.List;

public class RankAdapter extends BaseQuickAdapter<RankIndexBean, RankAdapter.RankViewHold> {

    Activity context;

    public RankAdapter(Activity context, @Nullable List data) {
        super(R.layout.item_rank_profile, data);
        this.context=context;

    }

    public void notifyData(List data)
    {
        this.replaceData(data);
    }

    @Override
    protected void convert(RankViewHold helper, RankIndexBean item) {

        helper.tvFollow.setSelected(item.isFollow());
        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(item.getNickName());
        spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(item.getLevel(), context));
        helper.tvNickName.setText(spanUtils.create());
        helper.tvHuo.setText(item.getHuo());
        helper.rpv.setIndex(RankProfileView.NONE,item.getLevel()%7,false);
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
