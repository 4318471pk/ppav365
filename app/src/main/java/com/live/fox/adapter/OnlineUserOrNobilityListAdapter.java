package com.live.fox.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.User;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ImageUtils;
import com.live.fox.utils.ResourceUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.RankProfileView;

import java.util.List;

public class OnlineUserOrNobilityListAdapter extends BaseQuickAdapter<String, OnlineUserOrNobilityListAdapter.OnlineUserOrNobilityListHolder> {

    Context context;
    public OnlineUserOrNobilityListAdapter(Context context, List<String> data) {
        super(R.layout.item_online_user_nobility, data);
        this.context=context;
    }

    @Override
    protected void convert(OnlineUserOrNobilityListHolder helper, String item) {


        User user=new User();
        user.setSex(1);
        user.setUserLevel(34);
        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(getAllIconSpan(user, context));

        helper.tvNickName.setText("名字");
        helper.tvIcons.setText(spanUtils.create());
        helper.rpv.setIndex(RankProfileView.NONE,48%7);
    }


    public void appendSex(SpanUtils spanUtils, User user, Context context) {
        int sexResId = user.getSex() == 1 ? R.mipmap.men : R.mipmap.women;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), sexResId);
        if (bitmap == null) return;
        spanUtils.appendImage(ImageUtils.scale(bitmap, 41, 39), SpanUtils.ALIGN_BOTTOM);
        spanUtils.append(" ");
    }

    public void appendLevel(SpanUtils spanUtils, Integer userLevel, Context context) {
        if(userLevel==null)return;
        if(userLevel==0)
        {
            userLevel=1;
        }
        if(userLevel>200)
        {
            userLevel=199;
        }

        int index=userLevel%10==0?userLevel/10-1:userLevel/10;
        int[] level = new ResourceUtils().getResourcesID(R.array.level);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), level[index]);
        if (bitmap == null) return;
        Bitmap newBitmap=ImageUtils.addTextForLevel(context,bitmap, userLevel);
        spanUtils.appendImage(newBitmap, SpanUtils.ALIGN_BOTTOM);
    }

    public Spanned getAllIconSpan(User user, Context context) {
        SpanUtils spanUtils = new SpanUtils();
        appendSex(spanUtils,user,context);
        appendLevel(spanUtils, user.getUserLevel(), context);

        spanUtils.appendSpace(ScreenUtils.getDip2px(context,2));
        Bitmap admin = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_admin);
        spanUtils.appendImage(admin, SpanUtils.ALIGN_BOTTOM);

        spanUtils.appendSpace(ScreenUtils.getDip2px(context,2));
        Bitmap shouhu = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_small_shouhu);
        spanUtils.appendImage(shouhu, SpanUtils.ALIGN_BOTTOM);

        spanUtils.appendSpace(ScreenUtils.getDip2px(context,2));
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_beatiful);
        spanUtils.appendImage(bitmap, SpanUtils.ALIGN_BOTTOM);
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
