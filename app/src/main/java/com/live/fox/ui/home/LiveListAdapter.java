package com.live.fox.ui.home;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.BuildConfig;
import com.live.fox.R;
import com.live.fox.entity.Anchor;
import com.live.fox.svga.AnchorInfoBean;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;

import java.util.List;

/**
 * 主页直播适配器
 */
public class LiveListAdapter extends BaseSectionQuickAdapter<AnchorInfoBean, BaseViewHolder> {

    List<Anchor> bannerAdList;

    public LiveListAdapter(List<AnchorInfoBean> data) {
        super(R.layout.item_liveroom, R.layout.item_liveroomlist_adbanner, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final AnchorInfoBean dataBean) {

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
    protected void convert(BaseViewHolder helper, AnchorInfoBean data) {
        Anchor anchor = data.t;
        if (anchor == null) return;

        //Views
        TextView category = helper.getView(R.id.tv_cai_category);  //类别
        ImageView roomStata = helper.getView(R.id.layout_living);

        // 0普通房间 1广告房间
        helper.setText(R.id.tv_num, RegexUtils.formatNumber(anchor.getRq()));
        if (anchor.getIsAd() == 1) {
            category.setVisibility(View.GONE);
            roomStata.setVisibility(View.INVISIBLE);
            helper.getView(R.id.layout_pking).setVisibility(View.INVISIBLE);
            helper.getView(R.id.iv_joytd).setVisibility(View.INVISIBLE);
        } else {
            //普通直播间
            if (anchor.getLiveStartLottery() == null || anchor.getLiveStartLottery().size() == 0) {
                category.setVisibility(View.GONE);
            } else {
                category.setVisibility(View.VISIBLE);
                String str;
                if (anchor.getLiveStartLottery().size() == 2) {
                    if (BuildConfig.AppFlavor.equals("QQLive")) {
                        str = anchor.getLiveStartLottery().get(0).getLotteryName() + " / " +
                                anchor.getLiveStartLottery().get(1).getLotteryName();
                    } else {
                        str = anchor.getLiveStartLottery().get(0).getCpName() + " / " +
                                anchor.getLiveStartLottery().get(1).getCpName();
                    }
                } else {
                    if (BuildConfig.AppFlavor.equals("QQLive")) {
                        str = anchor.getLiveStartLottery().get(0).getLotteryName();
                    } else {
                        str = anchor.getLiveStartLottery().get(0).getCpName();
                    }
                }
                category.setText(str);
            }

            //跳蛋房间
            if (anchor.getToy() == 1) {
                helper.getView(R.id.iv_joytd).setVisibility(View.VISIBLE);
            } else {
                helper.getView(R.id.iv_joytd).setVisibility(View.GONE);
            }

            Drawable drawable;
            //房间类型
            switch (anchor.getType()) {
                case 0: //普通房间
                    roomStata.setVisibility(View.GONE);
                    drawable = null;
                    break;
                case 1: // 记时
                    drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_times);
                    break;

                case 2: //付费
                    roomStata.setVisibility(View.VISIBLE);
                    drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_frequency);
                    break;

                case 3: //是否需要密码
                    drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_live_room_lock);
                    break;

                default:
                    drawable = null;
                    break;
            }
            roomStata.setImageDrawable(drawable);
        }
        //直播间在 PK
        if (anchor.isPking()) {
            helper.getView(R.id.layout_pking).setVisibility(View.VISIBLE);
            roomStata.setVisibility(View.INVISIBLE);
        } else {
            helper.getView(R.id.layout_pking).setVisibility(View.GONE);
            roomStata.setVisibility(View.VISIBLE);
        }

        TextView anchorDetail = helper.getView(R.id.tv_sign);
        String anchorDetailStr;
        if (anchor.getIsAd() == 1) {   //广告直播间
            helper.getView(R.id.tv_num).setVisibility(View.GONE);
            helper.getView(R.id.tv_nickname).setVisibility(View.GONE);
            anchorDetailStr = anchor.getNickname();
        } else {   //正常直播间

            helper.getView(R.id.tv_num).setVisibility(View.VISIBLE);
            if (StringUtils.isEmpty(anchor.getSignature())) {
                helper.getView(R.id.tv_nickname).setVisibility(View.GONE);
                anchorDetailStr = anchor.getNickname();
            } else {
                helper.getView(R.id.tv_nickname).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_nickname, anchor.getNickname());
                anchorDetailStr = anchor.getSignature();
            }
        }

        if (TextUtils.isEmpty(anchor.getNickname())) {
            anchorDetail.setVisibility(View.GONE);
        } else {
            anchorDetail.setText(anchorDetailStr);
        }

        GlideUtils.loadDefaultRoundedImage(mContext, anchor.getAvatar(), helper.getView(R.id.iv_cover));
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

}
