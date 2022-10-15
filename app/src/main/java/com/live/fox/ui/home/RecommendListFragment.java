package com.live.fox.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.adapter.LiveListAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentRecommendListBinding;
import com.live.fox.entity.Advert;
import com.live.fox.entity.Anchor;
import com.live.fox.manager.DataCenter;
import com.live.fox.svga.AnchorInfoBean;
import com.live.fox.entity.GameColumn;
import com.live.fox.entity.GameItem;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.entity.User;
import com.live.fox.server.Api_AgGame;
import com.live.fox.server.Api_Config;
import com.live.fox.server.Api_Cp;
import com.live.fox.server.Api_FwGame;
import com.live.fox.server.Api_KyGame;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_Pay;
import com.live.fox.server.Api_TYGame;
import com.live.fox.ui.game.GameFullWebViewActivity;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.ui.login.LoginModeSelActivity;
import com.live.fox.utils.AdManger;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.LiveListHeader;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.LruCacheUtil;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.ZoomOutSlideTransformer;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.view.convenientbanner.ConvenientBanner;
import com.live.fox.view.convenientbanner.holder.Holder;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.luck.picture.lib.tools.DoubleUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页直播列表
 * Home页面
 */
public class RecommendListFragment extends BaseBindingFragment {

    FragmentRecommendListBinding mBind;
    private ConvenientBanner<Advert> convenientBanner;
    private LiveListAdapter livelistAdapter;
    List<AnchorInfoBean> anchorInfoBeanList;
    User currentUser;
    private int anchorPosition = -1;
    private final List<Advert> bannerList = new ArrayList<>();
    private boolean hasBanner;
//    private RecyclerViewSkeletonScreen skeletonScreen;

    public static RecommendListFragment newInstance() {
        return new RecommendListFragment();
    }

