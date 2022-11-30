package com.live.fox.ui.lottery;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.live.fox.ConstantValue;
import com.live.fox.R;
import com.live.fox.adapter.BaseFragmentPagerAdapter;
import com.live.fox.common.JsonCallback;
import com.live.fox.entity.LivingLotteryListBean;
import com.live.fox.entity.UserAssetsBean;
import com.live.fox.server.Api_Living_Lottery;
import com.live.fox.server.Api_Order;
import com.live.fox.server.BaseApi;
import com.live.fox.base.BaseBindingDialogFragment;
import com.live.fox.base.BaseFragment;
import com.live.fox.databinding.DialogLotteryBinding;
import com.live.fox.ui.mine.RechargeActivity;
import com.live.fox.utils.LogUtils;
import com.live.fox.utils.SPUtils;
import com.live.fox.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LotteryDialog extends BaseBindingDialogFragment {

    DialogLotteryBinding mBind;

//    List<LivingLotteryListBean.ItemsBean> lotteryNameList = new ArrayList<>();

    List<BaseFragment> fragmentList = new ArrayList<>();
    private Animation rotate;

    public static LotteryDialog getInstance()
    {
        LotteryDialog lotteryDialog=new LotteryDialog();
        return lotteryDialog;
    }

    @Override
    public boolean onBackPress() {
        startAnimate(mBind.rllMain,false);
        return true;
    }

    @Override
    public void onClickView(View view) {
        if (view == mBind.rlMain ){
            startAnimate(mBind.rllMain,false);
        } else if (view == mBind.tvCharge){
            RechargeActivity.startActivity(this.getContext());
        } else if (view == mBind.ivGame) {

        } else if (view == mBind.ivRefersh){
            getAsset();

            if (rotate != null) {
                mBind.ivRefersh.startAnimation(rotate);
            } else {
                mBind.ivRefersh.setAnimation(rotate);
                mBind.ivRefersh.startAnimation(rotate);
            }
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

        String jsonStr= SPUtils.getInstance().getString(ConstantValue.gameCategoryTitles,"");
        if(!TextUtils.isEmpty(jsonStr))
        {
            LivingLotteryListBean bean = new Gson().fromJson(jsonStr, LivingLotteryListBean.class);
            if(bean.getItems()!=null)
            {
                setVp(bean.getItems());
            }
        }
        CacheData();
        getAsset();
        startAnimate(mBind.rllMain,true);

        rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
    }

    private void setVp(List<LivingLotteryListBean.ItemsBean> lotteryNameList){

        for(int i=0 ; i< lotteryNameList.size(); i++) {
            fragmentList.add(LotteryItemListFragment.newInstance(lotteryNameList.get(i)));
        }

        mBind.vp.setOffscreenPageLimit(fragmentList.size()-1);
        mBind.vp.setAdapter(new BaseFragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getFragment(int position) {
                return fragmentList.get(position);
            }

            @Override
            public String getTitle(int position) {
                return lotteryNameList.get(position).getName();
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }
        });

        mBind.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(lotteryNameList.size()>0)
        {
            mBind.vp.setCurrentItem(0);
        }
        mBind.tabLayout.setViewPager(mBind.vp);
        mBind.tabLayout.setGradient(0xffA800FF,0xffEA00FF);
    }

    /**
     *  获取游戏列表
     */
    public void CacheData() {
        Api_Living_Lottery.ins().getLivingGameList(new JsonCallback<String>() {
            @Override
            public void onSuccess(int code, String msg, String data) {
                if(isConditionOk())
                {
                    if (code == 0 ) {
                        SPUtils.getInstance().put(ConstantValue.gameCategoryTitles,data);
                        LivingLotteryListBean bean = new Gson().fromJson(data, LivingLotteryListBean.class);
                        if (bean.getItems() != null && bean.getItems().size() > 0) {
                            if(mBind.vp.getAdapter()==null || mBind.vp.getAdapter().getCount()==0)
                            {
                                setVp(bean.getItems());
                            }
                        }
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }
            }
        });
    }

    private void getAsset(){
        HashMap<String, Object> commonParams = BaseApi.getCommonParams();
        Api_Order.ins().getAssets(new JsonCallback<UserAssetsBean>() {
            @Override
            public void onSuccess(int code, String msg, UserAssetsBean data) {
                if(isConditionOk())
                {
                    if (code == 0) {
                        mBind.tvBalance.setText(data.getGold() + "");
                    } else {
                        ToastUtils.showShort(msg);
                    }
                }

            }
        }, commonParams);
    }


}
