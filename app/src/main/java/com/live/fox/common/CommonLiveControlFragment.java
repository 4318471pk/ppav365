package com.live.fox.common;

import static com.live.fox.dialog.BetCartDialogFragment.NOT_FORM_MINUTEGAMEDIALOGFRAGMENT;
import static com.live.fox.entity.MessageEvent.APPEND_BET_TYPE;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_FF;
import static com.live.fox.entity.cp.CpOutputFactory.TYPE_CP_HNCP;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.live.fox.AnchorLiveActivity;
import com.live.fox.AppConfig;
import com.live.fox.BuildConfig;
import com.live.fox.Constant;
import com.live.fox.LiveControlFragment;
import com.live.fox.R;
import com.live.fox.adapter.AudienceAdapter;
import com.live.fox.adapter.LiveRoomChatAdapter;
import com.live.fox.adapter.PkGiftAdapter;
import com.live.fox.adapter.RecommendLiveListAdapter;
import com.live.fox.base.BaseFragment;
import com.live.fox.db.DataBase;
import com.live.fox.dialog.AudienceListDialog;
import com.live.fox.dialog.BetCartDialogFragment;
import com.live.fox.dialog.DialogFactory;
import com.live.fox.dialog.FCartDialogFragment;
import com.live.fox.dialog.FGameDialogFragment;
import com.live.fox.dialog.GameDialogFragment;
import com.live.fox.dialog.GameListFragment;
import com.live.fox.dialog.GameWebViewFragment;
import com.live.fox.dialog.HNDialogFragment;
import com.live.fox.dialog.KJDialogFragment;
import com.live.fox.dialog.LevelUpDialog;
import com.live.fox.dialog.LiveRoomLetterFragment;
import com.live.fox.dialog.MinuteGameDialogFragment;
import com.live.fox.dialog.MyShareDialog;
import com.live.fox.dialog.RedBagRainStatusDialog;
import com.live.fox.dialog.UserDetailForCardFragment;
import com.live.fox.entity.Advert;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.Audience;
import com.live.fox.entity.ChatEntity;
import com.live.fox.entity.Game;
import com.live.fox.entity.Gift;
import com.live.fox.entity.LanguageUtilsEntity;
import com.live.fox.entity.Letter;
import com.live.fox.entity.LiveStartLotteryEntity;
import com.live.fox.entity.MessageEvent;
import com.live.fox.entity.PkStatus;
import com.live.fox.entity.ReceiveGiftBean;
import com.live.fox.entity.User;
import com.live.fox.entity.response.ChipsVO;
import com.live.fox.entity.response.GamePeriodInfoVO;
import com.live.fox.entity.response.RedBagRainBean;
import com.live.fox.entity.response.RedBagRainGetBean;
import com.live.fox.language.MultiLanguageUtils;
import com.live.fox.manager.DataCenter;
import com.live.fox.svga.AdmissionManager;
import com.live.fox.svga.GiftManager;
import com.live.fox.manager.SPManager;
import com.live.fox.server.Api_Cp;
import com.live.fox.server.Api_Live;
import com.live.fox.server.Api_Pay;
import com.live.fox.server.Api_User;
import com.live.fox.svga.ShowBigGiftFragment;

import com.live.fox.ui.live.PiaopingFragment;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.ui.rank.AnchorRankActivity;
import com.live.fox.manager.AppUserManger;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.ClickUtil;
import com.live.fox.utils.FragmentContentActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.IntentUtils;
import com.live.fox.utils.KeyboardUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.NumberUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.ViewUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.view.ChatPanelView;
import com.live.fox.view.GiftEffectFragment;
import com.live.fox.view.GiftPanelView;
import com.live.fox.view.LiveRoomBanner;
import com.live.fox.view.danmukux.DanmuContainerLuckView;
import com.live.fox.view.danmukux.DanmuEntity;
import com.live.fox.view.danmukux.DanmuKJJGViewa;
import com.live.fox.view.danmukux.DanmuKJJGViewb;
import com.live.fox.view.danmukux.DanmuMgrKJJGa;
import com.live.fox.view.danmukux.DanmuMgrKJJGb;
import com.live.fox.view.danmukux.DanmuMgrLuck;
import com.lzy.okgo.model.Response;
import com.makeramen.roundedimageview.RoundedImageView;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGADynamicEntity;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 直播间控制界面的公共类
 * 这里主要抽取了主播端和客户端相同的代码
 */
