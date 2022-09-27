package com.live.fox.ui.mine.moneyout;

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
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.view.tab.SimpleTabLayout;


/**
 * 提现、兑换金币 记录
 */
public class MoneyOutRecordActivity extends BaseHeadActivity {

    private SimpleTabLayout tabLayout;
    private ViewPager viewPager;

    ViewPagerFragmentAdapter<BaseFragment> adapter;


    BaseFragment fragments[] = new BaseFragment[2];


    public static void startActivity(Context context) {
        Constant.isAppInsideClick=true;
        Intent i = new Intent(context, MoneyOutRecordActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moneyoutrecord_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.souyijixian), true, true);

        setTitle(getString(R.string.withdrawals_record));

        tabLayout = findViewById(R.id.tabLayout_);
        viewPager = findViewById(R.id.viewpager_);

        fragments[0] = WithdrawalsRecordFragment.newInstance(3);
        fragments[1] = MoneyoutRecord2Fragment.newInstance(3);


        adapter = new ViewPagerFragmentAdapter<>(getSupportFragmentManager());
        //遍历栏目列表 设置Fragment
        String titles[] = {getString(R.string.xianjintixiazn), getString(R.string.duihuangjinbi)};
        for (int i = 0; i < titles.length; i++) {
            adapter.addFragment(fragments[i], titles[i]);
        }

        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }
}
