package com.live.fox.ui.rank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.adapter.ViewPagerFragmentAdapter;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Rank;
import com.live.fox.server.Api_Rank;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GsonUtil;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.StringUtils;
import com.live.fox.view.tab.SimpleTabLayout;

import org.json.JSONObject;

import java.util.List;


/**
 * 排行榜
 */
public class RankActivity extends BaseHeadActivity {

    private SimpleTabLayout tabLayout1;
    private ViewPager viewpaget1;
    private SimpleTabLayout tabLayout2;
    private ViewPager viewpaget2;
    private SimpleTabLayout tabLayout3;
    private ViewPager viewpaget3;
    private SimpleTabLayout tabLayout4;
    private ViewPager viewpaget4;

    RankFragment rankFragmentr1;
    RankFragment rankFragmentr2;
    RankFragment rankFragmentr3;
    RankFragment rankFragmentr4;

    RankFragment rankFragmentr11;
    RankFragment rankFragmentr12;
    RankFragment rankFragmentr13;

    RankFragment rankFragmentr21;
    RankFragment rankFragmentr22;
    RankFragment rankFragmentr23;

    RankFragment rankFragmentr31;
    RankFragment rankFragmentr32;
    RankFragment rankFragmentr33;

    RankFragment rankFragment1[] = {rankFragmentr1 = RankFragment.newInstance(1, 1),
            rankFragmentr2 = RankFragment.newInstance(1, 4),
            rankFragmentr3 = RankFragment.newInstance(1, 2),
            rankFragmentr4 = RankFragment.newInstance(1, 3)};

    RankFragment rankFragment2[] = {rankFragmentr11 = RankFragment.newInstance(2, 1),
            rankFragmentr12 = RankFragment.newInstance(2, 2),
            rankFragmentr13 = RankFragment.newInstance(2, 3)};

    RankFragment rankFragment3[] = {rankFragmentr21 = RankFragment.newInstance(3, 1),
            rankFragmentr22 = RankFragment.newInstance(3, 2),
            rankFragmentr23 = RankFragment.newInstance(3, 3)};

    RankFragment rankFragment4[] = {rankFragmentr31 = RankFragment.newInstance(4, 1),
            rankFragmentr32 = RankFragment.newInstance(4, 2),
            rankFragmentr33 = RankFragment.newInstance(4, 3)};

