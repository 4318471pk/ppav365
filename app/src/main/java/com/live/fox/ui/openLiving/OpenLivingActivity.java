package com.live.fox.ui.openLiving;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.databinding.ActivityOpenLivingBinding;
import com.live.fox.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class OpenLivingActivity extends BaseBindingViewActivity {


    ActivityOpenLivingBinding mBind;
    List<BaseBindingFragment> fragments=new ArrayList<>();

    public static void startActivity(Context context)
    {
        context.startActivity(new Intent(context,OpenLivingActivity.class));
    }

    @Override
    public boolean isHasHeader() {
        return false;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_open_living;
    }

    @Override
    public void initView() {
        mBind=getViewDataBinding();
        mBind.setClick(this);

        int paddingTop=StatusBarUtil.getStatusBarHeight(this);
        mBind.frameLayout.setPadding(0,paddingTop,0,0);
        setWindowsFlag();

        fragments.add(new PreparingLivingFragment());
        fragments.add(new StartLivingFragment());

        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        for (int i = 0; i <fragments.size() ; i++) {
            fragmentTransaction.add(R.id.frameLayout,fragments.get(i));
        }

        for (int i = 0; i <fragments.size() ; i++) {
            fragmentTransaction.hide(fragments.get(i));
        }
        fragmentTransaction.commitAllowingStateLoss();

        showPreParingFragment();
    }

    private void showPreParingFragment()
    {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(fragments.get(0));
        fragmentTransaction.hide(fragments.get(1));
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void showStartLiving()
    {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments.get(0));
        fragmentTransaction.show(fragments.get(1));
        fragmentTransaction.commitAllowingStateLoss();
    }


}
