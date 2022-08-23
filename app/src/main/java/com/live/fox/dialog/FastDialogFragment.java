package com.live.fox.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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


public class FastDialogFragment extends DialogFragment {
    private String lotteryName;
    int page = 0;
    BaseQuickAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;
    private String nickName;
    private TextView tvTitle;
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
        nickName= bundle.getString("nickName");
        initView(view);
        getAllLotteryLatestResult(true);
    }

    public static FastDialogFragment newInstance(String lotteryName, String nickName) {
        FastDialogFragment fragment = new FastDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("lotteryName", lotteryName);
        bundle.putString("nickName", nickName);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        rv = (RecyclerView) view.findViewById(R.id.rv_);
        tvTitle=view.findViewById(R.id.tvTitle);
        tvTitle.setText(nickName);
        setRecycleView();
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_history_ssc, new ArrayList<LsResult>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                LsResult lsResult = (LsResult) item;
                helper.setText(R.id.tv_qihao,lsResult.getExpect());
                List<Integer> lotteryResult=lsResult.getLotteryResult();
                helper.setText(R.id.numa,lotteryResult.get(0)+"");
                helper.setText(R.id.numb,lotteryResult.get(1)+"");
                helper.setText(R.id.numc,lotteryResult.get(2)+"");
                helper.setText(R.id.numd,lotteryResult.get(3)+"");
                helper.setText(R.id.nume,lotteryResult.get(4)+"");
                helper.setVisible(R.id.numf,false);
                helper.setVisible(R.id.numg,false);
                helper.setVisible(R.id.numh,false);
                helper.setVisible(R.id.numi,false);
                helper.setVisible(R.id.numj,false);
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
    }
}