public class CommonLiveControlFragment extends BaseFragment implements
        ChatPanelView.OnChatActionListener, View.OnClickListener {

    protected ConstraintLayout rl_play_root;
    protected ShapeableImageView anchorAvatar;
    protected TextView tvAchorname;
    protected ImageView btnFollow;
    protected View followMargin;
    protected RecyclerView rvAudience;
    protected RecyclerView chatRecycleView;
    protected TextView unreadMessage;
    protected ImageView ivSetting;
    protected FrameLayout flBottomLayout;
    protected LinearLayout bottomButtonLayout;
    protected TextView layoutMl;
    protected TextView layoutRq; //人气，主播魅力
    protected TextView tvChat;
    protected RoundedImageView ivAd;
    protected RelativeLayout imageAdLayout;
    protected LinearLayout layoutPkprogressbar;
    protected ProgressBar pkProgressBar;
    protected TextView tvUserA;
    protected TextView tvUserB;
    protected FrameLayout layoutPkprogress;
    protected TextView tvCountdown;
    protected RoundLinearLayout layoutPkcountdown;
    protected ImageView ivPkPingju;
    protected ImageView ivPkResult1;
    protected ImageView ivPkResult2;
    protected ImageView ivPkTxt;
    protected TextView tvPkTxt;
    protected LinearLayout layoutFollow;
    protected TextView tvUserBName;
    protected RoundTextView tvUserBFoloow;
    protected LiveRoomBanner convenientBanner;
    protected RelativeLayout layout_user2;
    protected ImageView ivCp;
    protected ImageView ivCpTop;
    protected ImageView ivCpTop2;
    protected LinearLayout ll_cptop;
    protected LinearLayout ll_cptop2;
    protected TextView tv_cptop;
    protected TextView tv_cptop2;
    protected RecyclerView rvA;
    protected RecyclerView rvB;
    protected TextView tv_djs;
    protected SVGAImageView svgaView;
    protected ViewGroup rlRedBagRain;
    protected ImageView ivRedBagRainIcon;
    protected TextView tvRedBagRainTime;
    protected ImageView ivRedBagRainClose;
    protected WebView mWebView;

    protected boolean isGetResult = true;
    protected boolean isGetResult2 = true;
    protected boolean isShowGame = true;
    protected boolean isShowCp = true;

    protected Anchor anchor;
    protected boolean isInit = false;
    protected AudienceAdapter audienceAdapter;
    protected long lastEnterUserId = -1; //最后一条进房消息的用户Uid
    protected long lastFollowUserId = -1; //最后一条进房消息的用户Uid

    protected boolean isSilent = false; //  是否被主播禁言

    protected GiftEffectFragment giftEffectFragment;
    protected ShowBigGiftFragment showBigGiftFragment;

    protected ArrayList<ChatEntity> mChatEntityList;//聊天列表
    protected LiveRoomChatAdapter chatAdapter; // 聊天列表适配器
    protected int unreadMessageNum;//原来消息数量
    protected int enterMsgIndex = -1;   //进房消息位置
    protected int currentRoomType = -1;
    protected int currentRoomPrice = -1;

    protected DanmuMgrLuck piaopingMgr; //豪氣禮物飄屏
    protected DanmuMgrKJJGa kjjgMgr;
    protected DanmuMgrKJJGb kjjgMgr2;
    protected final Gson mGson = new Gson();//由于需要频繁创建  所以全局

    protected int pkCountDownTime;
    protected boolean isStartPKCountdown = false;
    public boolean isAnchor = false;   //是否是直播
    ArrayList<ChatEntity> tempList = new ArrayList<>(); //聊天列表

    protected List<Advert> liveRoomTextAdList;     //直播间文字广告列表
    protected List<Advert> liveRoomImageAdList;    //直播间图片广告列表
    protected List<Advert> liveRoomPiaopingAdList; //直播间飘屏广告列表
    protected int liveRoomTextAdIndex = 0;
    protected int liveRoomImageAdIndex = 0;
    protected int liveRoomPiaopingAdIndex = 0;

    protected MyShareDialog shareDialog;
    protected LevelUpDialog levelUpDialog;
    protected PiaopingFragment piaopingFragment;

    protected int onePx;
    protected long mLastClickTime = 0;
    public static final long TIME_INTERVAL = 1000L;

    //true 主播自己开的直播间  false 观众进入主播直播间
    protected boolean isAnchorLiveRoom = true;

    protected User userInfo;
    protected PkGiftAdapter aAdapter;
    protected PkGiftAdapter bAdapter;
    protected long timelong;//单位分钟
    protected static long S_COUNTDOWN = 60;
    protected long timelong2;//单位分钟
    protected static long S_COUNTDOWN2 = 60;
    protected boolean flagc1 = true;
    protected boolean flagc2 = true;

    protected List<LiveStartLotteryEntity> liveStartLottery;

    protected GameDialogFragment gameDialogFragment;
    protected SmartRefreshLayout recommendListSrl;
    protected ParentNotDealEventRecyclerview recommendList;
    protected ImageView recommendButton;

    protected List<Anchor> sourceListData;  //推荐列表元数据
    protected RecommendLiveListAdapter recommendLiveListAdapter;
    protected LiveRoomLetterFragment liveRoomLetterFragment;

    protected UserDetailForCardFragment userDetailForCardFragment;
    protected GiftPanelView mGiftPanelView;
    protected boolean textAdFlag = true;  //文字广告

    protected MediaPlayer player = null;
    protected ChatPanelView mChatPanelView;
    protected EditText chatEt;
    protected int pkType;
    protected boolean isPKing = false;
    protected PkStatus pkStatus;
    protected float chatRvdownX = 0;
    protected float chatRvdownY = 0;
    private int preview = -1;  //是否是预览房间 0不是，1是
    public static String lotteryCustom;
    private View titleBg;
    private RedBagRainBean mRedBagRainBean;
    private GamePeriodInfoVO mCpGameResultInfoVO;
    private GamePeriodInfoVO mCpGameResultInfoVO2;

    public static LiveControlFragment newInstance(Anchor anchor) {
        LiveControlFragment fragment = new LiveControlFragment();
        Bundle args = new Bundle();
        args.putSerializable("anchor", anchor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.live_control_fragment, container, false);
            initView(rootView);
        }
        lotteryCustom = getString(R.string.custom);
        initData(getArguments());
        return rootView;
    }

    private void initView(View rootView) {
        rl_play_root = rootView.findViewById(R.id.rl_play_root);
        anchorAvatar = rootView.findViewById(R.id.live_control_title_avatar);
        tvAchorname = rootView.findViewById(R.id.live_control_title_name);
        btnFollow = rootView.findViewById(R.id.live_control_title_follow);
        followMargin = rootView.findViewById(R.id.live_control_title_margin);
        rvAudience = rootView.findViewById(R.id.live_control_title_audience);
        chatRecycleView = rootView.findViewById(R.id.live_control_recommend_chat);
        unreadMessage = rootView.findViewById(R.id.unread_message);
        ivSetting = rootView.findViewById(R.id.iv_setting);
        flBottomLayout = rootView.findViewById(R.id.layout_bottom);
        bottomButtonLayout = rootView.findViewById(R.id.ll_bottom_button);
        layoutMl = rootView.findViewById(R.id.live_control_title_gift);
        layoutRq = rootView.findViewById(R.id.live_control_title_charm);
        tvChat = rootView.findViewById(R.id.tv_chat);
        ivAd = rootView.findViewById(R.id.iv_ad);
        imageAdLayout = rootView.findViewById(R.id.rl_imagead);
        layoutPkprogressbar = rootView.findViewById(R.id.layout_pkprogressbar);
        pkProgressBar = rootView.findViewById(R.id.progressBar_pk);
        tvUserA = rootView.findViewById(R.id.tv_userA);
        tvUserB = rootView.findViewById(R.id.tv_userB);
        layoutPkprogress = rootView.findViewById(R.id.layout_pkprogress);
        tvCountdown = rootView.findViewById(R.id.tv_countdown);
        layoutPkcountdown = rootView.findViewById(R.id.layout_pkcountdown);
        ivPkPingju = rootView.findViewById(R.id.iv_pk_pingju);
        ivPkResult1 = rootView.findViewById(R.id.iv_pkresult1);
        ivPkResult2 = rootView.findViewById(R.id.iv_pkresult2);
        ivPkTxt = rootView.findViewById(R.id.iv_pktxt);
        tvPkTxt = rootView.findViewById(R.id.tv_pktxt);
        layoutFollow = rootView.findViewById(R.id.layout_follow);
        tvUserBName = rootView.findViewById(R.id.tv_userB_name);
        tvUserBFoloow = rootView.findViewById(R.id.tv_userB_follow);
        convenientBanner = rootView.findViewById(R.id.convenientBanner);
        layout_user2 = rootView.findViewById(R.id.layout_user2);
        ivCp = rootView.findViewById(R.id.live_play_iv_cp);
        ivCpTop = rootView.findViewById(R.id.iv_cptop);
        ivCpTop2 = rootView.findViewById(R.id.iv_cptop2);
        ll_cptop = rootView.findViewById(R.id.ll_cptop);
        ll_cptop2 = rootView.findViewById(R.id.ll_cptop2);
        tv_cptop = rootView.findViewById(R.id.tv_cptop);
        tv_cptop2 = rootView.findViewById(R.id.tv_cptop2);
        rvA = rootView.findViewById(R.id.rvA);
        rvB = rootView.findViewById(R.id.rvB);
        tv_djs = rootView.findViewById(R.id.tv_djs);
        svgaView = rootView.findViewById(R.id.live_control_user_admission);
        rlRedBagRain = rootView.findViewById(R.id.llRedBagRain);
        ivRedBagRainIcon = rootView.findViewById(R.id.ivRedBagRainIcon);
        tvRedBagRainTime = rootView.findViewById(R.id.tvRedBagRainTime);
        ivRedBagRainClose = rootView.findViewById(R.id.ivRedBagRainClose);
        mWebView = rootView.findViewById(R.id.web_view);
        ivRedBagRainClose.setOnClickListener(this);
        ivRedBagRainIcon.setOnClickListener(this);
        ivCp.setOnClickListener(this);
        tvRedBagRainTime.setOnClickListener(this);
        rl_play_root.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        anchorAvatar.setOnClickListener(this);
        layoutMl.setOnClickListener(this);
        layoutRq.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
        unreadMessage.setOnClickListener(this);
        ll_cptop.setOnClickListener(this);
        ll_cptop2.setOnClickListener(this);
        layout_user2.setOnClickListener(this);
        rootView.findViewById(R.id.iv_gift).setOnClickListener(this);
        rootView.findViewById(R.id.iv_game).setOnClickListener(this);
        rootView.findViewById(R.id.iv_share).setOnClickListener(this);
        rootView.findViewById(R.id.layout_chat).setOnClickListener(this);
        rootView.findViewById(R.id.iv_roomclose).setOnClickListener(this);
        rootView.findViewById(R.id.iv_kaijiang).setOnClickListener(this);

        DanmuContainerLuckView haoqiluckView = rootView.findViewById(R.id.haoqi_gift_luck);
        piaopingMgr = new DanmuMgrLuck(getContext());
        piaopingMgr.setDanmakuView(haoqiluckView);
        initWebView();
        initChildView(rootView);
    }

    public void initChildView(View view) {

    }

    public void setPreview(int preview) {
        this.preview = preview;
    }

    public void setView(View view) {
        isInit = true;
        //true 主播自己直播间 false 观众直播间
        isAnchorLiveRoom = !(requireActivity() instanceof PlayLiveActivity);
        userInfo = DataCenter.getInstance().getUserInfo().getUser();
        tvCountdown.getPaint().setFakeBoldText(true);
        tvPkTxt.getPaint().setFakeBoldText(true);

        onePx = DeviceUtils.px2dp(CommonApp.getInstance(), 1);

        showBigGiftFragment = ShowBigGiftFragment.newInstance();

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.gift_content, showBigGiftFragment)
                .commitAllowingStateLoss();

        //礼物侧边栏展示
        giftEffectFragment = new GiftEffectFragment();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.gift_content, giftEffectFragment)
                .commitAllowingStateLoss();

        DanmuKJJGViewa cpjg1 = view.findViewById(R.id.live_lottery_open_copy);
        DanmuKJJGViewb cpjg2 = view.findViewById(R.id.live_lottery_open);
        kjjgMgr = new DanmuMgrKJJGa(getContext());
        kjjgMgr.setDanmakuView(cpjg1);
        kjjgMgr2 = new DanmuMgrKJJGb(getContext());
        kjjgMgr2.setDanmakuView(cpjg2);

        //带图片的飘萍
        piaopingFragment = PiaopingFragment.newInstance();
        requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.haoqi_gift_luck1, piaopingFragment)
                .commitAllowingStateLoss();

        String bannerContent = SPUtils.getInstance("ad_liveroomBanner").getString("content", "");
        if (!StringUtils.isEmpty(bannerContent)) {
            List<Advert> bannerAdList = GsonUtil.getObjects(bannerContent, Advert[].class);
            convenientBanner.setVisibility(View.VISIBLE);
            if (!bannerAdList.isEmpty()) {
                convenientBanner.setData(CommonApp.getInstance(), bannerAdList);
            }
        }

        ivCp.setVisibility(SPManager.getIsCpButton() == 0 ? View.VISIBLE : View.GONE);

        layoutMl.setVisibility(View.INVISIBLE);
        layoutRq.setVisibility(View.INVISIBLE);

        initAudienceRv();
        initChatRv();

        if (isAnchorLiveRoom) {
            refreshLiveRoom(anchor, false);
        } else {
            initRedBagRain(TYPE_INIT_RED_BAG_RAIN_GET);
            ivSetting.setVisibility(View.GONE);
        }
        rvA.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
        rvB.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvA.setAdapter(aAdapter = new PkGiftAdapter(new ArrayList<>(), requireActivity()));
        rvB.setAdapter(bAdapter = new PkGiftAdapter(new ArrayList<>(), requireActivity()));
        if (!isAnchor) {  //非主播
            handRecommendList(view);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView() {
        mWebView.setBackgroundColor(0);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        //是否允许脚本支持
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                showMsg(message, result);
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                showMsg(message, result);
                result.cancel();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                showMsg(message, result);
                result.cancel();
                return true;
            }

            private void showMsg(String msg, JsResult result) {
                result.cancel();
                if (!TextUtils.isEmpty(msg) && msg.contains("GetRedBag")) {
                    Api_Cp.ins().getRedBagRain(mRedBagRainBean.getId(), new JsonCallback<RedBagRainGetBean>() {
                        @Override
                        public void onSuccess(int code, String msg, RedBagRainGetBean data) {
                            if (code == 0) {
                                RedBagRainStatusDialog.newInstanceGetMoney(
                                        data.getMoney()).show(getChildFragmentManager(), "RedBagRainStatusDialog");
                            } else {
                                RedBagRainStatusDialog.newInstance(msg)
                                        .show(getChildFragmentManager(), "RedBagRainStatusDialog");
                            }
                        }
                    });
                } else {
                    mWebView.setVisibility(View.GONE);
                    RedBagRainStatusDialog.newInstance(msg)
                            .show(getChildFragmentManager(), "redBagRainStatusDialog");
                }
            }
        });
    }

    private CountDownTimer mCountdownTimer;
    private final long nh = 1000 * 60 * 60;
    private final long nm = 1000 * 60;
    private final long ns = 1000;
    private final int TYPE_INIT_RED_BAG_RAIN_GET = 0;//初始化红包雨数据
    private final int TYPE_INIT_RED_BAG_RAIN_CLICK = 1;//点击红包
    private final int TYPE_INIT_RED_BAG_RAIN_START = 2;//红包雨开始（网页下红包雨动画）
    private boolean mRedBagClick = false;
    private final Runnable mRedBagWebRunnable = new Runnable() {
        @Override
        public void run() {
            mWebView.setVisibility(View.GONE);
        }
    };

    private void initRedBagRain(int type) {
        Api_Cp.ins().getRedBagRainInfo(new JsonCallback<RedBagRainBean>() {
            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                mRedBagRainBean = null;
            }

            @Override
            public void onSuccess(int code, String msg, RedBagRainBean redBagRainBean) {
                if (mCountdownTimer != null) {
                    mCountdownTimer.cancel();
                    mCountdownTimer = null;
                }
                mRedBagRainBean = redBagRainBean;
                if (mRedBagRainBean == null || !redBagRainBean.isShowRedBag()) {
                    rlRedBagRain.setVisibility(View.GONE);
                    return;
                }
                rlRedBagRain.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(redBagRainBean.getIconUrl())) {
                    GlideUtils.loadImage(getActivity(), redBagRainBean.getIconUrl(), ivRedBagRainIcon);
                }
                if (redBagRainBean.getActivityStatus() == 2 || redBagRainBean.getActivityStatus() == 3) {
                    tvRedBagRainTime.setText(R.string.activity_in_progress);
                } else {
                    if (TimeUtils.getCurrentTime() < redBagRainBean.getLatelyStartTime()) {
                        long millisInFuture = redBagRainBean.getLatelyStartTime() - TimeUtils.getCurrentTime();
                        mCountdownTimer = new CountDownTimer(millisInFuture, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                // 计算差多少小时
                                long hour = millisUntilFinished / nh;
                                // 计算差多少分钟
                                long min = millisUntilFinished % nh / nm;
                                // 计算差多少秒//输出结果
                                long s = millisUntilFinished % nh % nm / ns;
                                String hourStr = String.valueOf(hour);
                                if (hour < 10) {
                                    hourStr = "0" + hourStr;
                                }
                                String minStr = String.valueOf(min);
                                if (min < 10) {
                                    minStr = "0" + minStr;
                                }
                                String sStr = String.valueOf(s);
                                if (s < 10) {
                                    sStr = "0" + sStr;
                                }
                                String time = hourStr + ":" + minStr + ":" + sStr;
                                tvRedBagRainTime.setText(time);
                            }

                            @Override
                            public void onFinish() {
                                tvRedBagRainTime.setText(R.string.activity_in_progress);
                            }
                        };
                        mCountdownTimer.start();
                    } else {
                        tvRedBagRainTime.setText(R.string.activity_in_progress);
                    }
                }
                switch (redBagRainBean.getActivityStatus()) {
                    case 1:
                    case 2:
                        if (type == TYPE_INIT_RED_BAG_RAIN_CLICK) {
                            RedBagRainStatusDialog.newInstance(
                                    redBagRainBean.getActivityStatus()).show(getChildFragmentManager(), "redBagRainStatusDialog");
                            mRedBagClick = false;
                        }
                        break;
                    case 3:
                        if (type == TYPE_INIT_RED_BAG_RAIN_CLICK && mWebView.getVisibility() == View.GONE) {
                            Api_Cp.ins().getRedBagRain(redBagRainBean.getId(), new JsonCallback<RedBagRainGetBean>() {
                                @Override
                                public void onSuccess(int code, String msg, RedBagRainGetBean data) {
                                    if (code == 0) {
                                        RedBagRainStatusDialog.newInstanceGetMoney(data.getMoney()).show(getChildFragmentManager(), "RedBagRainStatusDialog");
                                    } else {
                                        RedBagRainStatusDialog.newInstance(msg).show(getChildFragmentManager(), "RedBagRainStatusDialog");
                                    }
                                    mRedBagClick = false;
                                }
                            });
                        } else if (type == TYPE_INIT_RED_BAG_RAIN_START && redBagRainBean.getAnimationTime() > 0) {
                            mWebView.setVisibility(View.VISIBLE);
                            String redBagRainUrl = "https://oss.tenqqlive.com/hongbaoyu/index.html";
                            String language = MultiLanguageUtils.getRequestHeader();
                            switch (BuildConfig.AppFlavor) {
                                // 注释备用
//                                case "MMLive":
//                                    redBagRainUrl = "http://oss.tenqqlive.com/hongbaoyu/index.html";
//                                    break;
                                case "QQLive":
                                    redBagRainUrl = "https://oss.tenqqlive.com/hongbaoyu/index.html";
                                    language = "YN";
                                    break;
//                                case "AiAi":
//                                    redBagRainUrl = "http://oss.tenqqlive.com/hongbaoyu/index.html";
//                                    break;
//                                case "Live24":
//                                    redBagRainUrl = "http://oss.tenqqlive.com/hongbaoyu/index.html";
//                                    break;
                                case "ThiLive":
                                    redBagRainUrl = "http://www.thlive-oss.com/hongbaoyu/index.html";
                                    break;
                            }
                            String url = redBagRainUrl + "?language=" +
                                    language +
                                    "&animationTime=" + redBagRainBean.getAnimationTime();
                            mWebView.loadUrl(url);
                            mWebView.postDelayed(mRedBagWebRunnable, redBagRainBean.getAnimationTime() * 1000L);
                        }
                        break;
                }
            }
        });
    }

    protected Handler pkCountdownHandler = new Handler(message -> {
        pkCountDown();
        return false;
    });

    public void pkCountDown() {
        pkCountdownHandler.removeMessages(1);
        pkCountDownTime = pkCountDownTime - 1;
        LogUtils.e("pk倒计时:" + pkCountDownTime);
        tvCountdown.setText(String.valueOf(pkCountDownTime));
    }

    public void initData(Bundle bundle) {
        EventBus.getDefault().register(this);
        if (bundle != null) {
            anchor = (Anchor) bundle.getSerializable("anchor");
            if (1 == anchor.getLiveStatus() || 3 == anchor.getLiveStatus()) {
                initLotteryData(anchor);
            }
        }
        mChatEntityList = new ArrayList<>();
        if (0 == anchor.getLiveStatus()) {
            flBottomLayout.setVisibility(View.INVISIBLE);
        }

        setView(rootView);
    }

    public void dismissCpListDialog() {
        if (gameDialogFragment != null && gameDialogFragment.isAdded()) {
            gameDialogFragment.dismiss();
        }
    }

    public void initLotteryData(Anchor anchor) {
        liveStartLottery = anchor.getLiveStartLottery();
        flagc1 = true;
        flagc2 = true;
        showLotteryType(true);
    }

    public void showLotteryType(boolean flag) {
        if (liveStartLottery != null && !liveStartLottery.isEmpty()) {
            if (liveStartLottery.size() == 2) {
                if (SPManager.getIsCpButton() == 0) {
                    if (flag) {
                        doGetCpInfo();
                        doGetCpInfo2();
                        GlideUtils.loadImage(getActivity(), liveStartLottery.get(0).getLorretyIcon(), ivCpTop);
                        GlideUtils.loadImage(getActivity(), liveStartLottery.get(1).getLorretyIcon(), ivCpTop2);
                    }
                    ll_cptop2.setVisibility(View.VISIBLE);
                } else {
                    ll_cptop2.setVisibility(View.GONE);
                }
                ll_cptop.setVisibility(mCpGameResultInfoVO == null ? View.GONE : View.VISIBLE);
                ll_cptop2.setVisibility(mCpGameResultInfoVO2 == null ? View.GONE : View.VISIBLE);
            } else {
                if (SPManager.getIsCpButton() == 0) {// 0 开启  1不开启
                    if (flag) {
                        doGetCpInfo();
                        GlideUtils.loadImage(getActivity(), liveStartLottery.get(0).getLorretyIcon(), ivCpTop);
                    }
                }
                ll_cptop.setVisibility(mCpGameResultInfoVO == null ? View.GONE : View.VISIBLE);
                ll_cptop2.setVisibility(View.GONE);
            }
        }
    }

    public CountDownTimer timer = new CountDownTimer(10000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            if (isAdded()) {
                tv_djs.setText(Html.fromHtml(getString(R.string.djs) + "<font color='#EB4A81'>" + (millisUntilFinished / 1000) + "s</font>"));
            }
        }

        @Override
        public void onFinish() {
            if (isAdded()) {
                tv_djs.setVisibility(View.GONE);
            }
        }
    };

    public void doGetCpInfo() {
        if (liveStartLottery != null && liveStartLottery.size() > 0) {
            String name = AppConfig.isThLive() ? liveStartLottery.get(0).getLotteryName() : liveStartLottery.get(0).getCpName();
            Api_Cp.ins().getCp(name, new JsonCallback<GamePeriodInfoVO>() {
                @Override
                public void onSuccess(int code, String msg, GamePeriodInfoVO cpGameResultInfoVO) {
                    mCpGameResultInfoVO = cpGameResultInfoVO;
                    if (cpGameResultInfoVO != null) {
                        timelong = cpGameResultInfoVO.getTimelong();
                        S_COUNTDOWN = cpGameResultInfoVO.getDown_time() * 1000L + SystemClock.elapsedRealtime();
                        if (flagc1) {
                            tv_cptop.postDelayed(mRunnable, 0);
                            flagc1 = false;
                        }
                    }
                    ll_cptop.setVisibility(mCpGameResultInfoVO == null ? View.GONE : View.VISIBLE);
                }
            });
        }
    }

    protected final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isAdded()) {
                return;
            }

            long formatTitle;
            if (liveStartLottery != null && liveStartLottery.size() > 0 &&
                    CommonApp.LOTERNAME.equals(liveStartLottery.get(0).getLotteryName())) {
                formatTitle = CommonApp.S_COUNTDOWNa - SystemClock.elapsedRealtime();
            } else {
                formatTitle = S_COUNTDOWN - SystemClock.elapsedRealtime();
            }

            long second = formatTitle / 1000;
            String text = null;
            if (tv_cptop == null) return;

            if (timelong == 1) {//一分彩游戏
                if (second > 0) {
                    if (second >= 55 && second <= 59) {
                        text = getString(R.string.closing);
                    } else {
                        if (second < 10) {
                            text = "00:0" + second;
                        } else {
                            text = "00:" + second;
                        }
                    }
                } else {
                    text = getString(R.string.closing);
                    doGetCpInfo();
                }
                tv_cptop.postDelayed(this, 1000);
            } else if (timelong > 1) {//1分钟以上 一期
                long m = second / 60;
                long sec = second % 60;
                long temp = timelong * 60 - 5;
                if (second > 0) {
                    if (second >= temp && second <= timelong * 60) {
                        text = getString(R.string.closing);
                    } else {
                        isGetResult = true;
                        if (m < 10) {
                            if (sec < 10) {
                                text = "0" + m + ":0" + sec;
                            } else {
                                text = "0" + m + ":" + sec;
                            }
                        } else {
                            if (sec < 10) {
                                text = m + ":0" + sec;
                            } else {
                                text = m + ":" + sec;
                            }
                        }
                    }
                    tv_cptop.postDelayed(this, 1000);
                } else {
                    text = getString(R.string.closing);
                    if (liveStartLottery.get(0).getLotteryName().equals(TYPE_CP_HNCP)) {
                        if (isGetResult) {
                            tv_cptop.postDelayed(this, 28000);
                        } else {
                            tv_cptop.postDelayed(this, 1000);
                            doGetCpInfo();
                        }
                        isGetResult = false;
                    } else {
                        doGetCpInfo();
                    }
                }
            }
            tv_cptop.setText(text);
        }
    };

    public void doGetCpInfo2() {
        if (liveStartLottery != null && liveStartLottery.size() > 1) {
            String name = AppConfig.isThLive() ? liveStartLottery.get(1).getLotteryName() : liveStartLottery.get(1).getCpName();
            Api_Cp.ins().getCp(name, new JsonCallback<GamePeriodInfoVO>() {
                @Override
                public void onSuccess(int code, String msg, GamePeriodInfoVO cpGameResultInfoVO) {
                    mCpGameResultInfoVO2 = cpGameResultInfoVO;
                    if (cpGameResultInfoVO != null) {
                        timelong2 = cpGameResultInfoVO.getTimelong();
                        S_COUNTDOWN2 = cpGameResultInfoVO.getDown_time() * 1000L + SystemClock.elapsedRealtime();
                        if (flagc2) {
                            tv_cptop2.postDelayed(mRunnable2, 0);
                            flagc2 = false;
                        }
                    }
                    ll_cptop2.setVisibility(mCpGameResultInfoVO2 == null ? View.GONE : View.VISIBLE);
                }
            });
        }
    }

    protected Runnable mRunnable2 = new Runnable() {
        @Override
        public void run() {
            if (!isAdded()) {
                return;
            }
            long formatTitle = 0;

            if (liveStartLottery != null) {
                if (liveStartLottery.size() <= 1) {
                    tv_cptop2.removeCallbacks(mRunnable2);
                    return;
                }
                if (CommonApp.LOTERNAME.equals(liveStartLottery.get(1).getLotteryName())) {
                    formatTitle = CommonApp.S_COUNTDOWNa - SystemClock.elapsedRealtime();
                } else {
                    formatTitle = S_COUNTDOWN2 - SystemClock.elapsedRealtime();
                }
            }

            long second = formatTitle / 1000;
            String text = null;
            if (tv_cptop2 == null) return;
            if (timelong2 == 1) {//一分彩游戏
                if (second > 0) {
                    if (second >= 55 && second <= 59) {
                        text = getString(R.string.closing);
                    } else {
                        if (second < 10) {
                            text = "00:0" + second;
                        } else {
                            text = "00:" + second;
                        }
                    }
                } else {
                    text = getString(R.string.closing);
                    doGetCpInfo2();
                }
                tv_cptop2.postDelayed(this, 1000);
            } else if (timelong2 > 1) {//1分钟以上 一期
                long m = second / 60;
                long sec = second % 60;
                long temp = timelong2 * 60 - 5;
                if (second > 0) {
                    if (second >= temp && second <= timelong2 * 60) {
                        text = getString(R.string.closing);
                    } else {
                        isGetResult2 = true;
                        if (m < 10) {
                            if (sec < 10) {
                                text = "0" + m + ":0" + sec;
                            } else {
                                text = "0" + m + ":" + sec;
                            }
                        } else {
                            if (sec < 10) {
                                text = m + ":0" + sec;
                            } else {
                                text = m + ":" + sec;
                            }
                        }
                    }
                    tv_cptop2.postDelayed(this, 1000);
                } else {
                    text = getString(R.string.closing);
                    if (liveStartLottery.get(1).getLotteryName().equals(TYPE_CP_HNCP)) {
                        if (isGetResult2) {
                            tv_cptop2.postDelayed(this, 28000);
                        } else {
                            tv_cptop2.postDelayed(this, 1000);
                            doGetCpInfo2();
                        }
                        isGetResult2 = false;
                    } else {
                        doGetCpInfo2();
                    }
                }
            }
            tv_cptop2.setText(text);
        }
    };


    /**
     * 推荐列表数据
     *
     * @param view 根view
     */
    private void handRecommendList(View view) {
        recommendButton = view.findViewById(R.id.live_control_recommend_list_button);
        recommendListSrl = view.findViewById(R.id.live_control_recommend_list_srl);
        recommendList = view.findViewById(R.id.live_control_recommend_list);
        recommendButton.setVisibility(View.GONE);
        recommendListSrl.setEnableLoadMore(true);
        recommendListSrl.setOnRefreshListener(refreshLayout -> requestRecommendListData());
        recommendListSrl.setOnLoadMoreListener(refreshLayout -> requestRecommendListData());
        recommendButton.setOnClickListener(view1 -> {
            //如果显示就隐藏
            boolean isShow = recommendListSrl.getVisibility() == View.VISIBLE;
            recommendListSrl.setVisibility(isShow ? View.GONE : View.VISIBLE);
            recommendButton.setImageDrawable(ContextCompat.getDrawable(CommonApp.getInstance(), isShow ?
                    R.drawable.ic_arrow_left : R.drawable.ic_arrow_right));
        });

        recommendList.setLayoutManager(new LinearLayoutManager(CommonApp.getInstance()));
        recommendLiveListAdapter = new RecommendLiveListAdapter();
        recommendList.setAdapter(recommendLiveListAdapter);
        requestRecommendListData();
        recommendLiveListAdapter.setOnItemClickListener((adapter1, view12, position) -> {
            Anchor anchorClick = (Anchor) adapter1.getData().get(position);
            if (anchorClick != null) {
                hideRecommendList();
                if (requireActivity() instanceof PlayLiveActivity) {
                    PlayLiveActivity playLiveActivity = (PlayLiveActivity) requireActivity();
                    playLiveActivity.switchRoom(true, anchorClick);
                }
            }
        });

        View inflate = LayoutInflater.from(requireActivity()).inflate(R.layout.item_recommend_head, null);
        recommendLiveListAdapter.setHeaderView(inflate);
    }

    private void requestRecommendListData() {
//        Api_Live.ins().getRecommendLiveList(1, new JsonCallback<List<Anchor>>() {
//            @Override
//            public void onSuccess(int code, String msg, List<Anchor> data) {
//                if (data != null && data.size() > 0) {
//                    sourceListData = data;
//                    removeRecommendAnchor(anchor);
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                recommendListSrl.finishRefresh();
//                recommendListSrl.finishLoadMore();
//            }
//
//            @Override
//            public void onError(Response<String> response) {
//                super.onError(response);
//                recommendListSrl.finishRefresh();
//                recommendListSrl.finishLoadMore();
//            }
//        });
    }

    /**
     * 如果当前播放的主播在推荐列表中
     * 就移除推荐列表中的当前在播放的
     */
    public void removeRecommendAnchor(Anchor anchor) {
        if (sourceListData != null) {
            ArrayList<Anchor> temp = new ArrayList<>(sourceListData);
            if (!isAnchor) {
                for (int i = 0; i < temp.size(); i++) {
                    Anchor anchorList = temp.get(i);
                    if (anchorList.getLiveId() == anchor.getLiveId()) {
                        temp.remove(anchorList);
                    }
                }
                recommendButton.setVisibility(View.VISIBLE);
                recommendLiveListAdapter.setNewData(temp);
            }
        }
    }

    /**
     * 隐藏直播列表
     */
    public void hideRecommendList() {
        if (!isAnchor && recommendListSrl.getVisibility() == View.VISIBLE) {
            recommendListSrl.setVisibility(View.GONE);
            recommendButton.setImageDrawable(ContextCompat.getDrawable(CommonApp.getInstance(),
                    R.drawable.ic_arrow_left));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void sendChatMsg(String msg) {
        LogUtils.e("ChatMsg:" + msg);
        if (StringUtils.isEmpty(msg)) {
            return;
        }

        if (isSilent) {
            ToastUtils.showShort(getString(R.string.canNotSpeak));
            return;
        }

        byte[] byte_num = msg.getBytes(StandardCharsets.UTF_8);

        if (byte_num.length > 160) {
            ToastUtils.showShort(getString(R.string.contentLong));
            return;
        }

        mChatPanelView.changeStata(1);

        Api_Live.ins().chat(anchor.getLiveId(), msg, preview, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String result) {
                LogUtils.i("sendChatMsg：preview-->" + preview);
                mChatPanelView.changeStata(2);
                if (code == 2017) {
                    ToastUtils.showShort(msg);
                } else if (code != 0) {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    public void initAudienceRv() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        rvAudience.setLayoutManager(linearLayoutManager);
        audienceAdapter = new AudienceAdapter(new ArrayList<>());
        audienceAdapter.bindToRecyclerView(rvAudience);
        rvAudience.setAdapter(audienceAdapter);
        rvAudience.setHorizontalFadingEdgeEnabled(true);
        rvAudience.setFadingEdgeLength(50);
        audienceAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (ClickUtil.isFastDoubleClick()) return;
            if (position < 0) return;  //bugly有position=-1的情况
            Audience audience = audienceAdapter.getData().get(position);
            User user = new User();
            user.setUid(audience.getUid());
            user.setAvatar(audience.getAvatar());
            user.setUserLevel(audience.getUserLevel());
            if (audience.getChatHide() == 0) {
                user.setNickname(getString(R.string.mysteriousMan));
                showUserDetailDialog(user);
            } else {
                user.setNickname(audience.getNickname());
            }
        });
    }

    public void initChatRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chatRecycleView.setLayoutManager(layoutManager);
        chatRecycleView.setVerticalFadingEdgeEnabled(true);
        chatRecycleView.setFadingEdgeLength(50);
        chatAdapter = new LiveRoomChatAdapter(mChatEntityList);
        chatRecycleView.setAdapter(chatAdapter);
        chatRecycleView.scrollToPosition(0);
        chatRecycleView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == 0) {
                    unreadMessageNum = 0;
                    unreadMessage.setVisibility(View.GONE);
                } else if (firstVisibleItemPosition < unreadMessageNum) {
                    unreadMessageNum = firstVisibleItemPosition;
                    String str = unreadMessageNum + getString(R.string.numberNewMessage);
                    unreadMessage.setText(str);
                }
            }
        });

        chatAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (ClickUtil.isFastDoubleClick()) return;
            ChatEntity chatEntity = (ChatEntity) adapter.getData().get(position);
            if (chatEntity.getJsonObject().optInt("protocol") == Constant.MessageProtocol.PROTOCOL_FOCUS
                    || chatEntity.getJsonObject().optInt("protocol") == Constant.MessageProtocol.PROTOCOL_RECEIVE_GIFT
                    || chatEntity.getJsonObject().optInt("protocol") == Constant.MessageProtocol.PROTOCOL_CHAT) {
                if (chatEntity.getUser().chatHide == 0) {
                    showUserDetailDialog(chatEntity.getUser());
                }
            }
        });

        //recyclerView空白处点击事件
        chatRecycleView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                chatRvdownX = event.getX();
                chatRvdownY = event.getY();
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (v.getId() != 0 && Math.abs(chatRvdownX - event.getX()) <= 5 &&
                        Math.abs(chatRvdownY - event.getY()) <= 5) {
                    hideGiftPanel();
                    if (KeyboardUtils.isSoftInputVisible(requireActivity())) {
                        KeyboardUtils.hideSoftInput(requireActivity());
                    }
                    hideRecommendList();
                }
            }
            return false;
        });
    }

    public void dismissUserDetailDialog() {
        if (userDetailForCardFragment != null && userDetailForCardFragment.isShow) {
            if (requireActivity().getSupportFragmentManager().isStateSaved())
                return;
            userDetailForCardFragment.dismiss();
        }
    }

    /**
     * 隐藏礼物面板
     */
    private void hideGiftPanel() {
        final GiftPanelView giftPanelView = this.mGiftPanelView;
        if (giftPanelView != null && giftPanelView.isShowing()) {
            LogUtils.e("2");
            giftPanelView.hide();
        }
    }

    public void refreshLiveRoom(Anchor anchor, boolean isPay) {
        this.anchor = anchor;
        this.currentRoomType = anchor.getRoomType();
        this.currentRoomPrice = anchor.getPrice();
        if (!isInit) return;
        initRedBagRain(TYPE_INIT_RED_BAG_RAIN_GET);
        GlideUtils.loadCircleRingImage(requireActivity(), anchor.getAvatar(),
                onePx, Color.WHITE, R.drawable.img_default,
                R.drawable.img_default, anchorAvatar);

        tvAchorname.setText(anchor.getNickname());
        tvAchorname.setSelected(true);
        tvAchorname.setFocusable(true);
        refreshRq(anchor.getRq());
        refreshMl(anchor.getZb());

        ViewUtils.setVisiableWithAlphaAnim(layoutMl);
        ViewUtils.setVisiableWithAlphaAnim(layoutRq);

        if (isAnchorLiveRoom) {  //主播端
            followMargin.setVisibility(View.VISIBLE);
        } else {  //观众端
            timer.cancel();
            if ((1 == anchor.getType() || 2 == anchor.getType()) && !isPay) {
                tv_djs.setVisibility(View.VISIBLE);
                timer.start();
            } else {
                tv_djs.setVisibility(View.GONE);
            }

            if (anchor.getAnchorId() != DataCenter.getInstance().getUserInfo().getUser().getUid()) {
                btnFollow.setVisibility(anchor.isFollow() ? View.GONE : View.VISIBLE);
            }

            followMargin.setVisibility(anchor.isFollow() ? View.VISIBLE : View.GONE);
            refreshAudienceList();

            imageAdHandler.removeMessages(1);
            piaopingAdHandler.removeMessages(1);
            //进房2min后显示图片广告
            imageAdHandler.sendEmptyMessageDelayed(1, 20 * 1000);
            //进来时显示飘屏广告 之后每2分钟先试一次飘屏广告
            //piaopingAdHandler.sendEmptyMessageDelayed(1, 10 * 1000);
            //进来时显示文组广告 之后每2分钟刷新一次广告


            // 0 开启  1不开启
            LogUtils.e("getIsGameStart:" + SPManager.getIsGameStart());
            if (isShowGame && SPManager.getIsGameStart() == 0) {
                isShowGame = false;
                showGameListDialog();
            }

            if (isShowCp && isShowGame && SPManager.getIsCpStart() == 0) {
                isShowCp = false;
                if (Constant.isOpenWindow) {
                    showLotteryDialog();
                }
            }
        }

        //如果有座驾 则显示进房座驾
        if (anchor.getCarId() > 0) {
            Gift gift = DataBase.getDbInstance().getGiftByGid(anchor.getCarId());
            showBigGiftFragment.showBigGiftEffect(anchor.getCarId(), gift.getBimgs(),
                    gift.getType(), gift.getResourceUrl());
        }
    }

    /**
     * 发送房间公共
     */
    public void sendRoomBulletin() {
        textAdHandler.removeMessages(1);
        textAdHandler.sendEmptyMessage(1);
    }

    //飘屏广告
    private final Handler piaopingAdHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (liveRoomPiaopingAdList == null) {
                String content = SPUtils.getInstance("ad_liveroomPP").getString("content", "");
                if (!StringUtils.isEmpty(content)) {
                    liveRoomPiaopingAdList = GsonUtil.getObjects(content, Advert[].class);
                    liveRoomPiaopingAdIndex = 0;
                }
            } else {
                if (liveRoomPiaopingAdIndex == liveRoomPiaopingAdList.size() - 1) {
                    liveRoomPiaopingAdIndex = 0;
                } else {
                    liveRoomPiaopingAdIndex++;
                }
            }

            if (liveRoomPiaopingAdList != null) {
                Advert ad = liveRoomPiaopingAdList.get(liveRoomPiaopingAdIndex);
                String jsonStr = ad.getContent();
                String textStr;
                if (jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
                    textStr = LanguageUtilsEntity.getLanguage(new Gson().fromJson(jsonStr, LanguageUtilsEntity.class));
                } else {
                    textStr = jsonStr;
                }
                piaopingMgr.addRoomPiaoPingAd(textStr, ad.getJumpUrl());
                piaopingAdHandler.removeMessages(1);
                piaopingAdHandler.sendEmptyMessageDelayed(1, 2 * 60 * 1000);
            }
            return false;
        }
    });

    /**
     * 直播间公告
     */
    protected final Handler textAdHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (textAdFlag) {
                if (liveRoomTextAdList == null) {
                    String content = SPUtils.getInstance("ad_liveroomTxt").getString("content", "");
                    if (!StringUtils.isEmpty(content)) {
                        liveRoomTextAdList = GsonUtil.getObjects(content, Advert[].class);
                        liveRoomTextAdIndex = 0;
                    }
                } else {
                    if (liveRoomTextAdIndex == liveRoomTextAdList.size() - 1) {
                        liveRoomTextAdIndex = 0;
                    } else {
                        liveRoomTextAdIndex++;
                    }
                }

                if (liveRoomTextAdList != null) {
                    textAdFlag = false;
                    String jsonStr = liveRoomTextAdList.get(liveRoomTextAdIndex).getContent();
                    String textAd;
                    if (jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
                        textAd = LanguageUtilsEntity.getLanguage(new Gson().fromJson(jsonStr, LanguageUtilsEntity.class));
                    } else {
                        textAd = jsonStr;
                    }

                    if (StringUtils.isEmpty(textAd)) {
                        return false;
                    }
                    if (!isAnchorLiveRoom) {
                        PlayLiveActivity activity = (PlayLiveActivity) requireActivity();
                        activity.sendLiveRoomNotice(textAd);
                    } else {
                        AnchorLiveActivity activity = (AnchorLiveActivity) requireActivity();
                        activity.sendLiveRoomNotice(textAd);
                    }

                    textAdHandler.removeMessages(1);
                    //3 * 60 * 1000
                    textAdHandler.sendEmptyMessageDelayed(1, 0);
                }
            } else {
                textAdFlag = true;
                textAdHandler.removeMessages(1);
            }
            return false;
        }
    });

    public final Handler imageAdHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (getActivity() == null) return false;
            String content = SPUtils.getInstance("ad_liveroomImage").getString("content", "");
            if (!StringUtils.isEmpty(content)) {
                liveRoomImageAdList = GsonUtil.getObjects(content, Advert[].class);
                if (liveRoomImageAdList.size() > 1) {
                    liveRoomImageAdIndex = NumberUtils.getRandom(0, liveRoomImageAdList.size() - 1);
                } else {
                    liveRoomImageAdIndex = 0;
                }

                Advert roomAd = liveRoomImageAdList.get(liveRoomImageAdIndex);

                imageAdLayout.setVisibility(View.VISIBLE);
                GlideUtils.loadDefaultRoundedImage(requireActivity(), roomAd.getContent(), rootView.findViewById(R.id.iv_ad));
                rootView.findViewById(R.id.iv_ad).setOnClickListener(view -> {
                    if (!StringUtils.isEmpty(roomAd.getJumpUrl())) {
                        if (roomAd.getOpenWay() == 0) { //打开方式 0站内，1站外
                            FragmentContentActivity.startWebActivity(requireActivity(), "", roomAd.getJumpUrl());
                        } else {
                            IntentUtils.toBrowser(requireActivity(), roomAd.getJumpUrl());
                        }
                    }
                });
                rootView.findViewById(R.id.rl_imagead_close).setOnClickListener(view -> imageAdLayout.setVisibility(View.GONE));
            }
            return false;
        }
    });

    public void hidePkView() {
        setPkView(1, -1, null);
    }

    /**
     * 设置PK
     *
     * @param type     type=1 隐藏pk界面
     * @param pkResult pkResult只有在type=2时才用得到
     * @param pkStatus pkStatus是当前房间的Pk状态，只有在观众端用得到 没有就传null
     */
    public void setPkView(int type, int pkResult, PkStatus pkStatus) {
        if (pkStatus != null) {
            LogUtils.e("setPkView pkStatus " + mGson.toJson(pkStatus));
            if (!isAnchorLiveRoom) {
                this.pkStatus = pkStatus;
                layoutFollow.setVisibility(View.VISIBLE);
                tvUserBName.setVisibility(View.VISIBLE);
                tvUserBName.setText(pkStatus.getNickname());
                tvUserBFoloow.setVisibility(pkStatus.isFollow() ? View.GONE : View.VISIBLE);
                tvUserBFoloow.setOnClickListener(view -> {
                    if (ClickUtil.isFastDoubleClick()) return;
                    Api_User.ins().follow(pkStatus.getTargetId(), !pkStatus.isFollow(), new JsonCallback<String>() {
                        @Override
                        public void onSuccess(int code, String msg, String result) {
                            if (result != null) LogUtils.e("follow result : " + result);
                            if (code == 0 && result != null) {
                                pkStatus.setFollow(!pkStatus.isFollow());
                                tvUserBFoloow.setVisibility(pkStatus.isFollow() ? View.GONE : View.VISIBLE);
                                ToastUtils.showShort(pkStatus.isFollow() ? getString(R.string.successFocus) : getString(R.string.cancelFocus));
                            } else {
                                ToastUtils.showShort(msg);
                            }
                        }
                    });
                });
            }
        } else {
            layoutFollow.setVisibility(View.GONE);
        }

        this.pkType = type;
        switch (type) {
            case 0: //PK开启
                isPKing = true;
                layout_user2.setVisibility(View.VISIBLE);
                convenientBanner.setVisibility(View.GONE);
                this.isStartPKCountdown = true;
                layoutPkprogress.setVisibility(View.VISIBLE);
                layoutPkprogressbar.setVisibility(View.VISIBLE);
                layoutPkcountdown.setVisibility(View.VISIBLE);
                ivPkTxt.setVisibility(View.VISIBLE);
                tvPkTxt.setVisibility(View.GONE);
                ivPkPingju.setVisibility(View.GONE);
                ivPkResult1.setVisibility(View.GONE);
                ivPkResult2.setVisibility(View.GONE);
                showLotteryType(false);
                if (pkStatus == null) {
                    pkProgressBar.setProgress(50);
                    tvUserA.setText(getString(R.string.myPart) + "0");
                    tvUserB.setText("0" + getString(R.string.yourPart));
                    this.pkCountDownTime = 300;
                } else {
                    refreshPkScore(pkStatus.getScoreA(),
                            pkStatus.getScoreB(),
                            pkStatus.getListA(), pkStatus.getListB());
                    LogUtils.e("PK Time" + (System.currentTimeMillis() - pkStatus.getStartTime()));
                    this.pkCountDownTime = 300 - (int) (System.currentTimeMillis() - pkStatus.getStartTime()) / 1000;
                    if (pkCountDownTime <= 0) pkCountDownTime = 300;
                }
                tvCountdown.setText(String.valueOf(pkCountDownTime));
                pkCountdownHandler.removeMessages(1);
                pkCountdownHandler.sendEmptyMessageDelayed(1, 1000);
                break;

            case 1: //PK关闭
                showLotteryType(false);

                layout_user2.setVisibility(View.GONE);
                isPKing = false;
                this.pkStatus = null;
                convenientBanner.setVisibility(View.VISIBLE);
                this.isStartPKCountdown = false;
                pkCountdownHandler.removeMessages(1);
                layoutPkprogress.setVisibility(View.GONE);
                layoutPkprogressbar.setVisibility(View.GONE);
                layoutPkcountdown.setVisibility(View.GONE);
                layoutFollow.setVisibility(View.GONE);
                ivPkPingju.setVisibility(View.GONE);
                ivPkResult1.setVisibility(View.GONE);
                ivPkResult2.setVisibility(View.GONE);
                refreshPkScore(0, 0, null, null);
                break;

            case 2: //PK惩罚阶段开启
                pkCountdownHandler.removeMessages(1);
                layoutPkprogress.setVisibility(View.VISIBLE);
                layoutPkprogressbar.setVisibility(View.VISIBLE);
                layoutPkcountdown.setVisibility(View.VISIBLE);
                ivPkTxt.setVisibility(View.GONE);
                tvPkTxt.setVisibility(View.VISIBLE);
                if (pkStatus != null) pkResult = pkStatus.getResult();
                if (pkStatus == null) {
                    this.pkCountDownTime = 180;
                } else {
                    refreshPkScore(pkStatus.getScoreA(), pkStatus.getScoreB(),
                            pkStatus.getListA(), pkStatus.getListB());
                    LogUtils.e("PK Time" + (System.currentTimeMillis() - pkStatus.getStartTime()));
                    this.pkCountDownTime = 480 - (int) (System.currentTimeMillis() - pkStatus.getStartTime()) / 1000;
                    if (pkCountDownTime <= 0) pkCountDownTime = 180;
                }
                tvCountdown.setText(String.valueOf(pkCountDownTime));
                pkCountdownHandler.removeMessages(1);
                pkCountdownHandler.sendEmptyMessageDelayed(1, 1000);
                switch (pkResult) {
                    case 0: //pk已结束
                        setPkView(1, -1, null);
                        break;
                    case 1: //1胜
                        this.isStartPKCountdown = true;

                        ivPkPingju.setVisibility(View.GONE);
                        ivPkResult1.setVisibility(View.VISIBLE);
                        ivPkResult2.setVisibility(View.VISIBLE);
                        ivPkResult1.setImageResource(R.drawable.pk_victory);
                        ivPkResult2.setImageResource(R.drawable.pk_fail);
                        break;
                    case 2: //2负
                        this.isStartPKCountdown = true;

                        ivPkPingju.setVisibility(View.GONE);
                        ivPkResult1.setVisibility(View.VISIBLE);
                        ivPkResult2.setVisibility(View.VISIBLE);
                        ivPkResult1.setImageResource(R.drawable.pk_fail);
                        ivPkResult2.setImageResource(R.drawable.pk_victory);
                        break;
                    case 3: //3平局
                        ToastUtils.showShort(getString(R.string.equalPublish));
                        this.isStartPKCountdown = true;

                        ivPkPingju.setVisibility(View.VISIBLE);
                        ivPkResult1.setVisibility(View.GONE);
                        ivPkResult2.setVisibility(View.GONE);
                        break;
                }
                break;
        }
    }

    /**
     * Pk 中的比分
     */
    public void refreshPkScore(long scoreA,
                               long scoreB, List<User> mListA, List<User> mListB) {
        tvUserA.setText(getString(R.string.myPart) + scoreA);
        tvUserB.setText(scoreB + getString(R.string.yourPart));
        if (scoreA + scoreB == 0) {
            pkProgressBar.setProgress(50);
        } else {
            int progress = (int) (((float) scoreA / (float) (scoreA + scoreB)) * 100);
            LogUtils.e("PK " + progress);
            if (progress >= 97) progress = 97;
            pkProgressBar.setProgress(progress);
        }
        aAdapter.getData().clear();

        if (mListA != null) {

            if (!mListA.isEmpty()) {
                aAdapter.getData().addAll(mListA);
            }
        }
        aAdapter.notifyDataSetChanged();
        bAdapter.getData().clear();

        if (mListB != null) {
            if (!mListB.isEmpty()) {
                bAdapter.getData().addAll(mListB);
            }
        }
        bAdapter.notifyDataSetChanged();
    }

    /**
     * 显示游戏
     * 彩票投注
     */
    private void showLotteryDialog() {
        gameDialogFragment = GameDialogFragment.newInstance(anchor.getLiveId());
        if (!gameDialogFragment.isAdded()) {
            gameDialogFragment.show(requireActivity().getSupportFragmentManager(),
                    GameDialogFragment.class.getSimpleName());
        }
    }

    public void showGameListDialog() {
        GameListFragment gameListFragment = GameListFragment.newInstance();
        if (getActivity() == null || getActivity().getSupportFragmentManager().isStateSaved()) {
            return;
        }
        gameListFragment.show(getChildFragmentManager(), "");
    }

    /**
     * 刷新房间人气值
     */
    public void refreshRq(long rq) {
        layoutRq.setText(RegexUtils.formatNumber(rq));
    }

    /**
     * 刷新礼物
     */
    public void refreshMl(long ml) {
        layoutMl.setText(RegexUtils.formatNumber(ml));
    }

    /**
     * 刷新观众列表
     * 普通用戶根據用戶經驗排序
     */
    private void refreshAudienceList() {
        if (!isInit) return;
        Api_Live.ins().getAudienceList(anchor.getLiveId(), new JsonCallback<List<Audience>>() {
            @Override
            public void onSuccess(int code, String msg, List<Audience> result) {
                if (result != null) LogUtils.e("getAudience result : " + mGson.toJson(result));
                ViewUtils.setVisiableWithAlphaAnim(rvAudience);
                if (code == 0 && result != null) {
                    if (result.size() > 0) {
                        Collections.sort(result, (t0, t1) -> (int) t1.getUserExp() - (int) t0.getUserExp());
                        audienceAdapter.setNewData(result);
                    }
                }
            }
        });
    }

    /**
     * 切换房间时根据状态显示不一样的界面
     *
     * @param state         状态
     * @param currentAnchor 当前用户
     */
    public void switchRoomByState(int state, Anchor currentAnchor) {
        switch (state) {
            case 1://切换时时的处理
                rl_play_root.setVisibility(View.GONE);
                if (1 == currentAnchor.getLiveStatus() || 3 == currentAnchor.getLiveStatus()) {
                    flBottomLayout.setVisibility(View.VISIBLE);
                    layoutMl.setVisibility(View.VISIBLE);
                    layoutRq.setVisibility(View.VISIBLE);
                    rvAudience.setVisibility(View.VISIBLE);
                    convenientBanner.setVisibility(View.VISIBLE);
                    initLotteryData(currentAnchor);
                }

                enterMsgIndex = -1;
                unreadMessageNum = 0;
                unreadMessage.setVisibility(View.GONE);
                mChatEntityList.clear();
                chatAdapter.notifyDataSetChanged();
                showBigGiftFragment.clearGiftShow();

                //如果是用户端 则显示广告
                if (!isAnchorLiveRoom) {
                    sendRoomBulletin();

                    imageAdHandler.removeMessages(1);
                    imageAdHandler.sendEmptyMessageDelayed(1, 10 * 1000);

                    piaopingAdHandler.removeMessages(1);
                    piaopingAdHandler.sendEmptyMessageDelayed(1, 10 * 1000);
                }
                break;

            case 2://直播流播放成功后的处理
                if (rl_play_root != null) {
                    rl_play_root.setVisibility(View.VISIBLE);
                }
                if (0 == currentAnchor.getLiveStatus()) {
                    flBottomLayout.setVisibility(View.INVISIBLE);
                    layoutMl.setVisibility(View.INVISIBLE);
                    layoutRq.setVisibility(View.INVISIBLE);
                    rvAudience.setVisibility(View.INVISIBLE);
                    convenientBanner.setVisibility(View.INVISIBLE);
                    ll_cptop.setVisibility(View.INVISIBLE);
                    ll_cptop2.setVisibility(View.INVISIBLE);
                    unreadMessage.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (ClickUtil.isFastDoubleClick()) return;
        int id = view.getId();
        if (id == R.id.ivRedBagRainClose) {
            rlRedBagRain.setVisibility(View.GONE);
        } else if ((id == R.id.ivRedBagRainIcon || id == R.id.tvRedBagRainTime) && !mRedBagClick) {
            mRedBagClick = true;
            initRedBagRain(TYPE_INIT_RED_BAG_RAIN_CLICK);
        }

        if (id == R.id.rl_play_root) {
            clickPlayRoot();
        }

        if (id == R.id.live_control_title_follow) {
            if (ClickUtil.isFastDoubleClick()) return;
            Api_User.ins().follow(anchor.getAnchorId(), !anchor.isFollow(), new JsonCallback<String>() {
                @Override
                public void onSuccess(int code, String msg, String result) {
                    if (code == 0 && result != null) {
                        anchor.setFollow(!anchor.isFollow());
                        if (anchor.getAnchorId() != DataCenter.getInstance().getUserInfo().getUser().getUid()) {
                            btnFollow.setVisibility(anchor.isFollow() ? View.GONE : View.VISIBLE);
                        }
                        followMargin.setVisibility(anchor.isFollow() ? View.VISIBLE : View.GONE);
                        ToastUtils.showShort(anchor.isFollow() ? getString(R.string.successFocus) : getString(R.string.cancelFocus));
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            });
        }

        if (id == R.id.live_control_title_avatar) {
            User tempUser = new User();
            tempUser.setUid(anchor.getAnchorId());
            tempUser.setAvatar(anchor.getAvatar());
            tempUser.setNickname(anchor.getNickname());
            showUserDetailDialog(tempUser);
        }

        if (id == R.id.live_control_title_gift) {
            AnchorRankActivity.startActivity(getActivity(), anchor.getAnchorId());
        }

        if (id == R.id.live_control_title_charm) {
            AudienceListDialog audienceListDialog = AudienceListDialog.newInstance(anchor.getLiveId());
            audienceListDialog.show(getChildFragmentManager(), AudienceListDialog.class.getName());
        }

        if (id == R.id.iv_setting) {
            setLiveRoom();
        }

        if (id == R.id.iv_gift) {
            showGiftPanel();
        }

        if (id == R.id.iv_game) {
            showGameListDialog();
        }

        if (id == R.id.iv_share) {
            if (shareDialog == null) {
                shareDialog = new MyShareDialog(getActivity());
            }
            shareDialog.show();
        }

        if (id == R.id.layout_chat) {
            bottomButtonLayout.setVisibility(View.GONE);
            showChatPanel("");
            chatEt.requestFocus();
            KeyboardUtils.openSoftInput(chatEt);
        }


        if (id == R.id.unread_message) {
            final RecyclerView publicMessage = chatRecycleView;
            LinearLayoutManager layoutManager = (LinearLayoutManager) publicMessage.getLayoutManager();
            if (layoutManager != null) {
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                unreadMessage.setVisibility(View.GONE);
                if (firstVisibleItemPosition < 100) {
                    publicMessage.smoothScrollToPosition(0);//公聊滚到最底部
                } else {
                    publicMessage.scrollToPosition(0);//公聊直接到底部
                }
            }
            unreadMessageNum = 0;
        }

        if (id == R.id.iv_roomclose) {
            closeLiveRoom();
        }

        if (id == R.id.layout_user2) {
            goToPkRoom();
        }

        if (id == R.id.live_play_iv_cp) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - mLastClickTime > TIME_INTERVAL) {
                mLastClickTime = nowTime;
                showLotteryDialog();
            }
        }

        if (id == R.id.iv_kaijiang) {
            long nowTimea = System.currentTimeMillis();
            if (nowTimea - mLastClickTime > TIME_INTERVAL) {
                mLastClickTime = nowTimea;
                showKJDialog();
            }
        }

        if (id == R.id.ll_cptop) {
            long nowTime1 = System.currentTimeMillis();
            if (nowTime1 - mLastClickTime > TIME_INTERVAL) {
                mLastClickTime = nowTime1;
                showYFSSCDialog(0);
            }
        }

        if (id == R.id.ll_cptop2) {
            long nowTime2 = System.currentTimeMillis();
            if (nowTime2 - mLastClickTime > TIME_INTERVAL) {
                mLastClickTime = nowTime2;
                showYFSSCDialog(1);
            }
        }
    }


    public void showUserDetailDialog(User cardUser) {
        userDetailForCardFragment = UserDetailForCardFragment.newInstance(cardUser);
        userDetailForCardFragment.show(getChildFragmentManager(), "");
        Api_User.ins().getCardInfoData(cardUser.getUid(), anchor.getLiveId(), anchor.getAnchorId(),
                new JsonCallback<User>() {
                    @Override
                    public void onSuccess(int code, String msg, User data) {
                        if (code == 0 && data != null) {
                            if (userDetailForCardFragment.isShow) {
                                userDetailForCardFragment.refreshPage(true, data,
                                        anchor.getLiveId(), anchor.getAnchorId(),
                                        isAnchorLiveRoom, anchor.isRoomManager());
                            }
                        }
                    }
                });
    }

    /**
     * 设置房间
     * 只有主播端需要实现
     */
    public void setLiveRoom() {

    }

    /**
     * 关闭房间
     * 具体实现在子类中
     * 主播端可用户端不一样
     */
    public void closeLiveRoom() {
        if (ClickUtil.isFastDoubleClick()) return;
        if (KeyboardUtils.isSoftInputVisible(requireActivity())) {
            KeyboardUtils.hideSoftInput(requireActivity());
        }
    }

    /**
     * 点击根部
     */
    public void clickPlayRoot() {
        if (ClickUtil.isFastDoubleClick()) return;
        hideGiftPanel();

        //如果聊天框展开 点击外部隐藏聊天框
        if (KeyboardUtils.isSoftInputVisible(requireActivity())) {
            KeyboardUtils.hideSoftInput(requireActivity());
        }
        hideRecommendList();
    }

    /**
     * 去PK直播间
     */
    public void goToPkRoom() {
        LogUtils.e("PK中点击去了对方的直播间isPKing==" + isPKing + "pkStatus==" + pkStatus);
    }

    /**
     * 收到消息
     * 收到进房消息 如果此时的进房的人和最新一条的人相同 则不显示  其他情况下正常显示
     *
     * @param protocol 消息协议
     * @param msg      消息
     */
    public void onReceived(int protocol, JSONObject msg) {
        LogUtils.e("onReceived ： " + protocol + "，" + msg.toString());
        if (!isInit) {
            return;
        }

        if (protocol == Constant.MessageProtocol.PROTOCOL_AUDIENCE) { //进房退房消息
            Audience audience = mGson.fromJson(msg.toString(), Audience.class);
            //如果是进房消息 且上一个人跟现在是同一个人 则不处理
            if (msg.optBoolean("isInter") && lastEnterUserId != audience.getUid()) {
                ChatEntity chatEntity = ChatSpanUtils.ins().getChatEntity(requireActivity(),
                        msg, anchor.getAnchorId(),
                        anchor.getLiveId(), mChatEntityList.size(), lastFollowUserId);
                if (chatEntity != null) {
                    if (chatEntity.getUser() != null) {
                        lastEnterUserId = chatEntity.getUser().getUid();
                    }
                    if (chatEntity.isVip()) {
                        if (enterMsgIndex >= 0) {
                            enterMsgIndex++;
                        }
                    } else {
                        if (audience.getUserLevel() < 10) {
                            if (enterMsgIndex >= 0 && enterMsgIndex < mChatEntityList.size()) {
                                mChatEntityList.remove(enterMsgIndex);
                                chatAdapter.notifyItemRemoved(enterMsgIndex);
                            }
                            enterMsgIndex = 0;
                        } else {
                            chatEntity.setHighLeverEnterMsg(true);
                            if (enterMsgIndex >= 0) {
                                enterMsgIndex++;
                            }
                        }
                    }
                    notifyMsg(chatEntity);
                }
            }
        } else {
            if (isAdded()) {
                ChatEntity chatEntity = ChatSpanUtils.ins().getChatEntity(requireActivity(),
                        msg, anchor.getAnchorId(),
                        anchor.getLiveId(), mChatEntityList.size(), lastFollowUserId);

                if (chatEntity != null) {
                    if (chatEntity.getUser() != null && chatEntity.getUser().getUid()!=null) {
                        lastEnterUserId = chatEntity.getUser().getUid();
                    }
                    if (enterMsgIndex >= 0) {
                        enterMsgIndex++;
                    }
                    notifyMsg(chatEntity);
                }
            }
        }

        switch (protocol) {
            case Constant.MessageProtocol.PROTOCOL_FOCUS: //关注
                String uid1 = msg.optString("uid");
                lastFollowUserId = Long.parseLong(uid1);
                break;

            case Constant.MessageProtocol.PROTOCOL_AUDIENCE: //5 進房或退房消息
                boolean isInter = msg.optBoolean("isInter");
                int roomHide = msg.optInt("roomHide");

                Audience audience = mGson.fromJson(msg.toString(), Audience.class);
                if (isInter) { //进房
                    if (DataCenter.getInstance().getUserInfo().getUser() != null && audience.getIsRoomPreview() != 1) {
                        if (audience.getCarId() > 0 && DataCenter.getInstance().getUserInfo().getUser().getUid() != audience.getUid()) { //座驾
                            Gift gift = DataBase.getDbInstance().getGiftByGid(audience.getCarId());
                            showBigGiftFragment.showBigGiftEffect(audience.getCarId(),
                                    gift.getBimgs(), gift.getType(), gift.getResourceUrl());
                        }
                    }

                    if (audience.getUid() != userInfo.getUid() && roomHide == 0) {
                        audienceAdapter.addItem(audience);
                    }

                } else { //退房
                    audienceAdapter.removeItem(audience.getUid());
                }

                if (msg.optLong("liveId") == anchor.getLiveId()) {
                    refreshRq(audience.getRq());
                }

                if (audience.getShowType() == 0 && audience.getIsRoomPreview() != 1) {
                    audience.setLevel(audience.getUserLevel());
                    playSvg(audience);
                }
                break;

            case Constant.MessageProtocol.PROTOCOL_RECEIVE_GIFT: //7.送禮消息
                ReceiveGiftBean giftBean = mGson.fromJson(msg.toString(), ReceiveGiftBean.class);
                long liveId = msg.optLong("liveId");
                int tipType = msg.optInt("tipType");
                if (tipType == 1) {
                    // 豪气礼物票屏
                    DataBase db = DataBase.getDbInstance();
                    Gift tempGift = db.getGiftByGid(msg.optInt("gid"));
                    if (tempGift != null)
                        piaopingMgr.addHQDanmu(msg.optString("nickname"),
                                msg.optString("anchorNickname"), tempGift.getGname());
                }

                //大礼物特效
                if (liveId == anchor.getLiveId() && anchor.getAnchorId() == msg.optLong("anchorId")) {
                    giftEffectFragment.showOnSlide(giftBean);
                    if (giftBean.getGift() != null) {
                        if (String.valueOf(giftBean.getGift().getPlayType()) != null) {
                            if (giftBean.getGift().getPlayType() != 0) {
                                showBigGiftFragment.showBigGiftEffect(giftBean.getGift().getGid(),
                                        giftBean.getGift().getBimgs(),
                                        giftBean.getGift().getType(), giftBean.getGift().getResourceUrl());
                            }
                        }
                    }

                    long zb = msg.optLong("zb");
                    refreshMl(zb);
                    refreshRq(giftBean.getRq());
                }
                break;

            case Constant.MessageProtocol.PROTOCOL_CP: //29开奖结果
                lotteryDraw(msg);
                break;

            case Constant.MessageProtocol.PROTOCOL_SILENT: //8.禁言和取消禁言消息
                boolean isBlack = msg.optBoolean("isBlack");
                long uid = msg.optLong("uid");
                if (userInfo.getUid() == uid) {
                    isSilent = isBlack;
                }
                break;

            case Constant.MessageProtocol.PROTOCOL_BALANCE_CHANGE: //12、金币变动消息
                Double ret = msg.optDouble("goldCoin");
                LogUtils.e("chat json :jinbi " + ret);
                userInfo.setGoldCoin(ret.floatValue());
                User user=new User();
                user.setGoldCoin(ret.floatValue());
                DataCenter.getInstance().getUserInfo().updateUser(user);
                if (this.mGiftPanelView != null) {
                    this.mGiftPanelView.updateMoney();
                }
                break;

            case Constant.MessageProtocol.PROTOCOL_LIVE_UPDATE_GIFT: //16.更新礼物资源消息
                GiftManager.ins().downloadAllGifts();
                break;

            case Constant.MessageProtocol.PROTOCOL_LIVE_UPDATE_ADMISSION://更新入场动画
                AdmissionManager.getInstance().downloadAdmissionGiftList();  //下载入场动画
                break;

            case Constant.MessageProtocol.PROTOCOL_LEVEL_UP: //6.主播或用户 升级
                int levelType = msg.optInt("levelType");
                int level = msg.optInt("level");

                if (levelUpDialog == null || !levelUpDialog.isShow) {
                    levelUpDialog = LevelUpDialog.newInstance();
                    if (getChildFragmentManager().isStateSaved()) return;
                    levelUpDialog.show(getChildFragmentManager(), "");
                }
                levelUpDialog.setData(level, levelType == 1);
                break;

            case Constant.MessageProtocol.PROTOCOL_LIVE_KICK:  //15直播间踢人消息
                long KickedUid = msg.optLong("uid");
                audienceAdapter.removeItem(KickedUid);
                break;

            case Constant.MessageProtocol.PROTOCOL_GAME_Noticition:  //25游戏飘屏
                String content = msg.optString("content");
                if (!StringUtils.isEmpty(content)) {
                    DanmuEntity danmuEntity = new DanmuEntity();
                    danmuEntity.setType(13);
                    danmuEntity.setContent(content);
                    danmuEntity.setNickname("");
                    piaopingFragment.show(danmuEntity, 0);
                }
                break;

            case Constant.MessageProtocol.PROTOCOL_CpWin:  //27 cp中奖消息
                long zjUid = msg.optLong("uid");
                if (zjUid == userInfo.getUid()) {
                    playGoldInVoice();
                }
                break;

            case Constant.MessageProtocol.PROTOCOL_RoomManager:  //28 设置房管消息
                long uid28 = msg.optLong("uid");
                int type28 = msg.optInt("type"); //1添加房管, 2取消房管
                LogUtils.e("onReceived " + uid28 + ", " + type28);
                if (uid28 == userInfo.getUid()) {
                    anchor.setRoomManager(type28 == 1);
                }
                break;
            //红包雨
            case Constant.MessageProtocol.RED_BAG_START:
                initRedBagRain(TYPE_INIT_RED_BAG_RAIN_START);
                break;
            case Constant.MessageProtocol.RED_BAG_END:
            case Constant.MessageProtocol.RED_BAG_UPDATE:
                initRedBagRain(TYPE_INIT_RED_BAG_RAIN_GET);
                break;
        }
    }

    /**
     * 收到消息
     *
     * @param msg 消息体积
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent msg) {
        switch (msg.getType()) {
            case 3: //私信
                User user3 = mGson.fromJson(msg.getMessage(), User.class);
                liveRoomLetterFragment = LiveRoomLetterFragment.newInstance(user3);
                liveRoomLetterFragment.show(getChildFragmentManager(), "");
                break;

            case 4: //@Ta
                User user4 = mGson.fromJson(msg.getMessage(), User.class);
                bottomButtonLayout.setVisibility(View.GONE);
                showChatPanel(user4.getNickname());
                chatEt.requestFocus();
                KeyboardUtils.toggleSoftInput();
                break;

            case 5: //关注主播状态改变
                User user = mGson.fromJson(msg.getMessage(), User.class);
                if (user.getUid() == anchor.getAnchorId()) {
                    anchor.setFollow(user.isFollow());
                    btnFollow.setVisibility(user.isFollow() ? View.GONE : View.VISIBLE);
                    followMargin.setVisibility(anchor.isFollow() ? View.VISIBLE : View.GONE);
                }
                break;

            case 90: //收到一条私信
                Letter letter = mGson.fromJson(msg.getMessage(), Letter.class);
                if (liveRoomLetterFragment != null && liveRoomLetterFragment.isShow) {
                    LogUtils.e("没有显示");
                    liveRoomLetterFragment.getOneLetter(letter);
                }
                break;

            case 66: //游戏
                Game game = mGson.fromJson(msg.getMessage(), Game.class);
                User user66 = DataCenter.getInstance().getUserInfo().getUser();
                if (user66 == null) {
                    ToastUtils.showShort(getString(R.string.reLogin));
                    return;
                }
                getGame(game, user66);
                break;

            case 16: //礼物更新
                if (mGiftPanelView != null) {
                    mGiftPanelView.refreshGift();
                }
                break;

            case APPEND_BET_TYPE: //去cp 车  跟投
                ChipsVO chipsVO = new ChipsVO();
                chipsVO.setName(msg.getMessage());
                chipsVO.setChinese(msg.getName());

                if (TYPE_CP_FF.equals(msg.getName())) {
                    FCartDialogFragment fCartDialogFragment = FCartDialogFragment.newInstance(
                            NOT_FORM_MINUTEGAMEDIALOGFRAGMENT, chipsVO, msg.getTimes(), anchor.getLiveId());
                    if (!fCartDialogFragment.isAdded()) {
                        fCartDialogFragment.show(requireActivity().getSupportFragmentManager(), FCartDialogFragment.class.getSimpleName());
                    }
                } else {
                    BetCartDialogFragment betCartDialogFragment = BetCartDialogFragment.newInstance(
                            NOT_FORM_MINUTEGAMEDIALOGFRAGMENT, chipsVO, msg.getTimes(), anchor.getLiveId());
                    if (!betCartDialogFragment.isAdded()) {
                        betCartDialogFragment.show(
                                requireActivity().getSupportFragmentManager(), BetCartDialogFragment.class.getSimpleName());
                    }
                }
                break;
        }
    }

    /**
     * 获取游戏
     *
     * @param game   游戏
     * @param user66 用户
     */
    private void getGame(Game game, User user66) {
        Api_Pay.ins().getGame(user66.getUid() + "", user66.getNickname(), game.getGameId(),
                1, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String data) {
                        if (data != null) LogUtils.e(data);
                        if (code == 0) {
                            GameWebViewFragment gameWebViewFragment = GameWebViewFragment.newInstance("Game", data);
                            if (requireActivity().getSupportFragmentManager().isStateSaved())
                                return;
                            gameWebViewFragment.show(getChildFragmentManager(), "");
                            gameWebViewFragment.setBtnClick(new GameWebViewFragment.OnBtnClick() {
                                @Override
                                public void onEmptyClick(View view) {
                                    DialogFactory.showTwoBtnDialog(requireActivity(), getString(R.string.currentGame),
                                            (button, dialog) -> dialog.dismiss(), (button, dialog) -> {
                                                dialog.dismiss();
                                                showLoadingDialog();
                                                User user1 = DataCenter.getInstance().getUserInfo().getUser();
                                                if (user1 != null) {
                                                    Api_Pay.ins().kickout(user1.getUid() + "", new JsonCallback<String>() {
                                                        @Override
                                                        public void onSuccess(int code1, String msg1, String data1) {
                                                            hideLoadingDialog();
                                                            if (code1 == 0) {
                                                                gameWebViewFragment.dismiss();
                                                            } else {
                                                                ToastUtils.showShort(msg1);
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                }

                                @Override
                                public void onExit() {
                                    showLoadingDialog();
                                    User user = DataCenter.getInstance().getUserInfo().getUser();
                                    if (user != null) {
                                        Api_Pay.ins().kickout(user.getUid() + "", new JsonCallback<String>() {
                                            @Override
                                            public void onSuccess(int code, String msg, String data) {
                                                hideLoadingDialog();
                                                if (code == 0) {
                                                    gameWebViewFragment.dismiss();
                                                } else {
                                                    ToastUtils.showShort(msg);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
    }

    /**
     * 点击返回
     *
     * @param height 键盘高度
     */
    public void onKeyBoardHide(int height) {
        LogUtils.e("4444," + height);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) flBottomLayout.getLayoutParams();
        params.bottomMargin = height;
        flBottomLayout.setLayoutParams(params);

        hideChatPanel();
        flBottomLayout.postDelayed(() -> bottomButtonLayout.setVisibility(View.VISIBLE), 150);
    }

    public void onKeyBoardShow(int height) {
        LogUtils.e("3333," + height);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) flBottomLayout.getLayoutParams();
        params.bottomMargin = height;
        flBottomLayout.setLayoutParams(params);
    }

    /**
     * 隐藏聊天面板
     */
    private void hideChatPanel() {
        final ChatPanelView chatPanelView = this.mChatPanelView;
        if (chatPanelView != null && chatPanelView.getVisibility() == View.VISIBLE) {
            chatPanelView.setVisibility(View.GONE);
        }
    }

    private void showYFSSCDialog(int position) {
        if(liveStartLottery==null)
        {
            return;
        }
        ChipsVO chipsVO = new ChipsVO();
        if (AppConfig.isThLive()) {
            chipsVO.setName(liveStartLottery.get(position).getLotteryName());
            chipsVO.setChinese(liveStartLottery.get(position).getCpName());
        } else {
            chipsVO.setName(liveStartLottery.get(position).getCpName());
            chipsVO.setChinese(liveStartLottery.get(position).getLotteryName());
        }

        chipsVO.setIcon(liveStartLottery.get(position).getLorretyIcon());
        chipsVO.setPlayMethod(liveStartLottery.get(position).getPlayMethod());
        if (TYPE_CP_HNCP.equals(chipsVO.getName())) {
            HNDialogFragment fragmenthn = HNDialogFragment.newInstance(chipsVO, anchor.getLiveId());
            if (!fragmenthn.isAdded()) {
                fragmenthn.showNow(requireActivity().getSupportFragmentManager(),
                        HNDialogFragment.class.getSimpleName());
            }
        } else if (TYPE_CP_FF.equals(chipsVO.getName())) {
            FGameDialogFragment fragment = FGameDialogFragment.newInstance(chipsVO, anchor.getLiveId());
            if (!fragment.isAdded()) {
                fragment.showNow(requireActivity().getSupportFragmentManager(),
                        FGameDialogFragment.class.getSimpleName());
            }
        } else {
            MinuteGameDialogFragment fragment = MinuteGameDialogFragment.newInstance(chipsVO, anchor.getLiveId());
            if (!fragment.isAdded()) {
                fragment.showNow(requireActivity().getSupportFragmentManager(),
                        MinuteGameDialogFragment.class.getSimpleName());
            }
        }
    }

    /**
     * 开奖记录
     */
    public void showKJDialog() {
        KJDialogFragment kjDialogFragment = KJDialogFragment.newInstance();
        if (!kjDialogFragment.isAdded()) {
            kjDialogFragment.show(requireActivity().getSupportFragmentManager(),
                    KJDialogFragment.class.getSimpleName());
        }
    }

    /**
     * 显示礼物面板
     * nickname null 普通聊天 @某人
     */
    private void showGiftPanel() {
        ViewStub vsGiftPanel = rootView.findViewById(R.id.vs_gift_panel);
        if (vsGiftPanel != null) {
            LogUtils.e("showGiftPanel");
            this.mGiftPanelView = (GiftPanelView) vsGiftPanel.inflate();
            this.mGiftPanelView.setOnGiftListener(new GiftPanelView.OnGiftActionListener() {
                @Override
                public void showChargeActivity() {

                }

                @Override
                public void sendGift(Gift gift, int count) {
                    if (anchor.getToy() == 1 && gift.getTags().contains("7")
                            && getActivity() != null && !isAnchorLiveRoom) {
                        playDingVoice();
                    }
                    doSendGiftApi(gift, count);
                }

                @Override
                public void onGiftPanelViewHide(Gift gift) {

                }
            });
        }

        final GiftPanelView giftPanelView = this.mGiftPanelView;
        if (giftPanelView == null || giftPanelView.isShowing()) return;
        giftPanelView.show();
    }


    /**
     * 开奖结果
     */
    public void lotteryDraw(JSONObject msg) {
        LogUtils.e("lotteryDraw:" + msg.toString());
    }

    /**
     * 直播间消息刷新
     *
     * @param entity 消息实体
     */
    private void notifyMsg(ChatEntity entity) {
        LogUtils.e("notifyMsg:" + entity.getContent().toString());
        int maxIndex = 300;
        int sIndex = 40;
        mChatEntityList.add(0, entity);
        LinearLayoutManager layoutManager = (LinearLayoutManager) chatRecycleView.getLayoutManager();
        if (layoutManager != null) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (firstVisibleItemPosition == 0 || firstVisibleItemPosition == RecyclerView.NO_POSITION) {
                unreadMessageNum = 0;
                unreadMessage.setVisibility(View.GONE);
                chatAdapter.notifyItemInserted(0);
                chatRecycleView.scrollToPosition(0);
                if (mChatEntityList.size() >= maxIndex) {
                    maxIndex = mChatEntityList.size();
                    tempList.clear();
                    tempList.addAll(mChatEntityList.subList(sIndex, maxIndex));
                    mChatEntityList.removeAll(tempList);
                    chatAdapter.notifyItemRangeRemoved(sIndex, maxIndex - sIndex);
                    chatAdapter.notifyItemRangeChanged(sIndex, maxIndex - sIndex);
                }
            } else {
                unreadMessageNum++;
                chatAdapter.notifyItemInserted(0);
                String str = unreadMessageNum + getString(R.string.news);
                unreadMessage.setText(str);
                unreadMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    public void playSvg(Audience audience) {
        try {
            File svgAFile = AdmissionManager.getInstance().getSvgAFile(audience);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(svgAFile));
            if (isAdded()) {
                SVGAParser svgaParser = new SVGAParser(requireActivity());
                svgaParser.decodeFromInputStream(bufferedInputStream, svgAFile.getAbsolutePath(),
                        new SVGAParser.ParseCompletion() {
                            @Override
                            public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                                Log.e("playSvg", "onComplete: " + audience.getAvatar());
                                if (isAdded()) {
                                    admission(svgaVideoEntity, audience);
                                }
                            }

                            @Override
                            public void onError() {
                                Log.e("playSvg", "onError: ");
                            }
                        }, true, null, null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消Svg的动画
     */
    public void cancelSvgPlay() {
        svgaView.clearAnimation();
        showBigGiftFragment.clearGiftShow();
    }

    /**
     * 设置Svg动画信息
     *
     * @param svgaVideoEntity svg 播放实体
     * @param audience        当前用户信息
     */
    private void admission(SVGAVideoEntity svgaVideoEntity, Audience audience) {

        SVGADynamicEntity dynamicEntity = new SVGADynamicEntity();
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(28);
        String content = audience.getNickname() + " " + getString(R.string.player_come_to_live_room);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);

        dynamicEntity.setDynamicText(new StaticLayout(spannableStringBuilder, 10,
                spannableStringBuilder.length(),
                textPaint,
                10,
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0.0f,
                false), "content");


        if (TextUtils.isEmpty(audience.getAvatar())) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.img_default);

            dynamicEntity.setDynamicImage(bm, "avatar");
        } else {
            String url;
            if (audience.getAvatar().startsWith("http://oss.jwanfu.com")) {
                url = audience.getAvatar().replace("http://oss.jwanfu.com",
                        "http://bw18.oss-accelerate.aliyuncs.com");
            } else {
                url = audience.getAvatar();
            }

            dynamicEntity.setDynamicImage(url, "avatar");
            LogUtils.e("admission:  " + audience.getAvatar());
        }

        SVGADrawable drawable = new SVGADrawable(svgaVideoEntity, dynamicEntity);
        svgaView.setImageDrawable(drawable);
        svgaView.startAnimation();
        svgaView.stepToFrame(1, true);
    }

    public void playDingVoice() {
        try {
            if (player != null) {
                player.stop();
                player.release();
                player = null;
            }

            player = new MediaPlayer();
            AssetManager assetManager = requireActivity().getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("ding.mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playGoldInVoice() {
        try {
            if (player != null) {
                player.stop();
                player.release();
                player = null;
            }
            player = new MediaPlayer();
            AssetManager assetManager = requireActivity().getAssets();
            AssetFileDescriptor fileDescriptor = assetManager.openFd("gold_in.mp3");
            player.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void daojisi() {
        if (1 == anchor.getType() || 2 == anchor.getType()) {
            tv_djs.setVisibility(View.VISIBLE);
            timer.start();
        } else {
            tv_djs.setVisibility(View.GONE);
        }
    }

    /**
     * 调用赠送礼物接口
     */
    public void doSendGiftApi(Gift gift, int count) {
        Api_Live.ins().sendGift(gift.getGid(), anchor.getAnchorId(),
                anchor.getLiveId(), 1, count, new JsonCallback<String>() {
                    @Override
                    public void onSuccess(int code, String msg, String result) {
                        LogUtils.e("json : " + result);
                        if (code != 0) {
                            ToastUtils.showShort(msg);
                        }
                    }
                });
    }

    /**
     * 展示發消息彈出框
     */
    private void showChatPanel(String nickname) {
        LogUtils.e("showChatPanel()");
        ViewStub vsChatPanel = rootView.findViewById(R.id.vs_chat_panel);

        if (vsChatPanel != null) {
            mChatPanelView = (ChatPanelView) vsChatPanel.inflate();
            chatEt = mChatPanelView.findViewById(R.id.et_input_message);
            mChatPanelView.setPageType(2);
            mChatPanelView.setOnChatListener(this);
        }

        final ChatPanelView chatPanelView = this.mChatPanelView;
        chatPanelView.setNickName(nickname);
        if (chatPanelView.getVisibility() == View.VISIBLE) return;
        chatPanelView.setVisibility(View.VISIBLE);
        LogUtils.e("showChatPanel() end");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            // 如果先调用destroy()方法，
            //则会命中if (isDestroyed()) return;需要先onDetachedFromWindow()
            //再 destory()
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        if (mCountdownTimer != null) {
            mCountdownTimer.cancel();
            mCountdownTimer = null;
        }
        imageAdHandler.removeMessages(1);
        textAdHandler.removeMessages(1);
        piaopingAdHandler.removeMessages(1);
        pkCountdownHandler.removeMessages(1);
        if (timer != null) {
            timer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWebView.removeCallbacks(mRedBagWebRunnable);
        tv_cptop.removeCallbacks(mRunnable);
        tv_cptop2.removeCallbacks(mRunnable2);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
