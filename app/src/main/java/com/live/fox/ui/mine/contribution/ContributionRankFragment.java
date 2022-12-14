package com.live.fox.ui.mine.contribution;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.ContributionRankAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentContributionRankBinding;
import com.live.fox.dialog.bottomDialog.ContributionRankDialog;
import com.live.fox.entity.ContributionRankItemBean;
import com.live.fox.entity.RankItemBean;
import com.live.fox.entity.User;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
import com.live.fox.utils.ChatSpanUtils;
import com.live.fox.utils.FixImageSize;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.SpanUtils;
import com.live.fox.utils.Strings;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.RankProfileView;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.grantland.widget.AutofitTextView;

public class ContributionRankFragment extends BaseBindingFragment {

    FragmentContributionRankBinding mBind;
    ContributionRankAdapter adapter;
    int pagePosition;
    RelativeLayout header;

    public static ContributionRankFragment newInstance(int position)
    {
        ContributionRankFragment fragment=new ContributionRankFragment();
        fragment.pagePosition=position;
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
    public void notifyFragment() {
        super.notifyFragment();
        mBind.srlRefresh.finishRefresh(true);
        setPageData();
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        List<ContributionRankItemBean> list=new ArrayList<>();
        for (int i = 0; i <27 ; i++) {
            list.add(null);
        }
        adapter=new ContributionRankAdapter(getActivity(),list);
        adapter.setOnClickItemListener(new ContributionRankAdapter.OnClickItemListener() {
            @Override
            public void onClickFollow(ContributionRankItemBean bean, int position) {
                follow(bean.getUid(),position+3);
            }

            @Override
            public void onClickProfileImage(ContributionRankItemBean bean, int position) {
                if(Strings.isDigitOnly(bean.getUid()))
                {
                    UserDetailActivity.startActivity(getActivity(),Integer.valueOf(bean.getUid()));
                }
            }
        });

        int widthScreen= ScreenUtils.getScreenWidth(getActivity());
        int dip5=ScreenUtils.getDip2px(getActivity(),5);
        int margin=(widthScreen-(int)(widthScreen*0.147f*6))/10;
        header=makeHeader(widthScreen);
        adapter.addHeaderView(header);

        mBind.srlRefresh.setRefreshHeader(new MyWaterDropHeader(getActivity()));
        mBind.srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                refreshData();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBind.rvMain.setLayoutManager(layoutManager);
        mBind.rvMain.setAdapter(adapter);

    }

    private void setEmptyData()
    {
        List list=new ArrayList();
        for (int i = 0; i < 27; i++) {
            list.add(null);
        }
        adapter.setNewData(list);
    }

    private void refreshData()
    {
        if(getParentFragment()!=null && getParentFragment() instanceof ContributionRankDialog)
        {
            ContributionRankDialog contributionRankDialog=(ContributionRankDialog)getParentFragment();
            contributionRankDialog.getContributionList();
        }

        if(getActivity()!=null && getActivity() instanceof ContributionRankActivity)
        {
            ContributionRankActivity contributionRankActivity=(ContributionRankActivity)getActivity();
            contributionRankActivity.getContributionList();
        }
    }

    private  List<List<ContributionRankItemBean>> getDataList()
    {
        if(getParentFragment()!=null && getParentFragment() instanceof ContributionRankDialog)
        {
            ContributionRankDialog contributionRankDialog=(ContributionRankDialog)getParentFragment();
            return contributionRankDialog.getDataLists();
        }

        if(getActivity()!=null && getActivity() instanceof ContributionRankActivity)
        {
            ContributionRankActivity contributionRankActivity=(ContributionRankActivity)getActivity();
            return contributionRankActivity.getDataLists();
        }

        return null;
    }

    private void setPageData()
    {
        if(header==null)
        {
            return;
        }

        List<ContributionRankItemBean> list;
        List<ContributionRankItemBean> temple=new ArrayList<>();
        if(getDataList().size()>pagePosition)
        {
            list=getDataList().get(pagePosition);
            if(list!=null)
            {
                setHeadData();
                if(list.size()>3)
                {
                    for (int i =3; i < list.size(); i++) {
                        temple.add(list.get(i));
                    }
                    adapter.setNewData(temple);
                }
                else
                {
                    setEmptyData();
                }
            }
        }
    }

    private void setHeadData()
    {
        for (int i = 1; i <4 ; i++) {
            LinearLayout linearLayout=(LinearLayout) header.getChildAt(i);
            RankProfileView profileView=(RankProfileView)linearLayout.getChildAt(0);
            TextView nickName=(TextView)linearLayout.getChildAt(1);
            TextView icons=(TextView)linearLayout.getChildAt(2);

            if(getDataList().get(pagePosition).size()>i-1)
            {
                ContributionRankItemBean contributionRankItemBean=getDataList().get(pagePosition).get(i-1);
                nickName.setText(contributionRankItemBean.getNickname());
                profileView.setIndex(profileView.getCrownIndex(),contributionRankItemBean.getVipLevel(),false);
                GlideUtils.loadCircleImage(getActivity(),contributionRankItemBean.getAvatar(),R.mipmap.user_head_error,R.mipmap.user_head_error,profileView.getProfileImage());
                SpanUtils spanUtils=new SpanUtils();
                if(ChatSpanUtils.appendLevelIcon(spanUtils,contributionRankItemBean.getUserLevel(), getActivity()))
                {
                    spanUtils.append(" ");
                }
                if(ChatSpanUtils.appendVipLevelRectangleIcon(spanUtils,contributionRankItemBean.getVipLevel(), getActivity()))
                {
                    spanUtils.append(" ");
                }
                icons.setText(spanUtils.create());
                profileView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Strings.isDigitOnly(contributionRankItemBean.getUid()))
                        {
                            UserDetailActivity.startActivity(getActivity(),Integer.valueOf(contributionRankItemBean.getUid()));
                        }
                    }
                });
            }
            else
            {
                profileView.setIndex(profileView.getCrownIndex(),1,false);
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

            if(getDataList().get(pagePosition).size()>i-4)
            {
                ContributionRankItemBean rankItemBean=getDataList().get(pagePosition).get(i-4);
                if(!rankItemBean.isAnchor())
                {
                    relativeLayout.setVisibility(View.INVISIBLE);
                }
                else
                {
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                tvHuo.setText(String.format(templeText,rankItemBean.getRankValue()+""));
                follow.setVisibility(View.VISIBLE);
                follow.setSelected(rankItemBean.isFollow());
                follow.setText(rankItemBean.isFollow()?followedString:followString);
                follow.setEnabled(!rankItemBean.isFollow());
                follow.setTag(i-4);
                follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index=(int)v.getTag();
                        follow(rankItemBean.getUid(),index);
                    }
                });
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
        ivBackground.setImageDrawable(getResources().getDrawable(R.mipmap.bg_contyribution_rank));

        ivBackground.setAdjustViewBounds(true);
        ivBackground.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        relativeLayout.addView(ivBackground);

        FixImageSize.setImageSizeOnWidthWithSRC(ivBackground, screenWidth, new FixImageSize.OnFixListener() {
            @Override
            public void onfix(int width, int height, float ratio) {
                relativeLayout.addView(makeTop3View(width,height,0,1));
                relativeLayout.addView(makeTop3View(width,height,1,1));
                relativeLayout.addView(makeTop3View(width,height,2,1));

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
        icons.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);

        linearLayout.addView(icons);

        return linearLayout;
    }


    private LinearLayout makeBotView(int screenWidth,int height,int crownIndex)
    {
        int itemWidth=(int)(screenWidth*0.275f);
        LinearLayout linearLayout=new LinearLayout(getActivity());
        linearLayout.setVisibility(View.INVISIBLE);
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

    private void follow(String targetId,int position)
    {
        if(TextUtils.isEmpty(targetId))
        {
            return;
        }
        showLoadingDialogWithNoBgBlack();
        Api_User.ins().followUser(targetId, true, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                hideLoadingDialog();
                if(isActivityOK())
                {
                    if(code==0)
                    {
                        for (int i = 0; i < getDataList().size(); i++) {
                            List<ContributionRankItemBean> list=getDataList().get(i);
                            for (int j = 0; j <list.size() ; j++) {
                                if(!TextUtils.isEmpty(list.get(j).getUid()))
                                {
                                    if(list.get(j).getUid().equals(targetId))
                                    {
                                        getDataList().get(i).get(j).setFollow(true);
                                    }
                                }
                            }
                        }
                        setPageData();
                    }
                    else
                    {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });
    }
}
