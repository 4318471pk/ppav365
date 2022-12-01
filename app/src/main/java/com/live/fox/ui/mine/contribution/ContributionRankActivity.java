package com.live.fox.ui.mine.contribution;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.ActivityContributionRankBinding;
import com.live.fox.entity.ContributionRankItemBean;
import com.live.fox.manager.DataCenter;
import com.live.fox.server.Api_Rank;
import com.live.fox.ui.rank.RankActivity;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContributionRankActivity extends BaseBindingViewActivity {

    ActivityContributionRankBinding mBind;
    List<List<ContributionRankItemBean>> dataLists=new ArrayList<>();
    List<ContributionRankFragment> fragmentList=new ArrayList<>();
    int currentPosition=0;
    String ContributionDataList;

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,ContributionRankActivity.class));
    }

    public static void startActivity(Context context,String ContributionDataList)
    {
        Intent intent=new Intent(context,ContributionRankActivity.class);
        intent.putExtra("ContributionData",ContributionDataList);
        context.startActivity(intent);
    }

    public List<List<ContributionRankItemBean>> getDataLists() {
        return dataLists;
    }

    @Override
    public void onClickView(View view) {
        if(view.getId()==mBind.ivRank.getId())
        {
            RankActivity.startActivity(this);
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_contribution_rank;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        mBind.setClick(this);
        setActivityTitle(R.string.listOfContributionBoss);

        this.ContributionDataList=getIntent().getStringExtra("ContributionData");
        String titles[]=getResources().getStringArray(R.array.rank_tab_contribution);
        int widthScreen= ScreenUtils.getScreenWidth(this);
        fragmentList.add(ContributionRankFragment.newInstance(0));
        fragmentList.add(ContributionRankFragment.newInstance(1));
        fragmentList.add(ContributionRankFragment.newInstance(2));
        fragmentList.add(ContributionRankFragment.newInstance(3));
        mBind.vpMain.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public String getTitle(int position) {
                return titles[position];
            }

            @Override
            public int getItemCount() {
                return titles.length;
            }
        });

        mBind.vpMain.setOffscreenPageLimit(4);
        mBind.vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton radioButton=(RadioButton)mBind.rgTabs.getChildAt(position);
                radioButton.setChecked(true);
                currentPosition=position;
                fragmentList.get(currentPosition).notifyFragment();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int dip5=ScreenUtils.getDip2px(this,5);

        for (int i = 0; i < titles.length; i++) {
            RadioButton radioButton=new RadioButton(this);
            radioButton.setButtonDrawable(new ColorDrawable(0));
            radioButton.setText(titles[i]);
            radioButton.setTextColor(0xffa800ff);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            RadioGroup.LayoutParams rl=new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip5*6);
            rl.leftMargin=dip5;
            rl.gravity=Gravity.CENTER_VERTICAL;
            rl.weight=1;
            radioButton.setLayoutParams(rl);
            radioButton.setTag(i);
            radioButton.setBackgroundResource(R.drawable.round_stroke_a800ff);
//            if(i==0)
//            {
//                radioButton.performClick()
//            }
//            else
//            {
//                radioButton.setChecked(false);
//            }
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    compoundButton.setTextColor(b?0xffffffff:0xffa800ff);
                    compoundButton.setBackgroundResource(b?R.drawable.round_gradient_a800ff_d689ff:R.drawable.round_stroke_a800ff);
                    if(b)
                    {
                        mBind.vpMain.setCurrentItem((int)compoundButton.getTag());
                    }

                }
            });
            mBind.rgTabs.addView(radioButton);
        }

        if(TextUtils.isEmpty(ContributionDataList))
        {
            getContributionList();
        }
        else
        {
            setData(ContributionDataList);
        }

        ((RadioButton)mBind.rgTabs.getChildAt(0)).setChecked(true);
    }

    public void getContributionList()
    {
        String uid=String.valueOf(DataCenter.getInstance().getUserInfo().getUser().getUid());

        Api_Rank.ins().getContributionRankList("",uid,new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                Log.e("getContributionList",data);
                if(code==0 )
                {
                    setData(data);
                }
                else
                {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void setData(String data)
    {
        try {
            JSONObject jsonObject=new JSONObject(data);
            dataLists.clear();
            analysisData(jsonObject,"dayList");
            analysisData(jsonObject,"weekList");
            analysisData(jsonObject,"monthList");
            analysisData(jsonObject,"allList");

            fragmentList.get(currentPosition).notifyFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void analysisData(JSONObject jsonObject, String arrayTitle)
    {
        String rankDayList=jsonObject.optString(arrayTitle);
        if(rankDayList!=null && rankDayList.length()>0)
        {
            dataLists.add(GsonUtil.getObjects(rankDayList, ContributionRankItemBean[].class));
        }
        else
        {
            dataLists.add(new ArrayList<>());
        }
    }
}
