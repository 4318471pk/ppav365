package com.live.fox.ui.rank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.base.BaseHeadActivity;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.Anchor;
import com.live.fox.entity.Rank;
import com.live.fox.server.Api_Rank;
import com.live.fox.ui.live.PlayLiveActivity;
import com.live.fox.ui.mine.activity.UserDetailActivity;
import com.live.fox.utils.BarUtils;
import com.live.fox.utils.GlideUtils;
import com.live.fox.utils.LruCacheUtil;
import com.live.fox.utils.RegexUtils;
import com.live.fox.utils.StatusBarUtil;
import com.live.fox.utils.ViewUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * getString(R.string.anchorBana)详情
 */
public class RankDetaiActivity extends BaseHeadActivity {
    private ImageView ivRank1;
    private ImageView ivRank1Bg;
    private ImageView ivRank2;
    private ImageView ivRank2Bg;
    private ImageView ivRank3;
    private ImageView ivRank3Bg;
    private RelativeLayout bottomRankLayout;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;
    private TextView tvValueTitle3;
    private TextView tvRank11;
    private TextView tvRank12;
    private TextView tvRank13;
    private TextView tvRank21;
    private TextView tvRank22;
    private TextView tvRank23;
    private TextView tvRank31;
    private TextView tvRank32;
    private TextView tvRank33;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private ImageView iv_zbz1;
    private ImageView iv_zbz2;
    private ImageView iv_zbz3;

    int type; // 1星值2财富
    int rankType; // 1+getString(R.string.erban)2周榜3月榜4昨+getString(R.string.erban)

    BaseQuickAdapter adapter;

