package com.live.fox.ui.home;

import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.adapter.LiveListAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentRecommendListBinding;
import com.live.fox.entity.Advert;
import com.live.fox.entity.HomeBanner;
import com.live.fox.entity.HomeFragmentRoomListBean;
import com.live.fox.entity.RoomListBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.entity.User;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Live;
import com.live.fox.ui.living.LivingActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LiveListHeader;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.luck.picture.lib.tools.DoubleUtils;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页直播列表
 * Home页面
 */
public class RecommendListFragment extends BaseBindingFragment {

    FragmentRecommendListBinding mBind;
    private LiveListAdapter livelistAdapter;
    int tabIndex=0;
    User currentUser;
    HomeFragmentRoomListBean listBean;
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


    /**
     * 主页直播列表
     */
    public void initListRecycleView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),
                2, GridLayoutManager.VERTICAL, false);
        mBind.rvAnchorList.setLayoutManager(layoutManager);
        mBind.rvAnchorList.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 2.5f)));
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
        mBind.homeRefreshLayout.setEnableLoadMore(true);
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
                if(data!=null && data.size()>0 && header!=null)
                {
                    header.setBannerList(data);
                }
            }
        });
    }

    public void doGetLiveListApi() {

        Api_Live.ins().getLiveList(0, new JsonCallback<HomeFragmentRoomListBean>() {

            @Override
            public void onFinish() {
                super.onFinish();
                if (mBind.homeRefreshLayout != null){
                    mBind.homeRefreshLayout.finishRefresh();
                }
            }

            @Override
            public void onSuccess(int code, String msg, HomeFragmentRoomListBean data) {
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

        initGongGao();
        initListRecycleView();
        initRefreshLayout();
        doGetLiveListApi();
        doGetBanner();
    }

    private void initView() {
        MyWaterDropHeader header=new MyWaterDropHeader(getActivity());
        header.setBackgroundColor(0xffF5F1F8);
        mBind.homeRefreshLayout.setRefreshHeader(header);

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
        if (listBean!=null && listBean.getList().get(tabIndex).getRoomList()!=null &&
                listBean.getList().get(tabIndex).getRoomList().size()> 0) {

            List<RoomListBean> gameLists=new ArrayList<>();
            List<RoomListBean> AdultLists=new ArrayList<>();
            List<RoomListBean> greenLists=new ArrayList<>();
            for (int i = 0; i <listBean.getList().get(tabIndex).getRoomList().size() ; i++) {
                RoomListBean roomListBean=listBean.getList().get(tabIndex).getRoomList().get(i);

                int type=listBean.getList().get(tabIndex).getRoomList().get(i).getCategoryType();
                switch (type)
                {
                    case 1:
                        //游戏
                        gameLists.add(roomListBean);
                        break;
                    case 2:
                        //成人
                        AdultLists.add(roomListBean);
                        break;
                    case 3:
                        //绿播
                        greenLists.add(roomListBean);
                        break;

                }
            }

            if(tabIndex==0)
            {
                header.setAdultLists(AdultLists);
                header.setGameLists(gameLists);
                livelistAdapter.addHeaderView(header);
                livelistAdapter.setNewData(greenLists);
            }
            else
            {
                livelistAdapter.setNewData(listBean.getList().get(tabIndex).getRoomList());
            }

        }
        else
        {
            showEmptyView(getString(R.string.noData));
            livelistAdapter.setEmptyView(R.layout.view_empty, (ViewGroup) mBind.rvAnchorList.getParent());
        }
    }

    private void setTabs(List<HomeFragmentRoomListBean.ChannelList> channelLists)
    {
        mBind.hostTypeTabs.removeAllTabs();
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

        mBind.hostTypeTabs.selectTab(mBind.hostTypeTabs.getTabAt(tabIndex));
    }


    public void initGongGao() {
        mBind.rlBroadcast.setVisibility(View.VISIBLE);
        String content = SPUtils.getInstance("ad_gonggao").getString("content", "");

        if (StringUtils.isEmpty(content)) {
            mBind.rlBroadcast.setVisibility(View.GONE);
        } else {
            mBind.rlBroadcast.setVisibility(View.VISIBLE);
            List<Advert> advertList = GsonUtil.getObjects(content, Advert[].class);
            String jsonStr = advertList.get(0).getContent();
            if (jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
                jsonStr = LanguageUtilsEntity.getLanguage(new Gson().fromJson(jsonStr, LanguageUtilsEntity.class));
            }

            mBind.mvBroadcast.setContent(jsonStr);
            mBind.mvBroadcast.setOnClickListener(view -> {
                if (!StringUtils.isEmpty(advertList.get(0).getJumpUrl())) {
                    if (advertList.get(0).getOpenWay() == 0) { //打开方式 0站内，1站外
                        FragmentContentActivity.startWebActivity(getActivity(), "", advertList.get(0).getJumpUrl());
                    } else {
                        IntentUtils.toBrowser(getActivity(), advertList.get(0).getJumpUrl());
                    }
                }
            });
        }
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


}
