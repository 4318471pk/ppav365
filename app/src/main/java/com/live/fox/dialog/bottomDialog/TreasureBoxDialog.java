package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.live.fox.R;
import com.live.fox.adapter.TreasureBoxPagerAdapter;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.databinding.DialogTreasureBoxBinding;
import com.live.fox.entity.TreasureItemBean;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.ScreenUtils;
import com.live.fox.view.BotTriangleBubbleView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//包含礼物特权背包
public class TreasureBoxDialog extends BaseBindingDialogFragment {

    DialogTreasureBoxBinding mBind;
    List<List<TreasureItemBean>> lists;
    TreasureBoxPagerAdapter adapter;


    public static TreasureBoxDialog getInstance()
    {
        return new TreasureBoxDialog();
    }

    public void setFullscreen(boolean isShowStatusBar, boolean isShowNavigationBar) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (!isShowStatusBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        if (!isShowNavigationBar) {
            uiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        getDialog().getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        //隐藏标题栏
        // getSupportActionBar().hide();
        setNavigationStatusColor(Color.TRANSPARENT);
    }

    public void setNavigationStatusColor(int color) {
        //VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        if (Build.VERSION.SDK_INT >= 21) {
            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getDialog().getWindow().setNavigationBarColor(color);
            getDialog().getWindow().setStatusBarColor(color);
        }
    }

    private  void setAndroidNativeLightStatusBar( boolean dark) {
        View decor =getDialog().getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

//        //设置dialog背景色为透明色
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置dialog窗体颜色透明
        getDialog().getWindow().setDimAmount(0);
        setFullscreen(true, true);
        setAndroidNativeLightStatusBar( true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                startAnimate(mBind.rlContent,false);
                break;
            case R.id.rlAmount:
                addBubbleView();
                break;
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_treasure_box;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);



        view.setVisibility(View.GONE);
        int screenWidth= ScreenUtils.getScreenWidth(getActivity());
        int screenHeight=ScreenUtils.getScreenHeight(getActivity());
        mBind.rlContent.getLayoutParams().height=(int)(screenHeight*0.69f);

        FixImageSize.setImageSizeOnWidthWithSRC(mBind.ivFirstTimeTopUp, screenWidth, new FixImageSize.OnFixListener() {
            @Override
            public void onfix(int width, int height, float ratio) {

                RelativeLayout.LayoutParams rlContent=(RelativeLayout.LayoutParams)mBind.rrlContent.getLayoutParams();
                rlContent.topMargin=height/2;
                mBind.rrlContent.setLayoutParams(rlContent);
                mBind.rrlContent.setPadding(0,height/2,0,0);

                initAdapter(height);
                view.setVisibility(View.VISIBLE);
            }
        });

        addTabView();
        startAnimate(mBind.rlContent,true);
    }

    private void addBubbleView()
    {
        int screenWidth=ScreenUtils.getScreenWidth(getActivity());
        int height=mBind.rlContent.getLayoutParams().height;
        RelativeLayout relativeLayout=new RelativeLayout(getActivity());
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height));
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBind.rlContent.removeView(view);
            }
        });

        BotTriangleBubbleView botTriangleBubbleView=new BotTriangleBubbleView(getActivity());
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rl.leftMargin=screenWidth-ScreenUtils.dp2px(getActivity(),132);
        rl.bottomMargin=ScreenUtils.dp2px(getActivity(),50);
        rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        botTriangleBubbleView.setLayoutParams(rl);
        relativeLayout.addView(botTriangleBubbleView);
        botTriangleBubbleView.setOnCLickItemListener(new BotTriangleBubbleView.onCLickItemListener() {
            @Override
            public void onClick(String amount) {
                mBind.tvAmount.setText(amount);
                mBind.rlContent.removeView(relativeLayout);
            }
        });

        mBind.rlContent.addView(relativeLayout);
    }

    private void addTabView()
    {
        String tabTitles[]=getResources().getStringArray(R.array.giftDialogTabs);

        int itemWidth=ScreenUtils.dp2px(getActivity(),60);
        int dip12=ScreenUtils.dp2px(getActivity(),12);
        for (int i = 0; i < tabTitles.length; i++) {
            RelativeLayout tabItemRL = new RelativeLayout(getContext());
            tabItemRL.setLayoutParams(new ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT));

            TextView tvTab = new TextView(getContext());
            tvTab.setText(tabTitles[i]);
            tvTab.setGravity(Gravity.CENTER);
            tvTab.setTextColor(0xffffffff);
            tvTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rl.topMargin=dip12;
            tvTab.setLayoutParams(rl);
            tabItemRL.addView(tvTab);

            ImageView imageView=new ImageView(getActivity());
            RelativeLayout.LayoutParams rlIMG=new RelativeLayout.LayoutParams(itemWidth/3,itemWidth/12);
            rlIMG.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
            rlIMG.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
            imageView.setLayoutParams(rlIMG);
            imageView.setVisibility(View.INVISIBLE);
            imageView.setBackground(getResources().getDrawable(R.drawable.tab_a800ff_ea00ff));
            tabItemRL.addView(imageView);

            mBind.tabLayout.addTab(mBind.tabLayout.newTab().setCustomView(tabItemRL));
        }

        mBind.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                RelativeLayout relativeLayout = (RelativeLayout) tab.getCustomView();
                ImageView line = (ImageView) relativeLayout.getChildAt(1);
                line.setVisibility(View.VISIBLE);
                setAdapterIndex(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                RelativeLayout relativeLayout = (RelativeLayout) tab.getCustomView();
                ImageView line = (ImageView) relativeLayout.getChildAt(1);
                line.setVisibility(View.GONE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                RelativeLayout relativeLayout = (RelativeLayout) tab.getCustomView();
                ImageView line = (ImageView) relativeLayout.getChildAt(1);
                line.setVisibility(View.VISIBLE);
            }
        });

        mBind.tabLayout.selectTab(mBind.tabLayout.getTabAt(0));
    }

    private void initAdapter(int marginTop)
    {
        lists=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            List<TreasureItemBean> list=new ArrayList<>();
            if(i<2)
            {
                for (int j = 0; j < 36; j++) {
                    TreasureItemBean bean=new TreasureItemBean();
                    bean.setCostDiamond(99);
                    bean.setImgUrl("");
                    bean.setSelected(false);
                    bean.setName("谢谢");
                    list.add(bean);
                }
            }
            lists.add(list);
        }

        int dip10=ScreenUtils.dp2px(getActivity(),10);
        int viewPagerHeight= mBind.rlContent.getLayoutParams().height-marginTop-dip10*12;
        adapter=new TreasureBoxPagerAdapter(getActivity(),viewPagerHeight,lists);
        mBind.viewPager.setOffscreenPageLimit(1);
        mBind.viewPager.setAdapter(adapter);
        setAdapterIndex(0);

        mBind.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mBind.rlCircles.setSelectedIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void setAdapterIndex(int index)
    {
        mBind.viewPager.setCurrentItem(0);
        adapter.setPageIndex(index);
        if(adapter.getLists().get(index)==null)
        {
            mBind.rlCircles.setIndicatorAmount(0);
        }
        else
        {
            int totalSize=adapter.getLists().get(index).size();
            int pageSize=totalSize%8==0?(totalSize/8):(totalSize/8+1);
            mBind.rlCircles.setIndicatorAmount(pageSize);
        }

    }

}