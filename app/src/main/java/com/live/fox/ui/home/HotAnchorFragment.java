package com.live.fox.ui.home;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.live.fox.R;
import com.live.fox.adapter.AnchorGameListAdapter;
import com.live.fox.adapter.HotAnchorListAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentHotAnchorBinding;
import com.live.fox.entity.Advert;
import com.live.fox.entity.Anchor;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.view.convenientbanner.ConvenientBanner;
import com.live.fox.view.convenientbanner.holder.Holder;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class HotAnchorFragment extends BaseBindingFragment {

    HotAnchorListAdapter adapter;
    FragmentHotAnchorBinding mBind;

    public static HotAnchorFragment newInstance()
    {
        return new HotAnchorFragment();
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_hot_anchor;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        mBind.srlRefresh.setRefreshHeader(new MyWaterDropHeader(getActivity()));
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                2, GridLayoutManager.VERTICAL, false);
        mBind.rvMain.setLayoutManager(layoutManager);
        mBind.rvMain.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 2.5f)));

        List<Anchor> list=new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(new Anchor());
        }
        adapter=new HotAnchorListAdapter(getActivity(),list);
        mBind.rvMain.setAdapter(adapter);
        setTabs();
        setBanner();

    }

    private void setTabs()
    {
        mBind.hotHostTypeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RelativeLayout relativeLayout = (RelativeLayout) tab.getCustomView();
                TextView item = (TextView) relativeLayout.getChildAt(0);
                item.setBackground(getResources().getDrawable(R.drawable.round_gradient_a800ff_d689ff));
                item.setTextColor(0xffffffff);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                RelativeLayout relativeLayout = (RelativeLayout) tab.getCustomView();
                TextView item = (TextView) relativeLayout.getChildAt(0);
                item.setBackground(getResources().getDrawable(R.drawable.oval_f4f1f8));
                item.setTextColor(0xff404040);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        int dip1 = ScreenUtils.dip2px(getContext(), 1);
        int screenWidth = ScreenUtils.getScreenWidth(getContext());
        int itemWidth = (screenWidth - ScreenUtils.dip2px(getContext(), 50)) / 5;
        for (int i = 0; i < 10; i++) {
            RelativeLayout tabItemRL = new RelativeLayout(getContext());
            tabItemRL.setLayoutParams(new ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT));

            TextView tvTab = new TextView(getContext());
            tvTab.setText(i > 0 ? "姐姐" : "休闲鞋好");
            tvTab.setGravity(Gravity.CENTER);
            tvTab.setTextColor(0xff404040);
            tvTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            tvTab.setBackground(getResources().getDrawable(R.drawable.oval_f4f1f8));
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            rl.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//            rl.topMargin=dip1*5;
            rl.bottomMargin = dip1 * 10;
            tvTab.setLayoutParams(rl);
            tabItemRL.addView(tvTab);

            mBind.hotHostTypeTabs.addTab(mBind.hotHostTypeTabs.newTab().setCustomView(tabItemRL));
        }
    }

    private void setBanner()
    {
        List<Advert> bannerList =new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            Advert advert=new Advert();
            bannerList.add(advert);
        }
        mBind.hotConvenientBanner.setPages(AnchorGameFragment.BannerHolder::new, bannerList)
                .setPageIndicator(new int[]{R.drawable.shape_banner_dot_normal, R.drawable.shape_banner_dot_sel})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

//        mBind.gameConvenientBanner.getViewPager().setPageTransformer(true, new ZoomOutSlideTransformer());

        if (!mBind.hotConvenientBanner.isTurning()) {
            mBind.hotConvenientBanner.startTurning(5000);
        }
    }

    public static class BannerHolder implements Holder<Advert> {

        private ImageView bannerImg;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_live_banner, null);
            bannerImg = view.findViewById(R.id.home_banner_image);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, Advert banner) {
            String jsonStr = banner.getContent();
            if(jsonStr==null)
            {
                jsonStr="";
            }
            GlideUtils.loadDefaultImage(context, jsonStr, bannerImg);
        }
    }
}
