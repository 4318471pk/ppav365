package com.live.fox.ui.rank;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.Rank;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.ui.mine.activity.UserDetailActivity;
import com.live.fox.utils.GlideUtils;

import java.util.List;



public class RankFragment extends BaseFragment implements View.OnClickListener{

    private ImageView ivRank1;
    private ImageView ivRank1Bg;
    private ImageView ivRank2;
    private ImageView ivRank2Bg;
    private ImageView ivRank3;
    private ImageView ivRank3Bg;
    private ImageView iv_zbz1;
    private ImageView iv_zbz2;
    private ImageView iv_zbz3;

    int type; // 1主播榜 2贡献榜 3PK榜 4游戏
    int rankType; // 1日榜2周榜3月榜4昨日榜
    List<Rank> rankList;

    boolean isInit = false;

    public static RankFragment newInstance(int type, int rankType) {
        RankFragment fragment = new RankFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("rankType", rankType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.rank_fragment, null, false);
        initData(getArguments());
        setView(rootView);
        return rootView;
    }


    public void initData(Bundle bundle) {
        if (bundle != null) {
            type = bundle.getInt("type");
            rankType = bundle.getInt("rankType");
        }
    }

    public void setView(View bindSource) {
        ivRank1 = bindSource.findViewById(R.id.iv_rank1);
        ivRank1Bg = bindSource.findViewById(R.id.iv_rank1_bg);
        ivRank2 = bindSource.findViewById(R.id.iv_rank2);
        ivRank2Bg = bindSource.findViewById(R.id.iv_rank2_bg);
        ivRank3 = bindSource.findViewById(R.id.iv_rank3);
        ivRank3Bg = bindSource.findViewById(R.id.iv_rank3_bg);
        iv_zbz1 = bindSource.findViewById(R.id.iv_zbz1);
        iv_zbz2 = bindSource.findViewById(R.id.iv_zbz2);
        iv_zbz3 = bindSource.findViewById(R.id.iv_zbz3);
        bindSource.findViewById(R.id.fl_rank1).setOnClickListener(this);
        bindSource.findViewById(R.id.fl_rank2).setOnClickListener(this);
        bindSource.findViewById(R.id.fl_rank3).setOnClickListener(this);
        bindSource.findViewById(R.id.tv_more).setOnClickListener(this);
        isInit = true;
        refreshPage(rankList);
    }

    public void refreshPage(List<Rank> data) {
        if (data == null) return;
        this.rankList = data;
        if (!isInit) return;

        ivRank1.setVisibility(View.INVISIBLE);
        ivRank2.setVisibility(View.INVISIBLE);
        ivRank3.setVisibility(View.INVISIBLE);
        ivRank1Bg.setImageResource(R.drawable.rank1_none);
        ivRank2Bg.setImageResource(R.drawable.rank2_none);
        ivRank3Bg.setImageResource(R.drawable.rank3_none);

        for (int i = 0; i < data.size(); i++) {
            Rank rank = data.get(i);
            switch (i) {
                case 0:
                    if (rank.getRankHidden() == 0)
                        GlideUtils.loadDefaultCircleImage(getActivity(), rank.getAvatar(), ivRank1);
                    else {
                        ivRank1.setImageResource(R.drawable.ic_shenmi);
                    }
                    if (0 == rankList.get(0).getLiveId() || 2 == type || 4 == type) {//0 不在线或者非主播
                        iv_zbz1.setVisibility(View.GONE);
                    } else {
                        GlideUtils.loadImageByGif(getActivity(), R.drawable.zhibozhonga, iv_zbz1);
                        iv_zbz1.setVisibility(View.VISIBLE);
                    }
                    ivRank1.setVisibility(View.VISIBLE);
                    ivRank1Bg.setImageResource(R.drawable.rank1_detail);
                    break;
                case 1:
                    if (rank.getRankHidden() == 0)
                        GlideUtils.loadDefaultCircleImage(getActivity(), rank.getAvatar(), ivRank2);
                    else {
                        ivRank2.setImageResource(R.drawable.ic_shenmi);
                    }
                    if (0 == rankList.get(1).getLiveId() || 2 == type || 4 == type) {
                        iv_zbz2.setVisibility(View.GONE);
                    } else {
                        GlideUtils.loadImageByGif(getActivity(), R.drawable.zhibozhonga, iv_zbz2);
                        iv_zbz2.setVisibility(View.VISIBLE);
                    }
                    ivRank2.setVisibility(View.VISIBLE);
                    ivRank2Bg.setImageResource(R.drawable.rank2_detail);
                    break;
                case 2:
                    if (rank.getRankHidden() == 0) {
                        GlideUtils.loadDefaultCircleImage(getActivity(), rank.getAvatar(), ivRank3);
                    } else {
                        ivRank3.setImageResource(R.drawable.ic_shenmi);
                    }
                    if (0 == rankList.get(2).getLiveId() || 2 == type || 4 == type) {//0 不在线或者非主播
                        iv_zbz3.setVisibility(View.GONE);
                    } else {
                        GlideUtils.loadImageByGif(getActivity(), R.drawable.zhibozhonga, iv_zbz3);
                        iv_zbz3.setVisibility(View.VISIBLE);
                    }
                    ivRank3.setVisibility(View.VISIBLE);
                    ivRank3Bg.setImageResource(R.drawable.rank3_detail);
                    break;
            }
        }
    }




