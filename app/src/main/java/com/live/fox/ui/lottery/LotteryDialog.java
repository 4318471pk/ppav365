package com.live.fox.ui.lottery;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.live.fox.R;
import com.live.fox.ui.lottery.adapter.LotteryNameAdapter;
import com.live.fox.ui.lottery.adapter.LotteryNameAdapter;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.BaseFragment;
import com.live.fox.databinding.DialogLotteryBinding;
import com.live.fox.ui.mine.RechargeActivity;

import java.util.ArrayList;
import java.util.List;

public class LotteryDialog extends BaseBindingDialogFragment {

    DialogLotteryBinding mBind;

    LotteryNameAdapter lotteryNameAdapter;
    List<Boolean> lotteryNameList = new ArrayList<>();

    List<BaseFragment> fragmentList = new ArrayList<>();


    @Override
    public void onClickView(View view) {
        if (view == mBind.rlMain){
            dismissAllowingStateLoss();
        } else if (view == mBind.tvCharge){
            RechargeActivity.startActivity(this.getContext());
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_lottery;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen_2);
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        test();
        for(int i=0 ; i< lotteryNameList.size(); i++) {
            fragmentList.add(LotteryItemListFragment.newInstance());
        }

        lotteryNameAdapter = new LotteryNameAdapter(lotteryNameList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBind.rcLotteryName.setLayoutManager(layoutManager);
        mBind.rcLotteryName.setAdapter(lotteryNameAdapter);

        lotteryNameAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                changeLotteryHead(position);
            }
        });

        mBind.vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        mBind.vp.setCurrentItem(0);
        mBind.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeLotteryHead(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void changeLotteryHead(int position){
        for (int i = 0; i < lotteryNameList.size(); i ++) {
            if (lotteryNameList.get(i)) {
                if (i == position) {
                    return;
                } else {
                    lotteryNameList.set(i, false);
                    break;
                }
            }
        }
        lotteryNameList.set(position, true);
        lotteryNameAdapter.notifyDataSetChanged();
    }

    private void test(){
        lotteryNameList.add(true);
        lotteryNameList.add(false);
        lotteryNameList.add(false);
        lotteryNameList.add(false);
        lotteryNameList.add(false);

    }



}
