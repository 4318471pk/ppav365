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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.adapter.RankAdapter;
import com.live.fox.adapter.devider.RecyclerSpace;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseFragment;
import com.live.fox.databinding.RankFragmentBinding;
import com.live.fox.entity.User;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.DeviceUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.RankProfileView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import me.grantland.widget.AutofitTextView;


public class RankFragment extends BaseBindingFragment {

    RankFragmentBinding mBind;
    RankAdapter rankAdapter;

    public static RankFragment newInstance(int type, int rankType) {
        RankFragment fragment = new RankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.rank_fragment;
    }

    @Override
    public void initView(View view) {
        initData(view);
    }

    public void initData(View view) {
        mBind=getViewDataBinding();
        int widthScreen= ScreenUtils.getScreenWidth(getActivity());
        int dip5=ScreenUtils.getDip2px(getActivity(),5);
        int margin=(widthScreen-(int)(widthScreen*0.147f*6))/10;
        String[] stringArray = getResources().getStringArray(R.array.rank_tab);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rvList.setLayoutManager(layoutManager);
        mBind.rvList.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 4)));
        rankAdapter=new RankAdapter(R.layout.layout_rank_item,new ArrayList());

        mBind.smartRefresh.setEnableAutoLoadMore(false);
        mBind.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
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
            mBind.rgTabs.addView(radioButton);
        }

        View header=makeHeader(widthScreen);
        rankAdapter.addHeaderView(header);
        mBind.rvList.setAdapter(rankAdapter);
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
                relativeLayout.addView(makeTop3View(width,height,0,2));
                relativeLayout.addView(makeTop3View(width,height,1,3));
                relativeLayout.addView(makeTop3View(width,height,2,4));
            }
        });

        return relativeLayout;
    }

    private View makeTop3View(int screenWidth,int height,int crownIndex,int decorationIndex)
    {
        int itemWidth=(int)(screenWidth*0.258f);
        LinearLayout linearLayout=new LinearLayout(getActivity());
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (crownIndex)
        {
            case 0:
                rl.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                rl.topMargin=(int)(height*0.22f);
                break;
            case 1:
                rl.topMargin=(int)(height*0.24f);
                rl.leftMargin=(int)(screenWidth*0.085f);
                rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
                break;
            case 2:
                rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
                rl.topMargin=(int)(height*0.24f);
                rl.rightMargin=(int)(screenWidth*0.085f);
                break;
        }

        linearLayout.setLayoutParams(rl);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        int profileImageWidth=(int)(screenWidth*0.173f);
        RankProfileView rankProfileView=new RankProfileView(getActivity(),crownIndex,decorationIndex);
        LinearLayout.LayoutParams ivRL=new LinearLayout.LayoutParams(profileImageWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivRL.gravity=Gravity.CENTER_HORIZONTAL;
        rankProfileView.setLayoutParams(ivRL);
        linearLayout.addView(rankProfileView);

        AutofitTextView textView=new AutofitTextView(getActivity());
        textView.setText("你的名字你的名字");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(0xffffffff);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        textView.setMaxLines(1);
        textView.setSizeToFit();
        LinearLayout.LayoutParams tvLL=new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tvLL.topMargin=ScreenUtils.getDip2px(getActivity(),5);
        textView.setLayoutParams(tvLL);
        linearLayout.addView(textView);

        TextView icons=new TextView(getActivity());
        LinearLayout.LayoutParams llIcons=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        llIcons.topMargin=ScreenUtils.getDip2px(getActivity(),5);
        llIcons.gravity=Gravity.CENTER_HORIZONTAL;
        icons.setLayoutParams(llIcons);
        User user=new User();
        user.setUserLevel(new Random().nextInt(200));
        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(user, getActivity()));
        icons.setText(spanUtils.create());
        linearLayout.addView(icons);

        return linearLayout;
    }

}

