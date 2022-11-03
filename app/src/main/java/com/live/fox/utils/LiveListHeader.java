package com.live.fox.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.adapter.HeaderHorListAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.entity.HomeBanner;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.entity.RoomListBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.view.convenientbanner.ConvenientBanner;
import com.live.fox.view.convenientbanner.holder.Holder;
import com.marquee.dingrui.marqueeviewlib.MarqueeView;

import java.util.ArrayList;
import java.util.List;

public class LiveListHeader extends RelativeLayout {

    ConvenientBanner convenientBanner;
    MarqueeView ivHomeHotRecommendRolling;
    MarqueeView ivHomePlayGameRolling;
    RecyclerView hsPlayGameList;
    RecyclerView hsHotRecommendList;
    List<RoomListBean> gameLists=new ArrayList<>();
    List<RoomListBean> AdultLists=new ArrayList<>();
    List<HomeBanner> homeBanners=new ArrayList<>();
    LinearLayout llHot;
    LinearLayout llGame;

    public LiveListHeader(Context context) {
        super(context);
        initView();
    }

    public LiveListHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LiveListHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setBannerList(List<HomeBanner> homeBanners)
    {
        this.homeBanners=homeBanners;
        if(homeBanners!=null )
        {
            convenientBanner.setNewData(homeBanners);
            convenientBanner.setVisibility(homeBanners.size()>0?VISIBLE:GONE);
            if (homeBanners.size()>1 && !convenientBanner.isTurning() ) {
                convenientBanner.startTurning(5000);
            }
        }
        else
        {
            convenientBanner.setVisibility(GONE);
        }


    }

    public void setGameLists(List<RoomListBean> gameLists) {
        this.gameLists.clear();
        if(gameLists!=null && gameLists.size()>0)
        {
            this.gameLists.addAll(gameLists);
            if(hsHotRecommendList.getAdapter()!=null)
            {
                llHot.setVisibility(VISIBLE);
                HeaderHorListAdapter headerHorListAdapter=(HeaderHorListAdapter) hsHotRecommendList.getAdapter();
                headerHorListAdapter.setNewData(this.gameLists);
            }
        }
        else
        {
            llHot.setVisibility(GONE);
        }


    }

    public void setAdultLists(List<RoomListBean> adultLists) {
        this.AdultLists.clear();
        if(adultLists!=null && adultLists.size()>0)
        {
            this.AdultLists.addAll(adultLists);
            if(hsPlayGameList.getAdapter()!=null)
            {
                llGame.setVisibility(VISIBLE);
                HeaderHorListAdapter headerHorListAdapter=(HeaderHorListAdapter) hsPlayGameList.getAdapter();
                headerHorListAdapter.setNewData(this.AdultLists);
            }
        }
        else
        {
            llGame.setVisibility(GONE);
        }

    }

    private void initView()
    {
        addView(View.inflate(getContext(), R.layout.fragment_header_livelist,null));
        hsHotRecommendList=findViewById(R.id.hsHotRecommendList);
        hsPlayGameList=findViewById(R.id.hsPlayGameList);
        ivHomePlayGameRolling=findViewById(R.id.ivHomePlayGameRolling);
        ivHomeHotRecommendRolling=findViewById(R.id.ivHomeHotRecommendRolling);
        convenientBanner=findViewById(R.id.convenientBanner);
        convenientBanner.getViewPager().setPageTransformer(true, new ZoomOutSlideTransformer());


        convenientBanner.setPages(BannerHolder::new, homeBanners)
                .setPageIndicator(new int[]{R.drawable.shape_banner_dot_normal, R.drawable.shape_banner_dot_sel})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

        //点击Banner
        convenientBanner.setOnItemClickListener(position -> {
            if (!StringUtils.isEmpty(homeBanners.get(position).getContent())) {
                //跳转类型:1内链 2外链
                switch (homeBanners.get(position).getJump())
                {
                    case 1:
                        FragmentContentActivity.startWebActivity(getContext(), "", homeBanners.get(position).getContent());
                        break;
                    case 2:
                        IntentUtils.toBrowser(getContext(), homeBanners.get(position).getContent());
                        break;
                }
            }
        });

        llHot=findViewById(R.id.llHot);
        llGame=findViewById(R.id.llGame);

        int dip5=ScreenUtils.dp2px(getContext(),5);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        hsHotRecommendList.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager2=new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        hsPlayGameList.setLayoutManager(linearLayoutManager2);
        hsHotRecommendList.addItemDecoration(new RecyclerSpace(dip5,RecyclerSpace.TypeLeft));
        hsPlayGameList.addItemDecoration(new RecyclerSpace(dip5,RecyclerSpace.TypeLeft));

        HeaderHorListAdapter adultAdapter=new HeaderHorListAdapter(getContext(),AdultLists);
        hsHotRecommendList.setAdapter(adultAdapter);

        HeaderHorListAdapter gameAdapter=new HeaderHorListAdapter(getContext(),gameLists);
        hsPlayGameList.setAdapter(gameAdapter);

        adultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                toLiveRoom((ArrayList<RoomListBean>)adapter.getData(),position);
            }
        });
        gameAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toLiveRoom((ArrayList<RoomListBean>)adapter.getData(),position);
            }
        });

    }

    //跳往直播间
    public void toLiveRoom(ArrayList<RoomListBean> roomListBeans,int position) {
        if (!DataCenter.getInstance().getUserInfo().isLogin()) {
            LoginModeSelActivity.startActivity(getContext());
            return;
        }

        LivingActivity.startActivity(getContext(),roomListBeans,position);
    }


    public static class BannerHolder implements Holder<HomeBanner> {

        private ImageView bannerImg;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_live_banner, null);
            bannerImg = view.findViewById(R.id.home_banner_image);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, HomeBanner banner) {
            String jsonStr = banner.getContent();
            String bannerUrl;
            if (jsonStr.endsWith("{") && jsonStr.endsWith("}")) {
                bannerUrl = LanguageUtilsEntity.getLanguage(new Gson().fromJson(jsonStr, LanguageUtilsEntity.class));
            } else {
                bannerUrl = jsonStr;
            }

            GlideUtils.loadDefaultImage(context, bannerUrl, bannerImg);
        }
    }
}
