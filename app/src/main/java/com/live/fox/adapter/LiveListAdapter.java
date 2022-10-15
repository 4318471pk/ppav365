package com.live.fox.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.entity.Anchor;
import com.live.fox.svga.AnchorInfoBean;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.GradientTextView;

import java.util.List;

/**
 * 主页直播适配器
 */
public class LiveListAdapter extends BaseSectionQuickAdapter<AnchorInfoBean, LiveListAdapter.LiveListViewHold> {

    List<Anchor> bannerAdList;
    int itemWidth;
    Context context;
    Drawable clock,diamond;


    public LiveListAdapter(Context context,List<AnchorInfoBean> data) {
        super(R.layout.item_anchor_list, R.layout.item_liveroomlist_adbanner, data);
        itemWidth= (ScreenUtils.getScreenWidth(context)-ScreenUtils.getDip2px(context,15))/2;
        clock=context.getResources().getDrawable(R.mipmap.icon_clock);
        diamond=context.getResources().getDrawable(R.mipmap.icon_diamond);
    }

    @Override
    protected void convertHead(LiveListViewHold helper, final AnchorInfoBean dataBean) {

        if (bannerAdList == null) return;

        int index = (helper.getLayoutPosition() / 6) - 1;
        if (index < 0 || index > bannerAdList.size() - 1) return;

        GlideUtils.loadDefaultRoundedImage(mContext, bannerAdList.get(index).getContent(), helper.getView(R.id.iv_));
        helper.getView(R.id.iv_).setOnClickListener(view -> {
            Anchor banner = bannerAdList.get(index);
            if (banner != null && !StringUtils.isEmpty(banner.getJumpUrl())) {
                if (banner.getOpenWay() == 1) {
                    IntentUtils.toBrowser(mContext, banner.getJumpUrl());
                } else {
                    FragmentContentActivity.startWebActivity(mContext, "", banner.getJumpUrl());
                }
            }
        });
    }

    @Override
    protected void convert(LiveListViewHold helper, AnchorInfoBean data) {
        Anchor anchor = data.t;
        if (anchor == null) return;

        ViewGroup.LayoutParams vl= helper.itemView.getLayoutParams();
        vl.width=itemWidth;
        vl.height=itemWidth;

        GradientTextView gtvUnitPrice = helper.getView(R.id.gtvUnitPrice);  //类别
        ImageView ivRoundBG = helper.getView(R.id.ivRoundBG);

        SpanUtils spUtils=new SpanUtils();
        spUtils.appendImage(clock,SpanUtils.ALIGN_CENTER);
        spUtils.append(" 21 ").setAlign(Layout.Alignment.ALIGN_CENTER);
        spUtils.appendImage(diamond,SpanUtils.ALIGN_CENTER);
        spUtils.append("/分钟").setAlign(Layout.Alignment.ALIGN_CENTER);
        gtvUnitPrice.setText(spUtils.create());

//        //Views
//        TextView category = helper.getView(R.id.tv_cai_category);  //类别
//        ImageView roomStata = helper.getView(R.id.layout_living);
//
//        // 0普通房间 1广告房间
//        helper.setText(R.id.tv_num, RegexUtils.formatNumber(anchor.getRq()));
//        if (anchor.getIsAd() == 1) {
//            category.setVisibility(View.GONE);
//            roomStata.setVisibility(View.INVISIBLE);
//            helper.getView(R.id.iv_joytd).setVisibility(View.INVISIBLE);
//        } else {
//            //普通直播间
//            if (anchor.getLiveStartLottery() == null || anchor.getLiveStartLottery().size() == 0) {
//                category.setVisibility(View.GONE);
//            } else {
//                category.setVisibility(View.VISIBLE);
//                String str;
//                if (anchor.getLiveStartLottery().size() == 2) {
//                    if (BuildConfig.AppFlavor.equals("QQLive")) {
//                        str = anchor.getLiveStartLottery().get(0).getLotteryName() + " / " +
//                                anchor.getLiveStartLottery().get(1).getLotteryName();
//                    } else {
//                        str = anchor.getLiveStartLottery().get(0).getCpName() + " / " +
//                                anchor.getLiveStartLottery().get(1).getCpName();
//                    }
//                } else {
//                    if (BuildConfig.AppFlavor.equals("QQLive")) {
//                        str = anchor.getLiveStartLottery().get(0).getLotteryName();
//                    } else {
//                        str = anchor.getLiveStartLottery().get(0).getCpName();
//                    }
//                }
//                category.setText(str);
//            }
//
//            //跳蛋房间
//            if (anchor.getToy() == 1) {
//                helper.getView(R.id.iv_joytd).setVisibility(View.VISIBLE);
//            } else {
//                helper.getView(R.id.iv_joytd).setVisibility(View.GONE);
//            }
//
//            Drawable drawable;
//            //房间类型
//            switch (anchor.getType()) {
//                case 0: //普通房间
//                    roomStata.setVisibility(View.GONE);
//                    drawable = null;
//                    break;
//                case 1: // 记时
//                    drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_times);
//                    break;
//
//                case 2: //付费
//                    roomStata.setVisibility(View.VISIBLE);
//                    drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_frequency);
//                    break;
//
//                case 3: //是否需要密码
//                    drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_live_room_lock);
//                    break;
//
//                default:
//                    drawable = null;
//                    break;
//            }
//            roomStata.setImageDrawable(drawable);
//        }
//
//        TextView anchorDetail = helper.getView(R.id.tv_sign);
//        String anchorDetailStr;
//        if (anchor.getIsAd() == 1) {   //广告直播间
//            helper.getView(R.id.tv_num).setVisibility(View.GONE);
//            helper.getView(R.id.tv_nickname).setVisibility(View.GONE);
//            anchorDetailStr = anchor.getNickname();
//        } else {   //正常直播间
//
//            helper.getView(R.id.tv_num).setVisibility(View.VISIBLE);
//            if (StringUtils.isEmpty(anchor.getSignature())) {
//                helper.getView(R.id.tv_nickname).setVisibility(View.GONE);
//                anchorDetailStr = anchor.getNickname();
//            } else {
//                helper.getView(R.id.tv_nickname).setVisibility(View.VISIBLE);
//                helper.setText(R.id.tv_nickname, anchor.getNickname());
//                anchorDetailStr = anchor.getSignature();
//            }
//        }
//
//        if (TextUtils.isEmpty(anchor.getNickname())) {
//            anchorDetail.setVisibility(View.GONE);
//        } else {
//            anchorDetail.setText(anchorDetailStr);
//        }
//
//        GlideUtils.loadDefaultRoundedImage(mContext, anchor.getAvatar(), helper.getView(R.id.iv_cover));
    }


    //type 1热门
    public void setData(int type, List<AnchorInfoBean> data) {
        int size1 = data.size();
        if (type == 1) {
            String content = SPUtils.getInstance("ad_banner2").getString("content");
            LogUtils.e("二级Banner:" + content);
            if (!StringUtils.isEmpty(content)) {
                bannerAdList = GsonUtil.getObjects(content, Anchor[].class);
                for (int i = 0; i < bannerAdList.size(); i++) {
                    Anchor bannerAd = bannerAdList.get(i);
                    bannerAd.setRoomType(2);
                    int index = (i + 1) * 7 - 1;
                    if (index <= data.size() - 1) {
                        data.add(index, new AnchorInfoBean(true));
                    }
                }
            }
        }
        LogUtils.e("size变化: " + size1 + ", " + data.size());
        setNewData(data);
    }

    public static class LiveListViewHold extends BaseViewHolder{

        public LiveListViewHold(View view) {
            super(view);
        }

    }
}
