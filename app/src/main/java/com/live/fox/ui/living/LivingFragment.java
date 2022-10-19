package com.live.fox.ui.living;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.effective.android.panel.PanelSwitchHelper;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentLivingBinding;

import static android.view.View.OVER_SCROLL_NEVER;

public class LivingFragment extends BaseBindingFragment {

    int position;
    FragmentLivingBinding mBind;
    LivingControlPanel livingControlPanel;
    private PanelSwitchHelper mHelper;

    public static LivingFragment getInstance(int position)
    {
        LivingFragment livingFragment=new LivingFragment();
        livingFragment.position=position;
        return livingFragment;
    }
    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_living;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        initView();

    }


    private void initView()
    {
        livingControlPanel= new LivingControlPanel(LivingFragment.this,mBind.viewPager);

        mBind.viewPager.setOverScrollMode(OVER_SCROLL_NEVER);
        mBind.viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view.equals(object);
            }

            public Object instantiateItem(ViewGroup container, int position) {

                if (position == 1) {
                    container.addView(livingControlPanel);
                    return livingControlPanel;
                }
                return null;
            }
        });

        LivingActivity activity=(LivingActivity) getActivity();
        mBind.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position>0)
                {
                    activity.getDrawLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
                }
                else
                {
                    activity.getDrawLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBind.viewPager.setCurrentItem(1);
    }

    @Override
    public void onStart() {
        super.onStart();

//        if(mHelper==null)
//        {
//            mHelper=new PanelSwitchHelper.Builder(getActivity())
//                    .
//        }
    }
}
