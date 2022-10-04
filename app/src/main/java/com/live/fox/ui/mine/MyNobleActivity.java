package com.live.fox.ui.mine;

import android.view.View;

import com.live.fox.R;
import com.live.fox.adapter.HomeFragmentPagerAdapter;
import com.live.fox.adapter.MyNoblePagerAdapter;
import com.live.fox.base.BaseBindingViewActivity;
import com.live.fox.base.BaseFragment;
import com.live.fox.databinding.ActivityMyNobleBinding;

import java.util.ArrayList;
import java.util.List;

/*
* 贵族页面
* */
public class MyNobleActivity extends BaseBindingViewActivity {

    ActivityMyNobleBinding mBind;

    MyNoblePagerAdapter<BaseFragment> adapter;

    @Override
    public int onCreateLayoutId() {
        return R.layout.activity_my_noble;
    }

    @Override
    public void initView() {
        mBind = getViewDataBinding();
        mBind.tabLayout.setGradient(0xffE2B361,0xffFFDFA9);
        List<String> mTitles = new ArrayList<>();
        mTitles.add(getString(R.string.nanjue));
        mTitles.add(getString(R.string.zijue));
        mTitles.add(getString(R.string.bojue));
        mTitles.add(getString(R.string.houjue));
        mTitles.add(getString(R.string.gongjue));
        mTitles.add(getString(R.string.qinwang));
        mTitles.add(getString(R.string.king));
        mBind.viewpager.setOffscreenPageLimit(1);


        adapter = new MyNoblePagerAdapter(getSupportFragmentManager(),mTitles);
        //遍历栏目列表 设置Fragment

//                if(data!=null && data.size()>5)
//                {
        mBind.viewpager.setOffscreenPageLimit(1);
//                }
        mBind.viewpager.setAdapter(adapter);
        mBind.tabLayout.setViewPager(mBind.viewpager);



    }

    @Override
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_head_left:
                finish();
                break;

        }
    }
}
