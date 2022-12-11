package com.live.fox.dialog.bottomDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.DialogContributionRankBinding;
import com.live.fox.entity.ContributionRankItemBean;
import com.live.fox.server.Api_Rank;
import com.live.fox.server.Api_User;
import com.live.fox.ui.mine.contribution.ContributionRankFragment;
import com.live.fox.ui.rank.RankActivity;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContributionRankDialog extends BaseBindingDialogFragment {

    DialogContributionRankBinding mBind;
    String liveId,uid;
    int currentPosition=0;
    List<ContributionRankFragment> fragmentList=new ArrayList<>();
    List<List<ContributionRankItemBean>> dataLists=new ArrayList<>();


    public static final ContributionRankDialog getInstance(String liveId,String uid)
    {
        ContributionRankDialog contributionRankDialog= new ContributionRankDialog();
        contributionRankDialog.liveId=liveId;
        contributionRankDialog.uid=uid;
        return contributionRankDialog;
    }

    public List<List<ContributionRankItemBean>> getDataLists() {
        return dataLists;
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
    public boolean onBackPress() {
        startAnimate(mBind.rllContent,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        switch (view.getId())
        {
            case R.id.rlMain:
                mBind.rlMain.setEnabled(false);
                startAnimate(mBind.rllContent,false);
                break;
            case R.id.ivRank:
                RankActivity.startActivity(getActivity());
                break;
        }

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_contribution_rank;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        int screenHeight= ScreenUtils.getScreenHeight(getActivity());
        mBind.rllContent.getLayoutParams().height=(int)(screenHeight*0.7f);

        setData();
        getContributionList();
        startAnimate(mBind.rllContent,true);
    }


    private void setData()
    {
        String titles[]=getResources().getStringArray(R.array.rank_tab_contribution);
        int widthScreen= ScreenUtils.getScreenWidth(getActivity());

        fragmentList.add(ContributionRankFragment.newInstance(0));
        fragmentList.add(ContributionRankFragment.newInstance(1));
        fragmentList.add(ContributionRankFragment.newInstance(2));
        fragmentList.add(ContributionRankFragment.newInstance(3));

        mBind.vpDialogMain.setAdapter(new BaseFragmentPagerAdapter(getChildFragmentManager()){
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

        mBind.vpDialogMain.setOffscreenPageLimit(3);
        mBind.vpDialogMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        int dip5=ScreenUtils.getDip2px(getActivity(),5);

        for (int i = 0; i < titles.length; i++) {
            RadioButton radioButton=new RadioButton(getActivity());
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
                        mBind.vpDialogMain.setCurrentItem((int)compoundButton.getTag());
                    }

                }
            });
            mBind.rgTabs.addView(radioButton);
        }

        ((RadioButton)mBind.rgTabs.getChildAt(0)).setChecked(true);
    }

    public void getContributionList()
    {
        Api_Rank.ins().getContributionRankList(liveId,uid,new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(isConditionOk())
                {
                    if(code==0 && getArg().equals(liveId))
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
                    else
                    {

                    }
                }
            }
        });
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
