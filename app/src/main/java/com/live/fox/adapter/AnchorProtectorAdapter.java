package com.live.fox.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.RankIndexBean;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.RankProfileView;

import java.util.List;

public class AnchorProtectorAdapter extends BaseQuickAdapter<String, AnchorProtectorAdapter.ProtectListHolder> {

    Activity context;

    public AnchorProtectorAdapter(Activity context,  List<String> data) {
        super(R.layout.item_protect_anchor_list, data);
        this.context=context;
    }

    public void notifyData(List data)
    {
        this.replaceData(data);
    }

    @Override
    protected void convert(ProtectListHolder helper, String item) {
        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(48, context));

        helper.tvNickName.setText("名字");
        helper.tvIcons.setText(spanUtils.create());
        helper.rpv.setIndex(RankProfileView.NONE,48%7);
    }


    public class ProtectListHolder extends BaseViewHolder
    {
        TextView tvHuo;
        RankProfileView rpv;
        TextView tvNickName;
        TextView tvIcons;

        public ProtectListHolder(View view) {
            super(view);
            tvHuo=view.findViewById(R.id.tvHuo);
            rpv=view.findViewById(R.id.rpv);
            tvNickName=view.findViewById(R.id.tvNickName);
            tvIcons=view.findViewById(R.id.tvIcons);
        }
    }
}
