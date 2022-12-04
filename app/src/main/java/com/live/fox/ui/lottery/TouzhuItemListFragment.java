package com.live.fox.ui.lottery;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.live.fox.R;
import com.live.fox.base.BaseBindingFragment;
import com.live.fox.base.DialogFramentManager;
import com.live.fox.databinding.LayoutRcBinding;
import com.live.fox.entity.LiveRoomGameDetailBean;
import com.live.fox.entity.TouzhuDetailBean;
import com.live.fox.ui.lottery.adapter.LotteryAdapter;
import com.live.fox.ui.lottery.adapter.TouzhuDetailAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TouzhuItemListFragment extends BaseBindingFragment {

    public static final int VIEW_NORMAIL = 0; //正常布局
    public static final int VIEW_YXX_1 = 1; //鱼虾蟹1
    public static final int VIEW_YXX_2 = 2; //鱼虾蟹2
    public static final int VIEW_NN = 3; //牛牛

    LayoutRcBinding mBind;

    TouzhuDetailAdapter mAdapgter;
    List<TouzhuDetailBean> lotteryList = new ArrayList<>();

    int itemNum = 0;//一行显示几个投注按钮

    int viewType = VIEW_NORMAIL;
    private LiveRoomGameDetailBean liveRoomGameDetailBean;


    public static TouzhuItemListFragment newInstance(int num, LiveRoomGameDetailBean liveRoomGameDetailBean) {
        TouzhuItemListFragment fragment = new TouzhuItemListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("itemNum", num);
        bundle.putSerializable("liveRoomGameDetailBean",liveRoomGameDetailBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TouzhuItemListFragment newInstance(int num, int viewType ) {
        TouzhuItemListFragment fragment = new TouzhuItemListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("itemNum", num);
        bundle.putInt("viewType", viewType);
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
        viewType = this.getArguments().getInt("viewType");
        liveRoomGameDetailBean= (LiveRoomGameDetailBean) this.getArguments().getSerializable("liveRoomGameDetailBean");


        lotteryList.clear();

        if(liveRoomGameDetailBean!=null){
            String json2 = liveRoomGameDetailBean.getOdds();

            HashMap<String, Double> map = new Gson().fromJson(json2, new TypeToken<HashMap>() {
                        }.getType());


            Iterator<Map.Entry<String, Double>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Double> entry = it.next();


                TouzhuDetailBean touzhuDetailBean=new TouzhuDetailBean();
                touzhuDetailBean.select=false;
                touzhuDetailBean.name= entry.getKey();
                touzhuDetailBean.odds= entry.getValue();


                lotteryList.add(touzhuDetailBean);
            }



        }


        mAdapgter = new TouzhuDetailAdapter(this.getContext(),lotteryList);
        mAdapgter.setShowItemNum(itemNum);
        mAdapgter.setViewType(viewType);
        mBind.rc.setAdapter(mAdapgter);
        mBind.rc.setNumColumns(itemNum);

        mBind.rc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lotteryList.get(position).select=!lotteryList.get(position).select;
//                lotteryList.set(position, true);
                if (touzhuSelectSuc != null) {
                    touzhuSelectSuc.clickTz("test");
                }
                mAdapgter.notifyDataSetChanged();
            }
        });

    }


    public List<TouzhuDetailBean> getLotteryList() {
        return lotteryList;
    }

    public interface TouzhuSelectSuc{

        //点击小的
        public void clickTz(String text);
        public void cancelTz(String text);
    }

    private TouzhuSelectSuc touzhuSelectSuc;

    public void setTouzhuSelectSuc(TouzhuSelectSuc touzhuSelectSuc) {
        this.touzhuSelectSuc = touzhuSelectSuc;
    }


}
