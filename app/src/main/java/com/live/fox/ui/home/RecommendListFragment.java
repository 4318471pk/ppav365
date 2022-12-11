package com.live.fox.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.adapter.LiveListAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentRecommendListBinding;
import com.live.fox.entity.Advert;
import com.live.fox.entity.HomeBanner;
import com.live.fox.entity.HomeFragmentRoomListBean;
import com.live.fox.entity.HomeRecommendRoomListBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.entity.User;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.JumpLinkUtils;
import com.live.fox.utils.LiveListHeader;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.view.convenientbanner.ConvenientBanner;
import com.live.fox.view.convenientbanner.holder.Holder;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.luck.picture.lib.tools.DoubleUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * 主页直播列表
 * Home页面
 */
public class RecommendListFragment extends BaseBindingFragment {

    FragmentRecommendListBinding mBind;
    private LiveListAdapter livelistAdapter;
    int tabIndex=0;
    User currentUser;
    HomeRecommendRoomListBean listBean;
    LiveListHeader header;
//    private RecyclerViewSkeletonScreen skeletonScreen;

    public static RecommendListFragment newInstance() {
        return new RecommendListFragment();
    }


    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_recommend_list;
    }


    @Override
    public void onStart() {
        super.onStart();
        mBind.mvBroadcast.continueRoll();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBind.mvBroadcast.stopRoll();
    }

    /**
     * 主页直播列表
     */
    public void initListRecycleView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                2, GridLayoutManager.VERTICAL, false);
        mBind.rvAnchorList.setLayoutManager(layoutManager);
//        mBind.rvAnchorList.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 2.5f)));
        mBind.rvAnchorList.setAdapter(livelistAdapter = new LiveListAdapter(getActivity(),new ArrayList<>()));
        header=new LiveListHeader(getContext());

        livelistAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (DoubleUtils.isFastDoubleClick()) return;
            if (livelistAdapter.getItem(position) == null) return;

            toLiveRoom((ArrayList<RoomListBean>)adapter.getData(),position);
        });

