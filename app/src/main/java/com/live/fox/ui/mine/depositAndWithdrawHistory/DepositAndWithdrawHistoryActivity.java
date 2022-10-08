package com.live.fox.ui.mine.depositAndWithdrawHistory;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityDepositWithdrawHistoryBinding;
import com.live.fox.ui.mine.diamondIncomeAndExpenses.DiamondExpensesFragment;
import com.live.fox.ui.mine.diamondIncomeAndExpenses.DiamondIncomeAndExpensesActivity;
import com.live.fox.ui.mine.diamondIncomeAndExpenses.DiamondIncomeFragment;
import com.live.fox.utils.OnClickFrequentlyListener;
import com.live.fox.utils.device.ScreenUtils;

public class DepositAndWithdrawHistoryActivity extends BaseBindingViewActivity {

    ActivityDepositWithdrawHistoryBinding mBind;

    private int titles[]=new int[]{R.string.depositHistory,R.string.withdrawHistory};

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context, DepositAndWithdrawHistoryActivity.class));
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_deposit_withdraw_history;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        setActivityTitle(R.string.recharge_record);
        setServiceImage(new OnClickFrequentlyListener() {
            @Override
            public void onClickView(View view) {

            }
        });

        int widthScreen= ScreenUtils.getScreenWidth(this);

        mBind.vpMain.setAdapter(new BaseFragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getFragment(int position) {
                switch (position)
                {
                    case 0:
                        return new DepositHistoryFragment();
                    case 1:
                        return new WithdrawHistoryFragment();
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
