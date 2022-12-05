package com.live.fox.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.RoomListBean;
import com.live.fox.svga.AnchorInfoBean;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.ImageUtils;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.view.AnchorRoundImageView;
import com.live.fox.view.GradientTextView;
import com.live.fox.view.RecommendAnchorListFooter;

import java.util.List;

/**
 * 主页直播适配器
 */
public class LiveListAdapter extends BaseSectionQuickAdapter<RoomListBean, LiveListAdapter.LiveListViewHold> {

    List<Anchor> bannerAdList;
    int itemWidth;
    Context context;
    Bitmap clock,diamond,ticket;
    int defaultDrawable;
    int dip10,dip13,diamondWidth,diamondHeight;

    public LiveListAdapter(Context context,List<RoomListBean> data) {
        super(R.layout.item_anchor_list, R.layout.item_liveroomlist_adbanner, data);
        this.context=context;
        itemWidth= (ScreenUtils.getScreenWidth(context))/2;
        ticket= BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_ticket);
        clock= BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_clock);
        diamond=BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_diamond);
        defaultDrawable=R.mipmap.icon_anchor_loading;
        dip10= ScreenUtils.dp2px(context,10);
        dip13= ScreenUtils.dp2px(context,13);
        diamondWidth= ScreenUtils.dp2px(context,12.5f);
        diamondHeight= ScreenUtils.dp2px(context,9.5f);
    }

    @Override
    protected void convertHead(LiveListViewHold helper, final RoomListBean dataBean) {

        if (bannerAdList == null) return;

        int index = (helper.getLayoutPosition() / 6) - 1;
        if (index < 0 || index > bannerAdList.size() - 1) return;

        GlideUtils.loadDefaultRoundedImage(mContext, bannerAdList.get(index).getContent(), helper.getView(R.id.iv_));
    }

    @Override
    protected void convert(LiveListViewHold helper, RoomListBean data) {
        if (data == null) return;

        ViewGroup.LayoutParams vl= helper.itemView.getLayoutParams();
        vl.width=itemWidth;
        vl.height=itemWidth;
        helper.itemView.setLayoutParams(vl);
        int position=helper.getLayoutPosition()-getHeaderLayoutCount();
        int right=position%2==0?0:dip10/2;

        helper.itemView.setPadding(dip10/2,dip10/2,right,dip10/2);

        GradientTextView gtvUnitPrice = helper.getView(R.id.gtvUnitPrice);  //类别
        GradientTextView tvAnchorPaymentType=helper.getView(R.id.tvAnchorPaymentType);
        AnchorRoundImageView ivRoundBG = helper.getView(R.id.ivRoundBG);
        TextView tvNum=helper.getView(R.id.tvNum);
        tvNum.setText(data.getLiveSum()+"");

        helper.setText(R.id.tv_nickname,data.getTitle());

        ivRoundBG.setRadius(dip10);
        GlideUtils.loadRoundedImage(mContext, dip10,data.getRoomIcon(),0,defaultDrawable, ivRoundBG);

        setPaymentType(data,gtvUnitPrice,tvAnchorPaymentType);
    }


    private void setPaymentType(RoomListBean data,GradientTextView gtvUnitPrice,GradientTextView tvAnchorPaymentType)
    {
        SpanUtils spUtils=new SpanUtils();
        //房间类型:0普通房间 1计时付费 2场次付费
        switch (data.getRoomType())
        {
            case 0:
                tvAnchorPaymentType.setVisibility(View.INVISIBLE);
                gtvUnitPrice.setVisibility(View.INVISIBLE);
                break;
            case 1:
                gtvUnitPrice.setSolidBackground(0x4c000000, com.live.fox.utils.ScreenUtils.dp2px(context,7.5f));
                spUtils.appendImage(ImageUtils.scale(clock, dip13, dip13),SpanUtils.ALIGN_CENTER);
                spUtils.append(" ").append(data.getRoomPrice()).setAlign(Layout.Alignment.ALIGN_CENTER).append(" ");

                spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_CENTER);
                spUtils.append(context.getResources().getString(R.string.unitPriceMin)).setAlign(Layout.Alignment.ALIGN_CENTER);
                gtvUnitPrice.setText(spUtils.create());
                gtvUnitPrice.setVisibility(View.VISIBLE);
                gtvUnitPrice.setSolidBackground(0x4c000000, com.live.fox.utils.ScreenUtils.dp2px(context,7.5f));

                tvAnchorPaymentType.setVisibility(View.VISIBLE);
                tvAnchorPaymentType.setText(context.getResources().getString(R.string.charge_on_time));
                tvAnchorPaymentType.setSolidBackground(0x4c000000, com.live.fox.utils.ScreenUtils.dp2px(context,7.5f));

                break;
            case 2:
                gtvUnitPrice.setSolidBackground(0x4cBF003A, com.live.fox.utils.ScreenUtils.dp2px(context,7.5f));

                gtvUnitPrice.setSolidBackground(0x4c000000, com.live.fox.utils.ScreenUtils.dp2px(context,10));
                spUtils.appendImage(ImageUtils.scale(ticket, dip13, dip13),SpanUtils.ALIGN_CENTER);
                spUtils.append(" ").append(data.getRoomPrice()).setFontSize(11,true).setAlign(Layout.Alignment.ALIGN_CENTER).append(" ");

                spUtils.appendImage(ImageUtils.scale(diamond, diamondWidth, diamondHeight),SpanUtils.ALIGN_CENTER);
                spUtils.append(context.getResources().getString(R.string.unitPriceShow)).setFontSize(11,true).setAlign(Layout.Alignment.ALIGN_CENTER);
                gtvUnitPrice.setText(spUtils.create());
                gtvUnitPrice.setVisibility(View.VISIBLE);
                gtvUnitPrice.setSolidBackground(0x4cBF003A, com.live.fox.utils.ScreenUtils.dp2px(context,7.5f));

                tvAnchorPaymentType.setVisibility(View.VISIBLE);
                tvAnchorPaymentType.setText(context.getString(R.string.charge_per_site));
                tvAnchorPaymentType.setSolidBackground(0x4cBF003A, com.live.fox.utils.ScreenUtils.dp2px(context,7.5f));

                break;
            default:
                tvAnchorPaymentType.setVisibility(View.GONE);
                gtvUnitPrice.setVisibility(View.GONE);
        }
    }

    public static class LiveListViewHold extends BaseViewHolder{

        public LiveListViewHold(View view) {
            super(view);
        }

    }
}
