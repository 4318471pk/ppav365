package com.live.fox.ui.lottery;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.entity.LivingLotteryListBean;
import com.live.fox.ui.lottery.adapter.LotteryAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.LayoutRcBinding;

import java.util.ArrayList;
import java.util.List;

public class LotteryItemListFragment extends BaseBindingFragment {

    LayoutRcBinding mBind;

    LivingLotteryListBean.ItemsBean itemsBean;

    LotteryAdapter lotteryAdapter;
    List<LivingLotteryListBean.ItemsBean.ConfigGameBaseListBean> lotteryList = new ArrayList<>();


    public static LotteryItemListFragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString("data", data);
        LotteryItemListFragment lotteryItemListFragment = new LotteryItemListFragment();
        lotteryItemListFragment.setArguments(args);
        return lotteryItemListFragment;
    }

    @Override
    public void onClickView(View view) {

    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.layout_rc;
    }

    @Override
    public void initView(View view) {
        mBind = getViewDataBinding();

        String data = getArguments().getString("data");
        itemsBean = new Gson().fromJson(data, LivingLotteryListBean.ItemsBean.class);
        if (itemsBean.getConfigGameBaseList() != null && itemsBean.getConfigGameBaseList().size() > 0 ) {
            lotteryList.addAll(itemsBean.getConfigGameBaseList());
        }
        lotteryAdapter = new LotteryAdapter(this.getContext(),lotteryList);
        mBind.rc.setAdapter(lotteryAdapter);

        mBind.rc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TouzhuDialog touzhuDialog = new TouzhuDialog();
                DialogFramentManager.getInstance().showDialog(LotteryItemListFragment.this.getActivity().getSupportFragmentManager(), touzhuDialog);
            }
        });

    }

}
