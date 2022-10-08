package com.live.fox.ui.mine.diamondIncomeAndExpenses;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityDiamondIncomeExpensesBinding;
import com.live.fox.ui.rank.RankFragment;
import com.live.fox.utils.device.ScreenUtils;

import java.util.List;

public class DiamondIncomeAndExpensesActivity extends BaseBindingViewActivity {

    ActivityDiamondIncomeExpensesBinding mBind;
    private int titles[]=new int[]{R.string.diamondIncome,R.string.diamondExpense};

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,DiamondIncomeAndExpensesActivity.class));
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_diamond_income_expenses;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setActivityTitle(R.string.diamond_income_expense);

        int widthScreen= ScreenUtils.getScreenWidth(this);

        mBind.vpMain.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getFragment(int position) {
                switch (position)
                {
                    case 0:
                        return new DiamondExpensesFragment();
                    case 1:
                        return new DiamondIncomeFragment();
                }
                return null;
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
        mBind.tabLayout.setTabWidthPX(widthScreen/2);
        mBind.tabLayout.setViewPager(mBind.vpMain);
        mBind.vpMain.setOffscreenPageLimit(2);
        mBind.tabLayout.setGradient(0xffA800FF,0xffEA00FF);
    }
}