    ViewPagerFragmentAdapter adapter1;
    ViewPagerFragmentAdapter adapter2;
    ViewPagerFragmentAdapter adapter3;
    ViewPagerFragmentAdapter adapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_activity);
        initView();
    }


    public void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.rankBan), true, true);

        tabLayout1 = findViewById(R.id.tabLayout_1);
        viewpaget1 = findViewById(R.id.viewpaget_1);
        tabLayout2 = findViewById(R.id.tabLayout_2);
        viewpaget2 = findViewById(R.id.viewpaget_2);
        tabLayout3 = findViewById(R.id.tabLayout_3);
        viewpaget3 = findViewById(R.id.viewpaget_3);
        tabLayout4 = findViewById(R.id.tabLayout_4);
        viewpaget4 = findViewById(R.id.viewpaget_4);

        String title1[] = {getString(R.string.erban), getString(R.string.yesBan), getString(R.string.weekBan), getString(R.string.yueban)};
        String title2[] = {getString(R.string.erban), getString(R.string.weekBan), getString(R.string.totalBan)};
        String title3[] = {getString(R.string.erban), getString(R.string.weekBan), getString(R.string.yueban)};
        String title4[] = {getString(R.string.erban), getString(R.string.weekBan), getString(R.string.yueban)};
        adapter1 = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        for (int i = 0; i < title1.length; i++) {
            adapter1.addFragment(rankFragment1[i], title1[i]);
        }
        viewpaget1.setAdapter(adapter1);
        tabLayout1.setViewPager(viewpaget1);

        adapter2 = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        for (int i = 0; i < title2.length; i++) {
            adapter2.addFragment(rankFragment2[i], title2[i]);
        }
        viewpaget2.setAdapter(adapter2);
        tabLayout2.setViewPager(viewpaget2);

        adapter3 = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        for (int i = 0; i < title3.length; i++) {
            adapter3.addFragment(rankFragment3[i], title3[i]);
        }
        viewpaget3.setAdapter(adapter3);
        tabLayout3.setViewPager(viewpaget3);

        adapter4 = new ViewPagerFragmentAdapter(getSupportFragmentManager());
        for (int i = 0; i < title4.length; i++) {
            adapter4.addFragment(rankFragment4[i], title4[i]);
        }
        viewpaget4.setAdapter(adapter4);
        tabLayout4.setViewPager(viewpaget4);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetRankApi();
    }

    private void doGetRankApi() {
        Api_Rank.ins().getRankList(1, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (data != null) LogUtils.e(data);
                if (code == 0) {
                    try {
                        List<Rank> rankList1 = GsonUtil.getObjects(new JSONObject(data).opt("rankList1").toString(), Rank[].class);
                        List<Rank> rankList2 = GsonUtil.getObjects(new JSONObject(data).opt("rankList2").toString(), Rank[].class);
                        List<Rank> rankList3 = GsonUtil.getObjects(new JSONObject(data).opt("rankList3").toString(), Rank[].class);
                        List<Rank> rankList4 = GsonUtil.getObjects(new JSONObject(data).opt("rankList4").toString(), Rank[].class);
                        rankFragmentr1.refreshPage(rankList1);
                        rankFragmentr2.refreshPage(rankList4);
                        rankFragmentr3.refreshPage(rankList2);
                        rankFragmentr4.refreshPage(rankList3);
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                        showToastTip(false, getString(R.string.jiexiWrong));
                    }

                } else {
                    if (StringUtils.isEmpty(msg)) msg = " ";
                    showToastTip(false, msg);
                }
            }
        });

        Api_Rank.ins().getRankList(2, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    try {
                        List<Rank> rankList1 = GsonUtil.getObjects(new JSONObject(data).opt("rankList1").toString(), Rank[].class);
                        List<Rank> rankList2 = GsonUtil.getObjects(new JSONObject(data).opt("rankList2").toString(), Rank[].class);
                        List<Rank> rankList3 = GsonUtil.getObjects(new JSONObject(data).opt("rankList3").toString(), Rank[].class);
                        rankFragmentr11.refreshPage(rankList1);
                        rankFragmentr12.refreshPage(rankList2);
                        rankFragmentr13.refreshPage(rankList3);
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                        showToastTip(false, getString(R.string.jiexiWrong));
                    }
                } else {
                    showToastTip(false, msg);
                }
            }
        });

        Api_Rank.ins().getRankList(3, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    try {
                        List<Rank> rankList1 = GsonUtil.getObjects(new JSONObject(data).opt("rankList1").toString(), Rank[].class);
                        List<Rank> rankList2 = GsonUtil.getObjects(new JSONObject(data).opt("rankList2").toString(), Rank[].class);
                        List<Rank> rankList3 = GsonUtil.getObjects(new JSONObject(data).opt("rankList3").toString(), Rank[].class);
                        rankFragmentr21.refreshPage(rankList1);
                        rankFragmentr22.refreshPage(rankList2);
                        rankFragmentr23.refreshPage(rankList3);
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                        showToastTip(false, getString(R.string.jiexiWrong));
                    }
                } else {
                    showToastTip(false, msg);
                }
            }
        });

        Api_Rank.ins().getRankList(4, new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if (code == 0) {
                    try {
                        List<Rank> rankList1 = GsonUtil.getObjects(new JSONObject(data).opt("rankList1").toString(), Rank[].class);
                        List<Rank> rankList2 = GsonUtil.getObjects(new JSONObject(data).opt("rankList2").toString(), Rank[].class);
                        List<Rank> rankList3 = GsonUtil.getObjects(new JSONObject(data).opt("rankList3").toString(), Rank[].class);
                        rankFragmentr31.refreshPage(rankList1);
                        rankFragmentr32.refreshPage(rankList2);
                        rankFragmentr33.refreshPage(rankList3);
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                        showToastTip(false, getString(R.string.jiexiWrong));
                    }
                } else {
                    showToastTip(false, msg);
                }
            }
        });

    }

    public static void startActivity(Context context) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, RankActivity.class);
        context.startActivity(intent);
    }

}