    private void requestAppAd() {
        Api_Config.ins().getAdVert(new JsonCallback<List<Advert>>() {
            @Override
            public void onSuccess(int code, String msg, List<Advert> data) {
                if (data != null) {
                    bannerList.clear();
                    handleAdData(data);
                }
            }
        });
    }


    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_recommend_list;
    }

    /**
     * 处理广告数据
     */
    private void handleAdData(List<Advert> data) {
        List<Advert> banner2List = new ArrayList<>();  //2.次级横幅广告
        List<Advert> gonggaoTxtList = new ArrayList<>();  //3.首页飘屏文字广告
        List<Advert> liveroomTxtList = new ArrayList<>();  //4.直播间文字广告
        List<Advert> liveroomPPAdList = new ArrayList<>();   //5.直播间飘屏广告
        List<Advert> liveroomImageList = new ArrayList<>();  //6.房间活动图片广告
        List<Advert> liveroomBannerList = new ArrayList<>();  //7.直播间左下角的广告
        List<Advert> gameBannerList = new ArrayList<>();  //8.游戏广告
        List<Advert> tixianList = new ArrayList<>();  //8.提现广告

        for (Advert advert : data) {
            switch (advert.getType()) {
                case 1:
                    bannerList.add(advert);
                    break;
                case 2:
                    banner2List.add(advert);
                    break;
                case 3:
                    gonggaoTxtList.add(advert);
                    break;
                case 4:
                    liveroomTxtList.add(advert);
                    break;
                case 5:
                    liveroomPPAdList.add(advert);
                    break;
                case 6:
                    liveroomBannerList.add(advert);
                    break;
                case 7:
                    liveroomImageList.add(advert);
                    break;
                case 8:
                    gameBannerList.add(advert);
                    break;
                case 9:
                    tixianList.add(advert);
                    break;
            }
        }
        AdManger.Instance().setBannerList(bannerList);

        //清空本地
        SPUtils.getInstance("ad_banner").clear();
        SPUtils.getInstance("ad_banner2").clear();
        SPUtils.getInstance("ad_gonggao").clear();
        SPUtils.getInstance("ad_liveroomTxt").clear();
        SPUtils.getInstance("ad_liveroomPP").clear();
        SPUtils.getInstance("ad_liveroomBanner").clear();
        SPUtils.getInstance("ad_liveroomImage").clear();
        SPUtils.getInstance("ad_gameBanner").clear();

        if (bannerList.size() > 0) {
            LogUtils.e("Banner1:" + bannerList.size());
            SPUtils.getInstance("ad_banner").put("content", new Gson().toJson(bannerList));
        }
        if (banner2List.size() > 0) {
            SPUtils.getInstance("ad_banner2").put("content", new Gson().toJson(banner2List));
        }
        if (gonggaoTxtList.size() > 0) {
            SPUtils.getInstance("ad_gonggao").put("content", new Gson().toJson(gonggaoTxtList));
        }
        if (liveroomTxtList.size() > 0) {
            SPUtils.getInstance("ad_liveroomTxt").put("content", new Gson().toJson(liveroomTxtList));
        }
        if (liveroomPPAdList.size() > 0) {
            SPUtils.getInstance("ad_liveroomPP").put("content", new Gson().toJson(liveroomPPAdList));
        }
        if (liveroomBannerList.size() > 0) {
            SPUtils.getInstance("ad_liveroomBanner").put("content", new Gson().toJson(liveroomBannerList));
        }
        if (liveroomImageList.size() > 0) {
            SPUtils.getInstance("ad_liveroomImage").put("content", new Gson().toJson(liveroomImageList));
        }
        if (gameBannerList.size() > 0) {
            SPUtils.getInstance("ad_gameBanner").put("content", new Gson().toJson(gameBannerList));
        }
        if (tixianList.size() > 0) {
            SPUtils.getInstance("tixianAd").put("content", new Gson().toJson(tixianList));
        }
        initConvenientBanner(bannerList);
    }

    //登录游戏
    public void loginGame(GameItem gameItem) {
        showLoadingDialog(getString(R.string.baseLoading), false, false);
        if (gameItem.getType() == 0) { //0：老朱；1：开元 2：AG
            Api_Pay.ins().getGame(DataCenter.getInstance().getUserInfo().getUser().getUid() + "",
                    gameItem.getName(), gameItem.getGameId(), 2, new JsonCallback<String>() {
                        @Override
                        public void onSuccess(int code, String msg, String data) {
                            if (data != null) LogUtils.e(data);
                            hideLoadingDialog();
                            if (code == 0) {
                                GameFullWebViewActivity.startActivity(requireActivity(), gameItem.getType(), data);
                            } else {
                                ToastUtils.showShort(msg);
                            }
                        }
                    });
        } else if (gameItem.getType() == 1) { //1 开元
            Api_KyGame.ins().login(gameItem.getGameId(), new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String result) {
                    if (result != null) LogUtils.e("getKYGameList : " + new Gson().toJson(result));
                    hideLoadingDialog();
                    if (code == 0 && result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String url = jsonObject.optString("url");
                            LogUtils.e("url: " + url);
                            GameFullWebViewActivity.startActivity(requireActivity(), gameItem.getType(), url);
                        } catch (JSONException e) {
                            ToastUtils.showShort(e.getMessage());
                        }

                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        } else if (gameItem.getType() == 2) { //2 AG
            Api_AgGame.ins().forwardGame(gameItem.getGameId(), new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String result) {
                    if (result != null) LogUtils.e("getAgGameList : " + new Gson().toJson(result));
                    hideLoadingDialog();
                    if (code == 0 && result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String url = jsonObject.optString("param");
                            LogUtils.e("param: " + url);
                            GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), url);
                        } catch (JSONException e) {
                            ToastUtils.showShort(e.getMessage());
                        }

                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        } else if (gameItem.getType() == 3) { //3 CP
            Api_Cp.ins().geth5url(gameItem.getGameId(), new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String result) {
                    if (result != null) LogUtils.e("geth5url : " + new Gson().toJson(result));
                    hideLoadingDialog();
                    if (code == 0) {
                        LogUtils.e("url: " + result);
                        GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), result);
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        } else if (gameItem.getType() == 4) { //4 CP
            Api_FwGame.ins().login(gameItem.getGameId(), new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String result) {
                    if (result != null) LogUtils.e("getKYGameList : " + new Gson().toJson(result));
                    hideLoadingDialog();
                    if (code == 0 && result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String url = jsonObject.optString("url");
                            LogUtils.e("url: " + url);
                            GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), url);
                        } catch (JSONException e) {
                            ToastUtils.showShort(e.getMessage());
                        }
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        } else if (gameItem.getType() == 5 || gameItem.getType() == 6) { //bg
            Api_FwGame.ins().loginBg(gameItem.getGameId(),
                    DataCenter.getInstance().getUserInfo().getUser().getUid() + "", new JsonCallback<String>() {
                        @Override
                        public void onSuccess(int code, String msg, String result) {
                            if (result != null)
                                LogUtils.e("getBGGameList : " + new Gson().toJson(result));
                            hideLoadingDialog();
                            if (code == 0 && result != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String url = jsonObject.optString("url");
                                    LogUtils.e("url: " + url);
                                    GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), url);
                                } catch (JSONException e) {
                                    ToastUtils.showShort(e.getMessage());
                                }
                            } else {
                                ToastUtils.showShort(msg);
                            }
                        }
                    });
        } else if (gameItem.getType() == 7) { //体育
            Api_TYGame.ins().forwardGame(gameItem.getGameId(),
                    DataCenter.getInstance().getUserInfo().getUser().getUid() + "", new JsonCallback<String>() {
                        @Override
                        public void onSuccess(int code, String msg, String result) {
                            if (result != null)
                                LogUtils.e("getBGGameList : " + new Gson().toJson(result));
                            hideLoadingDialog();
                            if (code == 0 && result != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String url = jsonObject.optString("url");
                                    LogUtils.e("url: " + url);
                                    GameFullWebViewActivity.startActivity(getActivity(), gameItem.getType(), url);
                                } catch (JSONException e) {
                                    ToastUtils.showShort(e.getMessage());
                                }
                            } else {
                                ToastUtils.showShort(msg);
                            }
                        }
                    });
        } else {
            hideLoadingDialog();
            ToastUtils.showShort(getString(R.string.comingSoon));
        }
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
        LiveListHeader header=new LiveListHeader(getContext());
        livelistAdapter.addHeaderView(header);
        convenientBanner = header.findViewById(R.id.home_convenient_banner);
        livelistAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (DoubleUtils.isFastDoubleClick()) return;
            if (livelistAdapter.getItem(position) == null) return;

            AnchorInfoBean item = livelistAdapter.getItem(position);
            if (item != null) {
                Anchor liveRoom = item.t;
                if (liveRoom == null) return;
                toLiveRoom(liveRoom, position);
            }
        });

