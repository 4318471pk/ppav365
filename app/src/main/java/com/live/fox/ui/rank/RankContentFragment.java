package com.live.fox.ui.rank;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.live.fox.R;
import com.live.fox.base.BaseFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.Rank;
import com.live.fox.server.Api_Rank;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.LruCacheUtil;

import java.util.List;

/**
 * 排行底部数据
 * 非前三
 */
public class RankContentFragment extends BaseFragment {

    public static final String PARAMETERS_KEY = "parameters key";

    private RecyclerView recyclerView;
    private RankContentAdapter adapter;
    private List<Rank> rankList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        recyclerView = view.findViewById(R.id.single_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            ParametersEntity parameters = getArguments().getParcelable(PARAMETERS_KEY);
            if (parameters != null) {
                adapter = new RankContentAdapter(parameters.rankType);
                recyclerView.setAdapter(adapter);
                requestRemote(parameters);
                gotoAnchorListener();
            }
        }
    }

    //去直播间
    private void gotoAnchorListener() {
        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            Rank rank = rankList.get(i);
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
        });
    }

    /**
     * 请求接口数据
     */
    private void requestRemote(ParametersEntity parameters) {
        Api_Rank.ins().getRankLDetailsList(parameters.rankType, parameters.rankClass, new JsonCallback<List<Rank>>() {
            @Override
            public void onSuccess(int code, String msg, List<Rank> data) {
                if (code == 0) {
                    rankList = data;
                    adapter.addData(data);
                    LogUtils.e("Api_Rank", data);
                } else {
                    showToastTip(false, msg);
                }
            }
        });
    }

    /**
     * 请求排行榜所需参数
     */
    public static class ParametersEntity implements Parcelable {

        private int rankType;    //排行类型
        private int rankClass;   //排行类别

        public ParametersEntity() {

        }

        protected ParametersEntity(Parcel in) {
            rankType = in.readInt();
            rankClass = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(rankType);
            dest.writeInt(rankClass);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ParametersEntity> CREATOR = new Creator<ParametersEntity>() {
            @Override
            public ParametersEntity createFromParcel(Parcel in) {
                return new ParametersEntity(in);
            }

            @Override
            public ParametersEntity[] newArray(int size) {
                return new ParametersEntity[size];
            }
        };

        public int getRankType() {
            return rankType;
        }

        public void setRankType(int rankType) {
            this.rankType = rankType;
        }

        public int getRankClass() {
            return rankClass;
        }

        public void setRankClass(int rankClass) {
            this.rankClass = rankClass;
        }
    }
}
