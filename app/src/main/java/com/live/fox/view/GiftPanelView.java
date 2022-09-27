package com.live.fox.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.adapter.GiftGridViewAdapter;
import com.live.fox.adapter.GiftViewPagerAdapter;
import com.live.fox.db.DataBase;
import com.live.fox.entity.Gift;
import com.live.fox.manager.DataCenter;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.DensityUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.DeviceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Date: 2019/02/13
 */
public class GiftPanelView extends RelativeLayout implements View.OnClickListener, GiftGridViewAdapter.OnItemClickListener,
        QuickSendGift.OnComboClickListener {

    public static final int ONE_PAGER_MAX_COUNT = 8;

    private final Context context;
    private TextView giftTipView;
    private TextView tvPanelMoney;
    private TextView gift_count;
    private TextView gift_send;
    private RelativeLayout layout_giftsend;
    private TextView add_currency1, add_currency2;
    private View mArrow;


    private RadioGroup dotGroup;
    private View lastClickItemView;
    private OnGiftActionListener giftListener;

    private AnimationSet[] animationSet;

    public List<String> titleList;
    private LinkedHashMap<String, List<Gift>> giftMap;
    private final List<ViewGroup> childViewPagers = new ArrayList<>();

    List<Gift> packages;

    private int catCurrencyView;
    private int clickCount = 0;//连击次数
    private int currentFatherPager = 0;  //当前父ViewPager在第几页
    private int currentChildPager = 0;//当前子ViewPager在第几页

    private long originalCatCurrency;

    private boolean isShowing;

    private Gift currentGift;
    private Drawable mDrawable1;
    private Drawable mDrawable2;
    private PopupWindow mGiftCountPopupWindow;//选择分组数量的popupWindow
    int sendGiftCount = 1;
    private static final int DEFAULT_COUNT = 1;

    public GiftPanelView(Context context) {
        this(context, null);
    }

    public GiftPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    public List<String> getTabTitleList() {
        List<String> temp = new ArrayList<>();
        temp.add(context.getString(R.string.hotCa));
        return temp;
    }

    /**
     * 从数据库中读取礼物数据
     *
     * @param titleList 爱他
     * @return 返回礼物数据
     */
    public LinkedHashMap<String, List<Gift>> getGiftMap(List<String> titleList) {
        List<Gift> giftsList = DataBase.getDbInstance().getPackageGift();
        LinkedHashMap<String, List<Gift>> giftMap = new LinkedHashMap<>();
        giftMap.put(titleList.get(0), giftsList);
        return giftMap;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
        titleList = getTabTitleList();
        giftMap = getGiftMap(titleList);
        if (giftMap != null && giftMap.size() != 0 && titleList != null && titleList.size() != 0) {
            initGiftDisplay();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void initView() {
        giftTipView = findViewById(R.id.tv_gift_info);
        tvPanelMoney = findViewById(R.id.tv_panel_money);
        gift_count = findViewById(R.id.gift_count);
        gift_send = findViewById(R.id.gift_send);
        layout_giftsend = findViewById(R.id.layout_giftsend);
        mArrow = findViewById(R.id.iv_giftcountArrow);

        mDrawable1 = ContextCompat.getDrawable(context, R.drawable.bg_live_gift_send);
        mDrawable2 = ContextCompat.getDrawable(context, R.drawable.bg_live_gift_send_2);

        findViewById(R.id.layout_recharge).setOnClickListener(this);
        findViewById(R.id.layout_giftsend).setOnClickListener(this);
        findViewById(R.id.gift_count).setOnClickListener(this);
        dotGroup = findViewById(R.id.radioGroup_main);
    }

    private void initGiftDisplay() {
        initDots(giftMap.get(titleList.get(0)), 0);
        for (int i = 0; i < giftMap.size(); i++) {
            List<Gift> giftList = giftMap.get(titleList.get(i));
            childViewPagers.add(initChildViewPager(giftList));
        }
        initFatherViewPager();
        updateMoney();
    }

    //刷新礼物界面
    public void refreshGift() {
        if (giftMap == null) return;

        titleList.clear();
        giftMap.clear();
        viewGroups.clear();
        childViewPagers.clear();
        dotGroup.removeAllViews();

        titleList.addAll(getTabTitleList());
        giftMap.putAll(getGiftMap(titleList));

        initDots(giftMap.get(titleList.get(0)), 0);
        for (int i = 0; i < giftMap.size(); i++) {
            List<Gift> giftList = giftMap.get(titleList.get(i));
            childViewPagers.add(initChildViewPager(giftList));
        }
        fatherViewPagerAdapter.notifyDataSetChanged();

        gift_count.setVisibility(View.INVISIBLE);
        mArrow.setVisibility(View.INVISIBLE);
        gift_send.setBackground(mDrawable2);
        gift_send.setEnabled(false);
        layout_giftsend.setEnabled(false);
    }

    GiftGridViewAdapter adapter;

    /**
     * 真正的子RecyclerView
     */
    private RecyclerView initRecyclerView(int page, List<Gift> giftList) {
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);

        adapter = new GiftGridViewAdapter(context, giftList, page);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        return recyclerView;
    }

    GiftViewPagerAdapter viewPagerAdapter;
    List<ViewGroup> viewGroups;

    /**
     * 初始化子ViewPager
     */
    private ViewPager initChildViewPager(List<Gift> giftList) {
        if (giftList == null) return null;
        ViewPager viewPager = new ViewPager(context);
        viewGroups = new ArrayList<>();
        int pager = giftList.size() / ONE_PAGER_MAX_COUNT;
        if (giftList.size() % ONE_PAGER_MAX_COUNT != 0) pager++;
        LogUtils.e("初始化 页数:" + pager);
        for (int i = 0; i < pager; i++) {
            viewGroups.add(initRecyclerView(i, giftList));
        }
        viewPagerAdapter = new GiftViewPagerAdapter(viewGroups, null);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                RadioButton newButton = (RadioButton) dotGroup.getChildAt(position);
                LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) newButton.getLayoutParams();
                layoutParams1.width = DensityUtils.dp2px(getContext(), 10);
                layoutParams1.leftMargin = 10;
                newButton.setLayoutParams(layoutParams1);

                RadioButton oldButton = (RadioButton) dotGroup.getChildAt(currentChildPager);
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) oldButton.getLayoutParams();
                layoutParams2.width = DensityUtils.dp2px(getContext(), 5);
                layoutParams2.leftMargin = 10;
                oldButton.setLayoutParams(layoutParams2);

                newButton.setChecked(true);
                currentChildPager = position;
            }
        });
        return viewPager;
    }

    GiftViewPagerAdapter fatherViewPagerAdapter;

    /**
     * 初始化父ViewPager
     */
    private void initFatherViewPager() {
        ViewPager fatherViewpager = findViewById(R.id.gift_viewpager);
        fatherViewPagerAdapter = new GiftViewPagerAdapter(childViewPagers, titleList);
        fatherViewpager.setAdapter(fatherViewPagerAdapter);
        fatherViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                ViewPager childViewPager = (ViewPager) childViewPagers.get(position);
                currentChildPager = childViewPager.getCurrentItem();
                initDots(giftMap.get(titleList.get(position)), currentChildPager);
                currentFatherPager = position;
            }
        });
    }

    //弹框显示动画
    public void show() {
        setTranslationY(DensityUtils.dp2px(context, 180));
        animate().translationY(0).setDuration(200)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isShowing = true;
                        setVisibility(View.VISIBLE);
                    }
                }).start();
    }

    //弹框关闭动画
    public void hide() {
        LogUtils.e("hide");
        if (mGiftCountPopupWindow != null) {
            mGiftCountPopupWindow.dismiss();
        }
        isShowing = false;
        animate().translationY(getHeight()).setDuration(200)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setVisibility(View.GONE);
                        if (giftListener != null) {
                            giftListener.onGiftPanelViewHide(currentGift);
                        }
                    }
                }).start();
    }


    private void initDots(List<Gift> giftList, int selectPosition) {
        if (giftList == null || giftList.size() == 0) return;
        int count = giftList.size() / ONE_PAGER_MAX_COUNT;
        if (giftList.size() % ONE_PAGER_MAX_COUNT != 0) count++;
        int size = dotGroup.getChildCount();
        if (size > count) {
            for (int i = count; i < size; i++) {
                dotGroup.removeViewAt(count);
            }
        } else if (size < count) {
            for (int i = size; i < count; i++) {
                RadioButton radioButton = new AppCompatRadioButton(context);
                LinearLayout.LayoutParams layoutParams1 =
                        new LinearLayout.LayoutParams(DensityUtils.dp2px(getContext(), 10), LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.leftMargin = 10;
                radioButton.setLayoutParams(layoutParams1);
                radioButton.setButtonDrawable(R.drawable.sel_gift_radiobutton);
                dotGroup.addView(radioButton, i);
            }
        }
        for (int j = 0; j < dotGroup.getChildCount(); j++) {
            RadioButton button = (RadioButton) dotGroup.getChildAt(j);
            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) button.getLayoutParams();
            if (selectPosition == j) {
                layoutParams1.width = DensityUtils.dp2px(getContext(), 10);
            } else {
                layoutParams1.width = DensityUtils.dp2px(getContext(), 5);
            }
            layoutParams1.leftMargin = 10;
            button.setLayoutParams(layoutParams1);
        }
        ((RadioButton) dotGroup.getChildAt(selectPosition)).setChecked(true);
    }


    /**
     * 设置用户余额
     */
    public void updateMoney() {
        tvPanelMoney.setText(RegexUtils.westMoney(DataCenter.getInstance().getUserInfo().getUser().getGoldCoin()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_giftsend: //发送礼物
                LogUtils.e("发送礼物");
                if (currentGift != null) {
                    sendGift();
                }
                break;
            case R.id.layout_recharge: //充值
                LogUtils.e("充值");
                hide();
                RechargeActivity.startActivity(context);
                break;
            case R.id.gift_count: //礼物数量
                showGiftCount();
                break;
        }
    }

    /**
     * 显示分组数量
     */
    private void showGiftCount() {
        String[] mArray = new String[]{"1", "10", "66", "88", "100", "520", "1314"};
        List<String> data = Arrays.asList(mArray);
        View v = LayoutInflater.from(context).inflate(R.layout.view_gift_count, null);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
        BaseQuickAdapter adapter;
        recyclerView.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_live_gift_count, data) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                helper.setText(R.id.tv_, String.valueOf(item));
            }
        });

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            sendGiftCount = Integer.parseInt(adapter1.getItem(position).toString());
            gift_count.setText(String.valueOf(sendGiftCount));
            hideGiftCount();
        });
        recyclerView.setAdapter(adapter);
        mGiftCountPopupWindow = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mGiftCountPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mGiftCountPopupWindow.setOutsideTouchable(true);
        mGiftCountPopupWindow.showAtLocation(gift_count, Gravity.BOTTOM | Gravity.RIGHT, DeviceUtils.dp2px(context, 50), DeviceUtils.dp2px(context, 40));
    }

    /**
     * 隐藏分组数量
     */
    private void hideGiftCount() {
        if (mGiftCountPopupWindow != null) {
            mGiftCountPopupWindow.dismiss();
        }
    }

    boolean isPackageGift = false;

    public void sendGift() {
        if (currentGift.getPlayType() == 0) {
            if (DataCenter.getInstance().getUserInfo().getUser() != null) {
                if (DataCenter.getInstance().getUserInfo().getUser().getGoldCoin() < currentGift.getGoldCoin() * sendGiftCount) {
                    ToastUtils.showShort(context.getString(R.string.yuerLess));
                    return;
                }
            }
        }

        //如果是大禮物 則檢查本地是否下載解壓完
        if (currentGift.getPlayType() != 0) {
//            Gift giftBean = DataBase.getDbInstance(Utils.getApp()).getGiftByGid(currentGift.getGid());
//            LogUtils.e(giftBean.toString());
//            String filePath = Constant.DEFAULT_DOWNLOAD_DIR + ".tx/" + giftBean.getGid() + "/";
//            if (!giftBean.getResourceUrl().endsWith(".zip")) {
//                //svga礼物
//                File bigGiftLastFile = new File(filePath + giftBean.getGid());
//                if (!bigGiftLastFile.exists()) {
//                    ToastUtils.showShort("禮物正在下載中,請稍後再試..");
//                    GiftManager.ins().download(giftBean);
//                    return;
//                }
//            } else {
//                //gif礼物
//                int maxIndex = giftBean.getBimgs();
//                File bigGiftLastFile = new File(filePath + "b_" + maxIndex + ".png");
//                if (!bigGiftLastFile.exists()) {
//                    ToastUtils.showShort("禮物正在下載中,請稍後再試..");
//                    GiftManager.ins().download(giftBean);
//                    return;
//                }
//            }
        }

        if (currentGift.getPlayType() == 0) {
            showCombo(currentGift);
        }

        if (giftListener != null) {
            giftListener.sendGift(currentGift, sendGiftCount);
        }
//        Api_Live.ins().sendGift(currentGift.getGid(), new JsonCallback<String>() {
//            @Override
//            public void onSuccess(int code, String msg, String result) {
//                LogUtils.e("json : " + result);
//                if (code == 0) {
//                    //服务端从2018-11-30开始不再对此接口做金币返回的信息，所有金币变动走PROTOCOL_BALANCE_CHANGE消息
////                    updateMoney();
////                    if (currentGift.getGiftOrPackage() == 1) {
////                        LogUtils.e("送出的是背包礼物，刷新礼物背包");
////                        refreshPackageGift();
////                    }
//                } else {
//                    ToastUtils.showShort(msg);
//                }
//            }
//        });
    }

