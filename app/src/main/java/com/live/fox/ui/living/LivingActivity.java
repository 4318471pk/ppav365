package com.live.fox.ui.living;

import android.view.LayoutInflater;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.live.fox.R;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityLivingBinding;

public class LivingActivity extends BaseBindingViewActivity {

    ActivityLivingBinding mBind;

    @Override
    public boolean isHasHeader() {
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
        mBind=getViewDataBinding();

        mBind.drawerLayout.setScrimColor(0x00000000);
        mBind.drawerLayout.requestFocus();
        mBind.drawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ViewPager2 vp2=findViewById(R.id.vp2);
        vp2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        LayoutInflater layoutInflater=getLayoutInflater();
        vp2.setOffscreenPageLimit(1);


        int kll[]=new int[]{1,2,3,4,5};

        vp2.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public Fragment createFragment(int position) {
                int realPoi=(position-(Integer.MAX_VALUE/2))%kll.length;
                if(realPoi<0)
                {
                    realPoi=realPoi+kll.length;
                }
                return LivingFragment.getInstance(realPoi);
            }

            @Override
            public int getItemCount() {
                return Integer.MAX_VALUE;
            }
        });



        vp2.setCurrentItem(Integer.MAX_VALUE/2,false);
    }

    public DrawerLayout getDrawLayout()
    {
        return mBind.drawerLayout;
    }
}
