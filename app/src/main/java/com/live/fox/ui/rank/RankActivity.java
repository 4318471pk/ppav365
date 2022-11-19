package com.live.fox.ui.rank;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.RankActivityBinding;
import com.live.fox.entity.RankItemBean;
import com.live.fox.server.Api_Rank;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.device.ScreenUtils;
import com.opensource.svgaplayer.SVGAParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 排行榜
 */
public class RankActivity extends BaseBindingViewActivity {

    RankActivityBinding mBind;
    private int titles[]=new int[]{R.string.anchorBana,R.string.conBan};
    List<List<RankItemBean>> rankAnchorBeans=new ArrayList<>();//主播
    List<List<RankItemBean>> rankRichManBeans=new ArrayList<>();//土豪
    List<RankFragment> rankFragments=new ArrayList<>();

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,RankActivity.class));
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public boolean isHasHeader() {
        return false;
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.rank_activity;
    }

    @Override
    public void initView()
    {
        mBind=getViewDataBinding();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        SVGAParser.Companion.shareParser().init(this);
        int widthScreen= ScreenUtils.getScreenWidth(this);
        mBind.tabLayout.setGradient(0xffA800FF,0xffEA00FF);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rankFragments.add(RankFragment.newInstance(0));
        rankFragments.add(RankFragment.newInstance(1));
        mBind.viewPager.setOffscreenPageLimit(1);
        mBind.viewPager.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getFragment(int position) {
                return rankFragments.get(position);
            }

            @Override
            public String getTitle(int position) {
                return getString(titles[position]);
            }

            @Override
            public int getItemCount() {
                return titles.length;
            }
        });


        mBind.tabLayout.setTabWidthPX((widthScreen-ScreenUtils.getDip2px(this,100))/2);
        mBind.tabLayout.setViewPager(mBind.viewPager);
        getAnchorList();
        getRichManList();
    }


    public void getAnchorList()
    {
        Api_Rank.ins().getRankList(1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(code==0 && !TextUtils.isEmpty(data))
                {
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        rankAnchorBeans.clear();
                        analysisAnchorData(jsonObject,"rankDayList");
                        analysisAnchorData(jsonObject,"rankWeekList");
                        analysisAnchorData(jsonObject,"rankMonthList");
                        analysisAnchorData(jsonObject,"lastDayList");
                        analysisAnchorData(jsonObject,"lastWeekList");
                        analysisAnchorData(jsonObject,"lastMonthList");

                        rankFragments.get(0).notifyFragment();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getRichManList()
    {
        Api_Rank.ins().getRankList(2, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(code==0 && !TextUtils.isEmpty(data))
                {
                    try {
                        JSONObject jsonObject=new JSONObject(data);
                        rankRichManBeans.clear();
                        analysisRichManData(jsonObject,"rankDayList");
                        analysisRichManData(jsonObject,"rankWeekList");
                        analysisRichManData(jsonObject,"rankMonthList");
                        analysisRichManData(jsonObject,"lastDayList");
                        analysisRichManData(jsonObject,"lastWeekList");
                        analysisRichManData(jsonObject,"lastMonthList");

                        rankFragments.get(1).notifyFragment();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void analysisAnchorData(JSONObject jsonObject,String arrayTitle)
    {
        String rankDayList=jsonObject.optString(arrayTitle);
        if(rankDayList!=null && rankDayList.length()>0)
        {
            rankAnchorBeans.add(GsonUtil.getObjects(rankDayList,RankItemBean[].class));
        }
        else
        {
            rankAnchorBeans.add(new ArrayList<>());
        }

    }

    private void analysisRichManData(JSONObject jsonObject,String arrayTitle)
    {
        String rankDayList=jsonObject.optString(arrayTitle);
        if(rankDayList!=null && rankDayList.length()>0)
        {
            rankRichManBeans.add(GsonUtil.getObjects(rankDayList,RankItemBean[].class));
        }
        else
        {
            rankRichManBeans.add(new ArrayList<>());
        }
    }
}
