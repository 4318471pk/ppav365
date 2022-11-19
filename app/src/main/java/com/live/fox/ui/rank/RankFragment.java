package com.live.fox.ui.rank;

import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.Gravity;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.RankAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.RankFragmentBinding;
import com.live.fox.entity.RankItemBean;
import com.live.fox.entity.User;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.RankProfileView;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.grantland.widget.AutofitTextView;


public class RankFragment extends BaseBindingFragment {

    RankFragmentBinding mBind;
    RankAdapter rankAdapter;
    int type;
    int currentTimePosition=0;
    RelativeLayout header;

    public static RankFragment newInstance(int type) {
        RankFragment fragment = new RankFragment();
        fragment.type=type;
        return fragment;
    }

    @Override
    public void onClickView(View view) {

    }

    private RankActivity getRankActivity()
    {
        if(isActivityOK())
        {
            RankActivity rankActivity=(RankActivity)getActivity();
            return rankActivity;
        }
        return null;
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
//        mBind.rvList.addItemDecoration(new RecyclerSpace(DeviceUtils.dp2px(requireActivity(), 4)));

        List<RankItemBean> list=new ArrayList<>();
        for (int i = 0; i <27 ; i++) {
            list.add(null);
        }
        rankAdapter=new RankAdapter(getActivity(),list);

        mBind.smartRefresh.setEnableAutoLoadMore(false);
        mBind.smartRefresh.setRefreshHeader(new MyWaterDropHeader(getActivity()));
        mBind.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                switch (type)
                {
                    case 0:
                        getRankActivity().getAnchorList();
                        break;
                    case 1:
                        getRankActivity().getRichManList();
                        break;
                }

            }
        });

        for (int i = 0; i < stringArray.length; i++) {
            RadioButton radioButton=new RadioButton(getActivity());
            radioButton.setButtonDrawable(new ColorDrawable(0));
            radioButton.setText(stringArray[i]);
            radioButton.setTextColor(0xffa800ff);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            radioButton.setTag(i);
            RadioGroup.LayoutParams rl=new RadioGroup.LayoutParams((int)(widthScreen*0.147f),dip5*6);
            rl.leftMargin=margin;
            rl.topMargin=dip5*2;
            rl.bottomMargin=dip5*2;
            if(i==3)
            {
                rl.leftMargin=(widthScreen-(int)(widthScreen*0.147f*6))-margin*6;
            }
            radioButton.setLayoutParams(rl);
            radioButton.setBackgroundResource(R.drawable.round_stroke_a800ff);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b)
                    {
                        int index=(int)compoundButton.getTag();
                        currentTimePosition=index;
                        setPageData();
                    }
                    compoundButton.setTextColor(b?0xffffffff:0xffa800ff);
                    compoundButton.setBackgroundResource(b?R.drawable.round_gradient_a800ff_d689ff:R.drawable.round_stroke_a800ff);
                }
            });
            mBind.rgTabs.addView(radioButton);
        }
        RadioButton radioButton=(RadioButton)mBind.rgTabs.getChildAt(0);
        radioButton.setChecked(true);

        header=makeHeader(widthScreen);
        rankAdapter.addHeaderView(header);
        mBind.rvList.setAdapter(rankAdapter);
    }

    @Override
    public void notifyFragment() {
        super.notifyFragment();
        mBind.smartRefresh.finishRefresh(true);
        setPageData();
    }

    private void setPageData()
    {
        List<RankItemBean> list;
        switch (type)
        {
            case 0:
                if(getRankActivity().rankAnchorBeans.size()>currentTimePosition)
                {
                    list=getRankActivity().rankAnchorBeans.get(currentTimePosition);
                    if(list!=null)
                    {
                        if(list.size()>3)
                        {
                            rankAdapter.setNewData(list);
                        }
                        else
                        {
                            setHeadData();
                            setEmptyData();
                        }
                    }
                }
                break;
            case 1:
                if(getRankActivity().rankRichManBeans.size()>currentTimePosition)
                {
                    list=getRankActivity().rankRichManBeans.get(currentTimePosition);
                    if(list!=null)
                    {
                        if(list.size()>3)
                        {
                            rankAdapter.setNewData(list);
                        }
                        else
                        {
                            setHeadData();
                            setEmptyData();
                        }
                    }
                }
                break;
        }
    }

    private void setEmptyData()
    {
        List list=new ArrayList();
        for (int i = 0; i < 27; i++) {
            list.add(null);
        }
        rankAdapter.setNewData(list);
    }

    private void setHeadData()
    {
        if(header==null)
        {
            return;
        }

        List<RankItemBean> tabList=null;
        switch (type)
        {
            case 0:
                tabList=getRankActivity().rankAnchorBeans.get(currentTimePosition);
                break;
            case 1:
                tabList=getRankActivity().rankRichManBeans.get(currentTimePosition);
                break;
        }
        if(currentTimePosition>=tabList.size())
        {
            return;
        }


        for (int i = 1; i <4 ; i++) {
            LinearLayout linearLayout=(LinearLayout) header.getChildAt(i);
            RankProfileView profileView=(RankProfileView)linearLayout.getChildAt(0);
            TextView nickName=(TextView)linearLayout.getChildAt(1);
            TextView icons=(TextView)linearLayout.getChildAt(2);

            if(tabList.size()>i-1)
            {
                RankItemBean rankItemBean=tabList.get(i-1);
                nickName.setText(rankItemBean.getNickname());
                GlideUtils.loadCircleImage(getActivity(),rankItemBean.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,profileView.getProfileImage());
//                icons.setText("");
            }
            else
            {
                profileView.setIndex(i-1,0,false);
                profileView.getProfileImage().setImageDrawable(getResources().getDrawable(R.mipmap.user_head_error));
                nickName.setText(getStringWithoutContext(R.string.emptyPosition));
                icons.setText("");
            }
        }

        String templeText=getActivity().getResources().getString(R.string.tip10);
        String followString=getActivity().getResources().getString(R.string.follow);
        String followedString=getActivity().getResources().getString(R.string.followed);
        for (int i = 4; i <7 ; i++) {
            LinearLayout relativeLayout=(LinearLayout) header.getChildAt(i);
            TextView tvHuo=(TextView)relativeLayout.getChildAt(0);
            TextView follow=(TextView)relativeLayout.getChildAt(1);

            if(tabList.size()>i-4)
            {
                RankItemBean rankItemBean=tabList.get(i-4);
                tvHuo.setText(String.format(templeText,rankItemBean.getRankValue()+""));
                follow.setVisibility(View.VISIBLE);
                follow.setSelected(rankItemBean.isFollow());
                follow.setText(rankItemBean.isFollow()?followedString:followString);
                follow.setEnabled(!rankItemBean.isFollow());
//                icons.setText("");
            }
            else
            {
                follow.setVisibility(View.INVISIBLE);
                tvHuo.setText("");
            }
        }
    }

    private RelativeLayout makeHeader(int screenWidth)
    {
        RelativeLayout relativeLayout=new RelativeLayout(getActivity());
        relativeLayout.setPadding(0,0,0,ScreenUtils.getDip2px(getActivity(),10));
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ImageView ivBackground=new ImageView(getActivity());
        if(type==0)
        {
            ivBackground.setImageDrawable(getResources().getDrawable(R.mipmap.bg_rank_anchor));
        }
        else
        {
            ivBackground.setImageDrawable(getResources().getDrawable(R.mipmap.bg_rank_tuhao));
        }

        ivBackground.setAdjustViewBounds(true);
        ivBackground.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        relativeLayout.addView(ivBackground);

        FixImageSize.setImageSizeOnWidthWithSRC(ivBackground, screenWidth, new FixImageSize.OnFixListener() {
            @Override
            public void onfix(int width, int height, float ratio) {
                relativeLayout.addView(makeTop3View(width,height,0,0));
                relativeLayout.addView(makeTop3View(width,height,1,0));
                relativeLayout.addView(makeTop3View(width,height,2,0));

                relativeLayout.addView(makeBotView(width,height,0));
                relativeLayout.addView(makeBotView(width,height,1));
                relativeLayout.addView(makeBotView(width,height,2));

            }
        });

        return relativeLayout;
    }

    private View makeTop3View(int screenWidth,int height,int crownIndex,int decorationIndex)
    {
        int itemWidth=(int)(screenWidth*0.275f);
        LinearLayout linearLayout=new LinearLayout(getActivity());
        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (crownIndex)
        {
            case 0:
                rl.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                rl.topMargin=(int)(height*0.22f);
                break;
            case 1:
                rl.topMargin=(int)(height*0.28f);
                rl.leftMargin=(int)(screenWidth*0.072f);
                rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
                break;
            case 2:
                rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
                rl.topMargin=(int)(height*0.28f);
                rl.rightMargin=(int)(screenWidth*0.072f);
                break;
        }

        linearLayout.setLayoutParams(rl);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        int profileImageWidth=(int)(screenWidth*0.173f);
        RankProfileView rankProfileView=new RankProfileView(getActivity(),crownIndex,decorationIndex,false);
        LinearLayout.LayoutParams ivRL=new LinearLayout.LayoutParams(profileImageWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivRL.gravity=Gravity.CENTER_HORIZONTAL;
        rankProfileView.setResumeAniAfterAttached(true);
        rankProfileView.setLayoutParams(ivRL);
        rankProfileView.getProfileImage().setImageDrawable(getResources().getDrawable(R.mipmap.user_head_error));
//        rankProfileView.setIndex(crownIndex,decorationIndex,true);
        linearLayout.addView(rankProfileView);

        AutofitTextView textView=new AutofitTextView(getActivity());
        textView.setText(getStringWithoutContext(R.string.emptyPosition));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(0xffffffff);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        textView.setMaxLines(1);
//        textView.setSizeToFit();
        LinearLayout.LayoutParams tvLL=new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tvLL.topMargin=ScreenUtils.getDip2px(getActivity(),5);
        textView.setLayoutParams(tvLL);
        linearLayout.addView(textView);

        TextView icons=new TextView(getActivity());
        LinearLayout.LayoutParams llIcons=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        llIcons.topMargin=ScreenUtils.getDip2px(getActivity(),5);
        llIcons.gravity=Gravity.CENTER_HORIZONTAL;
        icons.setGravity(Gravity.CENTER);
        icons.setLayoutParams(llIcons);
//        User user=new User();
//        user.setUserLevel(new Random().nextInt(200));
//        SpanUtils spanUtils=new SpanUtils();
//        spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(user, getActivity()));
//        icons.setText(spanUtils.create());
        linearLayout.addView(icons);

        return linearLayout;
    }


    private LinearLayout makeBotView(int screenWidth,int height,int crownIndex)
    {
        int itemWidth=(int)(screenWidth*0.275f);
        LinearLayout linearLayout=new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout.LayoutParams rl=new RelativeLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (crownIndex)
        {
            case 0:
                rl.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
                break;
            case 1:
                rl.leftMargin=(int)(screenWidth*0.072f);
                rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
                break;
            case 2:
                rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
                rl.rightMargin=(int)(screenWidth*0.072f);
                break;
        }
        rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        linearLayout.setLayoutParams(rl);

        TextView huo=new TextView(getActivity());
        huo.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
        huo.setText("");
        huo.setGravity(Gravity.CENTER);
        huo.setTextColor(0xffffffff);
        huo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(huo);

        int followWidth=(int)(screenWidth*0.16f);
        int followHeight=(int)(1.0f*followWidth*48/120);
        TextView follow=new TextView(getActivity());
        follow.setBackground(getResources().getDrawable(R.drawable.bg_follow));
        LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(followWidth,followHeight);
        ll.gravity=Gravity.CENTER_HORIZONTAL;
        ll.topMargin=ScreenUtils.getDip2px(getActivity(),5);
        ll.bottomMargin=ScreenUtils.getDip2px(getActivity(),2);
        follow.setLayoutParams(ll);
        follow.setGravity(Gravity.CENTER);
        follow.setTextColor(0xffffffff);
        follow.setText(getStringWithoutContext(R.string.follow));
        follow.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        follow.setVisibility(View.INVISIBLE);
        linearLayout.addView(follow);

        return linearLayout;
    }

}

