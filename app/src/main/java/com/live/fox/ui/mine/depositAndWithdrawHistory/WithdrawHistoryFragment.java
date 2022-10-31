package com.live.fox.ui.mine.depositAndWithdrawHistory;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.live.fox.R;
import com.live.fox.adapter.DepositHistoryAdapter;
import com.live.fox.adapter.WithdrawHistoryAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.common.JsonCallback;
import com.live.fox.databinding.FragmentWithdrawHistoryBinding;
import com.live.fox.entity.DepositeHistoryBean;
import com.live.fox.entity.WithDrawRecordBean;
import com.live.fox.entity.WithdrawHistoryBean;
import com.live.fox.server.Api_Order;
import com.live.fox.utils.ToastUtils;
import com.live.fox.utils.device.ScreenUtils;
import com.live.fox.view.myHeader.MyWaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WithdrawHistoryFragment extends BaseBindingFragment {

    FragmentWithdrawHistoryBinding mBind;
    WithdrawHistoryAdapter withdrawHistoryAdapter;
    private int titles[]=new int[]{R.string.withdrawTime,R.string.amountOfMoney,R.string.withdrawType2,R.string.depositStatus};
    List<WithdrawHistoryBean> list;

    int pageSize = 20;
    int pageNum = 1;

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.fragment_withdraw_history;
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();

        mBind.refersh.setRefreshHeader(new MyWaterDropHeader(this.getContext()));

        list=new ArrayList<>();
//        for (int i = 0; i <20 ; i++) {
//            WithdrawHistoryBean bean=new WithdrawHistoryBean();
//            bean.setAmountOfMoney(new BigDecimal("21.23"));
//            bean.setStatusStr("成功");
//            bean.setWithdrawTime(System.currentTimeMillis());
//            bean.setWithdrawWay("支付宝");
//            list.add(bean);
//        }
        withdrawHistoryAdapter=new WithdrawHistoryAdapter(getActivity(),list);
        withdrawHistoryAdapter.addHeaderView(makeHeader());
        mBind.rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        mBind.rvMain.setAdapter(withdrawHistoryAdapter);

        mBind.refersh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull @NotNull RefreshLayout refreshLayout) {
                pageNum ++ ;
                getRecord(false);
            }
        });

        mBind.refersh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                list.clear();
                pageNum = 1;
                getRecord(true);
            }
        });
        getRecord(true);
    }

    private View makeHeader()
    {

        float ratio[]=new float[]{0.2f,0.3f,0.3f,0.2f};

        int height= ScreenUtils.getDip2px(getContext(),38);
        int width=ScreenUtils.getScreenWidth(getContext())
                -ScreenUtils.getDip2px(getContext(),10);

        LinearLayout linearLayout=new LinearLayout(getContext());
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(width,height));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(0xffffffff);

        for (int i = 0; i <titles.length ; i++) {
            TextView textView=new TextView(getContext());
            int itemWidth=(int)(width*ratio[i]);
            LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(itemWidth,height);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            textView.setTextColor(0xff665275);
            textView.setText(titles[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(ll);
            linearLayout.addView(textView);
        }

        return linearLayout;
    }


    private void getRecord(boolean isFrash){
        Api_Order.ins().getWithDrawRecord(new JsonCallback<WithDrawRecordBean>() {
            @Override
            public void onSuccess(int code, String msg, WithDrawRecordBean data) {
                dismissLoadingDialog();
                mBind.refersh.finishRefresh();
                mBind.refersh.finishLoadMore();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                        if (data.getRecords() != null && data.getRecords().size() >0) {
                            list.addAll(data.getRecords());
                            if (data.getRecords().size() < pageSize ) {
                                mBind.refersh.setEnableLoadMore(true);
                            }
                            if (isFrash) {
                                withdrawHistoryAdapter.notifyDataSetChanged();
                            } else {
                                withdrawHistoryAdapter.notifyItemRangeInserted(
                                        list.size() - data.getRecords().size(), data.getSize());
                            }

                            mBind.tvNoMore.setVisibility(View.VISIBLE);
                            if (list.size() /2 == 0) {
                                mBind.tvNoMore.setBackgroundColor(getResources().getColor(R.color.white));
                            } else {
                                mBind.tvNoMore.setBackgroundColor(getResources().getColor(R.color.gray_bg));
                            }
                        } else {
                            mBind.refersh.setEnableLoadMore(true);
                            if (isFrash) {
                                mBind.tvNoMore.setVisibility(View.GONE);
                            }
                        }
                } else {
                    if (!isFrash) {
                        pageNum --;
                    } else {
                        mBind.tvNoMore.setVisibility(View.GONE);
                    }

                    ToastUtils.showShort(msg);
                }
            }
        }, pageNum, pageSize);
    }

}
