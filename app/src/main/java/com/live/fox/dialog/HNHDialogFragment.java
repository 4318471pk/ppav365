package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen;
import com.ethanhua.skeleton.Skeleton;
import com.live.fox.Constant;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.LsResult;
import com.live.fox.server.Api_Cp;
import com.live.fox.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class HNHDialogFragment extends DialogFragment {
    private String lotteryName;
    int page = 0;
    BaseQuickAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;
    private String nickName;
    private TextView tvTitle;
    private RecyclerViewSkeletonScreen show;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_cplist);
        dialog.setCanceledOnTouchOutside(true); // 外部點擊取消
        // 設置寬度爲屏寬, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 寬度持平
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.BOTTOM);
        window.setDimAmount(0.05f);
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
        window.setAttributes(lp);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cplist, container, false);
    }

    private void getAllLotteryLatestResult(boolean isRefresh) {
        Api_Cp.ins().getKSResult(lotteryName, page, new JsonCallback<List<LsResult>>() {
            @Override
            public void onSuccess(int code, String msg, List<LsResult> data) {
                show.hide();
                if (code == 0) {
                    if (isRefresh) {
                        refreshLayout.finishRefresh();
                        refreshLayout.setEnableLoadMore(true);
                        if (data == null || data.size() == 0) {
                            ToastUtils.showShort(getString(R.string.noList));
                        } else {
                            adapter.setNewData(data);
                        }
                    } else {
                        refreshLayout.finishLoadMore();
                        List<LsResult> list = adapter.getData();
                        adapter.addData(data);
                        adapter.notifyItemRangeInserted(list.size(), data.size());
                    }
                    if (data != null && data.size() < Constant.pageSizeLater) {
                        //如果没有更多的数据 则隐藏加载更多功能
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        lotteryName = bundle.getString("lotteryName");
        nickName = bundle.getString("nickName");
        initView(view);
    }

    public static HNHDialogFragment newInstance(String lotteryName, String nickName) {
        HNHDialogFragment fragment = new HNHDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lotteryName", lotteryName);
        bundle.putString("nickName", nickName);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        rv = (RecyclerView) view.findViewById(R.id.rv_);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(nickName);
        setRecycleView();
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_history_hn, new ArrayList<LsResult>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                LsResult lsResult = (LsResult) item;
                helper.setText(R.id.tv_qihao, lsResult.getExpect());
                LinearLayout llblow = helper.getView(R.id.llblow);
                llblow.setVisibility(View.GONE);
                LinearLayout llup = helper.getView(R.id.llup);
                ImageView iv_hn = helper.getView(R.id.iv_hn);
                List<Integer> lotteryResult = lsResult.getLotteryResult();
                helper.setText(R.id.numa, lotteryResult.get(0) + "");
                helper.setText(R.id.numb, lotteryResult.get(1) + "");
                helper.setText(R.id.numc, lotteryResult.get(2) + "");
                helper.setText(R.id.numd, lotteryResult.get(3) + "");
                helper.setText(R.id.tvg4, lotteryResult.get(4) + "");
                helper.setText(R.id.tvg5, lotteryResult.get(5) + "");
                helper.setText(R.id.tvg6, lotteryResult.get(6) + "");
                helper.setText(R.id.tvg7, lotteryResult.get(7) + "");
                helper.setText(R.id.tvg8, lotteryResult.get(8) + "");
                helper.setText(R.id.tvg9, lotteryResult.get(9) + "");
                helper.setText(R.id.tvg10, lotteryResult.get(10) + "");
                helper.setText(R.id.tvg11, lotteryResult.get(11) + "");
                helper.setText(R.id.tvg12, lotteryResult.get(12) + "");
                helper.setText(R.id.tvg13, lotteryResult.get(13) + "");
                helper.setText(R.id.tvg14, lotteryResult.get(14) + "");
                helper.setText(R.id.tvg15, lotteryResult.get(15) + "");
                helper.setText(R.id.tvg16, lotteryResult.get(16) + "");
                helper.setText(R.id.tvg17, lotteryResult.get(17) + "");
                helper.setText(R.id.tvg18, lotteryResult.get(18) + "");
                helper.setText(R.id.tvg19, lotteryResult.get(19) + "");
                helper.setText(R.id.tvg20, lotteryResult.get(20) + "");
                helper.setText(R.id.tvg21, lotteryResult.get(21) + "");
                helper.setText(R.id.tvg22, lotteryResult.get(22) + "");
                if (lotteryResult.get(23) != null && lotteryResult.get(23) < 10) {
                    helper.setText(R.id.tvg23, "0" + lotteryResult.get(23));
                } else {
                    helper.setText(R.id.tvg23, lotteryResult.get(23) + "");
                }
                if (lotteryResult.get(24) != null && lotteryResult.get(24) < 10) {
                    helper.setText(R.id.tvg24, "0" + lotteryResult.get(24));
                } else {
                    helper.setText(R.id.tvg24, lotteryResult.get(24) + "");
                }
                if (lotteryResult.get(25) != null && lotteryResult.get(25) < 10) {
                    helper.setText(R.id.tvg25, "0" + lotteryResult.get(25));
                } else {
                    helper.setText(R.id.tvg25, lotteryResult.get(25) + "");
                }
                if (lotteryResult.get(26) != null && lotteryResult.get(26) < 10) {
                    helper.setText(R.id.tvg26, "0" + lotteryResult.get(26));
                } else {
                    helper.setText(R.id.tvg26, lotteryResult.get(26) + "");
                }
                llup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (View.GONE == llblow.getVisibility()) {
                            llblow.setVisibility(View.VISIBLE);
                            iv_hn.setImageResource(R.drawable.hncabove);
                        } else {
                            llblow.setVisibility(View.GONE);
                            iv_hn.setImageResource(R.drawable.hncblow);
                        }
                    }
                });
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                getAllLotteryLatestResult(true);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page = page + 1;
                getAllLotteryLatestResult(false);
            }
        });

        show = Skeleton.bind(rv).adapter(adapter).load(R.layout.item_text_loading).show();

        getAllLotteryLatestResult(true);
    }
}
