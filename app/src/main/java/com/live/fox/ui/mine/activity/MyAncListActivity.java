package com.live.fox.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.viewpager.widget.ViewPager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.ViewPagerFragmentAdapter;
import com.live.fox.base.BaseFragment;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.ui.mine.activity.zbgl.ZbjlFragment;
import com.live.fox.ui.mine.activity.zbgl.ZblbFragment;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.view.tab.SimpleTabLayout;

public class MyAncListActivity extends BaseHeadActivity {

    private SimpleTabLayout tabLayout;
    private ViewPager viewPager;

    ViewPagerFragmentAdapter<BaseFragment> adapter;

    BaseFragment[] fragments = new BaseFragment[2];

    long uid;

    public static void startActivity(Context context, long uid) {
        Constant.isAppInsideClick=true;
        Intent i = new Intent(context, MyAncListActivity.class);
        i.putExtra("uid", uid);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myanclist_activity);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        if (getIntent() != null) {
            uid = getIntent().getLongExtra("uid", 0);
        }
        initView();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.anchorDetails), true, true);

        tabLayout = findViewById(R.id.tabLayout_);
        viewPager = findViewById(R.id.viewpager_);

        fragments[0] = ZblbFragment.newInstance(uid);
        fragments[1] = ZbjlFragment.newInstance(uid);
        adapter = new ViewPagerFragmentAdapter<>(getSupportFragmentManager());
        //遍历栏目列表 设置Fragment
        String[] titles = {"Danh sách IDOL", getString(R.string.liveRecord)};//Lịch sử live
        for (int i = 0; i < titles.length; i++) {
            adapter.addFragment(fragments[i], titles[i]);
        }
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
