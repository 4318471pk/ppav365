package com.live.fox.ui.lottery;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.live.fox.R;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.ActBean;
import com.live.fox.entity.LivingLotteryListBean;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.server.Api_Living_Lottery;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.ui.lottery.adapter.LotteryNameAdapter;
import com.live.fox.ui.lottery.adapter.LotteryNameAdapter;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.BaseFragment;
import com.live.fox.databinding.DialogLotteryBinding;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LotteryDialog extends BaseBindingDialogFragment {

    DialogLotteryBinding mBind;

    LotteryNameAdapter lotteryNameAdapter;
    List<LivingLotteryListBean.ItemsBean> lotteryNameList = new ArrayList<>();

    List<BaseFragment> fragmentList = new ArrayList<>();


    @Override
    public void onClickView(View view) {
        if (view == mBind.rlMain ){
            dismissAllowingStateLoss();
        } else if (view == mBind.tvCharge){
            RechargeActivity.startActivity(this.getContext());
        } else if (view == mBind.ivGame) {

        } else if (view == mBind.ivRefersh){
            getAsset();
        }
    }

    @Override
    public int onCreateLayoutId() {
        return R.layout.dialog_lottery;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen_2);
    }

    @Override
    public void initView(View view) {
        mBind=getViewDataBinding();
        mBind.setClick(this);


        lotteryNameAdapter = new LotteryNameAdapter(lotteryNameList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBind.rcLotteryName.setLayoutManager(layoutManager);
        mBind.rcLotteryName.setAdapter(lotteryNameAdapter);

        lotteryNameAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                changeLotteryHead(position,true);
            }
        });

        getData();
        getAsset();
    }

    private void setVp(){
        mBind.vp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        mBind.vp.setCurrentItem(0);
        mBind.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeLotteryHead(position, false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void changeLotteryHead(int position, boolean changeItem){
        for (int i = 0; i < lotteryNameList.size(); i ++) {
            if (lotteryNameList.get(i).isSelect()) {
                if (i == position) {
                    return;
                } else {
                    lotteryNameList.get(i).setSelect(false);
                    break;
                }
            }
        }
        lotteryNameList.get(position).setSelect(true);
        lotteryNameAdapter.notifyDataSetChanged();
        if (changeItem){
            mBind.vp.setCurrentItem(position);
        }
    }


    /**
     *  获取游戏列表
     */
    public void getData() {
        showLoadingDialog();
        Api_Living_Lottery.ins().getLivingGameList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                dismissLoadingDialog();
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                    LivingLotteryListBean bean = new Gson().fromJson(data, LivingLotteryListBean.class);
                    LogUtils.i(bean.getItems().size() + "");
                    if (bean.getItems() != null && bean.getItems().size() > 0) {
                        lotteryNameList.addAll(bean.getItems());
                        lotteryNameList.get(0).setSelect(true);
                        for(int i=0 ; i< lotteryNameList.size(); i++) {
                            String json = new Gson().toJson(lotteryNameList.get(i)).toString();
                            fragmentList.add(LotteryItemListFragment.newInstance(json));
                        }
                    }
                    lotteryNameAdapter.notifyDataSetChanged();
                    setVp();
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        });
    }

    private void getAsset(){
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        Api_Order.ins().getAssets(new JsonCallback<UserAssetsBean>() {
            @Override
            public void onSuccess(int code, String msg, UserAssetsBean data) {
                if (code == 0 && msg.equals("ok") || "success".equals(msg)) {
                     mBind.tvBalance.setText(data.getGold() + "");
                } else {
                    ToastUtils.showShort(msg);
                }
            }
        }, commonParams);
    }


}