    List<Rank> allData = new ArrayList<>();
    private Anchor currentAnchor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankdetail_activity);
        initView();
    }

    public void initView() {
        StatusBarUtil.setStatusBarFulAlpha(this);
        BarUtils.setStatusBarLightMode(this, false);
        setHead(getString(R.string.anchorBana), true, true);

        ivRank1 = findViewById(R.id.iv_rank1);
        ivRank1Bg = findViewById(R.id.iv_rank1_bg);
        ivRank2 = findViewById(R.id.iv_rank2);
        ivRank2Bg = findViewById(R.id.iv_rank2_bg);
        ivRank3 = findViewById(R.id.iv_rank3);
        ivRank3Bg = findViewById(R.id.iv_rank3_bg);
        bottomRankLayout = findViewById(R.id.layout_bottomrank);
        refreshLayout = findViewById(R.id.refreshLayout);
        rv = findViewById(R.id.rv_);
        tvValueTitle3 = findViewById(R.id.tv_valuetitle3);
        tvRank11 = findViewById(R.id.tv_rank1_1);
        tvRank12 = findViewById(R.id.tv_rank1_2);
        tvRank13 = findViewById(R.id.tv_rank1_3);
        tvRank21 = findViewById(R.id.tv_rank2_1);
        tvRank22 = findViewById(R.id.tv_rank2_2);
        tvRank23 = findViewById(R.id.tv_rank2_3);
        tvRank31 = findViewById(R.id.tv_rank3_1);
        tvRank32 = findViewById(R.id.tv_rank3_2);
        tvRank33 = findViewById(R.id.tv_rank3_3);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        iv_zbz1 = findViewById(R.id.iv_zbz1);
        iv_zbz2 = findViewById(R.id.iv_zbz2);
        iv_zbz3 = findViewById(R.id.iv_zbz3);
        findViewById(R.id.iv_rank1_bg).setOnClickListener(this);
        findViewById(R.id.iv_rank2_bg).setOnClickListener(this);
        findViewById(R.id.iv_rank3_bg).setOnClickListener(this);


        type = getIntent().getIntExtra("type", 1);
        rankType = getIntent().getIntExtra("rankType", 1);

        switch (type) {
            case 1:
                switch (rankType) {//423   234
                    case 1:
                        setTitle(getString(R.string.rank_title_today));
                        break;
                    case 2:
                        setTitle(getString(R.string.rank_title_week));
                        break;
                    case 3:
                        setTitle(getString(R.string.rank_title_month));
                        break;
                    case 4:
                        setTitle(getString(R.string.rank_title_yesterday));
                        break;
                }
                tvValueTitle3.setText(getString(R.string.mlz));
                tvRank13.setText(getString(R.string.mlz));
                tvRank23.setText(getString(R.string.mlz));
                tvRank33.setText(getString(R.string.mlz));
                break;
            case 2:
                switch (rankType) {
                    case 1:
                        setTitle(getString(R.string.rank_title_today));
                        break;
                    case 2:
                        setTitle(getString(R.string.rank_title_week));
                        break;
                    case 3:
                        setTitle(getString(R.string.totalBan));
                        break;

                }
                tvValueTitle3.setText(getString(R.string.gongxianzhi));
                tvRank13.setText(getString(R.string.gongxianzhi));
                tvRank23.setText(getString(R.string.gongxianzhi));
                tvRank33.setText(getString(R.string.gongxianzhi));
                break;

            case 3:
                switch (rankType) {
                    case 1:
                        setTitle(getString(R.string.rank_title_today));
                        break;
                    case 2:
                        setTitle(getString(R.string.rank_title_week));
                        break;
                    case 3:
                        setTitle(getString(R.string.rank_title_month));
                        break;

                }
                tvValueTitle3.setText(getString(R.string.PKjifeng));
                tvRank13.setText(getString(R.string.PKjifeng));
                tvRank23.setText(getString(R.string.PKjifeng));
                tvRank33.setText(getString(R.string.PKjifeng));
                break;
            case 4:
                switch (rankType) {
                    case 1:
                        setTitle(getString(R.string.rank_title_today));
                        break;
                    case 2:
                        setTitle(getString(R.string.rank_title_week));
                        break;
                    case 3:
                        setTitle(getString(R.string.rank_title_month));
                        break;
                }
                tvValueTitle3.setText(getString(R.string.flowingWater));
                tvRank13.setText(getString(R.string.flowingWater));
                tvRank23.setText(getString(R.string.flowingWater));
                tvRank33.setText(getString(R.string.flowingWater));
                break;
        }

        bottomRankLayout.setVisibility(View.INVISIBLE);
        setRecycleView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetRankListApi();
    }

    public void setRecycleView() {
        refreshLayout.setEnablePureScrollMode(true);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_rankdetail, new ArrayList<Rank>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                Rank rank = (Rank) item;
                if (rank.getRankHidden() == 0) {
                    GlideUtils.loadDefaultCircleImage(context, rank.getAvatar(), helper.getView(R.id.iv_head));
                    helper.setText(R.id.tv_name, rank.getNickname());
                } else {
                    helper.setImageResource(R.id.iv_head, R.drawable.ic_shenmi);
                    helper.setText(R.id.tv_name, getString(R.string.mysteriousMan));
                }
                ImageView iv_zbz = helper.getView(R.id.iv_zbz);
                if (0 == rank.getLiveId() || 2 == type || 4 == type) {
                    iv_zbz.setVisibility(View.GONE);
                } else {
                    iv_zbz.setVisibility(View.VISIBLE);
                    GlideUtils.loadImageByGif(context, R.drawable.zhibozhonga, iv_zbz);
                }
                helper.setText(R.id.tv_ranknum, (helper.getLayoutPosition() + 4) + "");
                helper.setText(R.id.tv_sendnum, RegexUtils.westMoney(rank.getRankValue()) + "");
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Rank rank = (Rank) adapter.getData().get(position);
            if (rank.getRankHidden() == 0) {
                if (0 == rank.getLiveId() || 2 == type || 4 == type) {
                    UserDetailActivity.startActivity(RankDetaiActivity.this, rank.getUid());
                } else {
                    currentAnchor = new Anchor();
                    currentAnchor.setAnchorId(rank.getAnchorId());
                    currentAnchor.setNickname(rank.getNickname());
                    currentAnchor.setAvatar(rank.getAvatar());
                    currentAnchor.setSignature(rank.getSignature());
                    currentAnchor.setLiveId(rank.getLiveId());
                    currentAnchor.setType(rank.getType());
                    currentAnchor.setPrice(rank.getPrice());
                    currentAnchor.setPking(rank.isPking());
                    currentAnchor.setRq(rank.getRq());
                    currentAnchor.setToy(rank.getToy());
                    LruCacheUtil.getInstance().put(currentAnchor);
                    LruCacheUtil.getInstance().get().clear();
                    PlayLiveActivity.startActivity(RankDetaiActivity.this, currentAnchor);
                }
            }
        });
    }

    public void refreshPage(List<Rank> data) {
        if (data == null && data.size() == 0) return;
        allData.clear();
        allData.addAll(data);

        ivRank1.setVisibility(View.INVISIBLE);
        ivRank2.setVisibility(View.INVISIBLE);
        ivRank3.setVisibility(View.INVISIBLE);
        layout1.setVisibility(View.INVISIBLE);
        layout2.setVisibility(View.INVISIBLE);
        layout3.setVisibility(View.INVISIBLE);
        ivRank1Bg.setImageResource(R.drawable.rank1_none);
        ivRank2Bg.setImageResource(R.drawable.rank2_none);
        ivRank3Bg.setImageResource(R.drawable.rank3_none);

        List<Rank> tempData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Rank rank = data.get(i);
            switch (i) {
                case 0:
                    if (rank.getRankHidden() == 0) {
                        GlideUtils.loadDefaultCircleImage(this, rank.getAvatar(), ivRank1);
                        tvRank11.setText(rank.getNickname());
                    } else {
                        ivRank1.setImageResource(R.drawable.ic_shenmi);
                        tvRank11.setText(getString(R.string.mysteriousMan));
                    }
                    ivRank1.setVisibility(View.VISIBLE);
                    layout1.setVisibility(View.VISIBLE);
                    ivRank1Bg.setImageResource(R.drawable.rank1_detail);
                    tvRank12.setText(RegexUtils.westMoney(rank.getRankValue()) + "");
                    if (0 == rank.getLiveId() || 2 == type || 4 == type) {
                        iv_zbz1.setVisibility(View.GONE);
                    } else {
                        GlideUtils.loadImageByGif(this, R.drawable.zhibozhonga, iv_zbz1);
                        iv_zbz1.setVisibility(View.VISIBLE);
                    }
                    break;
                case 1:
                    if (rank.getRankHidden() == 0) {
                        GlideUtils.loadDefaultCircleImage(this, rank.getAvatar(), ivRank2);
                        tvRank21.setText(rank.getNickname());
                    } else {
                        ivRank2.setImageResource(R.drawable.ic_shenmi);
                        tvRank21.setText(getString(R.string.mysteriousMan));
                    }
                    ivRank2.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.VISIBLE);
                    ivRank2Bg.setImageResource(R.drawable.rank2_detail);
                    tvRank22.setText(RegexUtils.westMoney(rank.getRankValue()) + "");
                    if (0 == rank.getLiveId() || 2 == type || 4 == type) {
                        iv_zbz2.setVisibility(View.GONE);
                    } else {
                        GlideUtils.loadImageByGif(this, R.drawable.zhibozhonga, iv_zbz2);
                        iv_zbz2.setVisibility(View.VISIBLE);
                    }
                    break;
                case 2:
                    if (rank.getRankHidden() == 0) {
                        GlideUtils.loadDefaultCircleImage(this, rank.getAvatar(), ivRank3);
                        tvRank31.setText(rank.getNickname());
                    } else {
                        ivRank3.setImageResource(R.drawable.ic_shenmi);
                        tvRank31.setText(getString(R.string.mysteriousMan));
                    }
                    ivRank3.setVisibility(View.VISIBLE);
                    layout3.setVisibility(View.VISIBLE);
                    ivRank3Bg.setImageResource(R.drawable.rank3_detail);
                    tvRank32.setText(RegexUtils.westMoney(rank.getRankValue()) + "");
                    if (0 == rank.getLiveId() || 2 == type || 4 == type) {
                        iv_zbz3.setVisibility(View.GONE);
                    } else {
                        GlideUtils.loadImageByGif(this, R.drawable.zhibozhonga, iv_zbz3);
                        iv_zbz3.setVisibility(View.VISIBLE);
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

    private void doGetRankListApi() {
        Api_Rank.ins().getRankLDetailsList(type, rankType, new JsonCallback<List<Rank>>() {
            @Override
            public void onSuccess(int code, String msg, List<Rank> data) {
                if (code == 0) {
                    refreshPage(data);
                } else {
                    showToastTip(false, msg);
                }
            }
        });
    }

    public static void startActivity(Context context, int type, int rankType) {
        Constant.isAppInsideClick = true;
        Intent intent = new Intent(context, RankDetaiActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("rankType", rankType);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_rank1_bg:
                if (allData == null || allData.size() < 1) return;
                if (allData.get(0).getRankHidden() == 0) {
                    if (0 == allData.get(0).getLiveId() || 2 == type || 4 == type) {//0 不在线
                        UserDetailActivity.startActivity(RankDetaiActivity.this, allData.get(0).getUid());
                    } else {
                        currentAnchor = new Anchor();
                        currentAnchor.setAnchorId(allData.get(0).getAnchorId());
                        currentAnchor.setNickname(allData.get(0).getNickname());
                        currentAnchor.setAvatar(allData.get(0).getAvatar());
                        currentAnchor.setSignature(allData.get(0).getSignature());
                        currentAnchor.setLiveId(allData.get(0).getLiveId());
                        currentAnchor.setType(allData.get(0).getType());
                        currentAnchor.setPrice(allData.get(0).getPrice());
                        currentAnchor.setPking(allData.get(0).isPking());
                        currentAnchor.setRq(allData.get(0).getRq());
                        currentAnchor.setToy(allData.get(0).getToy());
                        LruCacheUtil.getInstance().get().clear();
                        PlayLiveActivity.startActivity(RankDetaiActivity.this, currentAnchor);
                    }
                }
                break;
            case R.id.iv_rank2_bg:
                if (allData == null || allData.size() < 2) return;
                if (allData.get(1).getRankHidden() == 0) {
                    if (0 == allData.get(1).getLiveId() || 2 == type || 4 == type) {
                        UserDetailActivity.startActivity(RankDetaiActivity.this, allData.get(1).getUid());
                    } else {
                        currentAnchor = new Anchor();
                        currentAnchor.setAnchorId(allData.get(1).getAnchorId());
                        currentAnchor.setNickname(allData.get(1).getNickname());
                        currentAnchor.setAvatar(allData.get(1).getAvatar());
                        currentAnchor.setSignature(allData.get(1).getSignature());
                        currentAnchor.setLiveId(allData.get(1).getLiveId());
                        currentAnchor.setType(allData.get(1).getType());
                        currentAnchor.setPrice(allData.get(1).getPrice());
                        currentAnchor.setPking(allData.get(1).isPking());
                        currentAnchor.setRq(allData.get(1).getRq());
                        currentAnchor.setToy(allData.get(1).getToy());
                        LruCacheUtil.getInstance().get().clear();
                        PlayLiveActivity.startActivity(RankDetaiActivity.this, currentAnchor);
                    }
                }
                break;
            case R.id.iv_rank3_bg:
                if (allData == null || allData.size() < 3) return;
                if (allData.get(2).getRankHidden() == 0) {
                    if (0 == allData.get(2).getLiveId() || 2 == type || 4 == type) {
                        UserDetailActivity.startActivity(RankDetaiActivity.this, allData.get(2).getUid());
                    } else {
                        currentAnchor = new Anchor();
                        currentAnchor.setAnchorId(allData.get(2).getAnchorId());
                        currentAnchor.setNickname(allData.get(2).getNickname());
                        currentAnchor.setAvatar(allData.get(2).getAvatar());
                        currentAnchor.setSignature(allData.get(2).getSignature());
                        currentAnchor.setLiveId(allData.get(2).getLiveId());
                        currentAnchor.setType(allData.get(2).getType());
                        currentAnchor.setPrice(allData.get(2).getPrice());
                        currentAnchor.setPking(allData.get(2).isPking());
                        currentAnchor.setRq(allData.get(2).getRq());
                        currentAnchor.setToy(allData.get(2).getToy());
                        LruCacheUtil.getInstance().get().clear();
                        PlayLiveActivity.startActivity(RankDetaiActivity.this, currentAnchor);
                    }
                }
        }
    }
}
