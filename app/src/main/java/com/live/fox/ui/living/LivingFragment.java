package com.live.fox.ui.living;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.effective.android.panel.PanelSwitchHelper;
import com.effective.android.panel.interfaces.listener.OnKeyboardStateListener;
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener;
import com.effective.android.panel.view.panel.IPanelView;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.FragmentLivingBinding;
import com.live.fox.dialog.FirstTimeTopUpDialog;
import com.live.fox.dialog.PleaseDontLeaveDialog;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.MyFlowLayout;

import static android.view.View.OVER_SCROLL_NEVER;

public class LivingFragment extends BaseBindingFragment {

    int position;
    FragmentLivingBinding mBind;
    LivingControlPanel livingControlPanel;
    private PanelSwitchHelper mHelper;

    public static LivingFragment getInstance(int position)
    {
        Log.e("LivingFragment",position+" ");
        LivingFragment livingFragment=new LivingFragment();
        livingFragment.position=position;
        return livingFragment;
    }

    public void notifyShow(int position)
    {
        Log.e("LivingFragment22",position+" ");
        if(getView()!=null && isAdded())
        {

        }
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
                    container.post(new Runnable() {
                        @Override
                        public void run() {
//                            initBotView();
                        }
                    });
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

}
