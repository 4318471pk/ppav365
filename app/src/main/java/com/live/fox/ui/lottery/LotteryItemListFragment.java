package com.live.fox.ui.lottery;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.CountDownBean;
import com.live.fox.entity.LiveRoomGameDetailBean;
import com.live.fox.entity.LivingLotteryListBean;
import com.live.fox.server.Api_Living_Lottery;
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


    public static LotteryItemListFragment newInstance(LivingLotteryListBean.ItemsBean itemsBean) {
        LotteryItemListFragment lotteryItemListFragment = new LotteryItemListFragment();
        lotteryItemListFragment.itemsBean=itemsBean;
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

        if (itemsBean != null &&itemsBean.getConfigGameBaseList() != null && itemsBean.getConfigGameBaseList().size() > 0 ) {
            lotteryList.addAll(itemsBean.getConfigGameBaseList());
        }
        lotteryAdapter = new LotteryAdapter(this.getContext(),lotteryList);
        mBind.rc.setAdapter(lotteryAdapter);

        mBind.rc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String gameCode=lotteryList.get(position).getGameCode();


                getLiveRoomGameDetail(gameCode);

                countDown( gameCode);

                TouzhuDialog touzhuDialog =TouzhuDialog.newInstance(lotteryList.get(position).getGameCode(),lotteryList.get(position).getId()+"",lotteryList.get(position).getGameName());

                DialogFramentManager.getInstance().showDialog(LotteryItemListFragment.this.getActivity().getSupportFragmentManager(), touzhuDialog);
                if(getParentFragment()!=null && (getParentFragment() instanceof LotteryDialog))
                {
                    LotteryDialog lotteryDialog=(LotteryDialog)getParentFragment();
                    lotteryDialog.onBackPress();
                }
            }
        });
    }

    private void getLiveRoomGameDetail(String gameCode)
    {
        Api_Living_Lottery.ins().getLiveRoomGameDetail(gameCode, new JsonCallback<List<LiveRoomGameDetailBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<LiveRoomGameDetailBean> data) {

                if(data==null){
                    return;
                }
//                Log.e("getLiveRoomGameDetail",data);
            }
        });



    }


    private void countDown(String gameCode)
    {
        Api_Living_Lottery.ins().countDown(gameCode, new JsonCallback<CountDownBean>() {
            @Override
            public void onSuccess(int code, String msg, CountDownBean data) {

                if(data==null){
                    return;
                }
//                Log.e("getLiveRoomGameDetail",data);
            }
        });
    }

}
