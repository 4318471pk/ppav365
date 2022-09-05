package com.live.fox.ui.rank;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.Rank;
import com.live.fox.entity.RankListEntity;
import com.live.fox.helper.SimpleTabSelectedListener;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.utils.LruCacheUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Rank 排行榜内容
 * 排行内容列表，包括头部前三
 */
public class RankListFragment extends BaseFragment implements IRankView {

    public static final String RANK_TYPE_KEY = "rank type key";

    //Views
    private RecyclerView topThirdRecyclerView;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private int type;
    private String[] tabTitle;
    private RankPresenter rankPresenter;
    private List<RankContentFragment> rankContentFragments;
    private RankListEntity rankListEntity;
    private RankTop3Adapter topThirdAdapter;
    private List<Rank> rankList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_list, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {

        topThirdRecyclerView = view.findViewById(R.id.rank_list_top_3);
        viewPager = view.findViewById(R.id.rank_list_content_viewpager);
        tabLayout = view.findViewById(R.id.rank_list_content_tab_layout);
        topThirdRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        tabLayout.addOnTabSelectedListener(new SimpleTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTop3(tab.getPosition());
            }
        });
    }

    /**
     * 初始化数据
     * 设置TabTitle
     * 请求远程数据
     */
    private void initData() {
        if (getArguments() != null) {
            rankContentFragments = new ArrayList<>();
            rankPresenter = new RankPresenter(this);
            type = getArguments().getInt(RANK_TYPE_KEY);
            handleData(type);
            requestRemote();
        }
    }

    /**
     * 更具类型处理排行榜滑动数据
     * 设置tab的标题和创建对应的 fragment
     *
     * @param type 1 类别
     */
    private void handleData(int type) {
        switch (type) {
            case 1:
                tabTitle = new String[]{getString(R.string.erban), getString(R.string.yesBan), getString(R.string.weekBan), getString(R.string.yueban)};
                break;
            case 2:
                tabTitle = new String[]{getString(R.string.erban), getString(R.string.weekBan), getString(R.string.totalBan)};
                break;

            case 3:
            case 4:
                tabTitle = new String[]{getString(R.string.erban), getString(R.string.weekBan), getString(R.string.yueban)};
                break;
        }

        //创建和标题对应的 Fragment
        for (int i = 0; i < tabTitle.length; i++) {
            RankContentFragment contentFragment = new RankContentFragment();
            RankContentFragment.ParametersEntity parameters = new RankContentFragment.ParametersEntity();
            parameters.setRankType(type);
            parameters.setRankClass(i);
            Bundle args = new Bundle();
            args.putParcelable(RankContentFragment.PARAMETERS_KEY, parameters);
            contentFragment.setArguments(args);
            rankContentFragments.add(contentFragment);
        }
    }

    /**
     * 设置ViewPager
     */
    private void setViewPagerAndTop() {
        RankFragmentAdapter adapter = new RankFragmentAdapter(getChildFragmentManager());
        adapter.setFragments(rankContentFragments, Arrays.asList(tabTitle));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager); // 将tab layout 和 ViewPager 绑定
        tabLayout.setTabMode(TabLayout.MODE_FIXED);  //设置tab layout 滚动

        //设置顶部数据
        topThirdAdapter = new RankTop3Adapter();
        topThirdRecyclerView.setAdapter(topThirdAdapter);

        //看主播
        topThirdAdapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            Rank rank = rankList.get(i);
            goAnchorRoom(rank);
        });
    }

    /**
     * 去直播间
     *
     * @param rank 主播信息
     */
    private void goAnchorRoom(Rank rank) {
        Anchor anchor = new Anchor();
        anchor.setAnchorId(rank.getAnchorId());
        anchor.setNickname(rank.getNickname());
        anchor.setAvatar(rank.getAvatar());
        anchor.setSignature(rank.getSignature());
        anchor.setLiveId(rank.getLiveId());
        anchor.setType(rank.getType());
        anchor.setPrice(rank.getPrice());
        anchor.setPking(rank.isPking());
        anchor.setRq(rank.getRq());
        anchor.setToy(rank.getToy());
        LruCacheUtil.getInstance().put(anchor);
        LruCacheUtil.getInstance().get().clear();
        PlayLiveActivity.startActivity(requireActivity(), anchor);
    }

    /**
     * 请求远程数据
     */
    private void requestRemote() {
        rankPresenter.doRequestData(type);
        setViewPagerAndTop();
    }

    @Override
    public void error(String msg) {
        showToastTip(false, msg);
    }

    @Override
    public void requestResult(RankListEntity rankListEntity) {
        Log.d("RankListFragment", "Type = " + type + "  data:" + rankListEntity.toString());
        this.rankListEntity = rankListEntity;
        rankList = rankListEntity.getRankList1();
        handleTop3();
    }

    /**
     * 处理头部的数据
     */
    private void handleTop3() {
        topThirdAdapter.getData().clear();

        addFakeData();
        topThirdAdapter.addData(rankList);
    }

    /**
     * 假数据
     */
    private void addFakeData() {
        switch (rankList.size()) {
            case 0:
                makeFakeData(3);
                break;
            case 1:
                makeFakeData(2);
                break;
            case 2:
                makeFakeData(1);
                break;
        }
    }

    private void makeFakeData(int size) {
        for (int i = size; i > 0; i--) {
            Rank rank = new Rank();
            rank.setNickname("Fake User" + i);
            rankList.add(rank);
        }
    }

    private void updateTop3(int position) {
        Log.e("RankListFragment", "position: " + position + "");
        if (rankListEntity == null) {
            return;
        }

        switch (position) {
            case 0:
                if (rankListEntity.getRankList1() != null) {
                    rankList = rankListEntity.getRankList1();
                    handleTop3();
                }
                break;
            case 1:
                if (rankListEntity.getRankList2() != null) {
                    rankList = rankListEntity.getRankList2();
                    handleTop3();
                }
                break;
            case 2:
                if (rankListEntity.getRankList3() != null) {
                    rankList = rankListEntity.getRankList3();
                    handleTop3();
                }
                break;
            case 3:
                if (rankListEntity.getRankList4() != null) {
                    rankList = rankListEntity.getRankList4();
                    handleTop3();
                }
                break;
        }
    }
}
