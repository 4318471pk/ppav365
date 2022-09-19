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
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
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
public class RankActivity extends BaseActivity {

    private SimpleTabLayout tabLayout;
    private ViewPager viewPager;
    private int titles[]=new int[]{R.string.anchorBana,R.string.conBan};

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,RankActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_activity);
        initView();
    }

    private void initView()
    {
        int widthScreen= ScreenUtils.getScreenWidth(this);
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewPager);
        tabLayout.setGradient(0xffA800FF,0xffEA00FF);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getFragment(int position) {
                return RankFragment.newInstance(position,position);
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


        tabLayout.setTabWidthPX((widthScreen-ScreenUtils.getDip2px(this,50))/2);
        tabLayout.setViewPager(viewPager);
    }
}
