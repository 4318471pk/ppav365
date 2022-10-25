package com.live.fox.ui.rank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.adapter.ViewPagerFragmentAdapter;
import com.live.fox.base.BaseActivity;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.RankActivityBinding;
import com.live.fox.entity.Rank;
import com.live.fox.server.Api_Rank;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.tab.SimpleTabLayout;

import org.json.JSONObject;

import java.util.List;


/**
 * 排行榜
 */
public class RankActivity extends BaseBindingViewActivity {

    RankActivityBinding mBind;
    private int titles[]=new int[]{R.string.anchorBana,R.string.conBan};

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
        int widthScreen= ScreenUtils.getScreenWidth(this);
        mBind.tabLayout.setGradient(0xffA800FF,0xffEA00FF);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBind.viewPager.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getFragment(int position) {
                return RankFragment.newInstance(position);
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
    }
}
