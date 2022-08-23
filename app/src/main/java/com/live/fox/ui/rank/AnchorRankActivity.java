package com.live.fox.ui.rank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.ViewPagerFragmentAdapter;
import com.live.fox.base.BaseFragment;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.AnchorRank;
import com.live.fox.server.Api_Rank;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.view.tab.SimpleTabLayout;


/**
 * 主播个人榜
 */
public class AnchorRankActivity extends BaseHeadActivity {

    private SimpleTabLayout tabLayout;
    private ViewPager viewPager;

    ViewPagerFragmentAdapter<BaseFragment> adapter;

    BaseFragment[] fragments = new BaseFragment[2];

    AnchorRankFragment anchorRankFragment1 = AnchorRankFragment.newInstance(1);
    AnchorRankFragment anchorRankFragment2 = AnchorRankFragment.newInstance(2);

    long anchorId = 0;

    public static void startActivity(Context context, long anchorId) {
        Constant.isAppInsideClick = true;
        Intent i = new Intent(context, AnchorRankActivity.class);
        i.putExtra("anchorId", anchorId);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anchorrank_activity);

        anchorId = getIntent().getLongExtra("anchorId", 0);

        initView();
        doGetRankListApi();
    }

    private void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.anchorBan), true, true);

        tabLayout = findViewById(R.id.tabLayout_);
        viewPager = findViewById(R.id.viewpager_);

        fragments[0] = anchorRankFragment1;
        fragments[1] = anchorRankFragment2;

        adapter = new ViewPagerFragmentAdapter<>(getSupportFragmentManager());
        //遍历栏目列表 设置Fragment
        String[] titles = {getString(R.string.erban), getString(R.string.yueban)};
        for (int i = 0; i < titles.length; i++) {
            adapter.addFragment(fragments[i], titles[i]);
        }
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }

    private void doGetRankListApi() {
        Api_Rank.ins().getRankList(anchorId, new JsonCallback<AnchorRank>() {
            @Override
            public void onSuccess(int code, String msg, AnchorRank data) {
                if (code == 0 && data != null) {
                    if (data.getDayList() != null && data.getDayList().size() > 0) {
                        anchorRankFragment1.refreshPage(data.getDayList());
                    }
                    if (data.getAllList() != null && data.getAllList().size() > 0) {
                        anchorRankFragment2.refreshPage(data.getAllList());
                    }
                } else {
                    showToastTip(false, msg);
                }
            }
        });
    }
}
