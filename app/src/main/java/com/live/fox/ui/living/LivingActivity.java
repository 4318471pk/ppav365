package com.live.fox.ui.living;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.live.fox.R;
import com.live.fox.adapter.LivingFragmentStateAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityLivingBinding;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class LivingActivity extends BaseBindingViewActivity {

    ActivityLivingBinding mBind;
    LivingFragmentStateAdapter livingFragmentStateAdapter;

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,LivingActivity.class));
    }

    @Override
    public boolean isHasHeader() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return true;
    }

    @Override
    public void onClickView(View view) {

    }


    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_living;
    }

    @Override
    public void initView() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mBind=getViewDataBinding();
        mBind.drawerLayout.setScrimColor(0x00000000);

        mBind.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                mBind.drawerLayout.requestFocus();
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mBind.drawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        StatusBarUtil.setStatusBarAlpha(this,0,mBind.rlMain);

        mBind.vp2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        mBind.vp2.setOffscreenPageLimit(1);

        List<String> strings=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add(i+" ");
        }
        livingFragmentStateAdapter=new LivingFragmentStateAdapter(this,strings);
        mBind.vp2.setAdapter(livingFragmentStateAdapter);
        mBind.vp2.setCurrentItem(Integer.MAX_VALUE/2,false);
        mBind.vp2.setOffscreenPageLimit(2);
        mBind.vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                livingFragmentStateAdapter.getFragment(position)
                        .notifyShow(livingFragmentStateAdapter.getRealPosition(position));

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

//        mBind.vp2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                int position=mBind.vp2.getCurrentItem();
//                updatePagerHeightForChild(livingFragmentStateAdapter.getFragment(position).getView(),mBind.vp2);
//            }
//        });
    }

    private void updatePagerHeightForChild(View view, ViewPager2 pager) {
        view.post(() -> {
            int wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
            int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(wMeasureSpec, hMeasureSpec);
            if (pager.getLayoutParams().height != view.getMeasuredHeight()) {
                ViewGroup.LayoutParams layoutParams = pager.getLayoutParams();
                layoutParams.height = view.getMeasuredHeight();
                pager.setLayoutParams(layoutParams);
            }
        });
    }


    public DrawerLayout getDrawLayout()
    {
        return mBind.drawerLayout;
    }

    public void setUserScrollAvailAble(boolean isAvailable)
    {
        mBind.vp2.setUserInputEnabled(isAvailable);
    }

}
