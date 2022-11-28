package com.tencent.demo.avatar.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.tencent.demo.R;
import com.tencent.demo.avatar.model.AvatarItem;
import com.tencent.demo.avatar.model.MainTab;
import com.tencent.demo.avatar.model.SubTab;
import com.tencent.xmagic.avatar.AvatarCategory;
import com.tencent.xmagic.avatar.AvatarData;

import java.util.List;
import java.util.Map;

/**
 * 捏脸面板类
 */
public class AvatarPanel extends LinearLayout implements AvatarPage.AvatarPageItemViewCallBack {

    private static final int TITLE_HEIGHT = 50;
    private static final int TITLE_PADDING = 10;

    private LinearLayout contentLayout;
    private Context mContext;
    private AvatarPanelCallBack avatarPanelCallBack;
    private Map<String, AvatarPage> avatarPageMap = new ArrayMap<>();
    private int lineColor = Color.parseColor("#F4F5F9");
    private List<MainTab> mainTabList;

    private boolean faceTypeHasSelected = true;

    public AvatarPanel(Context context) {
        this(context, null);
    }

    public AvatarPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOrientation(VERTICAL);
    }

    public void setAvatarPanelCallBack(AvatarPanelCallBack avatarPanelCallBack) {
        this.avatarPanelCallBack = avatarPanelCallBack;
    }


    public void initView(List<MainTab> mainTabList) {
        removeAllViews();
        this.mainTabList = mainTabList;
        if (this.mainTabList == null) {
            return;
        }
        TabLayout tabLayout = new TabLayout(mContext);
        tabLayout.setBackgroundResource(R.drawable.avatar_panel_top_bg);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.setTabRippleColor(null);
        tabLayout.setOverScrollMode(OVER_SCROLL_NEVER);
        LayoutParams tabLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(mContext, TITLE_HEIGHT));
        int padding = dip2px(mContext, TITLE_PADDING);
        tabLayout.setPadding(0, padding, 0, padding);
        this.addView(tabLayout, tabLayoutParams);
        LinearLayout line = new LinearLayout(mContext);
        line.setBackgroundColor(lineColor);
        this.addView(line, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(mContext, 1)));
        contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(VERTICAL);
        contentLayout.setBackgroundColor(Color.WHITE);
        this.addView(contentLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            private TabLayout.Tab lastSelectedTab;

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != lastSelectedTab) {
                    MainTab mainTab = (MainTab) tab.getTag();
                    if (mainTab != null) {
                        Glide.with(tabLayout).load(mainTab.checkedIconUrl).into((ImageView) tab.getCustomView());
                        onTabSelect(mainTab);
                    }
                }
                lastSelectedTab = tab;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                MainTab mainTab = (MainTab) tab.getTag();
                if (mainTab != null) {
                    Glide.with(tabLayout).load(mainTab.iconUrl).into((ImageView) tab.getCustomView());
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        for (MainTab mainTab : mainTabList) {
            if (TextUtils.isEmpty(mainTab.id)) {
                throw new IllegalArgumentException("pageModel id is not allowed to be null ");
            }
            ImageView icon = new ImageView(mContext);
            TabLayout.Tab tab = tabLayout.newTab().setCustomView(icon);
            Glide.with(tabLayout).load(mainTab.iconUrl).into((ImageView) tab.getCustomView());
            tab.setTag(mainTab);
            tabLayout.addTab(tab);
        }
    }


    private void onTabSelect(MainTab mainTab) {
        if (mainTab != null && mainTab.subTabs != null) {
            AvatarPage avatarPage = getAvatarPage(mainTab);
            contentLayout.removeAllViews();
            contentLayout.addView(avatarPage, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private AvatarPage getAvatarPage(MainTab mainTab) {
        AvatarPage avatarPage = avatarPageMap.get(mainTab.id);
        if (avatarPage == null) {
            avatarPage = new AvatarPage(mContext);
            avatarPage.initView(mainTab, this);
            avatarPageMap.put(mainTab.id, avatarPage);
        }
        return avatarPage;
    }


    @Override
    public void onItemChecked(MainTab mainTab, AvatarItem avatarItem) {
        if (AvatarCategory.FACE_TYPE.equals(avatarItem.category)) {  //表示选中脸型
            faceTypeHasSelected = true;
            for (SubTab subTab : mainTab.subTabs) {
                if (AvatarCategory.FACE_SHAPE_VALUE.equals(subTab.category)) {
                    AvatarData avatarData = subTab.items.get(0).avatarData;
                    avatarData.value= avatarItem.avatarData.value;
                    break;
                }
            }
        }
        if (avatarPanelCallBack != null) {
            avatarPanelCallBack.onItemChecked(mainTab, avatarItem);
        }
    }

    @Override
    public boolean onShowPage(AvatarPageInf avatarPageInf, SubTab subTab){
        if (avatarPanelCallBack != null) {
           return avatarPanelCallBack.onShowPage(avatarPageInf,subTab);
        }
        return true;
    }

    @Override
    public void onItemValueChange(MainTab mainTab, AvatarItem avatarItem) {
        if (AvatarCategory.FACE_SHAPE_VALUE.equals(avatarItem.category)) {
            if (faceTypeHasSelected) {
                for (SubTab subTab : mainTab.subTabs) {
                    if (AvatarCategory.FACE_TYPE.equals(subTab.category)) {
                        for (AvatarItem icon : subTab.items) {
                            icon.selected = false;
                        }
                        faceTypeHasSelected = false;
                        break;
                    }
                }
            }
        }
        if (avatarPanelCallBack != null) {
            avatarPanelCallBack.onItemValueChange(avatarItem);
        }
    }


    public void cleanView() {
        avatarPageMap.clear();
        removeAllViews();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public List<MainTab> getMainTabList(){
        return this.mainTabList;
    }
}
