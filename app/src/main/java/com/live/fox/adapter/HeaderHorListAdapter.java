package com.live.fox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.RoomListBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.view.AnchorRoundImageView;

import java.util.List;

public class HeaderHorListAdapter extends BaseQuickAdapter<RoomListBean, BaseViewHolder> {

    Context context;
    int itemWidth,defaultDrawable,dip10;

    public HeaderHorListAdapter(Context context, @Nullable List<RoomListBean> data) {
        super(R.layout.item_hor_list_home, data);
        this.context=context;
        itemWidth= (int)(ScreenUtils.getScreenWidth(context)*0.27f);
        defaultDrawable=R.mipmap.icon_anchor_loading;
        dip10=ScreenUtils.dp2px(context,10);
    }

    @Override
    protected void convert(BaseViewHolder helper, RoomListBean data) {
        ViewGroup.LayoutParams vl= helper.itemView.getLayoutParams();
        vl.width=itemWidth;
        vl.height=itemWidth;

        AnchorRoundImageView ivRoundBG = helper.getView(R.id.ivRoundBG);
        GlideUtils.loadDefaultImage(mContext, data.getRoomIcon(),defaultDrawable, ivRoundBG);
        ivRoundBG.setRadius(dip10);
        GlideUtils.loadRoundedImage(mContext, dip10,data.getRoomIcon(),0,defaultDrawable, ivRoundBG);
        helper.setText(R.id.tv_nickname,data.getTitle());

        TextView tvNum=helper.getView(R.id.tvNum);
        tvNum.setText(data.getLiveSum()+"");

    }
}
