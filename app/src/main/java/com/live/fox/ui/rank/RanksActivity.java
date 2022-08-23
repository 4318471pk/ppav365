package com.live.fox.ui.rank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.utils.BarUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 排行榜
 * 这是改版之后的，从原来的上下滑动，变成左右滑动
 * 竖排展示
 */
public class RanksActivity extends BaseActivity implements View.OnClickListener {

    //Views
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static void launch(Context context) {
        Constant.isAppInsideClick=true;
        Intent intent = new Intent(context, RanksActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranks);

        BarUtils.setStatusBarAlpha(this, 0);

        initView();

        initData();
    }

    private void initView() {
        findViewById(R.id.rank_toolbar_back).setOnClickListener(this);
        tabLayout = findViewById(R.id.rank_toolbar_tab);
        viewPager = findViewById(R.id.rank_content_view_pager);
    }

    /**
     * Init Data
     * base rank tabs create rank list fragment
     */
    private void initData() {
        List<RankListFragment> rankListFragments = new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.rank_tab);
        for (int i = 0; i < stringArray.length; i++) {
            RankListFragment fragment = new RankListFragment();
            Bundle args = new Bundle();
            args.putInt(RankListFragment.RANK_TYPE_KEY, i + 1);
            fragment.setArguments(args);
            rankListFragments.add(fragment);
        }

        //设置 ViewPager
        viewPager.setOffscreenPageLimit(stringArray.length);
        RankFragmentAdapter fragmentAdapter = new RankFragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.setFragments(rankListFragments, Arrays.asList(stringArray));
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.rank_toolbar_back) {
            finish();
        }
    }
}