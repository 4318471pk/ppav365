package com.live.fox.ui.lottery;

import android.view.View;
import android.widget.AdapterView;

import com.live.fox.R;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.ui.lottery.adapter.LotteryAdapter;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.databinding.LayoutRcBinding;

import java.util.ArrayList;
import java.util.List;

public class LotteryItemListFragment extends BaseBindingFragment {

    LayoutRcBinding mBind;

    LotteryAdapter lotteryAdapter;
    List<String> lotteryList = new ArrayList<>();


    public static LotteryItemListFragment newInstance() {
        LotteryItemListFragment lotteryItemListFragment = new LotteryItemListFragment();
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

        lotteryList.add("1");lotteryList.add("1");lotteryList.add("1");
        lotteryList.add("1");lotteryList.add("1");lotteryList.add("1");lotteryList.add("1");
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
