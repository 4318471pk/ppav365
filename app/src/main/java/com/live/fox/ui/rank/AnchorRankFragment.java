package com.live.fox.ui.rank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.entity.Rank;
import com.live.fox.ui.mine.editprofile.UserDetailActivity;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.ViewUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class AnchorRankFragment extends BaseFragment {

    private RoundedImageView ivRank2;
    private LinearLayout llRank2;
    private RoundedImageView ivRank1;
    private LinearLayout llRank1;
    private RoundedImageView ivRank3;
    private LinearLayout llRank3;
    private RecyclerView rv;
    private SmartRefreshLayout refreshLayout;
    private RelativeLayout bottomRankLayout;
    private TextView tvName2;
    private TextView tvValue2;
    private TextView tvName1;
    private TextView tvValue1;
    private TextView tvName3;
    private TextView tvValue3;

    int pageType = 1;
    BaseQuickAdapter adapter;

    public static AnchorRankFragment newInstance(int pageType) {
        AnchorRankFragment fragment = new AnchorRankFragment();
        Bundle args = new Bundle();
        args.putInt("pageType", pageType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.anchorrank_fragment, container, false);
        initData(getArguments());
        setView(rootView);
        return rootView;
    }

    public void initData(Bundle bundle) {
        if (bundle != null) {
            pageType = bundle.getInt("pageType");
        }
    }


    public void setView(View bindSource) {
        ivRank2 = bindSource.findViewById(R.id.iv_rank2);
        llRank2 = bindSource.findViewById(R.id.ll_rank2);
        ivRank1 = bindSource.findViewById(R.id.iv_rank1);
        llRank1 = bindSource.findViewById(R.id.ll_rank1);
        ivRank3 = bindSource.findViewById(R.id.iv_rank3);
        llRank3 = bindSource.findViewById(R.id.ll_rank3);
        rv = bindSource.findViewById(R.id.rv_);
        refreshLayout = bindSource.findViewById(R.id.refreshLayout);
        bottomRankLayout = bindSource.findViewById(R.id.layout_bottomrank);
        tvName2 = bindSource.findViewById(R.id.tv_name2);
        tvValue2 = bindSource.findViewById(R.id.tv_value2);
        tvName1 = bindSource.findViewById(R.id.tv_name1);
        tvValue1 = bindSource.findViewById(R.id.tv_value1);
        tvName3 = bindSource.findViewById(R.id.tv_name3);
        tvValue3 = bindSource.findViewById(R.id.tv_value3);
        bottomRankLayout.setVisibility(View.INVISIBLE);
        llRank1.setVisibility(View.INVISIBLE);
        llRank2.setVisibility(View.INVISIBLE);
        llRank3.setVisibility(View.INVISIBLE);
        setRecycleView();
    }

    public void setRecycleView() {
        refreshLayout.setEnablePureScrollMode(true);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_rankdetaila, new ArrayList<Rank>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                Rank rank = (Rank) item;
                if (rank.getRankHidden() == 0) {
                    helper.setText(R.id.tv_name, rank.getNickname());

                    GlideUtils.loadDefaultCircleImage(getActivity(), rank.getAvatar(), helper.getView(R.id.iv_head));
                } else {
                    helper.setText(R.id.tv_name, getString(R.string.mysteriousMan));

                    helper.setImageResource(R.id.iv_head, R.drawable.ic_shenmi);
                }
                helper.setText(R.id.tv_ranknum, (helper.getLayoutPosition() + 4) + "");
                helper.setText(R.id.tv_sendnum, RegexUtils.westMoney(rank.getRankValue()) + "");
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Rank rank = (Rank) adapter.getData().get(position);
                if (rank.getRankHidden() == 0)
                    UserDetailActivity.startActivity(getActivity(), rank.getUid());

            }
        });

    }

    public void refreshPage(List<Rank> data) {
        if (data == null && data.size() == 0) return;
        LogUtils.e(data.size() + " ,,,");

        llRank1.setVisibility(View.INVISIBLE);
        llRank2.setVisibility(View.INVISIBLE);
        llRank3.setVisibility(View.INVISIBLE);

        List<Rank> tempData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Rank rank = data.get(i);
            switch (i) {
                case 0:
                    llRank1.setVisibility(View.VISIBLE);
                    tvValue1.setText(RegexUtils.formatNumber(rank.getRankValue()));

                    if (rank.getRankHidden() == 0) {
                        llRank1.setOnClickListener(v -> UserDetailActivity.startActivity(getActivity(), rank.getUid()));
                        tvName1.setText(rank.getNickname());
                        GlideUtils.loadDefaultCircleImage(getActivity(), rank.getAvatar(), ivRank1);

                    } else {
                        tvName1.setText(getString(R.string.mysteriousMan));
                        ivRank1.setImageResource(R.drawable.ic_shenmi);

                    }

                    break;
                case 1:
                    llRank2.setVisibility(View.VISIBLE);
                    tvValue2.setText(RegexUtils.formatNumber(rank.getRankValue()));

                    if (rank.getRankHidden() == 0) {
                        llRank2.setOnClickListener(v -> {
                            if (rank.getRankHidden() == 0)
                                UserDetailActivity.startActivity(getActivity(), rank.getUid());
                        });
                        tvName2.setText(rank.getNickname());
                        GlideUtils.loadDefaultCircleImage(getActivity(), rank.getAvatar(), ivRank2);

                    } else {
                        tvName2.setText(getString(R.string.mysteriousMan));
                        ivRank2.setImageResource(R.drawable.ic_shenmi);

                    }
                    break;
                case 2:
                    llRank3.setVisibility(View.VISIBLE);
                    tvValue3.setText(RegexUtils.formatNumber(rank.getRankValue()));

                    if (rank.getRankHidden() == 0) {
                        llRank3.setOnClickListener(v -> {
                            if (rank.getRankValue() == 0)
                                UserDetailActivity.startActivity(getActivity(), rank.getUid());
                        });
                        tvName3.setText(rank.getNickname());
                        GlideUtils.loadDefaultCircleImage(getActivity(), rank.getAvatar(), ivRank3);

                    } else {
                        tvName3.setText(getString(R.string.mysteriousMan));
                        ivRank3.setImageResource(R.drawable.ic_shenmi);


                    }
                    break;
                default:
                    tempData.add(rank);
                    break;
            }
        }

        adapter.setNewData(tempData);

        if (adapter.getData().size() > 0) {
            ViewUtils.setVisiableWithAlphaAnim(bottomRankLayout);
        }
    }
}

