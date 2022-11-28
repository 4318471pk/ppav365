package com.tencent.demo.avatar.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.tencent.demo.avatar.model.AvatarItem;
import com.tencent.demo.avatar.model.MainTab;
import com.tencent.demo.avatar.model.SubTab;
import com.tencent.demo.avatar.view.groupitemview.GroupItemView;
import com.tencent.demo.avatar.view.groupitemview.IconGroupItemView;
import com.tencent.demo.avatar.view.groupitemview.SliderGroupItemView;
import com.tencent.xmagic.avatar.AvatarData;

import java.util.List;

/**
 * avatar 面板中的二级菜单页面
 */
 class AvatarPage extends LinearLayout implements AvatarPageInf {

    private static final int TAB_HEIGHT = 60;
    private static final int TAB_TXT_SIZE = 16;


    private Context mContext;
    private LinearLayout contentLayout;
    private AvatarPageItemViewCallBack pageItemViewCallBack;

    private int normalColor = Color.parseColor("#9EA7B9");
    private int selectedColor = Color.parseColor("#09BEFF");

    private MainTab mainTab = null;
    private TabLayout.Tab lastSelectedTab;

    public AvatarPage(Context context) {
        this(context, null);
    }

    public AvatarPage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(VERTICAL);
        mContext = context;
    }


    public void initView(MainTab mainTab, AvatarPageItemViewCallBack callBack) {
        pageItemViewCallBack = callBack;
        this.mainTab = mainTab;
        TabLayout tabLayout = new TabLayout(getContext());
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        tabLayout.setTabRippleColor(null);
        LayoutParams tabLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AvatarPanel
                .dip2px(getContext(), TAB_HEIGHT));

        contentLayout = new LinearLayout(mContext);
        contentLayout.setOrientation(VERTICAL);
        ScrollView scrollView = new ScrollView(mContext);
        scrollView.setOverScrollMode(OVER_SCROLL_NEVER);
        scrollView.setFillViewport(true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != lastSelectedTab) {
                    ((TextView) tab.getCustomView()).setTextColor(selectedColor);
                    onTabSelect(tab);
                }
                lastSelectedTab = tab;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextColor(normalColor);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        boolean isAddTabLayout = false;
        for (SubTab subTab : mainTab.subTabs) {
            if (!TextUtils.isEmpty(subTab.label)) {
                TextView textView = new TextView(mContext);
                textView.setText(subTab.label);
                textView.setTextSize(TAB_TXT_SIZE);
                textView.setTextColor(normalColor);
                TabLayout.Tab tab = tabLayout.newTab().setCustomView(textView);
                tab.setTag(subTab);
                tabLayout.addTab(tab);
                isAddTabLayout = true;
            }
        }
        if (isAddTabLayout) {
            this.addView(tabLayout, tabLayoutParams);
        }
        this.addView(scrollView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.addView(contentLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void refresh(){
        onTabSelect(lastSelectedTab);
    }

    private void onTabSelect(TabLayout.Tab tab) {
        SubTab subTab = (SubTab) tab.getTag();
        contentLayout.removeAllViews();
        if (subTab == null) {
            return;
        }

        List<AvatarItem> avatarItems = subTab.items;
        if (avatarItems == null || avatarItems.size() == 0) {
            return;
        }
        GroupItemView groupItemView = null;
        boolean isShowPage = true;
        if (subTab.type == AvatarData.TYPE_SELECTOR) {
            groupItemView = new IconGroupItemView(mContext);
        } else if (subTab.type == AvatarData.TYPE_SLIDER) {
            groupItemView = new SliderGroupItemView(mContext);
        }
        if (pageItemViewCallBack != null) {
            isShowPage = pageItemViewCallBack.onShowPage(this, subTab);
        }
        if (groupItemView != null) {
            groupItemView.setGroupItemViewCallBack(new GroupItemView.GroupItemViewCallBack() {
                @Override
                public void onItemChecked(AvatarItem avatarItem) {
                    if (pageItemViewCallBack != null) {
                        pageItemViewCallBack.onItemChecked(mainTab, avatarItem);
                    }
                }

                @Override
                public void onItemValueChange(AvatarItem avatarItem) {
                    if (pageItemViewCallBack != null) {
                        pageItemViewCallBack.onItemValueChange(mainTab, avatarItem);
                    }
                }
            });
            if (isShowPage) {
                groupItemView.initView(mainTab, subTab);
            }
            contentLayout.addView(groupItemView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }


    public interface AvatarPageItemViewCallBack {
        void onItemChecked(MainTab mainTab, AvatarItem avatarItem);

        //当页面展示的时候调用，需要
        boolean onShowPage(AvatarPageInf avatarPageInf, SubTab subTab);

        void onItemValueChange(MainTab mainTab, AvatarItem avatarItem);
    }

}
