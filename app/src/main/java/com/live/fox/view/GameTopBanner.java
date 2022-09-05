package com.live.fox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.live.fox.R;
import com.live.fox.entity.Advert;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.StringUtils;

import java.util.List;


/**
 * User: ljx
 * Date: 2017/08/17
 * Time: 09:47
 */
public class GameTopBanner extends ConvenientBanner {


    public GameTopBanner(Context context) {
        this(context, null);
    }

    public GameTopBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameTopBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    List<Advert> bannerList;
    Context context;

    public void setData(Context context, List<Advert> bannerList){
        this.bannerList = bannerList;
        this.context = context;

        //設置Banner切換效果
        this.getViewPager().setPageTransformer(true, new ZoomOutSlideTransformer());

        this.setPages(new CBViewHolderCreator() {
            @Override
            public Holder<Advert> createHolder() {
                return new ImageHolder();
            }
        }, bannerList);

//        if (!this.isTurning()) {
//            this.startTurning(2000);
//        }

        this.setOnItemClickListener(new com.bigkoo.convenientbanner.listener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (!StringUtils.isEmpty(bannerList.get(position).getJumpUrl())) {
                    if (bannerList.get(position).getOpenWay() == 1) {
                        try {
                            IntentUtils.toBrowser(context, bannerList.get(position).getJumpUrl());
                        } catch (Exception e) {

                        }
                    } else {
//                    H5Activity.start(context, "", bannerList.get(position).getJumpUrl());
                        FragmentContentActivity.startWebActivity(context, "", bannerList.get(position).getJumpUrl());
                    }
                }

            }
        });
    }

    public class ImageHolder implements Holder<Advert> {

        private ImageView iv;
        private TextView tv;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_game_banner, null);
            iv = view.findViewById(R.id.iv_banner);
            tv = view.findViewById(R.id.tv_banner);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, Advert data) {
            tv.setBackgroundResource(0);
            GlideUtils.loadDefaultRoundedImage(context, data.getContent(), iv);
        }
    }


}
