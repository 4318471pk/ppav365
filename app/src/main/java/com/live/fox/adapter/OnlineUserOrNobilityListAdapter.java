package com.live.fox.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.OnlineUserBean;
import com.live.fox.entity.User;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ImageUtils;
import com.live.fox.utils.ResourceUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.RankProfileView;

import java.util.List;

public class OnlineUserOrNobilityListAdapter extends BaseQuickAdapter<OnlineUserBean, OnlineUserOrNobilityListAdapter.OnlineUserOrNobilityListHolder> {

    Context context;
    public OnlineUserOrNobilityListAdapter(Context context, List<OnlineUserBean> data) {
        super(R.layout.item_online_user_nobility, data);
        this.context=context;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void convert(OnlineUserOrNobilityListHolder helper, OnlineUserBean item) {

        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(getAllIconSpan(item, context));

        helper.tvNickName.setText(item.getNickname());
        helper.tvIcons.setText(spanUtils.create());
        helper.rpv.setIndex(RankProfileView.NONE,item.getVipLevel(),false);
        GlideUtils.loadCircleImage(context,item.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,helper.rpv.getProfileImage());
    }


    public Spanned getAllIconSpan(OnlineUserBean user, Context context) {
        SpanUtils spanUtils = new SpanUtils();

        if(ChatSpanUtils.appendSexIcon(spanUtils,user.getSex(),context,SpanUtils.ALIGN_CENTER))
        {
            spanUtils.appendSpace(ScreenUtils.getDip2px(context,2));
        }

        if(ChatSpanUtils.appendLevelIcon(spanUtils,user.getUserLevel(),context))
        {
            spanUtils.appendSpace(ScreenUtils.getDip2px(context,2));
        }

        if(ChatSpanUtils.appendVipLevelRectangleIcon(spanUtils,user.getVipLevel(),context))
        {
            spanUtils.appendSpace(ScreenUtils.getDip2px(context,2));
        }


        return spanUtils.create();
    }


    public class OnlineUserOrNobilityListHolder extends BaseViewHolder
    {
        TextView tvHuo;
        RankProfileView rpv;
        TextView tvNickName;
        TextView tvIcons;

        public OnlineUserOrNobilityListHolder(View view) {
            super(view);
            tvHuo=view.findViewById(R.id.tvHuo);
            rpv=view.findViewById(R.id.rpv);
            tvNickName=view.findViewById(R.id.tvNickName);
            tvIcons=view.findViewById(R.id.tvIcons);
        }
    }
}
