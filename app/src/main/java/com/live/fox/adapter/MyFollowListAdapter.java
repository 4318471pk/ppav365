package com.live.fox.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Follow;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;
import com.live.fox.view.RankProfileView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MyFollowListAdapter extends BaseQuickAdapter<Follow, BaseViewHolder> {

    boolean isFans = false;
    String followed,follow;
    Context context;
    OnClickListener onClickListener;


    public MyFollowListAdapter(List data, boolean isFans, Context context) {
        super(R.layout.item_follow_list, data);
        this.isFans = isFans;
        this.context=context;
        follow=context.getResources().getString(R.string.follow);
        followed=context.getResources().getString(R.string.followed);

        setHasStableIds(true);
    }

    public void setOnCancelFollowListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, Follow data) {
//        if (data == null) {
//            return;
//        }
        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(data.getNickname());


        TextView tvIcons=helper.itemView.findViewById(R.id.tvIcons);
        SpanUtils spanUtils=new SpanUtils();

        ChatSpanUtils.appendSexIcon(spanUtils,data.getSex(),context,SpanUtils.ALIGN_BASELINE);
        spanUtils.append(" ");

        ChatSpanUtils.appendLevelIcon(spanUtils,data.getUserLevel(), context);
        spanUtils.append(" ");

        ChatSpanUtils.appendVipLevelRectangleIcon(spanUtils,data.getVipLevel(), context);
        tvIcons.setText(spanUtils.create());

        RankProfileView rpvView=helper.itemView.findViewById(R.id.rpvView);
        rpvView.setIndex(RankProfileView.NONE,data.getVipLevel(),data.getBroadcast());
        GlideUtils.loadCircleImage(context,data.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,rpvView.getProfileImage());
        rpvView.setTag(helper.getLayoutPosition()-getHeaderLayoutCount());
        rpvView.setOnClickListener(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {
                if(onClickListener!=null)
                {
                    onClickListener.onClickProfile((int)view.getTag());
                }
            }
        });

        TextView tvGz = helper.getView(R.id.tvGz);

        tvGz.setText(!data.isFollow()?followed:follow);

        if (!isFans) { //我的关注
//            if (!data.isFans()) {
//                tvGz.setText(mContext.getResources().getString(R.string.follow));
//                tvGz.setBackground(mContext.getResources().getDrawable(R.drawable.bg_a220f5_e794ff));
//            } else {
//
//
//            }
//                tvGz.setText(mContext.getResources().getString(R.string.cancle_gz));
            tvGz.setBackground(mContext.getResources().getDrawable(R.drawable.bg_5a21eb_857ff4));
            tvGz.setTag(helper.getLayoutPosition()-getHeaderLayoutCount());

            tvGz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean isFollow=data.isFollow();
                    data.setFollow(!data.isFollow());

                    if(onClickListener!=null)
                    {
                        onClickListener.onCancelFollow(data.getUid(),(int)v.getTag(),isFollow);
                    }
                }
            });



        } else {
//            if (!data.isFollow()) {
//                tvGz.setText(mContext.getResources().getString(R.string.follow));
//                tvGz.setBackground(mContext.getResources().getDrawable(R.drawable.bg_a220f5_e794ff));
//            } else {
//                tvGz.setText(mContext.getResources().getString(R.string.cancle_gz));
//                tvGz.setBackground(mContext.getResources().getDrawable(R.drawable.bg_5a21eb_857ff4));
//            }

            tvGz.setBackground(mContext.getResources().getDrawable(R.drawable.bg_5a21eb_857ff4));
            tvGz.setTag(helper.getAdapterPosition()-getHeaderLayoutCount());

            tvGz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isFollow=data.isFollow();
                    data.setFollow(!data.isFollow());

                    if(onClickListener!=null)
                    {
                        onClickListener.onCancelFollow(data.getUid(),(int)tvGz.getTag(),isFollow);

                    }

//                        tvGz.setText(follow);
                }
            });


        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Strings.isDigitOnly(data.getUid()))
                {
                    UserDetailActivity.startActivity(mContext, Long.valueOf(data.getUid()));
                }

            }
        });

    }


    public interface OnClickListener
    {
        void onCancelFollow(String uid,int pos, boolean isFollow);
        void onClickProfile(int pos);
    }
}
