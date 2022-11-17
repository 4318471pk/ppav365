package com.live.fox.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.AnchorGuardListBean;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.view.RankProfileView;

import java.util.List;

public class AnchorProtectorAdapter extends BaseQuickAdapter<AnchorGuardListBean.LiveGuardBean, AnchorProtectorAdapter.ProtectListHolder> {

    Activity context;

    public AnchorProtectorAdapter(Activity context,  List<AnchorGuardListBean.LiveGuardBean> data) {
        super(R.layout.item_protect_anchor_list, data);
        this.context=context;
    }

    public void notifyData(List data)
    {
        this.replaceData(data);
    }

    @Override
    protected void convert(ProtectListHolder helper, AnchorGuardListBean.LiveGuardBean item) {
        SpanUtils spanUtils=new SpanUtils();
        ChatSpanUtils.appendLevelIcon(spanUtils,item.getUserLevel(), context);
        ChatSpanUtils.appendVipLevelRectangleIcon(spanUtils,item.getVipLevel(), context);
        helper.tvNickName.setText(item.getNickname());
        helper.tvIcons.setText(spanUtils.create());

//        StringBuilder stringBuilder=new StringBuilder();
//        stringBuilder.append(context.getString(R.string.gongxianzhi)).append(item.get);
//        helper.tvHuo.setText();
        helper.rpv.setIndex(RankProfileView.NONE,item.getVipLevel(),false);
        GlideUtils.loadCircleImage(context,item.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,helper.rpv.getProfileImage());
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
