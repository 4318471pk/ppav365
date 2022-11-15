package com.live.fox.ui.lottery;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.LayoutRcBinding;
import com.live.fox.ui.lottery.adapter.LotteryAdapter;
import com.live.fox.ui.lottery.adapter.TouzhuDetailAdapter;

import java.util.ArrayList;
import java.util.List;

public class TouzhuItemListFragment extends BaseBindingFragment {

    LayoutRcBinding mBind;

    TouzhuDetailAdapter mAdapgter;
    List<Boolean> lotteryList = new ArrayList<>();

    int itemNum = 0;//一行显示几个投注按钮


    public static TouzhuItemListFragment newInstance(int num) {
        TouzhuItemListFragment fragment = new TouzhuItemListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("itemNum", num);
        fragment.setArguments(bundle);
        return fragment;
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
        itemNum = this.getArguments().getInt("itemNum");

        lotteryList.clear();
        lotteryList.add(false);lotteryList.add(false);
        lotteryList.add(false);lotteryList.add(false);
        lotteryList.add(false);lotteryList.add(false);
        lotteryList.add(false);lotteryList.add(false);
        lotteryList.add(false);lotteryList.add(false);
        lotteryList.add(false);lotteryList.add(false);
        lotteryList.add(false);lotteryList.add(false);

        mAdapgter = new TouzhuDetailAdapter(this.getContext(),lotteryList);
        mAdapgter.setShowItemNum(itemNum);
        mBind.rc.setAdapter(mAdapgter);
        mBind.rc.setNumColumns(itemNum);

        mBind.rc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < lotteryList.size(); i ++) {
                    if (lotteryList.get(i)) {
                        if (i == position) {
                            return;
                        } else {
                            lotteryList.set(i, false);
                            break;
                        }
                    }
                }
                lotteryList.set(position, true);
                if (touzhuSelectSuc != null) {
                    touzhuSelectSuc.clickTz("test");
                }
                mAdapgter.notifyDataSetChanged();
            }
        });

    }

    public interface TouzhuSelectSuc{
        public void clickTz(String text);
        public void cancelTz(String text);
    }

    private TouzhuSelectSuc touzhuSelectSuc;

    public void setTouzhuSelectSuc(TouzhuSelectSuc touzhuSelectSuc) {
        this.touzhuSelectSuc = touzhuSelectSuc;
    }


}