    @Override
    public void onClick(View view) {
        Anchor currentAnchor;
        switch (view.getId()) {
            case R.id.fl_rank1:
                if (rankList == null || rankList.size() < 1) return;
                if (rankList.get(0).getRankHidden() == 0) {
                    if (0 == rankList.get(0).getLiveId() || 2 == type || 4 == type) {//0 不在线或者非主播
                        UserDetailActivity.startActivity(requireActivity(), rankList.get(0).getUid());
                    } else {
                        currentAnchor = new Anchor();
                        currentAnchor.setAnchorId(rankList.get(0).getAnchorId());
                        currentAnchor.setNickname(rankList.get(0).getNickname());
                        currentAnchor.setAvatar(rankList.get(0).getAvatar());
                        currentAnchor.setSignature(rankList.get(0).getSignature());
                        currentAnchor.setLiveId(rankList.get(0).getLiveId());
                        currentAnchor.setType(rankList.get(0).getType());
                        currentAnchor.setPrice(rankList.get(0).getPrice());
                        currentAnchor.setPking(rankList.get(0).isPking());
                        currentAnchor.setRq(rankList.get(0).getRq());
                        currentAnchor.setToy(rankList.get(0).getToy());
                        PlayLiveActivity.startActivity(requireActivity(), currentAnchor);
                    }
                }
                break;

            case R.id.fl_rank2:
                if (rankList == null || rankList.size() < 2) return;
                if (rankList.get(1).getRankHidden() == 0 || 2 == type || 4 == type) {
                    if (0 == rankList.get(1).getLiveId()) {
                        UserDetailActivity.startActivity(requireActivity(), rankList.get(1).getUid());
                    } else {
                        currentAnchor = new Anchor();
                        currentAnchor.setAnchorId(rankList.get(1).getAnchorId());
                        currentAnchor.setNickname(rankList.get(1).getNickname());
                        currentAnchor.setAvatar(rankList.get(1).getAvatar());
                        currentAnchor.setSignature(rankList.get(1).getSignature());
                        currentAnchor.setLiveId(rankList.get(1).getLiveId());
                        currentAnchor.setType(rankList.get(1).getType());
                        currentAnchor.setPrice(rankList.get(1).getPrice());
                        currentAnchor.setPking(rankList.get(1).isPking());
                        currentAnchor.setRq(rankList.get(1).getRq());
                        currentAnchor.setToy(rankList.get(1).getToy());
                        PlayLiveActivity.startActivity(requireActivity(), currentAnchor);
                    }
                }
                break;

            case R.id.fl_rank3:
                if (rankList == null || rankList.size() < 3) return;
                if (rankList.get(2).getRankHidden() == 0 || 2 == type || 4 == type) {
                    if (0 == rankList.get(2).getLiveId()) {//rankList.get(2).getLiveId()!=null
                        UserDetailActivity.startActivity(requireActivity(), rankList.get(2).getUid());
                    } else {
                        currentAnchor = new Anchor();
                        currentAnchor.setAnchorId(rankList.get(2).getAnchorId());
                        currentAnchor.setNickname(rankList.get(2).getNickname());
                        currentAnchor.setAvatar(rankList.get(2).getAvatar());
                        currentAnchor.setSignature(rankList.get(2).getSignature());
                        currentAnchor.setLiveId(rankList.get(2).getLiveId());
                        currentAnchor.setType(rankList.get(2).getType());
                        currentAnchor.setPrice(rankList.get(2).getPrice());
                        currentAnchor.setPking(rankList.get(2).isPking());
                        currentAnchor.setRq(rankList.get(2).getRq());
                        currentAnchor.setToy(rankList.get(2).getToy());
                        PlayLiveActivity.startActivity(requireActivity(), currentAnchor);
                    }
                }
                break;

            case R.id.tv_more:
                RankDetaiActivity.startActivity(requireActivity(), type, rankType);
                break;
        }
    }
}

