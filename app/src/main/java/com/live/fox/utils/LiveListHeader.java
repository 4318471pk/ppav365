package com.live.fox.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.live.fox.R;
import com.luck.picture.lib.tools.ScreenUtils;
import com.marquee.dingrui.marqueeviewlib.MarqueeView;

public class LiveListHeader extends RelativeLayout {

    ConvenientBanner convenientBanner;
    MarqueeView ivHomeHotRecommendRolling;
    MarqueeView ivHomePlayGameRolling;
    HorizontalScrollView hsPlayGameList;
    HorizontalScrollView hsHotRecommendList;

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

    private void initView()
    {
        addView(View.inflate(getContext(), R.layout.fragment_header_livelist,null));
        hsHotRecommendList=findViewById(R.id.hsHotRecommendList);
        hsPlayGameList=findViewById(R.id.hsPlayGameList);
        ivHomePlayGameRolling=findViewById(R.id.ivHomePlayGameRolling);
        ivHomeHotRecommendRolling=findViewById(R.id.ivHomeHotRecommendRolling);
        convenientBanner=findViewById(R.id.convenientBanner);

        hsHotRecommendList.addView(getfakeData());
        hsPlayGameList.addView(getfakeData());
    }

    private LinearLayout getfakeData()
    {
        //假数据-------------------
        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        int dip1= ScreenUtils.dip2px(getContext(),1);
        for (int i = 0; i <20; i++) {
            TextView textView=new TextView(getContext());
            textView.setText(getResources().getText(R.string.home_bottom_tab_game));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(getResources().getColor(R.color.red));
            textView.setTextColor(0xff404040);
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(dip1*60,dip1*60);
            ll.leftMargin=dip1*5;
            textView.setLayoutParams(ll);
            linearLayout.addView(textView);
        }
        return linearLayout;
    }
}
