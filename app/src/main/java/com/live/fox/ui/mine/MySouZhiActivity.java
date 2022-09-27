package com.live.fox.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.ViewPagerFragmentAdapter;
import com.live.fox.base.BaseFragment;
import com.live.fox.ui.mine.gamerecord.SXDialogFragment;
import com.live.fox.ui.mine.gamerecord.XiaofeiFragment;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.view.tab.SimpleTabLayout;


public class MySouZhiActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout re_root;
    private SimpleTabLayout tabLayout;
    private ViewPager viewpager;
    ViewPagerFragmentAdapter<BaseFragment> adapter;
    BaseFragment[] fragments = new BaseFragment[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sou_zhi);
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setTopPaddingStatusBarHeight(re_root); //124578
        initView();
        fragments[0] = XiaofeiFragment.newInstance();
        fragments[1] = XiaofeiFragment.newInstance();
        adapter = new ViewPagerFragmentAdapter<>(getSupportFragmentManager());
        //遍历栏目列表 设置Fragment
        String[] titles = {"Chi tiêu", "Thu nhập"};
        for (int i = 0; i < titles.length; i++) {
            adapter.addFragment(fragments[i], titles[i]);
        }
        viewpager.setAdapter(adapter);
        tabLayout.setViewPager(viewpager);
    }

    private void initView() {
        re_root = findViewById(R.id.re_root);
        tabLayout = findViewById(R.id.tabLayout_);
        viewpager = findViewById(R.id.viewpager_);
        findViewById(R.id.iv_head_left).setOnClickListener(this);
        findViewById(R.id.iv_head_right).setOnClickListener(this);
    }

    public void setTopPaddingStatusBarHeight(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height += BarUtils.getStatusBarHeight();
        view.setLayoutParams(layoutParams);
        view.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
    }

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, MySouZhiActivity.class);
        context.startActivity(i);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head_left:
                Constant.isAppInsideClick = true;
                finish();
                break;
            case R.id.iv_head_right:
                SXDialogFragment sxDialogFragment = SXDialogFragment.newInstance();
                if (!sxDialogFragment.isAdded()) {
                    sxDialogFragment.show(getSupportFragmentManager(), SXDialogFragment.class.getSimpleName());
                }
                break;
        }
    }
}