package com.live.fox.ui.rank;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.adapter.RankAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseFragment;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.RankTop3ProfileImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class RankFragment extends BaseFragment {

    RadioGroup rgTabs;
    RecyclerView rvList;
    SmartRefreshLayout smartRefresh;
    RankAdapter rankAdapter;

    public static RankFragment newInstance(int type, int rankType) {
        RankFragment fragment = new RankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rank_fragment, null, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData(getArguments(),view);
    }

    public void initData(Bundle bundle,View view) {
        int widthScreen= ScreenUtils.getScreenWidth(getActivity());
        int dip5=ScreenUtils.getDip2px(getActivity(),5);
        int margin=(widthScreen-(int)(widthScreen*0.147f*6))/10;
        String[] stringArray = getResources().getStringArray(R.array.rank_tab);
        rvList=view.findViewById(R.id.rvList);
        smartRefresh=view.findViewById(R.id.smartRefresh);
        rgTabs=view.findViewById(R.id.rgTabs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 4)));
        rankAdapter=new RankAdapter(R.layout.layout_rank_item,new ArrayList());

        smartRefresh.setEnableAutoLoadMore(false);
        smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });

        for (int i = 0; i < stringArray.length; i++) {
            RadioButton radioButton=new RadioButton(getActivity());
            radioButton.setButtonDrawable(new ColorDrawable(0));
            radioButton.setText(stringArray[i]);
            radioButton.setTextColor(0xffa800ff);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            RadioGroup.LayoutParams rl=new RadioGroup.LayoutParams((int)(widthScreen*0.147f),dip5*6);
            rl.leftMargin=margin;
            rl.topMargin=dip5;
            rl.bottomMargin=dip5;
            if(i==3)
            {
                rl.leftMargin=(widthScreen-(int)(widthScreen*0.147f*6))-margin*6;
            }
            radioButton.setLayoutParams(rl);
            radioButton.setBackgroundResource(R.drawable.round_stroke_a800ff);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    compoundButton.setTextColor(b?0xffffffff:0xffa800ff);
                    compoundButton.setBackgroundResource(b?R.drawable.round_gradient_a800ff_d689ff:R.drawable.round_stroke_a800ff);
                }
            });
            rgTabs.addView(radioButton);
        }

        View header=makeHeader(widthScreen);
        rankAdapter.addHeaderView(header);
        rvList.setAdapter(rankAdapter);
    }


    private View makeHeader(int screenWidth)
    {
        RelativeLayout relativeLayout=new RelativeLayout(getActivity());
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ImageView ivBackground=new ImageView(getActivity());
        ivBackground.setImageDrawable(getResources().getDrawable(R.mipmap.top3_background));
        ivBackground.setAdjustViewBounds(true);
        ivBackground.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        relativeLayout.addView(ivBackground);

        FixImageSize.setImageSizeOnWidthWithSRC(ivBackground, screenWidth, new FixImageSize.OnFixListener() {
            @Override
            public void onfix(int width, int height, float ratio) {
                relativeLayout.addView(makeTop1View(width,height));
                relativeLayout.addView(makeTop2View(width,height));
                relativeLayout.addView(makeTop3View(width,height));
            }
        });

        return relativeLayout;
    }

    private View makeTop1View(int screenWidth,int height)
    {
        int itemWidth=(int)(screenWidth*0.173f);
        RelativeLayout relativeLayout=new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(itemWidth, height);
        rl.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        rl.topMargin=(int)(height*0.3f);
        relativeLayout.setLayoutParams(rl);

        RankTop3ProfileImageView crown=new RankTop3ProfileImageView(getActivity(),0);
        RelativeLayout.LayoutParams ivRL=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivRL.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        crown.setLayoutParams(ivRL);
        relativeLayout.addView(crown);

        return relativeLayout;
    }

    private View makeTop2View(int screenWidth,int height)
    {
        int itemWidth=(int)(screenWidth*0.173f);
        RelativeLayout relativeLayout=new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(itemWidth, height);
        rl.topMargin=(int)(height*0.32f);
        rl.leftMargin=(int)(screenWidth*0.13f);
        rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        relativeLayout.setLayoutParams(rl);

        RankTop3ProfileImageView crown=new RankTop3ProfileImageView(getActivity(),0);
        RelativeLayout.LayoutParams ivRL=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivRL.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        crown.setLayoutParams(ivRL);
        relativeLayout.addView(crown);

        return relativeLayout;

    }

    private View makeTop3View(int screenWidth,int height)
    {
        int itemWidth=(int)(screenWidth*0.173f);
        RelativeLayout relativeLayout=new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        rl.topMargin=(int)(height*0.32f);
        rl.rightMargin=(int)(screenWidth*0.13f);
        relativeLayout.setLayoutParams(rl);

        RankTop3ProfileImageView crown=new RankTop3ProfileImageView(getActivity(),0);
        RelativeLayout.LayoutParams ivRL=new RelativeLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivRL.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        crown.setLayoutParams(ivRL);
        relativeLayout.addView(crown);

        return relativeLayout;
    }

}

