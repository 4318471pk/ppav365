package com.live.fox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.Audience;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.view.ProfileWithIconsView;

import java.util.List;

public class LivingTop20OnlineUserAdapter extends BaseQuickAdapter<Audience, BaseViewHolder> {

    Context context;
    int errorPicResourece;
    int itemWH;

    public LivingTop20OnlineUserAdapter(Context context, @Nullable List<Audience> data) {
        super(R.layout.item_living_top20_audience, data);
        this.context=context;
        this.errorPicResourece=R.mipmap.user_head_error;
        itemWH=ScreenUtils.dp2px(context,34);
    }

    @Override
    protected void convert(BaseViewHolder helper, Audience item) {
        ViewGroup.LayoutParams vl= helper.itemView.getLayoutParams();
        vl.height=itemWH;
        vl.width=itemWH;
        helper.itemView.setLayoutParams(vl);
        ProfileWithIconsView ivAvatar= helper.getView(R.id.ivAvatar);
        RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams) ivAvatar.getLayoutParams();
        rl.width=itemWH;
        rl.height=itemWH;
        ivAvatar.setLayoutParams(rl);
        ivAvatar.setData(item.getVipLevel(),item.getGuardLevel(),item.getAvatar());
//        GlideUtils.loadCircleImage(context,item.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,ivAvatar);
    }
}
