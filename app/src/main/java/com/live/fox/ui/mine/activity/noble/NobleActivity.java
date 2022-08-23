package com.live.fox.ui.mine.activity.noble;

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

import java.util.ArrayList;
import java.util.List;


/**
 * 贵族
 * VIP 等级
 */
public class NobleActivity extends BaseActivity {

    private ViewPager vp;
    private TabLayout tabLayout;
    private final List<NobleFragment> fragmentList = new ArrayList<>();
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
        TextView tvTitle = findViewById(R.id.tv_head_title);
        tvTitle.setText(getString(R.string.becomeNobel));
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        View left = findViewById(R.id.iv_head_left);
        left.setVisibility(View.VISIBLE);
        left.setOnClickListener(v -> finish());

        Api_Config.ins().doVipInfo(new JsonCallback<List<VipInfo>>() {
            @Override
            public void onSuccess(int code, String msg, List<VipInfo> data) {
                if (code == Constant.Code.SUCCESS) {
                    NobleActivity.this.vipInfoList = data;
                    String[] titles = new String[5];
                    titles[0] = getString(R.string.grade_gold);
                    titles[1] = getString(R.string.grade_platinum);
                    titles[2] = getString(R.string.grade_diamond);
                    titles[3] = getString(R.string.grade_master);
                    titles[4] = getString(R.string.grade_king);

                    vp = findViewById(R.id.vp);

                    for (int i = Constant.LEVEL1; i <= Constant.LEVEL5; i++) {
                        NobleFragment nobleFragment = NobleFragment.newInstance(i);
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
                    tabLayout.setupWithViewPager(vp);

                } else {
                    NobleActivity.this.showToastTip(false, msg);
                }
            }
        });

    }

}
