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
import com.live.fox.entity.MyTZResult;
import com.live.fox.server.Api_Cp;
import com.live.fox.utils.AppUserManger;
import com.live.fox.utils.TimeUtils;
import com.live.fox.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class MyTZDialogFragment extends DialogFragment {
    int page = 0;

    BaseQuickAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogDefault);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_mytouzu);
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
        return inflater.inflate(R.layout.fragment_mytouzu, container, false);
    }

    private void getAllLotteryLatestResult(boolean isRefresh) {
        Api_Cp.ins().getMyTZResult(AppUserManger.getUserInfo().getUid(), page, new JsonCallback<List<MyTZResult>>() {
            @Override
            public void onSuccess(int code, String msg, List<MyTZResult> data) {
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
                        List<MyTZResult> list = adapter.getData();
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
        initView(view);
        getAllLotteryLatestResult(true);
    }

    public static MyTZDialogFragment newInstance() {
        MyTZDialogFragment fragment = new MyTZDialogFragment();
        return fragment;
    }

    private void initView(View view) {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        rv = view.findViewById(R.id.rv_);
        setRecycleView();
    }

    public void setRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter = new BaseQuickAdapter(R.layout.item_my_touzu, new ArrayList<MyTZResult>()) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                MyTZResult myTZResult = (MyTZResult) item;
                TextView tvJieguo = helper.getView(R.id.tv_jieguo);
                helper.setText(R.id.tv_time, TimeUtils.convertShortTime(myTZResult.getCreateTime()));
                helper.setText(R.id.tv_nickname, myTZResult.getNickName());
                helper.setText(R.id.tv_money, myTZResult.getBetAmount() + "");
                if (0 == myTZResult.getAwardStatus()) {
                    tvJieguo.setText(getString(R.string.lottery_open_prize_yet));
                    tvJieguo.setBackgroundResource(0);
                } else if (1 == myTZResult.getAwardStatus()) {
                    tvJieguo.setBackgroundResource(R.drawable.jfail);
                    tvJieguo.setText("");
                } else {
                    tvJieguo.setBackgroundResource(R.drawable.jsuccess);
                    tvJieguo.setText("");
                }
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            MyTZResult myTZResult = (MyTZResult) adapter.getData().get(position);
            if ("yn_hncp".equals(myTZResult.getLotteryName())) {
                MyLHCDialogFragment mylhcDialogFragment = MyLHCDialogFragment.newInstance(myTZResult);
                if (!mylhcDialogFragment.isAdded()) {
                    mylhcDialogFragment.show(requireActivity().getSupportFragmentManager(), MyLHCDialogFragment.class.getSimpleName());
                }
            } else {
                MyOhtersDialogFragment myOthersDialogFragment = MyOhtersDialogFragment.newInstance(myTZResult);
                if (!myOthersDialogFragment.isAdded()) {
                    myOthersDialogFragment.show(requireActivity().getSupportFragmentManager(), MyOhtersDialogFragment.class.getSimpleName());
                }
            }
        });
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 0;
            getAllLotteryLatestResult(true);
        });

        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page = page + 1;
            getAllLotteryLatestResult(false);
        });
    }
}