//        skeletonScreen = Skeleton.bind(livelistRv)
//                .adapter(livelistAdapter)
//                .load(R.layout.item_loading)
//                .show();
    }

    //跳往直播间
    public void toLiveRoom(ArrayList<RoomListBean> roomListBeans,int position) {
        if (!DataCenter.getInstance().getUserInfo().isLogin()) {
            LoginModeSelActivity.startActivity(requireContext());
            return;
        }

        LivingActivity.startActivity(getActivity(),roomListBeans,position);
    }

    /**
     * 刷新
     */
    public void initRefreshLayout() {
        MyWaterDropHeader header=new MyWaterDropHeader(getActivity());
        header.setBackgroundColor(0xffF5F1F8);
        mBind.homeRefreshLayout.setRefreshHeader(header);
//        mBind.homeRefreshLayout.setEnableLoadMore(true);
        mBind.homeRefreshLayout.setOnRefreshListener(refreshLayout -> {
            doGetLiveListApi();
            doGetBanner();
        });

    }

    public void doGetBanner()
    {
        if(tabIndex!=0)
        {
            return;
        }
        Api_Config.ins().getRecommendListBanner(new JsonCallback<List<HomeBanner>>() {
            @Override
            public void onSuccess(int code, String msg, List<HomeBanner> data) {
                if(!isActivityOK())
                {
                    return;
                }
                if(data!=null && data.size()>0 && header!=null)
                {
                    setBannerList(data);
                }
            }
        });
    }

    public void getRecommendAnnounceList()
    {
        if(tabIndex!=0)
        {
            return;
        }
        Api_Config.ins().getRecommendAnnounceList(new JsonCallback<List<HomeBanner>>() {
            @Override
            public void onSuccess(int code, String msg, final List<HomeBanner> data) {
                if(!isActivityOK())
                {
                    return;
                }

                if(data!=null && data.size()>0)
                {
                    List<String> strings=new ArrayList<>();
                    for (int i = 0; i <data.size() ; i++) {
                        strings.add(data.get(i).getContent());
                    }
                    mBind.mvBroadcast.setContent(strings);
//                    mBind.mvBroadcast.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(int position, TextView textView) {
//                            JumpLinkUtils.jumpHomeBannerLinks(getActivity(),data.get(position));
//                        }
//                    });
                }
            }
        });
    }

    public void doGetLiveListApi() {

        Api_Live.ins().recommended(0, new JsonCallback<HomeRecommendRoomListBean>() {

            @Override
            public void onFinish() {
                super.onFinish();
                if (mBind.homeRefreshLayout != null){
                    mBind.homeRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onSuccess(int code, String msg, HomeRecommendRoomListBean data) {
                if (data == null || !isActivityOK()) {
                    if (isAdded()) {
                        showEmptyView(getString(R.string.noData));
                    }
                    return;
                }
                hideEmptyView();
                if (mBind.homeRefreshLayout != null){
                    mBind.homeRefreshLayout.finishRefresh();
                }


                if (code == 0) {
                    if(data!=null && data.getList()!=null)
                    {
                        listBean=data;
                    }

                    if(data.getList()!=null && data.getList().size()>0)
                    {
                        setTabs(data.getList());
                    }
                    setAdapterNewData();
                }

                if (livelistAdapter.getData().size() == 0) {
                    showEmptyView(msg);
                }
            }
        });
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        initView();
        currentUser = DataCenter.getInstance().getUserInfo().getUser();

        mBind.homeConvenientBanner.getLayoutParams().height=(int)(ScreenUtils.getScreenWidth(getContext())*0.213f);

        mBind.homeConvenientBanner.setPages(BannerHolder::new, new ArrayList())
                .setPageIndicator(new int[]{R.drawable.shape_banner_dot_normal, R.drawable.shape_banner_dot_sel})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

//        convenientBanner.getViewPager().setPageTransformer(true, new ZoomOutSlideTransformer());

        getRecommendAnnounceList();
        initListRecycleView();
        initRefreshLayout();
        doGetLiveListApi();
        doGetBanner();
    }

    private void initView() {

        //假数据-------------------
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        int dip1 = ScreenUtils.dip2px(getContext(), 1);
        for (int i = 0; i < 20; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(getResources().getText(R.string.home_bottom_tab_game));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(getResources().getColor(R.color.red));
            textView.setTextColor(0xff404040);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(dip1 * 60, dip1 * 60);
            ll.leftMargin = dip1 * 5;
            textView.setLayoutParams(ll);
            linearLayout.addView(textView);
        }
        mBind.gamesHS.addView(linearLayout);
    }

    private void setAdapterNewData()
    {
        livelistAdapter.removeHeaderView(header);
        if(tabIndex==0)
        {

            header.setAdultLists(listBean.getList().get(tabIndex).getRoomCrList());
            header.setGameLists(listBean.getList().get(tabIndex).getRoomGameList());
        }

        if (listBean!=null && listBean.getList().get(tabIndex).getRoomAllList()!=null &&
                listBean.getList().get(tabIndex).getRoomAllList().size()> 0) {
            if(tabIndex==0)
            {

                livelistAdapter.addHeaderView(header);
                livelistAdapter.setNewData(listBean.getList().get(tabIndex).getRoomAllList());
            }
            else
            {
                livelistAdapter.setNewData(listBean.getList().get(tabIndex).getRoomAllList());
            }

        }
        else
        {
            showEmptyView(getString(R.string.noData));
            livelistAdapter.setEmptyView(R.layout.view_empty, (ViewGroup) mBind.rvAnchorList.getParent());
        }
    }

    private void setTabs(List<HomeRecommendRoomListBean.ChannelListData> channelLists)
    {
        mBind.hostTypeTabs.removeAllTabs();
        mBind.hostTypeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RelativeLayout relativeLayout = (RelativeLayout) tab.getCustomView();
                TextView item = (TextView) relativeLayout.getChildAt(0);
                item.setBackground(getResources().getDrawable(R.drawable.round_gradient_a800ff_d689ff));
                item.setTextColor(0xffffffff);
                tabIndex=tab.getPosition();
                setAdapterNewData();
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
                RelativeLayout relativeLayout = (RelativeLayout) tab.getCustomView();
                TextView item = (TextView) relativeLayout.getChildAt(0);
                item.setBackground(getResources().getDrawable(R.drawable.round_gradient_a800ff_d689ff));
                item.setTextColor(0xffffffff);
            }
        });
        int dip1 = ScreenUtils.dip2px(getContext(), 1);
        int screenWidth = ScreenUtils.getScreenWidth(getContext());
        int itemWidth = (screenWidth - ScreenUtils.dip2px(getContext(), 50)) / 5;
        for (int i = 0; i < channelLists.size(); i++) {
            RelativeLayout tabItemRL = new RelativeLayout(getContext());
            tabItemRL.setLayoutParams(new ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT));

            TextView tvTab = new TextView(getContext());
            tvTab.setText(channelLists.get(i).getChannelName());
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

            mBind.hostTypeTabs.addTab(mBind.hostTypeTabs.newTab().setCustomView(tabItemRL));
        }
        mBind.hostTypeTabs.selectTab(mBind.hostTypeTabs.getTabAt(tabIndex));
    }

    //每次回来都刷新直播列表
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x100) {
            if (livelistAdapter != null) {
                doGetLiveListApi();
            }
        }
    }

    public void setBannerList(List<HomeBanner> homeBanners)
    {
        if(homeBanners!=null )
        {
            mBind.homeConvenientBanner.setNewData(homeBanners);
            mBind.homeConvenientBanner.setVisibility(homeBanners.size()>0?VISIBLE:GONE);
            if (homeBanners.size()>1 && !mBind.homeConvenientBanner.isTurning() ) {
                mBind.homeConvenientBanner.startTurning(5000);
            }
        }
        else
        {
            mBind.homeConvenientBanner.setVisibility(GONE);
        }

        //点击Banner
        mBind.homeConvenientBanner.setOnItemClickListener(position -> {
            JumpLinkUtils.jumpHomeBannerLinks(getContext(),homeBanners.get(position));
        });
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
            String jsonStr = banner.getBannerImg();
            String bannerUrl;
            if (jsonStr.endsWith("{") && jsonStr.endsWith("}")) {
                bannerUrl = LanguageUtilsEntity.getLanguage(new Gson().fromJson(jsonStr, LanguageUtilsEntity.class));
            } else {
                bannerUrl = jsonStr;
            }

            GlideUtils.loadDefaultImage(context, bannerUrl,0,0, bannerImg);
        }
    }

    @Subscribe(tags = {@Tag(ConstantValue.refreshLive)})
    public void skipRegister(String jumpType) {


        switch (jumpType)
        {
            case "1":
                LogUtils.i("HotAnchorFragment......", "refreshLive onFinish");

                mBind.homeRefreshLayout.autoRefresh();
                break;
        }
    }
}
