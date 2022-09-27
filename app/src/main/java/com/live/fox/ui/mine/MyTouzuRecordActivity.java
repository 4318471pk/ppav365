package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.viewpager.widget.ViewPager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.ViewPagerFragmentAdapter;
import com.live.fox.base.BaseFragment;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.ui.mine.gamerecord.TouzuFragment;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.view.tab.SimpleTabLayout;


public class MyTouzuRecordActivity extends BaseHeadActivity {

    private SimpleTabLayout tabLayout;
    private ViewPager viewPager;

    ViewPagerFragmentAdapter<BaseFragment> adapter;

    BaseFragment[] fragments = new BaseFragment[4];
    private long uid;
    private String lotteryName;
    private int type;

    public static void startActivity(Context context, long uid, String lotteryName, int type) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MyTouzuRecordActivity.class);
        i.putExtra("uid", uid);
        i.putExtra("lotteryName", lotteryName);
        i.putExtra("type", type);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myanclist_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        Intent intent = getIntent();
        if (intent != null) {
            uid = intent.getLongExtra("uid", 0);
            lotteryName = intent.getStringExtra("lotteryName");
            type = intent.getIntExtra("type", 0);
        }
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.tzjl), true, true);

        tabLayout = findViewById(R.id.tabLayout_);
        viewPager = findViewById(R.id.viewpager_);

        fragments[0] = TouzuFragment.newInstance(0, uid, lotteryName, type);
        fragments[1] = TouzuFragment.newInstance(1, uid, lotteryName, type);
        fragments[2] = TouzuFragment.newInstance(2, uid, lotteryName, type);
        fragments[3] = TouzuFragment.newInstance(3, uid, lotteryName, type);
        adapter = new ViewPagerFragmentAdapter<>(getSupportFragmentManager());
        //遍历栏目列表 设置Fragment
        String[] titles = {getString(R.string.transaction_income_all), getString(R.string.weikaijiang), getString(R.string.shibai), getString(R.string.zhongjiang)};
        for (int i = 0; i < titles.length; i++) {
            adapter.addFragment(fragments[i], titles[i]);
        }
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }
}