//    //更新背包礼物的数据
//    public void refreshPackageGift() {
//        Api_UserProp.ins().getPackageGifts(TAG, new StringResponseCallback() {
//            @Override
//            public boolean onStringResponse(String result, int errCode, String errMsg, int id,
//                                            boolean fromCache) {
//                LogUtils.e("getPackageGifts result : " + result);
//                List<GiftBean> packages = new ArrayList<>();
//                if (errCode == HttpResponseCode.HTTP_SUCCESS) {
//                    PackageGiftResponse response = new Gson().fromJson(result, PackageGiftResponse.class);
//                    if (response.getData().size() != 0) {
//                        for (PackageGift packageGift : response.getData()) {
//                            if (packageGift.getEndTime() == 0 || System.currentTimeMillis() < packageGift.getEndTime() * 1000) {
//                                GiftBean giftBean = GiftBean.findById(GiftBean.class, Long.valueOf(packageGift.getGid()));
//                                if (giftBean != null) {
//                                    giftBean.setPackageGift(1);
//                                    giftBean.setGiftCount(packageGift.getNum());
//                                    packages.add(giftBean);
//                                }
//                            }
//                        }
//                    }
//                }
//                LogUtils.e("getPackageGifts result : " + packages.get(0).getGiftCount());
//                setPackages(packages);
//                return false;
//            }
//        });
//    }

    public void setOnGiftListener(OnGiftActionListener listener) {
        giftListener = listener;
    }


    protected QuickSendGift quickSendGift;//快捷送礼

    public void showCombo(Gift gift) {
        if (quickSendGift == null) {
            ViewStub vsQuickSendGIft = findViewById(R.id.vs_quick_send_gift);
            if (vsQuickSendGIft != null) {
                QuickSendGift quickGift = (QuickSendGift) vsQuickSendGIft.inflate();
                quickGift.setOnComboClickListener(this);
                quickGift.setVisibility(View.GONE);
                quickSendGift = quickGift;
            }
        }
        final QuickSendGift quickSendGift = this.quickSendGift;
        if (quickSendGift == null) return;
        quickSendGift.startCountDownTimer(gift);
        if (quickSendGift.getVisibility() != VISIBLE) {
            quickSendGift.showQuickGift(gift);
        }

    }

    //点击连发时的按钮回调
    @Override
    public void onComboClick() {
        sendGift();
    }

    @Override
    public void onItemClick(View newItemView, int position) {
        LogUtils.e("onItemClick");
        //如果上次点击的itemView跟这次点的不一样，就将上次的itemView重置
        if (lastClickItemView != null && lastClickItemView != newItemView) {
//            lastClickItemView.findViewById(R.id.tv_giftNum).setVisibility(View.INVISIBLE);
            lastClickItemView.findViewById(R.id.iv_select).setVisibility(View.INVISIBLE);
//            clickCount = 0;//选取了另外一个礼物，连击次数充值

//            SimpleDraweeView sdGIftIcon = lastClickItemView.findViewById(R.id.grid_item_img);
//            ViewGroup.LayoutParams layoutParams = sdGIftIcon.getLayoutParams();
//            layoutParams.width = DensityUtils.dp2px(context, 55);
//            layoutParams.height = DensityUtils.dp2px(context, 55);
//            sdGIftIcon.setLayoutParams(layoutParams);
//            FrescoUtil.loadUrl(currentGift.getHotIcon(), sdGIftIcon);
//            lastClickItemView.findViewById(R.id.tv_gift_name).setVisibility(View.GONE);

            //动画效果消除 但是有点问题 暂时不加
//              ImageView imageView = lastClickItemView.findViewById(R.id.iv_item_gift_panel_gift);
//              imageView.setImageResource(0);
//              GlideUtils.loadImage(context, currentGift.getCover(), imageView);
        }

//        TextView iv_giftNum = newItemView.findViewById(R.id.tv_giftNum);
        ImageView ivSelect = newItemView.findViewById(R.id.iv_select);
        ivSelect.setVisibility(View.VISIBLE);

        Animation mLittleAnimation = AnimationUtils.loadAnimation(context, R.anim.scale_big);
        ivSelect.startAnimation(mLittleAnimation);

        List<Gift> giftList = giftMap.get(titleList.get(currentFatherPager));
        Gift selectGift = giftList.get(currentChildPager * ONE_PAGER_MAX_COUNT + position);
        LogUtils.e("currentChildPager:" + currentChildPager + ", position:" + position);

        if (currentGift != selectGift) {
            currentGift = selectGift;

            gift_send.setEnabled(true);
            layout_giftsend.setEnabled(true);
            if (DEFAULT_COUNT != sendGiftCount) {
                sendGiftCount = DEFAULT_COUNT;
                gift_count.setText(String.valueOf(sendGiftCount));
            }
            if (currentGift.getPlayType() == 0) {
                //小礼物
                if (gift_count != null && gift_count.getVisibility() != View.VISIBLE) {
                    gift_count.setVisibility(View.VISIBLE);
                    mArrow.setVisibility(View.VISIBLE);
                    gift_send.setBackground(mDrawable1);
                }
            } else {
                if (gift_count != null && gift_count.getVisibility() == View.VISIBLE) {
                    gift_count.setVisibility(View.INVISIBLE);
                    mArrow.setVisibility(View.INVISIBLE);
                    gift_send.setBackground(mDrawable2);
                }

            }

            //取消连发按钮的显示
            if (quickSendGift != null) {
                quickSendGift.cancelTimer();
//                gift_send.setVisibility(View.VISIBLE);
//                gift_count.setVisibility(View.VISIBLE);
            }

//            newItemView.findViewById(R.id.tv_gift_name).setVisibility(View.VISIBLE);
//            SimpleDraweeView sdGIftIcon = newItemView.findViewById(R.id.grid_item_img);
//            if (Util.isLegal(currentGift.getWebpIcon())) {
//                DraweeController controller = Fresco.newDraweeControllerBuilder()
//                        .setUri(currentGift.getWebpIcon())
//                        .setAutoPlayAnimations(true)
//                        .setOldController(sdGIftIcon.getController())
//                        .build();
//                sdGIftIcon.setController(controller);
//            }
//            ViewGroup.LayoutParams layoutParams = sdGIftIcon.getLayoutParams();
//            layoutParams.width = DensityUtils.dp2px(context, 70);
//            layoutParams.height = DensityUtils.dp2px(context, 70);
//            sdGIftIcon.setLayoutParams(layoutParams);
        }
//        String[] giftSendNum = AppConfigManager.getInstance().extract(SwitchId.GIFT_NUM).split(",");
//        clickCount++;
//        if (clickCount > giftSendNum.length) {
//            clickCount = 1;
//        }
//        int sendGiftCount = 1;
//        if (selectGift.getGiftType() < 2) {
//            sendGiftCount = Integer.valueOf(giftSendNum[clickCount - 1]);
//        }
//        iv_giftNum.setVisibility(View.VISIBLE);
//        iv_giftNum.setText("X" + sendGiftCount);
//        selectGift.setCount(sendGiftCount);
//
//        giftTipView.setVisibility(View.VISIBLE);
//        giftTipView.setText("赠送\""+currentGift.getGname()+"\"后 主播可获得："+currentGift.getXl() + "魅力  "+currentGift.getXz() + "糧票");
        lastClickItemView = newItemView;
    }

    int dialogW;
    int dialogH;

    /**
     * 加币动画
     */
    private AnimationSet catFoodAnimation() {
        if (animationSet == null) {
            animationSet = new AnimationSet[2];
            for (int i = 0; i < 2; i++) {
                animationSet[i] = new AnimationSet(true);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                alphaAnimation.setDuration(1500);
                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1.5f);
                translateAnimation.setDuration(1500);
                DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(2.0f);
                animationSet[i].addAnimation(alphaAnimation);
                animationSet[i].addAnimation(translateAnimation);
                animationSet[i].setInterpolator(decelerateInterpolator);
                animationSet[i].setFillAfter(true);
            }
        }
        return animationSet[catCurrencyView];
    }

    private void resetCatAnimation() {
        if (add_currency1 != null) {
            add_currency1.setText("");
            add_currency2.setText("");
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public interface OnGiftActionListener {

        void showChargeActivity();

        void sendGift(Gift gift, int count);

        void onGiftPanelViewHide(Gift gift);
    }
}