//        skeletonScreen = Skeleton.bind(livelistRv)
//                .adapter(livelistAdapter)
//                .load(R.layout.item_loading)
//                .show();
    }

    //跳往直播间
    public void toLiveRoom(Anchor anchor, int position) {
        if (DataCenter.getInstance().getUserInfo().getUser() == null) {
            LoginModeSelActivity.startActivity(requireContext());
            return;
        }

        LogUtils.e(new Gson().toJson(anchor));
        //广告直播间
        if (anchor.getIsAd() == 1) {
            if (StringUtils.isEmpty(anchor.getAdJumpUrl())) {
                return;
            }
            //如果是广告房间 则跳浏览器
            try {
                IntentUtils.toBrowser(requireContext(), anchor.getAdJumpUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (DataCenter.getInstance().getUserInfo().getUser() == null) {
                LoginModeSelActivity.startActivity(requireContext());
                return;
            }
            if (ClickUtil.isFastDoubleClick2(2000)) return;
            //过滤广告直播间 获取直播列表
            List<AnchorInfoBean> tempAnchorList = livelistAdapter.getData();
            LruCacheUtil.getInstance().get().clear();
            for (int i = 0; i < tempAnchorList.size(); i++) {
                if (!tempAnchorList.get(i).isHeader && tempAnchorList.get(i).t.getIsAd() == 0) {
                    LruCacheUtil.getInstance().put(tempAnchorList.get(i).t);
                }
            }
            PlayLiveActivity.startActivityForResult(RecommendListFragment.this, anchor);
        }
    }

    /**
     * 刷新
     */
    public void initRefreshLayout() {
        mBind.homeRefreshLayout.setEnableLoadMore(true);
        mBind.homeRefreshLayout.setOnRefreshListener(refreshLayout -> {
            requestAppAd();
            doGetLiveListApi(1);
//            doGetGameListApi();
//            doGetLiveRecommendApi();
        });
    }

    public void doGetLiveRecommendApi() {
        if (!DataCenter.getInstance().getUserInfo().isLogin()) return;
        Api_Live.ins().getRocketlist(currentUser.getUid(), new JsonCallback<List<Anchor>>() {
            @Override
            public void onSuccess(int code, String msg, List<Anchor> data) {
                if (code == 0) {
                    if (data != null && data.size() != 0) {

                    }
                }
            }
        });
    }

    /**
     * 游戏列表
     */
    public void doGetGameListApi() {
        Api_Config.ins().getHomeGameList(new JsonCallback<GameColumn>() {
            @Override
            public void onSuccess(int code, String msg, GameColumn data) {
                if (code == 0) {
                    if (data == null || data.getList() == null || data.getList().size() == 0) {
                    } else {
                    }
                }
            }
        });
    }

    /**
     * 获取直播列表
     *
     * @param type 类型
     */
    public void doGetLiveListApi(int type) {
        Api_Live.ins().getLiveList(type, new JsonCallback<List<Anchor>>() {
            @Override
            public void onSuccess(int code, String msg, List<Anchor> data) {
                if (data == null) {
                    if (isAdded()) {
                        showEmptyView(getString(R.string.noData));
                    }
                    return;
                }
                hideEmptyView();
                LogUtils.e("房间列表：" + new Gson().toJson(data));
                if (mBind.homeRefreshLayout != null)
                    mBind.homeRefreshLayout.finishRefresh();
                if (code == 0) {
                    if (data.size() == 0) {
                        showEmptyView(getString(R.string.noData));
                        livelistAdapter.setEmptyView(R.layout.view_empty, (ViewGroup) mBind.rvAnchorList.getParent());
                    }
                    anchorInfoBeanList = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        Anchor anchor = data.get(i);
                        anchor.setRoomType(0);
                        //标记主播位置
                        if (DataCenter.getInstance().getUserInfo().isLogin() && anchor.getAnchorId() == DataCenter.getInstance().getUserInfo().getUser().getUid()) {
                            anchorPosition = i;
                        }
                        //测试测试测试测试测试测试测试测试测试测试测试测试
                        for (int j = 0; j < 30; j++) {
                            anchorInfoBeanList.add(new AnchorInfoBean(anchor));
//                            anchorInfoBeanList.add(new AnchorInfoBean(true));
                        }

                    }
                    livelistAdapter.setData(type, anchorInfoBeanList);

                    //在列表中移除主播
                    int size = livelistAdapter.getData().size();
                    if (anchorPosition != -1 && anchorPosition > 1 && anchorPosition < size) {
                        livelistAdapter.remove(anchorPosition);
                        livelistAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (livelistAdapter.getData().size() == 0) {
                        showEmptyView(msg);
                    }
                }
            }
        });
    }

    /**
     * 初始化Banner
     *
     * @param bannerList banner 数据
     */
    public void initConvenientBanner(List<Advert> bannerList) {
        if (bannerList != null && convenientBanner != null) {
            hasBanner = true;
            convenientBanner.setPages(BannerHolder::new, bannerList)
                    .setPageIndicator(new int[]{R.drawable.shape_banner_dot_normal, R.drawable.shape_banner_dot_sel})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);

            convenientBanner.getViewPager().setPageTransformer(true, new ZoomOutSlideTransformer());

            if (!convenientBanner.isTurning()) {
                convenientBanner.startTurning(5000);
            }

            //点击Banner
            convenientBanner.setOnItemClickListener(position -> {
                if (!StringUtils.isEmpty(bannerList.get(position).getJumpUrl())) {
                    if (bannerList.get(position).getOpenWay() == 0) { //打开方式 0站内，1站外
                        FragmentContentActivity.startWebActivity(requireActivity(), "", bannerList.get(position).getJumpUrl());
                    } else {
                        IntentUtils.toBrowser(requireActivity(), bannerList.get(position).getJumpUrl());
                    }
                }
            });
        } else {
            if (convenientBanner != null) {
                convenientBanner.setVisibility(View.GONE);
            }
            hasBanner = false;
        }
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        initView();
        currentUser = DataCenter.getInstance().getUserInfo().getUser();

        requestAppAd();
        initGongGao();
        initListRecycleView();
        initRefreshLayout();
//        doGetLiveRecommendApi();
//        doGetGameListApi();
        doGetLiveListApi(1);
    }

    private void initView() {
        MyWaterDropHeader header=new MyWaterDropHeader(getActivity());
        header.setBackgroundColor(0xffF5F1F8);
        mBind.homeRefreshLayout.setRefreshHeader(header);

        mBind.hostTypeTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

            mBind.hostTypeTabs.addTab(mBind.hostTypeTabs.newTab().setCustomView(tabItemRL));
        }


        //假数据

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
            String bannerUrl;
            if (jsonStr.endsWith("{") && jsonStr.endsWith("}")) {
                bannerUrl = LanguageUtilsEntity.getLanguage(new Gson().fromJson(jsonStr, LanguageUtilsEntity.class));
            } else {
                bannerUrl = jsonStr;
            }

            GlideUtils.loadDefaultImage(context, bannerUrl, bannerImg);
        }
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
                doGetLiveListApi(1);
            }
            doGetLiveRecommendApi();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hasBanner) {
            String bannerJson = SPUtils.getInstance("ad_banner").getString("content", new Gson().toJson(bannerList));
            ArrayList<Advert> arrayList = GsonUtil.getObjects(bannerJson, Advert[].class);
            if (arrayList.size() > 0) {
                bannerList.addAll(arrayList);
                initConvenientBanner(bannerList);
            }
        }
    }
}
