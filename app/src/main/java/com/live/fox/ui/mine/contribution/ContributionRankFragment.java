package com.live.fox.ui.mine.contribution;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.ContributionRankAdapter;
import com.live.fox.adapter.RankAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentContributionRankBinding;
import com.live.fox.entity.ContributionRankIndexBean;
import com.live.fox.entity.RankIndexBean;
import com.live.fox.entity.User;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.RankProfileView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.grantland.widget.AutofitTextView;

public class ContributionRankFragment extends BaseBindingFragment {

    FragmentContributionRankBinding mBind;
    ContributionRankAdapter adapter;
    int position;

    public static ContributionRankFragment newInstance(int position)
    {
        ContributionRankFragment fragment=new ContributionRankFragment();
        fragment.position=position;
        return fragment;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_contribution_rank;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        List<ContributionRankIndexBean> list=new ArrayList<>();
        for (int i = 0; i <27 ; i++) {
            ContributionRankIndexBean rankIndexBean=new ContributionRankIndexBean();
            rankIndexBean.setLevel(i+20);
            rankIndexBean.setFollow(i%2==0);
            rankIndexBean.setHuo("落后2.68万火力");
            rankIndexBean.setNickName("名字");
            rankIndexBean.setImageUrl("");
            list.add(rankIndexBean);
        }
        adapter=new ContributionRankAdapter(getActivity(),list);

        int widthScreen= ScreenUtils.getScreenWidth(getActivity());
        int dip5=ScreenUtils.getDip2px(getActivity(),5);
        int margin=(widthScreen-(int)(widthScreen*0.147f*6))/10;
        View header=makeHeader(widthScreen);
        adapter.addHeaderView(header);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rvMain.setLayoutManager(layoutManager);
        mBind.rvMain.setAdapter(adapter);

    }

    private View makeHeader(int screenWidth)
    {
        RelativeLayout relativeLayout=new RelativeLayout(getActivity());
        relativeLayout.setPadding(0,0,0,ScreenUtils.getDip2px(getActivity(),10));
        relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ImageView ivBackground=new ImageView(getActivity());
        ivBackground.setImageDrawable(getResources().getDrawable(R.mipmap.bg_contyribution_rank));

        ivBackground.setAdjustViewBounds(true);
        ivBackground.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        relativeLayout.addView(ivBackground);

        FixImageSize.setImageSizeOnWidthWithSRC(ivBackground, screenWidth, new FixImageSize.OnFixListener() {
            @Override
            public void onfix(int width, int height, float ratio) {
                relativeLayout.addView(makeTop3View(width,height,0,2));
                relativeLayout.addView(makeTop3View(width,height,1,3));
                relativeLayout.addView(makeTop3View(width,height,2,4));

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
        RankProfileView rankProfileView=new RankProfileView(getActivity(),crownIndex,decorationIndex);
        LinearLayout.LayoutParams ivRL=new LinearLayout.LayoutParams(profileImageWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        ivRL.gravity= Gravity.CENTER_HORIZONTAL;
        rankProfileView.setLayoutParams(ivRL);
        linearLayout.addView(rankProfileView);

        AutofitTextView textView=new AutofitTextView(getActivity());
        textView.setText("你的名字你的名字");
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
        User user=new User();
        user.setUserLevel(new Random().nextInt(200)+2);
        SpanUtils spanUtils=new SpanUtils();
        spanUtils.append(ChatSpanUtils.ins().getAllIconSpan(user, getActivity()));
        icons.setText(spanUtils.create());
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
        huo.setText("落后2.88万火力");
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
        ll.topMargin= ScreenUtils.getDip2px(getActivity(),5);
        ll.bottomMargin=ScreenUtils.getDip2px(getActivity(),2);
        follow.setLayoutParams(ll);
        follow.setGravity(Gravity.CENTER);
        follow.setTextColor(0xffffffff);
        follow.setText(getStringWithoutContext(R.string.follow));
        follow.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        linearLayout.addView(follow);

        return linearLayout;
    }
}
