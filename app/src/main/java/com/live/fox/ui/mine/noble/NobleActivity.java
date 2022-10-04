package com.live.fox.ui.mine.noble;

import static com.live.fox.R.layout.activity_noble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.VipInfo;
import com.live.fox.server.Api_Config;
import com.live.fox.utils.BarUtils;
import com.live.fox.view.tab.SimpleTabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 贵族
 * VIP 等级
 */
public class NobleActivity extends BaseActivity {

    public static final int NANJUE = 1;
    public static final int ZIJUE = 2;
    public static final int BOJUE = 3;
    public static final int HOUJUE = 4;
    public static final int GONGJUE = 5;
    public static final int QINWANG = 6;
    public static final int KING = 7;

    private ViewPager vp;
    //private TabLayout tabLayout;
    SimpleTabLayout tabLayout;
    private final List<NobleNewFragment> fragmentList = new ArrayList<>();
    List<VipInfo> vipInfoList;

    public static void startActivity(Activity activity) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(activity, NobleActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarAlpha(this);
        setContentView(activity_noble);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setGradient(0xffE2B361,0xffFFDFA9);

        View left = findViewById(R.id.iv_head_left);
        left.setOnClickListener(v -> finish());
        findViewById(R.id.iv_detail).setOnClickListener(v -> startActivity(new Intent(this, NobleDetailActivity.class)));

        Api_Config.ins().doVipInfo(new JsonCallback<List<VipInfo>>() {
            @Override
            public void onSuccess(int code, String msg, List<VipInfo> data) {
                if (code == Constant.Code.SUCCESS) {
                    NobleActivity.this.vipInfoList = data;
                    String[] titles = new String[7];
                    titles[0] = getString(R.string.nanjue);
                    titles[1] = getString(R.string.zijue);
                    titles[2] = getString(R.string.bojue);
                    titles[3] = getString(R.string.houjue);
                    titles[4] = getString(R.string.gongjue);
                    titles[5] = getString(R.string.qinwang);
                    titles[6] = getString(R.string.king);

                    vp = findViewById(R.id.vp);
                    vp.setOffscreenPageLimit(titles.length);
                    for (int i = Constant.LEVEL1; i <= Constant.LEVEL7; i++) {
                        NobleNewFragment nobleFragment = NobleNewFragment.newInstance(i);
                        fragmentList.add(nobleFragment);
                    }

                    vp.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            return fragmentList.get(position);
                        }

                        @Override
                        public int getCount() {
                            return fragmentList.size();
                        }

                        @Nullable
                        @Override
                        public CharSequence getPageTitle(int position) {
                            return titles[position];
                        }
                    });
                    tabLayout.setViewPager(vp);
                    //tabLayout.setupWithViewPager(vp);

                } else {
                    NobleActivity.this.showToastTip(false, msg);
                }
            }
        });

    }

}
