package com.live.fox.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.LiveFinishActivity;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Follow;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ToastUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class MyFollowListAdapter extends BaseQuickAdapter<Follow, BaseViewHolder> {

    boolean isFans = false;

    public MyFollowListAdapter(List data, boolean isFans) {
        super(R.layout.item_follow_list, data);
        this.isFans = isFans;
    }

    @Override
    protected void convert(BaseViewHolder helper, Follow data) {
//        if (data == null) {
//            return;
//        }
        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(data.getNickname());
        ImageView ivSex = helper.getView(R.id.ivSex);
        if (data.getSex() == 1) {
            ivSex.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.men));
        } else if (data.getSex() == 2) {
            ivSex.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.women));
        }

        RoundedImageView ivHead = helper.getView(R.id.iv_head);
        GlideUtils.loadImage(mContext, data.getAvatar(), ivHead);

        ImageView ivNoble = helper.getView(R.id.iv_noble_level);

        TextView tvGz = helper.getView(R.id.tvGz);
        if (!isFans) { //我的关注

            if (!data.isFans()) {
                tvGz.setText(mContext.getResources().getString(R.string.follow));
                tvGz.setBackground(mContext.getResources().getDrawable(R.drawable.bg_a220f5_e794ff));
            } else {
                tvGz.setText(mContext.getResources().getString(R.string.cancle_gz));
                tvGz.setBackground(mContext.getResources().getDrawable(R.drawable.bg_5a21eb_857ff4));

            }

            tvGz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followFans(data.getUid(), !data.isFans(), helper.getLayoutPosition());
                }
            });

        } else {
            if (!data.isFollow()) {
                tvGz.setText(mContext.getResources().getString(R.string.follow));
                tvGz.setBackground(mContext.getResources().getDrawable(R.drawable.bg_a220f5_e794ff));
            } else {
                tvGz.setText(mContext.getResources().getString(R.string.cancle_gz));
                tvGz.setBackground(mContext.getResources().getDrawable(R.drawable.bg_5a21eb_857ff4));

            }

            tvGz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followFans(data.getUid(), !data.isFollow(), helper.getLayoutPosition());
                }
            });
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailActivity.startActivity(mContext, data.getUid());
            }
        });

    }

    private void followFans(Long id, boolean isFollow, int pos){
        Api_User.ins().followUser(id+"", isFollow, new  JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    if (isFollow) {
                        ToastUtils.showShort(mContext.getString(R.string.successFocus));
                    } else {
                        ToastUtils.showShort(mContext.getString(R.string.cancelFocus));
                    }
                    int temp = pos;
                    for (int i = 0 ; i < mData.size(); i++) {
                        if (id == mData.get(i).getUid()) {
                            temp = i;
                            break;
                        }
                    }
                    if (!isFans) {
                        mData.get(temp).setFans(isFollow);
                    } else {
                        mData.get(temp).setFollow(isFollow);
                    }

                    notifyItemChanged(temp);

                }
            }
        });
    }


//    public interface MyFollowInterFace{
//        public void clickFollow(int pos, boolean cancelFollow, Follow data);
//    }
//
//    private MyFollowInterFace myFollowInterFace;
//
//    public void setMyFollowInterFace(MyFollowInterFace myFollowInterFace) {
//        this.myFollowInterFace = myFollowInterFace;
//    }
}
