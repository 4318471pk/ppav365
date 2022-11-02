package com.live.fox.ui.openLiving;

import android.view.View;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.FragmentStartLivingBinding;
import com.live.fox.utils.CountTimerUtil;

public class StartLivingFragment extends BaseBindingFragment {

    FragmentStartLivingBinding mBind;

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_start_living;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        CountTimerUtil.start(mBind.tvNumberAnim);
    }
}
